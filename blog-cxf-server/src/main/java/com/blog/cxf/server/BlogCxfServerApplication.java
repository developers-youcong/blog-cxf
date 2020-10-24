package com.blog.cxf.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:30
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BlogCxfServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogCxfServerApplication.class, args);
        System.out.println("====启动Blog Cxf Server====");

    }
}