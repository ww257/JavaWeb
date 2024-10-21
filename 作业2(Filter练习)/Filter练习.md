### Filter练习

**题目:** 实现一个登录验证过滤器

**目标:** 创建一个 Servlet的过滤器,用于验证用户是否已登录。对于未登录的用户,将其**重定向**到登录页面。

**要求:** 

1. 创建一个名为 `LoginFilter` 的类, 实现 `javax.servlet.Filter` 接口。

2. 使用 `@WebFilter` 注解配置过滤器,使其应用于所有 URL 路径 ("/*")。

3. 在 `doFilter` 方法中实现以下逻辑: 

   a.检查当前请求是否是对登录页面、注册页面或公共资源的请求。如果是,则允许请求通过。 

   b.如果不是上述情况,检查用户的 session 中是否存在表示已登录的属性(如 "user" 属性)。

   c.如果用户已登录,允许请求继续。 

   d.如果用户未登录,将请求重定向到登录页面。

4. 创建一个排除列表,包含不需要登录就能访问的路径(如 "/login", "/register", "/public")。

5. 实现一个方法来检查当前请求路径是否在排除列表中。

6. 添加适当的注释,解释代码的主要部分。

**提示：**简单的流程图

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A2(Filter%E7%BB%83%E4%B9%A0)/images/1.png)



##### 一、创建maven工程

![](D:\作业\JavaWeb\作业2(Filter练习)\images\2.png)



##### 二、创建如图所示的目录

![](D:\作业\JavaWeb\作业2(Filter练习)\images\3.png)



##### 三、创建LoginFilter类

```
package com.gzu.filterdemo;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// 使用 @WebFilter 注解配置过滤器，使其应用于所有 URL 路径 ("/*")
@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {

    // 创建排除列表，包含不需要登录就能访问的路径
    private static final Set<String> EXCLUDED_PATHS = new HashSet<>(Arrays.asList(
            "/login", "/register", "/public"
    ));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 将请求和响应转换为 HttpServletRequest 和 HttpServletResponse
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取当前请求的路径
        String requestURI = httpRequest.getRequestURI();

        // 检查当前请求是否在排除列表中
        if (isExcludedPath(requestURI)) {
            // 如果在排除列表中，允许请求通过
            chain.doFilter(request, response);
        } else {
            // 获取当前会话
            HttpSession session = httpRequest.getSession(false);

            // 检查用户的 session 中是否存在表示已登录的属性（如 "user" 属性）
            if (session != null && session.getAttribute("user") != null) {
                // 如果用户已登录，允许请求继续
                chain.doFilter(request, response);
            } else {
                // 如果用户未登录，将请求重定向到登录页面
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            }
        }
    }

    // 实现方法来检查当前请求路径是否在排除列表中
    private boolean isExcludedPath(String requestURI) {
        for (String excludedPath : EXCLUDED_PATHS) {
            if (requestURI.endsWith(excludedPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // 销毁过滤器
    }
}
```



##### 四、创建LoginServlet类

```
package com.gzu.filterdemo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login") // 定义Servlet的访问URL
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取表单数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 登录验证逻辑
        if ("ljm".equals(username) && "123".equals(password)) {
            // 登录成功，将用户信息存储在session中
            HttpSession session = req.getSession();
            session.setAttribute("user", username);

            // 重定向到index.jsp页面
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            // 登录失败，设置错误消息
            req.setAttribute("errorMessage", "用户名或者密码错误，请重新登录");
            // 转发回登录页面，显示错误消息
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 对于GET请求，转发到登录页面
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}

```



##### 五、创建login.jsp

```
<%--
  Created by IntelliJ IDEA.
  User: Nathan995
  Date: 2024/10/13
  Time: 21:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>登录</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: Arial, sans-serif;
        }
        .login-container {
            width: 300px;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            background-color: #f7f7f7;
        }
        .login-container h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .error-message {
            text-align: center;
            color: red;
            margin-bottom: 15px;
        }
        .login-form div {
            margin-bottom: 15px;
        }
        .login-form label {
            display: block;
            margin-bottom: 5px;
        }
        .login-form input[type="text"],
        .login-form input[type="password"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .login-form button {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 4px;
            background-color: #6b9ce3;
            color: white;
            cursor: pointer;
        }
        .login-form button:hover {
            background-color: #1862bf;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>登录页面</h2>
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>
    <form action="login" method="post" class="login-form">
        <div>
            <label for="username">用户名:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div>
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div>
            <button type="submit">登录</button>
        </div>
    </form>
    <p>测试 <a href="register.jsp">注册</a></p>
    <p>测试 <a href="public.jsp">公开页面</a></p>
</div>

</body>
</html>
```



##### 六、创建pubilc.jsp

```
<%--
  Created by IntelliJ IDEA.
  User: Nathan995
  Date: 2024/10/13
  Time: 21:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>公开页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #333;
        }
        p {
            line-height: 1.6;
        }
    </style>
</head>
<body>
<h1>欢迎来到公开页面</h1>
<p>这是一个任何人都可以访问的页面。</p>
<p>你可以在这里放置一些公共信息或内容。</p>
<p>如果你还没有注册或登录，请通过以下链接进行：<br>
    <a href="register.jsp">注册</a> |
    <a href="login.jsp">登录</a>
</p>
</body>
</html>

```



##### 七、创建register.jsp

```
<%--
  Created by IntelliJ IDEA.
  User: Nathan995
  Date: 2024/10/13
  Time: 22:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>注册</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: Arial, sans-serif;
        }
        .register-container {
            width: 300px;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            background-color: #f7f7f7;
        }
        .register-container h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .error-message {
            text-align: center;
            color: red;
            margin-bottom: 15px;
        }
        .register-form div {
            margin-bottom: 15px;
        }
        .register-form label {
            display: block;
            margin-bottom: 5px;
        }
        .register-form input[type="text"],
        .register-form input[type="password"],
        .register-form input[type="email"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .register-form button {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 4px;
            background-color: #6b9ce3;
            color: white;
            cursor: pointer;
        }
        .register-form button:hover {
            background-color: #1862bf;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h2>注册页面</h2>
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>
    <form action="register" method="post" class="register-form">
        <div>
            <label for="username">用户名:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div>
            <label for="email">邮箱:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div>
            <label for="password">密码:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div>
            <label for="confirmPassword">确认密码:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>
        <div>
            <button type="submit">注册</button>
        </div>
    </form>
    <p>已经有账户了？ <a href="login.jsp">登录</a></p>
</div>
</body>
</html>

```



##### 八、功能展示

1.登录页面：

![](D:\作业\JavaWeb\作业2(Filter练习)\images\4.png)

2.输入用户：cjp，密码：wyl

![](D:\作业\JavaWeb\作业2(Filter练习)\images\5.png)

![](D:\作业\JavaWeb\作业2(Filter练习)\images\6.png)

3.注册页面

![](D:\作业\JavaWeb\作业2(Filter练习)\images\7.png)

4.公开页面

![](D:\作业\JavaWeb\作业2(Filter练习)\images\8.png)
