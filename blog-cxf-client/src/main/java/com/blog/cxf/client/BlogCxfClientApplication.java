package com.blog.cxf.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 23:35
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BlogCxfClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogCxfClientApplication.class, args);
        System.out.println("====启动Blog Cxf Client====");

    }
}
