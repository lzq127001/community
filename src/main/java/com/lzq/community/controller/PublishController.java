package com.lzq.community.controller;

import com.lzq.community.mapper.QuestionMapper;
import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.Question;
import com.lzq.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            //从publish提交表单的post方式获取信息，写入Question类，再用QuestionMapper插入数据库
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model) {

        //因为判断标题内容不能为空的时候会返回该页面，所以之前输入的标题就得继续保存，不然标题不为空，内容为空，又得输入一次标题
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //判断输入不能为空
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }



        //从indexContoller页面复制过来的，为了获取user信息。
        User user = null;
        Cookie[] cookies = request.getCookies();
        //如果cookies不为空，循环之
        if(cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if(user == null){
            model.addAttribute("error","用户未登录");
            //如果用户未登录，不能发布，返回发布页面
            return "publish";
        }
        //往Question类中加信息
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        //将Question类中的信息插入数据库
        questionMapper.create(question);
        //正常反应：如果发布的问题插入数据库成功，返回首页，看看刚才发布的内容
        return "redirect:/";
    }
}
