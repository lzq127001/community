package com.lzq.community.dto;

import com.lzq.community.model.User;
import lombok.Data;
//为了从question表中的creator关联到user表中的id，拿到两个表的数据
//这个类比model层中的Question类再多一个User变量，因为model中是和数据库关联的，为了规范一点，在这里建
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private User user;
}
