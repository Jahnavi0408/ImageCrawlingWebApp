package com.jahnavi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.*;

public class ImageCrawlerUtil {

    private static final int MAX_DEPTH = 5;
    private static final int THREAD_POOL_SIZE = 10;

    public static Set<String> crawlWebsite(String url) throws URISyntaxException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        Set<String> visited = ConcurrentHashMap.newKeySet();
        Set<String> imageLinks = ConcurrentHashMap.newKeySet();
        String baseDomain = getDomain(url);

        executor.submit(() -> crawl(url, 0, baseDomain, visited, imageLinks, executor));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return imageLinks;
    }

    private static void crawl(String url, int depth, String baseDomain,
                              Set<String> visited, Set<String> imageLinks,
                              ExecutorService executor) {

        if (depth > MAX_DEPTH || visited.contains(url)) return;

        try {
            visited.add(url);
            Document doc = Jsoup.connect(url).get();

            for (Element img : doc.select("img")) {
                String src = img.absUrl("src");
                if (!src.isEmpty()) imageLinks.add(src);
            }

            for (Element link : doc.select("a[href]")) {
                String href = link.absUrl("href");
                if (!href.isEmpty() && getDomain(href).equals(baseDomain)) {
                    executor.submit(() -> crawl(href, depth + 1, baseDomain, visited, imageLinks, executor));
                }
            }

        } catch (Exception e) {
            System.out.println("Error crawling: " + url + " → " + e.getMessage());
        }
    }

    private static String getDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return uri.getHost() == null ? "" : uri.getHost();
    }
}
