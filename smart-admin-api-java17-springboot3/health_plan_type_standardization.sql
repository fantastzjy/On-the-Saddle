-- 马匹健康管理系统类型标准化脚本
-- 执行前请备份相关数据表

-- 1. 查看现有数据分布
SELECT 'healthplan_types' as table_name, plan_type, COUNT(*) as count FROM m_horse_health_plan GROUP BY plan_type
UNION ALL  
SELECT 'healthrecord_types' as table_name, plan_type, COUNT(*) as count FROM m_horse_health_record GROUP BY plan_type;

-- 2. 标准化健康计划表的类型值
UPDATE m_horse_health_plan SET plan_type = 'shoeing' WHERE plan_type IN ('钉蹄', '修蹄', 'shoeing');
UPDATE m_horse_health_plan SET plan_type = 'deworming' WHERE plan_type IN ('驱虫', 'deworming');
UPDATE m_horse_health_plan SET plan_type = 'dental' WHERE plan_type IN ('搓牙', '牙科', '牙齿', 'dental');
UPDATE m_horse_health_plan SET plan_type = 'vaccine' WHERE plan_type IN ('疫苗', 'vaccine');
UPDATE m_horse_health_plan SET plan_type = 'medication' WHERE plan_type IN ('用药', '治疗', '药物', 'medication');

-- 3. 标准化健康记录表的类型值
UPDATE m_horse_health_record SET plan_type = 'shoeing' WHERE plan_type IN ('钉蹄', '修蹄', 'shoeing');
UPDATE m_horse_health_record SET plan_type = 'deworming' WHERE plan_type IN ('驱虫', 'deworming');
UPDATE m_horse_health_record SET plan_type = 'dental' WHERE plan_type IN ('搓牙', '牙科', '牙齿', 'dental');
UPDATE m_horse_health_record SET plan_type = 'vaccine' WHERE plan_type IN ('疫苗', 'vaccine');
UPDATE m_horse_health_record SET plan_type = 'medication' WHERE plan_type IN ('用药', '治疗', '药物', 'medication');

-- 4. 同步记录表的planType（基于关联的计划）
UPDATE m_horse_health_record r 
JOIN m_horse_health_plan p ON r.plan_id = p.id 
SET r.plan_type = p.plan_type 
WHERE r.plan_id IS NOT NULL AND (r.plan_type IS NULL OR r.plan_type = '');

-- 5. 验证数据标准化结果
SELECT 'After_standardization_healthplan' as table_name, plan_type, COUNT(*) as count FROM m_horse_health_plan GROUP BY plan_type
UNION ALL  
SELECT 'After_standardization_healthrecord' as table_name, plan_type, COUNT(*) as count FROM m_horse_health_record GROUP BY plan_type;

-- 6. 添加检查约束确保类型值正确（可选）
-- ALTER TABLE m_horse_health_plan 
-- ADD CONSTRAINT chk_health_plan_type 
-- CHECK (plan_type IN ('shoeing', 'deworming', 'dental', 'vaccine', 'medication'));

-- 注意：
-- 1. 执行前请先备份数据库
-- 2. 建议在测试环境先执行验证
-- 3. 约束添加可能会失败如果存在不符合的数据，请先清理数据
-- 4. 如需回滚，请使用备份数据恢复