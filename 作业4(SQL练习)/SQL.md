#### `SQL`练习

**作业要求：至少做出25道员工信息练习题和40道学生选课题**

**作业提示：**排名可以使用`Rank()`窗口函数，结合 `CTX` 表达式实现，例如：练习中的第48题

```sql
WITH ranked_employees AS (
    SELECT e.*,
           d.dept_name,
           DENSE_RANK() OVER (PARTITION BY e.dept_id ORDER BY e.salary DESC) AS salary_rank
    FROM employees e
    JOIN departments d ON e.dept_id = d.dept_id
)
SELECT emp_id, first_name, last_name, dept_name, salary
FROM ranked_employees
WHERE salary_rank <= 3
ORDER BY dept_name, salary DESC;
```



#### 员工信息练习题(25道:1~25)

```sql
#1. 查询所有员工的姓名、邮箱和工作岗位。
SELECT first_name, email, job_title 
FROM employees;

#2. 查询所有部门的名称和位置。
SELECT dept_name, location 
FROM departments;

#3. 查询工资超过70000的员工姓名和工资。
SELECT first_name, salary 
FROM employees 
WHERE salary > 70000;

#4. 查询IT部门的所有员工。
SELECT * 
FROM employees 
WHERE dept_id = (SELECT dept_id FROM departments WHERE dept_name = 'IT');

#5. 查询入职日期在2020年之后的员工信息。
SELECT * 
FROM employees 
WHERE hire_date > '2020-12-31';

#6. 计算每个部门的平均工资。
SELECT dept_id, AVG(salary) AS 平均工资 
FROM employees 
GROUP BY dept_id;

#7. 查询工资最高的前3名员工信息。
SELECT * 
FROM employees 
ORDER BY salary DESC LIMIT 3;

#8. 查询每个部门员工数量。
SELECT dept_id, COUNT(*) AS 员工数量 
FROM employees 
GROUP BY dept_id;

#9. 查询没有分配部门的员工。
SELECT * 
FROM employees 
WHERE dept_id IS NULL;

#10. 查询参与项目数量最多的员工。
SELECT emp_id, COUNT(*) AS 项目数量最多 
FROM employee_projects 
GROUP BY emp_id 
ORDER BY num_projects DESC LIMIT 1;

#11. 计算所有员工的工资总和。
SELECT SUM(salary) AS 工资总和 
FROM employees;

#12. 查询姓"Smith"的员工信息。
SELECT * 
FROM employees 
WHERE last_name = 'Smith';

#13. 查询即将在半年内到期的项目。
SELECT * 
FROM projects 
WHERE end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 6 MONTH);

#14. 查询至少参与了两个项目的员工。
SELECT emp_id 
FROM employee_projects 
GROUP BY emp_id HAVING COUNT(*) >= 2;

#15. 查询没有参与任何项目的员工。
SELECT emp_id 
FROM employees 
WHERE emp_id NOT IN (SELECT emp_id FROM employee_projects);

#16. 计算每个项目参与的员工数量。
SELECT project_id, COUNT(*) AS 员工数量 
FROM employee_projects 
GROUP BY project_id;

#17. 查询工资第二高的员工信息。
SELECT * 
FROM employees 
ORDER BY salary DESC LIMIT 1,1;

#18. 查询每个部门工资最高的员工。
WITH ranked_employees AS (
    SELECT e.*,d.dept_name,
           RANK() OVER (PARTITION BY e.dept_id ORDER BY e.salary DESC) AS salary_rank
    FROM employees e
    JOIN departments d ON e.dept_id = d.dept_id
)
SELECT emp_id, first_name, last_name, dept_name, salary
FROM ranked_employees
WHERE salary_rank = 1
ORDER BY dept_name, salary DESC;

#19. 计算每个部门的工资总和,并按照工资总和降序排列。
SELECT dept_id, SUM(salary) AS 工资总和 
FROM employees 
GROUP BY dept_id 
ORDER BY total_salary DESC;

#20. 查询员工姓名、部门名称和工资。
SELECT e.first_name, d.dept_name, e.salary   
FROM employees e  
JOIN departments d ON e.dept_id = d.dept_id;

#21. 查询每个员工的上级主管(假设emp_id小的是上级)。
WITH ManagerCandidates AS (
    SELECT 
        emp_id,
        first_name,
        last_name,
        dept_id,
        MIN(emp_id) OVER (PARTITION BY dept_id) AS min_emp_id
    FROM employees
)
SELECT 
    e1.emp_id AS 员工编号,
    CONCAT(e1.first_name, ' ', e1.last_name) AS 员工姓名,
    COALESCE(e2.emp_id, NULL) AS 上级主管编号,
    COALESCE(CONCAT(e2.first_name, ' ', e2.last_name), NULL) AS 上级主管姓名
FROM employees e1
LEFT JOIN ManagerCandidates e2 ON e1.dept_id = e2.dept_id AND e1.emp_id != e2.min_emp_id AND e2.emp_id = e2.min_emp_id
WHERE e1.emp_id != e2.min_emp_id OR e1.emp_id IS NULL;

#22. 查询所有员工的工作岗位,不要重复。
SELECT DISTINCT job_title 
FROM employees;

#23. 查询平均工资最高的部门。
SELECT dept_id, AVG(salary) AS 平均工资最高的部门 
FROM employees   
GROUP BY dept_id   
ORDER BY avg_salary DESC   
LIMIT 1;

#24. 查询工资高于其所在部门平均工资的员工。
SELECT e.emp_id, CONCAT(e.first_name," ", e.last_name), e.salary, d.avg_salary
FROM employees e
JOIN (
    SELECT dept_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY dept_id
) d ON e.dept_id = d.dept_id
WHERE e.salary > d.avg_salary;

#25. 查询每个部门工资前两名的员工。
WITH ranked_employees AS (
    SELECT e.*, DENSE_RANK() OVER (PARTITION BY e.dept_id ORDER BY e.salary DESC) AS salary_rank
    FROM employees e
)
SELECT emp_id, CONCAT(first_name, " ",last_name),salary,dept_id
FROM ranked_employees
WHERE salary_rank <= 2;

```



#### 学生选课题(40道:5~44)

```sql
#5. 查询不同课程的平均分数。
SELECT course_id, AVG(score) AS 平均分数 
FROM score 
GROUP BY course_id;

#6. 查询每个学生的平均分数。
SELECT student_id, AVG(score) AS 平均分数
FROM score 
GROUP BY student_id;

#7. 查询分数大于85分的学生学号和课程号。
SELECT student_id, course_id 
FROM score 
WHERE score > 85;

#8. 查询每门课程的选课人数。
SELECT course_id, COUNT(*) AS 选课人数 
FROM score 
GROUP BY course_id;

#9. 查询选修了"高等数学"课程的学生姓名和分数。
SELECT s.name, sc.score 
FROM student s JOIN score sc ON s.student_id = sc.student_id 
WHERE sc.course_id = 'C001';

#10. 查询没有选修"大学物理"课程的学生姓名。
SELECT name 
FROM student 
WHERE student_id NOT IN (SELECT student_id FROM score WHERE course_id = 'C002');

#11. 查询C001比C002课程成绩高的学生信息及课程分数。
SELECT s.name, sc1.score AS C001成绩, sc2.score AS C002成绩
FROM student s
JOIN score sc1 ON s.student_id = sc1.student_id AND sc1.course_id = 'C001'
JOIN score sc2 ON s.student_id = sc2.student_id AND sc2.course_id = 'C002'
WHERE sc1.score > sc2.score;

#12. 统计各科成绩各分数段人数：课程编号，课程名称，[100-85]，[85-70]，[70-60]，[60-0] 及所占百分比
SELECT 
    c.course_id,
    c.course_name,
    COUNT(CASE WHEN sc.score BETWEEN 85 AND 100 THEN 1 END) AS score_100_85,
    COUNT(CASE WHEN sc.score BETWEEN 70 AND 84 THEN 1 END) AS score_85_70,
    COUNT(CASE WHEN sc.score BETWEEN 60 AND 69 THEN 1 END) AS score_70_60,
    COUNT(CASE WHEN sc.score < 60 THEN 1 END) AS score_60_0,
    ROUND((COUNT(CASE WHEN sc.score BETWEEN 85 AND 100 THEN 1 END) / COUNT(sc.student_id)) * 100, 2) AS percent_100_85,
    ROUND((COUNT(CASE WHEN sc.score BETWEEN 70 AND 84 THEN 1 END) / COUNT(sc.student_id)) * 100, 2) AS percent_85_70,
    ROUND((COUNT(CASE WHEN sc.score BETWEEN 60 AND 69 THEN 1 END) / COUNT(sc.student_id)) * 100, 2) AS percent_70_60,
    ROUND((COUNT(CASE WHEN sc.score < 60 THEN 1 END) / COUNT(sc.student_id)) * 100, 2) AS percent_60_0
FROM course c
JOIN score sc ON c.course_id = sc.course_id
GROUP BY c.course_id;

#13. 查询选择C002课程但没选择C004课程的成绩情况(不存在时显示为 null )。
SELECT s.student_id,s.name, sc.score
FROM student s
JOIN score sc ON s.student_id = sc.student_id AND sc.course_id = 'C002'
LEFT JOIN score sc2 ON s.student_id = sc2.student_id AND sc2.course_id = 'C004'
WHERE sc2.score IS NULL;

#14. 查询平均分数最高的学生姓名和平均分数。
WITH ranked_students AS (
    SELECT 
        s.name,
        AVG(sc.score) AS average_score,
        DENSE_RANK() OVER (ORDER BY AVG(sc.score) DESC) AS score_rank
    FROM student s
    JOIN score sc ON s.student_id = sc.student_id
    GROUP BY s.name
)
SELECT name, average_score
FROM ranked_students
WHERE score_rank = 1;

#15. 查询总分最高的前三名学生的姓名和总分。
WITH ranked_students AS (
    SELECT 
        s.name,
        SUM(sc.score) AS total_score,
        DENSE_RANK() OVER (ORDER BY SUM(sc.score) DESC) AS total_rank
    FROM student s
    JOIN score sc ON s.student_id = sc.student_id
    GROUP BY s.student_id
)
SELECT name, total_score
FROM ranked_students
WHERE total_rank <= 3;

/*16. 查询各科成绩最高分、最低分和平均分。要求如下：
以如下形式显示：课程 ID，课程 name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率
及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列*/
SELECT 
    c.course_id,
    c.course_name,
    MAX(sc.score) AS 最高分,
    MIN(sc.score) AS 最低分,
    AVG(sc.score) AS 平均分,
    ROUND(SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS 及格率,
    ROUND(SUM(CASE WHEN sc.score BETWEEN 70 AND 79 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS 中等率,
    ROUND(SUM(CASE WHEN sc.score BETWEEN 80 AND 89 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS 优良率,
    ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) / COUNT(sc.student_id) * 100, 2) AS 优秀率,
    COUNT(sc.student_id) AS number_of_students
FROM course c
JOIN score sc ON c.course_id = sc.course_id
GROUP BY c.course_id
ORDER BY number_of_students DESC, c.course_id ASC;

#17. 查询男生和女生的人数。
SELECT gender, COUNT(*) AS 人数 
FROM student 
GROUP BY gender;

#18. 查询年龄最大的学生姓名。
SELECT name 
FROM student 
ORDER BY birth_date ASC LIMIT 1;

#19. 查询年龄最小的教师姓名。
SELECT name 
FROM teacher 
ORDER BY birth_date DESC LIMIT 1;

#20. 查询学过「张教授」授课的同学的信息。
SELECT s.*
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
JOIN teacher t ON c.teacher_id = t.teacher_id
WHERE t.name = '张教授';

#21. 查询查询至少有一门课与学号为"2021001"的同学所学相同的同学的信息 。
SELECT DISTINCT sr.student_id, s.name 
FROM score sr
JOIN student s ON sr.student_id = s.student_id
JOIN score sc ON s.student_id = sc.student_id AND sc.course_id IN (SELECT course_id FROM score WHERE student_id = '2021001')
WHERE sr.student_id <> '2021001';

#22. 查询每门课程的平均分数，并按平均分数降序排列。
SELECT course_id, AVG(score) AS 平均分数 
FROM score 
GROUP BY course_id 
ORDER BY avg_score DESC;

#23. 查询学号为"2021001"的学生所有课程的分数。
SELECT course_id, score 
FROM score 
WHERE student_id = '2021001';

#24. 查询所有学生的姓名、选修的课程名称和分数。
SELECT s.name, c.course_name, sc.score 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
JOIN course c ON sc.course_id = c.course_id;

#25. 查询每个教师所教授课程的平均分数。
SELECT t.name, t.title, AVG(sc.score) AS 平均分数 
FROM teacher t 
JOIN course c ON t.teacher_id = c.teacher_id 
JOIN score sc ON c.course_id = sc.course_id GROUP BY t.teacher_id;

#26. 查询分数在80到90之间的学生姓名和课程名称。
SELECT s.name, c.course_name 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
JOIN course c ON sc.course_id = c.course_id 
WHERE sc.score BETWEEN 80 AND 90;

#27. 查询每个班级的平均分数。
SELECT my_class, AVG(score) AS 平均分数 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
GROUP BY my_class;

#28. 查询没学过"王讲师"老师讲授的任一门课程的学生姓名。
SELECT name 
FROM student 
WHERE student_id NOT IN (
    SELECT student_id 
    FROM score 
    WHERE course_id IN (
        SELECT course_id 
        FROM course 
        WHERE teacher_id = 'T003'
    )
);

#29. 查询两门及其以上小于85分的同学的学号，姓名及其平均成绩 。
SELECT s.student_id, s.name, AVG(sc.score) AS 平均成绩 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
GROUP BY s.student_id 
HAVING COUNT(sc.course_id) >= 2 AND AVG(sc.score) < 85;

#30. 查询所有学生的总分并按降序排列。
SELECT student_id, SUM(score) AS 总分 
FROM score 
GROUP BY student_id 
ORDER BY total_score DESC;

#31. 查询平均分数超过85分的课程名称。
SELECT c.course_name 
FROM course c 
JOIN score sc ON c.course_id = sc.course_id 
GROUP BY c.course_id 
HAVING AVG(sc.score) > 85;

#32. 查询每个学生的平均成绩排名。
SELECT student_id, AVG(score) AS 平均成绩, 
RANK() OVER (ORDER BY AVG(score) DESC) AS rank 
FROM score 
GROUP BY student_id;

#33. 查询每门课程分数最高的学生姓名和分数。
SELECT c.course_name, s.name, MAX(sc.score) AS 分数最高 
FROM course c 
JOIN score sc ON c.course_id = sc.course_id 
JOIN student s ON sc.student_id = s.student_id 
GROUP BY c.course_id;

#34. 查询选修了"高等数学"和"大学物理"的学生姓名。
SELECT s.name 
FROM student s 
WHERE s.student_id IN (
    SELECT student_id 
    FROM score 
    WHERE course_id = 'C001') AND s.student_id IN (
        SELECT student_id 
        FROM score 
        WHERE course_id = 'C002'
    );

#35. 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩（没有选课则为空）。
SELECT s.student_id, s.name, c.course_id, c.course_name, sc.score, sc2.avg_score
FROM student s
LEFT JOIN score sc ON s.student_id = sc.student_id
LEFT JOIN course c ON sc.course_id = c.course_id
LEFT JOIN (
    SELECT student_id, AVG(score) AS avg_score
    FROM score
    GROUP BY student_id
) sc2 ON s.student_id = sc2.student_id
ORDER BY sc2.avg_score DESC, s.student_id;

#36. 查询分数最高和最低的学生姓名及其分数。
SELECT name, score 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
ORDER BY score DESC LIMIT 1
UNION
SELECT name, score 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
ORDER BY score ASC LIMIT 1;

#37. 查询每个班级的最高分和最低分。
SELECT my_class, MAX(score) AS 最高分, MIN(score) AS 最低分 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
GROUP BY my_class;

#38. 查询每门课程的优秀率（优秀为90分）。
SELECT course_id, COUNT(*) AS count_90, COUNT(student_id) AS 该课程总人数, ROUND(COUNT(*) / COUNT(student_id) * 100, 2) AS 优秀率 
FROM score 
WHERE score >= 90 
GROUP BY course_id;

#39. 查询平均分数超过班级平均分数的学生。
WITH class_avg AS (
    SELECT my_class, AVG(score) AS 平均分数 
    FROM student s 
    JOIN score sc ON s.student_id = sc.student_id 
    GROUP BY my_class
)
SELECT s.name, s.my_class, sc.score, ca.avg_score 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
JOIN class_avg ca ON s.my_class = ca.my_class 
WHERE sc.score > ca.avg_score;

#40. 查询每个学生的分数及其与课程平均分的差值。
SELECT s.name, c.course_name, sc.score, AVG(sc.score) OVER (PARTITION BY sc.course_id) AS 平均分, sc.score - AVG(sc.score) OVER (PARTITION BY sc.course_id) AS 差值 
FROM student s 
JOIN score sc ON s.student_id = sc.student_id 
JOIN course c ON sc.course_id = c.course_id;

#41. 查询至少有一门课程分数低于80分的学生姓名。
SELECT name 
FROM student 
WHERE student_id IN (
    SELECT student_id 
    FROM score 
    WHERE score < 80
);

#42. 查询所有课程分数都高于85分的学生姓名。
SELECT name 
FROM student 
WHERE NOT EXISTS (
    SELECT * 
    FROM score 
    WHERE student_id = student.student_id AND score < 85
);

#43. 查询查询平均成绩大于等于90分的同学的学生编号和学生姓名和平均成绩。
SELECT student_id, name, AVG(score) AS 平均成绩 
FROM student 
JOIN score ON student_id = score.student_id 
GROUP BY student_id 
HAVING 平均成绩 >= 90;

#44. 查询选修课程数量最少的学生姓名。
SELECT name 
FROM student 
WHERE student_id = (
    SELECT student_id 
    FROM score 
    GROUP BY student_id 
    ORDER BY COUNT(course_id) ASC LIMIT 1);
```



#### 