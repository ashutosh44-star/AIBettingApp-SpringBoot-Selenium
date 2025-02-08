//package com.ashu.AIBet.service;
//
//import java.time.Duration;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.NoSuchElementException;
//import org.openqa.selenium.StaleElementReferenceException;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.FluentWait;
//import org.openqa.selenium.support.ui.Wait;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ashu.AIBet.Dao.Dao;
//import com.ashu.AIBet.entity.OverallProfitLoss;
//
//import jakarta.transaction.Transactional;
//
//@Component
//public class ProfitChecker {
//	
//	@Autowired
//	private Dao dao;
//	@Transactional
//	public OverallProfitLoss fetchProfitLoss(Integer id) {
//	    return dao.findWithInitializedFields(id);
//	}
//	@Transactional
//    public void saveOrUpdateProfitLoss(double totalMoney) {
//        // Retrieve the existing record by ID
//        OverallProfitLoss profitLoss = dao.findById(1).orElse(new OverallProfitLoss());
//        profitLoss.setId(1); // Ensure the ID is set for updates
//        profitLoss.setTotalMoney(totalMoney); // Update the total money
//        dao.save(profitLoss); // Save or update the record
//    }
//    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//
//    public  void startProfitMonitoring(WebDriverWait wait,WebDriver driver) {
//        scheduler.scheduleAtFixedRate(() -> {
//            try {driver.switchTo().defaultContent();
//            	WebDriverWait wait1=new WebDriverWait(driver, Duration.ofSeconds(1000));
//            	
//            	
//                System.out.println("Checking total profit of the day....");
//                OverallProfitLoss currentData = fetchProfitLoss(1);
//
//                if (currentData != null) {
//                    double initialAmount = currentData.getTotalMoney();
//                    System.out.println("Initial amount === " + initialAmount);
//                    
////                    		wait1.until(ExpectedConditions
////                            .frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));
////
////                    		wait1.until(ExpectedConditions
////                            .frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));
//                    wait.until(ExpectedConditions
//	                        .frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));
//
//                    // Step 3: Wait for the total amount element
//                    FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
//                            .withTimeout(Duration.ofSeconds(50))        // Total timeout
//                            .pollingEvery(Duration.ofMillis(100))       // Polling interval
//                            .ignoring(NoSuchElementException.class)     // Ignore missing elements
//                            .ignoring(StaleElementReferenceException.class); // Handle stale elements
//
//                    WebElement totalAmountElement = fluentWait.until(driver1 -> {
//                        WebElement element = driver1.findElement(By.cssSelector(".amount.font-weight-bold"));
//                        return element.isDisplayed() ? element : null;
//                    });
//                    String currentAmountText = totalAmountElement.getText().replaceAll("[^\\d.]", "");
//                    double currentAmount = Double.parseDouble(currentAmountText);
//
//                    double difference = currentAmount - initialAmount;
//                    System.out.println("Initial Amount: " + initialAmount + ", Current Amount: " + currentAmount
//                            + ", Difference: " + difference);
//                    if (difference >= 5000) {
//                        System.out.println("Profit increased by 5000 or more. Stopping operations.");
//                        currentData.setOverAllProfitMoney(difference);
//                        dao.save(currentData);
//
//                        stopOperations();
//                    } else {
//                        System.out.println("Daily profit goal has not been achieved yet...continue betting...");
//                    }
//                } else {
//                    System.out.println("Initial data not found in the database.");
//                }
//                driver.switchTo().defaultContent();
//            } catch (Exception e) {
////                e.printStackTrace();
//                System.err.println("Error monitoring profit: " + e.getMessage());
//            }
//        }, 0, 2, TimeUnit.MINUTES); // Initial delay = 0, Repeat every 10 minutes
//    }
//
//    private void stopOperations() {
//        try {
//            scheduler.shutdownNow();
//            System.exit(0);
//        } catch (Exception e) {
//            System.err.println("Error stopping operations: " + e.getMessage());
//        }
//    }
//
//}
