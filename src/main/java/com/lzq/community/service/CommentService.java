package com.lzq.community.service;

import com.lzq.community.dto.CommentDTO;
import com.lzq.community.enums.CommentTypeEnum;
import com.lzq.community.exception.CustomizeErrorCode;
import com.lzq.community.exception.CustomizeException;
import com.lzq.community.mapper.CommentMapper;
import com.lzq.community.mapper.QuestionExtMapper;
import com.lzq.community.mapper.QuestionMapper;
import com.lzq.community.model.Comment;
import com.lzq.community.model.CommentExample;
import com.lzq.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired(required = false)
    CommentMapper commentMapper;

    @Autowired(required = false)
    QuestionMapper questionMapper;

    @Autowired(required = false)
    QuestionExtMapper questionExtMapper;

    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId() == null && comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null && !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYOE_WRONG);
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey((long) comment.getParentId());
            if(dbcomment == null){
                throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }

//    public List<CommentDTO> listByQuestionId(Integer id) {
//        CommentExample example = new CommentExample();
//        example.createCriteria()
//                .andParentIdEqualTo(id);
//        commentMapper.selectByExample(example);
//    }
}
