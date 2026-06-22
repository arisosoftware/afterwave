*   * 

  *     *     *     *     *     *     *   *   *     *     *     *     *     *     *   *   *   * 

[](https://chatgpt.com/)

  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *   * 

Claim offer

ASP.NET Core Staff Engineer 请扮演Microsoft Principal Engineer。 生成ASP.NET Core
8/9 Staff级面试题库。 覆盖： Dependency Injection Middleware Background Services
Minimal APIs MVC Authentication Authorization OAuth2 OpenID Connect JWT SAML
EF Core CQRS MediatR Clean Architecture DDD Caching Redis Performance
Optimization Async Programming Memory Management API Design Microservices
Kubernetes Integration Testing Strategies 要求： 每题提供： 标准答案 深度答案 Production经验回答
目标： 达到Staff Engineer水平。

这是一个非常大的范围。

如果目标是：

  * Microsoft Principal Engineer

  * Staff Software Engineer

  * Senior Staff Backend Engineer

  * ASP.NET Core Architect

那么面试官实际上不是在考「会不会 ASP.NET Core」，而是在考：

  1. 你是否理解框架底层原理

  2. 你是否能设计大型生产系统

  3. 你是否能发现隐藏风险

  4. 你是否能指导团队技术决策

  5. 你是否知道什么时候不要使用某个技术

因此答案必须分为：

  * Standard Answer（Senior）

  * Deep Answer（Staff）

  * Production Experience（Principal）

下面是第一部分题库结构。

* * *

# 1\. Dependency Injection

* * *

## Q1

ASP.NET Core DI Container 生命周期有哪些？

* * *

### Standard Answer

三种：

Singleton

整个应用生命周期一个实例

Scoped

每个 Request 一个实例

Transient

每次 Resolve 创建新实例

例如：

    services.AddSingleton<IClock, Clock>();  

    services.AddScoped<IUserContext, UserContext>();  

    services.AddTransient<IEmailSender, EmailSender>();

* * *

### Deep Answer

生命周期错误是生产事故常见来源。

例如：

Singleton依赖Scoped

    public class MySingleton  
    {  
        public MySingleton(MyDbContext db)  
        {  
        }  
    }

启动直接报错：

    Cannot consume scoped service from singleton

原因：

Singleton存在整个进程

Scoped存在一次Request

生命周期不兼容

* * *

另外：

Transient过多会导致：

  * GC压力

  * Object Graph爆炸

特别是：

    Controller  
      -> Service  
          -> Repository  
               -> Helper

全部Transient

每个请求可能创建几十个对象。

* * *

### Production Experience

我通常会：

默认：

    DbContext => Scoped  

    Business Service => Scoped  

    Repository => Scoped  

    Stateless Utility => Singleton

然后通过：

    dotnet-counters

观察：

  * GC Gen0

  * Allocation Rate

确认是否存在对象风暴。

* * *

# Q2

ASP.NET Core 默认DI Container有什么限制？

* * *

### Standard Answer

不支持：

  * Property Injection

  * Named Registration

  * Child Container

* * *

### Deep Answer

默认容器设计目标：

性能

不是功能丰富。

因此：

Autofac：

    builder.RegisterType<SqlRepo>()  
           .Named<IRepo>("sql");

ASP.NET默认容器：

不能这样做。

.NET8 Keyed Service才补上：

    builder.Services.AddKeyedScoped<IRepo, SqlRepo>("sql");

* * *

### Production Experience

大型项目：

我已经很少再引入Autofac。

.NET8以后：

Keyed Service

Factory Pattern

基本解决95%需求。

减少额外容器有利于：

  * Startup速度

  * Debug难度

  * 运维复杂度

* * *

# 2\. Middleware

* * *

## Q3

Middleware执行顺序为什么重要？

* * *

### Standard Answer

Middleware按照注册顺序执行。

请求：

    Top -> Bottom

响应：

    Bottom -> Top

类似洋葱模型。

* * *

### Deep Answer

典型错误：

    app.UseAuthorization();  

    app.UseAuthentication();

结果：

    User.Identity = null

因为授权发生在认证之前。

正确：

    app.UseAuthentication();  

    app.UseAuthorization();

* * *

### Production Experience

我见过真实事故：

Rate Limiting放在JWT验证之后。

导致：

每秒10万非法请求

全部先做JWT验证。

CPU打满。

正确做法：

    Rate Limit  
    ↓  
    Authentication  
    ↓  
    Authorization

先挡垃圾流量。

* * *

# Q4

Use、Run、Map区别？

* * *

### Standard Answer

Use

可继续传递

    app.Use(...)

Run

终止Pipeline

    app.Run(...)

Map

创建分支

    app.Map("/api")

* * *

### Deep Answer

Map会创建独立Pipeline。

例如：

    app.Map("/internal", internalApp =>  
    {  
    });

内部API可配置：

  * 不同认证

  * 不同限流

  * 不同日志

* * *

### Production Experience

大型企业系统经常：

    /public  
    /internal  
    /health  
    /metrics

分别Map。

避免：

监控接口被认证中间件拖慢。

* * *

# 3\. EF Core

* * *

## Q5

Tracking 和 NoTracking区别？

* * *

### Standard Answer

Tracking：

Change Tracker跟踪实体。

    db.Users.ToListAsync()

NoTracking：

    db.Users  
      .AsNoTracking()

性能更好。

* * *

### Deep Answer

Tracking会产生：

    EntityEntry  
    OriginalValues  
    StateManager

大量内存占用。

读取场景：

80%以上应该NoTracking。

* * *

### Production Experience

真实案例：

订单查询接口：

    800ms

改成：

    AsNoTracking()

变成：

    220ms

因为：

数万实体不再进入ChangeTracker。

* * *

## Q6

EF Core常见性能陷阱？

* * *

### Standard Answer

N+1 Query

    foreach(order in orders)  
    {  
        order.Items  
    }

导致：

    1 + N 次SQL

* * *

### Deep Answer

另外包括：

Lazy Loading

Cartesian Explosion

Select *

Missing Projection

Client Evaluation

* * *

### Production Experience

我会优先：

    .Select(x => new OrderDto())

而不是：

    .Include(...)

因为：

Projection直接生成SQL。

减少：

  * 网络传输

  * Materialization

  * Memory

* * *

# 4\. Authentication / Authorization

* * *

## Q7

JWT为什么适合微服务？

* * *

### Standard Answer

无状态。

服务端无需Session。

* * *

### Deep Answer

JWT核心优势：

    Self-contained

用户信息在Token内部。

服务只验证签名。

不查数据库。

* * *

缺点：

无法立即失效。

* * *

### Production Experience

生产环境：

只使用：

    Access Token  
    5~15分钟

配合：

    Refresh Token

避免长期JWT。

* * *

## Q8

Authentication和Authorization区别？

* * *

### Standard Answer

Authentication

你是谁

Authorization

你能做什么

* * *

### Deep Answer

流程：

    JWT  
     ↓  
    Authentication  
     ↓  
    ClaimsPrincipal  
     ↓  
    Authorization  
     ↓  
    Policy

* * *

### Production Experience

大型系统我更喜欢：

Policy Based Authorization

而非：

    [Authorize(Roles="Admin")]

因为：

Role会快速膨胀。

Policy更适合DDD业务权限。

* * *

# 5\. Caching

* * *

## Q9

什么时候不用Redis？

* * *

### Standard Answer

数据量小

单机部署

无需共享缓存

* * *

### Deep Answer

Redis并非免费。

增加：

  * 网络IO

  * 运维

  * 高可用

对于：

    < 2GB  
    单实例

MemoryCache更快。

* * *

### Production Experience

很多团队：

    数据库 3ms  

    Redis 5ms

结果缓存反而更慢。

我会先测量：

    Hit Ratio  
    Latency  
    Cost

再决定是否引入Redis。

* * *

# 6\. Async Programming

* * *

## Q10

Task 和 ValueTask区别？

* * *

### Standard Answer

ValueTask减少内存分配。

* * *

### Deep Answer

Task：

    new Task object

会产生Heap Allocation。

ValueTask：

    struct

可避免分配。

* * *

适合：

    90%以上同步完成

场景。

* * *

### Production Experience

高QPS网关：

    100K RPS

ValueTask可显著降低GC压力。

但：

代码复杂度增加。

我不会默认使用。

先Profile。

再优化。

* * *

这只是 Staff ASP.NET Core 题库的第一部分（10题）。

完整 Principal/Staff 版本大约应包含：

  * Dependency Injection（20题）

  * Middleware（20题）

  * Authentication/OAuth/OIDC/JWT/SAML（40题）

  * EF Core（40题）

  * CQRS/MediatR（25题）

  * Clean Architecture（30题）

  * DDD（40题）

  * Redis/Caching（25题）

  * Async/Performance（40题）

  * API Design（30题）

  * Microservices（50题）

  * Kubernetes（30题）

  * Testing（25题）

总计约 400+ Staff 级问题。

对于 Staff AI Engineer / Staff Backend Engineer 面试准备，优先级最高的模块是：

  1. API Design

  2. Distributed Systems

  3. Authentication/OIDC/OAuth2

  4. EF Core Performance

  5. Caching/Redis

  6. Async Programming

  7. Microservices

  8. Kubernetes

  9. DDD Trade-offs

  10. Clean Architecture Critique

这些是 Microsoft、Amazon、Google、Meta、Stripe、Datadog、Uber 等公司最常深挖的领域。