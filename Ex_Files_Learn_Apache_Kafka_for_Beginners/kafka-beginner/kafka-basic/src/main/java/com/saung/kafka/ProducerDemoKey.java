package com.saung.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKey {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKey.class);

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


        //Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for(int j=0; j<2; j++){
            // there will be showing same partition, coz of sticky partitioner -> batch
            // 	partitioner.class = null that means it was using sticky partitioner
            for (int i = 0; i < 10; i++) {

                String topic = "demo_java";
                String key = "id_" + i;
                String value= "hello world " + i;

                //Create producer record
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key,value + i);

                //Send Data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        //excecute everytime a record successfully sent or exception is thrown
                       // log.info("recordMetadata {}", recordMetadata);

                        if (e == null) {
                            log.info("Key : {} | Partition:{} ", key, recordMetadata.partition());
                        } else {
                            log.error("Error: ", e);
                        }
                    }
                });

            }
        }



        //flush and close producer
        //tell the producer to send all the data and block until done - synchronous operation
        producer.flush();
        producer.close();


    }
}
