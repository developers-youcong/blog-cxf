package com.blog.cxf.server.dto;

import lombok.Data;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:56
 */
@Data
public class PostReqDto {

    private String postTitle;

    private String postContent;

    private String backgroupImg;

    private String categoryId;

    private String tagId;




}
