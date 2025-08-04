-- 员工表结构修改脚本
-- 添加生日和身份证号码字段

-- 1. 添加生日字段
ALTER TABLE `t_employee` ADD COLUMN `birth_date` DATE COMMENT '生日';

-- 2. 添加身份证号码字段
ALTER TABLE `t_employee` ADD COLUMN `id_card` VARCHAR(18) COMMENT '身份证号码';

-- 3. 查看修改后的表结构
-- DESCRIBE `t_employee`;