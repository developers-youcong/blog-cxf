package com.blog.cxf.server.config;

import com.blog.cxf.server.interceptor.AuthInterceptor;
import com.blog.cxf.server.interceptor.LogInInterceptor;
import com.blog.cxf.server.service.PostService;
import com.blog.cxf.server.service.UserService;
import com.blog.cxf.server.service.impl.PostServiceImpl;
import com.blog.cxf.server.service.impl.UserServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:37
 */
@Configuration
public class CxfConfig {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private LogInInterceptor logInInterceptor;

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public PostService postService() {
        return new PostServiceImpl();
    }

    /**
     * 发布服务并指定访问URL
     * @return
     */
    @Bean
    public EndpointImpl userEnpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), userService());
        endpoint.getInInterceptors().add(authInterceptor);
        endpoint.getInInterceptors().add(logInInterceptor);
        endpoint.publish("/user");
        return endpoint;
    }

    /**
     * 发布服务并指定访问URL
     * @return
     */
    @Bean
    public EndpointImpl postEnpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), postService());
        endpoint.publish("/post");
        return endpoint;
    }
}