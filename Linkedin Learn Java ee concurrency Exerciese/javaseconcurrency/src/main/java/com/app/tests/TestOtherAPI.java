package com.app.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.app.dao.DBConnection;
import com.app.runnables.LoggingProcessor;

public class TestOtherAPI {

	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(2);
		List<Callable<Boolean>> callables = new ArrayList<>();
		
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		callables.add(new LoggingProcessor());
		
		try {
			List<Future<Boolean>> futures = es.invokeAll(callables);
			
			for(Future<Boolean> future:futures) {
				try {
					System.out.println("future result: " + future.get());
				} catch (ExecutionException e) {
					Logger.getLogger(TestOtherAPI.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			
		} catch (InterruptedException e) {
			Logger.getLogger(TestOtherAPI.class.getName()).log(Level.SEVERE, null, e);
		}
		
		try {
			System.out.println("invokeAny: " + es.invokeAny(callables));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		es.shutdown();
		try {
			System.out.println("service shut down ? awaitTermination: " + es.awaitTermination(30, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			Logger.getLogger(TestOtherAPI.class.getName()).log(Level.SEVERE, null, e);
			es.shutdownNow();
		}
	}
	
	
}
