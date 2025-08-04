-- 马匹健康记录测试数据

INSERT INTO `m_horse_health_record` (
    `horse_id`,
    `plan_id`,
    `record_type`,
    `record_date`,
    `executor_id`,
    `content`,
    `img_url`,
    `next_date`,
    `record_data`,
    `create_by`,
    `update_by`,
    `is_valid`,
    `is_delete`
) VALUES 

-- 马匹1的健康记录
(1, 1, 'shoeing', '2024-06-15 09:00:00', 1001,
'完成定期钉蹄，马匹状态良好。前蹄更换了新的铝合金蹄铁，后蹄进行了修整。马匹配合度很高，整个过程顺利。',
'["https://example.com/records/horse1_shoeing_1.jpg", "https://example.com/records/horse1_shoeing_2.jpg"]',
'2024-07-27 09:00:00',
'{"duration": "45分钟", "hoof_condition": "良好", "shoe_size": {"front": "5号", "rear": "4号"}, "cost": 280}',
'admin', 'admin', 1, 0),

(1, 2, 'deworming', '2024-05-01 10:00:00', 1002,
'完成春季驱虫，使用伊维菌素200mg口服。马匹无不良反应，食欲正常。建议下次驱虫前进行粪便检查。',
'["https://example.com/records/horse1_deworming_1.jpg"]',
'2024-07-30 10:00:00',
'{"medication_batch": "IV2024050", "administration_time": "10:00", "post_observation": "24小时", "side_effects": "无"}',
'admin', 'admin', 1, 0),

-- 马匹2的健康记录
(2, 4, 'dental', '2023-08-15 09:30:00', 1003,
'年度牙科检查完成。发现上颚第三磨牙有轻微过长，已进行浮牙处理。建议6个月后复查。',
'["https://example.com/records/horse2_dental_1.jpg", "https://example.com/records/horse2_dental_2.jpg"]',
'2024-08-15 09:30:00',
'{"sedation_used": "右美托咪定", "teeth_floated": ["上颚左3", "上颚右3"], "duration": "35分钟"}',
'admin', 'admin', 1, 0),

(2, 5, 'shoeing', '2024-07-01 08:00:00', 1001,
'矫正性钉蹄完成。左前蹄蹄形得到明显改善，继续观察步态变化。使用钢质蹄铁增强支撑。',
'["https://example.com/records/horse2_shoeing_1.jpg"]',
'2024-08-05 08:00:00',
'{"correction_type": "蹄形矫正", "improvement": "明显", "gait_analysis": "步态更加平稳"}',
'admin', 'admin', 1, 0),

-- 马匹3的健康记录
(3, 6, 'deworming', '2024-04-20 11:00:00', 1002,
'春季驱虫顺利完成，使用芬苯达唑300mg拌料给药。马匹适应良好，无应激反应。',
'["https://example.com/records/horse3_deworming_1.jpg"]',
'2024-08-18 11:00:00',
'{"feed_mixed": "是", "consumption": "完全", "monitoring_period": "72小时", "effectiveness": "良好"}',
'admin', 'admin', 1, 0),

-- 马匹4的健康记录
(4, 8, 'shoeing', '2024-07-10 07:30:00', 1004,
'竞赛专用钉蹄完成。使用轻质合金蹄铁，增强抓地力。为即将到来的比赛做好准备。',
'["https://example.com/records/horse4_shoeing_1.jpg", "https://example.com/records/horse4_shoeing_2.jpg"]',
'2024-08-07 07:30:00',
'{"competition_type": "场地障碍", "shoe_weight": "200g", "grip_enhancement": "是", "performance_test": "通过"}',
'admin', 'admin', 1, 0),

(4, 9, 'medication', '2024-07-15 16:00:00', 1003,
'开始关节保健补充剂治疗。每日添加15g氨基葡萄糖到饲料中，马匹接受度良好。',
'["https://example.com/records/horse4_medication_1.jpg"]',
'2024-08-14 16:00:00',
'{"supplement_type": "氨基葡萄糖", "daily_dose": "15g", "administration_method": "混合饲料", "compliance": "良好"}',
'admin', 'admin', 1, 0),

-- 马匹5的健康记录
(5, 10, 'deworming', '2024-05-20 10:30:00', 1005,
'注射式驱虫完成，使用阿维菌素250mg肌肉注射。注射部位无异常，马匹状态稳定。',
'["https://example.com/records/horse5_deworming_1.jpg"]',
'2024-08-18 10:30:00',
'{"injection_site": "颈部肌肉", "needle_size": "18G", "injection_volume": "10ml", "post_care": "按摩注射部位"}',
'admin', 'admin', 1, 0),

-- 马匹6的健康记录
(6, 12, 'shoeing', '2024-07-20 09:00:00', 1006,
'治疗性钉蹄顺利完成。左前蹄使用矫形蹄铁，明显改善支撑角度。继续监测恢复情况。',
'["https://example.com/records/horse6_shoeing_1.jpg", "https://example.com/records/horse6_shoeing_2.jpg"]',
'2024-08-10 09:00:00',
'{"therapeutic_shoe": "矫形蹄铁", "angle_correction": "5度", "pain_relief": "明显", "mobility": "改善"}',
'admin', 'admin', 1, 0),

-- 马匹7的健康记录
(7, 14, 'medication', '2024-06-01 08:00:00', 1007,
'老年马保健计划启动。添加维生素E、硒补充剂，调整饲料配方。马匹精神状态良好。',
'["https://example.com/records/horse7_medication_1.jpg"]',
'2024-07-31 08:00:00',
'{"supplements": ["维生素E 400IU", "硒 2mg"], "diet_adjustment": "高纤维低糖", "body_weight": "510kg", "body_condition": "良好"}',
'admin', 'admin', 1, 0),

-- 马匹8的健康记录
(8, 15, 'deworming', '2024-06-10 11:30:00', 1008,
'幼马专用驱虫药物治疗完成。严密监测72小时，无不良反应。生长发育正常。',
'["https://example.com/records/horse8_deworming_1.jpg"]',
'2024-08-09 11:30:00',
'{"age_appropriate": "是", "dosage_calculation": "按体重20mg/kg", "growth_monitoring": "正常", "appetite": "良好"}',
'admin', 'admin', 1, 0),

-- 马匹9的健康记录（康复治疗）
(9, 16, 'medication', '2024-07-25 16:30:00', 1009,
'康复治疗进行中。右前蹄扭伤恢复良好，肿胀明显消退。继续冷敷和消炎药治疗。',
'["https://example.com/records/horse9_treatment_1.jpg", "https://example.com/records/horse9_treatment_2.jpg"]',
'2024-08-01 16:30:00',
'{"injury_type": "右前蹄扭伤", "swelling": "明显消退", "pain_level": "轻微", "treatment": "冷敷+消炎药", "recovery_progress": "良好"}',
'admin', 'admin', 1, 0),

-- 马匹10的健康记录
(10, 17, 'shoeing', '2024-07-05 10:00:00', 1010,
'表演马专用钉蹄完成。使用装饰性蹄铁，既美观又实用。为即将到来的表演做好准备。',
'["https://example.com/records/horse10_shoeing_1.jpg"]',
'2024-08-09 10:00:00',
'{"decorative_shoes": "是", "performance_ready": "是", "aesthetics": "优秀", "functionality": "完好"}',
'admin', 'admin', 1, 0),

-- 额外的常规检查记录
(1, NULL, 'health_check', '2024-07-20 14:00:00', 1003,
'月度健康检查：体温36.8°C，心率32次/分，呼吸12次/分。整体状况良好，无异常发现。',
'["https://example.com/records/horse1_health_1.jpg"]',
NULL,
'{"temperature": "36.8°C", "heart_rate": "32/min", "respiratory_rate": "12/min", "body_condition_score": "6/9"}',
'admin', 'admin', 1, 0),

(3, NULL, 'health_check', '2024-07-18 15:30:00', 1003,
'训练后健康检查：马匹恢复良好，无异常出汗或呼吸急促。建议继续当前训练强度。',
'["https://example.com/records/horse3_health_1.jpg"]',
NULL,
'{"post_training": "是", "recovery_time": "正常", "hydration": "良好", "muscle_tension": "无"}',
'admin', 'admin', 1, 0),

(5, NULL, 'emergency', '2024-07-22 20:15:00', 1003,
'紧急处理：马匹出现轻微绞痛症状。经检查为轻度胃肠不适，给予缓解药物。症状缓解。',
'["https://example.com/records/horse5_emergency_1.jpg"]',
NULL,
'{"symptoms": "轻微绞痛", "treatment": "止痛+胃肠药", "duration": "2小时", "outcome": "完全缓解", "follow_up": "24小时观察"}',
'admin', 'admin', 1, 0);