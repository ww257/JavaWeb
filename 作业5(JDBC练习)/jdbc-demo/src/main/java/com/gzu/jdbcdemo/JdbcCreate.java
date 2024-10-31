package com.gzu.jdbcdemo;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JdbcCreate {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/jdbc_demo?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "c20021217";

        // SQL插入语句，使用预编译语句（PreparedStatement）防止SQL注入
        String sql = "INSERT INTO teacher (id, name, course, birthday) VALUES (?, ?, ?, ?)";

        // 使用try-with-resources语句自动关闭资源
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 关闭自动提交，以手动控制事务
            conn.setAutoCommit(false);

            // 定义日期格式化器，用于解析和格式化日期
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate baseDate = LocalDate.parse("1990-10-02", formatter); // 设定基础日期

            // 循环插入500条数据
            for (int i = 1; i <= 500; i++) {
                // 设置SQL语句中的参数值
                pstmt.setInt(1, i);
                pstmt.setString(2, "Teacher" + i);
                pstmt.setString(3, "Course" + i);
                LocalDate birthday = baseDate.plusDays(i - 1); // 计算每个教师的生日
                pstmt.setDate(4, Date.valueOf(birthday)); // 将LocalDate转换为java.sql.Date

                // 将SQL语句添加到批处理中
                pstmt.addBatch();

                // 每100条或最后一条数据执行一次批处理，并检查是否有失败
                if (i % 100 == 0 || i == 500) {
                    int[] updateCounts = pstmt.executeBatch();

                    boolean hasFailure = false;
                    for (int updateCount : updateCounts) {
                        // 如果某条语句执行失败，updateCount将返回Statement.EXECUTE_FAILED的负值
                        if (updateCount == Statement.EXECUTE_FAILED) {
                            hasFailure = true;
                            break;
                        }
                    }

                    // 根据执行结果输出相应的信息
                    if (hasFailure) {
                        System.out.println("部分插入操作失败");
                    } else {
                        System.out.println("当前批次插入成功");
                    }

                    // 提交事务
                    conn.commit();
                }
            }

            // 插入完成后，重新开启自动提交模式
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            // 捕获并打印SQL异常
            e.printStackTrace();
        }
    }
}