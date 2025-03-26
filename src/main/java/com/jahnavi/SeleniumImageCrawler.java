package com.jahnavi;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;

public class SeleniumImageCrawler {

    private static final int MAX_DEPTH = 2;
    private static final Set<String> visited = ConcurrentHashMap.newKeySet();
    private static final Set<String> imageLinks = ConcurrentHashMap.newKeySet();

    public static Set<String> crawlWebsite(String startUrl) throws InterruptedException, URISyntaxException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--disable-gpu");
        WebDriver driver = new ChromeDriver(options);

        String baseDomain = getDomain(startUrl);
        crawl(driver, startUrl, baseDomain, 0);

        driver.quit();
        return imageLinks;
    }

    private static void crawl(WebDriver driver, String url, String baseDomain, int depth) throws URISyntaxException {
        if (depth > MAX_DEPTH || visited.contains(url)) return;

        try {
            visited.add(url);
            driver.get(url);

            // Extract images
            List<WebElement> imgs = driver.findElements(By.tagName("img"));
            for (WebElement img : imgs) {
                String src = img.getAttribute("src");
                if (src != null && !src.isEmpty()) {
                    imageLinks.add(src);
                }
            }

            // Extract internal links
            List<WebElement> links = driver.findElements(By.tagName("a"));
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                if (href != null && href.startsWith("http") && getDomain(href).equals(baseDomain) && !visited.contains(href)) {
                    crawl(driver, href, baseDomain, depth + 1);
                }
            }

        } catch (Exception e) {
            System.out.println(" Failed to crawl: " + url + " - " + e.getMessage());
        }
    }

    private static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return uri.getHost() == null ? "" : uri.getHost();
    }
}
