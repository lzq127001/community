package com.lzq.community.mapper;

import com.lzq.community.dto.QuestionDTO;
import com.lzq.community.dto.QuestionQueryDTO;
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

    //根据question中的id和tags查找（返回除了该id外所有标签相关的列表）
    List<Question> selectRelated(Question question);

    //根据搜索关键字查找标题
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}