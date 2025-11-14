package com.dingzk.dingsearch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 帖子
 */
@TableName(value ="post")
@Data
public class Post {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
    private String tags;

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

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;
}