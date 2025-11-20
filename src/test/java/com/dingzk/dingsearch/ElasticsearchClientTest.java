package com.dingzk.dingsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.domain.Product;
import com.dingzk.dingsearch.model.dto.PostEsDto;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * 测试 es 客户端连接及数据操作
 */
@SpringBootTest
public class ElasticsearchClientTest {

    @Resource
    private ElasticsearchClient esClient;

    @Resource
    private PostService postService;

    @Test
    void testFetchDataFromEs() throws IOException {
        SearchResponse<Product> search = esClient.search(s -> s
                        .index("product")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("鸡汤"))
                                )),
                Product.class);

        for (Hit<Product> hit: search.hits().hits()) {
            System.out.println(hit.source());
        }
    }

    @Test
    void testSaveDataFromMysqlToEs() throws IOException {
        // 注意使用已存在的 id
        Post post = postService.getById(1);
        // 保存到 es
        IndexResponse indexResponse = esClient.index(i -> i
                .index("post")
                .id(String.valueOf(post.getId()))
                .document(PostEsDto.fromPost(post))
        );
        String result = indexResponse.result().name();
        Assertions.assertTrue("Created".equalsIgnoreCase(result) || "Updated".equalsIgnoreCase(result));
    }

    @Test
    void testSearchDataFromEs() throws IOException {
        // 构造查询条件
        Query titleQuery = MatchQuery.of(m -> m.field("title").query("一"))._toQuery();
        Query contentQuery = MatchQuery.of(m -> m.field("content").query("2025"))._toQuery();
        Query tagQuery = TermQuery.of(t -> t.field("tags").value(".ne").caseInsensitive(true))._toQuery();
        Query authorIdQuery = TermQuery.of(t -> t.field("authorId").value(9))._toQuery();

        Query deletedQuery = TermQuery.of(t -> t.field("deleted").value(0))._toQuery();
        Query createTimeQuery = RangeQuery.of(r -> r.field("createTime")
                .gte(JsonData.of("2025-11-16 18:04:15"))
                .lte(JsonData.of("2025-11-16 20:04:15"))
                .timeZone("+08:00"))._toQuery();

        SearchResponse<PostEsDto> searchResponse = esClient.search(s -> s
                        .index("post")
                        .query(q -> q
                                .bool(b -> b
                                        .should(List.of(titleQuery, contentQuery, tagQuery, authorIdQuery))
                                        .filter(List.of(deletedQuery, createTimeQuery))
                                        .minimumShouldMatch("1")
                                ))
                        .size(1)
                , PostEsDto.class);
        List<Hit<PostEsDto>> hits = searchResponse.hits().hits();
        Assertions.assertFalse(CollectionUtils.isEmpty(hits));
    }
}
