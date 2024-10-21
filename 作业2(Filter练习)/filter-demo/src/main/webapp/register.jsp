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
