/*
 Navicat Premium Data Transfer

 Source Server         : cy
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : person_blog

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 16/06/2025 11:27:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `author_id` int NOT NULL COMMENT '发布者用户 ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逗号分隔的标签列表',
  `status` enum('DRAFT','PENDING','PUBLISHED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DRAFT',
  `category_id` int NULL DEFAULT NULL COMMENT '所属分类 ID',
  `view_count` int NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 7, 'ee', '', '11', 'PENDING', NULL, 0, '2025-05-21 11:40:15', '2025-05-21 11:40:15');
INSERT INTO `article` VALUES (2, 7, 'java', '<div>java代码</div>', 'Springboot', 'DRAFT', 1, 0, '2025-05-28 08:57:36', '2025-05-28 08:57:36');
INSERT INTO `article` VALUES (3, 7, 'java', '<div>Java哈哈哈</div>', 'Springboot', 'PENDING', 1, 0, '2025-05-28 08:57:46', '2025-05-28 08:57:46');
INSERT INTO `article` VALUES (4, 7, 'JAVA', '<h1>Springboot</h1>', '编程', 'PENDING', 1, 0, '2025-05-28 09:01:47', '2025-05-28 09:01:47');
INSERT INTO `article` VALUES (5, 7, 'SpringBoot', '<h1>SpringBoot</h1>', 'Spring', 'PUBLISHED', 8, 36, '2025-05-28 10:38:56', '2025-06-16 11:19:17');
INSERT INTO `article` VALUES (6, 7, '大学', '<div>哈哈哈哈哈哈哈</div>', '生活', 'PUBLISHED', 2, 11, '2025-05-28 11:10:18', '2025-06-16 10:34:40');
INSERT INTO `article` VALUES (7, 7, '大学', '<p>哇哇哇阿瓦阿瓦</p>', '生活', 'PUBLISHED', 2, 5, '2025-05-28 11:10:44', '2025-06-16 10:28:48');
INSERT INTO `article` VALUES (8, 7, 'java', '<div>java代码xiexiiiii</div>', 'Springboot', 'PENDING', 1, 10, '2025-06-03 22:16:42', '2025-06-16 10:19:57');
INSERT INTO `article` VALUES (9, 2, 'Spring Boot 入门指南', '本文介绍了如何快速上手 Spring Boot 框架，包括项目搭建、常用注解、配置文件及简单示例代码，帮助初学者快速掌握开发技巧。', 'springboot,java,后端开发', 'PUBLISHED', NULL, 123, '2025-05-10 10:00:00', '2025-06-11 10:22:33');
INSERT INTO `article` VALUES (10, 5, '个人博客系统的设计思路', '本文分享个人博客系统的设计思路，涵盖用户管理、文章发布、评论功能以及安全机制的实现，适合小型项目开发参考。', '博客系统,设计,Java', 'PUBLISHED', NULL, 86, '2025-05-12 15:30:00', '2025-06-06 19:13:31');
INSERT INTO `article` VALUES (11, 4, 'MyBatis-Plus 快速使用教程', '介绍 MyBatis-Plus 的核心功能、自动 CRUD 代码生成及使用技巧，提高开发效率，降低重复代码的书写。', 'mybatis-plus,数据库,Java', 'PUBLISHED', NULL, 45, '2025-05-14 09:20:00', '2025-06-06 18:49:51');
INSERT INTO `article` VALUES (12, 6, '前端分页实现方案比较', '本文比较几种前端分页的实现方案，包括纯前端分页、后端分页接口配合前端展示，及相关性能分析，帮助开发者选择合适方案。', '前端,分页,性能优化', 'PUBLISHED', NULL, 61, '2025-05-15 13:45:00', '2025-06-06 19:53:06');
INSERT INTO `article` VALUES (13, 7, '使用 Spring Security 实现用户认证与授权', '讲解如何基于 Spring Security 实现用户登录认证、权限管理和安全配置，确保系统安全稳定运行。', 'spring-security,安全,Java', 'PUBLISHED', NULL, 104, '2025-05-16 11:00:00', '2025-06-16 11:11:49');
INSERT INTO `article` VALUES (14, 7, 'victory', '<div><span style=\"background-color: #2dc26b;\">zheshiyiciweidadeshengli</span></div>', 'test', 'PUBLISHED', 2, 4, '2025-06-06 19:16:29', '2025-06-16 10:22:07');
INSERT INTO `article` VALUES (15, 2, 'Spring Boot快速入门与实战', 'Spring Boot 是当下非常流行的 Java 后端开发框架，它通过简化配置和自动化管理，极大提升了开发效率。本文将从基础开始，介绍如何搭建一个简单的 Spring Boot 项目，并逐步添加常用功能模块。首先，我们创建一个新的 Spring Boot 项目，可以使用官方的 Spring Initializr 工具快速生成项目骨架。项目依赖可以选择 Spring Web、Spring Data JPA、Thymeleaf 等常用模块。项目创建完成后，我们编写第一个控制器类，处理基础的 HTTP 请求。通过 @RestController 注解，可以快速暴露 RESTful 接口。接着配置 application.properties 文件，完成数据库连接等基础配置。本文还将介绍如何集成 MySQL 数据库，使用 Spring Data JPA 进行数据操作，以及如何通过 Thymeleaf 实现简单的页面渲染。开发过程中会涉及实体类设计、Repository 接口定义、Service 服务层实现等关键步骤。最后，讲解如何运行项目并通过浏览器访问，实现一个完整的动态网页应用。Spring Boot 强大的自动配置机制和丰富的生态插件，使得Java开发更加轻松高效。通过本文的学习，读者可以快速掌握 Spring Boot 的核心技术栈，搭建属于自己的后端服务，打下坚实的基础，为后续深入微服务架构和云原生开发做好准备。', 'springboot,java,后端开发,教程', 'PUBLISHED', NULL, 19, '2025-06-06 19:18:02', '2025-06-11 11:52:17');
INSERT INTO `article` VALUES (16, 4, 'MyBatis-Plus 详解与实践', 'MyBatis-Plus 是 MyBatis 的增强工具，在保持 MyBatis 灵活性的同时，大幅度简化了 CRUD 操作。本文详细介绍了 MyBatis-Plus 的安装、配置及常用功能。首先，介绍如何在 Spring Boot 项目中引入 MyBatis-Plus 依赖，并完成基础配置。然后，通过实体类和 Mapper 接口，实现自动生成 SQL 语句，减少手写代码量。重点演示了分页查询、条件构造器、逻辑删除、自动填充字段等实用功能，提升数据操作效率。此外，MyBatis-Plus 支持代码生成器，可以根据数据库表结构自动生成实体和 Mapper，大幅节省开发时间。通过多个代码示例，详细展示了如何灵活使用 MyBatis-Plus 提供的 Wrapper 条件构造器，完成复杂的查询需求。还分享了调优技巧，如合理使用缓存、SQL 日志打印等。最后，结合实际项目，讲述如何集成 MyBatis-Plus 与 Spring Security，实现数据权限控制。MyBatis-Plus 作为 Java 数据访问层的利器，助力开发者快速构建高效、稳定的后端服务。', 'mybatis-plus,数据库,java,教程', 'PUBLISHED', NULL, 2, '2025-06-06 19:18:02', '2025-06-06 19:53:25');
INSERT INTO `article` VALUES (17, 5, '个人博客系统设计与实现', '本文从需求分析出发，详细讲解如何设计并实现一个个人博客系统。系统包含用户注册登录、文章发布、分类标签管理、评论功能等模块。设计阶段，采用模块化思想，将系统拆分为用户模块、文章模块、评论模块和管理模块，方便后续维护扩展。数据库设计注重规范，合理使用外键保证数据完整性。实现方面，前端采用 Thymeleaf 模板引擎结合 Bootstrap 实现响应式页面，确保良好的用户体验。后端基于 Spring Boot 框架，结合 MyBatis-Plus 进行数据操作。文章发布支持富文本编辑，并实现文章状态管理（草稿、待审核、已发布），管理员可以对文章进行审核操作。评论模块支持多级回复，增强互动性。安全方面，集成 Spring Security 实现用户权限控制，保护系统安全。系统还支持文章的标签筛选和分页展示，满足用户多样化需求。通过本项目，读者可以系统掌握完整的博客平台开发流程，提升实际项目开发能力和架构设计水平。', '博客系统,java,springboot,设计', 'PUBLISHED', NULL, 1, '2025-06-06 19:18:02', '2025-06-11 09:25:07');
INSERT INTO `article` VALUES (18, 6, '前端分页实现方案探讨', '分页是前端开发中常见的功能，合理的分页方案能极大提升页面性能和用户体验。本文探讨多种前端分页实现方案，分析各自优缺点。纯前端分页适合数据量较小的场景，所有数据一次性加载到前端，通过 JavaScript 进行分页切换，开发简单但数据量大时性能不足。服务器端分页则由后端按页返回数据，前端仅展示当前页内容，减轻浏览器压力，适用于海量数据。前后端通过参数传递页码和每页数量，交互频繁。混合分页方案结合两者优点，前端缓存部分数据，减少请求次数，同时支持快速切换和动态加载，提升用户体验。本文还介绍了常用的分页组件和实现技巧，如无限滚动、加载更多按钮、分页样式设计等。并给出示例代码，帮助读者快速上手分页开发。选择合适的分页方案需根据实际业务需求、数据规模和用户习惯，保证性能与体验的平衡。', '前端,分页,性能优化,技术分享', 'PUBLISHED', NULL, 1, '2025-06-06 19:18:02', '2025-06-06 20:25:30');
INSERT INTO `article` VALUES (19, 7, 'Spring Security 用户认证与授权实战', 'Spring Security 是 Java 企业级应用安全的主流框架，提供完整的认证与授权解决方案。本文结合实例，讲解如何使用 Spring Security 保护应用安全。首先介绍基本概念，认证（Authentication）和授权（Authorization）的区别与联系。通过配置 SecurityConfig 类，实现基于内存或数据库的用户管理。演示登录页面定制、密码加密存储、角色权限分配等常见功能。通过注解和配置，实现对不同请求路径的访问控制，保障系统资源安全。还讲解了会话管理、防止 CSRF 攻击、记住我功能等高级特性，提升系统安全性和用户体验。最后结合项目实际，分享常见问题及调试技巧，帮助开发者快速定位并解决安全配置中的难题。通过本文学习，读者能够掌握 Spring Security 核心机制，独立完成复杂的安全控制需求。', 'spring-security,安全,认证,授权', 'PUBLISHED', NULL, 12, '2025-06-06 19:18:02', '2025-06-16 10:21:31');
INSERT INTO `article` VALUES (20, 7, 'java', '<div><span style=\"background-color: #2dc26b;\"><strong>java代码ttt</strong></span></div>', 'Springboot', 'DRAFT', 1, 10, '2025-06-11 11:24:44', '2025-06-16 10:20:05');
INSERT INTO `article` VALUES (21, 7, '测试浏览量', '<div>测试测试</div>', '', 'PUBLISHED', 1, 5, '2025-06-16 10:20:54', '2025-06-16 11:09:23');

-- ----------------------------
-- Table structure for article_view_stat
-- ----------------------------
DROP TABLE IF EXISTS `article_view_stat`;
CREATE TABLE `article_view_stat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '文章 ID',
  `view_date` date NOT NULL COMMENT '统计日期',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '该文章当日浏览量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_article_date`(`article_id` ASC, `view_date` ASC) USING BTREE,
  INDEX `idx_avs_article`(`article_id` ASC) USING BTREE,
  CONSTRAINT `fk_avs_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章每日浏览量统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_view_stat
-- ----------------------------
INSERT INTO `article_view_stat` VALUES (1, 21, '2025-06-16', 1);
INSERT INTO `article_view_stat` VALUES (2, 13, '2025-06-16', 1);
INSERT INTO `article_view_stat` VALUES (3, 5, '2025-06-16', 3);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '技术');
INSERT INTO `category` VALUES (2, '生活');
INSERT INTO `category` VALUES (3, '新闻');
INSERT INTO `category` VALUES (4, '旅行');
INSERT INTO `category` VALUES (5, '美食');
INSERT INTO `category` VALUES (6, '编程');
INSERT INTO `category` VALUES (7, '设计');
INSERT INTO `category` VALUES (8, '工具');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '文章 ID',
  `user_id` int NOT NULL COMMENT '评论者用户 ID',
  `parent_id` int NULL DEFAULT NULL COMMENT '父评论 ID，用于回复',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK8kcum44fvpupyw6f5baccx25c`(`user_id` ASC) USING BTREE,
  INDEX `FKde3rfu96lep00br5ov0mdieyt`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKde3rfu96lep00br5ov0mdieyt` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 5, 7, NULL, 'haahh', '2025-06-02 21:22:54');
INSERT INTO `comment` VALUES (2, 5, 7, NULL, 'nihao', '2025-06-03 21:38:33');
INSERT INTO `comment` VALUES (3, 6, 7, NULL, '你好\r\n', '2025-06-03 22:29:24');
INSERT INTO `comment` VALUES (4, 6, 7, 3, 'nihao', '2025-06-03 22:45:39');
INSERT INTO `comment` VALUES (5, 5, 7, NULL, '', '2025-06-06 19:00:36');
INSERT INTO `comment` VALUES (6, 5, 7, NULL, 'zaijian', '2025-06-06 19:00:43');
INSERT INTO `comment` VALUES (7, 5, 7, 1, '号\r\n', '2025-06-16 10:20:22');

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '操作用户 ID',
  `article_id` int NOT NULL COMMENT '文章 ID',
  `type` enum('LIKE','FAVORITE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'LIKE=点赞, FAVORITE=收藏',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (3, 7, 15, 'LIKE', '2025-06-11 10:00:30');
INSERT INTO `favorite` VALUES (9, 7, 5, 'LIKE', '2025-06-16 08:21:19');
INSERT INTO `favorite` VALUES (10, 7, 13, 'LIKE', '2025-06-16 08:21:24');
INSERT INTO `favorite` VALUES (11, 7, 13, 'FAVORITE', '2025-06-16 10:10:41');
INSERT INTO `favorite` VALUES (12, 7, 5, 'FAVORITE', '2025-06-16 10:20:15');
INSERT INTO `favorite` VALUES (13, 7, 21, 'LIKE', '2025-06-16 10:21:14');
INSERT INTO `favorite` VALUES (14, 7, 6, 'LIKE', '2025-06-16 10:22:28');
INSERT INTO `favorite` VALUES (15, 7, 6, 'FAVORITE', '2025-06-16 10:22:29');
INSERT INTO `favorite` VALUES (16, 7, 21, 'FAVORITE', '2025-06-16 10:34:51');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `reporter_id` int NOT NULL COMMENT '举报人用户 ID',
  `target_type` enum('ARTICLE','COMMENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '被举报对象类型',
  `target_id` int NOT NULL COMMENT '被举报对象的 ID',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报理由',
  `status` enum('PENDING','RESOLVED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` enum('PENDING','ACTIVE','BANNED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `is_admin` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1723902387@qq.com', '$2a$10$TyHQnDBSZJAbn9408HITreKV0ksTFs.cCtH/LUdpM/B87dITStYE6', '1723902387', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 09:05:21');
INSERT INTO `user` VALUES (2, 'cy@163.com', '$2a$10$xltj3NHQuVsQj0kVwRMSvumzbFUBlO74mQL4MX1K6I20HCBIfQ6NC', 'cy', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 09:25:05');
INSERT INTO `user` VALUES (3, 'cycy@qq.com', '$2a$10$SSDu7G7xoNso6YUMq8OUPuoC8WDpLihlbiMAnMH.IshPNWCvmR9TS', 'cycy', NULL, '/static/img/default-avatar.png', 'PENDING', 0, '2025-05-07 10:10:57');
INSERT INTO `user` VALUES (4, 'cycy@163.com', '$2a$10$m/cJ.p54k0FvDRkKgOKFOeRET1lZsPSGe9Qprh64QPO9FEntopd4i', 'cycy', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 10:34:21');
INSERT INTO `user` VALUES (5, 'aa@qq.com', '$2a$10$pZNoGm3oTOOaDgqomEkKUO5CBMSj8NTjHy7jF7kzhG2eIM0PYyPSC', 'aa', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 11:43:19');
INSERT INTO `user` VALUES (6, 'ww@163.com', '1234', 'ww666', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-14 08:00:30');
INSERT INTO `user` VALUES (7, 'ee@qq.com', '$2a$10$XFXf8GXywSAs.oK6w.Lho.TjmIVuqEpHfF5UCJL/ie00eU2TjjA1y', 'eett', 'feichanghao1', '1bff8e31-41d4-4b6a-9f9c-2c69f6556e29.jpg', 'ACTIVE', 0, '2025-05-14 08:54:39');

SET FOREIGN_KEY_CHECKS = 1;
