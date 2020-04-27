
@[toc](Spring Cloud 服务互相调用问题)
# 1、问题描述

首先来第一个问题
```java

Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Mon Apr 27 22:09:19 CST 2020
There was an unexpected error (type=Internal Server Error, status=500).
No instances available for eureka-client-service
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200427224301527.png)



明明服务已经启动了啊，访问相应的服务怎么还出异常呢？这是由于服务刚刚启动，`Eureka Server`还没有和客户端建立连接，而且`Eureka Client`默认配置几十秒才去拉取注册表。因此使用默认配置服务启动到可以使用得几分钟。在生产环境是万万不行的，一定要修改相关配置。好了废话不多说。等会再访问呢？不出意外的话，下一个异常来了。

```java
2020-04-27 22:03:26.398 ERROR 11992 --- [nio-8765-exec-3] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://eureka-client-service/hello/eureka-client-client": client; nested exception is java.net.UnknownHostException: client] with root cause

java.net.UnknownHostException: client
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:184) ~[na:1.8.0_191]
	at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172) ~[na:1.8.0_191]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_191]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_191]
	at java.net.Socket.connect(Socket.java:538) ~[na:1.8.0_191]
	at sun.net.NetworkClient.doConnect(NetworkClient.java:180) ~[na:1.8.0_191]
	at sun.net.www.http.HttpClient.openServer(HttpClient.java:463) ~[na:1.8.0_191]
	at sun.net.www.http.HttpClient.openServer(HttpClient.java:558) ~[na:1.8.0_191]
	at sun.net.www.http.HttpClient.<init>(HttpClient.java:242) ~[na:1.8.0_191]
	at sun.net.www.http.HttpClient.New(HttpClient.java:339) ~[na:1.8.0_191]
	at sun.net.www.http.HttpClient.New(HttpClient.java:357) ~[na:1.8.0_191]
	at sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1220) ~[na:1.8.0_191]
	at sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1156) ~[na:1.8.0_191]
	at sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1050) ~[na:1.8.0_191]
	at sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:984) ~[na:1.8.0_191]
	at org.springframework.http.client.SimpleBufferingClientHttpRequest.executeInternal(SimpleBufferingClientHttpRequest.java:76) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:48) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:53) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.http.client.InterceptingClientHttpRequest$InterceptingRequestExecution.execute(InterceptingClientHttpRequest.java:109) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory.lambda$createRequest$0(LoadBalancerRequestFactory.java:61) ~[spring-cloud-commons-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.execute(RibbonLoadBalancerClient.java:144) ~[spring-cloud-netflix-ribbon-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.execute(RibbonLoadBalancerClient.java:125) ~[spring-cloud-netflix-ribbon-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.execute(RibbonLoadBalancerClient.java:99) ~[spring-cloud-netflix-ribbon-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor.intercept(LoadBalancerInterceptor.java:58) ~[spring-cloud-commons-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.http.client.InterceptingClientHttpRequest$InterceptingRequestExecution.execute(InterceptingClientHttpRequest.java:93) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.http.client.InterceptingClientHttpRequest.executeInternal(InterceptingClientHttpRequest.java:77) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:48) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:53) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:739) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:674) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.client.RestTemplate.getForEntity(RestTemplate.java:342) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at com.example.eureka_client_client.controller.AskController.ask(AskController.java:44) ~[classes/:na]
	at sun.reflect.GeneratedMethodAccessor53.invoke(Unknown Source) ~[na:na]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_191]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_191]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:879) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:793) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:634) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.5.RELEASE.jar:5.2.5.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:373) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1594) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_191]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_191]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.33.jar:9.0.33]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_191]


Process finished with exit code -1

```
# 2、问题分析
&emsp;&emsp;这是由于本地`hosts`文件没有配置映射，有人说是负载均衡的问题，但是我明明配置了负载均衡还是出问题，但是配置了`hosts`映射就好了。

# 3、问题解决
&emsp;&emsp;修改`C:\Windows\System32\drivers\etc\hosts`。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200427224107945.png)


在`hosts`文件中添加`127.0.0.1  client`，这个`client`要和`server-service`配置中一致。


![在这里插入图片描述](https://img-blog.csdnimg.cn/20200427224119818.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200427224128546.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMyNTEwNTk3,size_16,color_FFFFFF,t_70)

保存之后再访问即可。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200427224155220.png)


然后读者可以尝试将`hosts`文件中刚刚添加的`127.0.0.1  client`删除。保存再访问，还是可以获取正确结果。这是怎么回事呢？这就是`Eureka Server`大名鼎鼎的延时问题，读者可以等个几分钟再尝试重新请求，发现会报错。所以还是那句话，生产环境中一定要配置`Eureka Server`的参数。

# 4、工程代码
为了方便读者排错，我把我的代码贴出来，如果以上可以解决问题，那就更好。这段直接跳过即可。
直接贴`GitHub`链接吧! [https://github.com/androidkaifa1/eureka](https://github.com/androidkaifa1/eureka)

# 5、总结
&emsp;&emsp;书上的代码直接运行绝大部分是对的，但是总有一些软件的更新使得作者无能为力。之前的API是对的，但是之后就废弃了或修改了是常有的事。所以我们需要跟踪源代码。这只是一个小小的问题，如果没有前辈的无私奉献，很难想象我们自己一天能学到多少内容。<font color=green size=5>感谢各位前辈的辛勤付出，让我们少走了很多的弯路！</font>

<font color=red size=20>点个赞再走呗！欢迎留言哦！</font>

