/*
 * 会员管理模块数据库表创建脚本
 *
 * @Author: Claude Code
 * @Date: 2025-01-11
 * @Copyright: 马术俱乐部管理系统
 */

SET NAMES utf8mb4;

-- ----------------------------
-- 会员主表
-- ----------------------------
DROP TABLE IF EXISTS `m_member`;
CREATE TABLE `m_member`
(
    `member_id`              bigint                                  NOT NULL AUTO_INCREMENT COMMENT '会员ID',
    `member_no`              varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '会员编号',
    `union_id`               varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信unionId',
    `open_id`                varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信openId',
    `login_name`             varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '登录账号',
    `login_pwd`              varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录密码',
    `actual_name`            varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '真实姓名',
    `phone`                  varchar(20) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '手机号',
    `email`                  varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '邮箱',
    `avatar_url`             varchar(500) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像地址',
    `gender`                 tinyint                                 NOT NULL DEFAULT '0' COMMENT '性别: 0-未知 1-男 2-女',
    `birth_date`             date                                             DEFAULT NULL COMMENT '出生日期',
    `club_id`                bigint                                  NOT NULL COMMENT '所属俱乐部ID',
    `is_membership`          tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否为会籍会员: 1-是 0-否',
    `membership_expire_date` date                                             DEFAULT NULL COMMENT '会籍有效期',
    `id_card_no`             varchar(20) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '身份证号',
    `rider_cert_no`          varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '骑手证号',
    `registration_status`    tinyint                                 NOT NULL DEFAULT '0' COMMENT '注册状态: 0-未激活 1-已注册',
    `created_by_guardian`    tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否由监护人创建: 1-是 0-否',
    `disabled_flag`          tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否禁用: 0-启用 1-禁用',
    `profile_data`           text COLLATE utf8mb4_general_ci COMMENT '扩展数据JSON格式',
    `last_login_time`        datetime                                         DEFAULT NULL COMMENT '最后登录时间',
    `create_by`              varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`            datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`              varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`            datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`               int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`              int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`member_id`),
    UNIQUE KEY `uk_member_no` (`member_no`),
    UNIQUE KEY `uk_login_name` (`login_name`),
    KEY                      `idx_phone` (`phone`),
    KEY                      `idx_club_id` (`club_id`),
    KEY                      `idx_registration_status` (`registration_status`),
    KEY                      `idx_is_membership` (`is_membership`),
    KEY                      `idx_membership_expire_date` (`membership_expire_date`),
    KEY                      `idx_id_card_no` (`id_card_no`),
    KEY                      `idx_rider_cert_no` (`rider_cert_no`),
    KEY                      `idx_create_time` (`create_time`),
    KEY                      `idx_is_delete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员表';

-- ----------------------------
-- 家庭组表
-- ----------------------------
DROP TABLE IF EXISTS `m_family_group`;
CREATE TABLE `m_family_group`
(
    `family_group_id` bigint                                  NOT NULL AUTO_INCREMENT COMMENT '家庭组ID',
    `family_name`     varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '家庭名称',
    `club_id`         bigint                                  NOT NULL COMMENT '俱乐部ID',
    `main_contact_id` bigint                                  NOT NULL DEFAULT '0' COMMENT '主要联系人会员ID',
    `description`     varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '家庭描述',
    `create_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`        int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`       int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`family_group_id`),
    KEY               `idx_club_id` (`club_id`),
    KEY               `idx_main_contact_id` (`main_contact_id`),
    KEY               `idx_is_delete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='家庭组表';

-- ----------------------------
-- 家庭成员关系表
-- ----------------------------
DROP TABLE IF EXISTS `m_family_member_relation`;
CREATE TABLE `m_family_member_relation`
(
    `id`              bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `family_group_id` bigint                                  NOT NULL COMMENT '家庭组ID',
    `member_id`       bigint                                  NOT NULL COMMENT '会员ID',
    `is_guardian`     tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否监护人: 1-是 0-否',
    `join_date`       datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入家庭日期',
    `remark`          varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`        int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`       int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_family_member` (`family_group_id`, `member_id`),
    KEY               `idx_family_group_id` (`family_group_id`),
    KEY               `idx_member_id` (`member_id`),
    KEY               `idx_is_guardian` (`is_guardian`),
    KEY               `idx_is_delete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='家庭成员关系表';

-- ----------------------------
-- 未注册成员扩展信息表
-- ----------------------------
DROP TABLE IF EXISTS `m_family_member_extra`;
CREATE TABLE `m_family_member_extra`
(
    `id`                   bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `member_id`            bigint                                 NOT NULL COMMENT '关联会员ID',
    `guardian_phone`       varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '监护人电话',
    `guardian_name`        varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '监护人姓名',
    `guardian_relation`    varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '与监护人关系',
    `default_coach_id`     bigint                                 NOT NULL DEFAULT '0' COMMENT '默认教练ID',
    `default_course_level` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '默认课程级别',
    `extra_data`           text COLLATE utf8mb4_general_ci COMMENT '其他扩展信息JSON格式',
    `create_by`            varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`          datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`            varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`          datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`             int                                    NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`            int                                    NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_id` (`member_id`),
    KEY                    `idx_default_coach_id` (`default_coach_id`),
    KEY                    `idx_is_delete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='未注册成员扩展信息表';

-- ----------------------------
-- 会籍续费历史表
-- ----------------------------
DROP TABLE IF EXISTS `m_membership_renew_history`;
CREATE TABLE `m_membership_renew_history`
(
    `id`              bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `member_id`       bigint                                  NOT NULL COMMENT '会员ID',
    `renew_months`    int                                     NOT NULL COMMENT '续费月数',
    `renew_amount`    decimal(10, 2)                          NOT NULL COMMENT '续费金额',
    `old_expire_date` date                                             DEFAULT NULL COMMENT '原到期时间',
    `new_expire_date` date                                    NOT NULL COMMENT '新到期时间',
    `payment_method`  varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '支付方式',
    `remark`          varchar(500) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `renew_date`      datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '续费时间',
    `create_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`        int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`       int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`id`),
    KEY               `idx_member_id` (`member_id`),
    KEY               `idx_renew_date` (`renew_date`),
    KEY               `idx_payment_method` (`payment_method`),
    KEY               `idx_is_delete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会籍续费历史表';

-- ----------------------------
-- 用户课时包表（预留，关联订单系统）
-- ----------------------------
DROP TABLE IF EXISTS `m_user_package`;
CREATE TABLE `m_user_package`
(
    `id`                 bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `member_id`          bigint                                  NOT NULL COMMENT '会员ID',
    `package_order_id`   bigint                                  NOT NULL COMMENT '购买订单ID',
    `package_id`         bigint                                  NOT NULL COMMENT '课时包商品ID',
    `package_name`       varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '课时包名称',
    `total_lessons`      int                                     NOT NULL COMMENT '总课时数',
    `used_lessons`       int                                     NOT NULL DEFAULT '0' COMMENT '已用课时数',
    `remaining_lessons`  int                                     NOT NULL COMMENT '剩余课时数',
    `purchase_date`      datetime                                NOT NULL COMMENT '购买日期时间',
    `expire_date`        datetime                                NOT NULL DEFAULT '2099-12-31 23:59:59' COMMENT '到期日期时间',
    `applicable_courses` text COLLATE utf8mb4_general_ci COMMENT '适用课程ID列表JSON格式',
    `create_by`          varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`        datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`        datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`           int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`          int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`id`),
    KEY                  `idx_member_id` (`member_id`),
    KEY                  `idx_package_order_id` (`package_order_id`),
    KEY                  `idx_expire_date` (`expire_date`),
    KEY                  `idx_is_delete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户课时包表';
