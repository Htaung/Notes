/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.runnables;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ketkee Aryamane
 */
public class URLHealthProcessor implements Runnable{
    
    private static final String urlName="http://www.google.com";

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" checking the health of the application"); 
       String statusOfApp="";
        try {
            
            URL url = new URL(urlName);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            
            if(responseCode==200){
                statusOfApp="Yes, it is working!";
            }
            else{
                statusOfApp="Sorry! Something went wrong";
            }
            
            System.out.println("Status of the app: "+statusOfApp);
        } catch (MalformedURLException ex) {
            Logger.getLogger(URLHealthProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(URLHealthProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
