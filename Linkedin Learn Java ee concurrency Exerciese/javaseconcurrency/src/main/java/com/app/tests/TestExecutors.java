package com.app.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.app.dao.UserDao;
import com.app.runnables.UserProcessor;

public class TestExecutors {

	public static void main(String[] args) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		
		List<String> users = getUserFromFile("D:\\Learning\\Linkedin Learn\\Ex_Files_Java_EE_Concurrency\\Exercise Files\\Chapter1\\01_02\\end\\javaseconcurrency\\new_users.txt");
		UserDao dao = new UserDao();
		for(String user:users) {
			es.submit(new UserProcessor(user, dao));
			/*Future<Integer> future = es.submit(new UserProcessor(user, dao));
			 * try {
				es.submit(new UserProcessor(user, dao));
				System.out.println("result of the operation: " + future.get());
			} catch (InterruptedException e) {
				Logger.getLogger(TestExecutors.class.getName()).log(Level.SEVERE, null, e);
			} catch (ExecutionException e) {
				Logger.getLogger(TestExecutors.class.getName()).log(Level.SEVERE, null, e);
			}*/
		}
		
		es.shutdown();
		System.out.println("MAIN EXECUTION OVER");
		
	}
	
	public static List<String> getUserFromFile(String fileName) {
		List<String> users = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))){
			String line = null;
			
			while((line = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() + " reading line: " + line);
				users.add(line);
			} 
			
		} catch (FileNotFoundException e) {
			Logger.getLogger(TestExecutors.class.getName()).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			Logger.getLogger(TestExecutors.class.getName()).log(Level.SEVERE, null, e);
		}
		
		return users;
	}
}
