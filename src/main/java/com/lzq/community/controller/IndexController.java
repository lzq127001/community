package com.lzq.community.controller;

import com.lzq.community.dto.PaginationDTO;
import com.lzq.community.mapper.UserMapper;
import com.lzq.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,//获取分页的页数和偏移量。好从数据库钟取出对应的数据
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "search",required = false) String search){

        //从数据库中拿quesiton表和user表的数据
        PaginationDTO pagination = questionService.list(search,page,size);
        //传数据回前端页面
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
