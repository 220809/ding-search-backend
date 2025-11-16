package com.dingzk.dingsearch;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class DingSearchBackendApplicationTests {

    @Resource
    private PostService postService;

    @Test
    void contextLoads() {
    }

    @Test
    void testFetchWebPageContent() throws IOException {
        final List<Long> authorIdList =
                List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L, 11L, 13L, 17L, 18L, 20L, 23L, 24L, 30L);
        Document document = Jsoup.connect("https://www.cnblogs.com/pick/" + "#p" + 10).get();
        Elements titleLinkElements = document.getElementsByClass("post-item-title");

        String[] linksArray = titleLinkElements.stream()
                .map(e -> e.attribute("href").getValue())
                .filter(StringUtils::isNotBlank).distinct().toArray(String[]::new);
        Document doc = Jsoup.connect(linksArray[0]).get();
        // 文章标题
        String title = doc.getElementsByClass("postTitle").get(0).text();
        // 文章内容
        Elements paragraphs = doc.select(".postBody p");
        String content = paragraphs.stream().map(Element::text).limit(20).collect(Collectors.joining("\n"));
//        Post post = new Post();
//        post.setTitle(title);
//        post.setContent(content);
//        Long authorId = authorIdList.get((int) (Math.random() * authorIdList.size()));
//        post.setAuthorId(authorId);
//        boolean result = postService.save(post);
//        Assertions.assertTrue(result);
//        Assertions.assertNotNull(post.getId());
    }

    @Test
    public void testFetchWebPageContent2() throws IOException {
        // 解决 jsoup 直接通过 '#p2' 无法获取到指定页面数据

        String url = "https://www.cnblogs.com/AggSite/AggSitePostList";
        // 构建请求体JSON数据
        JSONObject requestBody = new JSONObject();
        requestBody.set("CategoryType", "Picked");
        requestBody.set("ParentCategoryId", 0);
        requestBody.set("CategoryId", -2);
        requestBody.set("PageIndex", 4);
        requestBody.set("TotalPostCount", 1636);
        requestBody.set("ItemListActionName", "AggSitePostList");

        try (HttpResponse httpResponse = HttpRequest.post(url)
                .header("accept", "text/plain, */*; q=0.01")
                .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,en-GB;q=0.6")
                .header("content-type", "application/json; charset=UTF-8")
                .header("origin", "https://www.cnblogs.com")
//                .header("priority", "u=1, i")
                .header("referer", "https://www.cnblogs.com/pick/")
//                .header("sec-ch-ua", "\"Chromium\";v=\"142\", \"Microsoft Edge\";v=\"142\", \"Not_A Brand\";v=\"99\"")
//                .header("sec-ch-ua-mobile", "?0")
//                .header("sec-ch-ua-platform", "\"Windows\"")
//                .header("sec-fetch-dest", "empty")
//                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0")
//                .header("x-requested-with", "XMLHttpRequest")
                .body(requestBody.toString())
                .execute()) {
            Document document = Jsoup.parse(httpResponse.body());
            Elements titleLinkElements = document.getElementsByClass("post-item-title");

            String[] linksArray = titleLinkElements.stream()
                    .map(e -> e.attribute("href").getValue())
                    .filter(StringUtils::isNotBlank).distinct().toArray(String[]::new);
            Document doc = Jsoup.connect(linksArray[1]).get();
            Element keywordsElement = doc.head().selectFirst("[name=\"keywords\"]");
            String tags = keywordsElement.attr("content");
            // 文章标题
            String title = doc.getElementsByClass("postTitle").get(0).text();
            // 文章内容
            Elements paragraphs = doc.select(".postBody p");
            String content = paragraphs.stream().map(Element::text).limit(20).collect(Collectors.joining("\n"));
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            System.out.println(document);
        }
    }
}
