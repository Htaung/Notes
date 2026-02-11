package com.saung.wikimedia;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.HttpConnectStrategy;
import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import jdk.jfr.Event;
import okhttp3.Headers;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {
    public static void main(String[] args) {
        String groupId = "my-java-application";
        String topic = "wikimedia.recentchange";

        String bootStrapServer =  "localhost:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //set high throughput producer config
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.name);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);



        BackgroundEventHandler backgroundEventHandler = new WikimediaChangeHandler(producer, topic);

        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        String[] rawHeaders = {
                "User-Agent", "Kafka-Learning-App/1.0",
                "Accept", "text/event-stream",
        };
        Headers headers = Headers.of(rawHeaders);

        EventSource.Builder eventSourceBuilder = new EventSource.Builder(
                ConnectStrategy.http(URI.create(url))
                        .headers(headers)
                        .connectTimeout(10, TimeUnit.SECONDS)
        );

        BackgroundEventSource.Builder backgroundEventSourceBuilder = new BackgroundEventSource.Builder(backgroundEventHandler, eventSourceBuilder);
        BackgroundEventSource backgroundEventSource = backgroundEventSourceBuilder.build();

        //start producer in another thread
        backgroundEventSource.start();

        //produce for 10 mins and block the program until then
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
