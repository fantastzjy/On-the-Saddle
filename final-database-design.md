# 马术俱乐部SaaS系统数据库设计终版方案（优化版）

## 一、核心设计原则

1. **统一用户体系**：所有用户（注册/未注册）都在用户表中有记录
2. **简化家庭管理**：通过家庭组统一管理复杂的家庭关系
3. **避免数据迁移**：注册时只需激活用户，无需数据迁移
4. **保持现有架构**：基于Smart Admin v3进行扩展

## 二、统一审计字段标准

所有业务表统一使用以下审计字段：

```sql
-- 标准审计字段
create_by
varchar(50)    default ''                not null comment '创建人',
create_time             datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
update_by               varchar(50)    default ''                not null comment '更新人',
update_time             datetime       default CURRENT_TIMESTAMP not null on
update CURRENT_TIMESTAMP comment '更新时间',
    is_valid int default 1 not null comment '是否有效;1=有效;0=无效;',
    is_delete int default 0 not null comment '是否删除;1=已删除;0=未删除;'
```

## 三、优化要点

### 3.1 核心优化策略

1. **预创建用户记录**：
    - 未注册家庭成员也在用户表中创建记录
    - 使用 `registration_status` 区分用户激活状态
    - 避免复杂的数据迁移流程

2. **统一用户管理**：
    - 所有业务操作统一使用 user_id
    - 订单、课时包等业务表直接关联用户表
    - 简化权限控制和数据查询逻辑

3. **家庭组概念**：
    - 支持多对多的复杂家庭关系
    - 精细化的权限控制（预约权限、支付权限等）
    - 便于管理重组家庭、隔代抚养等场景

### 3.2 技术优化

- **移除冗余状态字段**：优先使用 is_valid 和 is_delete 管理状态
- **统一JSON字段为TEXT**：提高数据库兼容性
- **统一时间字段为DATETIME**：除营业时间外，统一时间精度
- **统一图片字段命名**：所有图片字段使用 _url 后缀

## 四、完整数据库表设计

### 4.1 用户管理模块（基于现有RBAC体系扩展）

```sql
-- 修改现有用户表，增加注册状态
ALTER TABLE `t_employee`
    ADD COLUMN `registration_status` TINYINT NOT NULL DEFAULT 1 COMMENT '注册状态: 0-未激活 1-已注册';

-- 用户扩展表 - 基于现有RBAC体系的扩展
CREATE TABLE `m_user_profile`
(
    `id`                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id`             BIGINT                                NOT NULL COMMENT '关联t_employee.employee_id',
    `user_type`           TINYINT                               NOT NULL COMMENT '用户类型: 1-系统员工 2-俱乐部会员 3-马主 4-教练',
    `club_id`             BIGINT          DEFAULT 0             NOT NULL COMMENT '所属俱乐部ID',
    `created_by_guardian` TINYINT         DEFAULT 0             NOT NULL COMMENT '是否由监护人创建: 1-是 0-否',
    `profile_data`        TEXT                                  NOT NULL COMMENT '扩展数据JSON格式',
    `create_by`           VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`         DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`           VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`         DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`            INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`           INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_user_type` (`user_id`, `user_type`),
    INDEX                 `idx_user_id` (`user_id`),
    INDEX                 `idx_club_id` (`club_id`),
    INDEX                 `idx_user_type` (`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户扩展信息表';
```

### 4.2 家庭管理模块（全新设计）

```sql
-- 家庭组表
CREATE TABLE `m_family_group`
(
    `family_group_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '家庭组ID',
    `family_name`     VARCHAR(50)                           NOT NULL COMMENT '家庭名称',
    `club_id`         BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `main_contact_id` BIGINT          DEFAULT 0             NOT NULL COMMENT '主要联系人ID',
    `description`     VARCHAR(200)    DEFAULT ''            NOT NULL COMMENT '家庭描述',
    `create_by`       VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`     DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`       VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`     DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`        INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`       INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX             `idx_club_id` (`club_id`),
    INDEX             `idx_main_contact_id` (`main_contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭组表';

-- 家庭成员关系表（统一管理所有用户）
CREATE TABLE `m_family_member_relation`
(
    `id`                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `family_group_id`   BIGINT                                NOT NULL COMMENT '家庭组ID',
    `user_id`           BIGINT                                NOT NULL COMMENT '用户ID（统一关联t_employee表）',
    `member_type`       TINYINT                               NOT NULL COMMENT '成员类型: 1-家长 2-孩子 3-其他',
    `relationship`      VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '关系: father-父亲, mother-母亲, child-子女, grandparent-祖父母等',
    `is_guardian`       TINYINT         DEFAULT 0             NOT NULL COMMENT '是否监护人: 1-是 0-否',
    `can_book`          TINYINT         DEFAULT 1             NOT NULL COMMENT '是否可预约: 1-可以 0-不可以',
    `can_pay`           TINYINT         DEFAULT 0             NOT NULL COMMENT '是否可支付: 1-可以 0-不可以',
    `emergency_contact` TINYINT         DEFAULT 0             NOT NULL COMMENT '是否紧急联系人: 1-是 0-否',
    `join_date`         DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '加入家庭日期',
    `create_by`         VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`       DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`         VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`       DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`          INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`         INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_family_user` (`family_group_id`, `user_id`),
    INDEX               `idx_family_group_id` (`family_group_id`),
    INDEX               `idx_user_id` (`user_id`),
    INDEX               `idx_member_type` (`member_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭成员关系表';

-- 未注册成员扩展信息表（仅存储特有信息）
CREATE TABLE `m_family_member_extra`
(
    `id`                   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id`              BIGINT                                NOT NULL COMMENT '关联用户ID',
    `guardian_phone`       VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '监护人电话',
    `guardian_name`        VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '监护人姓名',
    `rider_cert_no`        VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '骑手证号',
    `default_coach_id`     BIGINT          DEFAULT 0             NOT NULL COMMENT '默认教练ID',
    `default_course_level` VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '默认课程级别',
    `create_by`            VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`          DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`            VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`          DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`             INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`            INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX                  `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='未注册成员扩展信息表';
```

### 4.3 俱乐部管理模块

```sql
-- 俱乐部表
create table m_club
(
    club_id              bigint auto_increment comment '俱乐部ID'
        primary key,
    club_name            varchar(100)                           not null comment '俱乐部名称',
    club_code            varchar(50)  default ''                not null comment '俱乐部编码',
    logo_url             varchar(500) default ''                not null comment 'LOGO地址',
    carousel_images      text                                   not null comment '轮播图片地址列表JSON格式',
    pc_banner_url        varchar(500) default ''                not null comment 'PC端首页图片地址',
    business_start_time  time         default '09:00:00'        not null comment '营业开始时间',
    business_end_time    time         default '18:00:00'        not null comment '营业结束时间',
    address              varchar(200) default ''                not null comment '详细地址',
    phone                varchar(20)  default ''                not null comment '电话',
    description          text                                   not null comment '俱乐部详情',
    honor_info           text                                   not null comment '荣誉信息',
    booking_notice       text                                   not null comment '约课需知',
    province             varchar(50)  default ''                not null comment '省份',
    city                 varchar(50)  default ''                not null comment '城市',
    district             varchar(50)  default ''                not null comment '区县',
    business_license_url varchar(500) default ''                not null comment '营业执照图片地址',
    contact_person       varchar(50)  default ''                not null comment '联系人',
    contact_phone        varchar(20)  default ''                not null comment '联系电话',
    email                varchar(100) default ''                not null comment '邮箱',
    create_by            varchar(50)  default ''                not null comment '创建人',
    create_time          datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by            varchar(50)  default ''                not null comment '更新人',
    update_time          datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid             int          default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete            int          default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_club_code
        unique (club_code)
) comment '俱乐部表';

create index idx_club_name
    on m_club (club_name);

create index idx_is_delete
    on m_club (is_delete);

create index idx_is_valid
    on m_club (is_valid);
```

### 4.4 教练管理模块

```sql
-- 教练表
create table m_coach
(
    coach_id                 bigint auto_increment comment '教练ID'
        primary key,
    club_id                  bigint                                     not null comment '俱乐部ID',
    user_id                  bigint                                     not null comment '关联用户ID',
    coach_no                 varchar(50)  default ''                    not null comment '教练编号',
    avatar_url               varchar(500) default ''                    not null comment '头像照片地址',
    entry_date               datetime     default '1970-01-01 00:00:00' not null comment '从业时间',
    specialties              varchar(200) default ''                    not null comment '专长领域',
    introduction             text                                       not null comment '个人介绍',
    rider_cert_no            varchar(100) default ''                    not null comment '骑手证号码',
    rider_level_show_jumping varchar(20)  default ''                    not null comment '场地障碍等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    rider_level_dressage     varchar(20)  default ''                    not null comment '盛装舞步等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    rider_level_eventing     varchar(20)  default ''                    not null comment '三项赛等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    rider_cert_img_url       text                                       not null comment '骑手证书图片地址JSON格式',
    coach_cert_no            varchar(100) default ''                    not null comment '星级教练证号码',
    coach_level              varchar(20)  default ''                    not null comment '教练等级: 一星,二星,三星,四星,五星',
    coach_cert_img_url       text                                       not null comment '教练证书图片地址JSON格式',
    sort_order               int          default 0                     not null comment '排序',
    create_by                varchar(50)  default ''                    not null comment '创建人',
    create_time              datetime     default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by                varchar(50)  default ''                    not null comment '更新人',
    update_time              datetime     default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid                 int          default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete                int          default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_club_user
        unique (club_id, user_id)
) comment '教练表';

create index idx_club_id
    on m_coach (club_id);

create index idx_coach_no
    on m_coach (coach_no);

create index idx_user_id
    on m_coach (user_id);

-- 教练课表时间段表
CREATE TABLE `m_coach_schedule`
(
    `id`                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `club_id`           BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `coach_id`          BIGINT                                NOT NULL COMMENT '教练ID',
    `schedule_date`     DATETIME                              NOT NULL COMMENT '日期时间',
    `start_time`        DATETIME                              NOT NULL COMMENT '开始时间',
    `end_time`          DATETIME                              NOT NULL COMMENT '结束时间',
    `max_students`      INT         DEFAULT 1                 NOT NULL COMMENT '最大学员数',
    `booked_count`      INT         DEFAULT 0                 NOT NULL COMMENT '已预约数量',
    `available_courses` TEXT                                  NOT NULL COMMENT '可授课程ID列表JSON格式',
    `remark`            VARCHAR(200) DEFAULT ''               NOT NULL COMMENT '备注',
    `create_by`         VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time`       DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`         VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time`       DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`          INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`         INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_coach_datetime` (`coach_id`, `schedule_date`, `start_time`),
    INDEX               `idx_club_id` (`club_id`),
    INDEX               `idx_coach_id` (`coach_id`),
    INDEX               `idx_schedule_date` (`schedule_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练课表时间段表';
```

### 4.5 商品管理模块 (课程作为商品)

```sql
-- 扩展现有商品表用于课程管理
ALTER TABLE `t_goods`
    ADD COLUMN `club_id` BIGINT COMMENT '俱乐部ID';
ALTER TABLE `t_goods`
    ADD COLUMN `goods_type` VARCHAR(50) DEFAULT 'course' COMMENT '商品类型: course-课程, package-课时包, activity-活动';
ALTER TABLE `t_goods`
    ADD COLUMN `course_config` TEXT COMMENT '课程配置JSON格式';
ALTER TABLE `t_goods`
    ADD COLUMN `create_by` VARCHAR(50) DEFAULT '' NOT NULL COMMENT '创建人';
ALTER TABLE `t_goods`
    ADD COLUMN `update_by` VARCHAR(50) DEFAULT '' NOT NULL COMMENT '更新人';
ALTER TABLE `t_goods`
    ADD COLUMN `is_valid` INT DEFAULT 1 NOT NULL COMMENT '是否有效;1=有效;0=无效;';
ALTER TABLE `t_goods`
    ADD COLUMN `is_delete` INT DEFAULT 0 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;';

-- 课程时间段表
CREATE TABLE `m_course_time_slot`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `club_id`        BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `course_id`      BIGINT                                NOT NULL COMMENT '课程ID(goods_id)',
    `coach_id`       BIGINT                                NOT NULL COMMENT '教练ID',
    `available_date` DATETIME                              NOT NULL COMMENT '可用日期',
    `start_time`     DATETIME                              NOT NULL COMMENT '开始时间',
    `end_time`       DATETIME                              NOT NULL COMMENT '结束时间',
    `max_capacity`   INT         DEFAULT 1 COMMENT '最大容量',
    `booked_count`   INT         DEFAULT 0 COMMENT '已预约数量',
    `create_by`      VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time`    DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`      VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time`    DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`       INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`      INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_course_coach_time` (`course_id`, `coach_id`, `available_date`, `start_time`),
    INDEX            `idx_club_id` (`club_id`),
    INDEX            `idx_course_id` (`course_id`),
    INDEX            `idx_coach_id` (`coach_id`),
    INDEX            `idx_available_date` (`available_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程时间段表';
```

### 4.6 订单管理模块（统一用户ID）

```sql
-- 订单表 - 统一使用user_id
CREATE TABLE `m_order`
(
    `order_id`       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no`       VARCHAR(50)                           NOT NULL UNIQUE COMMENT '订单号',
    `club_id`        BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `customer_id`    BIGINT                                NOT NULL COMMENT '客户ID(统一使用user_id)',
    `order_type`     VARCHAR(50)                           NOT NULL COMMENT '订单类型: course-课程, package-课时包, activity-活动',
    `goods_id`       BIGINT                                NOT NULL COMMENT '商品ID',
    `goods_name`     VARCHAR(100)                          NOT NULL COMMENT '商品名称',
    `time_slot_id`   BIGINT          DEFAULT 0             NOT NULL COMMENT '时间段ID(课程订单必填)',
    `coach_id`       BIGINT          DEFAULT 0             NOT NULL COMMENT '教练ID',
    `horse_id`       BIGINT          DEFAULT 0             NOT NULL COMMENT '马匹ID',
    `order_date`     DATETIME                              NOT NULL COMMENT '订单日期时间',
    `class_date`     DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '上课日期时间',
    `start_time`     DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '开始时间',
    `end_time`       DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '结束时间',
    `duration`       INT             DEFAULT 0             NOT NULL COMMENT '时长(分钟)',
    `student_count`  INT             DEFAULT 1             NOT NULL COMMENT '学员数量',
    `unit_price`     DECIMAL(10, 2)                        NOT NULL COMMENT '单价',
    `total_amount`   DECIMAL(10, 2)                        NOT NULL COMMENT '总金额',
    `actual_amount`  DECIMAL(10, 2)                        NOT NULL COMMENT '实付金额',
    `payment_method` VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '支付方式: wechat, alipay, cash, package',
    `package_id`     BIGINT          DEFAULT 0             NOT NULL COMMENT '使用的课时包ID',
    `order_source`   VARCHAR(50)     DEFAULT 'manual'      NOT NULL COMMENT '订单来源: ai, manual, wechat',
    `order_status`   TINYINT                               NOT NULL DEFAULT 0 COMMENT '订单状态: 0-待支付 1-已支付 2-已确认 3-已完成 4-已取消 5-已退款',
    `payment_status` TINYINT                               NOT NULL DEFAULT 0 COMMENT '支付状态: 0-未支付 1-已支付 2-已退款',
    `class_status`   TINYINT         DEFAULT 0             NOT NULL COMMENT '上课状态: 0-未开始 1-进行中 2-已完成 3-已取消',
    `remark`         VARCHAR(500)    DEFAULT ''            NOT NULL COMMENT '备注',
    `create_by`      VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`      VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`    DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`       INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`      INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX            `idx_order_no` (`order_no`),
    INDEX            `idx_club_id` (`club_id`),
    INDEX            `idx_customer_id` (`customer_id`),
    INDEX            `idx_coach_id` (`coach_id`),
    INDEX            `idx_order_date` (`order_date`),
    INDEX            `idx_class_date` (`class_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单学员表 (小组课支持多学员)
CREATE TABLE `m_order_student`
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `order_id`     BIGINT                                NOT NULL COMMENT '订单ID',
    `student_type` TINYINT                               NOT NULL COMMENT '学员类型: 1-主账号 2-家庭成员',
    `student_id`   BIGINT          DEFAULT 0             NOT NULL COMMENT '学员ID',
    `student_name` VARCHAR(50)                           NOT NULL COMMENT '学员姓名',
    `age`          INT             DEFAULT 0             NOT NULL COMMENT '年龄',
    `level`        VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '水平等级',
    `create_by`    VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`  DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`    VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`  DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`     INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`    INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX          `idx_order_id` (`order_id`),
    INDEX          `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单学员表';

-- 订单评价表
CREATE TABLE `m_order_evaluation`
(
    `id`                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `order_id`          BIGINT                                NOT NULL COMMENT '订单ID',
    `customer_id`       BIGINT                                NOT NULL COMMENT '客户ID',
    `coach_score`       INT                                   NOT NULL COMMENT '教练评分(1-5)',
    `course_score`      INT                                   NOT NULL COMMENT '课程评分(1-5)',
    `service_score`     INT                                   NOT NULL COMMENT '服务评分(1-5)',
    `overall_score`     DECIMAL(3, 2)   DEFAULT 0.00          NOT NULL COMMENT '综合评分',
    `content`           TEXT                                  NOT NULL COMMENT '评价内容',
    `img_url`           TEXT                                  NOT NULL COMMENT '图片地址JSON格式',
    `is_anonymous`      TINYINT         DEFAULT 0             NOT NULL COMMENT '是否匿名: 1-是 0-否',
    `club_visible_only` TINYINT         DEFAULT 1             NOT NULL COMMENT '仅俱乐部可见: 1-是 0-否',
    `create_by`         VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`       DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`         VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`       DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`          INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`         INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_order_customer` (`order_id`, `customer_id`),
    INDEX               `idx_order_id` (`order_id`),
    INDEX               `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';
```

### 4.7 用户课时包管理

```sql
-- 用户课时包表（统一使用user_id）
CREATE TABLE `m_user_package`
(
    `id`                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id`            BIGINT                                NOT NULL COMMENT '用户ID（统一使用）',
    `package_order_id`   BIGINT                                NOT NULL COMMENT '购买订单ID',
    `package_id`         BIGINT                                NOT NULL COMMENT '课时包商品ID',
    `package_name`       VARCHAR(100)                          NOT NULL COMMENT '课时包名称',
    `total_lessons`      INT                                   NOT NULL COMMENT '总课时数',
    `used_lessons`       INT         DEFAULT 0                 NOT NULL COMMENT '已用课时数',
    `remaining_lessons`  INT                                   NOT NULL COMMENT '剩余课时数',
    `purchase_date`      DATETIME                              NOT NULL COMMENT '购买日期时间',
    `expire_date`        DATETIME    DEFAULT '2099-12-31 23:59:59' NOT NULL COMMENT '到期日期时间',
    `applicable_courses` TEXT                                  NOT NULL COMMENT '适用课程ID列表JSON格式',
    `create_by`          VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time`        DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`          VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time`        DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`           INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`          INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX                `idx_user_id` (`user_id`),
    INDEX                `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户课时包表';

-- 课时包使用记录表
CREATE TABLE `m_package_usage_log`
(
    `id`                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_package_id`   BIGINT                                NOT NULL COMMENT '用户课时包ID',
    `order_id`          BIGINT                                NOT NULL COMMENT '使用的订单ID',
    `used_lessons`      INT                                   NOT NULL COMMENT '使用课时数',
    `remaining_lessons` INT                                   NOT NULL COMMENT '剩余课时数',
    `usage_date`        DATETIME                              NOT NULL COMMENT '使用日期时间',
    `create_by`         VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '创建人',
    `create_time`       DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`         VARCHAR(50) DEFAULT ''                NOT NULL COMMENT '更新人',
    `update_time`       DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`          INT         DEFAULT 1                 NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`         INT         DEFAULT 0                 NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX               `idx_user_package_id` (`user_package_id`),
    INDEX               `idx_order_id` (`order_id`),
    INDEX               `idx_usage_date` (`usage_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课时包使用记录表';
```

### 4.8 马匹管理模块

```sql
-- 马匹表
create table m_horse
(
    horse_id             bigint auto_increment comment '马匹ID'
        primary key,
    club_id              bigint                                     not null comment '俱乐部ID',
    horse_name           varchar(100)                               not null comment '马名',
    horse_code           varchar(50)  default ''                    not null comment '马匹编号',
    breed                varchar(50)  default ''                    not null comment '品种',
    gender               tinyint      default 1                     not null comment '性别: 1-公 2-母 3-骟',
    color                varchar(50)  default ''                    not null comment '毛色',
    birth_date           datetime     default '1970-01-01 00:00:00' not null comment '出生日期时间',
    chip_no              varchar(100) default ''                    not null comment '芯片号',
    passport_no          varchar(100) default ''                    not null comment '护照号',
    pedigree_cert_url    varchar(500) default ''                    not null comment '血统证书图片地址',
    horse_type           tinyint                                    not null comment '类型: 1-俱乐部马 2-马主马 3-教练马',
    owner_id             bigint       default 0                     not null comment '马主ID(马主马)',
    responsible_coach_id bigint       default 0                     not null comment '责任教练ID',
    responsible_groom_id bigint       default 0                     not null comment '责任马工ID',
    boarding_start_date  datetime     default '1970-01-01 00:00:00' not null comment '寄养开始日期时间',
    boarding_end_date    datetime     default '1970-01-01 00:00:00' not null comment '寄养结束日期时间',
    health_status        tinyint      default 1                     not null comment '健康状态: 1-健康 2-观察 3-治疗',
    work_status          tinyint      default 1                     not null comment '工作状态: 1-可用 2-休息 3-治疗',
    height               int          default 0                     not null comment '身高(cm)',
    weight               int          default 0                     not null comment '体重(kg)',
    horse_data           text                                       not null comment '马匹扩展数据JSON格式',
    create_by            varchar(50)  default ''                    not null comment '创建人',
    create_time          datetime     default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by            varchar(50)  default ''                    not null comment '更新人',
    update_time          datetime     default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid             int          default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete            int          default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    constraint chip_no
        unique (chip_no)
) comment '马匹表';

create index idx_chip_no
    on m_horse (chip_no);

create index idx_club_id
    on m_horse (club_id);

create index idx_horse_code
    on m_horse (horse_code);

create index idx_horse_type
    on m_horse (horse_type);

create index idx_owner_id
    on m_horse (owner_id);

-- 马匹健康计划表
create table m_horse_health_plan
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    horse_id      bigint                                    not null comment '马匹ID',
    plan_type     varchar(50)                               not null comment '计划类型: shoeing-钉蹄, deworming-驱虫, dental-搓牙, vaccine-疫苗, medication-用药',
    cycle_days    int         default 0                     not null comment '周期天数',
    last_date     datetime    default '1970-01-01 00:00:00' not null comment '上次执行日期时间',
    next_date     datetime                                  not null comment '下次执行日期时间',
    reminder_days int         default 7                     not null comment '提前提醒天数',
    plan_config   text                                      not null comment '计划配置JSON格式',
    create_by     varchar(50) default ''                    not null comment '创建人',
    create_time   datetime    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by     varchar(50) default ''                    not null comment '更新人',
    update_time   datetime    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid      int         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete     int         default 0                     not null comment '是否删除;1=已删除;0=未删除;'
) comment '马匹健康计划表' charset = utf8mb4;

create index idx_horse_id
    on m_horse_health_plan (horse_id);

create index idx_next_date
    on m_horse_health_plan (next_date);

create index idx_plan_type
    on m_horse_health_plan (plan_type);

-- 马匹健康记录表
create table m_horse_health_record
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    horse_id    bigint                                    not null comment '马匹ID',
    plan_id     bigint      default 0                     not null comment '关联计划ID',
    record_date datetime                                  not null comment '记录日期时间',
    executor_id bigint      default 0                     not null comment '执行人ID',
    content     text                                      not null comment '记录内容',
    img_url     text                                      not null comment '图片地址JSON格式',
    next_date   datetime    default '1970-01-01 00:00:00' not null comment '下次执行日期时间',
    record_data text                                      not null comment '记录扩展数据JSON格式',
    create_by   varchar(50) default ''                    not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by   varchar(50) default ''                    not null comment '更新人',
    update_time datetime    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid    int         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete   int         default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    plan_type   varchar(50) default ''                    not null comment '计划类型'
) comment '马匹健康记录表' charset = utf8mb4;

create index idx_horse_id
    on m_horse_health_record (horse_id);

create index idx_plan_id
    on m_horse_health_record (plan_id);

create index idx_record_date
    on m_horse_health_record (record_date);
```

### 4.9 支付财务模块

```sql
-- 支付记录表
CREATE TABLE `m_payment`
(
    `payment_id`      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付ID',
    `payment_no`      VARCHAR(50)                              NOT NULL UNIQUE COMMENT '支付单号',
    `order_id`        BIGINT                                   NOT NULL COMMENT '订单ID',
    `order_no`        VARCHAR(50)                              NOT NULL COMMENT '订单号',
    `club_id`         BIGINT                                   NOT NULL COMMENT '俱乐部ID',
    `customer_id`     BIGINT                                   NOT NULL COMMENT '客户ID',
    `payment_amount`  DECIMAL(10, 2)                           NOT NULL COMMENT '支付金额',
    `payment_method`  VARCHAR(50)                              NOT NULL COMMENT '支付方式: wechat, alipay, cash, package',
    `payment_channel` VARCHAR(50)     DEFAULT ''               NOT NULL COMMENT '支付渠道',
    `transaction_id`  VARCHAR(100)    DEFAULT ''               NOT NULL COMMENT '第三方交易号',
    `payment_status`  TINYINT                                  NOT NULL DEFAULT 0 COMMENT '支付状态: 0-待支付 1-支付成功 2-支付失败 3-已退款',
    `payment_time`    DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '支付时间',
    `refund_amount`   DECIMAL(10, 2)  DEFAULT 0.00             NOT NULL COMMENT '退款金额',
    `refund_time`     DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '退款时间',
    `refund_reason`   VARCHAR(200)    DEFAULT ''               NOT NULL COMMENT '退款原因',
    `payment_data`    TEXT                                     NOT NULL COMMENT '支付扩展数据JSON格式',
    `create_by`       VARCHAR(50)     DEFAULT ''               NOT NULL COMMENT '创建人',
    `create_time`     DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`       VARCHAR(50)     DEFAULT ''               NOT NULL COMMENT '更新人',
    `update_time`     DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`        INT             DEFAULT 1                NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`       INT             DEFAULT 0                NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX             `idx_payment_no` (`payment_no`),
    INDEX             `idx_order_id` (`order_id`),
    INDEX             `idx_order_no` (`order_no`),
    INDEX             `idx_club_id` (`club_id`),
    INDEX             `idx_customer_id` (`customer_id`),
    INDEX             `idx_payment_status` (`payment_status`),
    INDEX             `idx_payment_time` (`payment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 财务统计表
CREATE TABLE `m_financial_summary`
(
    `id`                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `club_id`             BIGINT                                   NOT NULL COMMENT '俱乐部ID',
    `summary_date`        DATETIME                                 NOT NULL COMMENT '统计日期时间',
    `summary_type`        VARCHAR(50)                              NOT NULL COMMENT '统计类型: daily-日, monthly-月, yearly-年',
    `total_income`        DECIMAL(12, 2) DEFAULT 0.00             NOT NULL COMMENT '总收入',
    `course_income`       DECIMAL(12, 2) DEFAULT 0.00             NOT NULL COMMENT '课程收入',
    `package_income`      DECIMAL(12, 2) DEFAULT 0.00             NOT NULL COMMENT '课时包收入',
    `activity_income`     DECIMAL(12, 2) DEFAULT 0.00             NOT NULL COMMENT '活动收入',
    `total_refund`        DECIMAL(12, 2) DEFAULT 0.00             NOT NULL COMMENT '总退款',
    `total_coach_fee`     DECIMAL(12, 2) DEFAULT 0.00             NOT NULL COMMENT '总教练费',
    `order_count`         INT            DEFAULT 0                NOT NULL COMMENT '订单数量',
    `new_member_count`    INT            DEFAULT 0                NOT NULL COMMENT '新会员数量',
    `active_member_count` INT            DEFAULT 0                NOT NULL COMMENT '活跃会员数量',
    `summary_data`        TEXT                                     NOT NULL COMMENT '统计扩展数据JSON格式',
    `create_by`           VARCHAR(50)    DEFAULT ''               NOT NULL COMMENT '创建人',
    `create_time`         DATETIME       DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`           VARCHAR(50)    DEFAULT ''               NOT NULL COMMENT '更新人',
    `update_time`         DATETIME       DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`            INT            DEFAULT 1                NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`           INT            DEFAULT 0                NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_club_date_type` (`club_id`, `summary_date`, `summary_type`),
    INDEX                 `idx_club_id` (`club_id`),
    INDEX                 `idx_summary_date` (`summary_date`),
    INDEX                 `idx_summary_type` (`summary_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务统计表';
```

## 五、核心业务流程

### 5.1 添加未注册家庭成员流程

```java
public Long addUnregisteredFamilyMember(AddFamilyMemberRequest request) {
	// 1. 创建未激活用户记录
	User user = new User();
	user.setLoginName(generateTempLoginName()); // temp_uuid格式
	user.setActualName(request.getMemberName());
	user.setPhone(request.getPhone());
	user.setGender(request.getGender());
	user.setRegistrationStatus(0); // 未激活
	user.setDisabledFlag(1); // 禁用登录
	user.setDeletedFlag(0);
	user.setDepartmentId(1L); // 默认部门
	userService.save(user);

	// 2. 创建用户扩展信息
	UserProfile profile = new UserProfile();
	profile.setUserId(user.getEmployeeId());
	profile.setUserType(2); // 会员
	profile.setClubId(request.getClubId());
	profile.setCreatedByGuardian(1); // 由监护人创建
	profile.setProfileData(buildProfileData(request));
	userProfileService.save(profile);

	// 3. 建立家庭关系
	FamilyMemberRelation relation = new FamilyMemberRelation();
	relation.setFamilyGroupId(request.getFamilyGroupId());
	relation.setUserId(user.getEmployeeId());
	relation.setMemberType(2); // 孩子
	relation.setRelationship("child");
	relation.setCanBook(1);
	relation.setCanPay(0);
	familyMemberRelationService.save(relation);

	// 4. 保存扩展信息
	if (hasExtraInfo(request)) {
		FamilyMemberExtra extra = new FamilyMemberExtra();
		extra.setUserId(user.getEmployeeId());
		extra.setGuardianName(request.getGuardianName());
		extra.setGuardianPhone(request.getGuardianPhone());
		extra.setDefaultCoachId(request.getDefaultCoachId());
		familyMemberExtraService.save(extra);
	}

	return user.getEmployeeId();
}
```

### 5.2 用户注册激活流程

```java
public void registerAndActivateUser(UserRegistrationRequest request) {
	// 1. 检查是否有匹配的未激活用户
	User unactivatedUser = userService.findUnactivatedUserByPhone(request.getPhone());

	if (unactivatedUser != null) {
		// 激活现有用户
		activateExistingUser(unactivatedUser, request);
	} else {
		// 创建新用户
		createNewRegisteredUser(request);
	}
}

private void activateExistingUser(User user, UserRegistrationRequest request) {
	// 更新用户基本信息
	user.setLoginName(request.getLoginName());
	user.setLoginPwd(passwordEncoder.encode(request.getPassword()));
	user.setEmail(request.getEmail());
	user.setRegistrationStatus(1); // 已注册
	user.setDisabledFlag(0); // 启用登录
	userService.updateById(user);

	// 更新扩展信息
	UserProfile profile = userProfileService.getByUserId(user.getEmployeeId());
	profile.setCreatedByGuardian(0); // 已自主注册
	// 合并profile_data
	String newProfileData = mergeProfileData(profile.getProfileData(), request.getExtendedInfo());
	profile.setProfileData(newProfileData);
	userProfileService.updateById(profile);

	// 清理临时信息
	familyMemberExtraService.deleteByUserId(user.getEmployeeId());
}
```

### 5.3 统一的业务操作

```java
// 课程预约 - 统一处理注册和未注册用户
public void bookCourse(Long userId, BookCourseRequest request) {
	// 不需要区分用户是否注册，统一使用user_id
	Order order = new Order();
	order.setCustomerId(userId); // 统一使用user_id
	order.setClubId(request.getClubId());
	order.setGoodsId(request.getCourseId());
	order.setTimeSlotId(request.getTimeSlotId());
	// ... 其他业务逻辑

	orderService.save(order);
}

// 查询家庭成员 - 统一查询接口
public List<FamilyMemberVO> getFamilyMembers(Long familyGroupId) {
	List<FamilyMemberRelation> relations = familyMemberRelationService.list(
			new QueryWrapper<FamilyMemberRelation>()
					.eq("family_group_id", familyGroupId)
					.eq("is_delete", 0)
	);

	List<FamilyMemberVO> result = new ArrayList<>();
	for (FamilyMemberRelation relation : relations) {
		User user = userService.getById(relation.getUserId());
		UserProfile profile = userProfileService.getByUserId(relation.getUserId());

		FamilyMemberVO vo = new FamilyMemberVO();
		vo.setUserId(user.getEmployeeId());
		vo.setMemberName(user.getActualName());
		vo.setPhone(user.getPhone());
		vo.setGender(user.getGender());
		vo.setMemberType(relation.getMemberType());
		vo.setRelationship(relation.getRelationship());
		vo.setRegistrationStatus(user.getRegistrationStatus());
		vo.setCanBook(relation.getCanBook());
		vo.setCanPay(relation.getCanPay());

		result.add(vo);
	}

	return result;
}
```

## 六、关键技术要点

### 6.1 优化方案优势

1. **架构统一**：所有用户统一管理，避免双轨制设计
2. **逻辑简化**：无需复杂的数据迁移和状态转换逻辑
3. **性能优化**：避免多表JOIN和数据迁移开销
4. **扩展性强**：基于统一用户体系，便于功能扩展
5. **维护便利**：减少代码复杂度，降低维护成本

### 6.2 数据隔离策略

- **多租户设计**: 所有业务表都包含club_id字段
- **权限控制**: 基于用户所属俱乐部进行数据过滤
- **软删除**: 使用is_delete字段而非物理删除

### 6.3 扩展性设计

- **JSON字段**: 使用JSON存储不同用户类型的扩展信息
- **商品化课程**: 课程作为商品管理，支持灵活配置
- **统一订单**: 课程、活动、课时包统一使用订单表

### 6.4 关键技术点

1. **临时用户名生成**：`temp_` + UUID前8位，确保唯一性
2. **状态管理**：通过`registration_status`区分用户激活状态
3. **权限控制**：基于统一的RBAC体系和家庭关系权限
4. **数据清理**：定期清理长期未激活的临时用户
5. **业务兼容**：保持与现有Smart Admin v3架构的兼容性

## 七、业务特色

- **AI约课**: 智能识别用户习惯，自动匹配教练和课程
- **家庭管理**: 支持多子女不同教练和课程级别
- **马匹健康**: 完整的健康档案和自动提醒机制
- **财务结算**: 自动化的教练费结算和报表统计
- **统一用户体系**: 避免数据迁移，简化业务逻辑

这个优化方案完全避免了数据迁移的复杂性，同时保持了业务逻辑的清晰和系统架构的一致性。通过预创建用户记录的方式，实现了真正的统一用户管理，大幅降低了系统的复杂度和维护成本。
