package com.blog.cxf.server.service;

import com.blog.cxf.server.dto.PostReqDto;
import com.blog.cxf.server.dto.UserReqDto;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:55
 */
@WebService(targetNamespace = "http://service.server.cxf.blog.com/")
public interface PostService {


    /**
     * 添加新文章
     * @param postReqDto
     * @return
     */
    int addPost(@WebParam(name="post")PostReqDto postReqDto);
}
