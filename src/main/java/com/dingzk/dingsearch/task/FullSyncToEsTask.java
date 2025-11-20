package com.dingzk.dingsearch.task;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.dto.PostEsDto;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

// 首次允许项目时，启用此 @Component 注解
@Component
@Slf4j
public class FullSyncToEsTask {

    @Resource
    private ElasticsearchClient esClient;

    @Resource
    private PostService postService;

    @Scheduled(initialDelay = 500)
    public void fullAsyncPostToEs() {
        // 当前先通过全量一次性同步方式实现
        List<Post> postList = postService.list();
        List<PostEsDto> postEsDtoList = postList.stream().map(PostEsDto::fromPost).toList();

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (PostEsDto postEsDto : postEsDtoList) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("post")
                            .id(String.valueOf(postEsDto.getId()))
                            .document(postEsDto)
                    )
            );
        }

        BulkResponse result;
        try {
            result = esClient.bulk(br.build());
            log.info("post数据同步成功");
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "全量同步post数据失败");
        }

        // Log errors, if any
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }
}
