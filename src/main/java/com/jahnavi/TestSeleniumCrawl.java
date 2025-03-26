package com.jahnavi;

import java.util.Set;

public class TestSeleniumCrawl {
    public static void main(String[] args) throws Exception {
        Set<String> images = SeleniumImageCrawler.crawlWebsite("https://jobright.ai/ai-resume-builder");
        for (String img : images) {
            System.out.println(img);
        }
    }
}
