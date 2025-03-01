/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.rest;

import com.app.runnables.LoggingProcessor;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Ketkee Aryamane
 */
@Path("logging")
public class LoggingResource {
    
    
    @Resource(lookup = "java:comp/DefaultManagedThreadFactory")
    private ManagedThreadFactory threadFactory;
    
    @GET
    public String logData(){
        Thread thread = threadFactory.newThread(new LoggingProcessor());
        thread.setName("Logging-Thread");
        thread.start();
               
        ExecutorService service = getApplicationPool();
        
        for(int i=0;i<7;i++){
            service.submit(new LoggingProcessor());
        }
        
        return "Logging thread doing its job, check console!";
    }
    
    
    public ExecutorService getApplicationPool(){
        ExecutorService service = new ThreadPoolExecutor(3, 10, 5, TimeUnit.SECONDS, new ArrayBlockingQueue(2), threadFactory);
        return service;
    }
    
 
}
