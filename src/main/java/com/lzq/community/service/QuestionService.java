package com.lzq.community.service;

import com.lzq.community.dto.QuestionDTO;
import com.lzq.community.mapper.QuestionMapper;
import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.Question;
import com.lzq.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    //操作Mapper类可对数据库进行操作
    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    public List<QuestionDTO> list() {

        //一个总的列表，包含question中的所有属性加变量user
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //从question表中取出所有问题列表
        List<Question> questionList = questionMapper.list();
        for (Question question : questionList) {
            //先建一个DTO，用来收纳qustion和user
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.findByID(question.getCreator());
            BeanUtils.copyProperties(question, questionDTO);//把question中的属性都复制到questionDTO中去
            questionDTO.setUser(user);

            //将完成拼接的一个列表项（用户+问题）加入到列表中去，最后将列表返回即可
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
