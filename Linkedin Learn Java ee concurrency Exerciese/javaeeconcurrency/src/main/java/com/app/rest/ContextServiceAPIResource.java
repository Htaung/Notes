/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.rest;

import com.app.runnables.ContextServiceRunnable;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ContextService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Ketkee Aryamane
 */
@Path("contextservice")
public class ContextServiceAPIResource {
    
    @Resource(lookup = "java:comp/DefaultContextService")
    private ContextService contextService;
    
    @GET
    public String accessSecurityInfo(){
        
        ContextServiceRunnable runnable = new ContextServiceRunnable();
      
        
        Runnable proxy = contextService.createContextualProxy(runnable, Runnable.class);
        Thread thread = new Thread(proxy);
        thread.start();
        
        return "Context capturing information";

    }    
}
