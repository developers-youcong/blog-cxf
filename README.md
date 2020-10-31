## SpringBoog整合Apache CXF

### 项目

#### 例子说明
1.客户端和服务端实现通信(服务端发布WebService，客户端通过RestFul进行请求);
2.增加了webservic安全校验机制(Token鉴权、签名校验、IP白名单等)。

博客文章:

[SpringBoot整合Apache CXF](https://developers-youcong.github.io/2020/10/24/SpringBoot%E6%95%B4%E5%90%88Apache-CXF%E5%AE%9E%E8%B7%B5/)
[WebService安全机制的思考与实践](https://developers-youcong.github.io/2020/10/31/WebService%E5%AE%89%E5%85%A8%E6%9C%BA%E5%88%B6%E7%9A%84%E6%80%9D%E8%80%83%E4%B8%8E%E5%AE%9E%E8%B7%B5/)


#### 目录结构
-- blog-cxf

---- blog-cxf-client(客户端)

---- blog-cxf-server(服务端)