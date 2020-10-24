package com.blog.cxf.client.controller;

import com.blog.cxf.server.dto.UserReqDto;
import com.blog.cxf.server.service.UserService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 23:37
 */
@RestController
@RequestMapping("/user")
public class UserApiController {


    @PostMapping("/add")
    public int add(@RequestParam String email, @RequestParam String username, @RequestParam String password) {

        try {
            // 接口地址
            String address = "http://127.0.0.1:9090/cxf/user?wsdl";
            // 代理工厂
            JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
            // 设置代理地址
            jaxWsProxyFactoryBean.setAddress(address);
            // 设置接口类型
            jaxWsProxyFactoryBean.setServiceClass(UserService.class);
            // 创建一个代理接口实现
            UserService userService = (UserService) jaxWsProxyFactoryBean.create();

            return userService.addUser(email, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
