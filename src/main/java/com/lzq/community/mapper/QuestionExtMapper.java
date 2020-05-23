package com.lzq.community.mapper;

import com.lzq.community.model.Question;
import com.lzq.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/*
 * 自己写增加阅读数的接口，以免被mybatis generator覆盖，
 * 因为并发阅读时需要viewCount = viewCount+1；（从数据库实时拿viewCount）
 * 不能viewCount= 15+1；这样
 */
public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
}