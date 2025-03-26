package com.jahnavi;

import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

@WebServlet("/crawl")
public class ImageCrawlerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getParameter("url");

        if (url == null || url.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing URL parameter.");
            return;
        }

        try {
            Set<String> images = ImageCrawlerUtil.crawlWebsite(url);
//            Set<String> images = SeleniumImageCrawler.crawlWebsite(url);



            String json = new Gson().toJson(images);
            System.out.println("Returning JSON: " + json);
            resp.setContentType("application/json");
            resp.getWriter().write(json);

        } catch (URISyntaxException | InterruptedException e) {
            resp.setStatus(500);
            resp.getWriter().write("Server error: " + e.getMessage());
        }
    }
}
