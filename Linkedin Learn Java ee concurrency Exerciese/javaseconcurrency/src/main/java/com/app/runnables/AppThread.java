package com.app.runnables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppThread extends Thread{

	@Override
	public void run() {
		String path = "D:\\Learning\\Linkedin Learn\\Ex_Files_Java_EE_Concurrency\\Exercise Files\\Chapter2\\02_03\\end\\javaseconcurrency\\sample.txt";
		
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(path)))){
			String line = null;
			
			while((line = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() + " reading line: " + line);
			} 
			
		} catch (FileNotFoundException e) {
			Logger.getLogger(AppThread.class.getName()).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			Logger.getLogger(AppThread.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}
}
