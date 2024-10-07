# 1.会话安全性

## 1.1.会话劫持

### 定义

​	会话劫持（Session hijacking），这是一种通过获取用户Session ID后，使用该Session ID登录目标账号的攻击方法，此时攻击者实际上是使用了目标账户的有效Session。会话劫持的第一步是取得一个合法的会话标识来伪装成合法用户，因此需要保证会话标识不被泄漏。



### 类型

#### 跨站脚本（XSS）

#### 会话侧劫持/嗅探

#### 会话固定

#### 可预测的会话ID和暴力破解

#### 浏览器中的人（又名中间人攻击或恶意软件）



### 防御

#### 使用HTTPS

#### 依赖web框架进行会话cookie管理

#### 认证后更改会话密钥

#### 引入额外的身份验证区域

#### 引入入侵检测系统（IDS）和入侵保护系统（IPS）



## 1.2.跨站脚本攻击（XSS）

### 定义

​	跨站脚本攻击（Cross-site Scripting，通常称为XSS），是一种典型的Web程序漏洞利用攻击。攻击者利用Web程序对用户输入检查不足的漏洞将可执行恶意脚本注入网站或Web应用，当用户访问网页时触发恶意脚本的执行，从而达到窃取用户个人数据、弹出广告，甚至篡改网页内容等攻击目的。



### 类型

#### 反射型XSS

#### 存储型XSS

#### DOM型XSS



### 防御

#### 输入验证和输出编码

#### 内容安全策略（Content Security Policy, CSP）

#### HTTP 头部安全

#### 使用安全开发框架

#### 定期安全审计



## 1.3.跨站请求伪造（CSRF）

### 定义

​	跨站请求伪造（Cross-site request forgery），也被称为 one-click attack 或者 session riding，通常缩写为 CSRF 或者 XSRF， 是一种挟制用户在当前已登录的Web应用程序上执行非本意的操作的攻击方法。跟跨站脚本（XSS）相比，XSS 利用的是用户对指定网站的信任，CSRF 利用的是网站对用户网页浏览器的信任。



### 防御

#### 验证HTTP Referer字段

#### 在请求地址中添加Token并验证

#### 使用验证码



# 2.分布式会话管理

## 2.1.分布式环境下的会话同步问题

### 定义

​	在分布式环境中，会话同步问题是指多个应用实例或节点之间需要共享和保持会话状态的一致性。由于分布式系统的分布性，不同的节点可能独立地处理用户请求，并维护各自的会话状态。然而，当用户在多个节点之间切换时，需要确保他们的会话状态能够在这些节点之间同步，以保证用户体验的一致性和数据的完整性。



### 原因

1.**节点独立性**：

​	分布式系统中的每个节点都是独立的，它们可能各自维护一套会话状态，而缺乏统一的同步机制。

2.**网络延迟和故障**：

​	网络延迟和故障可能导致节点之间的通信不畅，从而影响会话状态的同步。

3.**数据一致性问题**：

​	在分布式系统中，由于多个节点可能同时访问和修改同一个数据，因此容易出现数据不一致的问题。



### 解决方案

1.**会话复制**：

​	将会话状态复制到多个节点上，以确保每个节点都能访问到最新的会话状态。然而，这种方法会增加网络的开销和节点的存储压力。

2.**会话集中存储**：

​	使用专门的存储系统（如Redis）来集中存储会话状态。所有节点都通过访问这个存储系统来获取和更新会话状态。这种方法可以确保会话状态的一致性，但需要额外的存储和维护成本。

3.**会话粘性**：

​	通过某种机制（如负载均衡器的会话粘性策略）将用户的请求始终路由到同一个节点上，从而避免会话状态在不同节点之间的切换。然而，这种方法可能会增加单个节点的负载压力，并降低系统的可扩展性。



## 2.2.Session集群解决方案

1.**粘性会话（Sticky Sessions）**：

优点：实现简单，不需要额外的配置。
缺点：如果服务器宕机，用户的Session会丢失，且负载均衡可能不均。

2.**复制会话（Session Replication）**：

优点：任何服务器都可以处理请求。
缺点：Session复制可能会导致网络开销大，且随着服务器数量的增加，复制开销也会增加。

3.**分布式缓存（Distributed Caching）**：

优点：支持大规模集群，性能好，可靠性高。
缺点：需要引入额外的缓存服务器，增加系统复杂度。

4.**客户端存储（Client-Side Storage）**：

优点：服务器无状态，易于扩展。
缺点：安全性较差，存储容量有限。

5.**数据库共享（Database Sharing）**：

优点：数据持久化，不容易丢失。
缺点：数据库可能成为性能瓶颈，且需要额外的数据库访问开销。

6.**基于Token的认证（Token-Based Authentication）**：

优点：无状态，服务器扩展性好，适合RESTful API。
缺点：Token长度可能较大，每次请求都需要携带，且Token一旦生成，无法在有效期内撤销。

## 2.3.使用Redis等缓存技术实现分布式会话

### 1.添加依赖

```
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 2.配置存储类型

```
spring:
	session:
		store-type: redis
```

### 3.添加注解@EnableRedisHttpSession

```
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30) //开启使用redis作为spring-session
public class FoodieApplication {
	public static void main(String[] args) {
        SpringApplication.run(FoodieApplication.class, args);
    }
}
```



# 3.会话状态的序列化和反序列化

## 3.1.会话状态的序列化和反序列化

### 概念

​	序列化是指将对象的状态信息转换为可以存储或传输的形式的过程。这通常涉及到将数据结构或对象转换成字节流或字符串格式。反序列化则是序列化的逆过程，即将序列化后的数据转换回原始的数据结构或对象。

### 区别

- **方向性**：序列化是数据从内存到存储/传输的转换，而反序列化则是从存储/传输到内存的转换。
- **目的性**：序列化用于数据的持久化和网络传输，反序列化用于数据的恢复和使用。

## 3.2.为什么需要序列化会话状态

1. **持久性存储**：	

   ​	对象可以被写入到硬盘上，以便在程序下次启动时重新加载。

2. **网络传输**：

   ​	对象可以通过网络发送到另一台机器。

3. **分布式系统**：

   ​	在分布式系统中，对象的序列化和反序列化是常见的操作。

## 3.3.Java对象序列化

1. **实现 `Serializable` 接口**：这个接口是一个标记性接口，不包含方法，实现此接口即可使类的对象序列化。

2. **定义 `serialVersionUID`**：这是一个唯一的版本标识符，用于确保序列化的对象和对应的类在结构上一致。

   ```
   import java.io.Serializable;
    
   public class User implements Serializable {
       private static final long serialVersionUID = 1L;
    
       private String name;
       private int age;
    
       public User(String name, int age) {
           this.name = name;
           this.age = age;
       }
    
       // Getters and setters
   }
   ```

   

## 3.4.自定义序列化策略

### 使用Externalizable接口

```
package cn.juwatech.serialization;

import java.io.*;

public class CustomSerializationWithExternalizable {

    public static void main(String[] args) {
        CustomUser user = new CustomUser("Bob", 25);

        // 序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("customUser.ser"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("customUser.ser"))) {
            CustomUser deserializedUser = (CustomUser) ois.readObject();
            System.out.println("反序列化后的用户: " + deserializedUser);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class CustomUser implements Externalizable {
    private String name;
    private int age;

    public CustomUser() {
        // 必须有无参构造函数
    }

    public CustomUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // 自定义序列化逻辑
        out.writeUTF(name);
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // 自定义反序列化逻辑
        name = in.readUTF();
        age = in.readInt();
    }

    @Override
    public String toString() {
        return "CustomUser{name='" + name + "', age=" + age + '}';
    }
}
```



### 自定义writeObject和readObject方法

```
package cn.juwatech.serialization;

import java.io.*;

public class CustomSerializationWithWriteObject {

    public static void main(String[] args) {
        SecureUser user = new SecureUser("Charlie", 28, "mySecretPassword");

        // 序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("secureUser.ser"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("secureUser.ser"))) {
            SecureUser deserializedUser = (SecureUser) ois.readObject();
            System.out.println("反序列化后的用户: " + deserializedUser);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class SecureUser implements Serializable {
    private String name;
    private int age;
    private transient String password; // transient修饰，不会默认序列化

    public SecureUser(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // 序列化非transient字段
        oos.writeObject(encrypt(password)); // 自定义序列化敏感数据
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // 反序列化非transient字段
        password = decrypt((String) ois.readObject()); // 自定义反序列化敏感数据
    }

    private String encrypt(String data) {
        // 简单加密示例，实际场景应使用更安全的加密算法
        return new StringBuilder(data).reverse().toString();
    }

    private String decrypt(String data) {
        // 简单解密示例
        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public String toString() {
        return "SecureUser{name='" + name + "', age=" + age + ", password='" + password + "'}";
    }
}
```



#### 

#### 





