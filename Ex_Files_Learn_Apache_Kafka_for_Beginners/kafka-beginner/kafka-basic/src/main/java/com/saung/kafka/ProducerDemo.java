package com.saung.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);

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

        //Create producer record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "helloworld");

        //Send Data
        producer.send(producerRecord);

        //flush and close producer
        //tell the producer to send all the data and block until done - synchronous operation
        producer.flush();
        producer.close();


    }
}
