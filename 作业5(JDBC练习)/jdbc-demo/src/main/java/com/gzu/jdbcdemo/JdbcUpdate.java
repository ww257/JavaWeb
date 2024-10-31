package com.gzu.jdbcdemo;

import java.sql.*;

public class JdbcUpdate {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "c20021217";

        // SQL更新语句，使用占位符?来避免SQL注入
        String sql = "UPDATE teacher SET name = ? WHERE id = ?";

        // 使用try-with-resources语句确保数据库连接和PreparedStatement在使用后自动关闭
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 准备更新所需的参数
            String newName = "Teacher995"; // 要更新的教师姓名
            int id = 257; // 要更新的教师ID

            // 设置PreparedStatement中的参数值
            // 第一个参数索引从1开始，对应SQL语句中的第一个?
            pstmt.setString(1, newName); // 设置教师姓名为newName
            pstmt.setInt(2, id); // 设置教师ID为id

            // 执行更新操作，返回受影响的行数
            int affectedRows = pstmt.executeUpdate();

            // 根据受影响的行数判断更新是否成功
            if (affectedRows > 0) {
                System.out.println("更新成功，影响了 " + affectedRows + " 行。");
                // 如果有多行受到影响，通常意味着SQL语句存在问题，因为ID应该是唯一的
            } else {
                System.out.println("更新失败，没有找到ID为 " + id + " 的教师记录。");
            }
        } catch (SQLException e) {
            // 捕获并处理SQL异常
            e.printStackTrace();
        }
    }
}
