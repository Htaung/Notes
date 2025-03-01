package com.mdrsolutions.SpringJmsExample.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.mdrsolutions.SpringJmsExample.listener.BookOrderProcessingMessageListener;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;


@EnableJms
@Configuration
public class JmsConfig implements JmsListenerConfigurer{

	
	@Bean
	public BookOrderProcessingMessageListener jmsMessageListener() {
		BookOrderProcessingMessageListener listener = new BookOrderProcessingMessageListener();
		return listener;
	}
	
    //@Bean
    public MessageConverter jacksonJmsMessageConverter(){

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public MessageConverter xmlMarshallingMessageConverter(){
        MarshallingMessageConverter converter = new MarshallingMessageConverter(xmlMarshaller());
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }
    @Bean
    public XStreamMarshaller xmlMarshaller(){
        return new XStreamMarshaller();
    }
    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");
        return factory;
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setMessageConverter(xmlMarshallingMessageConverter());
        
        
        return factory;
    }


	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		SimpleJmsListenerEndpoint endPoint = new SimpleJmsListenerEndpoint();
		endPoint.setMessageListener(jmsMessageListener());
		endPoint.setDestination("book.order.processed.queue");
		endPoint.setId("book-order-processed-queue");
		endPoint.setSubscription("my-subscription");
		endPoint.setConcurrency("1");
		
		registrar.setContainerFactory(jmsListenerContainerFactory());
		registrar.registerEndpoint(endPoint, jmsListenerContainerFactory());
	}
}
