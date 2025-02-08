//package com.ashu.AIBet.SingletonConfig;
//
//import java.io.IOException;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DriverSingletonInstance {
//	private WebDriver driver;
//	
//	@Bean
//	public WebDriver WebDriver() {
//		if(driver==null) {
//			io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
//			try {
//				Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
//			} catch (IOException e) {
//
//			}
//			ChromeOptions options = new ChromeOptions();
//			options.addArguments("user-data-dir=C:/Users/dell/AppData/Local/Google/Chrome/User Data");
//			options.addArguments("--profile-directory=Profile 4");
////			options.addArguments("--headless=new");
////			options.addArguments("--disable-gpu");
////			options.addArguments("--disable-extensions");
//			options.addArguments("--disable-features=CrashRecovery");
//			options.addArguments("--disable-infobars");
//			options.addArguments("--disable-notifications");
//			options.addArguments("--enable-smooth-scrolling");
//			options.addArguments("--no-sandbox");
//			options.addArguments("--ignore-certificate-errors");
//			options.addArguments("--disable-crash-reporter");
//			options.addArguments("--disable-background-timer-throttling");
//			options.addArguments("--disable-backgrounding-occluded-windows");
//			options.addArguments("--disable-renderer-backgrounding");
//			driver = new ChromeDriver(options);
//			driver.get("https://reddybook.win/casino-detail/99995/870360");
//		}
//		return driver;
//	}
//
//}
