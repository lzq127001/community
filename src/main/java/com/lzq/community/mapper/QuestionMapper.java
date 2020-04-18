package com.lzq.community.mapper;

import com.lzq.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//插入数据库
@Mapper
public interface QuestionMapper {
    //从Question类中拿数据添加到数据库
    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question")
    List<Question> list();
}
