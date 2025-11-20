package com.dingzk.dingsearch.model.dto;

import com.dingzk.dingsearch.model.domain.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Post 保存在 es 中的字段数据
 */
@Data
public class PostEsDto {

    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer deleted;

    private static final Gson GSON = new Gson();
    public static PostEsDto fromPost(Post post) {
        PostEsDto postEsDto = new PostEsDto();
        BeanUtils.copyProperties(post, postEsDto);
        String postTags = post.getTags();
        List<String> tagList = GSON.fromJson(postTags, new TypeToken<List<String>>() {});
        postEsDto.setTags(tagList);
        return postEsDto;
    }
}
