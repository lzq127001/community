package com.lzq.community.controller;

import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        //如果cookies不为空，循环之
        if(cookies != null) {
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

        return "index";
    }
}
