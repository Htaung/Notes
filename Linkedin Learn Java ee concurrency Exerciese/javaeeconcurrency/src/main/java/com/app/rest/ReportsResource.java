/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.rest;

import com.app.beans.BankAccount;
import com.app.dao.BankAccountDao;
import com.app.runnables.ReportsProcessor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Ketkee Aryamane
 */
@Path("reports")
public class ReportsResource {
    
    private BankAccountDao dao;
    
     @Resource(lookup = "java:comp/DefaultManagedExecutorService")
     private ManagedExecutorService service;

    public ReportsResource() {
        
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setUser("root");
            dataSource.setPassword("root");
            dao = new BankAccountDao(dataSource);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ReportsResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @GET
    public String generateReports(){
        System.out.println("service object from JNDI look up: "+service);
        List<BankAccount> accounts = dao.getAllBankAccounts();
        for(BankAccount account: accounts){
            try {
                Future<Boolean> future = service.submit(new ReportsProcessor(account, dao));
                System.out.println("report generated? "+future.get());
            } catch (InterruptedException ex) {
                Logger.getLogger(ReportsResource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ReportsResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "Report generation tasks submitted!";
    }
     
     


}
