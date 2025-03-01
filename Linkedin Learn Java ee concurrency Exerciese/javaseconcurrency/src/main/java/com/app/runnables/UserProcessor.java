package com.app.runnables;

import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import com.app.beans.User;
import com.app.dao.UserDao;

public class UserProcessor implements Callable<Integer>{

	private String userRecord;
	private UserDao userDao;
	
	
	
	public UserProcessor(String userRecord, UserDao userDao) {
		super();
		this.userRecord = userRecord;
		this.userDao = userDao;
	}



	@Override
	public Integer call() throws Exception {
		System.out.println(Thread.currentThread().getName() + " processing records for: " + userRecord);
		
		StringTokenizer tokenizer = new StringTokenizer(userRecord, ",");
		User user = null;
		int rows = 0;
		
		while(tokenizer.hasMoreTokens()) {
			user = new  User();
			user.setEmailAddress(tokenizer.nextToken());
			user.setName(tokenizer.nextToken());
			user.setId(Integer.valueOf(tokenizer.nextToken()));
			
			rows = userDao.saveUser(user);
		}
		return rows;
	}

}
