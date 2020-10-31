package com.blog.cxf.server.interceptor;

import cn.hutool.core.util.XmlUtil;
import com.blog.cxf.server.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @description: 日志拦截器
 * @author: youcong
 * @time: 2020/10/31 16:50
 */
@Slf4j
@Component
public class LogInInterceptor extends AbstractPhaseInterceptor<Message> {

    public LogInInterceptor() {
        super(Phase.RECEIVE);
    }

    public void handleMessage(Message message) throws Fault {


        try {

            Message msg = PhaseInterceptorChain.getCurrentMessage();

            HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);

            //请求URL
            log.info("url:" + request.getRequestURI());

            //请求IP
            log.info("ip:" + IpUtils.getIpAddr(request));

            String xml;
            InputStream is = message.getContent(InputStream.class);

            String encoding = (String) message.get(Message.ENCODING);
            xml = IOUtils.toString(is);


            log.info("请求报文为：" + xml);

            //将字符串信息转为文档节点(XML)
            Document doc = XmlUtil.parseXml(xml);

            //获取请求头 token value
            Object value = XmlUtil.getByXPath("//soapenv:Envelope//soapenv:Header//auth//token", doc, XPathConstants.STRING);

            log.info("value:" + value);

            message.setContent(InputStream.class, new ByteArrayInputStream(xml.getBytes(encoding)));
            message.getExchange().put("idtest", xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
