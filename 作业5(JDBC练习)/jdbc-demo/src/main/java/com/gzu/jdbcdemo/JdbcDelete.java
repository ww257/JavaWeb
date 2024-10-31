package com.gzu.jdbcdemo;

import java.sql.*;

public class JdbcDelete {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "c20021217";

        // 准备SQL删除语句，使用参数化查询来防止SQL注入
        String sql = "DELETE FROM teacher WHERE id = ?";

        // 使用try-with-resources语句自动关闭资源
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置删除操作的参数，这里我们删除ID为300的教师记录
            int idToDelete = 99;
            pstmt.setInt(1, idToDelete); // 第一个问号(?)对应的参数索引为1

            // 执行删除操作，并返回受影响的行数
            int affectedRows = pstmt.executeUpdate();

            // 根据受影响的行数判断删除操作是否成功
            if (affectedRows > 0) {
                System.out.println("删除成功，删除了 " + affectedRows + " 条记录。");
            } else {
                System.out.println("删除失败，没有找到ID为 " + idToDelete + " 的教师记录。");
            }

        } catch (SQLException e) {
            // 打印SQL异常信息
            e.printStackTrace();
        }
    }
}