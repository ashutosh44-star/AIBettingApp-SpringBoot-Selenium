package com.ashu.AIBet.service;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

public class CookieManager {

    // Method to check if cookies are available in WebDriver session
    public boolean areCookiesAvailable(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        return cookies != null && !cookies.isEmpty(); // Returns true if cookies exist
    }

    // Method to save cookies to a file
    public void saveCookies(WebDriver driver, String filePath) {
        Set<Cookie> cookies = driver.manage().getCookies();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Cookie cookie : cookies) {
                writer.write(cookie.getName() + ";" + cookie.getValue() + ";" + cookie.getDomain() + ";"
                        + cookie.getPath() + ";" + cookie.getExpiry() + ";" + cookie.isSecure());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load cookies from a file
    public void loadCookies(WebDriver driver, String filePath) {
        File cookieFile = new File(filePath);
        if (!cookieFile.exists()) {
            System.out.println("No cookies to load.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(cookieFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Cookie cookie = new Cookie(parts[0], parts[1], parts[2], parts[3],
                        parts[4].equals("null") ? null : new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(parts[4]),
                        Boolean.parseBoolean(parts[5]));
                driver.manage().addCookie(cookie);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
