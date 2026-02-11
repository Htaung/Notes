package com.saung.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallBack {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

    /**
     * Create Producer properties
     * Create Producer
     * Send Data
     * flush and close the producer
     *
     * @param args
     */
    public static void main(String[] args) {
        log.info("Producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");

        //Set Producer Propery
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //for demostration
        properties.setProperty("batch.size", "400");
        properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        //Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // there will be showing same partition, coz of sticky partitioner -> batch
        // 	partitioner.class = null that means it was using sticky partitioner
        for(int j=0; j< 10; j++){
            for(int i=0; i< 30; i++){
                //Create producer record
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "helloworld" + i);

                //Send Data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        //excecute everytime a record successfully sent or exception is thrown
                        log.info("recordMetadata {}", recordMetadata);

                        if(e == null) {
                            log.info("Success Topic:{}", recordMetadata.topic());
                            log.info("Partition:{}", recordMetadata.partition());
                            log.info("Offset:{}", recordMetadata.offset());
                            log.info("TimeStamp:{}", recordMetadata.timestamp());
                        }else{
                            log.error("Error:{}", e);
                        }
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error("Error:{}", e);
                }
            }
        }





        //flush and close producer
        //tell the producer to send all the data and block until done - synchronous operation
        producer.flush();
        producer.close();


    }
}
