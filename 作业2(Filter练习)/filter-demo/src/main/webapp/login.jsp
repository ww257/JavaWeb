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