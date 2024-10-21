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