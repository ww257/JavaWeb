package com.gzu.jdbcdemo;

import java.sql.*;

public class JdbcRead {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "c20021217";

        // SQL查询语句，使用占位符?来避免SQL注入
        String sql = "SELECT * FROM teacher WHERE id = ?";

        // 使用try-with-resources语句自动管理资源，确保数据库连接、PreparedStatement和ResultSet会被正确关闭
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置查询条件
            int teacherId = 24;
            pstmt.setInt(1, teacherId);

            // 执行查询
            try (ResultSet rs = pstmt.executeQuery()) {
                // 检查查询结果是否为空，并处理结果
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String course = rs.getString("course");
                    Date birthday = rs.getDate("birthday");

                    // 打印查询到的教师信息
                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Course: " + course);
                    System.out.println("Birthday: " + birthday);
                } else {
                    System.out.println("没有找到ID为 " + teacherId + " 的教师信息。");
                }
            }

        } catch (SQLException e) {
            // 打印SQL异常信息
            e.printStackTrace();
        }
    }
}
