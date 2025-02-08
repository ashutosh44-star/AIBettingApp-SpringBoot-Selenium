package com.ashu.AIBet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ashu.AIBet.service.AviatorService;
import com.ashu.AIBet.service.AviatorService.MultiplierMonitor;
import com.ashu.AIBet.service.TelegramService;

import org.springframework.context.ConfigurableApplicationContext;
@SpringBootApplication
@EnableScheduling
public class AiAviatorBettingAppApplication {
	private static ConfigurableApplicationContext context;
	private static TelegramService service;
	private static AviatorService aviatorService;
	 public static void main(String[] args) {
		 	
	        context = SpringApplication.run(AiAviatorBettingAppApplication.class, args);
//	        new AviatorService().startAutomation();
//	        new TelegramService().connect();
	    }

	    public static void stopApplication() {
	        if (context != null) {
//	            new MultiplierMonitor().stopMonitoring();
	            context.close();
	            context = null;
	            System.out.println("Application context destroyed.");
	        }
	    }

	    public static String startApplication() {
	        if (context == null) {
	            context = SpringApplication.run(AiAviatorBettingAppApplication.class);
	            System.out.println("Application context initialized.");
	            return "Started";
	        } else {
	            System.out.println("Application is already running.");
	            return "Already Running";
	        }
	    }

}
