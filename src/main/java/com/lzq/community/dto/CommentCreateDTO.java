package com.lzq.community.dto;

import lombok.Data;

//需要从前端页面传回来的json数据
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;

}
