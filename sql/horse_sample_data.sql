-- 马匹管理模块模拟数据
-- Author: 1024创新实验室-主任：卓大
-- Date: 2024-01-08
-- 基于已有的俱乐部和教练数据生成马匹模拟数据

-- ============================================
-- 1. 马匹基础数据（两匹马）
-- ============================================

-- 第一匹马：俱乐部自有马匹（温血马）
INSERT INTO `m_horse` (
    `club_id`, `horse_name`, `horse_code`, `breed`, `gender`, `color`, `birth_date`, 
    `chip_no`, `passport_no`, `pedigree_cert_url`, `horse_type`, `owner_id`, 
    `responsible_coach_id`, `responsible_groom_id`, `boarding_start_date`, `boarding_end_date`,
    `health_status`, `work_status`, `horse_data`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `is_valid`, `is_delete`
) VALUES (
    1, -- club_id：示例马术俱乐部
    '黑骏马', -- horse_name
    'HORSE_001', -- horse_code
    '温血马', -- breed
    1, -- gender：1-公马
    '黑色', -- color
    '2018-05-15 10:30:00', -- birth_date
    'CHIP202401080001', -- chip_no
    'PASSPORT_H001', -- passport_no
    '/uploads/horse/pedigree_001.jpg', -- pedigree_cert_url
    1, -- horse_type：1-俱乐部马
    NULL, -- owner_id：俱乐部马无马主
    1, -- responsible_coach_id：张教练
    NULL, -- responsible_groom_id
    NULL, -- boarding_start_date：俱乐部马无寄养时间
    NULL, -- boarding_end_date
    1, -- health_status：1-健康
    1, -- work_status：1-可用
    '{"height": "165cm", "weight": "520kg", "training_level": "中级", "preferred_gait": "快步", "special_notes": "性格温顺，适合初中级学员"}', -- horse_data
    'admin', -- create_by
    '2024-01-08 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-08 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 第二匹马：马主寄养马匹（阿拉伯马）
INSERT INTO `m_horse` (
    `club_id`, `horse_name`, `horse_code`, `breed`, `gender`, `color`, `birth_date`, 
    `chip_no`, `passport_no`, `pedigree_cert_url`, `horse_type`, `owner_id`, 
    `responsible_coach_id`, `responsible_groom_id`, `boarding_start_date`, `boarding_end_date`,
    `health_status`, `work_status`, `horse_data`, `create_by`, `create_time`, 
    `update_by`, `update_time`, `is_valid`, `is_delete`
) VALUES (
    1, -- club_id：示例马术俱乐部
    '沙漠之星', -- horse_name
    'HORSE_002', -- horse_code
    '阿拉伯马', -- breed
    2, -- gender：2-母马
    '栗色', -- color
    '2017-03-22 14:15:00', -- birth_date
    'CHIP202401080002', -- chip_no
    'PASSPORT_H002', -- passport_no
    '/uploads/horse/pedigree_002.jpg', -- pedigree_cert_url
    2, -- horse_type：2-马主马
    1, -- owner_id：假设马主ID为1（需要在用户表中存在）
    2, -- responsible_coach_id：李教练
    NULL, -- responsible_groom_id
    '2023-06-01 09:00:00', -- boarding_start_date：寄养开始时间
    '2024-12-31 18:00:00', -- boarding_end_date：寄养结束时间
    1, -- health_status：1-健康
    1, -- work_status：1-可用
    '{"height": "155cm", "weight": "450kg", "training_level": "高级", "preferred_gait": "跑步", "special_notes": "血统优良，适合竞技训练，性格敏感需要有经验的骑手"}', -- horse_data
    'admin', -- create_by
    '2024-01-08 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-08 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- ============================================
-- 2. 健康计划数据
-- ============================================

-- 黑骏马的健康计划
-- 钉蹄计划
INSERT INTO `m_horse_health_plan` (
    `horse_id`, `plan_type`, `plan_name`, `cycle_days`, `last_date`, `next_date`, 
    `reminder_days`, `plan_config`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    1, -- horse_id：黑骏马
    'shoeing', -- plan_type：钉蹄
    '黑骏马钉蹄计划', -- plan_name
    42, -- cycle_days：6周
    '2024-01-01 10:00:00', -- last_date：上次钉蹄时间
    '2024-02-12 10:00:00', -- next_date：下次钉蹄时间
    7, -- reminder_days：提前7天提醒
    '{"farrier": "王师傅", "shoe_type": "标准马蹄铁", "special_requirements": "注意左前蹄形状调整"}', -- plan_config
    'admin', -- create_by
    '2024-01-08 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-08 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 驱虫计划
INSERT INTO `m_horse_health_plan` (
    `horse_id`, `plan_type`, `plan_name`, `cycle_days`, `last_date`, `next_date`, 
    `reminder_days`, `plan_config`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    1, -- horse_id：黑骏马
    'deworming', -- plan_type：驱虫
    '黑骏马驱虫计划', -- plan_name
    90, -- cycle_days：3个月
    '2023-12-15 09:00:00', -- last_date：上次驱虫时间
    '2024-03-14 09:00:00', -- next_date：下次驱虫时间
    14, -- reminder_days：提前14天提醒
    '{"medication": "阿苯达唑", "dosage": "7.5mg/kg", "vet_contact": "李兽医 13900001234"}', -- plan_config
    'admin', -- create_by
    '2024-01-08 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-08 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 沙漠之星的健康计划
-- 疫苗计划
INSERT INTO `m_horse_health_plan` (
    `horse_id`, `plan_type`, `plan_name`, `cycle_days`, `last_date`, `next_date`, 
    `reminder_days`, `plan_config`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    2, -- horse_id：沙漠之星
    'vaccine', -- plan_type：疫苗
    '沙漠之星疫苗计划', -- plan_name
    365, -- cycle_days：1年
    '2023-04-10 14:00:00', -- last_date：上次疫苗时间
    '2024-04-10 14:00:00', -- next_date：下次疫苗时间
    30, -- reminder_days：提前30天提醒
    '{"vaccine_type": "马流感+马疱疹病毒疫苗", "manufacturer": "辉瑞", "batch_number": "VAC20240101", "vet_clinic": "北京马匹医院"}', -- plan_config
    'admin', -- create_by
    '2024-01-08 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-08 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 搓牙计划
INSERT INTO `m_horse_health_plan` (
    `horse_id`, `plan_type`, `plan_name`, `cycle_days`, `last_date`, `next_date`, 
    `reminder_days`, `plan_config`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    2, -- horse_id：沙漠之星
    'dental', -- plan_type：搓牙
    '沙漠之星牙齿护理计划', -- plan_name
    180, -- cycle_days：6个月
    '2023-10-20 11:00:00', -- last_date：上次搓牙时间
    '2024-04-17 11:00:00', -- next_date：下次搓牙时间
    14, -- reminder_days：提前14天提醒
    '{"dentist": "赵兽医", "procedure": "牙齿浮点处理", "sedation_required": true, "notes": "上次发现后磨牙过长"}', -- plan_config
    'admin', -- create_by
    '2024-01-08 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-08 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- ============================================
-- 3. 健康记录数据
-- ============================================

-- 黑骏马的钉蹄记录
INSERT INTO `m_horse_health_record` (
    `horse_id`, `plan_id`, `record_type`, `record_date`, `executor_id`, `content`, 
    `img_url`, `next_date`, `record_data`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    1, -- horse_id：黑骏马
    1, -- plan_id：关联钉蹄计划
    'shoeing', -- record_type：钉蹄记录
    '2024-01-01 10:00:00', -- record_date：执行日期
    1, -- executor_id：假设执行人ID为1（钉蹄师）
    '完成6周定期钉蹄，所有马蹄铁状况良好。左前蹄形状已调整，马匹行走平稳。未发现蹄部疾病。', -- content
    '["/uploads/horse/shoeing_001_1.jpg", "/uploads/horse/shoeing_001_2.jpg"]', -- img_url
    '2024-02-12 10:00:00', -- next_date：下次钉蹄时间
    '{"shoe_condition": "良好", "hoof_health": "健康", "special_attention": "左前蹄需持续关注", "cost": 300}', -- record_data
    'admin', -- create_by
    '2024-01-01 10:30:00', -- create_time
    'admin', -- update_by
    '2024-01-01 10:30:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 沙漠之星的体检记录
INSERT INTO `m_horse_health_record` (
    `horse_id`, `plan_id`, `record_type`, `record_date`, `executor_id`, `content`, 
    `img_url`, `next_date`, `record_data`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    2, -- horse_id：沙漠之星
    NULL, -- plan_id：常规体检非计划性
    'health_check', -- record_type：健康检查
    '2024-01-05 15:30:00', -- record_date：检查日期
    2, -- executor_id：假设执行人ID为2（兽医）
    '月度常规健康检查。体温36.8°C，心率正常，呼吸平稳。体重450kg，体况良好。建议继续当前的训练强度和饮食搭配。', -- content
    '["/uploads/horse/health_check_002_1.jpg"]', -- img_url
    '2024-02-05 15:30:00', -- next_date：下次体检时间
    '{"temperature": 36.8, "heart_rate": 35, "weight": 450, "body_condition_score": 5, "recommendations": "保持现状"}', -- record_data
    'admin', -- create_by
    '2024-01-05 16:00:00', -- create_time
    'admin', -- update_by
    '2024-01-05 16:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 黑骏马的训练记录
INSERT INTO `m_horse_health_record` (
    `horse_id`, `plan_id`, `record_type`, `record_date`, `executor_id`, `content`, 
    `img_url`, `next_date`, `record_data`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    1, -- horse_id：黑骏马
    NULL, -- plan_id：训练记录非计划性
    'training', -- record_type：训练记录
    '2024-01-07 09:00:00', -- record_date：训练日期
    1, -- executor_id：张教练
    '进行基础调教训练，包括慢步、快步和跑步三种步态。马匹配合度良好，注意力集中。今日重点练习转弯和停止指令。', -- content
    '["/uploads/horse/training_001_1.jpg", "/uploads/horse/training_001_2.jpg"]', -- img_url
    NULL, -- next_date：训练记录无固定下次时间
    '{"duration_minutes": 45, "gaits_practiced": ["walk", "trot", "canter"], "rider_level": "intermediate", "performance_score": 8}', -- record_data
    'admin', -- create_by
    '2024-01-07 10:00:00', -- create_time
    'admin', -- update_by
    '2024-01-07 10:00:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);

-- 沙漠之星的用药记录
INSERT INTO `m_horse_health_record` (
    `horse_id`, `plan_id`, `record_type`, `record_date`, `executor_id`, `content`, 
    `img_url`, `next_date`, `record_data`, `create_by`, `create_time`, `update_by`, 
    `update_time`, `is_valid`, `is_delete`
) VALUES (
    2, -- horse_id：沙漠之星
    NULL, -- plan_id：临时用药非计划性
    'medication', -- record_type：用药记录
    '2024-01-03 08:00:00', -- record_date：用药日期
    2, -- executor_id：兽医/饲养员
    '发现右后腿轻微擦伤，已清洁伤口并涂抹抗菌药膏。连续观察3天，伤口愈合良好。今日停药。', -- content
    '["/uploads/horse/medication_002_1.jpg"]', -- img_url
    NULL, -- next_date：用药记录通常无固定下次时间
    '{"medication_name": "聚维酮碘软膏", "dosage": "外用适量", "duration_days": 3, "reason": "右后腿擦伤", "result": "愈合良好"}', -- record_data
    'admin', -- create_by
    '2024-01-03 08:30:00', -- create_time
    'admin', -- update_by
    '2024-01-03 08:30:00', -- update_time
    1, -- is_valid
    0  -- is_delete
);