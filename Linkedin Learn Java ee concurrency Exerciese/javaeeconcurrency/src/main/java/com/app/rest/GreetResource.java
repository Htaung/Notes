/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Ketkee Aryamane
 */
@Path("greetUser")
public class GreetResource {
    @GET
    public String greetUser(){
        return "Java EE concurrency starts!";
    }
    
}
