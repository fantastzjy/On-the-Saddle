/*
 * 会员管理模块字典数据配置SQL
 *
 * @Author: Claude Code
 * @Date: 2025-01-11
 * @Copyright: 马术俱乐部管理系统
 */

SET NAMES utf8mb4;

-- ----------------------------
-- 会员管理相关字典配置
-- ----------------------------

-- 1. 性别字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('性别', 'GENDER', '会员性别选项字典', 0, 0, 1, NOW(), NOW());

SET @gender_dict_id = LAST_INSERT_ID();

-- 性别字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@gender_dict_id, '0', '未知', '性别未知', 1, 0, 0, 1, NOW(), NOW()),
(@gender_dict_id, '1', '男', '男性', 2, 0, 0, 1, NOW(), NOW()),
(@gender_dict_id, '2', '女', '女性', 3, 0, 0, 1, NOW(), NOW());

-- 2. 注册状态字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('会员注册状态', 'MEMBER_REGISTRATION_STATUS', '会员注册状态字典', 0, 0, 1, NOW(), NOW());

SET @registration_status_dict_id = LAST_INSERT_ID();

-- 注册状态字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@registration_status_dict_id, '0', '未激活', '未激活状态，无法登录', 1, 0, 0, 1, NOW(), NOW()),
(@registration_status_dict_id, '1', '已注册', '已注册激活，可正常登录', 2, 0, 0, 1, NOW(), NOW());

-- 3. 会籍状态字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('会籍状态', 'MEMBERSHIP_STATUS', '会员会籍类型状态字典', 0, 0, 1, NOW(), NOW());

SET @membership_status_dict_id = LAST_INSERT_ID();

-- 会籍状态字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@membership_status_dict_id, '0', '普通会员', '普通会员，无特殊权益', 1, 0, 0, 1, NOW(), NOW()),
(@membership_status_dict_id, '1', '会籍会员', '会籍会员，享受特殊权益', 2, 0, 0, 1, NOW(), NOW());

-- 4. 创建方式字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('会员创建方式', 'MEMBER_CREATED_BY', '会员创建方式字典', 0, 0, 1, NOW(), NOW());

SET @created_by_dict_id = LAST_INSERT_ID();

-- 创建方式字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@created_by_dict_id, '0', '自主注册', '用户自主注册的会员', 1, 0, 0, 1, NOW(), NOW()),
(@created_by_dict_id, '1', '监护人创建', '由监护人代为创建的会员', 2, 0, 0, 1, NOW(), NOW());

-- 5. 启用状态字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('启用状态', 'DISABLED_FLAG', '通用启用禁用状态字典', 0, 0, 1, NOW(), NOW());

SET @disabled_flag_dict_id = LAST_INSERT_ID();

-- 启用状态字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@disabled_flag_dict_id, '0', '启用', '账号正常启用状态', 1, 0, 0, 1, NOW(), NOW()),
(@disabled_flag_dict_id, '1', '禁用', '账号被禁用状态', 2, 0, 0, 1, NOW(), NOW());

-- 6. 支付方式字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('支付方式', 'PAYMENT_METHOD', '会籍续费支付方式字典', 0, 0, 1, NOW(), NOW());

SET @payment_method_dict_id = LAST_INSERT_ID();

-- 支付方式字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@payment_method_dict_id, 'wechat', '微信支付', '微信支付', 2, 0, 0, 1, NOW(), NOW());

-- 7. 家庭成员关系字典
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('家庭成员关系', 'FAMILY_RELATION', '与监护人的关系字典', 0, 0, 1, NOW(), NOW());

SET @family_relation_dict_id = LAST_INSERT_ID();

-- 家庭成员关系字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@family_relation_dict_id, '父亲', '父亲', '父子/父女关系', 1, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '母亲', '母亲', '母子/母女关系', 2, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '爷爷', '爷爷', '祖孙关系', 3, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '奶奶', '奶奶', '祖孙关系', 4, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '外公', '外公', '外祖孙关系', 5, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '外婆', '外婆', '外祖孙关系', 6, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '叔叔', '叔叔', '叔侄关系', 7, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '阿姨', '阿姨', '阿姨侄子/侄女关系', 8, 0, 0, 1, NOW(), NOW()),
(@family_relation_dict_id, '其他', '其他', '其他亲属关系', 9, 0, 0, 1, NOW(), NOW());

-- 8. 课程级别字典（用于家庭成员默认课程级别）
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('课程级别', 'COURSE_LEVEL', '马术课程级别字典', 0, 0, 1, NOW(), NOW());

SET @course_level_dict_id = LAST_INSERT_ID();

-- 课程级别字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@course_level_dict_id, '初级', '初级', '马术初级课程', 1, 0, 0, 1, NOW(), NOW()),
(@course_level_dict_id, '中级', '中级', '马术中级课程', 2, 0, 0, 1, NOW(), NOW()),
(@course_level_dict_id, '高级', '高级', '马术高级课程', 3, 0, 0, 1, NOW(), NOW()),
(@course_level_dict_id, '专业', '专业', '马术专业级课程', 4, 0, 0, 1, NOW(), NOW()),
(@course_level_dict_id, '竞技', '竞技', '马术竞技级课程', 5, 0, 0, 1, NOW(), NOW());

-- 9. 订单状态字典（预留，关联订单系统）
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('订单状态', 'ORDER_STATUS', '订单状态字典', 0, 0, 1, NOW(), NOW());

SET @order_status_dict_id = LAST_INSERT_ID();

-- 订单状态字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@order_status_dict_id, '0', '待支付', '订单已创建，等待支付', 1, 0, 0, 1, NOW(), NOW()),
(@order_status_dict_id, '1', '已支付', '订单已支付，待确认', 2, 0, 0, 1, NOW(), NOW()),
(@order_status_dict_id, '2', '已确认', '订单已确认，待完成', 3, 0, 0, 1, NOW(), NOW()),
(@order_status_dict_id, '3', '已完成', '订单已完成', 4, 0, 0, 1, NOW(), NOW()),
(@order_status_dict_id, '4', '已取消', '订单已取消', 5, 0, 0, 1, NOW(), NOW()),
(@order_status_dict_id, '5', '已退款', '订单已退款', 6, 0, 0, 1, NOW(), NOW());

-- 10. 会员编号前缀字典（系统配置用）
INSERT INTO `t_dict` (`dict_name`, `dict_code`, `remark`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`)
VALUES ('会员编号配置', 'MEMBER_NO_CONFIG', '会员编号生成配置', 0, 0, 1, NOW(), NOW());

SET @member_no_config_dict_id = LAST_INSERT_ID();

-- 会员编号配置字典数据
INSERT INTO `t_dict_data` (`dict_id`, `value`, `label`, `remark`, `sort`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_time`) VALUES
(@member_no_config_dict_id, 'M', '会员编号前缀', '会员编号统一前缀为M', 1, 0, 0, 1, NOW(), NOW()),
(@member_no_config_dict_id, '8', '编号长度', '会员编号总长度为8位', 2, 0, 0, 1, NOW(), NOW()),
(@member_no_config_dict_id, '000001', '起始编号', '会员编号起始值', 3, 0, 0, 1, NOW(), NOW());

-- ----------------------------
-- 输出配置结果
-- ----------------------------
SELECT
    '✅ 会员管理字典数据配置完成' AS result,
    '共添加字典分类' AS dict_count,
    COUNT(DISTINCT dict_id) AS count_value,
    '共添加字典数据' AS dict_data_count,
    (SELECT COUNT(*) FROM t_dict_data WHERE dict_id IN (
        @gender_dict_id, @registration_status_dict_id, @membership_status_dict_id,
        @created_by_dict_id, @disabled_flag_dict_id, @payment_method_dict_id,
        @family_relation_dict_id, @course_level_dict_id, @order_status_dict_id, @member_no_config_dict_id
    )) AS data_count_value
FROM t_dict
WHERE dict_id IN (
    @gender_dict_id, @registration_status_dict_id, @membership_status_dict_id,
    @created_by_dict_id, @disabled_flag_dict_id, @payment_method_dict_id,
    @family_relation_dict_id, @course_level_dict_id, @order_status_dict_id, @member_no_config_dict_id
);

-- ----------------------------
-- 查看新添加的字典数据（验证用）
-- ----------------------------
SELECT
    d.dict_name,
    d.dict_code,
    dd.label,
    dd.value,
    dd.sort,
    dd.remark
FROM t_dict d
JOIN t_dict_data dd ON d.dict_id = dd.dict_id
WHERE d.dict_id IN (
    @gender_dict_id, @registration_status_dict_id, @membership_status_dict_id,
    @created_by_dict_id, @disabled_flag_dict_id, @payment_method_dict_id,
    @family_relation_dict_id, @course_level_dict_id, @order_status_dict_id, @member_no_config_dict_id
)
ORDER BY d.dict_code, dd.sort;
