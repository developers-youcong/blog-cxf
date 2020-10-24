package com.blog.cxf.server.service.impl;

import com.blog.cxf.server.dto.PostReqDto;
import com.blog.cxf.server.service.PostService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:58
 */
@WebService(serviceName = "postService",//对外发布的服务名
        targetNamespace = "http://service.server.cxf.blog.com/",//指定你想要的名称空间，通常使用使用包名反转
        endpointInterface = "com.blog.cxf.server.service.PostService")
@Component
public class PostServiceImpl implements PostService {
    public int addPost(PostReqDto postReqDto) {
        return 1;

    }
}
