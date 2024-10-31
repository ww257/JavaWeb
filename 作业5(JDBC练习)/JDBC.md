### JDBC练习

```java
CREATE TABLE `teacher` (
  `id` int NOT NULL COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `course` varchar(255) DEFAULT NULL COMMENT '课程',
  `birthday` date DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`)
);
```

**要求：**

1. **完成teacher的CRUD练习，提供CRUD的代码。**

2. **完成teacher表的批量插入练习，插入500个教师，每插入100条数据提交一次。**

3. **完成可滚动的结果集练习，只查看结果集中倒数第2条数据。**

4. **提交代码即可，但是代码中不要包含taget目录。**

   

#### 一、创建数据库

```sql
CREATE DATABASE jdbc_demo;
USE jdbc_demo;

CREATE TABLE `teacher` (
  `id` int NOT NULL COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `course` varchar(255) DEFAULT NULL COMMENT '课程',
  `birthday` date DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`)
);
```

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/1.png)



#### 二、创建maven工程

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/2.png)



#### 三、创建如图所示目录

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/3.png)



#### 四、创建JdbcCreate类

```java
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
```

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/4.png)

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/5.png)



#### 五、创建JdbcRead类

##### 1.读取id为24的教师信息

```java
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

```

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/6.png)

##### 2.可滚动的结果集

```java
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
```

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/7.png)

#### 

#### 六、创建JdbcUpdate类

```java
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

```

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/8.png)

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/9.png)



#### 七、创建JdbcDelete类

```java
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
```

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/10.png)

![](https://github.com/ww257/JavaWeb/blob/main/%E4%BD%9C%E4%B8%9A5(JDBC%E7%BB%83%E4%B9%A0)/images/11.png)
