package com.lzq.community.controller;

import com.lzq.community.dto.CommentDTO;
import com.lzq.community.dto.QuestionDTO;
import com.lzq.community.enums.CommentTypeEnum;
import com.lzq.community.service.CommentService;
import com.lzq.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QustionController {

    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired(required = false)
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model){

        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
