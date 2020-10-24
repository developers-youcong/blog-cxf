package com.blog.cxf.server.service;

import com.blog.cxf.server.dto.UserReqDto;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:32
 */
@WebService(targetNamespace = "http://service.server.cxf.blog.com/")
public interface UserService {


    /**
     * 添加用户
     * @param email
     * @param username
     * @param password
     * @return
     */
    int addUser(@WebParam(name = "email") String email, @WebParam(name = "username") String username, @WebParam(name = "password") String password);


    /**
     * 更新用户信息
     * @param userReqDto
     * @return
     */
    int updateUser(@WebParam(name="user")UserReqDto userReqDto);
}
