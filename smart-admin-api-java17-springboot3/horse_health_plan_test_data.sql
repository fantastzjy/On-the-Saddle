-- 马匹健康计划测试数据

INSERT INTO `m_horse_health_plan` (
    `horse_id`,
    `plan_type`,
    `plan_name`,
    `cycle_days`,
    `last_date`,
    `next_date`,
    `reminder_days`,
    `plan_config`,
    `create_by`,
    `update_by`,
    `is_valid`,
    `is_delete`
) VALUES 

-- 马匹1的健康计划
(1, 'shoeing', '定期钉蹄', 42, '2024-06-15 09:00:00', '2024-07-27 09:00:00', 3,
'{"farrier": "张师傅", "shoe_type": "铝合金蹄铁", "special_requirements": "前蹄加强型"}',
'admin', 'admin', 1, 0),

(1, 'deworming', '常规驱虫', 90, '2024-05-01 10:00:00', '2024-07-30 10:00:00', 7,
'{"medication": "伊维菌素", "dosage": "200mg", "method": "口服"}',
'admin', 'admin', 1, 0),

(1, 'vaccine', '流感疫苗', 180, '2024-03-01 14:00:00', '2024-08-28 14:00:00', 14,
'{"vaccine_type": "马流感疫苗", "manufacturer": "辉瑞", "batch_number": "FL2024001"}',
'admin', 'admin', 1, 0),

-- 马匹2的健康计划
(2, 'dental', '搓牙检查', 365, '2023-08-15 09:30:00', '2024-08-15 09:30:00', 14,
'{"veterinarian": "李兽医", "examination_items": ["牙齿磨损", "咬合检查"], "sedation": "是"}',
'admin', 'admin', 1, 0),

(2, 'shoeing', '定期钉蹄', 35, '2024-07-01 08:00:00', '2024-08-05 08:00:00', 3,
'{"farrier": "王师傅", "shoe_type": "钢质蹄铁", "special_requirements": "矫正蹄形"}',
'admin', 'admin', 1, 0),

-- 马匹3的健康计划
(3, 'deworming', '春季驱虫', 120, '2024-04-20 11:00:00', '2024-08-18 11:00:00', 7,
'{"medication": "芬苯达唑", "dosage": "300mg", "method": "拌料"}',
'admin', 'admin', 1, 0),

(3, 'vaccine', '破伤风疫苗', 365, '2023-09-10 15:30:00', '2024-09-10 15:30:00', 21,
'{"vaccine_type": "破伤风疫苗", "manufacturer": "勃林格", "batch_number": "TT2023002"}',
'admin', 'admin', 1, 0),

-- 马匹4的健康计划
(4, 'shoeing', '竞赛钉蹄', 28, '2024-07-10 07:30:00', '2024-08-07 07:30:00', 2,
'{"farrier": "专业竞赛钉蹄师", "shoe_type": "轻质合金蹄铁", "special_requirements": "抓地力增强"}',
'admin', 'admin', 1, 0),

(4, 'medication', '关节保健', 30, '2024-07-15 16:00:00', '2024-08-14 16:00:00', 3,
'{"supplement": "氨基葡萄糖", "dosage": "15g/日", "duration": "连续30天"}',
'admin', 'admin', 1, 0),

-- 马匹5的健康计划
(5, 'deworming', '秋季驱虫', 90, '2024-05-20 10:30:00', '2024-08-18 10:30:00', 5,
'{"medication": "阿维菌素", "dosage": "250mg", "method": "注射"}',
'admin', 'admin', 1, 0),

(5, 'dental', '年度牙科检查', 365, '2023-11-01 13:00:00', '2024-11-01 13:00:00', 30,
'{"veterinarian": "专业马牙医", "examination_items": ["全口检查", "浮牙处理"], "equipment": "电动牙锉"}',
'admin', 'admin', 1, 0),

-- 马匹6的健康计划
(6, 'shoeing', '治疗性钉蹄', 21, '2024-07-20 09:00:00', '2024-08-10 09:00:00', 2,
'{"farrier": "治疗钉蹄专家", "shoe_type": "矫形蹄铁", "special_requirements": "左前蹄矫正", "therapeutic": true}',
'admin', 'admin', 1, 0),

(6, 'vaccine', '综合疫苗', 180, '2024-02-15 14:30:00', '2024-08-13 14:30:00', 10,
'{"vaccine_type": "四联疫苗", "manufacturer": "硕腾", "components": ["流感", "鼻肺炎", "脑脊髓炎", "破伤风"]}',
'admin', 'admin', 1, 0),

-- 马匹7的健康计划
(7, 'medication', '老年马保健', 60, '2024-06-01 08:00:00', '2024-07-31 08:00:00', 7,
'{"supplements": ["维生素E", "硒", "关节保健品"], "special_diet": "高纤维低糖", "monitoring": "定期体重检查"}',
'admin', 'admin', 1, 0),

-- 马匹8的健康计划
(8, 'deworming', '幼马驱虫', 60, '2024-06-10 11:30:00', '2024-08-09 11:30:00', 5,
'{"medication": "幼马专用驱虫药", "dosage": "按体重计算", "monitoring": "密切观察反应"}',
'admin', 'admin', 1, 0),

-- 马匹9的健康计划（康复期马匹）
(9, 'medication', '康复治疗', 7, '2024-07-25 16:30:00', '2024-08-01 16:30:00', 1,
'{"treatment": "物理治疗", "medication": "消炎药", "rest_period": "继续休息", "reassessment": "每周评估"}',
'admin', 'admin', 1, 0),

-- 马匹10的健康计划
(10, 'shoeing', '表演马钉蹄', 35, '2024-07-05 10:00:00', '2024-08-09 10:00:00', 3,
'{"farrier": "表演马专业钉蹄师", "shoe_type": "装饰性蹄铁", "special_requirements": "美观实用并重"}',
'admin', 'admin', 1, 0);