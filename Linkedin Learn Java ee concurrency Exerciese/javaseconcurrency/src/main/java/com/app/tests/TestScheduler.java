package com.app.tests;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.app.runnables.CleaningScheduler;

public class TestScheduler {

	public static void main(String[] args) {
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		//ses.schedule(new CleaningScheduler(), 5, TimeUnit.SECONDS);
		
		//ses.scheduleAtFixedRate(new CleaningScheduler(), 5, 4, TimeUnit.SECONDS);
		
		ses.scheduleWithFixedDelay(new CleaningScheduler(), 5, 4, TimeUnit.SECONDS);
		
	}
}
