package com.lzq.community.mapper;

import com.lzq.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//插入数据库
@Mapper
public interface QuestionMapper {
    //从Question类中拿数据添加到数据库
    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(Integer offset, Integer size);
    //获取问题总数
    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset}, #{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, Integer offset, Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId") Integer userId);
}
