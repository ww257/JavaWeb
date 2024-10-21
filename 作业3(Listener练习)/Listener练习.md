#### Listener练习

**题目：**完成请求日志记录（ServletRequestListener）功能
**要求：**

1. 实现一个 ServletRequestListener 来记录每个 HTTP 请求的详细信息。
2. 记录的信息应包括但不限于：
  ○ 请求时间
  ○ 客户端 IP 地址
  ○ 请求方法（GET, POST 等）
  ○ 请求 URI
  ○ 查询字符串（如果有）
  ○ User-Agent
  ○ 请求处理时间（从请求开始到结束的时间）
3. 在请求开始时记录开始时间，在请求结束时计算处理时间。
4. 使用适当的日志格式，确保日志易于阅读和分析。
5. 实现一个简单的测试 Servlet，用于验证日志记录功能。
6. 提供简要说明，解释你的实现方式和任何需要注意的事项。



##### 一、创建maven工程

![](https://github.com/ww257/JavaWeb/blob/main/作业3(Listener练习)/images/1.png)



##### 二、创建如图所示的目录

![](https://github.com/ww257/JavaWeb/blob/main/作业3(Listener练习)/images/2.png)



##### 三、创建`RequestLogger`类

```java
package com.gzu.listenerdemo;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这个类是一个ServletRequestListener的实现，用于记录对/test路径的HTTP请求的详细信息。
 */
@WebListener
public class RequestLogger implements ServletRequestListener {

    // 定义一个日期格式化对象，用于格式化时间戳
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 当请求被销毁时，这个方法会被调用。
     * 它计算请求的处理时间，并记录请求的详细信息。
     * @param sre ServletRequestEvent对象，包含请求的信息
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // 获取结束时间
        long endTime = System.currentTimeMillis();
        // 计算处理时间
        long processingTime = endTime - (Long) sre.getServletRequest().getAttribute("startTime");
        // 记录请求的详细信息
        logRequestDetails(sre, endTime, processingTime);
    }

    /**
     * 当请求被初始化时，这个方法会被调用。
     * 它设置请求的开始时间。
     * @param sre ServletRequestEvent对象，包含请求的信息
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        // 设置请求的开始时间为当前系统时间
        sre.getServletRequest().setAttribute("startTime", System.currentTimeMillis());
    }

    /**
     * 这个方法记录请求的详细信息，包括时间戳、客户端IP、请求方法、请求URI、查询字符串、User-Agent和处理时间。
     * @param sre ServletRequestEvent对象，包含请求的信息
     * @param endTime 请求结束的时间
     * @param processingTime 请求的处理时间
     */
    private void logRequestDetails(ServletRequestEvent sre, long endTime, long processingTime) {
        var request = sre.getServletRequest();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 只记录对/test路径的请求
        if (!httpRequest.getRequestURI().endsWith("/test")) {
            return;
        }

        // 获取并格式化当前时间
        String timestamp = DATE_FORMAT.format(new Date(endTime));
        // 获取客户端IP地址
        String remoteAddr = httpRequest.getRemoteAddr();
        // 获取请求方法
        String method = httpRequest.getMethod();
        // 获取请求URI
        String uri = httpRequest.getRequestURI();
        // 获取查询字符串，如果没有则为空字符串
        String query = httpRequest.getQueryString() != null ? httpRequest.getQueryString() : "";
        // 获取User-Agent
        String userAgent = httpRequest.getHeader("User-Agent");
        // 将处理时间转换为字符串
        String processingTimeMs = String.valueOf(processingTime);

        // 使用System.out.println打印日志信息
        System.out.println("时间戳: " + timestamp);
        System.out.println("客户端IP: " + remoteAddr);
        System.out.println("请求方法: " + method);
        System.out.println("请求URI: " + uri);
        System.out.println("查询字符串: " + query);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("处理时间(ms): " + processingTimeMs);
    }
}
```



##### 四、创建`TestServlet`类

```java
package com.gzu.listenerdemo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 这个类是一个Servlet，用于处理对/test路径的GET和POST请求。
 * 它验证了日志记录功能是否正常工作。
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    /**
     * 处理GET请求的方法。
     * 设置响应的字符编码和内容类型，然后向客户端输出一个简单的HTML页面。
     * @param req HttpServletRequest对象，包含客户端的请求信息
     * @param resp HttpServletResponse对象，用于向客户端发送响应
     * @throws ServletException 如果发生Servlet异常
     * @throws IOException 如果发生输入输出异常
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的字符编码为UTF-8
        resp.setCharacterEncoding("UTF-8");
        // 设置响应的内容类型为text/html，并指定字符编码为UTF-8
        resp.setContentType("text/html;charset=UTF-8");
        // 获取PrintWriter对象，用于向客户端输出内容
        PrintWriter out = resp.getWriter();
        // 输出一个简单的HTML页面
        out.println("这是一个简单的测试Servlet,用于验证日志记录功能");
    }

    /**
     * 处理POST请求的方法。
     * 这个方法直接调用doGet方法来处理POST请求，使得GET和POST请求的逻辑相同。
     * @param req HttpServletRequest对象，包含客户端的请求信息
     * @param resp HttpServletResponse对象，用于向客户端发送响应
     * @throws ServletException 如果发生Servlet异常
     * @throws IOException 如果发生输入输出异常
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```



##### 五、功能展示

1.访问`http://localhost:8080/listener_demo/test`

![](https://github.com/ww257/JavaWeb/blob/main/作业3(Listener练习)/images/3.png)



2.查看服务器记录的日志

![](https://github.com/ww257/JavaWeb/blob/main/作业3(Listener练习)/images/4.png)

