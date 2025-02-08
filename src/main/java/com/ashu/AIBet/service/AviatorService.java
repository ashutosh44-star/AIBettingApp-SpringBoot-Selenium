package com.ashu.AIBet.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
public class AviatorService {
	private static final ITesseract tesseract = new Tesseract();

	static {
		// Configure Tesseract instance once
		tesseract.setDatapath("C:/tesseract/tessdata");
		tesseract.setLanguage("eng");
		tesseract.setOcrEngineMode(1); // OCR engine mode: LSTM_ONLY
		tesseract.setPageSegMode(3); // Page segmentation mode: Fully automatic page segmentation
		tesseract.setTessVariable("debug_file", "NUL");
	}
//	private static final String URL = "https://1win.pro/casino/play/aviator";
	private static final String COOKIES_FILE = "cookies.txt";
	private WebDriver driver;
	WebDriver dr2;
	CookieManager cookieManager = new CookieManager();
	int i = 0;
	WebElement betButton;
	WebElement cashOutButton;
	int winCount = 0;
	int lossCount = 0;
	int refreshCount = 0;
	@Autowired
	private Dao dao;

	@Transactional
	public OverallProfitLoss fetchProfitLoss(Integer id) {
		return dao.findWithInitializedFields(id);
	}

	@Transactional
	public void saveOrUpdateProfitLoss(double totalMoney) {
		// Retrieve the existing record by ID
		OverallProfitLoss profitLoss = dao.findById(1).orElse(new OverallProfitLoss());
		profitLoss.setId(1); // Ensure the ID is set for updates
		profitLoss.setTotalMoney(totalMoney); // Update the total money
		dao.save(profitLoss); // Save or update the record
	}

	@Autowired
	TelegramService telegramService;

//	@Autowired
//	private ProfitChecker profitChecker;
	MultiplierMonitor monitor = new MultiplierMonitor();

//	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	public void startAutomation() {
		// Use WebDriverManager to set up WebDriver
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
////				options.addArguments("--log-level=3"); // Only critical errors
				options.addArguments("--window-size=2560,1440");
				options.addArguments("--no-sandbox");
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("--disable-crash-reporter");
				options.addArguments("--disable-background-timer-throttling");
				options.addArguments("--disable-backgrounding-occluded-windows");
				options.addArguments("--disable-renderer-backgrounding");

				driver = new ChromeDriver(options);
//		        login();
				// Open the specified URL
//				driver.get("https://odds96.in/en/casino/game/3838-aviator");
				driver.get("https://1win.pro/casino/play/aviator");
				System.out.println("New browser window opened.");
				i = 0;

			} else {
				i = 1;
				System.out.println("Existing browser window is already active.");
			}
//			if (driver == null) {
//				driver = new ChromeDriver();
//			}
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			if (cookieManager.areCookiesAvailable(driver)) {
//				cookieManager.loadCookies(driver, COOKIES_FILE);
//			} else {
//				// Perform login for the first signal
//				login();
//				System.out.println("Logged in and session cookies saved.");
//			}

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
			if (i == 0) {
				driver.switchTo().defaultContent();
//				WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
//						By.cssSelector("button.btnV2.btnV2-m.btnV2-color-blue.btnV2-type-outline")));
//				loginButton.click();
				dr2 = driver;
//				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//						By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));
//
//				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//						By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));

				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
						By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));

//				WebElement closeButton = wait.until(driver -> {
//					WebElement element = driver.findElement(
//							By.xpath("//div[contains(@class, 'sec-hand-btn') and contains(@class, 'close')]"));
//					if (element.isDisplayed() && element.isEnabled()) {
//						return element;
//					}
//					return null;
//				});
//				closeButton.click();
//				System.out.println("Extra Bet Div Is Closed");
				WebElement autoButton = wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("//button[contains(text(),'Auto') and not(contains(@class, 'active'))]")));
				autoButton.click();
				wait.until(ExpectedConditions.attributeContains(autoButton, "class", "active"));
				System.out.println("Button clicked and active!");
				WebElement autoCashOutSwitch = wait.until(ExpectedConditions
						.elementToBeClickable(By.cssSelector(".cash-out-switcher .input-switch:first-child")));

				if (autoCashOutSwitch.getAttribute("class").contains("off")) {
					autoCashOutSwitch.click();
					System.out.println("Auto Cash Out switch toggled on.");
				} else {
					System.out.println("Auto Cash Out switch is already on.");
				}
				WebElement cashoutInput = wait.until(
						ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cashout-spinner .input input")));
				wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(cashoutInput, "disabled", "")));
				System.out.println("Auto Cash Out input is enabled.");

				// Step 3: Set the Auto Cash Out value to 1.5
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].value = '';", cashoutInput);
				cashoutInput.sendKeys("1.4");
				System.out.println("Auto Cash Out value set to 1.5.");
				WebElement totalAmountElement = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".amount.font-weight-bold")));
				String initialAmountText = totalAmountElement.getText().replaceAll("[^\\d.]", ""); // Remove non-numeric
																									// characters
				Double initialAmount = Double.parseDouble(initialAmountText);
				saveOrUpdateProfitLoss(initialAmount);

				telegramService.connect();
				TimeUnit.SECONDS.sleep(5);
				monitor.startMultipliersMonitoring(true);
//				DisconnectMonitor dis = new DisconnectMonitor(driver);
//				dis.startMonitoring();
//				profitChecker.startProfitMonitoring(wait, driver);
			}

			driver.switchTo().defaultContent();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the browser
//            driver.quit();
		}
	}

	public void typeLikeHuman(WebElement element, String text) throws InterruptedException {
		for (char c : text.toCharArray()) {
			element.sendKeys(String.valueOf(c));
			Thread.sleep((long) (Math.random() * 600 + 50)); // Random delay between 50-200ms
		}
	}

	public boolean isElementVisible(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
					".LoginModalFormDividedWrapper_active_GrU1f .LoginModalFormDividedWrapper_formTabsTitle_ECyBf")));
			return element.isDisplayed();
		} catch (Exception e) {
			return false; // Element is not visible
		}
	}

	private final int baseBetAmount = 10; // Base bet amount
	private int currentBetAmount = baseBetAmount; // Current bet amount

	public void startBetting(String message) throws InterruptedException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
			FluentWait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(100))
					.pollingEvery(Duration.ofMillis(200)) // Polling interval
					.ignoring(NoSuchElementException.class) // Ignore specific exceptions
					.ignoring(ElementNotInteractableException.class);

			driver.switchTo().defaultContent();
//			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//					By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));
//
//			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//					By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));

			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
					By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));

			int martingaleLevel = parseMartingaleLevel(message);
			adjustBetAmountForMartingaleLevel(martingaleLevel);
			WebElement betInput = fluentWait.until(driver -> {
				WebElement element = driver.findElement(By.cssSelector(".input .font-weight-bold"));
				if (element.isDisplayed() && element.isEnabled()) {
					return element;
				}
				return null;
			});

			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			jsExecutor.executeScript("arguments[0].value = '';", betInput);

			betInput.sendKeys(String.valueOf(currentBetAmount));
			System.out.println("Bet amount set to: " + currentBetAmount);

			WebElement betButton = fluentWait.until(driver -> {
				WebElement element = driver.findElement(By.cssSelector(".btn.btn-success.bet.ng-star-inserted"));
				if (element.isDisplayed() && element.isEnabled()) {
					return element;
				}
				return null;
			});

			betButton.click();
			System.out.println("Bet placed successfully: " + currentBetAmount);
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			System.err.println("Error during betting process: " + e.getMessage());
			resetBetting();
			telegramService.sendMessageToGroup("There is some issue in Betting phase, you might check..!!!");
		}
	}

	private int parseMartingaleLevel(String message) {
		if (message.contains("Martingale 1")) {
			return 1;
		} else if (message.contains("Martingale 2")) {
			return 2;
		} else if (message.contains("Martingale 3")) {
			return 3;
		} else if (message.contains("place a bet")) {
			return 0;
		} else {
			return 0;
		}
	}

	private void adjustBetAmountForMartingaleLevel(int martingaleLevel) {
		switch (martingaleLevel) {
		case 0:
			currentBetAmount = baseBetAmount; // First Bet
			break;
		case 1:
			currentBetAmount = 20; // First Martingale: Double the base
			break;
		case 2:
			currentBetAmount = 40; // Second Martingale: Quadruple the base
			break;
		case 3:
			currentBetAmount = 80; // Third Martingale: Eight times the base
			break;
		default:
			currentBetAmount = baseBetAmount; // Reset to base bet
			break;
		}
		System.out.println("Adjusted bet amount for Martingale level " + martingaleLevel + ": " + currentBetAmount);
	}

	private void resetBetting() {
		currentBetAmount = baseBetAmount;
		System.out.println("Resetting betting to base amount: " + currentBetAmount);
	}

	private boolean isSessionInactive(WebDriver driver) {
		try {
			driver.getTitle(); // Attempt to fetch the title as a test
			return false; // Session is active
		} catch (Exception e) {
			return true; // Session is inactive
		}
	}

	public class MultiplierMonitor {
		private volatile boolean isMonitoringActive = true;

		public void startMultipliersMonitoring(boolean start) {
			new Thread(() -> {
				int activeThreads = Thread.activeCount();
				System.out.println("Active Threads: " + activeThreads);
				System.out.println("Multiplier monitoring thread started...!");
				int startMonitoring = 0;
				List<Double> multiplierList = new ArrayList<>();
				List<Double> consecutiveMultipliers1 = new ArrayList<>(); // For 3 consecutive <= 1.0 followed by 2
				boolean startBet=false;														// consecutive > 2.0
				List<Double> pattern1 = new ArrayList<>();
				List<Double> pattern2 = new ArrayList<>();
				List<Double> CoolDownWind = new ArrayList<>();
				List<Double> sessionWind = new ArrayList<>();
				String cashout = "";
				boolean allGreaterThanTwo=false;
				LocalDateTime previousTime = null;
				while (isMonitoringActive) {
					try {

						WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
						driver.switchTo().defaultContent();
//						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//								By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));
//
//						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//								By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));

						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
								By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));

						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Step 2: Wait for the canvas element to become visible
						WebElement canvasElement = wait.until(ExpectedConditions
								.visibilityOfElementLocated(By.cssSelector("div.stage-canvas > canvas")));
						String textFromCanvas = "";
						try {
							textFromCanvas = extractTextFromCanvas(canvasElement);
						} catch (Exception e) {
//							System.err.println("Error processing canvas screenshot: " + e.getMessage());
						}
						if (textFromCanvas.contains("WAITING FOR NEXT ROUND")) {
							LocalDateTime currentTime = LocalDateTime.now();
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							String formattedTime = currentTime.format(formatter);
							if (previousTime != null) {
								Duration timeDifference = Duration.between(previousTime, currentTime);
								if (timeDifference.getSeconds() > 6) {
									List<WebElement> multiplierElements = (List<WebElement>) js.executeScript(
											"return Array.from(document.querySelectorAll('div.bubble-multiplier')).filter(el => el.offsetParent !== null);");
									String multiplierText = multiplierElements.get(0).getText().replace("x", "").trim();
									double latestMultiplier = Double.parseDouble(multiplierText);
//									int totalElements = multiplierElements.size();
									int startIndex = 0; // Ensure we don't go out of bounds
//									System.out.println("sixe is==============="+totalElements);
									for (int i = startIndex; i <= 20; i++) {
									    String multipliers = multiplierElements.get(i).getText().replace("x", "").trim();
									    try {
									        double multiplier = Double.parseDouble(multipliers);
									        sessionWind.add(multiplier);
									    } catch (NumberFormatException e) {
//									        System.err.println("Failed to parse multiplier: " + multiplierText);
									    }
									}
									System.out.println("sessionWind===="+sessionWind);
									long countRange1to2 = sessionWind.stream()
							                .filter(multiplier -> multiplier >= 1.0 && multiplier <= 2.0)
							                .count();

							        // Count multipliers in the range (2.0, above)
							        long countAbove2 = sessionWind.stream()
							                .filter(multiplier -> multiplier > 2.0)
							                .count();
							        
							        System.out.println("1x count===="+countRange1to2);
							        System.out.println("2x count===="+countAbove2);
//							       
//									if (latestMultiplier <= 2.0) {
									if (latestMultiplier >= 1.0) {
										if(countAbove2-countRange1to2==5) {
										 allGreaterThanTwo = IntStream.range(0, Math.min(4, sessionWind.size()))
		                                             .allMatch(i -> sessionWind.get(i) > 2.0);
											startBet=true;
											cashout="2";
										}
										
//										if (latestMultiplier <= 1.5) {
//											pattern1.add(latestMultiplier);
//											if (pattern1.size() == 4) {
//												cashout = "1.5";
//											}
//										} else {
//											pattern1.clear();
//										}
//
//										if (latestMultiplier > 2.0) {
//											pattern2.add(latestMultiplier);
//											if (pattern2.size() == 5) {
//												cashout = "2";
//											}
//										} else {
//											pattern2.clear();
//										}
										System.out.println(
												"Latest Multiplier: " + latestMultiplier + " Time: " + formattedTime);

										if (startBet && !allGreaterThanTwo) {
											try {
												telegramService.sendMessageToGroup("Starting Placing Bets....!!");

												WebElement cashoutInput = driver
														.findElement(By.cssSelector(".cashout-spinner .input input"));
												System.out.println("Auto Cash Out input is enabled.");

												// Set the Auto Cash Out value to 1.4
												JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
												jsExecutor.executeScript("arguments[0].value = '';", cashoutInput);
												cashoutInput.sendKeys(cashout);

												// Start betting (placeholder for actual betting logic)
												System.out.println("Starting betting phase...");
												startBetting("place a bet");
												if (cashout == "2") {
													System.out.println("Starting betting phase...multiplierlist=="
															+ pattern2);
													telegramService.sendMessageToGroup(String.format(
															"1Win\n\n✅ Confirmed login\n\nplace a bet! %.2fx\n\ncash out! 2x\n\n⚠️ MAXIMUM 3 GALES\n\n📜 Current multipliers list: %s",
															latestMultiplier, sessionWind));
												}

												if (cashout == "1.5") {
													System.out.println("Starting betting phase...multiplierlist=="
															+ pattern1);
													telegramService.sendMessageToGroup(String.format(
															"1Win\n\n✅ Confirmed login\n\nplace a bet! %.2fx\n\ncash out! 1.50x\n\n⚠️ MAXIMUM 3 GALES\n\n📜 Current multipliers list: %s",
															latestMultiplier, pattern1));
												}

												// Clear all lists after the betting phase
												pattern1.clear();
												pattern2.clear();
												sessionWind.clear();
												multiplierList.clear();
												startBet=false;
												stopMonitoring();
												new BettingPhase().startBettingOnMartingale(Double.parseDouble(cashout));

											} catch (Exception e) {
												System.out.println("There is some issue in placing bet-->");

												telegramService.sendMessageToGroup(
														"There is some issue in placing bet, closing program...!");
												System.exit(0);
											}
										}
									}

//									else {
//										System.out.println("Found larger than 2x Multiplier: " + latestMultiplier
//												+ " clearing list and size is:" + multiplierElements.size() + " time== "
//												+ "[" + formattedTime + "]");
//										multiplierList.clear();
//									}
									previousTime = currentTime;
									Thread.sleep(4000);
								}
							} else {
								List<WebElement> multiplierElements = (List<WebElement>) js.executeScript(
										"return Array.from(document.querySelectorAll('div.bubble-multiplier')).filter(el => el.offsetParent !== null);");
								String multiplierText = multiplierElements.get(0).getText().replace("x", "").trim();
								double latestMultiplier = Double.parseDouble(multiplierText);
								if (latestMultiplier <= 2.0) {
									System.out.println(
											"Latest multiplier, first entry is lower than 2 :" + latestMultiplier);
									multiplierList.add(latestMultiplier);
								} else {
									System.out.println(
											"Latest multiplier, first entry is larger than 2, not adding in list:"
													+ latestMultiplier);
								}
								previousTime = currentTime;
								Thread.sleep(4000);

							}
							if (startMonitoring == 0) {
								multiplierList.clear();
								startMonitoring++;
							}
							sessionWind.clear();
						}
					} catch (Exception e) {
//	                    System.out.println("Error in monitoring: " + e.getMessage());
					}
				}
			}).start();
		}

		public void stopMonitoring() {
			System.out.println("Multiplier Monitoring Stopped..!");
			isMonitoringActive = false;
		}
	}

	public class DisconnectMonitor {
		private WebDriver driver;
		private WebDriverWait wait;
		private volatile boolean isMonitoring = true;

		public DisconnectMonitor(WebDriver driver) {
			this.driver = driver;
			this.wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
		}

		public void startMonitoring() {
			isMonitoring = true;
			new Thread(this::monitor).start();
		}

		public void stopMonitoring() {
			isMonitoring = false; // Set flag to false to stop the thread
			System.out.println("Monitoring has been stopped.");
		}

		private void monitor() {
			System.out.println("Disconnect Monitoring has been started.");
			long startTime = System.currentTimeMillis();
			long duration = 10 * 60 * 60 * 1000; // 10 hours in milliseconds

			try {
//				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//						By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));

//                wait.until(ExpectedConditions
//                        .frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));

				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
						By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));

				By homeButtonSelector = By.cssSelector("app-disconnect-message .text"); // ---1win
//				By homeButtonSelector = By.cssSelector("a.btn.btn-warning.btn-lg.text-uppercase.ng-star-inserted");

				while (isMonitoring && System.currentTimeMillis() - startTime < duration) {
					try {
						checkAndRefreshIfNecessary(homeButtonSelector);
					} catch (Exception e) {
						System.out.println("Monitoring error: " + e.getMessage());
					}

					// Sleep for 10 seconds before checking again
					try {
						Thread.sleep(5000); // Adjust sleep time if needed
					} catch (InterruptedException e) {
						System.err.println("Monitoring thread interrupted: " + e.getMessage());
//						break;
					}
				}
			} catch (Exception e) {
				System.err.println("Monitoring setup failed: " + e.getMessage());
			}
		}

		private void checkAndRefreshIfNecessary(By homeButtonSelector) {
			try {
				driver.switchTo().defaultContent();

				WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(homeButtonSelector));

				if (element.isDisplayed()) {
					System.out.println("Element detected. Refreshing the driver...");
					refreshCount++;
					telegramService.sendMessageToGroup(
							"Element detected. Refreshing the driver...& refreshCount is: " + refreshCount);
					driver.navigate().refresh();
					// Re-enter the login flow after refresh
					loginAndStartMonitoring();
				}
			} catch (Exception e) {
				System.out.println("Element not found. Monitoring continues...");
			}
		}

		private void loginAndStartMonitoring() {
			try {
				WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.cssSelector("button.btnV2.btnV2-m.btnV2-color-blue.btnV2-type-outline")));
				loginButton.click();
				driver.switchTo().defaultContent();
//				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//						By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));
//
//				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//						By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));

				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
						By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));

				FluentWait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(500)) // Maximum
						.pollingEvery(Duration.ofMillis(500)) // Polling interval
						.ignoring(NoSuchElementException.class); // Ignore specific exceptions during polling

				// Step 2: Click on the "Auto" button if not already active
//				WebElement closeButton = wait.until(driver -> {
//					WebElement element = driver.findElement(
//							By.xpath("//div[contains(@class, 'sec-hand-btn') and contains(@class, 'close')]"));
//					if (element.isDisplayed() && element.isEnabled()) {
//						return element;
//					}
//					return null;
//				});
//				closeButton.click();
//				telegramService.sendMessageToGroup("Closing second betting div..!");
				WebElement autoButton = fluentWait.until(driver -> {
					WebElement element = driver.findElement(
							By.xpath("//button[contains(text(),'Auto') and not(contains(@class, 'active'))]"));
					if (element.isDisplayed() && element.isEnabled()) {
						return element;
					}
					return null;
				});
				autoButton.click();

				// Wait for the button to have the "active" class
				fluentWait.until(driver -> autoButton.getAttribute("class").contains("active"));
				System.out.println("Button clicked and active!");

				// Step 3: Handle the "Auto Cash Out" switch
				WebElement autoCashOutSwitch = fluentWait.until(driver -> {
					WebElement element = driver.findElement(By.cssSelector(".cash-out-switcher .input-switch"));
					if (element.isDisplayed() && element.isEnabled()) {
						return element;
					}
					return null;
				});

				if (autoCashOutSwitch.getAttribute("class").contains("off")) {
					autoCashOutSwitch.click();
					System.out.println("Auto Cash Out switch toggled on.");
				} else {
					System.out.println("Auto Cash Out switch is already on.");
				}
//				telegramService.sendMessageToGroup("Auto Cash Out switch toggled on.");
				// Step 4: Enable and set the "Auto Cash Out" value
				WebElement cashoutInput = fluentWait.until(driver -> {
					WebElement element = driver
							.findElement(By.cssSelector(".cashout-spinner .input input:first-child"));
					if (element.isDisplayed()) {
						String disabledAttribute = element.getAttribute("disabled");
						if (disabledAttribute == null || !disabledAttribute.contains("")) {
							return element;
						}
					}
					return null;
				});
				System.out.println("Auto Cash Out input is enabled.");
//				telegramService.sendMessageToGroup("Auto Cash Out input is enabled.");

				// Step 3: Set the Auto Cash Out value to 1.5
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].value = '';", cashoutInput);
				cashoutInput.sendKeys("1.4");
				System.out.println("Auto Cash Out value set to 1.5.");
				telegramService.sendMessageToGroup("Auto Cash Out value set to 1.5.");
			} catch (Exception e) {
				System.err.println("Login and monitoring setup failed: " + e.getMessage());
			}
		}
	}

	public class BettingPhase {

		private volatile boolean isBetting = true;
		MultiplierMonitor monitor = new MultiplierMonitor();

		public void startBettingOnMartingale(double cashout) {
			System.out.println("betting on martingale thread started...!");
			new Thread(() -> {
				int bettingCount = 0;
				LocalDateTime previousTime = LocalDateTime.now();
				isBetting = true;
				while (isBetting) {
					try {
						if (bettingCount == 0) {
//							startBetting("place a bet");
							Thread.sleep(3000);
						}
						WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000));
						driver.switchTo().defaultContent();
//						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//								By.cssSelector("iframe[src^='https://sgc.odds96.com/gamesession/']")));
//
//						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
//								By.cssSelector("iframe[src^='https://demo.spribe.io/launch/aviator']")));

						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
								By.cssSelector("iframe[src^='https://launch.spribegaming.com/aviator?")));

//						FluentWait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
//								.ignoring(NoSuchElementException.class) // Ignore specific exceptions
//								.ignoring(StaleElementReferenceException.class); // Handle dynamic DOM updates

						// Step 2: Wait for the canvas element to become visible
						WebElement canvasElement = wait.until(ExpectedConditions
								.visibilityOfElementLocated(By.cssSelector("div.stage-canvas > canvas")));
						JavascriptExecutor js = (JavascriptExecutor) driver;
						String textFromCanvas = "";
						try {
							textFromCanvas = extractTextFromCanvas(canvasElement);
						} catch (Exception e) {
							System.err.println("Error processing canvas screenshot: " + e.getMessage());
						}
						if (textFromCanvas.contains("WAITING FOR NEXT ROUND")) {

							LocalDateTime currentTime = LocalDateTime.now();
							Duration timeDifference = Duration.between(previousTime, currentTime);
//							System.out.println("difference==="+timeDifference);
							if (timeDifference.getSeconds() > 6 || bettingCount == 1) {

								List<WebElement> multiplierElements = (List<WebElement>) js.executeScript(
										"return Array.from(document.querySelectorAll('div.bubble-multiplier')).filter(el => el.offsetParent !== null);");
								String multiplierText = multiplierElements.get(0).getText().replace("x", "").trim();
								double latestMultiplier = Double.parseDouble(multiplierText);
								if (latestMultiplier <= cashout) {
									System.out.println("In Betting Phase Latest multiplier: " + latestMultiplier);

//	                        if (latestMultiplier < 2.0) {
									if (bettingCount == 1) {
										startBetting("Martingale 1");
										telegramService
												.sendMessageToGroup("⚠️ Martingale 1 Enter Now!!! Last Multiplier Was: "
														+ latestMultiplier);
									} else if (bettingCount == 2) {
										startBetting("Martingale 2");
										telegramService
												.sendMessageToGroup("⚠️ Martingale 2 Enter Now!!! Last Multiplier Was: "
														+ latestMultiplier);
									} else if (bettingCount == 3) {
										startBetting("Martingale 3");
										telegramService
												.sendMessageToGroup("⚠️ Martingale 3 Enter Now!!! Last Multiplier Was: "
														+ latestMultiplier);
									}
								} else {
									winCount++;
									System.out.println("Cashout has been done at 1.4x and last multiplier was "
											+ latestMultiplier);
									isBetting = false;
									bettingCount = 0;
									WebElement totalAmountElement = wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(".amount.font-weight-bold")));
									String currentAmountText = totalAmountElement.getText().replaceAll("[^\\d.]", "");
									double currentAmount = Double.parseDouble(currentAmountText);
									telegramService.sendMessageToGroup(String.format(
											"🎲 Bet result: %s\n✅ Total Wins: %d\n❌ Total Losses: %d\n\n Current Amount: %.2f",
											latestMultiplier, winCount, lossCount, currentAmount));
									stopBettingMonitoring();
									monitor.startMultipliersMonitoring(true);
								}

								if (bettingCount == 4) {
									lossCount++;
									isBetting = false;
									bettingCount = 0;
									WebElement totalAmountElement = wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(".amount.font-weight-bold")));
									String currentAmountText = totalAmountElement.getText().replaceAll("[^\\d.]", "");
									double currentAmount = Double.parseDouble(currentAmountText);
									telegramService.sendMessageToGroup("😔 Bet Lost..!");
									telegramService.sendMessageToGroup(String.format(
											"🎲 Bet result: %s\n\n--------\n✅ Total Wins: %d\n❌ Total Losses: %d\n--------\n\n Current Amount: %.2f",
											latestMultiplier, winCount, lossCount, currentAmount));
									stopBettingMonitoring();
									monitor.startMultipliersMonitoring(true);
								}
								bettingCount++;
								previousTime = currentTime;
								Thread.sleep(4000);
							}

						}
						if (bettingCount == 0) {
							bettingCount++;
						}
						driver.switchTo().defaultContent();
					} catch (InterruptedException e) {
						System.err.println("Thread interrupted: " + e.getMessage());
//	                    Thread.currentThread().interrupt();
//	                    break;
					} catch (Exception e) {
						System.err.println("Error in betting phase: " + e.getMessage());
						telegramService.sendMessageToGroup("Error in betting phase:----------- ");
					}
				}

			}).start();
		}

		public void stopBettingMonitoring() {
			System.out.println("Betting Monitoring Stopped...!");
			isBetting = false;
		}
	}

//	private static String extractTextFromImage(BufferedImage imageFile) {
//		try {
//			ITesseract tesseract = new Tesseract(); // Tesseract OCR instance
//			tesseract.setDatapath("C:/tesseract/tessdata"); // Set the Tesseract data path
//			tesseract.setLanguage("eng");
//			return tesseract.doOCR(imageFile);
//		} catch (TesseractException e) {
//			System.err.println("Error extracting text from canvas image: " + e.getMessage());
//			return null;
//		}
//	}
	public static String extractTextFromCanvas(WebElement canvasElement) {
		try {
			// Capture screenshot as byte array for faster processing
			byte[] canvasScreenshotBytes = canvasElement.getScreenshotAs(OutputType.BYTES);
			ByteArrayInputStream bis = new ByteArrayInputStream(canvasScreenshotBytes);
			BufferedImage canvasImage = ImageIO.read(bis);

			// Extract text using Tesseract
			return tesseract.doOCR(canvasImage);
		} catch (IOException e) {
//            System.err.println("Error processing canvas screenshot: " + e.getMessage());
		} catch (TesseractException e) {
//            System.err.println("Error extracting text from canvas image: " + e.getMessage());
		}
		return null;
	}
}
