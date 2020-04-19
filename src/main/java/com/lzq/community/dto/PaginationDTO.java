package com.lzq.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
//questionDTO类和页面页数的集合
@Data
public class PaginationDTO {

    private List<QuestionDTO> questions;
    private boolean showPrevious; //前一页
    private boolean showFirstPage; //第一页
    private boolean showNext; //下一页
    private boolean showEndPage; //最后一页
    private Integer page;//当前页数
    private List<Integer> pages = new ArrayList<>(); //页数列表
    private Integer totalPage;//计算页面总数

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.page = page;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        pages.add(page);
        for(int i = 1; i <= 3; i++){
            if (page - i > 0){//page前面的页数
                pages.add(0, page - i);
            }
            if (page + i <= totalPage){//page后面的页数
                pages.add(page + i);
            }
        }
        //是否展示上一页
        if(page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }
        //是否展现下一页
        if (page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }
        //是否展现第一页
        if (pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
        //是否展现最后一页
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }

    }

}
