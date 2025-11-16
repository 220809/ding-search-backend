package com.dingzk.dingsearch.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RetrievePostFromWebTask {

    private static final String BASE_URL = "https://www.cnblogs.com/AggSite/AggSitePostList";
    private static final List<Long>  authorIdList =
            List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L, 11L,
                    13L, 17L, 18L, 20L, 23L, 24L, 26L, 30L,
                    31L, 34L, 35L, 37L, 40L);

    @Resource
    private PostService postService;

    @Scheduled(initialDelay = 500)
    public void retrievePostFromWeb() throws IOException {
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            // 构建请求体JSON数据
            JSONObject requestBody = new JSONObject();
            requestBody.set("CategoryType", "Picked");
            requestBody.set("ParentCategoryId", 0);
            requestBody.set("CategoryId", -2);
            requestBody.set("PageIndex", i);
            requestBody.set("TotalPostCount", 1636);
            requestBody.set("ItemListActionName", "AggSitePostList");

            try (HttpResponse httpResponse = HttpRequest.post(BASE_URL)
                    .header("accept", "text/plain, */*; q=0.01")
                    .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,en-GB;q=0.6")
                    .header("content-type", "application/json; charset=UTF-8")
                    .header("origin", "https://www.cnblogs.com")
                    .header("referer", "https://www.cnblogs.com/pick/")
                    .header("sec-fetch-site", "same-origin")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36 Edg/142.0.0.0")
                    .body(requestBody.toString())
                    .execute()) {
                Document document = Jsoup.parse(httpResponse.body());

                Elements titleLinkElements = document.getElementsByClass("post-item-title");

                String[] linksArray = titleLinkElements.stream()
                        .map(e -> e.attribute("href").getValue())
                        .filter(StringUtils::isNotBlank).distinct().toArray(String[]::new);

                List<Post> postList = new ArrayList<>();
                for (String link : linksArray) {
                    Document doc = Jsoup.connect(link).get();
                    // 文章标题
                    Element postTitleElement = doc.selectFirst(".postTitle");
                    if (postTitleElement == null) {
                        continue;
                    }
                    String title = postTitleElement.text();
                    if (StringUtils.isBlank(title)) {
                        continue;
                    }
                    // 文章内容
                    Element paragraphElement = doc.selectFirst(".postBody p");
                    if (paragraphElement == null) {
                        continue;
                    }
                    String content = paragraphElement.stream().map(Element::text).limit(20).collect(Collectors.joining(""));
                    if (StringUtils.isBlank(content)) {
                        continue;
                    }
                    // 标签
                    Element keywordsElement = doc.head().selectFirst("[name=\"keywords\"]");
                    String tags = "";
                    if (keywordsElement != null) {
                        tags = keywordsElement.attr("content");
                    }
                    // 创建帖子对象
                    Post post = new Post();
                    post.setTitle(title);
                    post.setContent(content);
                    Long authorId = authorIdList.get((int) (Math.random() * authorIdList.size()));
                    post.setAuthorId(authorId);

                    // 处理 tags
                    String wrappedTags = Arrays.stream(tags.split(","))
                            .map(tag -> StringUtils.wrap(tag.trim(), "\""))
                            .collect(Collectors.joining(","));
                    post.setTags("[" + wrappedTags + "]");
                    postList.add(post);
                }

                try {
                    postService.saveBatch(postList, postList.size());
                    count += postList.size();
                } catch (Exception e) {
                    log.error("Batch saving post occurs error: {}, {} posts ignored", e.getMessage(), postList.size());
                }

            }
        }

        log.info("Retrieve post from web ended, postList size: {}", count);
    }
}
