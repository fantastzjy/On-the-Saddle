-- ============================================
-- 教练管理模块 - coach模块相关SQL
-- 基于Smart Admin v3架构设计
-- ============================================

-- ----------------------------
-- Table structure for m_coach 教练表
-- ----------------------------
DROP TABLE IF EXISTS `m_coach`;
CREATE TABLE `m_coach` (
  `coach_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教练ID',
  `coach_name` varchar(50) NOT NULL COMMENT '教练姓名',
  `coach_code` varchar(50) DEFAULT NULL COMMENT '教练编号',
  `gender` tinyint DEFAULT NULL COMMENT '性别;1=男;2=女;',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像地址',
  `professional_level` varchar(50) DEFAULT NULL COMMENT '专业等级',
  `speciality` varchar(200) DEFAULT NULL COMMENT '专业特长',
  `years_experience` int DEFAULT NULL COMMENT '从业年限',
  `certification` varchar(500) DEFAULT NULL COMMENT '资质证书',
  `club_id` bigint DEFAULT NULL COMMENT '所属俱乐部ID',
  `salary` decimal(10,2) DEFAULT NULL COMMENT '薪资',
  `entry_date` date DEFAULT NULL COMMENT '入职日期',
  `address` varchar(200) DEFAULT NULL COMMENT '联系地址',
  `emergency_contact` varchar(50) DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) DEFAULT NULL COMMENT '紧急联系电话',
  `description` text COMMENT '个人简介',
  `achievements` text COMMENT '获奖经历',
  `teaching_style` text COMMENT '教学风格',
  `create_by` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_active` int NOT NULL DEFAULT '1' COMMENT '是否在职;1=在职;0=离职;',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
  PRIMARY KEY (`coach_id`),
  UNIQUE KEY `uk_coach_code` (`coach_code`),
  UNIQUE KEY `uk_id_card` (`id_card`),
  KEY `idx_coach_name` (`coach_name`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_is_delete` (`is_delete`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='教练表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
INSERT INTO `m_coach` VALUES 
(1, '张教练', 'COACH_001', 1, '1985-03-15', '13800138001', 'zhang@coach.com', '110101198503150001', NULL, '高级教练', '马术基础训练,障碍跳跃', 10, '国家二级教练员证书', 1, 8000.00, '2020-01-01', '北京市朝阳区', '李女士', '13900139001', '资深马术教练，拥有10年教学经验。', '全国马术比赛三等奖', '耐心细致，因材施教', 'admin', NOW(), 'admin', NOW(), 1, 0),
(2, '李教练', 'COACH_002', 2, '1990-07-20', '13800138002', 'li@coach.com', '110101199007200002', NULL, '中级教练', '马术基础,盛装舞步', 5, '马术教练资格证书', 1, 6500.00, '2021-06-01', '北京市海淀区', '王先生', '13900139002', '专业马术教练，擅长盛装舞步。', '北京市马术比赛二等奖', '严格要求，注重细节', 'admin', NOW(), 'admin', NOW(), 1, 0);

-- ----------------------------
-- 添加菜单权限数据
-- ----------------------------

-- 插入二级菜单：教练管理
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (408, '教练管理', 2, 400, 2, '/club/coach/coach-list', '/business/coach/coach-list.vue', NULL, NULL, NULL, 'UserOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：查询
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (409, '查询', 3, 408, 1, NULL, NULL, 1, 'club:coach:query', 'club:coach:query', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：新建
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (410, '新建', 3, 408, 2, NULL, NULL, 1, 'club:coach:add', 'club:coach:add', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：编辑
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (411, '编辑', 3, 408, 3, NULL, NULL, 1, 'club:coach:update', 'club:coach:update', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：删除
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (412, '删除', 3, 408, 4, NULL, NULL, 1, 'club:coach:delete', 'club:coach:delete', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入功能点：详情
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (413, '详情', 3, 408, 5, NULL, NULL, 1, 'club:coach:detail', 'club:coach:detail', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 插入隐藏页面：教练详情页面
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (414, '教练详情页', 2, 400, 3, '/club/coach/coach-detail', '/business/coach/coach-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 1, 0, 0, 0, 1, NOW(), 1, NOW());

-- ----------------------------
-- 为技术总监角色分配权限
-- ----------------------------
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
(1, 408, NOW(), NOW()),
(1, 409, NOW(), NOW()),
(1, 410, NOW(), NOW()),
(1, 411, NOW(), NOW()),
(1, 412, NOW(), NOW()),
(1, 413, NOW(), NOW()),
(1, 414, NOW(), NOW());