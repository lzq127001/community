package com.lzq.community.controller;

import com.lzq.community.dto.AccessTokenDTO;
import com.lzq.community.dto.GithubUser;
import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.User;
import com.lzq.community.provider.GithutProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithutProvider githutProvider;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        //获取accesstoken需要的参数
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //获得accesstoken
        String accessToken = githutProvider.getAccessToken(accessTokenDTO);
        //通过获取的accesstoken去github获取用户信息
        GithubUser githubUser = githutProvider.getUser(accessToken);
        if(githubUser == null)
            System.out.println("从github接口获取到的用户信息为空");
        System.out.println("登录账户id：" + githubUser.getId());
        System.out.println("名字：" + githubUser.getName());

        if(githubUser != null){
            //登录成功，写cookie和session

            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);//将获取的值写入数据库

            //客户端创建cookie
            response.addCookie(new Cookie("token",token));

//            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        }else {
            //登录失败
            return "redirect:/";
        }
    }


}
