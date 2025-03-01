/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.rest;

import com.app.runnables.URLHealthProcessor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Ketkee Aryamane
 */
@Path("urlCheck")
public class URLHealthResource {
    
    @Resource(lookup = "java:comp/DefaultManagedScheduledExecutorService")
    private ManagedScheduledExecutorService scheduledExecutorService;
    
    @GET
    public String checkHealthOfApp(){
        String message ="";
        scheduledExecutorService.schedule(new URLHealthProcessor(), 3, TimeUnit.SECONDS);
        
        message="Health check initiated";
        return message;
        
    }
    
}
