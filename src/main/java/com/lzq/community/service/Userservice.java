package com.lzq.community.service;

import com.lzq.community.mapper.UserMapper;
import com.lzq.community.model.User;
import com.lzq.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Userservice {

    @Autowired(required = false)
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //获取数据库的用户信息

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        //User dbUser = userMapper.findByAccountId(user.getAccountId());

        //根据accountId来判断，如果账户之前没登录过，则插入数据库
        if(dbUsers.size() == 0){
            //创建
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {//如果之前登录过，则更新相应的数据
            User dbUser = dbUsers.get(0);
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            dbUser.setGmtModified(System.currentTimeMillis());

            User updateUser = new User();
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            //userMapper.update(dbUser);
        }

    }
}
