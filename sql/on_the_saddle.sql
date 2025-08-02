/*
 Navicat Premium Data Transfer

 Source Server         : 115.120.213.163-root
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : 115.120.213.163:65234
 Source Schema         : on_the_saddle

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 03/08/2025 05:18:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_club
-- ----------------------------
DROP TABLE IF EXISTS `m_club`;
CREATE TABLE `m_club` (
  `club_id` bigint NOT NULL AUTO_INCREMENT COMMENT '俱乐部ID',
  `club_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '俱乐部名称',
  `club_code` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '俱乐部编码',
  `logo_url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'LOGO地址',
  `banner_url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '置顶图片地址',
  `pc_banner_url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'PC端首页图片地址',
  `business_start_time` time DEFAULT NULL COMMENT '营业开始时间',
  `business_end_time` time DEFAULT NULL COMMENT '营业结束时间',
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详细地址',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话',
  `description` text COLLATE utf8mb4_general_ci COMMENT '俱乐部详情',
  `honor_info` text COLLATE utf8mb4_general_ci COMMENT '荣誉信息',
  `booking_notice` text COLLATE utf8mb4_general_ci COMMENT '约课需知',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `province` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份',
  `city` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市',
  `district` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区县',
  `business_license_url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '营业执照图片地址',
  `legal_person` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '法人代表',
  `contact_person` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `expire_date` datetime DEFAULT NULL COMMENT '到期日期时间',
  `create_by` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_valid` int NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
  PRIMARY KEY (`club_id`),
  UNIQUE KEY `uk_club_code` (`club_code`),
  KEY `idx_club_name` (`club_name`),
  KEY `idx_is_delete` (`is_delete`),
  KEY `idx_is_valid` (`is_valid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='俱乐部表';

-- ----------------------------
-- Records of m_club
-- ----------------------------
BEGIN;
INSERT INTO `m_club` (`club_id`, `club_name`, `club_code`, `logo_url`, `banner_url`, `pc_banner_url`, `business_start_time`, `business_end_time`, `address`, `phone`, `description`, `honor_info`, `booking_notice`, `latitude`, `longitude`, `province`, `city`, `district`, `business_license_url`, `legal_person`, `contact_person`, `contact_phone`, `email`, `expire_date`, `create_by`, `create_time`, `update_by`, `update_time`, `is_valid`, `is_delete`) VALUES (1, '示例马术俱乐部', 'DEMO_CLUB_001', NULL, NULL, NULL, '09:00:00', '18:00:00', '北京市朝阳区示例街道123号', '010-12345678', '这是一个示例马术俱乐部，提供专业的马术培训服务。', '获得多项马术比赛奖项', '请提前预约课程，按时到达。', 39.904200, 116.407396, '北京市', '北京市', '朝阳区', NULL, '张三', '李四', '13800138000', 'demo@club.com', '2025-12-31 23:59:59', 'admin', '2025-08-03 02:24:05', 'admin', '2025-08-03 02:24:05', 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for m_coach
-- ----------------------------
DROP TABLE IF EXISTS `m_coach`;
CREATE TABLE `m_coach` (
  `coach_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教练ID',
  `coach_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '教练姓名',
  `coach_code` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '教练编号',
  `gender` tinyint DEFAULT NULL COMMENT '性别;1=男;2=女;',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `id_card` varchar(18) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证号',
  `avatar_url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像地址',
  `professional_level` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '专业等级',
  `speciality` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '专业特长',
  `years_experience` int DEFAULT NULL COMMENT '从业年限',
  `certification` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资质证书',
  `club_id` bigint DEFAULT NULL COMMENT '所属俱乐部ID',
  `salary` decimal(10,2) DEFAULT NULL COMMENT '薪资',
  `entry_date` date DEFAULT NULL COMMENT '入职日期',
  `address` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系地址',
  `emergency_contact` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '紧急联系电话',
  `description` text COLLATE utf8mb4_general_ci COMMENT '个人简介',
  `achievements` text COLLATE utf8mb4_general_ci COMMENT '获奖经历',
  `teaching_style` text COLLATE utf8mb4_general_ci COMMENT '教学风格',
  `create_by` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='教练表';

-- ----------------------------
-- Records of m_coach
-- ----------------------------
BEGIN;
INSERT INTO `m_coach` (`coach_id`, `coach_name`, `coach_code`, `gender`, `birth_date`, `phone`, `email`, `id_card`, `avatar_url`, `professional_level`, `speciality`, `years_experience`, `certification`, `club_id`, `salary`, `entry_date`, `address`, `emergency_contact`, `emergency_phone`, `description`, `achievements`, `teaching_style`, `create_by`, `create_time`, `update_by`, `update_time`, `is_active`, `is_delete`) VALUES (1, '张教练', 'COACH_001', 1, '1985-03-15', '13800138001', 'zhang@coach.com', '110101198503150001', NULL, '高级教练', '马术基础训练,障碍跳跃', 10, '国家二级教练员证书', 1, 8000.00, '2020-01-01', '北京市朝阳区', '李女士', '13900139001', '资深马术教练，拥有10年教学经验。', '全国马术比赛三等奖', '耐心细致，因材施教', 'admin', '2025-08-03 03:25:48', 'admin', '2025-08-03 03:25:48', 1, 0);
INSERT INTO `m_coach` (`coach_id`, `coach_name`, `coach_code`, `gender`, `birth_date`, `phone`, `email`, `id_card`, `avatar_url`, `professional_level`, `speciality`, `years_experience`, `certification`, `club_id`, `salary`, `entry_date`, `address`, `emergency_contact`, `emergency_phone`, `description`, `achievements`, `teaching_style`, `create_by`, `create_time`, `update_by`, `update_time`, `is_active`, `is_delete`) VALUES (2, '李教练', 'COACH_002', 2, '1990-07-20', '13800138002', 'li@coach.com', '110101199007200002', NULL, '中级教练', '马术基础,盛装舞步', 5, '马术教练资格证书', 1, 6500.00, '2021-06-01', '北京市海淀区', '王先生', '13900139002', '专业马术教练，擅长盛装舞步。', '北京市马术比赛二等奖', '严格要求，注重细节', 'admin', '2025-08-03 03:25:48', 'admin', '2025-08-03 03:25:48', 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for m_horse
-- ----------------------------
DROP TABLE IF EXISTS `m_horse`;
CREATE TABLE `m_horse` (
  `horse_id` bigint NOT NULL AUTO_INCREMENT COMMENT '马匹ID',
  `club_id` bigint NOT NULL COMMENT '俱乐部ID',
  `horse_name` varchar(100) NOT NULL COMMENT '马名',
  `horse_code` varchar(50) DEFAULT NULL COMMENT '马匹编号',
  `breed` varchar(50) DEFAULT NULL COMMENT '品种',
  `gender` tinyint DEFAULT NULL COMMENT '性别: 1-公 2-母 3-骟',
  `color` varchar(50) DEFAULT NULL COMMENT '毛色',
  `birth_date` datetime DEFAULT NULL COMMENT '出生日期时间',
  `chip_no` varchar(100) DEFAULT NULL COMMENT '芯片号',
  `passport_no` varchar(100) DEFAULT NULL COMMENT '护照号',
  `pedigree_cert_url` varchar(500) DEFAULT NULL COMMENT '血统证书图片地址',
  `horse_type` tinyint NOT NULL COMMENT '类型: 1-俱乐部马 2-马主马 3-教练马',
  `owner_id` bigint DEFAULT NULL COMMENT '马主ID(马主马)',
  `responsible_coach_id` bigint DEFAULT NULL COMMENT '责任教练ID',
  `responsible_groom_id` bigint DEFAULT NULL COMMENT '责任马工ID',
  `boarding_start_date` datetime DEFAULT NULL COMMENT '寄养开始日期时间',
  `boarding_end_date` datetime DEFAULT NULL COMMENT '寄养结束日期时间',
  `health_status` tinyint DEFAULT '1' COMMENT '健康状态: 1-健康 2-观察 3-治疗',
  `work_status` tinyint DEFAULT '1' COMMENT '工作状态: 1-可用 2-休息 3-治疗',
  `horse_data` text COMMENT '马匹扩展数据JSON格式',
  `create_by` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_valid` int NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
  PRIMARY KEY (`horse_id`),
  UNIQUE KEY `chip_no` (`chip_no`),
  KEY `idx_club_id` (`club_id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_chip_no` (`chip_no`),
  KEY `idx_horse_type` (`horse_type`),
  KEY `idx_horse_code` (`horse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='马匹表';

-- ----------------------------
-- Records of m_horse
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for m_horse_health_plan
-- ----------------------------
DROP TABLE IF EXISTS `m_horse_health_plan`;
CREATE TABLE `m_horse_health_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `horse_id` bigint NOT NULL COMMENT '马匹ID',
  `plan_type` varchar(50) NOT NULL COMMENT '计划类型: shoeing-钉蹄, deworming-驱虫, dental-搓牙, vaccine-疫苗, medication-用药',
  `plan_name` varchar(100) DEFAULT NULL COMMENT '计划名称',
  `cycle_days` int DEFAULT NULL COMMENT '周期天数',
  `last_date` datetime DEFAULT NULL COMMENT '上次执行日期时间',
  `next_date` datetime NOT NULL COMMENT '下次执行日期时间',
  `reminder_days` int DEFAULT '7' COMMENT '提前提醒天数',
  `plan_config` text COMMENT '计划配置JSON格式',
  `create_by` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_valid` int NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
  PRIMARY KEY (`id`),
  KEY `idx_horse_id` (`horse_id`),
  KEY `idx_next_date` (`next_date`),
  KEY `idx_plan_type` (`plan_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='马匹健康计划表';

-- ----------------------------
-- Records of m_horse_health_plan
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for m_horse_health_record
-- ----------------------------
DROP TABLE IF EXISTS `m_horse_health_record`;
CREATE TABLE `m_horse_health_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `horse_id` bigint NOT NULL COMMENT '马匹ID',
  `plan_id` bigint DEFAULT NULL COMMENT '关联计划ID',
  `record_type` varchar(50) NOT NULL COMMENT '记录类型',
  `record_date` datetime NOT NULL COMMENT '记录日期时间',
  `executor_id` bigint DEFAULT NULL COMMENT '执行人ID',
  `content` text COMMENT '记录内容',
  `img_url` text COMMENT '图片地址JSON格式',
  `next_date` datetime DEFAULT NULL COMMENT '下次执行日期时间',
  `record_data` text COMMENT '记录扩展数据JSON格式',
  `create_by` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_valid` int NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
  PRIMARY KEY (`id`),
  KEY `idx_horse_id` (`horse_id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_record_type` (`record_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='马匹健康记录表';

-- ----------------------------
-- Records of m_horse_health_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `category_type` smallint NOT NULL COMMENT '分类类型',
  `parent_id` int NOT NULL COMMENT '父级id',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `disabled_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `deleted_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分类表，主要用于商品分类';

-- ----------------------------
-- Records of t_category
-- ----------------------------
BEGIN;
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (1, '手机', 1, 0, 0, 0, 0, NULL, '2022-10-10 22:27:24', '2022-07-14 20:55:15');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (2, '键盘', 1, 0, 0, 0, 0, NULL, '2022-09-14 21:39:00', '2022-07-14 20:55:48');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (3, '自定义1', 2, 0, 0, 0, 0, NULL, '2022-09-14 22:01:06', '2022-07-14 20:56:03');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (4, '自定义2', 2, 0, 0, 0, 0, NULL, '2022-09-14 22:01:10', '2022-07-14 20:56:09');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (351, '鼠标', 1, 0, 0, 0, 0, NULL, '2022-09-14 21:39:06', '2022-09-14 21:39:06');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (352, '苹果', 1, 1, 0, 0, 0, NULL, '2022-09-14 21:39:25', '2022-09-14 21:39:25');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (353, '华为', 1, 1, 0, 0, 0, NULL, '2022-09-14 21:39:32', '2022-09-14 21:39:32');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (354, 'IKBC', 1, 2, 0, 0, 0, NULL, '2022-09-14 21:39:38', '2022-09-14 21:39:38');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (355, '双飞燕', 1, 2, 0, 0, 0, NULL, '2022-09-14 21:39:47', '2022-09-14 21:39:47');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (356, '罗技', 1, 351, 0, 0, 0, NULL, '2022-09-14 21:39:57', '2022-09-14 21:39:57');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (357, '小米', 1, 1, 0, 0, 0, NULL, '2022-10-10 22:27:39', '2022-10-10 22:27:39');
INSERT INTO `t_category` (`category_id`, `category_name`, `category_type`, `parent_id`, `sort`, `disabled_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (360, 'iphone', 1, 352, 0, 0, 0, NULL, '2023-12-04 21:26:55', '2023-12-01 19:54:22');
COMMIT;

-- ----------------------------
-- Table structure for t_change_log
-- ----------------------------
DROP TABLE IF EXISTS `t_change_log`;
CREATE TABLE `t_change_log` (
  `change_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '更新日志id',
  `update_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本',
  `type` int NOT NULL COMMENT '更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]',
  `publish_author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布人',
  `public_date` date NOT NULL COMMENT '发布日期',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新内容',
  `link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '跳转链接',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`change_log_id`) USING BTREE,
  UNIQUE KEY `version_unique` (`update_version`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统更新日志';

-- ----------------------------
-- Records of t_change_log
-- ----------------------------
BEGIN;
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (2, 'v1.1.0', 2, '卓大', '2020-05-09', 'SmartAdmin中后台系统 v1.1.0 版本（20200422）正式更新上线，更新内容如下：\n\n1.【新增】增加员工姓名查询\n\n2.【新增】增加文件预览组件\n\n3.【新增】新增四级菜单\n', 'http://smartadmin.1024lab.net/views/1.x/base/About.html', '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (8, 'v1.0.0', 1, '卓大', '2019-11-01', 'SmartAdmin中后台系统 v1.0.0 版本（20191101）正式更新上线，更新内容如下：\n\n1.【新增】人员管理\n\n2.【新增】系统设置\n\n3.【新增】心跳服务\n\n4.【新增】动态加载\n\n5.【新增】缓存策略\n\n6.【新增】定时任务', 'http://smartadmin.1024lab.net/views/1.x/base/About.html', '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (9, 'v1.2.0', 2, '卓大', '2020-05-23', 'SmartAdmin中后台系统 v1.2.0 版本（20200515）正式更新上线，更新内容如下：\n\n1.【新增】增加数据权限\n\n2.【新增】帮助文档', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (10, 'v1.2.1', 3, '卓大', '2020-05-24', 'SmartAdmin中后台系统 v1.2.1 版本（20200524）正式更新上线，更新内容如下：\n\n1.【修复】四级菜单权限bug\n\n2.【修复】缓存keepalive的Bug\n\n', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (11, 'v1.3.0', 2, '卓大', '2020-06-01', 'SmartAdmin中后台系统 v1.3.0 版本（20200601）正式更新上线，更新内容如下：\n\n1.【新增】工作台看板功能\n\n2.【新增】天气预报功能\n\n3.【新增】记录上次登录IP功能', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (12, 'v1.4.0', 2, '卓大', '2020-06-06', 'SmartAdmin中后台系统 v1.4.0 版本（20200606）正式更新上线，更新内容如下：\n\n1.【新增】联系客服功能\n\n2.【新增】意见反馈功能', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (13, 'v1.5.0', 2, '卓大', '2020-06-14', 'SmartAdmin中后台系统 v1.5.0 版本（20200614）正式更新上线，更新内容如下：\n\n1.【新增】OA系统\n\n2.【新增】通知公告', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (14, 'v1.6.0', 2, '卓大', '2020-06-17', 'SmartAdmin中后台系统 v1.6.0 版本（20200617）正式更新上线，更新内容如下：\n\n1.【新增】代码生成\n\n2.【新增】通知公告', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (15, 'v2.0.0', 1, '卓大', '2022-10-22', 'SmartAdmin中后台系统 v2.0.0 版本（20191101）正式更新上线，更新内容如下：\n\n1.【新增】人员管理\n\n2.【新增】系统设置\n\n3.【新增】心跳服务\n\n4.【新增】动态加载\n\n5.【新增】缓存策略\n\n6.【新增】定时任务', 'http://smartadmin.1024lab.net/views/1.x/base/About.html', '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (16, 'v1.7.0', 2, '卓大', '2022-10-22', 'SmartAdmin中后台系统 v1.7.0 版本（20200624）正式更新上线，更新内容如下：\n\n1.【新增】商品管理\n\n2.【新增】商品分类', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
INSERT INTO `t_change_log` (`change_log_id`, `update_version`, `type`, `publish_author`, `public_date`, `content`, `link`, `create_time`, `update_time`) VALUES (18, 'v3.0.0', 1, '卓大', '2024-01-01', 'SmartAdmin中后台系统 v3.0.0 版本（20240101）正式更新上线，更新内容如下：\n\n\n1、【新增】权限从SpringSecurity 转成 Sa-Token\n\n2、【新增】增加接口 加密、解密功能\n\n3、【新增】增加网络安全相关功能：登录限制、密码复杂度、最大在线时长等\n\n4、【新增】ant desgin vue 为 4.x 最新版本\n\n5、【新增】升级 vite5\n\n6、【新增】swagger增加knife4j接口文档\n\n7、【优化】后端sa-common 改名为 sa-base\n\n8、【优化】优化官网文档说明\n', NULL, '2022-10-04 21:33:50', '2022-10-04 21:33:50');
COMMIT;

-- ----------------------------
-- Table structure for t_code_generator_config
-- ----------------------------
DROP TABLE IF EXISTS `t_code_generator_config`;
CREATE TABLE `t_code_generator_config` (
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名',
  `basic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '基础命名信息',
  `fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '字段列表',
  `insert_and_update` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '新建、修改',
  `delete_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '删除',
  `query_fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '查询',
  `table_fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '列表',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '详情',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`table_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代码生成器的每个表的配置';

-- ----------------------------
-- Records of t_code_generator_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config` (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名字',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数key',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次修改时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统配置';

-- ----------------------------
-- Records of t_config
-- ----------------------------
BEGIN;
INSERT INTO `t_config` (`config_id`, `config_name`, `config_key`, `config_value`, `remark`, `update_time`, `create_time`) VALUES (1, '万能密码', 'super_password', '1024ok', '执行示例任务2', '2025-08-03 02:24:30', '2021-12-16 23:32:46');
INSERT INTO `t_config` (`config_id`, `config_name`, `config_key`, `config_value`, `remark`, `update_time`, `create_time`) VALUES (2, '三级等保', 'level3_protect_config', '{\n	\"fileDetectFlag\":true,\n	\"loginActiveTimeoutMinutes\":30,\n	\"loginFailLockMinutes\":30,\n	\"loginFailMaxTimes\":3,\n	\"maxUploadFileSizeMb\":30,\n	\"passwordComplexityEnabled\":true,\n	\"regularChangePasswordMonths\":3,\n	\"regularChangePasswordNotAllowRepeatTimes\":3,\n	\"twoFactorLoginEnabled\":false\n}', 'SmartJob Sample2 update', '2024-09-03 21:49:23', '2024-08-13 11:44:49');
COMMIT;

-- ----------------------------
-- Table structure for t_data_tracer
-- ----------------------------
DROP TABLE IF EXISTS `t_data_tracer`;
CREATE TABLE `t_data_tracer` (
  `data_tracer_id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` bigint NOT NULL COMMENT '各种单据的id',
  `type` int NOT NULL COMMENT '单据类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '操作内容',
  `diff_old` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '差异：旧的数据',
  `diff_new` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '差异：新的数据',
  `extra_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '额外信息',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型：1 后管用户 ',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ip',
  `ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ip地区',
  `user_agent` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ua',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`data_tracer_id`) USING BTREE,
  KEY `order_id_order_type` (`data_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='各种单据操作记录';

-- ----------------------------
-- Records of t_data_tracer
-- ----------------------------
BEGIN;
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (35, 10, 1, '新增', NULL, NULL, NULL, 47, 1, '善逸', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.61', '2023-10-07 19:02:24', '2023-10-07 19:02:24');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (36, 11, 1, '新增', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 19:55:53', '2023-12-01 19:55:53');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (37, 12, 1, '新增', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 19:57:26', '2023-12-01 19:57:26');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (38, 11, 1, '', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 19:58:09', '2023-12-01 19:58:09');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (39, 2, 3, '修改企业信息', '统一社会信用代码:\"1024lab\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:有限企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大\"<br/>省份名称:\"河南省\"<br/>企业logo:\"public/common/fb827d63dda74a60ab8b4f70cc7c7d0a_20221022145641_jpg\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室\"<br/>邮箱:\"lab1024@163.com\"', '营业执照:\"public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg\"<br/>统一社会信用代码:\"1024lab1\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:外资企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大1\"<br/>省份名称:\"河南省\"<br/>企业logo:\"\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室1\"<br/>邮箱:\"lab1024@163.com\"', NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:05:05', '2023-12-01 20:05:05');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (40, 2, 3, '修改企业信息', '营业执照:\"public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg\"<br/>统一社会信用代码:\"1024lab1\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:外资企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大1\"<br/>省份名称:\"河南省\"<br/>企业logo:\"\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室1\"<br/>邮箱:\"lab1024@163.com\"', '营业执照:\"public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg\"<br/>统一社会信用代码:\"1024lab\"<br/>详细地址:\"1024大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:外资企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"卓大\"<br/>省份名称:\"河南省\"<br/>企业logo:\"\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新实验室\"<br/>邮箱:\"lab1024@163.com\"', NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:05:54', '2023-12-01 20:05:54');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (41, 2, 3, '更新银行:<br/>', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:09:17', '2023-12-01 20:09:17');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (42, 2, 3, '更新发票：<br/>删除状态:由【false】变更为【】', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', '2023-12-01 20:09:20', '2023-12-01 20:09:20');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (49, 1, 3, '修改企业信息', '营业执照:\"public/common/852b7e19bef94af39c1a6156edf47cfb_20221022170332_jpg\"<br/>统一社会信用代码:\"1024lab_block\"<br/>详细地址:\"区块链大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:有限企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"开云\"<br/>省份名称:\"河南省\"<br/>企业logo:\"public/common/f4a76fa720814949a610f05f6f9545bf_20221022170256_jpg\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新区块链实验室\"', '营业执照:\"public/common/1d89055e5680426280446aff1e7e627c_20240306112451.jpeg\"<br/>统一社会信用代码:\"1024lab_block\"<br/>详细地址:\"区块链大楼\"<br/>区县名称:\"洛龙区\"<br/>禁用状态:false<br/>类型:有限企业<br/>城市名称:\"洛阳市\"<br/>删除状态:false<br/>联系人:\"开云\"<br/>省份名称:\"河南省\"<br/>企业logo:\"public/common/34f5ac0fc097402294aea75352c128f0_20240306112435.png\"<br/>联系人电话:\"18637925892\"<br/>企业名称:\"1024创新区块链实验室\"', NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '2024-03-06 11:24:55', '2024-03-06 11:24:55');
INSERT INTO `t_data_tracer` (`data_tracer_id`, `data_id`, `type`, `content`, `diff_old`, `diff_new`, `extra_data`, `user_id`, `user_type`, `user_name`, `ip`, `ip_region`, `user_agent`, `update_time`, `create_time`) VALUES (99, 12, 1, '', NULL, NULL, NULL, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36', '2024-09-03 21:06:32', '2024-09-03 21:06:32');
COMMIT;

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department` (
  `department_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门主键id',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `manager_id` bigint DEFAULT NULL COMMENT '部门负责人id',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '部门的父级id',
  `sort` int NOT NULL COMMENT '部门排序',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`department_id`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门';

-- ----------------------------
-- Records of t_department
-- ----------------------------
BEGIN;
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (1, '1024创新实验室', 1, 0, 1, '2022-10-19 20:17:09', '2022-10-19 20:17:09');
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (2, '开发部', 44, 1, 1000, '2022-10-19 20:22:23', '2022-10-19 20:22:23');
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (3, '产品部', 2, 1, 99, '2022-10-21 10:25:30', '2022-10-21 10:25:30');
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (4, '销售部', 64, 1, 9, '2022-10-21 10:25:47', '2022-10-21 10:25:47');
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (5, '测试部', 48, 1, 0, '2022-11-05 10:54:18', '2022-11-05 10:54:18');
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (7, '直播组', 44, 1, 1111, '2024-07-02 19:38:15', '2024-07-02 19:38:15');
INSERT INTO `t_department` (`department_id`, `department_name`, `manager_id`, `parent_id`, `sort`, `update_time`, `create_time`) VALUES (8, '抖音组', 47, 7, 0, '2024-07-02 19:39:11', '2024-07-02 19:39:11');
COMMIT;

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `dict_name` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名字',
  `dict_code` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典编码',
  `remark` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典备注',
  `disabled_flag` tinyint NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `unique_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of t_dict
-- ----------------------------
BEGIN;
INSERT INTO `t_dict` (`dict_id`, `dict_name`, `dict_code`, `remark`, `disabled_flag`, `create_time`, `update_time`) VALUES (1, '商品地区', 'GOODS_PLACE', '用于商品管理中的商品地区1', 0, '2025-03-27 14:42:26', '2025-03-31 11:23:03');
COMMIT;

-- ----------------------------
-- Table structure for t_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_data`;
CREATE TABLE `t_dict_data` (
  `dict_data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典数据id',
  `dict_id` bigint NOT NULL COMMENT '字典id',
  `data_value` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项值',
  `data_label` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项显示名称',
  `remark` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort_order` int NOT NULL COMMENT '排序（越大越靠前）',
  `disabled_flag` tinyint NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典数据表';

-- ----------------------------
-- Records of t_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `t_dict_data` (`dict_data_id`, `dict_id`, `data_value`, `data_label`, `remark`, `sort_order`, `disabled_flag`, `create_time`, `update_time`) VALUES (2, 1, 'LUO_YANG', '洛阳', 'sad', 2, 0, '2025-03-27 15:52:39', '2025-03-27 20:53:21');
INSERT INTO `t_dict_data` (`dict_data_id`, `dict_id`, `data_value`, `data_label`, `remark`, `sort_order`, `disabled_flag`, `create_time`, `update_time`) VALUES (3, 1, 'ZHENG_ZHOU', '郑州', '', 0, 0, '2025-03-27 18:58:16', '2025-03-27 20:53:32');
INSERT INTO `t_dict_data` (`dict_data_id`, `dict_id`, `data_value`, `data_label`, `remark`, `sort_order`, `disabled_flag`, `create_time`, `update_time`) VALUES (7, 1, 'BEI_JING', '北京', '', 0, 0, '2025-03-27 20:53:45', '2025-03-27 20:53:45');
INSERT INTO `t_dict_data` (`dict_data_id`, `dict_id`, `data_value`, `data_label`, `remark`, `sort_order`, `disabled_flag`, `create_time`, `update_time`) VALUES (8, 1, 'SHANG_HAI', '上海', '', 0, 0, '2025-03-27 20:53:45', '2025-03-27 20:53:45');
COMMIT;

-- ----------------------------
-- Table structure for t_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee` (
  `employee_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录帐号',
  `login_pwd` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录密码',
  `actual_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '员工名称',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别',
  `phone` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机号码',
  `department_id` bigint NOT NULL COMMENT '部门id',
  `position_id` bigint DEFAULT NULL COMMENT '职务ID',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `disabled_flag` tinyint unsigned NOT NULL COMMENT '是否被禁用 0否1是',
  `deleted_flag` tinyint unsigned NOT NULL COMMENT '是否删除0否 1是',
  `administrator_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否为超级管理员: 0 不是，1是',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`employee_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='员工表';

-- ----------------------------
-- Records of t_employee
-- ----------------------------
BEGIN;
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (1, 'admin', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '管理员', 'public/common/1eea469452484ffea4a42570c4072466_20240702220447.jpg', 0, '13500000000', 1, 3, NULL, 0, 0, 1, NULL, '2024-09-03 21:39:17', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (2, 'huke', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '胡克', NULL, 0, '13123123121', 1, 4, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:09', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (44, 'zhuoda', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '卓大', NULL, 1, '18637925892', 1, 6, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:10', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (47, 'shanyi', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '善逸', 'public/common/f823b00873684f0a9d31f0d62316cc8e_20240630015141.jpg', 1, '17630506613', 2, 5, NULL, 0, 0, 0, '这个是备注', '2024-09-03 21:36:11', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (48, 'qinjiu', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '琴酒', NULL, 2, '14112343212', 2, 6, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:12', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (63, 'kaiyun', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '开云', NULL, 0, '13112312346', 2, 5, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:13', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (64, 'qingye', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '清野', NULL, 1, '13123123111', 2, 4, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:14', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (65, 'feiye', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '飞叶', NULL, 1, '13123123112', 4, 3, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:14', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (66, 'luoyi', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '罗伊', NULL, 1, '13123123142', 4, 2, NULL, 1, 0, 0, NULL, '2024-09-03 21:36:15', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (67, 'chuxiao', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '初晓', NULL, 1, '13123123123', 1, 2, NULL, 1, 0, 0, NULL, '2024-09-03 21:36:18', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (68, 'xuanpeng', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '玄朋', NULL, 1, '13123123124', 1, 3, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:18', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (69, 'peixian', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '玄朋', NULL, 1, '18377482773', 1, 4, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:19', '2022-10-04 21:33:50');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (73, 'limbo', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', '陈琳博', NULL, 1, '18906662339', 2, 4, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:24', '2024-07-17 10:36:16');
INSERT INTO `t_employee` (`employee_id`, `login_name`, `login_pwd`, `actual_name`, `avatar`, `gender`, `phone`, `department_id`, `position_id`, `email`, `disabled_flag`, `deleted_flag`, `administrator_flag`, `remark`, `update_time`, `create_time`) VALUES (74, 'xzh', '$argon2id$v=19$m=16384,t=2,p=1$e/hqRAZYCYHydMS3SPo7yA$5hdCxLG7q+Jtf6KLJHVg/yb0I8LZrPuKUF66jLq+Drc', 'admin1', NULL, 1, '13654567897', 5, 6, NULL, 0, 0, 0, NULL, '2024-09-03 21:36:21', '2024-08-09 09:49:56');
COMMIT;

-- ----------------------------
-- Table structure for t_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback` (
  `feedback_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `feedback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '反馈内容',
  `feedback_attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '反馈图片',
  `user_id` bigint NOT NULL COMMENT '创建人id',
  `user_type` int NOT NULL COMMENT '创建人用户类型',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='意见反馈';

-- ----------------------------
-- Records of t_feedback
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
  `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `folder_type` tinyint unsigned NOT NULL COMMENT '文件夹类型',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名称',
  `file_size` int DEFAULT NULL COMMENT '文件大小',
  `file_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件key，用于文件下载',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人，即上传人',
  `creator_user_type` int DEFAULT NULL COMMENT '创建人用户类型',
  `creator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上次更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE KEY `uk_file_key` (`file_key`) USING BTREE,
  KEY `module_id_module_type` (`folder_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文件';

-- ----------------------------
-- Records of t_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods` (
  `goods_id` int NOT NULL AUTO_INCREMENT,
  `goods_status` int DEFAULT NULL COMMENT '商品状态:[1:预约中,2:售卖中,3:售罄]',
  `category_id` int NOT NULL COMMENT '商品类目',
  `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产地',
  `price` decimal(10,2) unsigned NOT NULL COMMENT '价格',
  `shelves_flag` tinyint unsigned NOT NULL COMMENT '上架状态',
  `deleted_flag` tinyint unsigned NOT NULL COMMENT '删除状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品';

-- ----------------------------
-- Records of t_goods
-- ----------------------------
BEGIN;
INSERT INTO `t_goods` (`goods_id`, `goods_status`, `category_id`, `goods_name`, `place`, `price`, `shelves_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (1, 1, 353, 'Mote60', 'BEI_JING', 9999.00, 1, 0, NULL, '2022-10-21 19:57:49', '2021-09-01 22:25:30');
INSERT INTO `t_goods` (`goods_id`, `goods_status`, `category_id`, `goods_name`, `place`, `price`, `shelves_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (7, 1, 352, 'iphone15 pro', 'LUO_YANG', 50000.00, 1, 0, '备注', '2024-06-16 09:34:08', '2022-10-21 19:58:07');
INSERT INTO `t_goods` (`goods_id`, `goods_status`, `category_id`, `goods_name`, `place`, `price`, `shelves_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (8, 1, 352, 'iphone14', 'ZHENG_ZHOU', 150.00, 0, 0, '', '2022-10-21 19:12:49', '2022-10-21 19:00:11');
INSERT INTO `t_goods` (`goods_id`, `goods_status`, `category_id`, `goods_name`, `place`, `price`, `shelves_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (10, 1, 357, '小米15', 'LUO_YANG', 7999.00, 1, 0, '', '2023-10-07 19:02:24', '2023-10-07 19:02:24');
INSERT INTO `t_goods` (`goods_id`, `goods_status`, `category_id`, `goods_name`, `place`, `price`, `shelves_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (11, 1, 354, '青轴键盘', 'ZHENG_ZHOU', 199.00, 1, 0, '支持usb', '2023-12-01 19:58:09', '2023-12-01 19:55:53');
INSERT INTO `t_goods` (`goods_id`, `goods_status`, `category_id`, `goods_name`, `place`, `price`, `shelves_flag`, `deleted_flag`, `remark`, `update_time`, `create_time`) VALUES (12, 1, 356, '罗技双模鼠标', 'BEI_JING,ZHENG_ZHOU', 99.00, 0, 0, '支持蓝牙', '2024-09-03 21:06:32', '2023-12-01 19:57:25');
COMMIT;

-- ----------------------------
-- Table structure for t_heart_beat_record
-- ----------------------------
DROP TABLE IF EXISTS `t_heart_beat_record`;
CREATE TABLE `t_heart_beat_record` (
  `heart_beat_record_id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `project_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
  `server_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器ip',
  `process_no` int NOT NULL COMMENT '进程号',
  `process_start_time` datetime NOT NULL COMMENT '进程开启时间',
  `heart_beat_time` datetime NOT NULL COMMENT '心跳时间',
  PRIMARY KEY (`heart_beat_record_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='公用服务 - 服务心跳';

-- ----------------------------
-- Records of t_heart_beat_record
-- ----------------------------
BEGIN;
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (188, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 84397, '2025-08-03 02:24:14', '2025-08-03 02:30:20');
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (189, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 85894, '2025-08-03 02:31:00', '2025-08-03 02:32:06');
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (190, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 86364, '2025-08-03 02:33:16', '2025-08-03 02:34:21');
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (191, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 86772, '2025-08-03 02:35:20', '2025-08-03 03:06:28');
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (192, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 93196, '2025-08-03 03:07:54', '2025-08-03 03:19:02');
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (193, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 96005, '2025-08-03 03:22:01', '2025-08-03 04:33:08');
INSERT INTO `t_heart_beat_record` (`heart_beat_record_id`, `project_path`, `server_ip`, `process_no`, `process_start_time`, `heart_beat_time`) VALUES (194, '/Users/mm/Desktop/鞍境/On-the-Saddle', '169.254.14.37;192.168.0.100;127.0.0.1', 12392, '2025-08-03 04:44:37', '2025-08-03 05:15:44');
COMMIT;

-- ----------------------------
-- Table structure for t_help_doc
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc`;
CREATE TABLE `t_help_doc` (
  `help_doc_id` bigint NOT NULL AUTO_INCREMENT,
  `help_doc_catalog_id` bigint NOT NULL COMMENT '类型1公告 2动态',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文本内容',
  `content_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'html内容',
  `attachment` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附件',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `page_view_count` int NOT NULL DEFAULT '0' COMMENT '页面浏览量，传说中的pv',
  `user_view_count` int NOT NULL DEFAULT '0' COMMENT '用户浏览量，传说中的uv',
  `author` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`help_doc_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='帮助文档';

-- ----------------------------
-- Records of t_help_doc
-- ----------------------------
BEGIN;
INSERT INTO `t_help_doc` (`help_doc_id`, `help_doc_catalog_id`, `title`, `content_text`, `content_html`, `attachment`, `sort`, `page_view_count`, `user_view_count`, `author`, `update_time`, `create_time`) VALUES (32, 6, '企业名称该写什么？', '需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 55, 1, '卓大', '2024-07-07 23:15:28', '2022-11-22 10:41:48');
INSERT INTO `t_help_doc` (`help_doc_id`, `help_doc_catalog_id`, `title`, `content_text`, `content_html`, `attachment`, `sort`, `page_view_count`, `user_view_count`, `author`, `update_time`, `create_time`) VALUES (33, 6, '谁有权限查看企业信息', '需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；', '<ul><li style=\"text-align: start;\">需求1：管理公司基本信息，包含：企业名称、Logo、地区、营业执照、联系人 等等，可以 增删拆改</li><li style=\"text-align: start;\">需求2：管理公司的银行账户，包含：银行信息、账户名称、账号、类型等，可以 增删拆改</li><li style=\"text-align: start;\">需求3：管理公司的发票信息，包含：开票抬头、纳税号、银行账户、开户行、备注等，可以 增删拆改</li><li style=\"text-align: start;\">需求4：对于公司信息、银行信息、发票信息 任何的修改，都有记录 数据变动记录；</li></ul>', '', 0, 13, 1, '卓大', '2024-04-10 19:36:55', '2022-11-22 10:42:19');
COMMIT;

-- ----------------------------
-- Table structure for t_help_doc_catalog
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc_catalog`;
CREATE TABLE `t_help_doc_catalog` (
  `help_doc_catalog_id` bigint NOT NULL AUTO_INCREMENT COMMENT '帮助文档目录',
  `name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `parent_id` bigint NOT NULL COMMENT '父级id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`help_doc_catalog_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='帮助文档-目录';

-- ----------------------------
-- Records of t_help_doc_catalog
-- ----------------------------
BEGIN;
INSERT INTO `t_help_doc_catalog` (`help_doc_catalog_id`, `name`, `sort`, `parent_id`, `create_time`, `update_time`) VALUES (6, '企业信息', 0, 0, '2022-11-05 10:52:40', '2022-11-22 10:37:38');
INSERT INTO `t_help_doc_catalog` (`help_doc_catalog_id`, `name`, `sort`, `parent_id`, `create_time`, `update_time`) VALUES (9, '企业信用', 0, 6, '2023-12-01 20:16:54', '2023-12-01 20:16:54');
INSERT INTO `t_help_doc_catalog` (`help_doc_catalog_id`, `name`, `sort`, `parent_id`, `create_time`, `update_time`) VALUES (10, '采购文档', 0, 11, '2023-12-01 20:17:08', '2023-12-01 20:17:29');
INSERT INTO `t_help_doc_catalog` (`help_doc_catalog_id`, `name`, `sort`, `parent_id`, `create_time`, `update_time`) VALUES (11, '进销存', 0, 0, '2023-12-01 20:17:23', '2023-12-01 20:17:23');
COMMIT;

-- ----------------------------
-- Table structure for t_help_doc_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc_relation`;
CREATE TABLE `t_help_doc_relation` (
  `relation_id` bigint NOT NULL COMMENT '关联id',
  `relation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联名称',
  `help_doc_id` bigint NOT NULL COMMENT '文档id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`relation_id`,`help_doc_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='帮助文档-关联表';

-- ----------------------------
-- Records of t_help_doc_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_help_doc_relation` (`relation_id`, `relation_name`, `help_doc_id`, `create_time`, `update_time`) VALUES (0, '首页', 32, '2023-12-04 13:34:17', '2023-12-04 13:34:17');
INSERT INTO `t_help_doc_relation` (`relation_id`, `relation_name`, `help_doc_id`, `create_time`, `update_time`) VALUES (0, '首页', 33, '2023-12-04 13:34:21', '2023-12-04 13:34:21');
COMMIT;

-- ----------------------------
-- Table structure for t_help_doc_view_record
-- ----------------------------
DROP TABLE IF EXISTS `t_help_doc_view_record`;
CREATE TABLE `t_help_doc_view_record` (
  `help_doc_id` bigint NOT NULL COMMENT '通知公告id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名称',
  `page_view_count` int DEFAULT '0' COMMENT '查看次数',
  `first_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首次ip',
  `first_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首次用户设备等标识',
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后一次ip',
  `last_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后一次用户设备等标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`help_doc_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='帮助文档-查看记录';

-- ----------------------------
-- Records of t_help_doc_view_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_login_fail
-- ----------------------------
DROP TABLE IF EXISTS `t_login_fail`;
CREATE TABLE `t_login_fail` (
  `login_fail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `login_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录名',
  `login_fail_count` int DEFAULT NULL COMMENT '连续登录失败次数',
  `lock_flag` tinyint DEFAULT '0' COMMENT '锁定状态:1锁定，0未锁定',
  `login_lock_begin_time` datetime DEFAULT NULL COMMENT '连续登录失败锁定开始时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`login_fail_id`) USING BTREE,
  UNIQUE KEY `uid_and_utype` (`user_id`,`user_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='登录失败次数记录表';

-- ----------------------------
-- Records of t_login_fail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `login_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `user_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `login_ip` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ip',
  `login_ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ip地区',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'user-agent信息',
  `login_device` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录设备',
  `login_result` int NOT NULL COMMENT '登录结果：0成功 1失败 2 退出',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`login_log_id`) USING BTREE,
  KEY `customer_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1906 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录日志';

-- ----------------------------
-- Records of t_login_log
-- ----------------------------
BEGIN;
INSERT INTO `t_login_log` (`login_log_id`, `user_id`, `user_type`, `user_name`, `login_ip`, `login_ip_region`, `user_agent`, `login_device`, `login_result`, `remark`, `update_time`, `create_time`) VALUES (1905, 1, 1, '管理员', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', '电脑端', 0, '', '2025-08-03 02:35:39', '2025-08-03 02:35:39');
COMMIT;

-- ----------------------------
-- Table structure for t_mail_template
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_template`;
CREATE TABLE `t_mail_template` (
  `template_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `template_subject` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `template_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板内容',
  `template_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '解析类型 string，freemarker',
  `disable_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`template_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_mail_template
-- ----------------------------
BEGIN;
INSERT INTO `t_mail_template` (`template_code`, `template_subject`, `template_content`, `template_type`, `disable_flag`, `update_time`, `create_time`) VALUES ('login_verification_code', '登录验证码', '<!DOCTYPE HTML>\r\n<html>\r\n<head>\r\n  <title>登录提醒</title>\r\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\r\n  <style>\r\n      * {\r\n          font-family: SimSun;\r\n          /* 4号字体 */\r\n          font-size: 18px;\r\n          /* 22磅行间距 */\r\n          line-height: 29px;\r\n      }\r\n\r\n      .main_font_size {\r\n          font-size: 12.0pt;\r\n      }\r\n\r\n      .mainContent {\r\n          line-height: 28px;\r\n      }\r\n\r\n      p {\r\n          margin: 0 auto;\r\n          text-align: justify;\r\n      }\r\n  </style>\r\n\r\n</head>\r\n<body>\r\n<div>\r\n  <div style=\"margin: 0px auto;width: 690px;\">\r\n    <div class=\"mainContent\">\r\n      <h1>验证码</h1>\r\n      <p>请在验证页面输入此验证码</p>\r\n      <p><b>${code}</b></p>\r\n      <p>验证码将于此电子邮件发出 5 分钟后过期。</p>\r\n      <p>如果你未曾提出此请求，可以忽略这封电子邮件。</p>\r\n    </div>\r\n\r\n  </div>\r\n</div>\r\n</body>\r\n</html>', 'freemarker', 0, '2024-08-06 09:13:08', '2024-07-28 13:56:06');
COMMIT;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_type` int NOT NULL COMMENT '类型',
  `parent_id` bigint NOT NULL COMMENT '父菜单ID',
  `sort` int DEFAULT NULL COMMENT '显示顺序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `perms_type` int DEFAULT NULL COMMENT '权限类型',
  `api_perms` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '后端权限字符串',
  `web_perms` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前端权限字符串',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `context_menu_id` bigint DEFAULT NULL COMMENT '功能点关联菜单ID',
  `frame_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为外链',
  `frame_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '外链地址',
  `cache_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否缓存',
  `visible_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '显示状态',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` bigint DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2435 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='菜单表';

-- ----------------------------
-- Records of t_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (26, '菜单管理', 2, 50, 1, '/menu/list', '/system/menu/menu-list.vue', NULL, NULL, NULL, 'CopyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, '2021-08-09 15:04:35', 1, '2023-12-01 19:39:03');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (40, '删除', 3, 26, NULL, NULL, NULL, 1, 'system:menu:batchDelete', 'system:menu:batchDelete', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 09:45:56', 1, '2023-10-07 18:15:50');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (45, '组织架构', 1, 0, 13, '/organization', NULL, NULL, NULL, NULL, 'UserSwitchOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 16:13:27', 1, '2025-08-03 04:30:48');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (46, '员工管理', 2, 45, 3, '/organization/employee', '/system/employee/index.vue', NULL, NULL, NULL, 'AuditOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 16:21:50', 1, '2024-07-02 20:15:23');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (47, '商品管理', 2, 48, 1, '/erp/goods/list', '/business/erp/goods/goods-list.vue', NULL, NULL, NULL, 'AliwangwangOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2021-08-12 17:58:39', 1, '2023-12-01 19:33:08');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (48, '商品管理', 1, 138, 3, '/goods', NULL, NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-12 18:02:59', 1, '2024-07-08 13:58:46');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (50, '系统设置', 1, 0, 16, '/setting', NULL, NULL, NULL, NULL, 'SettingOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-13 16:41:33', 1, '2025-08-03 04:30:30');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (76, '角色管理', 2, 45, 4, '/organization/role', '/system/role/index.vue', NULL, NULL, NULL, 'SlidersOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2021-08-26 10:31:00', 1, '2024-07-02 20:15:28');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (78, '商品分类', 2, 48, 2, '/erp/catalog/goods', '/business/erp/catalog/goods-catalog.vue', NULL, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2022-05-18 23:34:14', 1, '2023-12-01 19:33:13');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (79, '自定义分组', 2, 48, 3, '/erp/catalog/custom', '/business/erp/catalog/custom-catalog.vue', NULL, NULL, NULL, 'AppstoreAddOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-18 23:37:53', 1, '2023-12-01 19:33:16');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (81, '用户操作记录', 2, 213, 6, '/support/operate-log/operate-log-list', '/support/operate-log/operate-log-list.vue', NULL, NULL, NULL, 'VideoCameraOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-20 12:37:24', 44, '2024-08-13 14:34:10');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (85, '组件演示', 2, 84, NULL, '/demonstration/index', '/support/demonstration/index.vue', NULL, NULL, NULL, 'ClearOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-20 23:16:46', NULL, '2022-05-20 23:16:46');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (86, '添加部门', 3, 46, 1, NULL, NULL, 1, 'system:department:add', 'system:department:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:33:37', 1, '2023-10-07 18:26:35');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (87, '修改部门', 3, 46, 2, NULL, NULL, 1, 'system:department:update', 'system:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:34:11', 1, '2023-10-07 18:26:44');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (88, '删除部门', 3, 46, 3, NULL, NULL, 1, 'system:department:delete', 'system:department:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-26 23:34:49', 1, '2023-10-07 18:26:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (91, '添加员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:add', 'system:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:11:38', 1, '2023-10-07 18:27:46');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (92, '编辑员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:update', 'system:employee:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:10', 1, '2023-10-07 18:27:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (93, '禁用启用员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:disabled', 'system:employee:disabled', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:37', 1, '2023-10-07 18:27:53');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (94, '调整员工部门', 3, 46, NULL, NULL, NULL, 1, 'system:employee:department:update', 'system:employee:department:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:12:59', 1, '2023-10-07 18:27:34');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (95, '重置密码', 3, 46, NULL, NULL, NULL, 1, 'system:employee:password:reset', 'system:employee:password:reset', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:13:30', 1, '2023-10-07 18:27:57');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (96, '删除员工', 3, 46, NULL, NULL, NULL, 1, 'system:employee:delete', 'system:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:14:08', 1, '2023-10-07 18:28:01');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (97, '添加角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:add', 'system:role:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:00', 1, '2023-10-07 18:42:31');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (98, '删除角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:delete', 'system:role:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:19', 1, '2023-10-07 18:42:35');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (99, '编辑角色', 3, 76, NULL, NULL, NULL, 1, 'system:role:update', 'system:role:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:34:55', 1, '2023-10-07 18:42:44');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (100, '更新数据范围', 3, 76, NULL, NULL, NULL, 1, 'system:role:dataScope:update', 'system:role:dataScope:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:37:03', 1, '2023-10-07 18:41:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (101, '批量移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:batch:delete', 'system:role:employee:batch:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:05', 1, '2023-10-07 18:43:32');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (102, '移除员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:delete', 'system:role:employee:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:21', 1, '2023-10-07 18:43:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (103, '添加员工', 3, 76, NULL, NULL, NULL, 1, 'system:role:employee:add', 'system:role:employee:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:39:38', 1, '2023-10-07 18:44:05');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (104, '修改权限', 3, 76, NULL, NULL, NULL, 1, 'system:role:menu:update', 'system:role:menu:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:41:55', 1, '2023-10-07 18:44:11');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (105, '添加', 3, 26, NULL, NULL, NULL, 1, 'system:menu:add', 'system:menu:add', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:44:37', 1, '2023-10-07 17:35:35');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (106, '编辑', 3, 26, NULL, NULL, NULL, 1, 'system:menu:update', 'system:menu:update', NULL, 26, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 00:44:59', 1, '2023-10-07 17:35:48');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (109, '参数配置', 2, 50, 3, '/config/config-list', '/support/config/config-list.vue', NULL, NULL, NULL, 'AntDesignOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 13:34:41', 1, '2022-06-23 16:24:16');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (110, '数据字典', 2, 50, 4, '/setting/dict', '/support/dict/index.vue', NULL, NULL, NULL, 'BarcodeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-05-27 17:53:00', 1, '2022-05-27 18:09:14');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (111, '监控服务', 1, 0, 100, '/monitor', NULL, NULL, NULL, NULL, 'BarChartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-17 11:13:23', 1, '2023-11-28 17:43:56');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (113, '查询', 3, 112, NULL, NULL, NULL, NULL, NULL, 'ad', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-17 11:31:36', NULL, '2022-06-17 11:31:36');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (114, '运维工具', 1, 0, 200, NULL, NULL, NULL, NULL, NULL, 'NodeCollapseOutlined', NULL, 0, NULL, 0, 1, 0, 1, 1, '2022-06-20 10:09:16', 1, '2023-12-01 19:36:18');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (117, 'Reload', 2, 50, 12, '/hook', '/support/reload/reload-list.vue', NULL, NULL, NULL, 'ReloadOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-20 10:16:49', 1, '2023-12-01 19:39:17');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (122, '数据库监控', 2, 111, 4, '/support/druid/index', NULL, NULL, NULL, NULL, 'ConsoleSqlOutlined', NULL, 1, 'http://localhost:1024/druid', 1, 1, 0, 0, 1, '2022-06-20 14:49:33', 1, '2023-02-16 19:15:58');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (130, '单号管理', 2, 50, 6, '/support/serial-number/serial-number-list', '/support/serial-number/serial-number-list.vue', NULL, NULL, NULL, 'NumberOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 14:45:22', 1, '2022-06-28 16:23:41');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (132, '公告管理', 2, 138, 2, '/oa/notice/notice-list', '/business/oa/notice/notice-list.vue', NULL, NULL, NULL, 'SoundOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2022-06-24 18:23:09', 1, '2024-07-08 13:58:51');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (133, '缓存管理', 2, 50, 11, '/support/cache/cache-list', '/support/cache/cache-list.vue', NULL, NULL, NULL, 'BorderInnerOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 18:52:25', 1, '2023-12-01 19:39:13');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (138, '功能Demo', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 'BankOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-24 20:09:18', 1, '2024-07-08 13:46:54');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (142, '公告详情', 2, 132, NULL, '/oa/notice/notice-detail', '/business/oa/notice/notice-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-06-25 16:38:47', 1, '2022-09-14 19:46:17');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (143, '登录登出记录', 2, 213, 5, '/support/login-log/login-log-list', '/support/login-log/login-log-list.vue', NULL, NULL, NULL, 'LoginOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-06-28 15:01:38', 44, '2024-08-13 14:33:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (144, '企业管理', 2, 138, 1, '/oa/enterprise/enterprise-list', '/business/oa/enterprise/enterprise-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 17:00:07', 1, '2024-07-08 13:48:24');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (145, '企业详情', 2, 138, NULL, '/oa/enterprise/enterprise-detail', '/business/oa/enterprise/enterprise-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 18:52:52', 1, '2022-11-22 10:39:07');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (147, '帮助文档', 2, 218, 1, '/help-doc/help-doc-manage-list', '/support/help-doc/management/help-doc-manage-list.vue', NULL, NULL, NULL, 'FolderViewOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 19:59:01', 1, '2023-12-01 19:38:23');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (148, '意见反馈', 2, 218, 2, '/feedback/feedback-list', '/support/feedback/feedback-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-14 19:59:52', 1, '2023-12-01 19:38:40');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (149, '我的通知', 2, 132, NULL, '/oa/notice/notice-employee-list', '/business/oa/notice/notice-employee-list.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 20:29:41', 1, '2022-09-14 20:31:23');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (150, '我的通知公告详情', 2, 132, NULL, '/oa/notice/notice-employee-detail', '/business/oa/notice/notice-employee-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 0, 0, 0, 1, '2022-09-14 20:30:25', 1, '2022-09-14 20:31:38');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (151, '代码生成', 2, 0, 600, '/support/code-generator', '/support/code-generator/code-generator-list.vue', NULL, NULL, NULL, 'CoffeeOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-09-21 18:25:05', 1, '2022-10-22 11:27:58');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (152, '更新日志', 2, 218, 3, '/support/change-log/change-log-list', '/support/change-log/change-log-list.vue', NULL, NULL, NULL, 'HeartOutlined', NULL, 0, NULL, 0, 1, 0, 0, 44, '2022-10-10 10:31:20', 1, '2023-12-01 19:38:51');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (153, '清除缓存', 3, 133, NULL, NULL, NULL, 1, 'support:cache:delete', 'support:cache:delete', NULL, 133, 0, NULL, 0, 1, 1, 0, 1, '2022-10-15 22:45:13', 1, '2023-10-07 16:22:29');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (154, '获取缓存key', 3, 133, NULL, NULL, NULL, 1, 'support:cache:keys', 'support:cache:keys', NULL, 133, 0, NULL, 0, 1, 1, 0, 1, '2022-10-15 22:45:48', 1, '2023-10-07 16:22:35');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (156, '查看结果', 3, 117, NULL, NULL, NULL, 1, 'support:reload:result', 'support:reload:result', NULL, 117, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:17:23', 1, '2023-10-07 14:31:47');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (157, '单号生成', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:generate', 'support:serialNumber:generate', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:21:06', 1, '2023-10-07 18:22:46');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (158, '生成记录', 3, 130, NULL, NULL, NULL, 1, 'support:serialNumber:record', 'support:serialNumber:record', NULL, 130, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:21:34', 1, '2023-10-07 18:22:55');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (159, '查询', 3, 110, NULL, NULL, NULL, 1, 'support:dict:query', 'support:dict:query', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:23:51', 1, '2025-04-08 19:42:25');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (160, '添加', 3, 110, NULL, NULL, NULL, 1, 'support:dict:add', 'support:dict:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:05', 1, '2025-04-08 19:43:02');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (161, '更新', 3, 110, NULL, NULL, NULL, 1, 'support:dict:update', 'support:dict:update', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:34', 1, '2025-04-08 19:43:34');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (162, '删除', 3, 110, NULL, NULL, NULL, 1, 'support:dict:delete', 'support:dict:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:24:55', 1, '2025-04-08 19:43:52');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (163, '新建', 3, 109, NULL, NULL, NULL, 1, 'support:config:add', 'support:config:add', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:26:56', 1, '2023-10-07 18:16:17');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (164, '编辑', 3, 109, NULL, NULL, NULL, 1, 'support:config:update', 'support:config:update', NULL, 109, 0, NULL, 0, 1, 0, 0, 1, '2022-10-15 23:27:07', 1, '2023-10-07 18:16:24');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (165, '查询', 3, 47, NULL, NULL, NULL, 1, 'goods:query', 'goods:query', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:55:39', 1, '2023-10-07 13:58:28');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (166, '新建', 3, 47, NULL, NULL, NULL, 1, 'goods:add', 'goods:add', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:56:00', 1, '2023-10-07 13:58:32');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (167, '批量删除', 3, 47, NULL, NULL, NULL, 1, 'goods:batchDelete', 'goods:batchDelete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 19:56:15', 1, '2023-10-07 13:58:35');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (168, '查询', 3, 147, 11, NULL, NULL, 1, 'support:helpDoc:query', 'support:helpDoc:query', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:13', 1, '2023-10-07 14:05:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (169, '新建', 3, 147, 12, NULL, NULL, 1, 'support:helpDoc:add', 'support:helpDoc:add', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:37', 1, '2023-10-07 14:05:56');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (170, '新建目录', 3, 147, 1, NULL, NULL, 1, 'support:helpDocCatalog:addCategory', 'support:helpDocCatalog:addCategory', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:12:57', 1, '2023-10-07 14:06:38');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (171, '修改目录', 3, 147, 2, NULL, NULL, 1, 'support:helpDocCatalog:update', 'support:helpDocCatalog:update', NULL, 147, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:13:46', 1, '2023-10-07 14:06:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (173, '新建', 3, 78, NULL, NULL, NULL, 1, 'category:add', 'category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:02', 1, '2023-10-07 13:54:01');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (174, '查询', 3, 78, NULL, NULL, NULL, 1, 'category:tree', 'category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:22', 1, '2023-10-07 13:54:33');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (175, '编辑', 3, 78, NULL, NULL, NULL, 1, 'category:update', 'category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:38', 1, '2023-10-07 13:54:18');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (176, '删除', 3, 78, NULL, NULL, NULL, 1, 'category:delete', 'category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:50', 1, '2023-10-07 13:54:27');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (177, '新建', 3, 79, NULL, NULL, NULL, 1, 'category:add', 'custom:category:add', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:02', 1, '2023-10-07 13:57:32');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (178, '查询', 3, 79, NULL, NULL, NULL, 1, 'category:tree', 'custom:category:tree', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:22', 1, '2023-10-07 13:57:50');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (179, '编辑', 3, 79, NULL, NULL, NULL, 1, 'category:update', 'custom:category:update', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:38', 1, '2023-10-07 13:58:02');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (180, '删除', 3, 79, NULL, NULL, NULL, 1, 'category:delete', 'custom:category:delete', NULL, 78, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:17:50', 1, '2023-10-07 13:58:12');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (181, '查询', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:query', 'oa:enterprise:query', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:14', 1, '2023-10-07 12:00:09');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (182, '新建', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:add', 'oa:enterprise:add', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:25', 1, '2023-10-07 12:00:17');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (183, '编辑', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:update', 'oa:enterprise:update', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:36', 1, '2023-10-07 12:00:38');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (184, '删除', 3, 144, NULL, NULL, NULL, 1, 'oa:enterprise:delete', 'oa:enterprise:delete', NULL, 144, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:25:53', 1, '2023-10-07 12:00:46');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (185, '查询', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:query', 'oa:notice:query', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:26:38', 1, '2023-10-07 11:43:01');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (186, '新建', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:add', 'oa:notice:add', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:04', 1, '2023-10-07 11:43:07');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (187, '编辑', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:update', 'oa:notice:update', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:15', 1, '2023-10-07 11:43:12');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (188, '删除', 3, 132, NULL, NULL, NULL, 1, 'oa:notice:delete', 'oa:notice:delete', NULL, 132, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:27:23', 1, '2023-10-07 11:43:18');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (190, '查询', 3, 152, NULL, NULL, NULL, 1, '', 'support:changeLog:query', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:28:33', 1, '2023-10-07 14:25:05');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (191, '新建', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:add', 'support:changeLog:add', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:28:46', 1, '2023-10-07 14:24:15');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (192, '批量删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:batchDelete', 'support:changeLog:batchDelete', NULL, 152, 0, NULL, 0, 1, 0, 0, 1, '2022-10-16 20:29:10', 1, '2023-10-07 14:24:22');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (193, '文件管理', 2, 50, 20, '/support/file/file-list', '/support/file/file-list.vue', NULL, NULL, NULL, 'FolderOpenOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 11:26:11', 1, '2022-10-22 11:29:22');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (194, '删除', 3, 47, NULL, NULL, NULL, 1, 'goods:delete', 'goods:delete', NULL, 47, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:00:12', 1, '2023-10-07 13:58:39');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (195, '修改', 3, 47, NULL, NULL, NULL, 1, 'goods:update', 'goods:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:05:23', 1, '2023-10-07 13:58:42');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (196, '查看详情', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:detail', 'oa:enterprise:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:16:47', 1, '2023-10-07 11:48:59');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (198, '删除', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:delete', 'support:changeLog:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:42:34', 1, '2023-10-07 14:24:32');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (199, '查询', 3, 109, NULL, NULL, NULL, 1, 'support:config:query', 'support:config:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:45:14', 1, '2023-10-07 18:16:27');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (200, '查询', 3, 193, NULL, NULL, NULL, 1, 'support:file:query', 'support:file:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 20:47:23', 1, '2023-10-07 18:24:43');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (201, '删除', 3, 147, 14, NULL, NULL, 1, 'support:helpDoc:delete', 'support:helpDoc:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:03:20', 1, '2023-10-07 14:07:02');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (202, '更新', 3, 147, 13, NULL, NULL, 1, 'support:helpDoc:update', 'support:helpDoc:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:03:32', 1, '2023-10-07 14:06:56');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (203, '查询', 3, 143, NULL, NULL, NULL, 1, 'support:loginLog:query', 'support:loginLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-21 21:05:11', 1, '2023-10-07 14:27:23');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (204, '查询', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:query', 'support:operateLog:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:33:31', 1, '2023-10-07 14:27:56');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (205, '详情', 3, 81, NULL, NULL, NULL, 1, 'support:operateLog:detail', 'support:operateLog:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:33:49', 1, '2023-10-07 14:28:04');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (206, '心跳监控', 2, 111, 1, '/support/heart-beat/heart-beat-list', '/support/heart-beat/heart-beat-list.vue', 1, NULL, NULL, 'FallOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 10:47:03', 1, '2022-10-22 18:32:52');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (207, '更新', 3, 152, NULL, NULL, NULL, 1, 'support:changeLog:update', 'support:changeLog:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2022-10-22 11:51:32', 1, '2023-10-07 14:24:39');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (212, '查询', 3, 117, NULL, NULL, NULL, 1, 'support:reload:query', 'support:reload:query', NULL, NULL, 0, NULL, 1, 1, 1, 0, 1, '2023-10-07 14:31:36', NULL, '2023-10-07 14:31:36');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (213, '网络安全', 1, 0, 15, NULL, NULL, 1, NULL, NULL, 'SafetyCertificateOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-17 19:03:08', 1, '2025-08-03 04:30:36');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (214, '登录失败锁定', 2, 213, 4, '/support/login-fail', '/support/login-fail/login-fail-list.vue', 1, NULL, NULL, 'LockOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-17 19:04:24', 44, '2024-08-13 14:16:26');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (215, '接口加解密', 2, 213, 2, '/support/api-encrypt', '/support/api-encrypt/api-encrypt-index.vue', 1, NULL, NULL, 'CodepenCircleOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-10-24 11:49:28', 44, '2024-08-13 12:00:14');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (216, '导出', 3, 47, NULL, NULL, NULL, 1, 'goods:exportGoods', 'goods:exportGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:34:03', NULL, '2023-12-01 19:34:03');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (217, '导入', 3, 47, 3, NULL, NULL, 1, 'goods:importGoods', 'goods:importGoods', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:34:22', NULL, '2023-12-01 19:34:22');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (218, '文档中心', 1, 0, 14, NULL, NULL, 1, NULL, NULL, 'FileSearchOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2023-12-01 19:37:28', 1, '2025-08-03 04:30:41');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (219, '部门管理', 2, 45, 1, '/organization/department', '/system/department/department-list.vue', 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-06-22 16:40:21', 1, '2024-07-02 20:15:17');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (221, '定时任务', 2, 50, 25, '/job/list', '/support/job/job-list.vue', 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, NULL, 1, 1, 0, 0, 2, '2024-06-25 17:57:40', 2, '2024-06-25 19:49:21');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (228, '职务管理', 2, 45, 2, '/organization/position', '/system/position/position-list.vue', 1, NULL, NULL, 'ApartmentOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-06-29 11:11:09', 1, '2024-07-02 20:15:11');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (229, '查询任务', 3, 221, NULL, NULL, NULL, 1, 'support:job:query', 'support:job:query', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:14:15', 2, '2024-06-29 11:15:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (230, '更新任务', 3, 221, NULL, NULL, NULL, 1, 'support:job:update', 'support:job:update', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:15:40', NULL, '2024-06-29 11:15:40');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (231, '执行任务', 3, 221, NULL, NULL, NULL, 1, 'support:job:execute', 'support:job:execute', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:16:03', NULL, '2024-06-29 11:16:03');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (232, '查询记录', 3, 221, NULL, NULL, NULL, 1, 'support:job:log:query', 'support:job:log:query', NULL, 221, 0, NULL, 1, 1, 0, 0, 2, '2024-06-29 11:16:37', NULL, '2024-06-29 11:16:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (233, 'knife4j文档', 2, 218, 4, '/knife4j', NULL, 1, NULL, NULL, 'FileWordOutlined', NULL, 1, 'http://localhost:1024/doc.html', 1, 1, 0, 0, 1, '2024-07-02 20:23:50', 1, '2024-07-08 13:49:15');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (234, 'swagger文档', 2, 218, 5, '/swagger', 'http://localhost:1024/swagger-ui/index.html', 1, NULL, NULL, 'ApiOutlined', NULL, 1, 'http://localhost:1024/swagger-ui/index.html', 1, 1, 0, 0, 1, '2024-07-02 20:35:43', 1, '2024-07-08 13:49:26');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (250, '三级等保设置', 2, 213, 1, '/support/level3protect/level3-protect-config-index', '/support/level3protect/level3-protect-config-index.vue', 1, NULL, NULL, 'SafetyOutlined', NULL, 0, NULL, 1, 1, 0, 0, 44, '2024-08-13 11:41:02', 44, '2024-08-13 11:58:12');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (251, '敏感数据脱敏', 2, 213, 3, '/support/level3protect/data-masking-list', '/support/level3protect/data-masking-list.vue', 1, NULL, NULL, 'FileProtectOutlined', NULL, 0, NULL, 1, 1, 0, 0, 44, '2024-08-13 11:58:00', 44, '2024-08-13 11:59:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (252, '启用/禁用', 3, 110, NULL, NULL, NULL, 1, 'support:dict:updateDisabled', 'support:dict:updateDisabled', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:44:12', 1, '2025-04-08 19:46:03');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (253, '查询字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:query', 'support:dictData:query', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:46:47', NULL, '2025-04-08 19:46:47');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (254, '添加字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:add', 'support:dictData:add', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:00', NULL, '2025-04-08 19:48:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (255, '更新字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:update', 'support:dictData:update', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:19', NULL, '2025-04-08 19:48:19');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (256, '删除字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:delete', 'support:dictData:delete', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:38', NULL, '2025-04-08 19:48:38');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (257, '启用/禁用字典数据', 3, 110, NULL, NULL, NULL, 1, 'support:dictData:updateDisabled', 'support:dictData:updateDisabled', NULL, 110, 0, NULL, 0, 1, 0, 0, 1, '2025-04-08 19:48:57', NULL, '2025-04-08 19:48:57');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (258, '查询企业员工', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:queryEmployee', 'oa:enterprise:queryEmployee', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:11:46', 75, '2025-04-08 21:12:24');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (259, '查询银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:query', 'oa:bank:query', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:12:40', NULL, '2025-04-08 21:12:40');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (260, '查询发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:query', 'oa:invoice:query', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:12:56', NULL, '2025-04-08 21:12:56');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (261, '添加企业员工', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:addEmployee', 'oa:enterprise:addEmployee', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:35:34', NULL, '2025-04-08 21:35:34');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (262, '删除企业员工', 3, 145, NULL, NULL, NULL, 1, 'oa:enterprise:deleteEmployee', 'oa:enterprise:deleteEmployee', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:40:17', NULL, '2025-04-08 21:40:17');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (263, '添加银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:add', 'oa:bank:add', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:45:44', NULL, '2025-04-08 21:45:44');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (264, '更新银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:update', 'oa:bank:update', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:02', NULL, '2025-04-08 21:46:02');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (265, '删除银行信息', 3, 145, NULL, NULL, NULL, 1, 'oa:bank:delete', 'oa:bank:delete', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:12', NULL, '2025-04-08 21:46:12');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (266, '添加发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:add', 'oa:invoice:add', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:30', NULL, '2025-04-08 21:46:30');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (267, '更新发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:update', 'oa:invoice:update', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:47', NULL, '2025-04-08 21:46:47');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (268, '删除发票信息', 3, 145, NULL, NULL, NULL, 1, 'oa:invoice:delete', 'oa:invoice:delete', NULL, 145, 0, NULL, 0, 1, 0, 0, 75, '2025-04-08 21:46:59', NULL, '2025-04-08 21:46:59');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (300, '消息管理', 2, 50, 30, '/message', '/support/message/message-list.vue', 1, NULL, NULL, 'MailOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2025-04-09 14:30:04', 1, '2025-04-10 20:19:36');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (400, '俱乐部管理', 1, 0, 2, '/club', NULL, NULL, NULL, NULL, 'CrownOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (401, '俱乐部管理', 2, 400, 1, '/club/club/club-list', '/business/club/club-list.vue', NULL, NULL, NULL, 'ShopOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (402, '查询', 3, 401, 1, NULL, NULL, 1, 'club:club:query', 'club:club:query', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (403, '新建', 3, 401, 2, NULL, NULL, 1, 'club:club:add', 'club:club:add', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (404, '编辑', 3, 401, 3, NULL, NULL, 1, 'club:club:update', 'club:club:update', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (405, '删除', 3, 401, 4, NULL, NULL, 1, 'club:club:delete', 'club:club:delete', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (406, '详情', 3, 401, 5, NULL, NULL, 1, 'club:club:detail', 'club:club:detail', NULL, 401, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (407, '俱乐部详情页', 2, 400, 2, '/club/club/club-detail', '/business/club/club-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 1, 0, 0, 0, 1, '2025-08-03 03:03:37', 1, '2025-08-03 03:03:37');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (408, '教练管理', 2, 400, 2, '/club/coach/coach-list', '/business/coach/coach-list.vue', NULL, NULL, NULL, 'UserOutlined', NULL, 0, NULL, 1, 1, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (409, '查询', 3, 408, 1, NULL, NULL, 1, 'club:coach:query', 'club:coach:query', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (410, '新建', 3, 408, 2, NULL, NULL, 1, 'club:coach:add', 'club:coach:add', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (411, '编辑', 3, 408, 3, NULL, NULL, 1, 'club:coach:update', 'club:coach:update', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (412, '删除', 3, 408, 4, NULL, NULL, 1, 'club:coach:delete', 'club:coach:delete', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (413, '详情', 3, 408, 5, NULL, NULL, 1, 'club:coach:detail', 'club:coach:detail', NULL, 408, 0, NULL, 0, 1, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (414, '教练详情页', 2, 400, 3, '/club/coach/coach-detail', '/business/coach/coach-detail.vue', NULL, NULL, NULL, NULL, NULL, 0, NULL, 1, 0, 0, 0, 1, '2025-08-03 03:25:49', 1, '2025-08-03 03:25:49');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2400, '马匹管理', 1, 0, 3, NULL, NULL, 1, NULL, NULL, 'AlibabaOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2025-08-03 05:18:11');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2401, '马匹档案', 2, 2400, 1, '/club/horse/horse-list', '/business/horse/horse-list.vue', 1, NULL, 'club:horse:query', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2402, '马匹详情', 2, 2400, 2, '/club/horse/horse-detail', '/business/horse/horse-detail.vue', 1, NULL, 'club:horse:detail', NULL, NULL, 0, NULL, 1, 0, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2411, '马匹查询', 3, 2401, 1, NULL, NULL, 2, 'club:horse:query', 'club:horse:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2412, '马匹新增', 3, 2401, 2, NULL, NULL, 2, 'club:horse:add', 'club:horse:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2413, '马匹编辑', 3, 2401, 3, NULL, NULL, 2, 'club:horse:update', 'club:horse:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2414, '马匹删除', 3, 2401, 4, NULL, NULL, 2, 'club:horse:delete', 'club:horse:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2415, '马匹详情', 3, 2401, 5, NULL, NULL, 2, 'club:horse:detail', 'club:horse:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2421, '健康计划查询', 3, 2401, 6, NULL, NULL, 2, 'club:horse:health:plan:query', 'club:horse:health:plan:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2422, '健康计划新增', 3, 2401, 7, NULL, NULL, 2, 'club:horse:health:plan:add', 'club:horse:health:plan:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2423, '健康计划编辑', 3, 2401, 8, NULL, NULL, 2, 'club:horse:health:plan:update', 'club:horse:health:plan:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2424, '健康计划删除', 3, 2401, 9, NULL, NULL, 2, 'club:horse:health:plan:delete', 'club:horse:health:plan:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2431, '健康记录查询', 3, 2401, 10, NULL, NULL, 2, 'club:horse:health:record:query', 'club:horse:health:record:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2432, '健康记录新增', 3, 2401, 11, NULL, NULL, 2, 'club:horse:health:record:add', 'club:horse:health:record:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2433, '健康记录编辑', 3, 2401, 12, NULL, NULL, 2, 'club:horse:health:record:update', 'club:horse:health:record:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (2434, '健康记录删除', 3, 2401, 13, NULL, NULL, 2, 'club:horse:health:record:delete', 'club:horse:health:record:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');
COMMIT;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `message_type` smallint NOT NULL COMMENT '消息类型',
  `receiver_user_type` int NOT NULL COMMENT '接收者用户类型',
  `receiver_user_id` bigint NOT NULL COMMENT '接收者用户id',
  `data_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '相关数据id',
  `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `read_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读',
  `read_time` datetime DEFAULT NULL COMMENT '已读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`message_id`) USING BTREE,
  KEY `idx_msg` (`message_type`,`receiver_user_type`,`receiver_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='通知消息';

-- ----------------------------
-- Records of t_message
-- ----------------------------
BEGIN;
INSERT INTO `t_message` (`message_id`, `message_type`, `receiver_user_type`, `receiver_user_id`, `data_id`, `title`, `content`, `read_flag`, `read_time`, `create_time`, `update_time`) VALUES (1, 1, 1, 1, 'null', '张三的对公付款单 【3000元】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 0, '2024-09-02 23:00:54', '2024-06-27 01:14:07', '2024-09-03 20:44:19');
INSERT INTO `t_message` (`message_id`, `message_type`, `receiver_user_type`, `receiver_user_id`, `data_id`, `title`, `content`, `read_flag`, `read_time`, `create_time`, `update_time`) VALUES (2, 2, 1, 1, '234', '刘备的请假单【本周四】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 0, '2024-09-02 23:00:50', '2024-07-04 16:09:49', '2024-09-03 20:44:20');
INSERT INTO `t_message` (`message_id`, `message_type`, `receiver_user_type`, `receiver_user_id`, `data_id`, `title`, `content`, `read_flag`, `read_time`, `create_time`, `update_time`) VALUES (3, 1, 1, 1, '23', '武松的物资采购单【Macbook Pro】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 0, '2024-09-02 23:00:36', '2024-07-07 22:03:14', '2024-09-03 20:44:21');
INSERT INTO `t_message` (`message_id`, `message_type`, `receiver_user_type`, `receiver_user_id`, `data_id`, `title`, `content`, `read_flag`, `read_time`, `create_time`, `update_time`) VALUES (4, 1, 1, 1, '23', '孙悟空的出差申请【出差洛阳】', '尊敬的各位技术大佬：\r\n\r\n1024创新实验室技术分享即将隆重举行\r\n\r\n现将有关会议事宜通知如下：\r\n\r\n一、会议内容\r\n\r\n1、研究探讨SmartAdmin的技术体系\r\n\r\n二、会议形式\r\n\r\n大会专题小会分组讨论;\r\n\r\n三、会议时间及地点\r\n\r\n会议报到时间：xxx1年6月14日\r\n\r\n会议报到地点：洛阳市', 0, '2024-09-02 23:02:53', '2024-07-07 22:03:14', '2024-09-03 21:43:53');
COMMIT;

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `notice_type_id` bigint NOT NULL COMMENT '类型1公告 2动态',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `all_visible_flag` tinyint(1) NOT NULL COMMENT '是否全部可见',
  `scheduled_publish_flag` tinyint(1) NOT NULL COMMENT '是否定时发布',
  `publish_time` datetime NOT NULL COMMENT '发布时间',
  `content_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文本内容',
  `content_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'html内容',
  `attachment` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附件',
  `page_view_count` int NOT NULL DEFAULT '0' COMMENT '页面浏览量，传说中的pv',
  `user_view_count` int NOT NULL DEFAULT '0' COMMENT '用户浏览量，传说中的uv',
  `source` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源',
  `author` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作者',
  `document_number` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文号，如：1024创新实验室发〔2022〕字第36号',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='通知';

-- ----------------------------
-- Records of t_notice
-- ----------------------------
BEGIN;
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (49, 1, 'Spring Boot 3.0.0 首个 RC 发布', 1, 0, '2024-01-01 20:22:23', 'Spring Boot 3.0.0 首个 RC 发布\nSpring Boot 3.0 首个 RC 已发布，此外还为两个分支发布了更新：2.7.5 & 2.6.13。\n3.0.0-RC1\n发布公告写道，此版本包含 135 项功能增强、文档改进、依赖升级和 Bugfix。\nSpring Boot 3.0 的开发工作始于实验性的 Spring Native，旨在为 GraalVM 原生镜像提供支持。在该版本中，开发者现在可以使用标准 Spring Boot Maven 或 Gradle 插件将 Spring Boot 应用程序转换为原生可执行文件，而无需任何特殊配置。\n此版本还在参考文档中添加新内容来解释 AOT 处理背后的概念以及如何开始生成第一个 GraalVM 原生镜像。\n除此之外，Spring Boot 3.0 还完成了迁移到 JakartaEE 9 的工作，以及将使用的 Java 版本升级到 Java 17。\n其他新特性：\n为 Spring Data JDBC 提供更灵活的自动配置为 Prometheus 示例提供自动配置增强 Log4j2 功能，包括配置文件支持和环境属性查找\n详情查看 Release Note。\nSpring Boot 2.7.5 和 2.6.13 的更新内容主要是修复错误，优化文档和升级依赖，详情查看 Release Note (2.7.5、2.6.13)。\n相关链接\nSpring Boot 的详细介绍：点击查看Spring Boot 的下载地址：点击下载', '<h1 style=\"text-indent: 0px; text-align: start;\"><a href=\"https://www.oschina.net/news/214401/spring-boot-3-0-0-rc1-released\" target=\"_blank\">Spring&nbsp;Boot&nbsp;3.0.0&nbsp;首个&nbsp;RC&nbsp;发布</a></h1><p>Spring&nbsp;Boot&nbsp;3.0 首个&nbsp;RC 已发布，此外还为两个分支发布了更新：2.7.5 & 2.6.13。</p><p>3.0.0-RC1</p><p>发布公告写道，此版本包含 135&nbsp;项功能增强、文档改进、依赖升级和&nbsp;Bugfix。</p><p>Spring&nbsp;Boot&nbsp;3.0&nbsp;的开发工作始于实验性的&nbsp;Spring&nbsp;Native，旨在为&nbsp;GraalVM&nbsp;原生镜像提供支持。在该版本中，开发者现在可以使用标准&nbsp;Spring&nbsp;Boot&nbsp;Maven&nbsp;或&nbsp;Gradle&nbsp;插件将&nbsp;Spring&nbsp;Boot&nbsp;应用程序转换为原生可执行文件，而无需任何特殊配置。</p><p>此版本还在参考文档中添加新内容来解释 AOT&nbsp;处理背后的概念以及如何开始生成第一个&nbsp;GraalVM&nbsp;原生镜像。</p><p>除此之外，Spring&nbsp;Boot&nbsp;3.0&nbsp;还完成了迁移到 JakartaEE&nbsp;9&nbsp;的工作，以及将使用的&nbsp;Java&nbsp;版本升级到&nbsp;Java&nbsp;17。</p><p>其他新特性：</p><p>为&nbsp;Spring&nbsp;Data&nbsp;JDBC&nbsp;提供更灵活的自动配置为&nbsp;Prometheus&nbsp;示例提供自动配置增强&nbsp;Log4j2&nbsp;功能，包括配置文件支持和环境属性查找</p><p>详情查看&nbsp;Release&nbsp;Note。</p><p>Spring&nbsp;Boot&nbsp;2.7.5&nbsp;和&nbsp;2.6.13&nbsp;的更新内容主要是修复错误，优化文档和升级依赖，详情查看&nbsp;Release&nbsp;Note&nbsp;(2.7.5、2.6.13)。</p><p>相关链接</p><p>Spring&nbsp;Boot&nbsp;的详细介绍：点击查看Spring&nbsp;Boot&nbsp;的下载地址：点击下载</p>', '', 0, 0, '开源中国', '卓大', NULL, 0, 1, '2024-03-02 18:53:26', '2022-10-22 14:27:33');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (50, 1, 'Oracle 推出 JDK 8 的直接替代品', 1, 0, '2024-01-01 20:22:23', 'Oracle 推出 JDK 8 的直接替代品\n来源: OSCHINA\n编辑: 白开水不加糖\n2022-10-20 08:14:29\n 0\n为了向传统的 Java 8 服务器工作负载提供 Java 17 级别的性能，Oracle 宣布推出 Java SE Subscription Enterprise Performance Pack (Enterprise Performance Pack)。并声称这是 JDK 8 的直接替代品，现已在 MyOracleSupport 上面向所有 Java SE 订阅客户和 Oracle 云基础设施 (OCI) 用户免费提供。\n“Enterprise Performance Pack 为 JDK 8 用户提供了在 JDK 8 和 JDK 17 发布之间的 7 年时间里，为 Java 带来的重大内存管理和性能改进。这些改进包括：现代垃圾回收算法、紧凑字符串、增强的可观察性和数十种其他优化。”\nJava 8 发布于 2014 年，和 Java 17 一样都是长期支持 (LTS) 版本；尽管发布距今已有近九年的历史，但仍被很多开发人员和组织所广泛应用。New Relic 发布的一份 “2022 年 Java 生态系统状况报告” 数据表明，Java 8 仍被 46.45% 的 Java 应用程序在生产中使用。\n根据介绍，Enterprise Performance Pack 在 Intel 和基于 Arm 的系统（如 Ampere Altra）上支持 headless Linux 64 位工作负载。\nOracle 方面称，使用 Enterprise Performance Pack 的客户将可以立即看到以或接近内存或 CPU 容量运行的 JDK 8 工作负载的好处。在 Oracle 自己的产品和云服务进行的测试表明，高负载应用程序的内存和性能都提高了大约 40%。即使没有接近容量运行的 JDK 8 应用程序，也可以会看到高达 5% 的性能提升。\n虽然 Enterprise Performance Pack 中包含的许多改进可以通过默认选项获得，但 Oracle 建议用户还是自己研究文档，以最大限度地提高性能并最大限度地降低内存使用率。例如，通过启用可扩展的低延迟 ZGC 垃圾收集器来提高应用程序响应能力，需要通过 -XX:+UseZGC 选项。', '<h3>Oracle&nbsp;推出&nbsp;JDK&nbsp;8&nbsp;的直接替代品</h3><p>来源:&nbsp;OSCHINA</p><p>编辑: 白开水不加糖</p><p>2022-10-20&nbsp;08:14:29</p><p> 0</p><p>为了向传统的&nbsp;Java&nbsp;8&nbsp;服务器工作负载提供&nbsp;Java&nbsp;17&nbsp;级别的性能，Oracle 宣布推出&nbsp;Java&nbsp;SE&nbsp;Subscription&nbsp;Enterprise&nbsp;Performance&nbsp;Pack&nbsp;(Enterprise&nbsp;Performance&nbsp;Pack)。并声称这是 JDK&nbsp;8&nbsp;的直接替代品，现已在 MyOracleSupport 上面向所有&nbsp;Java&nbsp;SE&nbsp;订阅客户和&nbsp;Oracle&nbsp;云基础设施&nbsp;(OCI)&nbsp;用户免费提供。</p><p>“Enterprise&nbsp;Performance&nbsp;Pack&nbsp;为&nbsp;JDK&nbsp;8&nbsp;用户提供了在&nbsp;JDK&nbsp;8&nbsp;和&nbsp;JDK&nbsp;17&nbsp;发布之间的&nbsp;7&nbsp;年时间里，为&nbsp;Java&nbsp;带来的重大内存管理和性能改进。这些改进包括：现代垃圾回收算法、紧凑字符串、增强的可观察性和数十种其他优化。”</p><p>Java&nbsp;8&nbsp;发布于&nbsp;2014&nbsp;年，和&nbsp;Java&nbsp;17&nbsp;一样都是长期支持&nbsp;(LTS)&nbsp;版本；尽管发布距今已有近九年的历史，但仍被很多开发人员和组织所广泛应用。New&nbsp;Relic&nbsp;发布的一份 “2022&nbsp;年&nbsp;Java&nbsp;生态系统状况报告”&nbsp;数据表明，Java&nbsp;8&nbsp;仍被&nbsp;46.45%&nbsp;的&nbsp;Java&nbsp;应用程序在生产中使用。</p><p>根据介绍，Enterprise&nbsp;Performance&nbsp;Pack&nbsp;在&nbsp;Intel&nbsp;和基于&nbsp;Arm&nbsp;的系统（如&nbsp;Ampere&nbsp;Altra）上支持 headless&nbsp;Linux&nbsp;64&nbsp;位工作负载。</p><p>Oracle 方面称，使用&nbsp;Enterprise&nbsp;Performance&nbsp;Pack&nbsp;的客户将可以立即看到以或接近内存或&nbsp;CPU&nbsp;容量运行的&nbsp;JDK&nbsp;8&nbsp;工作负载的好处。在&nbsp;Oracle&nbsp;自己的产品和云服务进行的测试表明，高负载应用程序的内存和性能都提高了大约&nbsp;40%。即使没有接近容量运行的&nbsp;JDK&nbsp;8&nbsp;应用程序，也可以会看到高达&nbsp;5%&nbsp;的性能提升。</p><p>虽然&nbsp;Enterprise&nbsp;Performance&nbsp;Pack&nbsp;中包含的许多改进可以通过默认选项获得，但 Oracle 建议用户还是自己研究文档，以最大限度地提高性能并最大限度地降低内存使用率。例如，通过启用可扩展的低延迟&nbsp;ZGC&nbsp;垃圾收集器来提高应用程序响应能力，需要通过&nbsp;-XX:+UseZGC&nbsp;选项。</p>', '', 0, 0, 'OSChina', '卓大', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:29:56');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (51, 1, 'Spring Framework 6.0.0 RC2 发布', 1, 0, '2024-01-01 20:22:23', 'Spring Framework 6.0.0 RC2 发布\nSpring Framework 6.0.0 发布了第二个 RC 版本。\n新特性\n确保可以在构建时评估 classpath 检查 #29352为 JPA 持久化回调引入 Register 反射提示 #29348检查 @RegisterReflectionForBinding 是否至少指定一个类 #29346为 AOT 引擎设置引入 builder API #29341支持检测正在进行的 AOT 处理 #29340重新组织 HTTP Observation 类型 #29334支持在没有 java.beans.Introspector 的前提下，执行基本属性判断 #29320为BindingReflectionHintsRegistrar 添加 Kotlin 数据类组件支持 #29316将 HttpServiceFactory 和 RSocketServiceProxyFactory 切换到 builder 模型，以便优先进行可编程配置 #29296引入基于 GraalVM FieldValueTransformer API 的 PreComputeFieldFeature#29081在 TestContext 框架中引入 SPI 来处理 ApplicationContext 故障 #28826SimpleEvaluationContext 支持禁用 array 分配 #28808DateTimeFormatterRegistrar 支持默认回退到 ISO 解析 #26985\nSpring Framework 6.0 作为重大更新，要求使用 Java 17 或更高版本，并且已迁移到 Jakarta EE 9+（在 jakarta 命名空间中取代了以前基于 javax 的 EE API），以及对其他基础设施的修改。基于这些变化，Spring Framework 6.0 支持最新 Web 容器，如 Tomcat 10 / Jetty 11，以及最新的持久性框架 Hibernate ORM 6.1。这些特性仅可用于 Servlet API 和 JPA 的 jakarta 命名空间变体。\n值得一提的是，开发者可通过此版本在基于 Spring 的应用中体验 “虚拟线程”（JDK 19 中的预览版 “Project Loom”），查看此文章了解更多细节。现在提供了自定义选项来插入基于虚拟线程的 Executor 实现，目标是在 Project Loom 正式可用时提供 “一等公民” 的配置选项。\n除了上述的变化，Spring Framework 6.0 还包含许多其他改进和特性，例如：\n提供基于 @HttpExchange 服务接口的 HTTP 接口客户端对 RFC 7807 问题详细信息的支持Spring HTTP 客户端提供基于 Micrometer 的可观察性……\n详情查看 Release Note。\n按照发布计划，Spring Framework 6.0 将于 11 月正式 GA。', '<h1 style=\"text-indent: 0px; text-align: start;\"><a href=\"https://www.oschina.net/news/214472/spring-framework-6-0-0-rc2-released\" target=\"_blank\">Spring&nbsp;Framework&nbsp;6.0.0&nbsp;RC2&nbsp;发布</a></h1><p style=\"text-indent: 0px; text-align: left;\">Spring&nbsp;Framework&nbsp;6.0.0&nbsp;发布了<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fspring.io%2Fblog%2F2022%2F10%2F20%2Fspring-framework-6-0-0-rc2-available-now\" target=\"_blank\">第二个&nbsp;RC&nbsp;版本</a>。</p><p style=\"text-indent: 0px; text-align: left;\"><strong>新特性</strong></p><ul style=\"text-indent: 0px; text-align: left;\"><li>确保可以在构建时评估&nbsp;classpath&nbsp;检查&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29352\" target=\"_blank\">#29352</a></li><li>为&nbsp;JPA&nbsp;持久化回调引入&nbsp;Register&nbsp;反射提示&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29348\" target=\"_blank\">#29348</a></li><li>检查&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>@RegisterReflectionForBinding</code></span>&nbsp;是否至少指定一个类&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29346\" target=\"_blank\">#29346</a></li><li>为&nbsp;AOT&nbsp;引擎设置引入&nbsp;builder&nbsp;API&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29341\" target=\"_blank\">#29341</a></li><li>支持检测正在进行的&nbsp;AOT&nbsp;处理&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29340\" target=\"_blank\">#29340</a></li><li>重新组织&nbsp;HTTP&nbsp;Observation&nbsp;类型&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29334\" target=\"_blank\">#29334</a></li><li>支持在没有&nbsp;java.beans.Introspector&nbsp;的前提下，执行基本属性判断&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29320\" target=\"_blank\">#29320</a></li><li>为<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>BindingReflectionHintsRegistrar</code></span>&nbsp;添加&nbsp;Kotlin&nbsp;数据类组件支持&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29316\" target=\"_blank\">#29316</a></li><li>将&nbsp;HttpServiceFactory&nbsp;和&nbsp;RSocketServiceProxyFactory&nbsp;切换到&nbsp;builder&nbsp;模型，以便优先进行可编程配置&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29296\" target=\"_blank\">#29296</a></li><li>引入基于&nbsp;GraalVM&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>FieldValueTransformer</code></span>&nbsp;API&nbsp;的&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>PreComputeFieldFeature</code></span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F29081\" target=\"_blank\">#29081</a></li><li>在&nbsp;TestContext&nbsp;框架中引入&nbsp;SPI&nbsp;来处理&nbsp;ApplicationContext&nbsp;故障&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F28826\" target=\"_blank\">#28826</a></li><li>SimpleEvaluationContext&nbsp;支持禁用&nbsp;array&nbsp;分配&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F28808\" target=\"_blank\">#28808</a></li><li>DateTimeFormatterRegistrar&nbsp;支持默认回退到&nbsp;ISO&nbsp;解析&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Fissues%2F26985\" target=\"_blank\">#26985</a></li></ul><p style=\"text-indent: 0px; text-align: left;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">Spring&nbsp;Framework&nbsp;6.0&nbsp;作为重大更新，要求</span><span style=\"color: rgb(51, 51, 51);\"><strong>使用&nbsp;Java&nbsp;17&nbsp;或更高版本</strong></span><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">，并且已迁移到&nbsp;Jakarta&nbsp;EE&nbsp;9+（在&nbsp;</span><span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>jakarta</code></span><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">&nbsp;命名空间中取代了以前基于&nbsp;</span><span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>javax</code></span><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">&nbsp;的&nbsp;EE&nbsp;API），以及对其他基础设施的修改。基于这些变化，Spring&nbsp;Framework&nbsp;6.0&nbsp;支持最新&nbsp;Web&nbsp;容器，如&nbsp;</span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Ftomcat.apache.org%2Fwhichversion.html\" target=\"_blank\">Tomcat&nbsp;10</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">&nbsp;/&nbsp;</span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fwww.eclipse.org%2Fjetty%2Fdownload.php\" target=\"_blank\">Jetty&nbsp;11</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">，以及最新的持久性框架&nbsp;</span><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fhibernate.org%2Form%2Freleases%2F6.1%2F\" target=\"_blank\">Hibernate&nbsp;ORM&nbsp;6.1</a><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">。这些特性仅可用于&nbsp;Servlet&nbsp;API&nbsp;和&nbsp;JPA&nbsp;的&nbsp;jakarta&nbsp;命名空间变体。</span></p><p style=\"text-indent: 0px; text-align: left;\">值得一提的是，开发者可通过此版本在基于&nbsp;Spring&nbsp;的应用中体验&nbsp;“虚拟线程”（JDK&nbsp;19&nbsp;中的预览版&nbsp;“Project&nbsp;Loom”），<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fspring.io%2Fblog%2F2022%2F10%2F11%2Fembracing-virtual-threads\" target=\"_blank\">查看此文章</a>了解更多细节。现在提供了自定义选项来插入基于虚拟线程的&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>Executor</code></span>&nbsp;实现，目标是在&nbsp;Project&nbsp;Loom&nbsp;正式可用时提供&nbsp;“一等公民”&nbsp;的配置选项。</p><p style=\"text-indent: 0px; text-align: left;\">除了上述的变化，Spring&nbsp;Framework&nbsp;6.0&nbsp;还包含许多其他改进和特性，例如：</p><ul style=\"text-indent: 0px; text-align: left;\"><li>提供基于&nbsp;<span style=\"color: rgb(51, 51, 51); font-size: 13px;\"><code>@HttpExchange</code></span>&nbsp;服务接口的&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdocs.spring.io%2Fspring-framework%2Fdocs%2F6.0.0-RC1%2Freference%2Fhtml%2Fintegration.html%23rest-http-interface\" target=\"_blank\">HTTP&nbsp;接口客户端</a></li><li>对&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdocs.spring.io%2Fspring-framework%2Fdocs%2F6.0.0-RC1%2Freference%2Fhtml%2Fweb.html%23mvc-ann-rest-exceptions\" target=\"_blank\">RFC&nbsp;7807&nbsp;问题详细信息</a>的支持</li><li>Spring&nbsp;HTTP&nbsp;客户端提供基于&nbsp;Micrometer&nbsp;的可观察性</li><li>……</li></ul><p style=\"text-indent: 0px; text-align: left;\"><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework%2Freleases%2Ftag%2Fv6.0.0-RC2\" target=\"_blank\">详情查看&nbsp;Release&nbsp;Note</a>。</p><p style=\"text-indent: 0px; text-align: left;\">按照发布计划，Spring&nbsp;Framework&nbsp;6.0&nbsp;将于&nbsp;11&nbsp;月正式&nbsp;GA。</p>', '', 0, 0, 'CSDN', '罗伊', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:30:45');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (52, 1, 'Windows Terminal 正式成为 Windows 11 默认终端', 1, 0, '2024-01-01 20:22:23', '今年 7 月 ，微软在 Windows 11 的 Beta 版本测试了将系统默认终端设置为 Windows Terminal 。如今该设置已登录稳定版本，从 Windows 11 22H2 版本开始，Windows Terminal 将正式成为 Windows 11 的默认设置。\n默认终端是在打开命令行应用程序时默认启动的终端模拟器。从 Windows 诞生之日起，其默认终端一直是 Windows 控制台主机 conhost.exe。此次更新则意味着，以后 Windows 11 的所有命令行应用程序都将在 Windows Terminal 中自动打开。\nWindows Terminal 拥有非常多现代化的功能，毕竟它很新（ 2019 年 5 月在 Microsoft Build 上首次发布），吸取了很多现代终端的灵感。它支持多选项卡和窗格、命令面板等现代化的 UI 和操作方式，以及大量的自定义选项，比如目录、配置文件图标、自定义背景图像、配色方案、字体和透明度。\n当然，如果不想用 Windows Terminal，用户也可以在 Windows 设置中的 隐私和安全 > 开发人员页面和 Windows 终端设置 中调整默认终端设置，（此更新使用 “让 Windows 决定” 作为默认选择，即默认采用 Windows Terminal） 。\n此外，如果在更新之前就已设置其他默认终端，此次更新不会覆盖你的偏好。\n关于 Windows 11 默认终端的更多详情可查看微软博客。', '<p style=\"text-indent: 0px; text-align: left;\">今年&nbsp;7&nbsp;月&nbsp;，微软在&nbsp;Windows&nbsp;11&nbsp;的&nbsp;Beta&nbsp;版本<a href=\"https://www.oschina.net/news/204429/wt-default-terminal-in-win11-beta-channel\" target=\"\">测试</a>了将系统默认终端设置为&nbsp;Windows&nbsp;Terminal&nbsp;。如今该设置已登录稳定版本，从&nbsp;Windows&nbsp;11&nbsp;22H2&nbsp;版本开始，Windows&nbsp;Terminal&nbsp;将<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdevblogs.microsoft.com%2Fcommandline%2Fwindows-terminal-is-now-the-default-in-windows-11%2F\" target=\"_blank\">正式成为</a>&nbsp;Windows&nbsp;11&nbsp;的默认设置。</p><p style=\"text-indent: 0px; text-align: left;\">默认终端是在打开命令行应用程序时默认启动的终端模拟器。从&nbsp;Windows&nbsp;诞生之日起，其默认终端一直是&nbsp;Windows&nbsp;控制台主机&nbsp;conhost.exe。此次更新则意味着，以后&nbsp;Windows&nbsp;11&nbsp;的所有命令行应用程序都将在&nbsp;Windows&nbsp;Terminal&nbsp;中自动打开。</p><p style=\"text-indent: 0px; text-align: left;\">Windows&nbsp;Terminal&nbsp;拥有非常多现代化的功能，毕竟它<span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255);\">很新（&nbsp;2019&nbsp;年&nbsp;5&nbsp;月在&nbsp;Microsoft&nbsp;Build&nbsp;上首次发布），吸取了很多现代终端的灵感。它支持多</span>选项卡和窗格、命令面板等现代化的&nbsp;UI&nbsp;和操作方式，以及大量的自定义选项，比如目录、配置文件图标、自定义背景图像、配色方案、字体和透明度。</p><p style=\"text-indent: 0px; text-align: left;\">当然，如果不想用&nbsp;Windows&nbsp;Terminal，用户也可以在&nbsp;Windows&nbsp;设置中的&nbsp;<em>隐私和安全&nbsp;&gt;&nbsp;开发人员页面和&nbsp;Windows&nbsp;终端设置&nbsp;</em>中调整默认终端设置，（此更新使用&nbsp;“让&nbsp;Windows&nbsp;决定”&nbsp;作为默认选择，即默认采用&nbsp;Windows&nbsp;Terminal）&nbsp;。</p><p style=\"text-indent: 0px; text-align: left;\">此外，如果在更新之前就已设置其他默认终端，此次更新<strong>不会覆盖</strong>你的偏好。</p><p style=\"text-indent: 0px; text-align: left;\">关于&nbsp;Windows&nbsp;11&nbsp;默认终端的更多详情可查看<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fdevblogs.microsoft.com%2Fcommandline%2Fwindows-terminal-is-now-the-default-in-windows-11%2F\" target=\"_blank\">微软博客</a>。</p>', '', 0, 0, '开源中国', '善逸', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:33:03');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (53, 1, 'TypeScript 诞生 10 周年', 1, 0, '2024-01-01 20:22:23', 'TypeScript 已经诞生 10 年了。10 年前 ——2012 年 10 月 1 日，TypeScript 首次公开亮相。当时主导 TypeScript 开发的 Anders Hejlsberg 这样描述 TypeScript：\n它是 JavaScript 的类型化超集，可被编译成常用的 JavaScript。TypeScript 还可以通过启用丰富的工具体验来极大地帮助提升生产力，与此同时开发者保持不变维护现有的代码，并继续使用喜爱的 JavaScript 库。TypeScript is a typed superset of JavaScript that compiles to idiomatic (normal) JavaScript, can dramatically improve your productivity by enabling rich tooling experiences, all while maintaining your existing code and continuing to use the same JavaScript libraries you already love.\n微软在博客中回顾了 TypeScript 刚亮相时受到的评价，大多数人对它都是持怀疑态度，毕竟这对于许多 JavaScript 开发者来说，试图将静态类型引入 JavaScript 是一个笑话 —— 或是邪恶的阴谋。反对者则直言这是十分愚蠢的想法，他们认为当时已存在可以编译为 JavaScript 的强类型语言，例如 C#、Java 和 C++。他们还吐槽主导 TypeScript 开发的 Anders Hejlsberg 对静态类型有 “迷之执着”。\n当时微软意识到 JavaScript 未来将会被应用到无数场景，而且他们公司内部团队在处理复杂的 JavaScript 代码库时面临着巨大的挑战，所以他们觉得有必要创造强大的工具来帮助编写 JavaScript—— 尤其是针对大型 JavaScript 项目。基于此需求，TypeScript 也确定了自己的定位和特性，它是 JavaScript 的超集，将类型检查和静态分析、显式接口和最佳实践结合到单一语言和编译器中。通过在 JavaScript 上构建，TypeScript 能够更接近目标运行时，同时仅添加支持大型应用程序和大型团队所需的语法糖。\n团队还坚持 TypeScript 要能够与现有的 JavaScript 无缝交互，与 JavaScript 共同进化，并且看上去也和 JavaScript 类似。\nTypeScript 诞生之初的部分设计目标：\n不会对已有的程序增加运行时开销与当前和未来的 ECMAScript 提案保持一致保留所有 JavaScript 代码的运行时行为避免添加表达式类型的语法 (expression-level syntax)使用一致、完全可擦除的结构化类型系统……\n这些目标指导着 TypeScript 的发展方向：关注类型系统，成为 JavaScript 的类型检查器，只添加类型检查所需的语法，避免添加新的运行时语法和行为。\n微软提到，TypeScript 拥有如今的繁荣生态离不开一个重要属性：开源。TypeScript 一开始就是免费且开源 —— 语言规范和编译器都是开源项目，并且以真正开放的方式来运作。事实上，微软当时对外展现出的姿态并不是现在的 “拥抱开源”，所以他们内部并没真正认识到 TypeScript 的开源是如何帮助它走向成功。因此有人认为，TypeScript 在很大程度上引导微软开始更多地转向开源。\n现在，TypeScript 仍在积极发展和迭代改进，并被全球数百万开发者使用。在诸多编程语言排名、指数或开发者调查中，TypeScript 一直位居前列，也是最受欢迎和最常用的编程语言。', '<p style=\"text-indent: 0px; text-align: start;\">TypeScript&nbsp;已经诞生&nbsp;10&nbsp;年了。10&nbsp;年前&nbsp;——2012&nbsp;年&nbsp;10&nbsp;月&nbsp;1&nbsp;日，TypeScript&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fweb.archive.org%2Fweb%2F20121003001910%2Fhttps%3A%2F%2Fblogs.msdn.com%2Fb%2Fsomasegar%2Farchive%2F2012%2F10%2F01%2Ftypescript-javascript-development-at-application-scale.aspx\" target=\"_blank\"><strong>首次公开亮相</strong></a>。当时主导&nbsp;TypeScript&nbsp;开发的&nbsp;Anders&nbsp;Hejlsberg&nbsp;这样描述&nbsp;TypeScript：</p><blockquote style=\"text-indent: 0px; text-align: left;\">它是&nbsp;JavaScript&nbsp;的类型化超集，可被编译成常用的&nbsp;JavaScript。TypeScript&nbsp;还可以通过启用丰富的工具体验来极大地帮助提升生产力，与此同时开发者保持不变维护现有的代码，并继续使用喜爱的&nbsp;JavaScript&nbsp;库。TypeScript&nbsp;is&nbsp;a&nbsp;typed&nbsp;superset&nbsp;of&nbsp;JavaScript&nbsp;that&nbsp;compiles&nbsp;to&nbsp;idiomatic&nbsp;(normal)&nbsp;JavaScript,&nbsp;can&nbsp;dramatically&nbsp;improve&nbsp;your&nbsp;productivity&nbsp;by&nbsp;enabling&nbsp;rich&nbsp;tooling&nbsp;experiences,&nbsp;all&nbsp;while&nbsp;maintaining&nbsp;your&nbsp;existing&nbsp;code&nbsp;and&nbsp;continuing&nbsp;to&nbsp;use&nbsp;the&nbsp;same&nbsp;JavaScript&nbsp;libraries&nbsp;you&nbsp;already&nbsp;love.</blockquote><p style=\"text-indent: 0px; text-align: left;\">微软在博客中回顾了&nbsp;TypeScript&nbsp;刚亮相时受到的评价，大多数人对它都是持怀疑态度，毕竟这对于许多&nbsp;JavaScript&nbsp;开发者来说，试图将静态类型引入&nbsp;JavaScript&nbsp;是一个笑话&nbsp;——&nbsp;或是邪恶的阴谋。反对者则直言这是十分愚蠢的想法，他们认为当时已存在可以编译为&nbsp;JavaScript&nbsp;的强类型语言，例如&nbsp;C#、Java&nbsp;和&nbsp;C++。他们还吐槽主导&nbsp;TypeScript&nbsp;开发的&nbsp;Anders&nbsp;Hejlsberg&nbsp;对静态类型有&nbsp;“迷之执着”。</p><p style=\"text-indent: 0px; text-align: start;\">当时微软意识到&nbsp;JavaScript&nbsp;未来将会被应用到无数场景，而且他们公司内部团队在处理复杂的&nbsp;JavaScript&nbsp;代码库时面临着巨大的挑战，所以他们觉得有必要创造强大的工具来帮助编写&nbsp;JavaScript——&nbsp;尤其是针对大型&nbsp;JavaScript&nbsp;项目。基于此需求，TypeScript&nbsp;也确定了自己的定位和特性，它是&nbsp;JavaScript&nbsp;的超集，将类型检查和静态分析、显式接口和最佳实践结合到单一语言和编译器中。通过在&nbsp;JavaScript&nbsp;上构建，TypeScript&nbsp;能够更接近目标运行时，同时仅添加支持大型应用程序和大型团队所需的语法糖。</p><p style=\"text-indent: 0px; text-align: start;\">团队还坚持&nbsp;TypeScript&nbsp;要能够与现有的&nbsp;JavaScript&nbsp;无缝交互，与&nbsp;JavaScript&nbsp;共同进化，并且看上去也和&nbsp;JavaScript&nbsp;类似。</p><p style=\"text-indent: 0px; text-align: start;\">TypeScript&nbsp;诞生之初的部分<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fgithub.com%2Fmicrosoft%2FTypeScript%2Fwiki%2FTypeScript-Design-Goals%2F53ffa9b1802cd8e18dfe4b2cd4e9ef5d4182df10\" target=\"_blank\"><strong>设计目标</strong></a>：</p><ul style=\"text-indent: 0px; text-align: left;\"><li>不会对已有的程序增加运行时开销</li><li>与当前和未来的&nbsp;ECMAScript&nbsp;提案保持一致</li><li>保留所有&nbsp;JavaScript&nbsp;代码的运行时行为</li><li>避免添加表达式类型的语法&nbsp;(expression-level&nbsp;syntax)</li><li>使用一致、完全可擦除的结构化类型系统</li><li>……</li></ul><p style=\"text-indent: 0px; text-align: start;\">这些目标指导着&nbsp;TypeScript&nbsp;的发展方向：关注类型系统，成为&nbsp;JavaScript&nbsp;的类型检查器，只添加类型检查所需的语法，避免添加新的运行时语法和行为。</p><p style=\"text-indent: 0px; text-align: start;\">微软提到，TypeScript&nbsp;拥有如今的繁荣生态离不开一个重要属性：<strong>开源</strong>。TypeScript&nbsp;一开始就是免费且开源&nbsp;——<span style=\"color: rgb(51, 51, 51);\">&nbsp;语言规范和编译器都是开源项目，</span>并且以真正开放的方式来运作。事实上，微软当时对外展现出的姿态并不是现在的&nbsp;“拥抱开源”，所以他们内部并没真正认识到&nbsp;TypeScript&nbsp;的开源是如何帮助它走向成功。因此有人认为，TypeScript&nbsp;在很大程度上引导微软开始更多地转向开源。</p><p style=\"text-indent: 0px; text-align: start;\">现在，TypeScript&nbsp;仍在积极发展和迭代改进，并被全球数百万开发者使用。在诸多编程语言排名、指数或开发者调查中，TypeScript&nbsp;一直位居前列，也是最受欢迎和最常用的编程语言。</p>', '', 0, 0, '开源中国', '开云', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:34:56');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (54, 1, 'JetBrains Fleet 公测，下一代 IDE', 1, 0, '2024-01-01 20:22:23', 'JetBrains 宣布首次公共预览 Fleet，所有人都可以使用。Fleet 是由 JetBrains 打造的下一代 IDE，于 2021 年首次正式推出。它是一个新的分布式多语言编辑器和 IDE，基于 JetBrains 在后端的 IntelliJ 平台，采用了全新的用户界面和分布式架构从头开始构建。\n下载 Fleet：https://www.jetbrains.com.cn/fleet/download/\n\n公告表示，自从最初宣布 Fleet 以来，有超过 137,000 人报名参加私人预览；官方最初之所以决定从封闭式预览开始，是为了能够以渐进的方式处理反馈。现如今，JetBrains Fleet 仍处于起步阶段，还有大量的工作要做。其向公众开放预览的原因有两个方面：“首先，我们认为让所有注册者再等下去是不对的，但单独邀请这么多人对我们来说也缺乏意义。面向公众开放预览对我们来说更容易。第二，也是最重要的，我们一直是一家以开放态度打造产品的公司。我们不希望 Fleet 在这方面有任何不同。”\nJetBrains 方面提供了一个图表，以显示 Fleet 目前提供支持的语言和技术，以及每个技术的状态。但值得注意的是，Fleet 仍处于早期阶段，有些事情可能无法按预期工作；所以即使有些东西被列为受支持的，也有可能存在问题。\n同时 JetBrains 也强调称，他们并不打算取代其现有的 IDE。\n因此，请不要期望在 Fleet 中看到与我们的 IDE（如 IntelliJ IDEA）完全相同的功能。尽管我们会继续开发 Fleet，我们 IDE 的所有功能也不会出现在其中。Fleet 是我们为开发者提供不同用户体验的一个机会。话虽如此，我们确实希望听到你认为 Fleet 还缺少什么功能的反馈，例如特定的重构选项、工具集成等。我们现有的 IDE 将继续发展。我们对其有很多计划，包括性能改进、新的用户界面、远程开发等等。最后，Fleet 还在底层采用了我们现有工具的智慧，所以这些工具都不会消失。\nJetBrains 透露，在未来几个月他们将致力于稳定 Fleet，并尽可能地解决得到的反馈。同时，将在以下领域开展工作：\n为插件作者提供 API 支持和 SDK–鉴于 Fleet 有一个分布式架构，我们需要努力为插件作者简化工作。 虽然我们保证会为扩展 Fleet 提供一个平台，但也请求大家在这方面多一点耐心。 性能 – 我们希望 Fleet 不仅在内存占用方面，而且在响应时间方面都能表现出色。 有很多地方我们仍然可以提高性能，我们将在这些方面努力。 主题和键盘地图 – 我们知道许多开发者已经习惯了他们现有的编辑器和 IDE，当他们转移到新的 IDE 时，往往会想念他们以前的键盘绑定和主题。 我们将致力于增加对更多主题和键盘映射的支持。 我们当然也会致力于 Vim 的模拟。\n更多详情可查看官方博客。', '<p style=\"text-indent: 0px; text-align: left;\">JetBrains&nbsp;<a href=\"https://my.oschina.net/u/5494143/blog/5584325\" target=\"\">宣布</a>首次公共预览&nbsp;<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fwww.jetbrains.com.cn%2Ffleet%2F\" target=\"_blank\">Fleet</a>，所有人都可以使用。Fleet&nbsp;是由&nbsp;JetBrains&nbsp;打造的下一代&nbsp;IDE，于&nbsp;2021&nbsp;年首次正式<a href=\"https://my.oschina.net/u/5494143/blog/5332934\" target=\"\">推出</a>。它是一个新的分布式多语言编辑器和&nbsp;IDE，基于&nbsp;JetBrains&nbsp;在后端的&nbsp;IntelliJ&nbsp;平台，采用了全新的用户界面和分布式架构从头开始构建。</p><p style=\"text-indent: 0px; text-align: left;\"><strong>下载&nbsp;Fleet：</strong><a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fwww.jetbrains.com.cn%2Ffleet%2Fdownload%2F\" target=\"_blank\">https://www.jetbrains.com.cn/fleet/download/</a></p><p style=\"text-indent: 0px; text-align: left;\"><br></p><p style=\"text-indent: 0px; text-align: left;\">公告表示，自从最初宣布&nbsp;Fleet&nbsp;以来，有超过&nbsp;137,000&nbsp;人报名参加私人预览；官方最初之所以决定从封闭式预览开始，是为了能够以渐进的方式处理反馈。现如今，JetBrains&nbsp;Fleet&nbsp;仍处于起步阶段，还有大量的工作要做。其向公众开放预览的原因有两个方面：“首先，我们认为让所有注册者再等下去是不对的，但单独邀请这么多人对我们来说也缺乏意义。面向公众开放预览对我们来说更容易。第二，也是最重要的，我们一直是一家以开放态度打造产品的公司。我们不希望&nbsp;Fleet&nbsp;在这方面有任何不同。”</p><p style=\"text-indent: 0px; text-align: left;\">JetBrains&nbsp;方面提供了一个<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fjb.gg%2Ffleet-feature-matrix\" target=\"_blank\">图表</a>，以显示&nbsp;Fleet&nbsp;目前提供支持的语言和技术，以及每个技术的状态。但值得注意的是，Fleet&nbsp;仍处于早期阶段，有些事情可能无法按预期工作；所以即使有些东西被列为受支持的，也有可能存在问题。</p><p style=\"text-indent: 0px; text-align: left;\">同时&nbsp;JetBrains&nbsp;也强调称，他们并不打算取代其现有的&nbsp;IDE。</p><blockquote style=\"text-indent: 0px; text-align: left;\">因此，请不要期望在&nbsp;Fleet&nbsp;中看到与我们的&nbsp;IDE（如&nbsp;IntelliJ&nbsp;IDEA）完全相同的功能。尽管我们会继续开发&nbsp;Fleet，我们&nbsp;IDE&nbsp;的所有功能也不会出现在其中。Fleet&nbsp;是我们为开发者提供不同用户体验的一个机会。话虽如此，我们确实希望听到你认为&nbsp;Fleet&nbsp;还缺少什么功能的反馈，例如特定的重构选项、工具集成等。我们现有的&nbsp;IDE&nbsp;将继续发展。我们对其有很多计划，包括性能改进、新的用户界面、远程开发等等。最后，Fleet&nbsp;还在底层采用了我们现有工具的智慧，所以这些工具都不会消失。</blockquote><p style=\"text-indent: 0px; text-align: start;\">JetBrains&nbsp;透露，在未来几个月他们将致力于稳定&nbsp;Fleet，并尽可能地解决得到的反馈。同时，将在以下领域开展工作：</p><ul style=\"text-indent: 0px; text-align: left;\"><li><strong>为插件作者提供&nbsp;API&nbsp;支持和&nbsp;SDK</strong>–鉴于&nbsp;Fleet&nbsp;有一个<a href=\"https://www.oschina.net/action/GoToLink?url=https%3A%2F%2Fblog.jetbrains.com%2Fzh-hans%2Ffleet%2F2022%2F01%2Ffleet-below-deck-part-i-architecture-overview%2F\" target=\"_blank\">分布式架构</a>，我们需要努力为插件作者简化工作。&nbsp;虽然我们保证会为扩展&nbsp;Fleet&nbsp;提供一个平台，但也请求大家在这方面多一点耐心。&nbsp;</li><li><strong>性能</strong>&nbsp;–&nbsp;我们希望&nbsp;Fleet&nbsp;不仅在内存占用方面，而且在响应时间方面都能表现出色。&nbsp;有很多地方我们仍然可以提高性能，我们将在这些方面努力。&nbsp;</li><li><strong>主题和键盘地图</strong>&nbsp;–&nbsp;我们知道许多开发者已经习惯了他们现有的编辑器和&nbsp;IDE，当他们转移到新的&nbsp;IDE&nbsp;时，往往会想念他们以前的键盘绑定和主题。&nbsp;我们将致力于增加对更多主题和键盘映射的支持。&nbsp;我们当然也会致力于&nbsp;Vim&nbsp;的模拟。</li></ul><p style=\"text-indent: 0px; text-align: left;\">更多详情可<a href=\"https://my.oschina.net/u/5494143/blog/5584325\" target=\"\">查看官方博客</a>。</p>', '', 0, 0, 'CSDN', '开云', NULL, 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:36:10');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (55, 2, '1024创新实验室 十一放假通知', 1, 0, '2024-01-01 20:22:23', '国庆假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：10月1日至7日放假调休，共7天。\n衷心预祝\n国庆快乐，阖家幸福！', '<p style=\"text-indent: 0px; text-align: justify;\">国庆假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：<strong>10月1日至7日放假调休</strong>，共7天。</p><p style=\"text-indent: 0px; text-align: justify;\"><strong>衷心预祝</strong></p><p style=\"text-indent: 0px; text-align: justify;\"><strong>国庆快乐，阖家幸福！</strong></p>', '', 0, 0, '人力行政部', '卓大', '1024创新实验室发〔2022〕字第36号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:37:57');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (56, 2, '十月份技术分享会议', 1, 0, '2024-01-01 20:22:23', '尊敬的各位技术大佬：\n1024创新实验室技术分享即将隆重举行\n现将有关会议事宜通知如下：\n一、会议内容\n1、研究探讨SmartAdmin的技术体系\n二、会议形式\n大会专题小会分组讨论;\n三、会议时间及地点\n会议报到时间：xxx1年6月14日\n会议报到地点：洛阳市', '<p style=\"text-indent: 0px; text-align: start;\">尊敬的各位技术大佬：</p><p style=\"text-indent: 0px; text-align: start;\">1024创新实验室技术分享即将隆重举行</p><p style=\"text-indent: 0px; text-align: start;\">现将有关会议事宜通知如下：</p><p style=\"text-indent: 0px; text-align: start;\"><strong>一、会议内容</strong></p><p style=\"text-indent: 0px; text-align: start;\">1、研究探讨SmartAdmin的技术体系</p><p style=\"text-indent: 0px; text-align: start;\"><strong>二、会议形式</strong></p><p style=\"text-indent: 0px; text-align: start;\">大会专题小会分组讨论;</p><p style=\"text-indent: 0px; text-align: start;\"><strong>三、会议时间及地点</strong></p><p style=\"text-indent: 0px; text-align: start;\">会议报到时间：xxx1年6月14日</p><p style=\"text-indent: 0px; text-align: start;\">会议报到地点：洛阳市</p>', '', 0, 0, '技术部', '开云', '1024创新实验室发〔2022〕字第33号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:40:45');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (57, 2, '关于疫情防控上班通知', 1, 0, '2024-01-01 20:22:23', '近期，国内部分地区疫情频发，多地疫情出现外溢，为有效降低我市疫情输入和传播风险，洛阳市疾病预防控制中心发布疫情防控公众提示：\n一、所有入（返）洛阳人员均需提前3天向目的地社区（村居）、酒店宾馆、接待单位等所属网格进行报备，或通过“洛阳即时通系统”进行自主报备，配合做好健康码和行程码查验、核酸检测、隔离观察和健康监测等相关疫情防控措施。\n二、倡导广大群众减少跨地市出行，避免人群大范围流动引发的疫情传播扩散风险。\n三、对7天内有高风险区旅居史的人员，采取7天集中隔离医学观察；对7天内有中风险区旅居史的人员，采取7天居家隔离医学观察，如不具备居家隔离医学观察条件的，采取集中隔离医学观察。\n四、对疫情发生地出现一定范围社区传播或已实施大范围社区管控措施，基于对疫情输入风险研判结果，对近7天内来自疫情发生地所在县（市、区）的流入人员，参照中风险区旅居史人员的防控要求采取相应措施。\n五、对所有省外入（返）洛阳人员，须持有48小时内核酸检测阴性证明，抵达后进行“5天3检”，每次检测间隔24小时。推广“落地检”，按照“自愿免费即采即走，不限制流动”的原则，抵达我市后，立即进行1次核酸检测。\n六、加强重点机构场所疫情防控，坚持非必要不举办，对确需举办的培训、会展、文艺演出等大型聚集性活动，查验48小时内核酸检测阴性证明；建筑工地等人员密集型单位，查验外省（区、市）返岗人员48小时内核酸检测阴性证明；养老机构、儿童福利机构等查验探访人员48小时内核酸检测阴性证明；对进入宾馆、酒店和旅游景区等人流密集场所时，查验48小时内核酸检测阴性证明。\n七、近期有外出旅行史的人员，请密切关注疫情发生地区公布的病例和无症状感染者流调轨迹信息和中高风险区信息。有涉疫风险的人员要立即向社区（村）、住宿宾馆和单位报告，配合落实隔离医学观察。\n八、发热病人、健康码“黄码”等人员要履行个人防护责任，主动配合健康监测和核酸检测，在未排除感染风险前不出行。\n', '<p style=\"text-indent: 0px; text-align: justify;\">近期，国内部分地区疫情频发，多地疫情出现外溢，为有效降低我市疫情输入和传播风险，洛阳市疾病预防控制中心发布疫情防控公众提示：</p><p style=\"text-indent: 0px; text-align: justify;\">一、所有入（返）洛阳人员均需提前3天向目的地社区（村居）、酒店宾馆、接待单位等所属网格进行报备，或通过“洛阳即时通系统”进行自主报备，配合做好健康码和行程码查验、核酸检测、隔离观察和健康监测等相关疫情防控措施。</p><p style=\"text-indent: 0px; text-align: justify;\">二、倡导广大群众减少跨地市出行，避免人群大范围流动引发的疫情传播扩散风险。</p><p style=\"text-indent: 0px; text-align: justify;\">三、对7天内有高风险区旅居史的人员，采取7天集中隔离医学观察；对7天内有中风险区旅居史的人员，采取7天居家隔离医学观察，如不具备居家隔离医学观察条件的，采取集中隔离医学观察。</p><p style=\"text-indent: 0px; text-align: justify;\">四、对疫情发生地出现一定范围社区传播或已实施大范围社区管控措施，基于对疫情输入风险研判结果，对近7天内来自疫情发生地所在县（市、区）的流入人员，参照中风险区旅居史人员的防控要求采取相应措施。</p><p style=\"text-indent: 0px; text-align: justify;\">五、对所有省外入（返）洛阳人员，须持有48小时内核酸检测阴性证明，抵达后进行“5天3检”，每次检测间隔24小时。推广“落地检”，按照“自愿免费即采即走，不限制流动”的原则，抵达我市后，立即进行1次核酸检测。</p><p style=\"text-indent: 0px; text-align: justify;\">六、加强重点机构场所疫情防控，坚持非必要不举办，对确需举办的培训、会展、文艺演出等大型聚集性活动，查验48小时内核酸检测阴性证明；建筑工地等人员密集型单位，查验外省（区、市）返岗人员48小时内核酸检测阴性证明；养老机构、儿童福利机构等查验探访人员48小时内核酸检测阴性证明；对进入宾馆、酒店和旅游景区等人流密集场所时，查验48小时内核酸检测阴性证明。</p><p style=\"text-indent: 0px; text-align: justify;\">七、近期有外出旅行史的人员，请密切关注疫情发生地区公布的病例和无症状感染者流调轨迹信息和中高风险区信息。有涉疫风险的人员要立即向社区（村）、住宿宾馆和单位报告，配合落实隔离医学观察。</p><p style=\"text-indent: 0px; text-align: justify;\">八、发热病人、健康码“黄码”等人员要履行个人防护责任，主动配合健康监测和核酸检测，在未排除感染风险前不出行。</p><p style=\"text-indent: 0px; text-align: justify;\"><br></p>', '', 0, 0, '行政部', '卓大', '1024创新实验室发〔2022〕字第40号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:46:00');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (58, 2, '办公室消杀关键位置通知', 1, 0, '2024-01-01 20:22:23', '开展消毒消杀是杀灭病源、切断疫情传播的有效手段，是防控疫情的重要措施。为了切实将新型冠状病毒肺炎疫情防控工作落到实处，守护好辖区居民及工作人员的身体健康和生命安全，青山镇高度重视新型冠状病毒肺炎的消杀工作，将采购的防护服，防护面罩，一次性手套，口罩，84消毒液，酒精消毒液以及喷雾工具等消毒消杀物资，分发到镇级各站所各村（社区），全镇开展消杀工作。', '<p><span style=\"color: rgb(93, 93, 93); background-color: rgb(247, 247, 247);\">开展消毒消杀是杀灭病源、切断疫情传播的有效手段，是防控疫情的重要措施。为了切实将新型冠状病毒肺炎疫情防控工作落到实处，守护好辖区居民及工作人员的身体健康和生命安全，青山镇高度重视新型冠状病毒肺炎的消杀工作，将采购的防护服，防护面罩，一次性手套，口罩，84消毒液，酒精消毒液以及喷雾工具等消毒消杀物资，分发到镇级各站所各村（社区），全镇开展消杀工作。</span></p>', '', 0, 0, '行政部', '卓大', '1024创新实验室发〔2022〕字第26号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:47:12');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (59, 2, '十月份人事任命通知', 1, 0, '2024-01-01 20:22:23', '1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号\n1024创新实验室发〔2022〕字第36号', '<p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p><p>1024创新实验室发〔2022〕字第36号</p>', '', 0, 0, '销售部', '卓大', '1024创新实验室发〔2022〕字第30号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:50:11');
INSERT INTO `t_notice` (`notice_id`, `notice_type_id`, `title`, `all_visible_flag`, `scheduled_publish_flag`, `publish_time`, `content_text`, `content_html`, `attachment`, `page_view_count`, `user_view_count`, `source`, `author`, `document_number`, `deleted_flag`, `create_user_id`, `update_time`, `create_time`) VALUES (60, 2, '1024创新实验室 春节放假通知', 1, 0, '2024-01-01 20:22:23', '春节假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：10月1日至7日放假调休，共7天。\n衷心预祝\n国庆快乐，阖家幸福！', '<p style=\"text-indent: 0px; text-align: justify;\">国庆假期即将来临，根据国务院办公厅关于国庆节的放假安排，废纸信息网安排如下：<strong>10月1日至7日放假调休</strong>，共7天。</p><p style=\"text-indent: 0px; text-align: justify;\"><strong>衷心预祝</strong></p><p style=\"text-indent: 0px; text-align: justify;\"><strong>国庆快乐，阖家幸福！</strong></p>', '', 0, 0, '人力行政部', '卓大', '1024创新实验室发〔2022〕字第36号', 0, 1, '2024-01-08 19:02:12', '2022-10-22 14:37:57');
COMMIT;

-- ----------------------------
-- Table structure for t_notice_type
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_type`;
CREATE TABLE `t_notice_type` (
  `notice_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知类型',
  `notice_type_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_type_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='通知类型';

-- ----------------------------
-- Records of t_notice_type
-- ----------------------------
BEGIN;
INSERT INTO `t_notice_type` (`notice_type_id`, `notice_type_name`, `create_time`, `update_time`) VALUES (1, '新闻', '2022-08-16 20:29:15', '2024-09-03 21:44:42');
INSERT INTO `t_notice_type` (`notice_type_id`, `notice_type_name`, `create_time`, `update_time`) VALUES (2, '通知', '2022-08-16 20:29:20', '2022-08-16 20:29:20');
COMMIT;

-- ----------------------------
-- Table structure for t_notice_view_record
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_view_record`;
CREATE TABLE `t_notice_view_record` (
  `notice_id` bigint NOT NULL COMMENT '通知公告id',
  `employee_id` bigint NOT NULL COMMENT '员工id',
  `page_view_count` int DEFAULT '0' COMMENT '查看次数',
  `first_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首次ip',
  `first_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首次用户设备等标识',
  `last_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后一次ip',
  `last_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后一次用户设备等标识',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_id`,`employee_id`) USING BTREE,
  UNIQUE KEY `uk_notice_employee` (`notice_id`,`employee_id`) USING BTREE COMMENT '资讯员工'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='通知查看记录';

-- ----------------------------
-- Records of t_notice_view_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_notice_visible_range
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_visible_range`;
CREATE TABLE `t_notice_visible_range` (
  `notice_id` bigint NOT NULL COMMENT '资讯id',
  `data_type` tinyint NOT NULL COMMENT '数据类型1员工 2部门',
  `data_id` bigint NOT NULL COMMENT '员工or部门id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_notice_data` (`notice_id`,`data_type`,`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='通知可见范围';

-- ----------------------------
-- Records of t_notice_visible_range
-- ----------------------------
BEGIN;
INSERT INTO `t_notice_visible_range` (`notice_id`, `data_type`, `data_id`, `create_time`) VALUES (63, 1, 63, '2024-08-09 10:40:32');
COMMIT;

-- ----------------------------
-- Table structure for t_oa_bank
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_bank`;
CREATE TABLE `t_oa_bank` (
  `bank_id` bigint NOT NULL AUTO_INCREMENT COMMENT '银行信息ID',
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开户银行',
  `account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户名称',
  `account_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `business_flag` tinyint(1) NOT NULL COMMENT '是否对公',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`bank_id`) USING BTREE,
  KEY `idx_enterprise_id` (`enterprise_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='OA银行信息\n';

-- ----------------------------
-- Records of t_oa_bank
-- ----------------------------
BEGIN;
INSERT INTO `t_oa_bank` (`bank_id`, `bank_name`, `account_name`, `account_number`, `remark`, `business_flag`, `enterprise_id`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_user_name`, `create_time`, `update_time`) VALUES (26, '工商银行', '1024创新实验室', '1024', '基本户', 1, 2, 0, 0, 1, '管理员', '2022-10-22 17:58:43', '2022-10-22 17:58:43');
INSERT INTO `t_oa_bank` (`bank_id`, `bank_name`, `account_name`, `account_number`, `remark`, `business_flag`, `enterprise_id`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_user_name`, `create_time`, `update_time`) VALUES (27, '建设银行', '1024创新实验室', '10241', '其他户', 0, 2, 0, 0, 1, '管理员', '2022-10-22 17:59:19', '2022-10-22 17:59:19');
COMMIT;

-- ----------------------------
-- Table structure for t_oa_enterprise
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_enterprise`;
CREATE TABLE `t_oa_enterprise` (
  `enterprise_id` bigint NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `enterprise_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业名称',
  `enterprise_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '企业logo',
  `type` int NOT NULL DEFAULT '1' COMMENT '类型（1:有限公司;2:合伙公司）',
  `unified_social_credit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统一社会信用代码',
  `contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人',
  `contact_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份',
  `province_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份名称',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '市',
  `city_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市名称',
  `district` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区县',
  `district_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区县名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详细地址',
  `business_license` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '营业执照',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`enterprise_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='OA企业模块\r\n';

-- ----------------------------
-- Records of t_oa_enterprise
-- ----------------------------
BEGIN;
INSERT INTO `t_oa_enterprise` (`enterprise_id`, `enterprise_name`, `enterprise_logo`, `type`, `unified_social_credit_code`, `contact`, `contact_phone`, `email`, `province`, `province_name`, `city`, `city_name`, `district`, `district_name`, `address`, `business_license`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_user_name`, `create_time`, `update_time`) VALUES (1, '1024创新区块链实验室', 'public/common/34f5ac0fc097402294aea75352c128f0_20240306112435.png', 1, '1024lab_block', '开云', '18637925892', NULL, '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '区块链大楼', 'public/common/1d89055e5680426280446aff1e7e627c_20240306112451.jpeg', 0, 0, 1, '管理员', '2021-10-22 17:03:35', '2022-10-22 17:04:18');
INSERT INTO `t_oa_enterprise` (`enterprise_id`, `enterprise_name`, `enterprise_logo`, `type`, `unified_social_credit_code`, `contact`, `contact_phone`, `email`, `province`, `province_name`, `city`, `city_name`, `district`, `district_name`, `address`, `business_license`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_user_name`, `create_time`, `update_time`) VALUES (2, '1024创新实验室', '', 2, '1024lab', '卓大', '18637925892', 'lab1024@163.com', '410000', '河南省', '410300', '洛阳市', '410311', '洛龙区', '1024大楼', 'public/common/59b1ca99b7fe45d78678e6295798a699_20231201200459.jpg', 0, 0, 44, '卓大', '2022-10-22 14:57:36', '2022-10-22 17:03:57');
COMMIT;

-- ----------------------------
-- Table structure for t_oa_enterprise_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_enterprise_employee`;
CREATE TABLE `t_oa_enterprise_employee` (
  `enterprise_employee_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enterprise_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单ID',
  `employee_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货物名称',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`enterprise_employee_id`) USING BTREE,
  UNIQUE KEY `uk_enterprise_employee` (`enterprise_id`,`employee_id`) USING BTREE,
  KEY `idx_employee_id` (`employee_id`) USING BTREE,
  KEY `idx_enterprise_id` (`enterprise_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='企业关联的员工';

-- ----------------------------
-- Records of t_oa_enterprise_employee
-- ----------------------------
BEGIN;
INSERT INTO `t_oa_enterprise_employee` (`enterprise_employee_id`, `enterprise_id`, `employee_id`, `update_time`, `create_time`) VALUES (154, '2', '2', '2022-10-22 17:57:50', '2022-10-22 17:57:50');
INSERT INTO `t_oa_enterprise_employee` (`enterprise_employee_id`, `enterprise_id`, `employee_id`, `update_time`, `create_time`) VALUES (155, '2', '44', '2022-10-22 17:57:50', '2022-10-22 17:57:50');
COMMIT;

-- ----------------------------
-- Table structure for t_oa_invoice
-- ----------------------------
DROP TABLE IF EXISTS `t_oa_invoice`;
CREATE TABLE `t_oa_invoice` (
  `invoice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '发票信息ID',
  `invoice_heads` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开票抬头',
  `taxpayer_identification_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '纳税人识别号',
  `account_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行账户',
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开户行',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`invoice_id`) USING BTREE,
  KEY `idx_enterprise_id` (`enterprise_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='OA发票信息\n';

-- ----------------------------
-- Records of t_oa_invoice
-- ----------------------------
BEGIN;
INSERT INTO `t_oa_invoice` (`invoice_id`, `invoice_heads`, `taxpayer_identification_number`, `account_number`, `bank_name`, `remark`, `enterprise_id`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_user_name`, `create_time`, `update_time`) VALUES (15, '1024创新实验室', '1024lab', '1024lab', '中国银行', '123', 2, 0, 0, 1, '管理员', '2022-10-22 17:59:35', '2023-09-27 16:26:07');
COMMIT;

-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operate_log`;
CREATE TABLE `t_operate_log` (
  `operate_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate_user_id` bigint NOT NULL COMMENT '用户id',
  `operate_user_type` int NOT NULL COMMENT '用户类型',
  `operate_user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `module` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作模块',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '操作内容',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求路径',
  `method` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求方法',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `ip` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求ip',
  `ip_region` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求ip地区',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求user-agent',
  `success_flag` tinyint DEFAULT NULL COMMENT '请求结果 0失败 1成功',
  `fail_reason` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '失败原因',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`operate_log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4667 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作记录';

-- ----------------------------
-- Records of t_operate_log
-- ----------------------------
BEGIN;
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4499, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:35:39', '2025-08-03 02:35:39');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4500, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:35:39', '2025-08-03 02:35:39');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4501, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:40:53', '2025-08-03 02:40:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4502, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:40:53', '2025-08-03 02:40:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4503, 1, 1, '管理员', '马术俱乐部管理', '分页查询俱乐部', '/mclub/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:50:53', '2025-08-03 02:50:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4504, 1, 1, '管理员', '马术俱乐部管理', '查询俱乐部详情', '/mclub/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:51:00', '2025-08-03 02:51:00');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4505, 1, 1, '管理员', '马术俱乐部管理', '分页查询俱乐部', '/mclub/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:51:13', '2025-08-03 02:51:13');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4506, 1, 1, '管理员', '马术俱乐部管理', '查询俱乐部详情', '/mclub/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:51:15', '2025-08-03 02:51:15');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4507, 1, 1, '管理员', '马术俱乐部管理', '查询俱乐部详情', '/mclub/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:51:24', '2025-08-03 02:51:24');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4508, 1, 1, '管理员', '马术俱乐部管理', '查询俱乐部详情', '/mclub/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:51:49', '2025-08-03 02:51:49');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4509, 1, 1, '管理员', '马术俱乐部管理', '分页查询俱乐部', '/mclub/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 02:52:03', '2025-08-03 02:52:03');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4510, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:03:50', '2025-08-03 03:03:50');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4511, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:03:50', '2025-08-03 03:03:50');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4512, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":2,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:08:06', '2025-08-03 03:08:06');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4513, 1, 1, '管理员', 'OA办公-通知公告', '【员工】通知公告-查询全部 @author 卓大', '/oa/notice/employee/query', 'net.lab1024.sa.admin.module.business.oa.notice.controller.NoticeController.queryEmployeeNotice', '[{\"noticeTypeId\":1,\"pageNum\":1,\"pageSize\":6,\"searchCount\":false}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:08:06', '2025-08-03 03:08:06');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4514, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:08:07', '2025-08-03 03:08:07');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4515, 1, 1, '管理员', '俱乐部管理', '查询俱乐部详情', '/club/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:08:09', '2025-08-03 03:08:09');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4516, 1, 1, '管理员', '俱乐部管理', '查询俱乐部详情', '/club/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:08:16', '2025-08-03 03:08:16');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4517, 1, 1, '管理员', '俱乐部管理', '查询俱乐部详情', '/club/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:08:20', '2025-08-03 03:08:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4518, 1, 1, '管理员', '俱乐部管理', '查询俱乐部详情', '/club/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:09:15', '2025-08-03 03:09:15');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4519, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:09:38', '2025-08-03 03:09:38');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4520, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:09:59', '2025-08-03 03:09:59');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4521, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:20', '2025-08-03 03:22:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4522, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:20', '2025-08-03 03:22:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4523, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:20', '2025-08-03 03:22:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4524, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:20', '2025-08-03 03:22:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4525, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:21', '2025-08-03 03:22:21');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4526, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:21', '2025-08-03 03:22:21');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4527, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:30', '2025-08-03 03:22:30');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4528, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:31', '2025-08-03 03:22:31');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4529, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:33', '2025-08-03 03:22:33');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4530, 1, 1, '管理员', '俱乐部管理', '查询俱乐部详情', '/club/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:35', '2025-08-03 03:22:35');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4531, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:22:49', '2025-08-03 03:22:49');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4532, 1, 1, '管理员', '俱乐部管理', '查询俱乐部详情', '/club/club/get/1', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:23:05', '2025-08-03 03:23:05');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4533, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:24:31', '2025-08-03 03:24:31');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4534, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:25:32', '2025-08-03 03:25:32');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4535, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:25:45', '2025-08-03 03:25:45');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4536, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:26:02', '2025-08-03 03:26:02');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4537, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:26:04', '2025-08-03 03:26:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4538, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:26:04', '2025-08-03 03:26:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4539, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:26:11', '2025-08-03 03:26:11');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4540, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/1', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:26:11', '2025-08-03 03:26:11');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4541, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/1', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:27:28', '2025-08-03 03:27:28');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4542, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:27:32', '2025-08-03 03:27:32');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4543, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/1', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:27:32', '2025-08-03 03:27:32');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4544, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:27:43', '2025-08-03 03:27:43');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4545, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:27:43', '2025-08-03 03:27:43');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4546, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:15', '2025-08-03 03:41:15');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4547, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:15', '2025-08-03 03:41:15');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4548, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:15', '2025-08-03 03:41:15');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4549, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:15', '2025-08-03 03:41:15');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4550, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:24', '2025-08-03 03:41:24');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4551, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:24', '2025-08-03 03:41:24');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4552, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:24', '2025-08-03 03:41:24');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4553, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:41:24', '2025-08-03 03:41:24');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4554, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:41', '2025-08-03 03:51:41');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4555, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:41', '2025-08-03 03:51:41');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4556, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:43', '2025-08-03 03:51:43');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4557, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:44', '2025-08-03 03:51:44');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4558, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:45', '2025-08-03 03:51:45');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4559, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:46', '2025-08-03 03:51:46');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4560, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:51:46', '2025-08-03 03:51:46');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4561, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:57:45', '2025-08-03 03:57:45');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4562, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:57:45', '2025-08-03 03:57:45');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4563, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:57:57', '2025-08-03 03:57:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4564, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/1', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:57:57', '2025-08-03 03:57:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4565, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/1', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:58:05', '2025-08-03 03:58:05');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4566, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/2', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[2]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:58:12', '2025-08-03 03:58:12');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4567, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:58:34', '2025-08-03 03:58:34');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4568, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/1', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[1]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:58:34', '2025-08-03 03:58:34');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4569, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:58:50', '2025-08-03 03:58:50');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4570, 1, 1, '管理员', '教练管理', '查询教练详情', '/club/coach/get/2', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.getDetail', '[2]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 03:58:50', '2025-08-03 03:58:50');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4571, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:12:01', '2025-08-03 04:12:01');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4572, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:12:01', '2025-08-03 04:12:01');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4573, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:21:48', '2025-08-03 04:21:48');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4574, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:21:48', '2025-08-03 04:21:48');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4575, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:22:52', '2025-08-03 04:22:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4576, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:22:52', '2025-08-03 04:22:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4577, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:23:20', '2025-08-03 04:23:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4578, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:23:20', '2025-08-03 04:23:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4579, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:23:52', '2025-08-03 04:23:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4580, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:23:52', '2025-08-03 04:23:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4581, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:24:28', '2025-08-03 04:24:28');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4582, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:24:28', '2025-08-03 04:24:28');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4583, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:25:20', '2025-08-03 04:25:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4584, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:25:20', '2025-08-03 04:25:20');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4585, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:25:53', '2025-08-03 04:25:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4586, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:25:53', '2025-08-03 04:25:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4587, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:25:57', '2025-08-03 04:25:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4588, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:25:58', '2025-08-03 04:25:58');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4589, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:26:06', '2025-08-03 04:26:06');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4590, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:26:07', '2025-08-03 04:26:07');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4591, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:26:09', '2025-08-03 04:26:09');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4592, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:26:37', '2025-08-03 04:26:37');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4593, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:27:07', '2025-08-03 04:27:07');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4594, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:28:29', '2025-08-03 04:28:29');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4595, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:28:53', '2025-08-03 04:28:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4596, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:28:53', '2025-08-03 04:28:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4597, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:28:53', '2025-08-03 04:28:53');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4598, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:29:10', '2025-08-03 04:29:10');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4599, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:29:52', '2025-08-03 04:29:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4600, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:29:54', '2025-08-03 04:29:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4601, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:29:54', '2025-08-03 04:29:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4602, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:30:04', '2025-08-03 04:30:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4603, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:30:04', '2025-08-03 04:30:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4604, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 04:30:04', '2025-08-03 04:30:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4605, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:03:49', '2025-08-03 05:03:49');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4606, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:03:49', '2025-08-03 05:03:49');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4607, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:03:49', '2025-08-03 05:03:49');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4608, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:03:50', '2025-08-03 05:03:50');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4609, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:12', '2025-08-03 05:06:12');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4610, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:12', '2025-08-03 05:06:12');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4611, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:12', '2025-08-03 05:06:12');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4612, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:22', '2025-08-03 05:06:22');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4613, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:22', '2025-08-03 05:06:22');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4614, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:22', '2025-08-03 05:06:22');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4615, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:22', '2025-08-03 05:06:22');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4616, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:57', '2025-08-03 05:06:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4617, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:57', '2025-08-03 05:06:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4618, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:57', '2025-08-03 05:06:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4619, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:06:57', '2025-08-03 05:06:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4620, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:00', '2025-08-03 05:07:00');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4621, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:00', '2025-08-03 05:07:00');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4622, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:00', '2025-08-03 05:07:00');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4623, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:00', '2025-08-03 05:07:00');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4624, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:01', '2025-08-03 05:07:01');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4625, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:01', '2025-08-03 05:07:01');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4626, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:01', '2025-08-03 05:07:01');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4627, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:01', '2025-08-03 05:07:01');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4628, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:54', '2025-08-03 05:07:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4629, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:54', '2025-08-03 05:07:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4630, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:54', '2025-08-03 05:07:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4631, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:07:54', '2025-08-03 05:07:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4632, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:08:04', '2025-08-03 05:08:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4633, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:08:04', '2025-08-03 05:08:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4634, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:08:04', '2025-08-03 05:08:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4635, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:08:04', '2025-08-03 05:08:04');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4636, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:47', '2025-08-03 05:15:47');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4637, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:47', '2025-08-03 05:15:47');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4638, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:47', '2025-08-03 05:15:47');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4639, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:47', '2025-08-03 05:15:47');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4640, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:50', '2025-08-03 05:15:50');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4641, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:51', '2025-08-03 05:15:51');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4642, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:51', '2025-08-03 05:15:51');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4643, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:51', '2025-08-03 05:15:51');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4644, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:52', '2025-08-03 05:15:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4645, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:52', '2025-08-03 05:15:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4646, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:52', '2025-08-03 05:15:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4647, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:52', '2025-08-03 05:15:52');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4648, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:54', '2025-08-03 05:15:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4649, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:54', '2025-08-03 05:15:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4650, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:54', '2025-08-03 05:15:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4651, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:54', '2025-08-03 05:15:54');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4652, 1, 1, '管理员', '俱乐部管理', '分页查询俱乐部', '/club/club/page/query', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:56', '2025-08-03 05:15:56');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4653, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[true]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:57', '2025-08-03 05:15:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4654, 1, 1, '管理员', '教练管理', '分页查询教练', '/club/coach/page/query', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryByPage', '[{\"isDelete\":false,\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:57', '2025-08-03 05:15:57');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4655, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:59', '2025-08-03 05:15:59');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4656, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:59', '2025-08-03 05:15:59');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4657, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:59', '2025-08-03 05:15:59');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4658, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:15:59', '2025-08-03 05:15:59');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4659, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:16:55', '2025-08-03 05:16:55');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4660, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:16:55', '2025-08-03 05:16:55');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4661, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:16:55', '2025-08-03 05:16:55');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4662, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:16:55', '2025-08-03 05:16:55');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4663, 1, 1, '管理员', '教练管理', '教练列表查询', '/club/coach/query/list', 'net.lab1024.sa.admin.module.business.coach.controller.CoachController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:18:16', '2025-08-03 05:18:16');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4664, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:18:16', '2025-08-03 05:18:16');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4665, 1, 1, '管理员', '马匹管理', '分页查询马匹', '/club/horse/page/query', 'net.lab1024.sa.admin.module.business.horse.controller.HorseController.queryByPage', '[{\"breed\":\"\",\"isDelete\":0,\"isValid\":1,\"keywords\":\"\",\"pageNum\":1,\"pageSize\":10}]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:18:16', '2025-08-03 05:18:16');
INSERT INTO `t_operate_log` (`operate_log_id`, `operate_user_id`, `operate_user_type`, `operate_user_name`, `module`, `content`, `url`, `method`, `param`, `ip`, `ip_region`, `user_agent`, `success_flag`, `fail_reason`, `update_time`, `create_time`) VALUES (4666, 1, 1, '管理员', '俱乐部管理', '俱乐部列表查询', '/club/club/query/list', 'net.lab1024.sa.admin.module.business.club.controller.ClubController.queryList', '[]', '127.0.0.1', '0|0|0|内网IP|内网IP', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36', 1, NULL, '2025-08-03 05:18:16', '2025-08-03 05:18:16');
COMMIT;

-- ----------------------------
-- Table structure for t_password_log
-- ----------------------------
DROP TABLE IF EXISTS `t_password_log`;
CREATE TABLE `t_password_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` tinyint NOT NULL COMMENT '用户类型',
  `old_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '旧密码',
  `new_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '新密码',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_and_type_index` (`user_id`,`user_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='密码修改记录';

-- ----------------------------
-- Records of t_password_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_position
-- ----------------------------
DROP TABLE IF EXISTS `t_position`;
CREATE TABLE `t_position` (
  `position_id` bigint NOT NULL AUTO_INCREMENT COMMENT '职务ID',
  `position_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职务名称',
  `position_level` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '职级',
  `sort` int DEFAULT '0' COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `deleted_flag` tinyint(1) DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='职务表';

-- ----------------------------
-- Records of t_position
-- ----------------------------
BEGIN;
INSERT INTO `t_position` (`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (3, '技术P7', 'L1', 3, '', 0, '2024-06-29 15:57:07', '2024-07-15 23:34:35');
INSERT INTO `t_position` (`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (4, '技术P8', 'L2', 1, NULL, 0, '2024-07-15 23:34:14', '2024-07-15 23:34:23');
INSERT INTO `t_position` (`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (5, '管理M5', 'L1', 4, NULL, 0, '2024-07-15 23:34:48', '2024-07-15 23:34:48');
INSERT INTO `t_position` (`position_id`, `position_name`, `position_level`, `sort`, `remark`, `deleted_flag`, `create_time`, `update_time`) VALUES (6, '管理M6', 'L2', 5, NULL, 0, '2024-07-15 23:35:00', '2024-07-15 23:35:00');
COMMIT;

-- ----------------------------
-- Table structure for t_reload_item
-- ----------------------------
DROP TABLE IF EXISTS `t_reload_item`;
CREATE TABLE `t_reload_item` (
  `tag` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '项名称',
  `args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数 可选',
  `identification` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '运行标识',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='reload项目';

-- ----------------------------
-- Records of t_reload_item
-- ----------------------------
BEGIN;
INSERT INTO `t_reload_item` (`tag`, `args`, `identification`, `update_time`, `create_time`) VALUES ('system_config', '4', '234', '2024-08-13 14:14:30', '2019-04-18 11:48:27');
COMMIT;

-- ----------------------------
-- Table structure for t_reload_result
-- ----------------------------
DROP TABLE IF EXISTS `t_reload_result`;
CREATE TABLE `t_reload_result` (
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `identification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运行标识',
  `args` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `result` tinyint unsigned NOT NULL COMMENT '是否成功 ',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='reload结果';

-- ----------------------------
-- Records of t_reload_result
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色编码',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色描述',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `role_code_uni` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` (`role_id`, `role_name`, `role_code`, `remark`, `update_time`, `create_time`) VALUES (1, '技术总监', NULL, '', '2022-10-19 20:24:09', '2019-06-21 12:09:34');
INSERT INTO `t_role` (`role_id`, `role_name`, `role_code`, `remark`, `update_time`, `create_time`) VALUES (34, '销售总监', 'cto', '', '2023-09-06 19:10:34', '2019-08-30 09:30:50');
INSERT INTO `t_role` (`role_id`, `role_name`, `role_code`, `remark`, `update_time`, `create_time`) VALUES (35, '总经理', NULL, '', '2019-08-30 09:31:05', '2019-08-30 09:31:05');
INSERT INTO `t_role` (`role_id`, `role_name`, `role_code`, `remark`, `update_time`, `create_time`) VALUES (36, '董事长', NULL, '', '2019-08-30 09:31:11', '2019-08-30 09:31:11');
INSERT INTO `t_role` (`role_id`, `role_name`, `role_code`, `remark`, `update_time`, `create_time`) VALUES (37, '财务', NULL, '', '2019-08-30 09:31:16', '2019-08-30 09:31:16');
COMMIT;

-- ----------------------------
-- Table structure for t_role_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_role_data_scope`;
CREATE TABLE `t_role_data_scope` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_scope_type` int NOT NULL COMMENT '数据范围类型',
  `view_type` int NOT NULL COMMENT '数据可见范围类型',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色的数据范围';

-- ----------------------------
-- Records of t_role_data_scope
-- ----------------------------
BEGIN;
INSERT INTO `t_role_data_scope` (`id`, `data_scope_type`, `view_type`, `role_id`, `update_time`, `create_time`) VALUES (67, 1, 2, 1, '2024-03-18 20:41:00', '2024-03-18 20:41:00');
COMMIT;

-- ----------------------------
-- Table structure for t_role_employee
-- ----------------------------
DROP TABLE IF EXISTS `t_role_employee`;
CREATE TABLE `t_role_employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL COMMENT '角色id',
  `employee_id` bigint NOT NULL COMMENT '员工id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_role_employee` (`role_id`,`employee_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=342 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色员工功能表';

-- ----------------------------
-- Records of t_role_employee
-- ----------------------------
BEGIN;
INSERT INTO `t_role_employee` (`id`, `role_id`, `employee_id`, `update_time`, `create_time`) VALUES (325, 36, 63, '2022-10-19 20:25:26', '2022-10-19 20:25:26');
INSERT INTO `t_role_employee` (`id`, `role_id`, `employee_id`, `update_time`, `create_time`) VALUES (329, 34, 72, '2022-11-05 10:56:54', '2022-11-05 10:56:54');
INSERT INTO `t_role_employee` (`id`, `role_id`, `employee_id`, `update_time`, `create_time`) VALUES (330, 36, 72, '2022-11-05 10:56:54', '2022-11-05 10:56:54');
INSERT INTO `t_role_employee` (`id`, `role_id`, `employee_id`, `update_time`, `create_time`) VALUES (333, 1, 44, '2023-10-07 18:53:29', '2023-10-07 18:53:29');
INSERT INTO `t_role_employee` (`id`, `role_id`, `employee_id`, `update_time`, `create_time`) VALUES (334, 1, 47, '2023-10-07 18:55:00', '2023-10-07 18:55:00');
INSERT INTO `t_role_employee` (`id`, `role_id`, `employee_id`, `update_time`, `create_time`) VALUES (341, 1, 48, '2024-09-02 23:03:28', '2024-09-02 23:03:28');
COMMIT;

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `role_menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=866 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色-菜单\n';

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (236, 1, 138, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (237, 1, 132, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (238, 1, 142, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (239, 1, 149, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (240, 1, 150, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (241, 1, 185, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (242, 1, 186, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (243, 1, 187, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (244, 1, 188, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (245, 1, 145, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (246, 1, 196, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (247, 1, 144, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (248, 1, 181, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (249, 1, 183, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (250, 1, 184, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (251, 1, 165, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (252, 1, 47, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (253, 1, 48, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (254, 1, 137, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (255, 1, 166, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (256, 1, 194, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (257, 1, 78, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (258, 1, 173, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (259, 1, 174, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (260, 1, 175, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (261, 1, 176, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (262, 1, 50, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (263, 1, 26, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (264, 1, 40, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (265, 1, 105, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (266, 1, 106, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (267, 1, 109, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (268, 1, 163, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (269, 1, 164, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (270, 1, 199, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (271, 1, 110, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (272, 1, 159, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (273, 1, 160, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (274, 1, 161, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (275, 1, 162, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (276, 1, 130, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (277, 1, 157, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (278, 1, 158, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (279, 1, 133, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (280, 1, 117, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (281, 1, 156, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (282, 1, 193, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (283, 1, 200, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (284, 1, 220, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (285, 1, 45, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (286, 1, 219, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (287, 1, 46, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (288, 1, 91, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (289, 1, 92, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (290, 1, 93, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (291, 1, 94, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (292, 1, 95, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (293, 1, 96, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (294, 1, 86, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (295, 1, 87, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (296, 1, 88, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (297, 1, 76, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (298, 1, 97, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (299, 1, 98, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (300, 1, 99, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (301, 1, 100, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (302, 1, 101, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (303, 1, 102, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (304, 1, 103, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (305, 1, 104, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (306, 1, 213, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (307, 1, 214, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (308, 1, 143, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (309, 1, 203, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (310, 1, 215, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (311, 1, 218, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (312, 1, 147, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (313, 1, 170, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (314, 1, 171, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (315, 1, 168, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (316, 1, 169, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (317, 1, 202, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (318, 1, 201, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (319, 1, 148, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (320, 1, 152, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (321, 1, 190, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (322, 1, 191, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (323, 1, 192, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (324, 1, 198, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (325, 1, 207, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (326, 1, 111, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (327, 1, 206, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (328, 1, 81, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (329, 1, 204, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (330, 1, 205, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (331, 1, 122, '2024-06-30 23:21:37', '2024-06-30 23:21:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (835, 1, 400, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (836, 1, 401, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (837, 1, 402, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (838, 1, 403, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (839, 1, 404, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (840, 1, 405, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (841, 1, 406, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (842, 1, 407, '2025-08-03 03:03:37', '2025-08-03 03:03:37');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (843, 1, 408, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (844, 1, 409, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (845, 1, 410, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (846, 1, 411, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (847, 1, 412, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (848, 1, 413, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (849, 1, 414, '2025-08-03 03:25:49', '2025-08-03 03:25:49');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (850, 1, 2400, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (851, 1, 2401, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (852, 1, 2402, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (853, 1, 2411, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (854, 1, 2412, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (855, 1, 2413, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (856, 1, 2414, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (857, 1, 2415, '2025-08-03 05:15:38', '2025-08-03 05:15:38');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (858, 1, 2421, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (859, 1, 2422, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (860, 1, 2423, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (861, 1, 2424, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (862, 1, 2431, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (863, 1, 2432, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (864, 1, 2433, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
INSERT INTO `t_role_menu` (`role_menu_id`, `role_id`, `menu_id`, `update_time`, `create_time`) VALUES (865, 1, 2434, '2025-08-03 05:15:39', '2025-08-03 05:15:39');
COMMIT;

-- ----------------------------
-- Table structure for t_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `t_serial_number`;
CREATE TABLE `t_serial_number` (
  `serial_number_id` int NOT NULL,
  `business_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务名称',
  `format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '格式[yyyy]表示年,[mm]标识月,[dd]表示日,[nnn]表示三位数字',
  `rule_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则格式。none没有周期, year 年周期, month月周期, day日周期',
  `init_number` int unsigned NOT NULL COMMENT '初始值',
  `step_random_range` int unsigned NOT NULL COMMENT '步长随机数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `last_number` bigint DEFAULT NULL COMMENT '上次产生的单号, 默认为空',
  `last_time` datetime DEFAULT NULL COMMENT '上次产生的单号时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`serial_number_id`) USING BTREE,
  UNIQUE KEY `key_name` (`business_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='单号生成器定义表';

-- ----------------------------
-- Records of t_serial_number
-- ----------------------------
BEGIN;
INSERT INTO `t_serial_number` (`serial_number_id`, `business_name`, `format`, `rule_type`, `init_number`, `step_random_range`, `remark`, `last_number`, `last_time`, `update_time`, `create_time`) VALUES (1, '订单编号', 'DK[yyyy][mm][dd]NO[nnnnn]', 'day', 1000, 10, 'DK20201101NO321', 1, '2023-12-04 09:16:42', '2024-01-08 19:24:46', '2021-02-19 14:37:50');
INSERT INTO `t_serial_number` (`serial_number_id`, `business_name`, `format`, `rule_type`, `init_number`, `step_random_range`, `remark`, `last_number`, `last_time`, `update_time`, `create_time`) VALUES (2, '合同编号', 'HT[yyyy][mm][dd][nnnnn]-CX', 'none', 1, 1, '', 8, '2023-12-04 09:54:53', '2023-12-04 09:54:52', '2021-08-12 20:40:37');
COMMIT;

-- ----------------------------
-- Table structure for t_serial_number_record
-- ----------------------------
DROP TABLE IF EXISTS `t_serial_number_record`;
CREATE TABLE `t_serial_number_record` (
  `serial_number_id` int NOT NULL,
  `record_date` date NOT NULL COMMENT '记录日期',
  `last_number` bigint NOT NULL DEFAULT '0' COMMENT '最后更新值',
  `last_time` datetime NOT NULL COMMENT '最后更新时间',
  `count` bigint NOT NULL DEFAULT '0' COMMENT '更新次数',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  KEY `uk_generator` (`serial_number_id`,`record_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='serial_number记录表';

-- ----------------------------
-- Records of t_serial_number_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_smart_job
-- ----------------------------
DROP TABLE IF EXISTS `t_smart_job`;
CREATE TABLE `t_smart_job` (
  `job_id` int NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_class` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务执行类',
  `trigger_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发类型',
  `trigger_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发配置',
  `enabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启',
  `param` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数',
  `last_execute_time` datetime DEFAULT NULL COMMENT '最后一次执行时间',
  `last_execute_log_id` int DEFAULT NULL COMMENT '最后一次执行记录id',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `update_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务配置 @listen';

-- ----------------------------
-- Records of t_smart_job
-- ----------------------------
BEGIN;
INSERT INTO `t_smart_job` (`job_id`, `job_name`, `job_class`, `trigger_type`, `trigger_value`, `enabled_flag`, `param`, `last_execute_time`, `last_execute_log_id`, `sort`, `remark`, `deleted_flag`, `update_name`, `create_time`, `update_time`) VALUES (1, '示例任务1', 'net.lab1024.sa.base.module.support.job.sample.SmartJobSample1', 'cron', '10 15 0/1 * * *', 1, '执行示例任务1', '2025-08-03 05:15:10', 8000, 1, '执行示例任务1', 0, '管理员', '2024-06-17 20:00:46', '2025-08-03 05:15:10');
INSERT INTO `t_smart_job` (`job_id`, `job_name`, `job_class`, `trigger_type`, `trigger_value`, `enabled_flag`, `param`, `last_execute_time`, `last_execute_log_id`, `sort`, `remark`, `deleted_flag`, `update_name`, `create_time`, `update_time`) VALUES (2, '示例任务2', 'net.lab1024.sa.base.module.support.job.sample.SmartJobSample2', 'fixed_delay', '120', 1, '执行示例任务2', '2025-08-03 05:16:56', 8001, 2, '执行示例任务2', 0, '管理员', '2024-06-18 20:45:35', '2025-08-03 05:16:56');
COMMIT;

-- ----------------------------
-- Table structure for t_smart_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_smart_job_log`;
CREATE TABLE `t_smart_job_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `job_id` int NOT NULL COMMENT '任务id',
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行参数',
  `success_flag` tinyint(1) NOT NULL COMMENT '是否成功',
  `execute_start_time` datetime NOT NULL COMMENT '执行开始时间',
  `execute_time_millis` int DEFAULT NULL COMMENT '执行时长',
  `execute_end_time` datetime DEFAULT NULL COMMENT '执行结束时间',
  `execute_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip',
  `process_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '进程id',
  `program_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '程序目录',
  `create_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务-执行记录 @listen';

-- ----------------------------
-- Records of t_smart_job_log
-- ----------------------------
BEGIN;
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7933, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:24:30', 84, '2025-08-03 02:24:30', '执行成功,本次处理数据1条', '169.254.14.37', '84397', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:24:29');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7934, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:26:30', 81, '2025-08-03 02:26:30', '执行成功,本次处理数据1条', '169.254.14.37', '84397', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:26:30');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7935, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:28:30', 81, '2025-08-03 02:28:30', '执行成功,本次处理数据1条', '169.254.14.37', '84397', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:28:30');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7936, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:30:30', 92, '2025-08-03 02:30:31', '执行成功,本次处理数据1条', '169.254.14.37', '84397', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:30:30');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7937, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:33:31', 144, '2025-08-03 02:33:31', '执行成功,本次处理数据1条', '169.254.14.37', '86364', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:33:31');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7938, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:35:38', 83, '2025-08-03 02:35:38', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:35:37');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7939, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:39:38', 83, '2025-08-03 02:39:38', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:39:38');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7940, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:41:38', 91, '2025-08-03 02:41:38', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:41:38');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7941, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:43:39', 88, '2025-08-03 02:43:39', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:43:38');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7942, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:47:39', 89, '2025-08-03 02:47:39', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:47:38');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7943, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:49:39', 89, '2025-08-03 02:49:39', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:49:39');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7944, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:51:39', 92, '2025-08-03 02:51:39', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:51:39');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7945, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:53:40', 86, '2025-08-03 02:53:40', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:53:39');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7946, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:57:40', 95, '2025-08-03 02:57:40', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:57:40');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7947, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 02:59:40', 85, '2025-08-03 02:59:40', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 02:59:40');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7948, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:01:40', 87, '2025-08-03 03:01:40', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:01:40');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7949, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:03:41', 86, '2025-08-03 03:03:41', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:03:40');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7950, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:07:41', 85, '2025-08-03 03:07:41', '执行成功,本次处理数据1条', '169.254.14.37', '86772', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:07:41');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7951, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:10:12', 78, '2025-08-03 03:10:12', '执行成功,本次处理数据1条', '169.254.14.37', '93196', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:10:12');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7952, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:12:12', 75, '2025-08-03 03:12:12', '执行成功,本次处理数据1条', '169.254.14.37', '93196', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:12:12');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7953, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:14:12', 96, '2025-08-03 03:14:12', '执行成功,本次处理数据1条', '169.254.14.37', '93196', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:14:12');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7954, 1, '示例任务1', '执行示例任务1', 1, '2025-08-03 03:15:10', 0, '2025-08-03 03:15:10', '执行完毕,随便说点什么吧', '169.254.14.37', '93196', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:15:10');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7955, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:16:13', 90, '2025-08-03 03:16:13', '执行成功,本次处理数据1条', '169.254.14.37', '93196', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:16:12');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7956, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:20:13', 92, '2025-08-03 03:20:13', '执行成功,本次处理数据1条', '169.254.14.37', '93196', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:20:13');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7957, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:22:17', 88, '2025-08-03 03:22:17', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:22:17');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7958, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:24:18', 82, '2025-08-03 03:24:18', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:24:17');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7959, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:28:18', 82, '2025-08-03 03:28:18', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:28:18');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7960, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:30:18', 86, '2025-08-03 03:30:18', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:30:18');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7961, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:32:18', 85, '2025-08-03 03:32:19', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:32:18');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7962, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:34:19', 83, '2025-08-03 03:34:19', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:34:18');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7963, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:38:19', 89, '2025-08-03 03:38:19', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:38:19');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7964, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:40:19', 85, '2025-08-03 03:40:19', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:40:19');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7965, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:42:19', 80, '2025-08-03 03:42:20', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:42:19');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7966, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:44:20', 78, '2025-08-03 03:44:20', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:44:19');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7967, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:48:20', 172, '2025-08-03 03:48:20', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:48:20');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7968, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:50:20', 86, '2025-08-03 03:50:20', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:50:20');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7969, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:52:21', 92, '2025-08-03 03:52:21', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:52:20');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7970, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:56:21', 82, '2025-08-03 03:56:21', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:56:21');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7971, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 03:58:21', 81, '2025-08-03 03:58:21', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 03:58:21');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7972, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:00:21', 87, '2025-08-03 04:00:21', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:00:21');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7973, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:02:22', 87, '2025-08-03 04:02:22', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:02:21');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7974, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:06:22', 78, '2025-08-03 04:06:22', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:06:22');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7975, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:08:22', 81, '2025-08-03 04:08:22', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:08:22');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7976, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:10:22', 86, '2025-08-03 04:10:22', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:10:22');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7977, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:12:23', 81, '2025-08-03 04:12:23', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:12:22');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7978, 1, '示例任务1', '执行示例任务1', 1, '2025-08-03 04:15:10', 0, '2025-08-03 04:15:10', '执行完毕,随便说点什么吧', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:15:10');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7979, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:16:23', 77, '2025-08-03 04:16:23', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:16:23');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7980, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:18:23', 189, '2025-08-03 04:18:23', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:18:23');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7981, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:20:24', 190, '2025-08-03 04:20:24', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:20:23');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7982, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:22:24', 79, '2025-08-03 04:22:24', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:22:24');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7983, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:24:25', 80, '2025-08-03 04:24:25', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:24:24');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7984, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:28:25', 82, '2025-08-03 04:28:25', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:28:25');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7985, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:32:25', 75, '2025-08-03 04:32:25', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:32:25');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7986, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:34:25', 77, '2025-08-03 04:34:25', '执行成功,本次处理数据1条', '169.254.14.37', '96005', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:34:25');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7987, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:44:53', 82, '2025-08-03 04:44:53', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:44:53');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7988, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:46:53', 83, '2025-08-03 04:46:54', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:46:53');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7989, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:48:54', 87, '2025-08-03 04:48:54', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:48:53');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7990, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:52:54', 91, '2025-08-03 04:52:54', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:52:54');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7991, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:54:54', 92, '2025-08-03 04:54:54', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:54:54');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7992, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:56:54', 81, '2025-08-03 04:56:55', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:56:54');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7993, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 04:58:55', 80, '2025-08-03 04:58:55', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 04:58:54');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7994, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:02:55', 87, '2025-08-03 05:02:55', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:02:55');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7995, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:04:55', 81, '2025-08-03 05:04:55', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:04:55');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7996, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:06:55', 81, '2025-08-03 05:06:56', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:06:55');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7997, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:08:56', 82, '2025-08-03 05:08:56', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:08:55');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7998, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:12:56', 86, '2025-08-03 05:12:56', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:12:56');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (7999, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:14:56', 84, '2025-08-03 05:14:56', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:14:56');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (8000, 1, '示例任务1', '执行示例任务1', 1, '2025-08-03 05:15:10', 0, '2025-08-03 05:15:10', '执行完毕,随便说点什么吧', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:15:10');
INSERT INTO `t_smart_job_log` (`log_id`, `job_id`, `job_name`, `param`, `success_flag`, `execute_start_time`, `execute_time_millis`, `execute_end_time`, `execute_result`, `ip`, `process_id`, `program_path`, `create_name`, `create_time`) VALUES (8001, 2, '示例任务2', '执行示例任务2', 1, '2025-08-03 05:16:56', 93, '2025-08-03 05:16:57', '执行成功,本次处理数据1条', '169.254.14.37', '12392', '/Users/mm/Desktop/鞍境/On-the-Saddle', 'system', '2025-08-03 05:16:56');
COMMIT;

-- ----------------------------
-- Table structure for t_table_column
-- ----------------------------
DROP TABLE IF EXISTS `t_table_column`;
CREATE TABLE `t_table_column` (
  `table_column_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_type` int NOT NULL COMMENT '用户类型',
  `table_id` int NOT NULL COMMENT '表格id',
  `columns` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '具体的表格列，存入的json',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`table_column_id`) USING BTREE,
  UNIQUE KEY `uni_employee_table` (`user_id`,`table_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='表格的自定义列存储';

-- ----------------------------
-- Records of t_table_column
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
