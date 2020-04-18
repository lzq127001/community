package com.lzq.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private long id;
    private String bio;
    private String avatarUrl; //fastjson能自动把下划线avatar_url识别成驼峰形式
}
