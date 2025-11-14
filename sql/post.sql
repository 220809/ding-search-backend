create database if not exists your_db;

create table your_db.post
(
    id          bigint auto_increment comment '主键',
    title       varchar(256) default '标题'            not null comment '标题',
    content     text                                  not null comment '帖子内容',
    tags        varchar(512) default '[]'              not null comment '标签',
    author_id   bigint                                 null comment '作者id',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     tinyint      default 0                 not null comment '是否删除（0-未删除，1-已删除）',
    constraint post_pk
        primary key (id)
)
    comment '帖子';

INSERT INTO rekker.post (title, content, tags, author_id) VALUES
                                                               ('Java入门指南', 'Java作为一门经典的面向对象编程语言，以其"一次编写，到处运行"的特性深受开发者喜爱。本文将从环境搭建开始，详细讲解Java基础语法、数据类型、流程控制等核心概念。通过学习，您将能够编写简单的Java应用程序，理解面向对象编程的基本思想，为后续深入学习Spring框架打下坚实基础。无论您是编程新手还是有其他语言基础的开发者，这都是开启Java之旅的完美起点。', '["Java", "编程", "入门"]', 1),
                                                               ('SpringBoot实战教程', 'SpringBoot通过自动配置和起步依赖极大地简化了Spring应用的开发过程。本文将带您从零开始构建一个RESTful API项目，涵盖控制器编写、数据访问层配置、异常处理等关键环节。我们将使用内嵌Tomcat服务器，避免繁琐的部署配置，让您专注于业务逻辑开发。通过实际代码演示，您将掌握SpringBoot的核心特性和最佳实践。', '["Java", "SpringBoot", "实战"]', 2),
                                                               ('微服务架构设计原则', '在微服务架构中，如何划分服务边界是个关键问题。本文探讨了基于业务能力和服务粒度的划分策略，介绍了服务发现、配置管理、熔断机制等核心组件的选型考量。通过实际案例，分析微服务架构在弹性扩展、技术异构方面的优势，同时也讨论分布式事务、测试复杂度等挑战的应对方案。', '["微服务", "架构", "SpringCloud"]', 3),
                                                               ('Vue3组合式API详解', 'Vue3的组合式API为组件开发带来了全新的编程模式。与Options API相比，组合式API提供了更好的逻辑复用和类型推导支持。本文将通过对比分析，展示setup函数、ref、reactive等新特性的使用方法，并分享在大型项目中组织代码的最佳实践。', '["Vue", "前端", "JavaScript"]', 4),
                                                               ('Docker容器化部署指南', 'Docker通过容器化技术实现了应用与运行环境的解耦。本文从Dockerfile编写开始，讲解镜像构建、容器运行、网络配置等核心操作。我们将演示如何将传统的Web应用容器化，配置多容器编排，实现开发、测试、生产环境的一致性部署。', '["Docker", "部署", "容器"]', 5),
                                                               ('Redis缓存实战技巧', 'Redis不仅是缓存，更是高性能的数据结构服务器。本文重点介绍Redis在商品详情页缓存、会话存储、排行榜等场景的应用。通过性能对比测试，展示合理使用Redis带来的系统性能提升，并分享缓存穿透、雪崩等问题的解决方案。', '["Redis", "缓存", "性能优化"]', 6),
                                                               ('MySQL索引优化实践', '正确的索引设计是数据库性能的关键。本文通过执行计划分析，揭示索引的工作原理，介绍B+树索引、覆盖索引、最左前缀原则等核心概念。通过真实案例，展示如何识别慢查询，设计高效索引，避免索引失效的常见陷阱。', '["MySQL", "数据库", "性能优化"]', 7),
                                                               ('高并发系统架构设计', '面对百万级并发请求，系统架构需要如何设计？本文从负载均衡、缓存策略、数据库分库分表等角度，系统性地介绍高并发架构的核心要素。通过电商秒杀场景的案例分析，展示各个环节的技术选型和架构决策过程。', '["分布式", "高并发", "架构"]', 8),
                                                               ('React Hooks最佳实践', 'Hooks让函数组件具备了状态管理能力。本文深入探讨useState、useEffect、useContext等内置Hook的使用场景，分享自定义Hook的封装技巧。通过代码对比，展示Hooks如何简化组件逻辑，提高代码可读性和可维护性。', '["React", "前端", "Hooks"]', 9),
                                                               ('Linux性能排查指南', '当服务器出现性能问题时，如何快速定位瓶颈？本文介绍top、vmstat、iostat等常用监控工具的使用方法，分享CPU、内存、IO等资源瓶颈的排查思路。通过实际案例，演示从现象到根源的系统化排查流程。', '["Linux", "运维", "服务器"]', 10),
                                                               ('Spring Security权限管理', '在企业级应用中，安全是重中之重。Spring Security提供了完整的安全解决方案。本文演示如何配置认证授权、密码加密、会话管理等功能，介绍RBAC权限模型的实际应用，分享前后端分离项目的安全实践。', '["Spring", "安全", "Java"]', 11),
                                                               ('消息队列技术选型', 'Kafka和RabbitMQ各有优势，如何选择？本文从吞吐量、可靠性、延迟等维度对比两款消息队列，分析各自的适用场景。通过电商订单系统的案例，展示消息队列在解耦系统、流量削峰方面的实际价值。', '["Kafka", "RabbitMQ", "消息队列"]', 12),
                                                               ('Elasticsearch搜索优化', '构建高效的搜索引擎需要多方面的优化。本文介绍索引映射设计、分词器选型、查询DSL优化等技巧，分享集群部署和性能调优的经验。通过电商搜索场景，演示相关性排序和聚合分析的实现方法。', '["Elasticsearch", "搜索", "大数据"]', 13),
                                                               ('Git团队协作流程', '高效的团队协作需要规范的Git工作流。本文介绍Git Flow、GitHub Flow等流行工作流，分享分支管理、代码审查、CI集成的最佳实践。通过实际项目经验，展示如何避免代码冲突，提高团队开发效率。', '["Git", "团队协作", "版本控制"]', 14),
                                                               ('Kubernetes集群管理', 'K8s已经成为容器编排的事实标准。本文从集群部署开始，讲解Pod、Deployment、Service等核心资源的使用，分享监控日志、自动扩缩容的配置方法。通过微服务部署案例，展示K8s在生产环境中的实际应用。', '["Kubernetes", "容器", "运维"]', 15);