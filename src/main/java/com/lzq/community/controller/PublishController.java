package com.lzq.community.controller;

import com.lzq.community.cache.TagCache;
import com.lzq.community.dto.QuestionDTO;
import com.lzq.community.mapper.QuestionMapper;
import com.lzq.community.model.Question;
import com.lzq.community.model.User;
import com.lzq.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            //从publish提交表单的post方式获取信息，写入Question类，再用QuestionMapper插入数据库
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("qid") Long qid,
            HttpServletRequest request,
            Model model) {

        //因为判断标题内容不能为空的时候会返回该页面，所以之前输入的标题就得继续保存，不然标题不为空，内容为空，又得输入一次标题
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
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

        //验证标签的合法性
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }

        //添加了拦截器，本来这里是需要验证是否登录的
        User user = (User)request.getSession().getAttribute("user");
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


        question.setId(qid);//第一次发布的时候是null，点编辑的时候有值

        //将Question类中的信息插入或更新到数据库
        questionService.createOrUpdate(question);


        //正常反应：如果发布的问题插入数据库成功，返回首页，看看刚才发布的内容
        return "redirect:/";
    }

    //点击编辑返回到问题编辑页面
    @GetMapping("/publish/{qid}")
    public String edit(@PathVariable("qid") Long qid,
                       Model model){

        QuestionDTO question = questionService.getById(qid);

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("qid",qid);
        model.addAttribute("tags", TagCache.get());

        return "publish";
    }
}
