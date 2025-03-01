/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.runnables;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ketkee Aryamane
 */
public class LoggingProcessor implements Runnable{

    @Override
    public void run() {
        System.out.println("Thread :"+Thread.currentThread().getName());
        Logger.getLogger(LoggingProcessor.class.getName()).log(Level.INFO, "Logging data for logging resource");
    }
    
    
    
}
