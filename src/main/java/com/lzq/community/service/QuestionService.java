package com.lzq.community.service;

import com.lzq.community.dto.PaginationDTO;
import com.lzq.community.dto.QuestionDTO;
import com.lzq.community.exception.CustomizeException;
import com.lzq.community.exception.CustomizeErrorCode;
import com.lzq.community.mapper.QuestionExtMapper;
import com.lzq.community.mapper.QuestionMapper;
import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.Question;
import com.lzq.community.model.QuestionExample;
import com.lzq.community.model.User;
import org.apache.ibatis.session.RowBounds;
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

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {
        //先算偏移量(开始的位置)
        Integer offset = size*(page - 1);
        //一个总的列表，包含question中的所有属性加变量user
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //从question表中取出所有问题列表
        //List<Question> questionList = questionMapper.list(offset,size);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        //新建一个页面DTO类，（包含qusiton+user+页数），准备返回
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList) {
            //先建一个DTO，用来收纳qustion和user
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            BeanUtils.copyProperties(question, questionDTO);//把question中的属性都复制到questionDTO中去
            questionDTO.setUser(user);

            //将完成拼接的一个列表项（用户+问题）加入到列表中去，最后将列表返回即可
            questionDTOList.add(questionDTO);
        }

        //将数值添加到页面pagination中
        paginationDTO.setQuestions(questionDTOList);
        //Integer totalCount = questionMapper.count();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());

        paginationDTO.setPagination(totalCount, page, size);


        return paginationDTO;
    }

    //根据creator选择数据库中的内容，返回该创建者提交的问题列表
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //先算偏移量
        Integer offset = size*(page - 1);
        //一个总的列表，包含question中的所有属性加变量user
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //从question表中取出所有问题列表
        //List<Question> questionList = questionMapper.listByUserId(userId,offset,size);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        //新建一个页面DTO类，（包含qusiton+user+页数），准备返回
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList) {
            //先建一个DTO，用来收纳qustion和user
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            BeanUtils.copyProperties(question, questionDTO);//把question中的属性都复制到questionDTO中去
            questionDTO.setUser(user);

            //将完成拼接的一个列表项（用户+问题）加入到列表中去，最后将列表返回即可
            questionDTOList.add(questionDTO);
        }

        //将数值添加到页面pagination中
        paginationDTO.setQuestions(questionDTOList);
        //Integer totalCount = questionMapper.countByUserId(userId);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

        paginationDTO.setPagination(totalCount, page, size);


        return paginationDTO;
    }

    //根据问题id返回 问题+用户 信息
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }

    //和更新用户时类似
    public void createOrUpdate(Question question) {
        if(question.getId() == null){//创建提问
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());

            //不知道为什么默认值是0，存进去却为null

            questionMapper.insertSelective(question);
        }else {
            question.setGmtModified(question.getGmtCreate());
            int updated = questionMapper.updateByPrimaryKeySelective(question);

            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {

        /*//要更新的数据，用updateByExampleSelective语句，有选择的更新，不更的设置null，更新的设置值
        Question question = questionMapper.selectByPrimaryKey(id);
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(question.getViewCount() + 1);
        //要更新哪个id的问题，也得在语句里说明
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
        //这是更新语法
        questionMapper.updateByExampleSelective(updateQuestion, questionExample);*/

        //上面这样写可能遇到并发有问题，自己写一个映射
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);//因为每次递增一个步长
        questionExtMapper.incView(question);
    }
}
