package com.dingzk.dingsearch.model.vo;

import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostVo implements Serializable {
    /**
     * 主键
     */
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

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Gson gson = new Gson();

    public static PostVo fromPost(Post post) {
        PostVo result = new PostVo();
        try {
            BeanUtils.copyProperties(post, result);

            String postTags = post.getTags();
            List<String> postTagList = gson.fromJson(postTags, new TypeToken<>() {});
            result.setTags(postTagList);
        } catch (BeansException e) {
            throw new BusinessException(ErrorCode.DATA_CONVERSION_ERROR);
        }

        return result;
    }

    public static List<PostVo> fromPostList(List<Post> postList) {
        if (postList.isEmpty()) {
            return Collections.emptyList();
        }
        return postList.stream().map(PostVo::fromPost).collect(Collectors.toList());
    }
}
