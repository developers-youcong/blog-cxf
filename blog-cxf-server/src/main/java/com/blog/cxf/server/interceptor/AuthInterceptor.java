package com.blog.cxf.server.interceptor;

import cn.hutool.core.util.StrUtil;
import com.blog.cxf.server.security.SecretKey;
import com.blog.cxf.server.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @description: 认证鉴权拦截器
 * @author: youcong
 * @time: 2020/10/31 17:07
 */
@Slf4j
@Component
public class AuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {


    public AuthInterceptor() {
        super(Phase.PRE_INVOKE);
    }


    public void handleMessage(SoapMessage msg) throws Fault {


        Message ipVerify = PhaseInterceptorChain.getCurrentMessage();

        HttpServletRequest request = (HttpServletRequest) ipVerify.get(AbstractHTTPDestination.HTTP_REQUEST);

        //处理IP
        handleIp(request);

        Header authHeader = null;
        //获取验证头
        List<Header> headers = msg.getHeaders();

        if (headers.isEmpty()) {
            throw new Fault(new Exception("请求头为空"));
        }


        for (Header h : headers) {

            log.info("h:" + h.getName().toString().contains("auth"));
            if (h.getName().toString().contains("auth")) {
                authHeader = h;
                break;
            } else {
                throw new Fault(new Exception("请求头需包含auth"));
            }

        }

        Element auth = (Element) authHeader.getObject();

        NodeList childNodes = auth.getChildNodes();

        Set<String> reqHeader = new HashSet<String>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            //处理节点
            handleNode(childNodes.item(i), reqHeader);
        }
        //处理请求Key
        handleSOAPReqHeader(reqHeader);


    }

    //处理IP
    private void handleIp(HttpServletRequest request) {


        String[] ip_arr = new String[]{"127.0.0.1", "192.168.52.50"};

        for (String str : ip_arr) {
            System.out.println("ip:" + str);
        }

        Set<String> ipSet = new HashSet<String>();

        for (String item : ip_arr) {

            ipSet.add(item);
            if (ipSet.contains(IpUtils.getIpAddr(request))) {
                log.info("合法IP:" + item);
            } else {
                throw new Fault(new Exception("非法IP"));
            }
        }


    }

    //处理节点
    private void handleNode(Node items, Set<String> reqHeader) {

        Node item = items;

        //存储请求头Key
        if (item.getLocalName() != null) {
            String str = new String(item.getLocalName());
            reqHeader.add(str);
        }

        //获取请求头token
        if (item.getNodeName().contains("token")) {
            String tokenValue = item.getTextContent();

            if (!StrUtil.isEmpty(tokenValue)) {

                if ("soap".equals(tokenValue)) {

                    log.info("token Value:" + tokenValue);
                } else {
                    throw new Fault(new Exception("token错误"));
                }

            } else {
                throw new Fault(new Exception("token不能为空"));
            }

        }

        //获取请求头sign
        if (item.getNodeName().contains("sign")) {

            String signValue = item.getTextContent();

            if (!StrUtil.isEmpty(signValue)) {

                //原数据
                String originData = "test_webservice_api_2020";

                try {

                    //比对签名
                    boolean verifySign = SecretKey.verifySign(originData, signValue);

                    log.info("verifySign:" + verifySign);

                    if (verifySign) {
                        log.info("sign Value:" + signValue);
                    } else {
                        throw new Fault(new Exception("签名错误"));
                    }
                } catch (Exception e) {
                    throw new Fault(new Exception("签名错误"));
                }


            } else {
                throw new Fault(new Exception("签名不能为空"));
            }
        }
    }

    //处理SOAP请求头Key
    private void handleSOAPReqHeader(Set<String> reqHeader) {

        if (reqHeader.contains("token")) {
            log.info("包含token");
        } else {
            throw new Fault(new Exception("请求头auth需包含token"));
        }

        if (reqHeader.contains("sign")) {
            log.info("包含sign");
        } else {
            throw new Fault(new Exception("请求头auth需包含sign"));
        }

    }


}

