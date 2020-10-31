package com.cxf.test;

import com.blog.cxf.server.service.UserService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/31 19:49
 */
public class TestClient01 {



    public static void main(String[] args) {
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

            int count = userService.addUser("test@163.com", "test", "test");

            System.out.println("count:"+count);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
