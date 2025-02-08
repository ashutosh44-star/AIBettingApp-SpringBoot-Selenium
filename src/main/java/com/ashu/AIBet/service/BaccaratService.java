
package com.ashu.AIBet.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.hibernate.Hibernate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ashu.AIBet.Dao.Dao;
import com.ashu.AIBet.entity.OverallProfitLoss;

import jakarta.transaction.Transactional;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class BaccaratService {

//	private static final String URL = "https://1win.pro/casino/play/aviator";
	private WebDriver driver;
	int i = 0;
	@Autowired
	TelegramService telegramService;
String s="";
	public void startAutomation() {
		io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			if (driver == null || isSessionInactive(driver)) {
				// Initialize a new browser session
//		        Proxy proxy = new Proxy();
//		        proxy.setHttpProxy("localhost:3128").setSslProxy("localhost:3128");
				ChromeOptions options = new ChromeOptions();
//		        options.setProxy(proxy);
				options.addArguments("user-data-dir=C:/Users/dell/AppData/Local/Google/Chrome/User Data");
				options.addArguments("--profile-directory=Profile 4");
//				options.addArguments("--headless=new");
//				options.addArguments("--disable-gpu");
//				options.addArguments("--disable-extensions");
				options.addArguments("--disable-features=CrashRecovery");
				options.addArguments("--disable-infobars");
				options.addArguments("--disable-notifications");
//				options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
				options.addArguments("--enable-smooth-scrolling");
//				options.addArguments("--window-size=2560,1440");
				options.addArguments("--no-sandbox");
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("--disable-crash-reporter");
				options.addArguments("--disable-background-timer-throttling");
				options.addArguments("--disable-backgrounding-occluded-windows");
				options.addArguments("--disable-renderer-backgrounding");
				driver = new ChromeDriver(options);
//				driver.get("https://1win.pro/casino/play/fundist_1609939");
				driver.get("https://reddybook.win/casino-detail/99995/870360");
				System.out.println("New browser window opened.");
				i = 0;

			} else {
				i = 1;
				System.out.println("Existing browser window is already active.");
			}
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(5));
			if (i == 0) {
				driver.switchTo().defaultContent();
				DisconnectMonitor dis = new DisconnectMonitor(driver);
				SessionManager session = new SessionManager(driver, wait);
				try {
					WebElement loginButton = wait1.until(ExpectedConditions
							.presenceOfElementLocated(By.cssSelector("a.btn-login[type='button']:nth-of-type(2)")));
					if (loginButton.isDisplayed()) {
						loginButton.click();
						WebElement checkbox = wait.until(ExpectedConditions
								.elementToBeClickable(By.cssSelector("input.form-check-input[type='checkbox']")));
						checkbox.click();
						WebElement loginButton1 = wait.until(ExpectedConditions.elementToBeClickable(
								By.cssSelector("button.btn.button-login.btn-login[type='submit']")));
						loginButton1.click();
						wait.until(ExpectedConditions.visibilityOfElementLocated(
								By.cssSelector("a[href='/home'][routerlinkactive='nmm-active']")));
						TimeUnit.SECONDS.sleep(2);
						driver.get("https://reddybook.win/casino-detail/99995/870360");
					}
				} catch (Exception e) {

				}
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("casino-link")));
//				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//						By.xpath("//iframe[contains(@src, 'https://live.wirebankers.com/frontend/evo/r2/')]")));
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
						By.xpath("//iframe[contains(@src, 'https://tmmd.evo-games.com/frontend/evo/r2/')]")));
//				WebElement Lobby = wait.until(
//						ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-role='lobby-button']//span[text()='Lobby']")));
//				Lobby.click();
				WebElement closeButton = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-role='close-button']")));
				closeButton.click();
				System.out.println("Lobby Clicked..!");
				WebElement Faviourite = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.cssSelector("button[data-role='favourites-button'] span[data-role='typography']")));
				Faviourite.click();
				ActiveEvo(wait);
				session.startSession();
				dis.startMonitoring();
				telegramService.connect();

			}
			driver.switchTo().defaultContent();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public class SessionManager {
		private WebDriver driver;
		private WebDriverWait wait;
		private Thread sessionThread;
		private volatile boolean isSessionActive = false;

		public SessionManager(WebDriver driver, WebDriverWait wait) {
			this.driver = driver;
			this.wait = wait;
		}

		public synchronized void startSession() {
			if (!isSessionActive) {
				isSessionActive = true;
				sessionThread = new Thread(this::keepSessionActive);
				sessionThread.start();
				System.out.println("Session monitoring started.");
			} else {
				System.out.println("Session monitoring is already active.");
			}
		}

		public synchronized void stopSession() {
			if (isSessionActive) {
				isSessionActive = false; // Mark session as inactive
				if (sessionThread != null && sessionThread.isAlive()) {
					try {
						sessionThread.interrupt(); // Interrupt the session thread
						sessionThread.join(); // Wait for the thread to finish
					} catch (InterruptedException e) {
						System.err.println("Failed to stop session thread: " + e.getMessage());
					}
				}
				System.out.println("Session monitoring stopped.");
			} else {
				System.out.println("No active session to stop.");
			}
		}

		private void keepSessionActive() {
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));

			while (isSessionActive) {
				try {
					driver.switchTo().defaultContent();
					System.out.println("Attempt to keep session alive by simulating activity.");

					wait1.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("casino-link")));
					wait1.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
							By.xpath("//iframe[contains(@src, 'https://tmmd.evo-games.com/frontend/evo/r2/')]")));

					WebElement gamesButton = wait1.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//button[@data-role='games-button']//span[text()='Games']")));
					gamesButton.click();

					WebElement favouriteButton = wait1.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//button[@data-role='favourites-button']//span[text()='Favourites']")));
					favouriteButton.click();

					wait1.until(ExpectedConditions.presenceOfElementLocated(By.className("GridList--d4aee")));

					List<WebElement> gridItems = driver.findElements(By.cssSelector("li[data-role='grid-list-item']"));
					int playersTab1 = 0;
					int playersTab2 = 0;

					if (gridItems.size() >= 2) {
						// Tab 1
						String playersText1 = gridItems.get(0)
								.findElement(By.cssSelector("span[data-role='table-players']")).getText();
						playersTab1 = Integer.parseInt(playersText1);

						// Tab 2
						String playersText2 = gridItems.get(1)
								.findElement(By.cssSelector("span[data-role='table-players']")).getText();
						playersTab2 = Integer.parseInt(playersText2);
					}

					System.out.println("Number of players in Tab Lightning: " + playersTab1);
					System.out.println("Number of players in Tab Golden: " + playersTab2);

					WebElement mainElement = wait.until(
							ExpectedConditions.visibilityOfElementLocated(By.cssSelector("main.InnerContent--56377")));
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("canvas")));
					File mainScreenshot = mainElement.getScreenshotAs(OutputType.FILE);
					Path mainDestination = Path.of("D:\\Baccarat\\main_screenshot_with_canvas.png"); // Update the path
																										// as needed

					if (!mainDestination.getParent().toFile().exists()) {
						Files.createDirectories(mainDestination.getParent());
					}
					Files.copy(mainScreenshot.toPath(), mainDestination, StandardCopyOption.REPLACE_EXISTING);
					File inputFile = new File("D:\\Baccarat\\main_screenshot_with_canvas.png");
					BufferedImage originalImage = ImageIO.read(inputFile);
					// Define the cropping area (adjust coordinates and size as needed)
					int cropX = 40; // Starting X position
					int cropY = 50; // Starting Y position
					int cropWidth = 700; // Width of cropped area
					int cropHeight = 250; // Height of cropped area

					BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropWidth, cropHeight);

					File outputFile = new File("D:\\Baccarat\\cropped_image.png");
					ImageIO.write(croppedImage, "png", outputFile);
					System.out.println("Cropped image saved at: " + outputFile.getAbsolutePath());
					System.out.println("Main element screenshot saved at: " + mainDestination);

					if (playersTab1 > 400 && playersTab2 > 400) {
						telegramService.sendMessageToGroup(
								"Session is Running in Lightning Baccarat and Golden Baccarat...! Number of players: "
										+ "Lightning: " + playersTab1 + ", Golden: " + playersTab2);
						telegramService.sendPhotoToGroup(outputFile.getAbsolutePath());
					} else if (playersTab1 > 400) {
						telegramService.sendMessageToGroup(
								"Session is Running in Lightning Baccarat...! Number of players playing Lightning Baccarat: "
										+ playersTab1);
						telegramService.sendPhotoToGroup(outputFile.getAbsolutePath());
					} else if (playersTab2 > 400) {
						telegramService.sendMessageToGroup(
								"Session is Running in Golden Baccarat...! Number of players playing Golden Baccarat: "
										+ playersTab2);
						telegramService.sendPhotoToGroup(outputFile.getAbsolutePath());
					}

					Thread.sleep(60000); // Sleep for 60 seconds before next iteration
				} catch (InterruptedException e) {
					System.out.println("Session thread interrupted.");
				} catch (Exception e) {
//	                System.err.println("Error during session monitoring: " + e.getMessage());
				}
			}
		}
	}

	public void ActiveEvo(WebDriverWait wait) {

		Thread refreshThread = new Thread(() -> {
			while (true) {
				try {
					System.out.println("Calling Refresh....");
					driver.switchTo().defaultContent();
					driver.get("https://reddybook.win/casino-detail/99995/870360");
					refreshDriver(wait);
					Thread.sleep(600000); // 60000ms = 1 minute
				} catch (InterruptedException e) {
					System.out.println("Refresh thread interrupted.");
				} catch (Exception e) {
//                e.printStackTrace();
				}
			}
		});
		refreshThread.start();
	}

	public void refreshDriver(WebDriverWait wait) {
		try {
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement closeButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-role='close-button']")));
			closeButton.click();
			System.out.println("Close Button Clicked...Refresh Done...!");
			WebElement Faviourite = wait1.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//button[@data-role='favourites-button']//span[text()='Favourites']")));
			Faviourite.click();
		} catch (Exception e) {

		}
	}

	private boolean isSessionInactive(WebDriver driver) {
		try {
			driver.getTitle();
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	public class DisconnectMonitor {
		private WebDriver driver;
		private WebDriverWait wait;
		private volatile boolean isMonitoring = true;
		private Thread monitorThread;

		public DisconnectMonitor(WebDriver driver) {
			this.driver = driver;
			this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		}

		public synchronized void startMonitoring() {
			stopMonitoring();
			isMonitoring = true;
			monitorThread = new Thread(this::monitor);
			monitorThread.start();
		}

		public synchronized void stopMonitoring() {
			if (monitorThread != null && monitorThread.isAlive()) {
				isMonitoring = false;
				System.out.println("Stopping monitoring...");
				monitorThread.interrupt();
				try {
					monitorThread.join();
					System.out.println("Disconnect Monitoring thread stopped successfully.");
				} catch (InterruptedException e) {
					System.err.println("Failed to stop monitoring thread: " + e.getMessage());
				}
			}
		}

		private void monitor() {
			System.out.println("Disconnect Monitoring has been started.");
//			long startTime = System.currentTimeMillis();
//			long duration = 24 * 60 * 60 * 1000; // 24 Hrs

			try {
				By login = By.cssSelector("a.btn-login[type='button']:nth-of-type(2)");

				while (isMonitoring) {
					if (Thread.interrupted()) {
						System.out.println("Monitoring thread interrupted. Exiting loop...");
						return;
					}

					try {
						checkAndRefreshIfNecessary(login);
					} catch (Exception e) {
						System.out.println("Monitoring error: " + e.getMessage());
					}
					try {
						Thread.sleep(90000);
					} catch (InterruptedException e) {
						System.out.println("Thread interrupted while sleeping. Exiting monitoring...");
						Thread.currentThread().interrupt();
						return;
					}
				}

				System.out.println("Monitoring loop ended gracefully.");
			} catch (Exception e) {
				System.err.println("Monitoring setup failed: " + e.getMessage());
			}
		}

		private void checkAndRefreshIfNecessary(By login) {
			try {

				WebElement element1 = wait.until(ExpectedConditions.presenceOfElementLocated(login));
				if (element1.isDisplayed()) {
					telegramService.sendMessageToGroup("Login button is displayed....!");
					System.out.println("Login button is displayed....!");
					element1.click();
					WebElement checkbox = wait.until(ExpectedConditions
							.elementToBeClickable(By.cssSelector("input.form-check-input[type='checkbox']")));
					checkbox.click();
					WebElement loginButton = wait.until(ExpectedConditions
							.elementToBeClickable(By.cssSelector("button.btn.button-login.btn-login[type='submit']")));
					loginButton.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.cssSelector("a[href='/home'][routerlinkactive='nmm-active']")));
					TimeUnit.SECONDS.sleep(2);
					driver.get("https://reddybook.win/casino-detail/99995/870360");
					TimeUnit.SECONDS.sleep(5);
					driver.switchTo().defaultContent();
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("casino-link")));
//						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//								By.xpath("//iframe[contains(@src, 'https://live.wirebankers.com/frontend/evo/r2/')]")));
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
							By.xpath("//iframe[contains(@src, 'https://tmmd.evo-games.com/frontend/evo/r2/')]")));
//			            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[data-role='favourites-button'] span[data-role='typography']")));
					WebElement closeButton = wait.until(
							ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-role='close-button']")));
					closeButton.click();
					WebElement Faviourite = wait.until(ExpectedConditions.presenceOfElementLocated(
							By.cssSelector("button[data-role='favourites-button'] span[data-role='typography']")));
					Faviourite.click();
					telegramService.sendMessageToGroup("Re-Login has been done....!");
				}
			} catch (Exception e) {
				System.out.println("Element not found. Monitoring continues...");
			}
		}
	}

}
