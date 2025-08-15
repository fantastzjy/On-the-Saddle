-- ========================================
-- 马术俱乐部商品订单管理系统 - 数据库表结构
-- 开发阶段一：数据库表结构创建
-- ========================================

-- 1. 商品基础表 (m_product)
CREATE TABLE `m_product`
(
    `product_id`   bigint                                  NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `club_id`      bigint                                  NOT NULL COMMENT '俱乐部ID',
    `product_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
    `product_code` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '商品编码',
    `product_type` tinyint                                 NOT NULL COMMENT '商品类型: 1-课程 2-课时包 3-活动',
    `sub_type`     varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '商品子类型',
    `description`  text COLLATE utf8mb4_general_ci         NOT NULL COMMENT '商品描述',
    `images`       text COLLATE utf8mb4_general_ci         NOT NULL COMMENT '商品图片地址列表JSON格式',
    `status`       tinyint                                 NOT NULL DEFAULT '1' COMMENT '商品状态: 1-上架 2-下架 3-售罄',
    `sort_order`   int                                     NOT NULL DEFAULT '0' COMMENT '排序',
    `create_by`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`  datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`  datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`     int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`    int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`product_id`),
    UNIQUE KEY `uk_product_code` (`club_id`, `product_code`),
    KEY            `idx_club_id` (`club_id`),
    KEY            `idx_product_type` (`product_type`),
    KEY            `idx_status` (`status`),
    KEY            `idx_is_delete` (`is_delete`),
    CONSTRAINT `fk_product_club` FOREIGN KEY (`club_id`) REFERENCES `m_club` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品基础表';

-- 2. 课程商品表 (m_product_course)
CREATE TABLE `m_product_course`
(
    `id`                 bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`         bigint                                 NOT NULL COMMENT '商品ID',
    `class_type`         tinyint                                NOT NULL COMMENT '课程分类: 1-单人课 2-多人课',
    `duration_minutes`   int                                    NOT NULL DEFAULT '0' COMMENT '时长（分钟）',
    `duration_periods`   decimal(4, 1)                          NOT NULL DEFAULT '0.0' COMMENT '时长（鞍时）',
    `max_students`       int                                    NOT NULL DEFAULT '1' COMMENT '最大人数',
    `coach_fee`          decimal(10, 2)                         NOT NULL DEFAULT '0.00' COMMENT '教练费',
    `horse_fee`          decimal(10, 2)                         NOT NULL DEFAULT '0.00' COMMENT '马匹费用',
    `base_price`         decimal(10, 2) GENERATED ALWAYS AS (`coach_fee` + `horse_fee`) STORED COMMENT '基础单价=教练费+马匹费用',
    `multi_price_config` text COLLATE utf8mb4_general_ci COMMENT '多人课价格配置JSON: {coaches: [{coach_id, prices: {2: price, 3: price, 4: price, 5: price}}]}',
    `create_by`          varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`        datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`        datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`),
    KEY                  `idx_class_type` (`class_type`),
    CONSTRAINT `fk_course_product` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='课程商品表';

-- 3. 课时包商品表 (m_product_package)
CREATE TABLE `m_product_package`
(
    `id`             bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`     bigint                                 NOT NULL COMMENT '商品ID',
    `details`        text COLLATE utf8mb4_general_ci        NOT NULL COMMENT '课包详情',
    `price`          decimal(10, 2)                         NOT NULL COMMENT '课包单价',
    `total_sessions` int                                    NOT NULL DEFAULT '0' COMMENT '总课时数',
    `validity_days`  int                                    NOT NULL DEFAULT '365' COMMENT '有效期（天）',
    `stock_quantity` int                                    NOT NULL DEFAULT '0' COMMENT '库存数量',
    `create_by`      varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`    datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`    datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`),
    CONSTRAINT `fk_package_product` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='课时包商品表';

-- 4. 课时包用户关联表 (m_package_member)
CREATE TABLE `m_package_member`
(
    `id`                 bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_item_id`      bigint                                 NOT NULL COMMENT '订单明细ID',
    `package_id`         bigint                                 NOT NULL COMMENT '课时包ID',
    `member_id`          bigint                                 NOT NULL COMMENT '会员ID',
    `remaining_sessions` int                                    NOT NULL COMMENT '剩余课时数',
    `expire_date`        date                                   NOT NULL COMMENT '到期日期',
    `coach_id`           bigint                                          DEFAULT '0' COMMENT '绑定教练ID（可选）',
    `status`             tinyint                                NOT NULL DEFAULT '1' COMMENT '状态: 1-正常 2-已过期 3-已用完',
    `create_by`          varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`        datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`        datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY                  `idx_order_item_id` (`order_item_id`),
    KEY                  `idx_package_id` (`package_id`),
    KEY                  `idx_member_id` (`member_id`),
    KEY                  `idx_expire_date` (`expire_date`),
    KEY                  `idx_coach_id` (`coach_id`),
    CONSTRAINT `fk_package_member_package` FOREIGN KEY (`package_id`) REFERENCES `m_product_package` (`id`),
    CONSTRAINT `fk_package_member_member` FOREIGN KEY (`member_id`) REFERENCES `m_member` (`member_id`),
    CONSTRAINT `fk_package_member_coach` FOREIGN KEY (`coach_id`) REFERENCES `m_coach` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='课时包用户关联表';

-- 5. 活动商品表 (m_product_activity)
CREATE TABLE `m_product_activity`
(
    `id`                bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`        bigint                                  NOT NULL COMMENT '商品ID',
    `activity_name`     varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '活动名称（最多5个字）',
    `activity_details`  text COLLATE utf8mb4_general_ci         NOT NULL COMMENT '活动详情全文介绍',
    `start_time`        datetime                                NOT NULL COMMENT '活动开始时间',
    `end_time`          datetime                                NOT NULL COMMENT '活动结束时间',
    `activity_location` varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '活动地点',
    `price`             decimal(10, 2)                          NOT NULL COMMENT '活动单价',
    `max_participants`  int                                     NOT NULL COMMENT '最大参与人数',
    `refund_rule`       text COLLATE utf8mb4_general_ci         NOT NULL COMMENT '退款规则',
    `detail_images`     text COLLATE utf8mb4_general_ci         NOT NULL COMMENT '详情图片地址列表JSON格式（最多9张）',
    `create_by`         varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`       datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`       datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`),
    KEY                 `idx_start_time` (`start_time`),
    KEY                 `idx_end_time` (`end_time`),
    CONSTRAINT `fk_activity_product` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动商品表';

-- 6. 商品教练关联表 (m_product_coach)
CREATE TABLE `m_product_coach`
(
    `id`          bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`  bigint                                 NOT NULL COMMENT '商品ID',
    `coach_id`    bigint                                 NOT NULL COMMENT '教练ID',
    `is_default`  tinyint                                NOT NULL DEFAULT '0' COMMENT '是否默认教练: 1-是 0-否',
    `create_by`   varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time` datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_coach` (`product_id`, `coach_id`),
    KEY           `idx_product_id` (`product_id`),
    KEY           `idx_coach_id` (`coach_id`),
    CONSTRAINT `fk_product_coach_product` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`),
    CONSTRAINT `fk_product_coach_coach` FOREIGN KEY (`coach_id`) REFERENCES `m_coach` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品教练关联表';

-- 7. 订单主表 (m_order)
CREATE TABLE `m_order`
(
    `order_id`       bigint                                  NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '订单号',
    `club_id`        bigint                                  NOT NULL COMMENT '俱乐部ID',
    `member_id`      bigint                                  NOT NULL COMMENT '会员ID',
    `order_type`     tinyint                                 NOT NULL COMMENT '订单类型: 1-课程 2-课时包 3-活动',
    `order_status`   tinyint                                 NOT NULL DEFAULT '1' COMMENT '订单状态: 1-待支付 2-已支付 3-已完成 4-已取消 5-已退款',
    `total_amount`   decimal(10, 2)                          NOT NULL COMMENT '订单总金额',
    `paid_amount`    decimal(10, 2)                          NOT NULL DEFAULT '0.00' COMMENT '已支付金额',
    `payment_method` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '支付方式',
    `payment_time`   datetime                                         DEFAULT NULL COMMENT '支付时间',
    `remark`         varchar(500) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单备注',
    `create_by`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`       int                                     NOT NULL DEFAULT '1' COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`      int                                     NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`order_id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY              `idx_club_id` (`club_id`),
    KEY              `idx_member_id` (`member_id`),
    KEY              `idx_order_status` (`order_status`),
    KEY              `idx_create_time` (`create_time`),
    KEY              `idx_payment_time` (`payment_time`),
    KEY              `idx_is_delete` (`is_delete`),
    CONSTRAINT `fk_order_club` FOREIGN KEY (`club_id`) REFERENCES `m_club` (`club_id`),
    CONSTRAINT `fk_order_member` FOREIGN KEY (`member_id`) REFERENCES `m_member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单主表';

-- 8. 订单明细表 (m_order_item)
CREATE TABLE `m_order_item`
(
    `id`           bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id`     bigint                                  NOT NULL COMMENT '订单ID',
    `product_id`   bigint                                  NOT NULL COMMENT '商品ID',
    `product_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
    `product_type` tinyint                                 NOT NULL COMMENT '商品类型: 1-课程 2-课时包 3-活动',
    `quantity`     int                                     NOT NULL DEFAULT '1' COMMENT '数量',
    `unit_price`   decimal(10, 2)                          NOT NULL COMMENT '单价',
    `total_price`  decimal(10, 2)                          NOT NULL COMMENT '小计',
    `coach_id`     bigint                                  NOT NULL DEFAULT '0' COMMENT '关联教练ID',
    `item_config`  text COLLATE utf8mb4_general_ci COMMENT '商品配置JSON格式',
    `create_time`  datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY            `idx_order_id` (`order_id`),
    KEY            `idx_product_id` (`product_id`),
    CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `m_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单明细表';

-- 9. 预约约课表 (m_booking)
CREATE TABLE `m_booking`
(
    `booking_id`       bigint                                  NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `order_id`         bigint                                  NOT NULL COMMENT '订单ID',
    `order_item_id`    bigint                                  NOT NULL COMMENT '订单明细ID',
    `club_id`          bigint                                  NOT NULL COMMENT '俱乐部ID',
    `member_id`        bigint                                  NOT NULL COMMENT '会员ID',
    `product_id`       bigint                                  NOT NULL COMMENT '商品ID',
    `coach_id`         bigint                                  NOT NULL DEFAULT '0' COMMENT '教练ID',
    `horse_id`         bigint                                  NOT NULL DEFAULT '0' COMMENT '马匹ID',
    `start_time`       datetime                                NOT NULL COMMENT '预约开始时间',
    `end_time`         datetime                                NOT NULL COMMENT '预约结束时间',
    `booking_status`   tinyint                                 NOT NULL DEFAULT '1' COMMENT '预约状态: 1-待确认 2-已确认 3-进行中 4-已完成 5-已取消',
    `actual_coach_fee` decimal(10, 2)                          NOT NULL DEFAULT '0.00' COMMENT '实际教练费（到店可调整）',
    `actual_horse_fee` decimal(10, 2)                          NOT NULL DEFAULT '0.00' COMMENT '实际马匹费（到店可调整）',
    `arrival_time`     datetime                                         DEFAULT NULL COMMENT '到店时间',
    `completion_time`  datetime                                         DEFAULT NULL COMMENT '完成时间',
    `cancel_reason`    varchar(500) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '取消原因',
    `create_by`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`      datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`      datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`booking_id`),
    KEY                `idx_order_id` (`order_id`),
    KEY                `idx_member_id` (`member_id`),
    KEY                `idx_coach_id` (`coach_id`),
    KEY                `idx_start_time` (`start_time`),
    KEY                `idx_booking_status` (`booking_status`),
    CONSTRAINT `fk_booking_order` FOREIGN KEY (`order_id`) REFERENCES `m_order` (`order_id`),
    CONSTRAINT `fk_booking_member` FOREIGN KEY (`member_id`) REFERENCES `m_member` (`member_id`),
    CONSTRAINT `fk_booking_coach` FOREIGN KEY (`coach_id`) REFERENCES `m_coach` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预约约课表';

-- 10. 课单表 (m_lesson_schedule)
CREATE TABLE `m_lesson_schedule`
(
    `schedule_id`       bigint                                 NOT NULL AUTO_INCREMENT COMMENT '课单ID',
    `booking_id`        bigint                                 NOT NULL COMMENT '预约ID',
    `schedule_no`       varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '课单号',
    `club_id`           bigint                                 NOT NULL COMMENT '俱乐部ID',
    `member_id`         bigint                                 NOT NULL COMMENT '会员ID',
    `coach_id`          bigint                                 NOT NULL COMMENT '教练ID',
    `horse_id`          bigint                                 NOT NULL DEFAULT '0' COMMENT '马匹ID',
    `lesson_date`       date                                   NOT NULL COMMENT '上课日期',
    `start_time`        datetime                               NOT NULL COMMENT '开始时间',
    `end_time`          datetime                               NOT NULL COMMENT '结束时间',
    `lesson_status`     tinyint                                NOT NULL DEFAULT '1' COMMENT '课单状态: 1-待上课 2-进行中 3-已完成 4-已取消',
    `notification_sent` tinyint                                NOT NULL DEFAULT '0' COMMENT '是否已发送通知: 0-未发送 1-已发送',
    `reminder_sent`     tinyint                                NOT NULL DEFAULT '0' COMMENT '是否已发送提醒: 0-未发送 1-已发送',
    `create_by`         varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`       datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`       datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`schedule_id`),
    UNIQUE KEY `uk_schedule_no` (`schedule_no`),
    KEY                 `idx_booking_id` (`booking_id`),
    KEY                 `idx_coach_id` (`coach_id`),
    KEY                 `idx_lesson_date` (`lesson_date`),
    KEY                 `idx_start_time` (`start_time`),
    CONSTRAINT `fk_schedule_booking` FOREIGN KEY (`booking_id`) REFERENCES `m_booking` (`booking_id`),
    CONSTRAINT `fk_schedule_coach` FOREIGN KEY (`coach_id`) REFERENCES `m_coach` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='课单表';

-- 11. 前台核销记录表 (m_checkin_record)
CREATE TABLE `m_checkin_record`
(
    `id`              bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `booking_id`      bigint                                  NOT NULL COMMENT '预约ID',
    `schedule_id`     bigint                                  NOT NULL COMMENT '课单ID',
    `member_id`       bigint                                  NOT NULL COMMENT '会员ID',
    `checkin_time`    datetime                                NOT NULL COMMENT '签到时间',
    `checkin_staff`   varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '签到确认人员',
    `checkout_time`   datetime                                         DEFAULT NULL COMMENT '签退时间',
    `checkout_staff`  varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '签退确认人员',
    `actual_duration` int                                     NOT NULL DEFAULT '0' COMMENT '实际上课时长（分钟）',
    `remark`          varchar(500) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_by`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY               `idx_booking_id` (`booking_id`),
    KEY               `idx_member_id` (`member_id`),
    KEY               `idx_checkin_time` (`checkin_time`),
    CONSTRAINT `fk_checkin_booking` FOREIGN KEY (`booking_id`) REFERENCES `m_booking` (`booking_id`),
    CONSTRAINT `fk_checkin_member` FOREIGN KEY (`member_id`) REFERENCES `m_member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='前台核销记录表';

-- 12. 时间段模板表 (m_time_slot_template)
CREATE TABLE `m_time_slot_template`
(
    `id`           bigint                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `club_id`      bigint                                 NOT NULL COMMENT '俱乐部ID',
    `slot_name`    varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '时段名称',
    `start_time`   time                                   NOT NULL COMMENT '开始时间',
    `end_time`     time                                   NOT NULL COMMENT '结束时间',
    `week_days`    varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '适用星期 1,2,3,4,5,6,7',
    `is_available` tinyint                                NOT NULL DEFAULT '1' COMMENT '是否可用: 1-可用 0-不可用',
    `sort_order`   int                                    NOT NULL DEFAULT '0' COMMENT '排序',
    `create_time`  datetime                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_delete`    int                                    NOT NULL DEFAULT '0' COMMENT '是否删除;1=已删除;0=未删除;',
    PRIMARY KEY (`id`),
    KEY            `idx_club_id` (`club_id`),
    KEY            `idx_start_time` (`start_time`),
    KEY            `idx_is_delete` (`is_delete`),
    CONSTRAINT `fk_time_slot_club` FOREIGN KEY (`club_id`) REFERENCES `m_club` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='时间段模板表';

-- 13. 教练可用时间表 (m_coach_availability)
CREATE TABLE `m_coach_availability`
(
    `id`             bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `club_id`        bigint                                  NOT NULL COMMENT '俱乐部ID',
    `coach_id`       bigint                                  NOT NULL COMMENT '教练ID',
    `available_date` date                                    NOT NULL COMMENT '可用日期',
    `start_time`     time                                    NOT NULL COMMENT '开始时间',
    `end_time`       time                                    NOT NULL COMMENT '结束时间',
    `status`         tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态: 1-可用 2-请假 3-占用',
    `remark`         varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_coach_date_time` (`coach_id`, `available_date`, `start_time`),
    KEY              `idx_club_id` (`club_id`),
    KEY              `idx_available_date` (`available_date`),
    CONSTRAINT `fk_coach_availability_club` FOREIGN KEY (`club_id`) REFERENCES `m_club` (`club_id`),
    CONSTRAINT `fk_coach_availability_coach` FOREIGN KEY (`coach_id`) REFERENCES `m_coach` (`coach_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='教练可用时间表';

-- 14. 马匹可用时间表 (m_horse_availability)
CREATE TABLE `m_horse_availability`
(
    `id`             bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `club_id`        bigint                                  NOT NULL COMMENT '俱乐部ID',
    `horse_id`       bigint                                  NOT NULL COMMENT '马匹ID',
    `available_date` date                                    NOT NULL COMMENT '可用日期',
    `start_time`     time                                    NOT NULL COMMENT '开始时间',
    `end_time`       time                                    NOT NULL COMMENT '结束时间',
    `status`         tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态: 1-可用 2-休息 3-治疗 4-占用',
    `remark`         varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_horse_date_time` (`horse_id`, `available_date`, `start_time`),
    KEY              `idx_club_id` (`club_id`),
    KEY              `idx_available_date` (`available_date`),
    CONSTRAINT `fk_horse_availability_club` FOREIGN KEY (`club_id`) REFERENCES `m_club` (`club_id`),
    CONSTRAINT `fk_horse_availability_horse` FOREIGN KEY (`horse_id`) REFERENCES `m_horse` (`horse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='马匹可用时间表';
