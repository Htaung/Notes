package com.saung.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);

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

        //Subscribe to topic
        consumer.subscribe(Arrays.asList(topic));

        //poll for data
        while(true) {
            log.info("Polling");

            //da
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: {} | Value: {}", record.key(), record.value());
                log.info("Partition: {} | Offset: {}", record.partition(), record.offset());

            }
        }

    }
}
