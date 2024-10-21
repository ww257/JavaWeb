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