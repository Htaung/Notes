package com.mdrsolutions.SpringJmsExample.service.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.mdrsolutions.SpringJmsExample.pojos.BookOrder;

@Service
public class BookStoreOrderService {

	private static final String BOOK_ORDER_QUEUE = "book.order.queue";
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void send(BookOrder bookOrder) {
		jmsTemplate.convertAndSend(BOOK_ORDER_QUEUE, bookOrder);
	}
			
			
}

