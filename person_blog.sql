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

 Date: 01/06/2025 21:59:07
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
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 7, 'ee', '', '11', 'PENDING', NULL, '2025-05-21 11:40:15', '2025-05-21 11:40:15');
INSERT INTO `article` VALUES (2, 7, 'java', '<div>java代码</div>', 'Springboot', 'DRAFT', 1, '2025-05-28 08:57:36', '2025-05-28 08:57:36');
INSERT INTO `article` VALUES (3, 7, 'java', '<div>Java哈哈哈</div>', 'Springboot', 'PENDING', 1, '2025-05-28 08:57:46', '2025-05-28 08:57:46');
INSERT INTO `article` VALUES (4, 7, 'JAVA', '<h1>Springboot</h1>', '编程', 'PENDING', 1, '2025-05-28 09:01:47', '2025-05-28 09:01:47');
INSERT INTO `article` VALUES (5, 7, 'SpringBoot', '<h1>SpringBoot</h1>', 'Spring', 'DRAFT', 8, '2025-05-28 10:38:56', '2025-05-28 10:38:56');
INSERT INTO `article` VALUES (6, 7, '大学', '<div>哈哈哈哈哈哈哈</div>', '生活', 'DRAFT', 2, '2025-05-28 11:10:18', '2025-05-28 11:10:18');
INSERT INTO `article` VALUES (7, 7, '大学', '<p>哇哇哇阿瓦阿瓦</p>', '生活', 'PUBLISHED', 2, '2025-05-28 11:10:44', '2025-05-28 11:37:58');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1723902387@qq.com', '$2a$10$TyHQnDBSZJAbn9408HITreKV0ksTFs.cCtH/LUdpM/B87dITStYE6', '1723902387', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 09:05:21');
INSERT INTO `user` VALUES (2, 'cy@163.com', '$2a$10$xltj3NHQuVsQj0kVwRMSvumzbFUBlO74mQL4MX1K6I20HCBIfQ6NC', 'cy', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 09:25:05');
INSERT INTO `user` VALUES (3, 'cycy@qq.com', '$2a$10$SSDu7G7xoNso6YUMq8OUPuoC8WDpLihlbiMAnMH.IshPNWCvmR9TS', 'cycy', NULL, '/static/img/default-avatar.png', 'PENDING', 0, '2025-05-07 10:10:57');
INSERT INTO `user` VALUES (4, 'cycy@163.com', '$2a$10$m/cJ.p54k0FvDRkKgOKFOeRET1lZsPSGe9Qprh64QPO9FEntopd4i', 'cycy', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 10:34:21');
INSERT INTO `user` VALUES (5, 'aa@qq.com', '$2a$10$pZNoGm3oTOOaDgqomEkKUO5CBMSj8NTjHy7jF7kzhG2eIM0PYyPSC', 'aa', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-07 11:43:19');
INSERT INTO `user` VALUES (6, 'ww@163.com', '1234', 'ww666', NULL, '/static/img/default-avatar.png', 'ACTIVE', 0, '2025-05-14 08:00:30');
INSERT INTO `user` VALUES (7, 'ee@qq.com', '$2a$10$XFXf8GXywSAs.oK6w.Lho.TjmIVuqEpHfF5UCJL/ie00eU2TjjA1y', 'eett', 'feichanghao1', '3ae872b7-2ffc-408a-b6cb-276dec022cfd.jpg', 'ACTIVE', 0, '2025-05-14 08:54:39');

SET FOREIGN_KEY_CHECKS = 1;
