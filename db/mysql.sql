/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.123
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 192.168.1.123:3306
 Source Schema         : geoxus

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 09/02/2020 17:18:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for o_goods
-- ----------------------------
DROP TABLE IF EXISTS `o_goods`;
CREATE TABLE `o_goods`  (
  `goods_id` int(11) NOT NULL AUTO_INCREMENT,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `goods_price` decimal(10, 2) NULL DEFAULT NULL,
  `sku` int(255) NULL DEFAULT NULL,
  `ext` json NULL,
  `status` int(11) NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`goods_id`) USING BTREE,
  INDEX `idx__created_at_promotion_price`(`created_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for o_order_item
-- ----------------------------
DROP TABLE IF EXISTS `o_order_item`;
CREATE TABLE `o_order_item`  (
  `order_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `order_sn` bigint(30) NOT NULL,
  `goods_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `quantity` int(255) NULL DEFAULT NULL,
  `goods_price` decimal(10, 2) NULL DEFAULT NULL,
  `purchasing_price` decimal(10, 2) NULL DEFAULT NULL,
  `ext` json NULL,
  `status` int(11) NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`order_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for o_orders
-- ----------------------------
DROP TABLE IF EXISTS `o_orders`;
CREATE TABLE `o_orders`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_sn` bigint(30) NULL DEFAULT NULL,
  `order_type` tinyint(3) NULL DEFAULT NULL,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_price` decimal(12, 2) NULL DEFAULT NULL,
  `ext` json NULL,
  `pay_info` json NULL,
  `status` int(11) NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  `cancel_at` int(11) NULL DEFAULT NULL,
  `completed_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_comment
-- ----------------------------
DROP TABLE IF EXISTS `p_comment`;
CREATE TABLE `p_comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父级ID',
  `model_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型ID',
  `core_model_id` int(11) NULL DEFAULT NULL COMMENT '核心模型ID',
  `model_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型类型',
  `ext` json NULL COMMENT '额外数据',
  `comments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评论内容',
  `user_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '层级',
  `status` smallint(6) NULL DEFAULT NULL COMMENT '状态',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_content
-- ----------------------------
DROP TABLE IF EXISTS `p_content`;
CREATE TABLE `p_content`  (
  `content_id` int(11) NOT NULL,
  `category_id` int(11) NULL DEFAULT NULL,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contents` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ext` json NULL,
  `status` smallint(6) NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_feedback
-- ----------------------------
DROP TABLE IF EXISTS `p_feedback`;
CREATE TABLE `p_feedback`  (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT 0,
  `feedback_type` smallint(6) NULL DEFAULT NULL,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `model_id` int(11) NULL DEFAULT NULL,
  `ext` json NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `status` smallint(6) NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '留言与意见反馈' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_admin
-- ----------------------------
DROP TABLE IF EXISTS `s_admin`;
CREATE TABLE `s_admin`  (
  `admin_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级ID(用于特殊场景)',
  `nick_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员昵称',
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `salt` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐(用于加密)',
  `password` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密后的密码',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` smallint(2) NULL DEFAULT NULL COMMENT '状态',
  `remark` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `ext` json NULL COMMENT '额外数据',
  `is_super_admin` smallint(4) NULL DEFAULT 1 COMMENT '是否超级管理员',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_admin_has_permissions
-- ----------------------------
DROP TABLE IF EXISTS `s_admin_has_permissions`;
CREATE TABLE `s_admin_has_permissions`  (
  `permission_id` int(10) UNSIGNED NOT NULL COMMENT '权限ID',
  `admin_id` int(10) UNSIGNED NOT NULL COMMENT '管理员ID',
  `model_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'App\\System\\Entities\\SAdminModel' COMMENT '模型类型(用户获取用户权限,主要用于PHP)',
  PRIMARY KEY (`permission_id`, `admin_id`) USING BTREE,
  INDEX `fk_s_acl_has_s_admin_s_admin1_idx`(`admin_id`) USING BTREE,
  INDEX `fk_s_acl_has_s_admin_s_acl1_idx`(`permission_id`) USING BTREE,
  CONSTRAINT `s_admin_has_permissions_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `s_permissions` (`permission_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `s_admin_has_permissions_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `s_admin` (`admin_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员拥有的权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_admin_has_roles
-- ----------------------------
DROP TABLE IF EXISTS `s_admin_has_roles`;
CREATE TABLE `s_admin_has_roles`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `admin_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '管理员ID',
  `model_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'App\\System\\Entities\\SAdminModel' COMMENT '模型类型(使用的模型,主要用于PHP)',
  `created_at` int(11) NULL DEFAULT 0 COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_s_role_has_s_admin_s_admin1_idx`(`admin_id`) USING BTREE,
  INDEX `fk_s_role_has_s_admin_s_role1_idx`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与管理员对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_banner
-- ----------------------------
DROP TABLE IF EXISTS `s_banner`;
CREATE TABLE `s_banner`  (
  `banner_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (json_unquote(json_extract(`ext`,'$.intro'))) STORED COMMENT '简介' NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (json_unquote(json_extract(`ext`,'$.url'))) STORED COMMENT '跳转url' NULL,
  `ext` json NULL COMMENT '额外数据',
  `core_model_id` int(11) NULL DEFAULT NULL COMMENT '核心模型ID',
  `sort` tinyint(255) NULL DEFAULT NULL COMMENT '排序',
  `type` smallint(6) NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(255) NULL DEFAULT 0 COMMENT '状态',
  `position` int(255) NULL DEFAULT 0 COMMENT '位置',
  `province_id` int(11) GENERATED ALWAYS AS (json_unquote(json_extract(`ext`,'$.province_id'))) STORED COMMENT '省ID' NULL,
  `city_id` int(11) GENERATED ALWAYS AS (json_unquote(json_extract(`ext`,'$.city_id'))) STORED COMMENT '市ID' NULL,
  `created_at` int(255) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`banner_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_category
-- ----------------------------
DROP TABLE IF EXISTS `s_category`;
CREATE TABLE `s_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '上级ID',
  `sort` tinyint(3) NULL DEFAULT NULL COMMENT '排序',
  `ext` json NULL COMMENT '自定义数据字段(JSON串存储)',
  `status` int(11) NULL DEFAULT NULL COMMENT '是否禁用',
  `core_model_id` int(11) NULL DEFAULT NULL,
  `model_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内容分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_common_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `s_common_operation_log`;
CREATE TABLE `s_common_operation_log`  (
  `common_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `exec_time` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created_at` int(255) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`common_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_log
-- ----------------------------
DROP TABLE IF EXISTS `s_log`;
CREATE TABLE `s_log`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `core_model_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联核心模型ID',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '具体模型对象ID',
  `ext` json NULL COMMENT '详细数据',
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源',
  `user_id` bigint(25) NULL DEFAULT NULL COMMENT '用户ID',
  `created_at` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_menu
-- ----------------------------
DROP TABLE IF EXISTS `s_menu`;
CREATE TABLE `s_menu`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父级ID',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名字',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限(多个用逗号分割,如:user-list,post-create)',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '类型 0 : 目录  1 : 菜单  2 : 按钮',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` int(255) NULL DEFAULT NULL COMMENT '状态',
  `created_at` int(255) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_message
-- ----------------------------
DROP TABLE IF EXISTS `s_message`;
CREATE TABLE `s_message`  (
  `message_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `core_model_id` int(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` smallint(4) NULL DEFAULT 0 COMMENT '消息类型',
  `contents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息类容',
  `template_id` int(11) NULL DEFAULT 0 COMMENT '消息模板',
  `ext` json NULL COMMENT '额外信息',
  `status` smallint(6) NULL DEFAULT NULL COMMENT '状态',
  `created_at` int(11) NULL DEFAULT 0 COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_oss
-- ----------------------------
DROP TABLE IF EXISTS `s_oss`;
CREATE TABLE `s_oss`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_permissions
-- ----------------------------
DROP TABLE IF EXISTS `s_permissions`;
CREATE TABLE `s_permissions`  (
  `permission_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父级ID',
  `permission_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限码',
  `url` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求的url, 可以填正则表达式',
  `guard_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'api' COMMENT '权限保护类型(web,php，主要用于PHP)',
  `show_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `path` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前层级(1-2-3)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型，1：菜单，2：按钮，3：其他',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态，1：正常，0：冻结',
  `sort` int(11) NULL DEFAULT NULL COMMENT '权限在当前模块下的顺序，由小到大',
  `remark` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_region
-- ----------------------------
DROP TABLE IF EXISTS `s_region`;
CREATE TABLE `s_region`  (
  `id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pinyin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `first_letter` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `i_region_parent`(`parent_id`) USING BTREE,
  INDEX `idx_city_name`(`name`(3)) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_role_has_permissions
-- ----------------------------
DROP TABLE IF EXISTS `s_role_has_permissions`;
CREATE TABLE `s_role_has_permissions`  (
  `permission_id` int(10) UNSIGNED NOT NULL COMMENT '权限ID',
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `model_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模型类型(用户获取用户权限,主要用于PHP)',
  PRIMARY KEY (`permission_id`, `role_id`) USING BTREE,
  INDEX `fk_s_acl_has_s_role_s_role1_idx`(`role_id`) USING BTREE,
  INDEX `fk_s_acl_has_s_role_s_acl1_idx`(`permission_id`) USING BTREE,
  CONSTRAINT `s_role_has_permissions_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `s_permissions` (`permission_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `s_role_has_permissions_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `s_roles` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与权限的对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_roles
-- ----------------------------
DROP TABLE IF EXISTS `s_roles`;
CREATE TABLE `s_roles`  (
  `role_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父级ID(用于特殊场合),比如:用户自己又有自己的分配权限',
  `role_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名字',
  `show_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名字',
  `guard_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'api' COMMENT '保护的类型(web,api,主要用于PHP)',
  `type` int(11) NULL DEFAULT NULL COMMENT '角色的类型，1：管理员角色，2：其他',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态，1：可用，0：冻结',
  `remark` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '层级(0,1,2)(用于特殊场合),比如:用户自己又有自己的分配权限',
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_user_token
-- ----------------------------
DROP TABLE IF EXISTS `s_user_token`;
CREATE TABLE `s_user_token`  (
  `user_id` bigint(11) NOT NULL COMMENT '主键ID',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户Token',
  `type` smallint(3) NULL DEFAULT 2 COMMENT '类型(1、admin  2、user)',
  `expired_at` int(11) NULL DEFAULT NULL COMMENT '过期时间',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_balance
-- ----------------------------
DROP TABLE IF EXISTS `u_balance`;
CREATE TABLE `u_balance`  (
  `balance_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `balance` decimal(20, 2) UNSIGNED NULL DEFAULT NULL COMMENT '账户余额',
  `type` smallint(3) NULL DEFAULT NULL COMMENT '余额类型:\\r\\n\\n1、可用余额\\r\\n2、USDT(不可用)\\r\\n\\n3、SE\\n\\r\\n4、YE\\r\\n\\n5、ETH\\r\\n6、USDT(可用)\\r\\n7、SYE',
  `version` int(255) NULL DEFAULT 0,
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`balance_id`) USING BTREE,
  UNIQUE INDEX `unique_user_id_type_idx`(`user_id`, `type`) USING BTREE,
  INDEX `fk_balance_u_user1_idx`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_transaction_log
-- ----------------------------
DROP TABLE IF EXISTS `u_transaction_log`;
CREATE TABLE `u_transaction_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `block_number` int(11) NULL DEFAULT NULL COMMENT '区块号',
  `timestamp` int(11) NULL DEFAULT NULL COMMENT 'transaction时间戳',
  `transaction_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'transaction识别号',
  `eth_from` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送以太坊地址',
  `eth_to` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款以太坊地址',
  `token_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代币转账量',
  `eth_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ETH转账量',
  `status` smallint(4) NULL DEFAULT NULL COMMENT '状态\r\n0: pending\r\n1: confirmed\r\n2: failed',
  `type` smallint(4) NULL DEFAULT 0 COMMENT '转账类型:\n1、内部转账(转入)  \n2、外部转账(转出)',
  `tx_data` json NULL COMMENT '交易数据',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_u_transaction_log_u_user1_idx`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户交易记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_user
-- ----------------------------
DROP TABLE IF EXISTS `u_user`;
CREATE TABLE `u_user`  (
  `user_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint(11) NULL DEFAULT 0 COMMENT '推荐人ID或上级ID',
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码MD5+重叠混淆',
  `salt` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '加密盐',
  `wx_open_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '微信OPENID，用于快捷登录',
  `qq_open_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'QQ OPENID,用于快捷登录',
  `ali_pay_open_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '支付宝开放ID',
  `pay_password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '支付密码',
  `pay_salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付密码的盐',
  `nick_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户路径,用于实现层级关系(比如,无限级分销)',
  `hierarchy` smallint(255) NULL DEFAULT 1 COMMENT '用户所属层级',
  `status` tinyint(6) NULL DEFAULT NULL COMMENT '状态',
  `ext` json NULL COMMENT '额外数据',
  `core_model_id` int(11) NULL DEFAULT 14 COMMENT '核心模型ID',
  `user_type` int(6) NULL DEFAULT NULL COMMENT '用户类型',
  `last_login_at` int(11) NULL DEFAULT NULL COMMENT '最后登录时间',
  `invite_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `created_at` int(11) NULL DEFAULT 0 COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `path_index`(`hierarchy`, `path`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1226149153655099393 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帐户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `u_user_favorites`;
CREATE TABLE `u_user_favorites`  (
  `collect_id` int(11) NOT NULL AUTO_INCREMENT,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `model_id` int(11) NULL DEFAULT NULL,
  `ext` json NULL,
  `created_at` int(255) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  `association_core_model_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) GENERATED ALWAYS AS (json_extract(`ext`,'$.user_id')) VIRTUAL NULL,
  `status` smallint(6) GENERATED ALWAYS AS (json_extract(`ext`,'$.status')) VIRTUAL NULL,
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_wallet
-- ----------------------------
DROP TABLE IF EXISTS `u_wallet`;
CREATE TABLE `u_wallet`  (
  `wallet_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `password` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '钱包密码 (加密存储)',
  `dynamic_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥salt',
  `wallet_file` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '钱包文件名字',
  `address` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '钱包地址',
  `is_platform` tinyint(3) NULL DEFAULT 0 COMMENT '是否平台 (0、否  1、是)',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 (1、正常 2、失效)',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`wallet_id`) USING BTREE,
  INDEX `fk_u_eth_wallet_u_user1_idx`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户以太坊钱包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_withdraw
-- ----------------------------
DROP TABLE IF EXISTS `u_withdraw`;
CREATE TABLE `u_withdraw`  (
  `withdraw_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `core_model_id` int(11) NULL DEFAULT NULL COMMENT '核心模型ID',
  `ext` json NULL COMMENT '扩展数据',
  `status` smallint(6) GENERATED ALWAYS AS (json_unquote(json_extract(`ext`,'$.status'))) STORED COMMENT '状态' NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`withdraw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户提现' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_has_message
-- ----------------------------
DROP TABLE IF EXISTS `user_has_message`;
CREATE TABLE `user_has_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `core_model_id` int(11) NULL DEFAULT 0,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `message_id` int(11) NULL DEFAULT NULL COMMENT '消息ID',
  `ext` json NULL COMMENT '额外消息',
  `status` smallint(6) NULL DEFAULT NULL COMMENT '状态',
  `created_at` int(11) NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` int(11) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_log
-- ----------------------------
DROP TABLE IF EXISTS `user_log`;
CREATE TABLE `user_log`  (
  `user_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `core_model_id` int(11) NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `ext` json NULL,
  `event_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `action` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created_at` int(11) NULL DEFAULT NULL,
  `updated_at` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`user_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Triggers structure for table u_balance
-- ----------------------------
DROP TRIGGER IF EXISTS `check_balance_match`;
delimiter ;;
CREATE TRIGGER `check_balance_match` BEFORE UPDATE ON `u_balance` FOR EACH ROW begin
    DECLARE s_log_new_balance DOUBLE default 0.00;
	select new_balance into s_log_new_balance from s_log where user_id=NEW.user_id and core_model_id=200000 and balance_type=NEW.type and new_balance=NEW.balance order by id desc limit 1;
	if s_log_new_balance != NEW.balance THEN
		signal SQLSTATE '80000' SET MESSAGE_TEXT = "账户余额不匹配";
	end if;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
