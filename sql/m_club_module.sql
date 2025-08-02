-- ============================================
-- 俱乐部SaaS系统 - club模块相关SQL
-- 基于Smart Admin v3架构设计
-- ============================================

-- ----------------------------
-- Table structure for m_club 俱乐部表
-- ----------------------------
DROP TABLE IF EXISTS `m_club`;
CREATE TABLE `m_club` (
  `club_id` bigint NOT NULL AUTO_INCREMENT COMMENT '俱乐部ID',
  `club_name` varchar(100) NOT NULL COMMENT '俱乐部名称',
  `club_code` varchar(50) DEFAULT NULL COMMENT '俱乐部编码',
  `logo_url` varchar(500) DEFAULT NULL COMMENT 'LOGO地址',
  `banner_url` varchar(500) DEFAULT NULL COMMENT '置顶图片地址',
  `pc_banner_url` varchar(500) DEFAULT NULL COMMENT 'PC端首页图片地址',
  `business_start_time` time DEFAULT NULL COMMENT '营业开始时间',
  `business_end_time` time DEFAULT NULL COMMENT '营业结束时间',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `description` text COMMENT '俱乐部详情',
  `honor_info` text COMMENT '荣誉信息',
  `booking_notice` text COMMENT '约课需知',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `business_license_url` varchar(500) DEFAULT NULL COMMENT '营业执照图片地址',
  `legal_person` varchar(50) DEFAULT NULL COMMENT '法人代表',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `expire_date` datetime DEFAULT NULL COMMENT '到期日期时间',
  `create_by` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_valid` int NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
  PRIMARY KEY (`club_id`),
  UNIQUE KEY `uk_club_code` (`club_code`),
  KEY `idx_club_name` (`club_name`),
  KEY `idx_is_delete` (`is_delete`),
  KEY `idx_is_valid` (`is_valid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='俱乐部表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
INSERT INTO `m_club` VALUES 
(1, '示例马术俱乐部', 'DEMO_CLUB_001', NULL, NULL, NULL, '09:00:00', '18:00:00', '北京市朝阳区示例街道123号', '010-12345678', '这是一个示例马术俱乐部，提供专业的马术培训服务。', '获得多项马术比赛奖项', '请提前预约课程，按时到达。', 39.904200, 116.407396, '北京市', '北京市', '朝阳区', NULL, '张三', '李四', '13800138000', 'demo@club.com', '2025-12-31 23:59:59', 'admin', NOW(), 'admin', NOW(), 1, 0);

-- ----------------------------
-- 添加菜单权限数据
-- ----------------------------

-- 插入一级菜单：俱乐部管理
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (400, '俱乐部管理', 1, 0, 2, '/club', NULL, NULL, NULL, NULL, 'CrownOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入二级菜单：俱乐部管理
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (401, '俱乐部管理', 2, 400, 1, '/club/club/club-list', '/business/club/club-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：查询
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (402, '查询', 3, 401, 1, NULL, NULL, 1, 'club:club:query', 'club:club:query', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：新建
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (403, '新建', 3, 401, 2, NULL, NULL, 1, 'club:club:add', 'club:club:add', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：编辑
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (404, '编辑', 3, 401, 3, NULL, NULL, 1, 'club:club:update', 'club:club:update', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：删除
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (405, '删除', 3, 401, 4, NULL, NULL, 1, 'club:club:delete', 'club:club:delete', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：详情
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (406, '详情', 3, 401, 5, NULL, NULL, 1, 'club:club:detail', 'club:club:detail', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入隐藏页面：俱乐部详情页面
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (407, '俱乐部详情页', 2, 400, 2, '/club/club/club-detail', '/business/club/club-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 1, 0, 0, 0, 1, NOW(), 1, NOW());

-- ----------------------------
-- 为技术总监角色分配权限
-- ----------------------------
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 400, NOW(), NOW()),
(1, 401, NOW(), NOW()),
(1, 402, NOW(), NOW()),
(1, 403, NOW(), NOW()),
(1, 404, NOW(), NOW()),
(1, 405, NOW(), NOW()),
(1, 406, NOW(), NOW()),
(1, 407, NOW(), NOW());