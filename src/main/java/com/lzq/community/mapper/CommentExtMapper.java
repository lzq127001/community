package com.lzq.community.mapper;

import com.lzq.community.model.Comment;


public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}