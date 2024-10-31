package com.gzu.jdbcdemo;

import java.sql.*;

public class JdbcScrollableResultSet {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "c20021217";

        // SQL查询语句
        String sql = "SELECT * FROM teacher";

        // 数据库连接、声明和结果集变量
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. 注册JDBC驱动程序（在新版JDBC中，这一步通常是自动的，不需要显式调用）
            // Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 打开连接
            conn = DriverManager.getConnection(url, user, password);

            // 3. 执行查询并创建可滚动的、只读的ResultSet
            // ResultSet.TYPE_SCROLL_INSENSITIVE: 允许向前和向后滚动，对数据库变化不敏感
            // ResultSet.CONCUR_READ_ONLY: 结果集是只读的
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

            // 4. 处理结果集
            // 移动到结果集的最后一行，获取总行数
            rs.last();
            int totalRows = rs.getRow(); // 获取当前行号（即总行数）

            // 移动到倒数第二行
            if (totalRows >= 2) {
                rs.absolute(totalRows - 1); // 定位到倒数第二行
            } else {
                System.out.println("结果集中没有足够的数据。");
                return;
            }

            // 从结果集中提取数据
            int teacherId = rs.getInt("id");
            String name = rs.getString("name");
            String course = rs.getString("course");
            Date birthday = rs.getDate("birthday");

            // 打印数据
            System.out.println("ID: " + teacherId);
            System.out.println("Name: " + name);
            System.out.println("Course: " + course);
            System.out.println("Birthday: " + birthday);

        } catch (SQLException e) {
            // 5. 处理JDBC错误
            e.printStackTrace();
        } finally {
            // 6. 清理环境
            try {
                if (rs != null) {
                    rs.close(); // 关闭结果集
                }
                if (stmt != null) {
                    stmt.close(); // 关闭声明
                }
                if (conn != null) {
                    conn.close(); // 关闭连接
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }
}