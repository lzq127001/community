package com.lzq.community.controller;

import com.lzq.community.dto.QuestionDTO;
import com.lzq.community.mapper.QuestionMapper;
import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.Question;
import com.lzq.community.model.User;
import com.lzq.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();
        //如果cookies不为空，循环之
        if(cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        System.out.println("通过token从数据库获取的user信息不为空，在服务器端建立session");
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }else {
            System.out.println("cookies为空");
        }
        //从数据库中拿quesiton表和user表的数据
        List<QuestionDTO> questionList = questionService.list();
        //传数据回前端页面
        model.addAttribute("questions",questionList);

        return "index";
    }
}
