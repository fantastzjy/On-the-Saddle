-- 马匹管理模块完整SQL脚本
-- Author: 1024创新实验室-主任：卓大
-- Date: 2024-01-08
-- 基于 final-database-design.md 标准设计

-- ============================================
-- 0. 清理可能存在的冲突数据
-- ============================================
DELETE FROM `t_role_menu` WHERE `menu_id` >= 2400 AND `menu_id` <= 2499;
DELETE FROM `t_menu` WHERE `menu_id` >= 2400 AND `menu_id` <= 2499;

-- ============================================
-- 1. 马匹表（m_horse）
-- ============================================
CREATE TABLE IF NOT EXISTS `m_horse` (
    `horse_id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '马匹ID',
    `club_id`              BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `horse_name`           VARCHAR(100)                          NOT NULL COMMENT '马名',
    `horse_code`           VARCHAR(50) COMMENT '马匹编号',
    `breed`                VARCHAR(50) COMMENT '品种',
    `gender`               TINYINT COMMENT '性别: 1-公 2-母 3-骟',
    `color`                VARCHAR(50) COMMENT '毛色',
    `birth_date`           DATETIME COMMENT '出生日期时间',
    `chip_no`              VARCHAR(100) UNIQUE COMMENT '芯片号',
    `passport_no`          VARCHAR(100) COMMENT '护照号',
    `pedigree_cert_url`    VARCHAR(500) COMMENT '血统证书图片地址',
    `horse_type`           TINYINT                               NOT NULL COMMENT '类型: 1-俱乐部马 2-马主马 3-教练马',
    `owner_id`             BIGINT COMMENT '马主ID(马主马)',
    `responsible_coach_id` BIGINT COMMENT '责任教练ID',
    `responsible_groom_id` BIGINT COMMENT '责任马工ID',
    `boarding_start_date`  DATETIME COMMENT '寄养开始日期时间',
    `boarding_end_date`    DATETIME COMMENT '寄养结束日期时间',
    `health_status`        TINYINT     DEFAULT 1 COMMENT '健康状态: 1-健康 2-观察 3-治疗',
    `work_status`          TINYINT     DEFAULT 1 COMMENT '工作状态: 1-可用 2-休息 3-治疗',
    `horse_data`           TEXT COMMENT '马匹扩展数据JSON格式',
    `create_by`            VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time`          DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`            VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time`          DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`             INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`            INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX                  `idx_club_id` (`club_id`),
    INDEX                  `idx_owner_id` (`owner_id`),
    INDEX                  `idx_chip_no` (`chip_no`),
    INDEX                  `idx_horse_type` (`horse_type`),
    INDEX                  `idx_horse_code` (`horse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='马匹表';

-- ============================================
-- 2. 马匹健康计划表（m_horse_health_plan）
-- ============================================
CREATE TABLE IF NOT EXISTS `m_horse_health_plan` (
    `id`            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `horse_id`      BIGINT                                NOT NULL COMMENT '马匹ID',
    `plan_type`     VARCHAR(50)                           NOT NULL COMMENT '计划类型: shoeing-钉蹄, deworming-驱虫, dental-搓牙, vaccine-疫苗, medication-用药',
    `plan_name`     VARCHAR(100) COMMENT '计划名称',
    `cycle_days`    INT COMMENT '周期天数',
    `last_date`     DATETIME COMMENT '上次执行日期时间',
    `next_date`     DATETIME                              NOT NULL COMMENT '下次执行日期时间',
    `reminder_days` INT         DEFAULT 7 COMMENT '提前提醒天数',
    `plan_config`   TEXT COMMENT '计划配置JSON格式',
    `create_by`     VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time`   DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`     VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time`   DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`      INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`     INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX           `idx_horse_id` (`horse_id`),
    INDEX           `idx_next_date` (`next_date`),
    INDEX           `idx_plan_type` (`plan_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='马匹健康计划表';

-- ============================================
-- 3. 马匹健康记录表（m_horse_health_record）
-- ============================================
CREATE TABLE IF NOT EXISTS `m_horse_health_record` (
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `horse_id`    BIGINT                                NOT NULL COMMENT '马匹ID',
    `plan_id`     BIGINT COMMENT '关联计划ID',
    `record_type` VARCHAR(50)                           NOT NULL COMMENT '记录类型',
    `record_date` DATETIME                              NOT NULL COMMENT '记录日期时间',
    `executor_id` BIGINT COMMENT '执行人ID',
    `content`     TEXT COMMENT '记录内容',
    `img_url`     TEXT COMMENT '图片地址JSON格式',
    `next_date`   DATETIME COMMENT '下次执行日期时间',
    `record_data` TEXT COMMENT '记录扩展数据JSON格式',
    `create_by`   VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`   VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time` DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`    INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`   INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX         `idx_horse_id` (`horse_id`),
    INDEX         `idx_plan_id` (`plan_id`),
    INDEX         `idx_record_date` (`record_date`),
    INDEX         `idx_record_type` (`record_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='马匹健康记录表';

-- ============================================
-- 4. 菜单权限数据
-- ============================================

-- 马匹管理主菜单
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
(2400, '马匹管理', 1, 0, 3, NULL, NULL, 1, NULL, NULL, 'AlibabaOutlined', NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');

-- 马匹档案管理
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
(2401, '马匹档案', 2, 2400, 1, '/club/horse/horse-list', '/business/horse/horse-list.vue', 1, NULL, 'club:horse:query', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');

-- 马匹详情页（隐藏菜单）
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
(2402, '马匹详情', 2, 2400, 2, '/club/horse/horse-detail', '/business/horse/horse-detail.vue', 1, NULL, 'club:horse:detail', NULL, NULL, 0, NULL, 1, 0, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');

-- 健康计划管理菜单
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
(2403, '健康计划', 2, 2400, 3, '/club/horse/health-plan-list', '/business/horse/health-plan-list.vue', 1, NULL, 'club:horse:health:plan:query', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');

-- 健康记录管理菜单
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
(2404, '健康记录', 2, 2400, 4, '/club/horse/health-record-list', '/business/horse/health-record-list.vue', 1, NULL, 'club:horse:health:record:query', NULL, NULL, 0, NULL, 1, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');

-- ============================================
-- 5. 马匹管理权限（包含健康计划和健康记录权限）
-- ============================================
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
(2411, '马匹查询', 3, 2401, 1, NULL, NULL, 2, 'club:horse:query', 'club:horse:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2412, '马匹新增', 3, 2401, 2, NULL, NULL, 2, 'club:horse:add', 'club:horse:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2413, '马匹编辑', 3, 2401, 3, NULL, NULL, 2, 'club:horse:update', 'club:horse:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2414, '马匹删除', 3, 2401, 4, NULL, NULL, 2, 'club:horse:delete', 'club:horse:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2415, '马匹详情', 3, 2401, 5, NULL, NULL, 2, 'club:horse:detail', 'club:horse:detail', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),

-- 健康计划权限
(2421, '健康计划查询', 3, 2403, 1, NULL, NULL, 2, 'club:horse:health:plan:query', 'club:horse:health:plan:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2422, '健康计划新增', 3, 2403, 2, NULL, NULL, 2, 'club:horse:health:plan:add', 'club:horse:health:plan:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2423, '健康计划编辑', 3, 2403, 3, NULL, NULL, 2, 'club:horse:health:plan:update', 'club:horse:health:plan:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2424, '健康计划删除', 3, 2403, 4, NULL, NULL, 2, 'club:horse:health:plan:delete', 'club:horse:health:plan:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),

-- 健康记录权限
(2431, '健康记录查询', 3, 2404, 1, NULL, NULL, 2, 'club:horse:health:record:query', 'club:horse:health:record:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2432, '健康记录新增', 3, 2404, 2, NULL, NULL, 2, 'club:horse:health:record:add', 'club:horse:health:record:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2433, '健康记录编辑', 3, 2404, 3, NULL, NULL, 2, 'club:horse:health:record:update', 'club:horse:health:record:update', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00'),
(2434, '健康记录删除', 3, 2404, 4, NULL, NULL, 2, 'club:horse:health:record:delete', 'club:horse:health:record:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, '2024-01-08 10:00:00', 1, '2024-01-08 10:00:00');

-- ============================================
-- 6. 角色菜单权限分配（分配给管理员角色ID=1）
-- ============================================
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
-- 马匹管理主菜单和子菜单
(1, 2400, NOW(), NOW()),
(1, 2401, NOW(), NOW()), 
(1, 2402, NOW(), NOW()),
(1, 2403, NOW(), NOW()),
(1, 2404, NOW(), NOW()),

-- 马匹档案权限
(1, 2411, NOW(), NOW()),
(1, 2412, NOW(), NOW()),
(1, 2413, NOW(), NOW()),
(1, 2414, NOW(), NOW()),
(1, 2415, NOW(), NOW()),

-- 健康计划权限
(1, 2421, NOW(), NOW()),
(1, 2422, NOW(), NOW()),
(1, 2423, NOW(), NOW()),
(1, 2424, NOW(), NOW()),

-- 健康记录权限
(1, 2431, NOW(), NOW()),
(1, 2432, NOW(), NOW()),
(1, 2433, NOW(), NOW()),
(1, 2434, NOW(), NOW());

INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`) VALUES
                                                                                   (1, 408, NOW(), NOW()),
                                                                                   (1, 409, NOW(), NOW()),
                                                                                   (1, 410, NOW(), NOW()),
                                                                                   (1, 411, NOW(), NOW()),
                                                                                   (1, 412, NOW(), NOW()),
                                                                                   (1, 413, NOW(), NOW()),
                                                                                   (1, 414, NOW(), NOW());
