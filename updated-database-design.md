# 马术俱乐部SaaS系统数据库设计优化版（用户独立设计）

## 一、核心设计原则

1. **用户类型简化**：只分为员工和会员两种类型
2. **用户表独立**：从员工表中完全独立，避免概念混淆
3. **家庭关系简化**：只需要绑定关系，无复杂权限控制
4. **统一用户体系**：所有用户（注册/未注册）都在用户表中有记录
5. **避免数据迁移**：注册时只需激活用户，无需数据迁移

## 二、统一审计字段标准

所有业务表统一使用以下审计字段：

```sql
-- 标准审计字段
create_by               varchar(50)     default ''                not null comment '创建人',
create_time             datetime        default CURRENT_TIMESTAMP not null comment '创建时间',
update_by               varchar(50)     default ''                not null comment '更新人',
update_time             datetime        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
is_valid                int             default 1                 not null comment '是否有效;1=有效;0=无效;',
is_delete               int             default 0                 not null comment '是否删除;1=已删除;0=未删除;'
```

## 三、完整数据库表设计

### 3.1 用户管理模块（全新设计）

```sql
-- 用户主表（独立设计，不依赖员工表）
CREATE TABLE `m_user`
(
    `user_id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `member_id`           BIGINT          DEFAULT 0             NOT NULL COMMENT '会员ID',
    `union_id`            VARCHAR(100)    DEFAULT ''            NOT NULL COMMENT '微信unionId',
    `open_id`             VARCHAR(100)    DEFAULT ''            NOT NULL COMMENT '微信openId',
    `member_no`             VARCHAR(50)                           NOT NULL UNIQUE COMMENT '用户编号',
    `actual_name`         VARCHAR(50)                           NOT NULL COMMENT '真实姓名',
    `id_card_no`          VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '身份证号',
    `phone`               VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '手机号',
    `email`               VARCHAR(100)    DEFAULT ''            NOT NULL COMMENT '邮箱',
    `avatar_url`          VARCHAR(500)    DEFAULT ''            NOT NULL COMMENT '头像地址',
    `gender`              TINYINT         DEFAULT 0             NOT NULL COMMENT '性别: 0-未知 1-男 2-女',
    `birth_date`          DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '出生日期',
    `club_id`             BIGINT          DEFAULT 0             NOT NULL COMMENT '所属俱乐部ID(员工必填)',
    `is_membership`       TINYINT         DEFAULT 0             NOT NULL COMMENT '是否为会籍会员: 1-是 0-否',
    `membership_expire_date` DATETIME     DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '会籍有效期',
    `rider_cert_no`       VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '骑手证号',
    `id_card_front_url`   VARCHAR(500)    DEFAULT ''            NOT NULL COMMENT '身份证正面图片地址',
    `id_card_back_url`    VARCHAR(500)    DEFAULT ''            NOT NULL COMMENT '身份证反面图片地址',
    `default_coach_id`     BIGINT          DEFAULT 0             NOT NULL COMMENT '默认教练ID',
    `default_course_level` VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '默认课程级别',
    `registration_status` TINYINT         DEFAULT 0             NOT NULL COMMENT '注册状态: 0-未激活 1-已注册',
    `created_by_guardian` TINYINT         DEFAULT 0             NOT NULL COMMENT '是否由监护人创建: 1-是 0-否',
    `disabled_flag`       TINYINT         DEFAULT 0             NOT NULL COMMENT '是否禁用: 0-启用 1-禁用',
    `profile_data`        TEXT                                  NOT NULL COMMENT '扩展数据JSON格式',
    `last_login_time`     DATETIME        DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '最后登录时间',
    `create_by`           VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`         DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`           VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`         DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`            INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`           INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    INDEX                 `idx_member_id` (`member_id`),
    INDEX                 `idx_union_id` (`union_id`),
    INDEX                 `idx_open_id` (`open_id`),
    INDEX                 `idx_user_no` (`user_no`),
    INDEX                 `idx_login_name` (`login_name`),
    INDEX                 `idx_phone` (`phone`),
    INDEX                 `idx_user_type` (`user_type`),
    INDEX                 `idx_club_id` (`club_id`),
    INDEX                 `idx_is_membership` (`is_membership`),
    INDEX                 `idx_membership_expire_date` (`membership_expire_date`),
    INDEX                 `idx_id_card_no` (`id_card_no`),
    INDEX                 `idx_rider_cert_no` (`rider_cert_no`),
    INDEX                 `idx_registration_status` (`registration_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表';

```

### 3.2 家庭管理模块（简化设计）

```sql
-- 家庭组表
CREATE TABLE `m_family_group`
(
    `family_group_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '家庭组ID',
    `family_name`     VARCHAR(50)                           NOT NULL COMMENT '家庭名称',
    `club_id`         BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `main_contact_id` BIGINT          DEFAULT 0             NOT NULL COMMENT '主要联系人用户ID',
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

-- 家庭成员关系表（简化设计，仅绑定关系）
CREATE TABLE `m_family_member_relation`
(
    `id`              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `family_group_id` BIGINT                                NOT NULL COMMENT '家庭组ID',
    `user_id`         BIGINT                                NOT NULL COMMENT '用户ID',
    `is_guardian`     TINYINT         DEFAULT 0             NOT NULL COMMENT '是否监护人: 1-是 0-否',
    `join_date`       DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '加入家庭日期',
    `remark`          VARCHAR(200)    DEFAULT ''            NOT NULL COMMENT '备注',
    `create_by`       VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`     DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`       VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`     DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`        INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`       INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_family_user` (`family_group_id`, `user_id`),
    INDEX             `idx_family_group_id` (`family_group_id`),
    INDEX             `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭成员关系表';

-- 未注册成员扩展信息表（临时存储监护人信息等）
CREATE TABLE `m_family_member_extra`
(
    `id`                   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id`              BIGINT                                NOT NULL COMMENT '关联用户ID',
    `guardian_phone`       VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '监护人电话',
    `guardian_name`        VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '监护人姓名',
    `rider_cert_no`        VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '骑手证号',
    `default_coach_id`     BIGINT          DEFAULT 0             NOT NULL COMMENT '默认教练ID',
    `default_course_level` VARCHAR(20)     DEFAULT ''            NOT NULL COMMENT '默认课程级别',
    `extra_data`           TEXT                                  NOT NULL COMMENT '其他扩展信息JSON格式',
    `create_by`            VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '创建人',
    `create_time`          DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_by`            VARCHAR(50)     DEFAULT ''            NOT NULL COMMENT '更新人',
    `update_time`          DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`             INT             DEFAULT 1             NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`            INT             DEFAULT 0             NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX                  `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='未注册成员扩展信息表';
```

### 3.3 教练管理模块（更新用户ID关联）

```sql
-- 教练表（更新为关联用户表）
CREATE TABLE `m_coach`
(
    `coach_id`                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '教练ID',
    `club_id`                  BIGINT                                     NOT NULL COMMENT '俱乐部ID',
    `user_id`                  BIGINT                                     NOT NULL COMMENT '关联用户ID（m_user表）',
    `coach_no`                 VARCHAR(50)  DEFAULT ''                    NOT NULL COMMENT '教练编号',
    `avatar_url`               VARCHAR(500) DEFAULT ''                    NOT NULL COMMENT '头像照片地址',
    `entry_date`               DATETIME     DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '从业时间',
    `specialties`              VARCHAR(200) DEFAULT ''                    NOT NULL COMMENT '专长领域',
    `introduction`             TEXT                                       NOT NULL COMMENT '个人介绍',
    `rider_cert_no`            VARCHAR(100) DEFAULT ''                    NOT NULL COMMENT '骑手证号码',
    `rider_level_show_jumping` VARCHAR(20)  DEFAULT ''                    NOT NULL COMMENT '场地障碍等级',
    `rider_level_dressage`     VARCHAR(20)  DEFAULT ''                    NOT NULL COMMENT '盛装舞步等级',
    `rider_level_eventing`     VARCHAR(20)  DEFAULT ''                    NOT NULL COMMENT '三项赛等级',
    `rider_cert_img_url`       TEXT                                       NOT NULL COMMENT '骑手证书图片地址JSON格式',
    `coach_cert_no`            VARCHAR(100) DEFAULT ''                    NOT NULL COMMENT '星级教练证号码',
    `coach_level`              VARCHAR(20)  DEFAULT ''                    NOT NULL COMMENT '教练等级: 一星,二星,三星,四星,五星',
    `coach_cert_img_url`       TEXT                                       NOT NULL COMMENT '教练证书图片地址JSON格式',
    `sort_order`               INT          DEFAULT 0                     NOT NULL COMMENT '排序',
    `create_by`                VARCHAR(50)  DEFAULT ''                    NOT NULL COMMENT '创建人',
    `create_time`              DATETIME     DEFAULT CURRENT_TIMESTAMP     NOT NULL COMMENT '创建时间',
    `update_by`                VARCHAR(50)  DEFAULT ''                    NOT NULL COMMENT '更新人',
    `update_time`              DATETIME     DEFAULT CURRENT_TIMESTAMP     NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_valid`                 INT          DEFAULT 1                     NOT NULL COMMENT '是否有效;1=有效;0=无效;',
    `is_delete`                INT          DEFAULT 0                     NOT NULL COMMENT '是否删除;1=已删除;0=未删除;',
    UNIQUE KEY `uk_club_user` (`club_id`, `user_id`),
    INDEX                      `idx_club_id` (`club_id`),
    INDEX                      `idx_coach_no` (`coach_no`),
    INDEX                      `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练表';
```

### 3.5 订单管理模块（统一用户ID）

```sql
-- 订单表（统一使用m_user的user_id）
CREATE TABLE `m_order`
(
    `order_id`       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no`       VARCHAR(50)                           NOT NULL UNIQUE COMMENT '订单号',
    `club_id`        BIGINT                                NOT NULL COMMENT '俱乐部ID',
    `customer_id`    BIGINT                                NOT NULL COMMENT '客户ID（m_user.user_id）',
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

-- 用户课时包表（统一使用user_id）
CREATE TABLE `m_user_package`
(
    `id`                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id`            BIGINT                                NOT NULL COMMENT '用户ID（m_user.user_id）',
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
```

## 四、核心业务流程

### 4.1 添加未注册家庭成员流程

```java
public Long addUnregisteredFamilyMember(AddFamilyMemberRequest request) {
    // 1. 创建未激活用户记录
    User user = new User();
    user.setUserNo(generateUserNo()); // 生成用户编号
    user.setLoginName(generateTempLoginName()); // temp_uuid格式
    user.setActualName(request.getMemberName());
    user.setPhone(request.getPhone());
    user.setGender(request.getGender());
    user.setBirthDate(request.getBirthDate());
    user.setUserType(2); // 会员
    user.setClubId(request.getClubId());
    user.setRegistrationStatus(0); // 未激活
    user.setCreatedByGuardian(1); // 由监护人创建
    user.setDisabledFlag(1); // 禁用登录
    user.setProfileData(buildProfileData(request));
    userService.save(user);

    // 2. 建立家庭关系（简化设计）
    FamilyMemberRelation relation = new FamilyMemberRelation();
    relation.setFamilyGroupId(request.getFamilyGroupId());
    relation.setUserId(user.getUserId());
    relation.setIsGuardian(0); // 非监护人
    relation.setRemark(request.getRemark());
    familyMemberRelationService.save(relation);

    // 3. 保存扩展信息（临时信息）
    if (hasExtraInfo(request)) {
        FamilyMemberExtra extra = new FamilyMemberExtra();
        extra.setUserId(user.getUserId());
        extra.setGuardianName(request.getGuardianName());
        extra.setGuardianPhone(request.getGuardianPhone());
        extra.setDefaultCoachId(request.getDefaultCoachId());
        extra.setExtraData(buildExtraData(request));
        familyMemberExtraService.save(extra);
    }

    return user.getUserId();
}
```

### 4.2 用户注册激活流程

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
    user.setCreatedByGuardian(0); // 已自主注册
    user.setDisabledFlag(0); // 启用登录
    // 合并profile_data
    String newProfileData = mergeProfileData(user.getProfileData(), request.getExtendedInfo());
    user.setProfileData(newProfileData);
    userService.updateById(user);

    // 清理临时信息
    familyMemberExtraService.deleteByUserId(user.getUserId());
}
```

### 4.3 统一的业务操作

```java
// 查询家庭成员 - 简化后的统一查询接口
public List<FamilyMemberVO> getFamilyMembers(Long familyGroupId) {
    List<FamilyMemberRelation> relations = familyMemberRelationService.list(
        new QueryWrapper<FamilyMemberRelation>()
            .eq("family_group_id", familyGroupId)
            .eq("is_delete", 0)
    );

    List<FamilyMemberVO> result = new ArrayList<>();
    for (FamilyMemberRelation relation : relations) {
        User user = userService.getById(relation.getUserId());

        FamilyMemberVO vo = new FamilyMemberVO();
        vo.setUserId(user.getUserId());
        vo.setUserNo(user.getUserNo());
        vo.setMemberName(user.getActualName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setGender(user.getGender());
        vo.setBirthDate(user.getBirthDate());
        vo.setUserType(user.getUserType());
        vo.setRegistrationStatus(user.getRegistrationStatus());
        vo.setIsGuardian(relation.getIsGuardian());
        vo.setJoinDate(relation.getJoinDate());

        result.add(vo);
    }

    return result;
}

// 课程预约 - 统一处理所有用户
public void bookCourse(Long userId, BookCourseRequest request) {
    // 不需要区分用户类型，统一使用user_id
    Order order = new Order();
    order.setCustomerId(userId); // 统一使用user_id
    order.setClubId(request.getClubId());
    order.setGoodsId(request.getCourseId());
    order.setTimeSlotId(request.getTimeSlotId());
    // ... 其他业务逻辑

    orderService.save(order);
}
```

## 五、关键设计变更点

### 5.1 主要变更

1. **用户表独立**：
   - 从`t_employee`表完全独立出来
   - 用户类型简化为员工(1)和会员(2)两种
   - 统一的用户ID体系，避免概念混淆

2. **家庭关系简化**：
   - 移除复杂的权限控制字段
   - 移除详细的关系类型定义
   - 只保留必要的绑定关系和监护人标识

3. **教练管理独立**：
   - 教练信息通过`m_coach`表存储专业信息
   - 通过`user_id`关联用户表
   - 保持RBAC权限体系通过现有的Smart Admin框架管理

### 5.2 核心优势

1. **概念清晰**：用户统一管理，通过user_type区分员工和会员
2. **结构简化**：减少不必要的权限控制和关系类型复杂度
3. **扩展性强**：独立的用户表便于后续功能扩展
4. **维护便利**：清晰的表结构关系，降低维护成本
5. **性能优化**：避免复杂的多表联查和权限判断

### 5.3 技术要点

- **用户编号生成**：`USER` + 时间戳 + 序号，确保唯一性
- **临时登录名**：`temp_` + UUID前8位，便于识别未激活用户
- **JSON扩展字段**：使用`profile_data`和`extra_data`存储不同类型的扩展信息
- **软删除机制**：统一使用`is_delete`标识，便于数据恢复和审计
- **多租户隔离**：所有业务表都包含`club_id`字段实现数据隔离

这个优化设计完全独立了用户管理体系，简化了家庭关系管理，同时保持了系统的灵活性和扩展性。
