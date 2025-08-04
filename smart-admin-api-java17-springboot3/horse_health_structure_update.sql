-- 马匹健康计划和记录表结构修改脚本
-- 删除计划名称字段，记录表添加计划类型冗余字段

-- 1. 健康计划表：删除计划名称字段
ALTER TABLE `m_horse_health_plan` DROP COLUMN `plan_name`;

-- 2. 健康记录表：删除记录类型字段，添加计划类型冗余字段
ALTER TABLE `m_horse_health_record` DROP COLUMN `record_type`;
ALTER TABLE `m_horse_health_record` ADD COLUMN `plan_type` VARCHAR(50) COMMENT '计划类型(冗余字段)';

-- 3. 数据迁移：将现有记录的计划类型同步过来
UPDATE m_horse_health_record hr 
SET hr.plan_type = (
    SELECT hp.plan_type 
    FROM m_horse_health_plan hp 
    WHERE hp.id = hr.plan_id
) 
WHERE hr.plan_id IS NOT NULL;

-- 4. 查看修改后的表结构
-- DESCRIBE `m_horse_health_plan`;
-- DESCRIBE `m_horse_health_record`;