package com.lzq.community.dto;

import com.lzq.community.model.User;

public class CommentDTO {
    private Long id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
}
