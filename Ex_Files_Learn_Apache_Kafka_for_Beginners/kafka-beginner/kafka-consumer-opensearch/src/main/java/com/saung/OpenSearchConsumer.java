package com.saung;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.core.xcontent.XContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class OpenSearchConsumer {
    static Logger logger = LoggerFactory.getLogger(OpenSearchConsumer.class);

    public static void main(String[] args) throws IOException {
        //Create Opensearch Client
        RestHighLevelClient openSearchClient = createOpenSearchClient();

        //Create kafka openSearchClient
        KafkaConsumer<String, String> consumer = createKafkaConsumer();

        final Thread mainThread = Thread.currentThread();

        //adding shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run(){
                logger.info("Detected a shutdown, let's exit by calling consumer.wakeup");

                //trigger exception in this lineNo.69 of code ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                consumer.wakeup();


                //join the main thread to allow the execution of the code in the main thread
                try {
                    //to wait for all the code in this page to be completed and have no more execution
                    mainThread.join();
                } catch (InterruptedException e) {
                    logger.error("InterruptedException: ", e);
                }
            }
        });

        //create index  if it doesn't exists
        CreateIndexRequest request = new CreateIndexRequest("wikimedia");
        try (openSearchClient; consumer) {
            boolean indexExists = openSearchClient.indices().exists(new GetIndexRequest("wikimedia"), RequestOptions.DEFAULT);

            if (!indexExists) {
                openSearchClient.indices().create(request, RequestOptions.DEFAULT);
                logger.info("wikimedia created");
            } else {
                logger.info("wikimedia already exists");
            }

            //subscribe
            consumer.subscribe(Collections.singleton("wikimedia.recentchange"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                int recordCount = records.count();
                logger.info("Received {} records", recordCount);

                BulkRequest bulkRequest = new BulkRequest();


                for (ConsumerRecord<String, String> record : records) {
                    //Send the record into opensearch

                    //to avoid duplicate msg
                    //1. define and ID using kafka record coordinates
                    //String id = record.topic() + "_" + record.partition() + "_" + record.offset();

                    //2. use data id or database id
                    String appId = extractId(record.value());

                    // open search see same id in their index, it updated in index not creating new
                    try{
                        IndexRequest indexRequest = new IndexRequest("wikimedia").source(record.value(), XContentType.JSON).id(appId);
                        //IndexResponse response = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);
                        bulkRequest.add(indexRequest);

                        //logger.info(response.getId());
                    }catch (Exception e){
                        logger.error("Exception: ", e);
                        logger.info("appId: {}", appId);
                        logger.info("record: {}", record.value());
                    }
                }



                if(bulkRequest.numberOfActions() > 0){
                    BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                    logger.info("Inserted {} records ", bulkResponse.getItems().length);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    //commit offset after batch processing
                    consumer.commitSync();
                    logger.info("Offset have been commited");

                }


            }

        }catch (WakeupException e){
            logger.error("Consumer is starting to shut down WakeupException: ", e);
        }catch (Exception e){
            logger.error("Unexpected Exception in the consumer: ", e);
        }finally {
            //this will also commit offsets
            consumer.close();
            openSearchClient.close();
            logger.info("Consumer is now gracefully shutdown");
        }


    }

    private static String extractId(String json) {
        return JsonParser.parseString(json).getAsJsonObject().get("meta").getAsJsonObject().get("id").getAsString();
    }

    private static KafkaConsumer<String, String> createKafkaConsumer() {
        String groupId = "consumer-opensearch-demo";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");

        //Set Consumer Propery
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);

        /**
         * none - if consumer group not exists failed, have to create group first
         * earliest - read from beginning of topic
         * latest - recent msg
         */
        properties.setProperty("auto.offset.reset", "latest");

        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");


        //Create Consumer
        return new KafkaConsumer<>(properties);
    }

    public static RestHighLevelClient createOpenSearchClient() {
        String url = "http://localhost:9200";

        //we build a URI from
        RestHighLevelClient client = null;
        URI connectionURI = URI.create(url);

        //extract login info if exists
        String userInfo = connectionURI.getUserInfo();

        if (userInfo == null) {
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(connectionURI.getHost(), connectionURI.getPort(), connectionURI.getScheme())));
        } else {
            String[] auth = userInfo.split(":");
            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            /*client = new RestHighLevelClient(RestClient.builder(new HttpHost(connectionURI.getHost(), connectionURI.getPort(), connectionURI.getScheme()))
                    .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(new CredentialsProvider()))
                    );*/
        }

        return client;
    }
}
