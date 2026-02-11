package com.saung.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoWithShutDown {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithShutDown.class);

    public static void main(String[] args) {
        log.info("Consumer");
        String groupId = "my-java-application";
        String topic = "demo_java";

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
        properties.setProperty("auto.offset.reset", "earliest");


        //Create Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        final Thread mainThread = Thread.currentThread();

        //adding shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run(){
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup");

                //trigger exception in this lineNo.69 of code ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                consumer.wakeup();


                //join the main thread to allow the execution of the code in the main thread
                try {
                    //to wait for all the code in this page to be completed and have no more execution
                    mainThread.join();
                } catch (InterruptedException e) {
                    log.error("InterruptedException: ", e);
                }
            }
        });

        try{
            //Subscribe to topic
            consumer.subscribe(Arrays.asList(topic));

            //poll for data
            while(true) {
               // log.info("Polling");

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("Key: {} | Value: {}", record.key(), record.value());
                    log.info("Partition: {} | Offset: {}", record.partition(), record.offset());

                }
            }
        }catch (WakeupException e){
            log.error("Consumer is starting to shut down WakeupException: ", e);
        }catch (Exception e){
            log.error("Unexpected Exception in the consumer: ", e);
        }finally {
            //this will also commit offsets
            consumer.close();
            log.info("Consumer is now gracefully shutdown");
        }


    }
}
