package com.blog.cxf.server.service.impl;

import com.blog.cxf.server.dto.UserReqDto;
import com.blog.cxf.server.service.UserService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:35
 */
@WebService(serviceName = "userService",//对外发布的服务名
        targetNamespace = "http://service.server.cxf.blog.com/",//指定你想要的名称空间，通常使用使用包名反转
        endpointInterface = "com.blog.cxf.server.service.UserService")
@Component
public class UserServiceImpl implements UserService {
    public int addUser(String email, String username, String password) {
        System.out.println("注册用户:"+email);
        return 1;
    }

    public int updateUser(UserReqDto userReqDto) {
        return 1;
    }
}
