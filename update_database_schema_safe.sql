-- ========================================
-- 马术俱乐部数据库表结构更新脚本（安全版本）
-- 功能：将现有表的 NULL 字段修改为 NOT NULL 并添加默认值
-- 创建时间：2025-08-11
-- 说明：使用条件更新避免安全模式警告
-- ========================================

-- 临时禁用安全更新模式（如果需要）
-- SET SQL_SAFE_UPDATES = 0;

-- ========================================
-- 1. 更新 m_club 俱乐部表
-- ========================================

-- 使用条件更新现有的 NULL 值为默认值
UPDATE m_club SET club_code = '' WHERE club_code IS NULL;
UPDATE m_club SET logo_url = '' WHERE logo_url IS NULL;
UPDATE m_club SET carousel_images = '[]' WHERE carousel_images IS NULL;
UPDATE m_club SET pc_banner_url = '' WHERE pc_banner_url IS NULL;
UPDATE m_club SET business_start_time = '09:00:00' WHERE business_start_time IS NULL;
UPDATE m_club SET business_end_time = '18:00:00' WHERE business_end_time IS NULL;
UPDATE m_club SET address = '' WHERE address IS NULL;
UPDATE m_club SET phone = '' WHERE phone IS NULL;
UPDATE m_club SET description = '' WHERE description IS NULL;
UPDATE m_club SET honor_info = '' WHERE honor_info IS NULL;
UPDATE m_club SET booking_notice = '' WHERE booking_notice IS NULL;
UPDATE m_club SET province = '' WHERE province IS NULL;
UPDATE m_club SET city = '' WHERE city IS NULL;
UPDATE m_club SET district = '' WHERE district IS NULL;
UPDATE m_club SET business_license_url = '' WHERE business_license_url IS NULL;
UPDATE m_club SET contact_person = '' WHERE contact_person IS NULL;
UPDATE m_club SET contact_phone = '' WHERE contact_phone IS NULL;
UPDATE m_club SET email = '' WHERE email IS NULL;

-- 修改字段属性为 NOT NULL 并添加默认值
ALTER TABLE m_club 
    MODIFY COLUMN club_code VARCHAR(50) DEFAULT '' NOT NULL COMMENT '俱乐部编码',
    MODIFY COLUMN logo_url VARCHAR(500) DEFAULT '' NOT NULL COMMENT 'LOGO地址',
    MODIFY COLUMN carousel_images TEXT NOT NULL COMMENT '轮播图片地址列表JSON格式',
    MODIFY COLUMN pc_banner_url VARCHAR(500) DEFAULT '' NOT NULL COMMENT 'PC端首页图片地址',
    MODIFY COLUMN business_start_time TIME DEFAULT '09:00:00' NOT NULL COMMENT '营业开始时间',
    MODIFY COLUMN business_end_time TIME DEFAULT '18:00:00' NOT NULL COMMENT '营业结束时间',
    MODIFY COLUMN address VARCHAR(200) DEFAULT '' NOT NULL COMMENT '详细地址',
    MODIFY COLUMN phone VARCHAR(20) DEFAULT '' NOT NULL COMMENT '电话',
    MODIFY COLUMN description TEXT NOT NULL COMMENT '俱乐部详情',
    MODIFY COLUMN honor_info TEXT NOT NULL COMMENT '荣誉信息',
    MODIFY COLUMN booking_notice TEXT NOT NULL COMMENT '约课需知',
    MODIFY COLUMN province VARCHAR(50) DEFAULT '' NOT NULL COMMENT '省份',
    MODIFY COLUMN city VARCHAR(50) DEFAULT '' NOT NULL COMMENT '城市',
    MODIFY COLUMN district VARCHAR(50) DEFAULT '' NOT NULL COMMENT '区县',
    MODIFY COLUMN business_license_url VARCHAR(500) DEFAULT '' NOT NULL COMMENT '营业执照图片地址',
    MODIFY COLUMN contact_person VARCHAR(50) DEFAULT '' NOT NULL COMMENT '联系人',
    MODIFY COLUMN contact_phone VARCHAR(20) DEFAULT '' NOT NULL COMMENT '联系电话',
    MODIFY COLUMN email VARCHAR(100) DEFAULT '' NOT NULL COMMENT '邮箱';

-- 验证 m_club 表更新结果
SELECT 'm_club table updated successfully' AS status;

-- ========================================
-- 2. 更新 m_coach 教练表
-- ========================================

-- 使用条件更新现有的 NULL 值为默认值
UPDATE m_coach SET coach_no = '' WHERE coach_no IS NULL;
UPDATE m_coach SET avatar_url = '' WHERE avatar_url IS NULL;
UPDATE m_coach SET entry_date = '1970-01-01 00:00:00' WHERE entry_date IS NULL;
UPDATE m_coach SET specialties = '' WHERE specialties IS NULL;
UPDATE m_coach SET introduction = '' WHERE introduction IS NULL;
UPDATE m_coach SET rider_cert_no = '' WHERE rider_cert_no IS NULL;
UPDATE m_coach SET rider_level_show_jumping = '' WHERE rider_level_show_jumping IS NULL;
UPDATE m_coach SET rider_level_dressage = '' WHERE rider_level_dressage IS NULL;
UPDATE m_coach SET rider_level_eventing = '' WHERE rider_level_eventing IS NULL;
UPDATE m_coach SET rider_cert_img_url = '[]' WHERE rider_cert_img_url IS NULL;
UPDATE m_coach SET coach_cert_no = '' WHERE coach_cert_no IS NULL;
UPDATE m_coach SET coach_level = '' WHERE coach_level IS NULL;
UPDATE m_coach SET coach_cert_img_url = '[]' WHERE coach_cert_img_url IS NULL;
UPDATE m_coach SET sort_order = 0 WHERE sort_order IS NULL;

-- 修改字段属性为 NOT NULL 并添加默认值
ALTER TABLE m_coach 
    MODIFY COLUMN coach_no VARCHAR(50) DEFAULT '' NOT NULL COMMENT '教练编号',
    MODIFY COLUMN avatar_url VARCHAR(500) DEFAULT '' NOT NULL COMMENT '头像照片地址',
    MODIFY COLUMN entry_date DATETIME DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '入行时间',
    MODIFY COLUMN specialties VARCHAR(200) DEFAULT '' NOT NULL COMMENT '专长领域',
    MODIFY COLUMN introduction TEXT NOT NULL COMMENT '个人介绍',
    MODIFY COLUMN rider_cert_no VARCHAR(100) DEFAULT '' NOT NULL COMMENT '骑手证号码',
    MODIFY COLUMN rider_level_show_jumping VARCHAR(20) DEFAULT '' NOT NULL COMMENT '场地障碍等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    MODIFY COLUMN rider_level_dressage VARCHAR(20) DEFAULT '' NOT NULL COMMENT '盛装舞步等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    MODIFY COLUMN rider_level_eventing VARCHAR(20) DEFAULT '' NOT NULL COMMENT '三项赛等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    MODIFY COLUMN rider_cert_img_url TEXT NOT NULL COMMENT '骑手证书图片地址JSON格式',
    MODIFY COLUMN coach_cert_no VARCHAR(100) DEFAULT '' NOT NULL COMMENT '星级教练证号码',
    MODIFY COLUMN coach_level VARCHAR(20) DEFAULT '' NOT NULL COMMENT '教练等级: 一星,二星,三星,四星,五星',
    MODIFY COLUMN coach_cert_img_url TEXT NOT NULL COMMENT '教练证书图片地址JSON格式',
    MODIFY COLUMN sort_order INT DEFAULT 0 NOT NULL COMMENT '排序';

-- 验证 m_coach 表更新结果
SELECT 'm_coach table updated successfully' AS status;

-- ========================================
-- 3. 更新 m_horse 马匹表
-- ========================================

-- 使用条件更新现有的 NULL 值为默认值
UPDATE m_horse SET horse_code = '' WHERE horse_code IS NULL;
UPDATE m_horse SET breed = '' WHERE breed IS NULL;
UPDATE m_horse SET gender = 1 WHERE gender IS NULL;
UPDATE m_horse SET color = '' WHERE color IS NULL;
UPDATE m_horse SET birth_date = '1970-01-01 00:00:00' WHERE birth_date IS NULL;
UPDATE m_horse SET chip_no = '' WHERE chip_no IS NULL;
UPDATE m_horse SET passport_no = '' WHERE passport_no IS NULL;
UPDATE m_horse SET pedigree_cert_url = '' WHERE pedigree_cert_url IS NULL;
UPDATE m_horse SET owner_id = 0 WHERE owner_id IS NULL;
UPDATE m_horse SET responsible_coach_id = 0 WHERE responsible_coach_id IS NULL;
UPDATE m_horse SET responsible_groom_id = 0 WHERE responsible_groom_id IS NULL;
UPDATE m_horse SET boarding_start_date = '1970-01-01 00:00:00' WHERE boarding_start_date IS NULL;
UPDATE m_horse SET boarding_end_date = '1970-01-01 00:00:00' WHERE boarding_end_date IS NULL;
UPDATE m_horse SET health_status = 1 WHERE health_status IS NULL;
UPDATE m_horse SET work_status = 1 WHERE work_status IS NULL;
UPDATE m_horse SET height = 0 WHERE height IS NULL;
UPDATE m_horse SET weight = 0 WHERE weight IS NULL;
UPDATE m_horse SET horse_data = '{}' WHERE horse_data IS NULL;

-- 修改字段属性为 NOT NULL 并添加默认值
ALTER TABLE m_horse 
    MODIFY COLUMN horse_code VARCHAR(50) DEFAULT '' NOT NULL COMMENT '马匹编号',
    MODIFY COLUMN breed VARCHAR(50) DEFAULT '' NOT NULL COMMENT '品种',
    MODIFY COLUMN gender TINYINT DEFAULT 1 NOT NULL COMMENT '性别: 1-公 2-母 3-骟',
    MODIFY COLUMN color VARCHAR(50) DEFAULT '' NOT NULL COMMENT '毛色',
    MODIFY COLUMN birth_date DATETIME DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '出生日期时间',
    MODIFY COLUMN chip_no VARCHAR(100) DEFAULT '' NOT NULL COMMENT '芯片号',
    MODIFY COLUMN passport_no VARCHAR(100) DEFAULT '' NOT NULL COMMENT '护照号',
    MODIFY COLUMN pedigree_cert_url VARCHAR(500) DEFAULT '' NOT NULL COMMENT '血统证书图片地址',
    MODIFY COLUMN owner_id BIGINT DEFAULT 0 NOT NULL COMMENT '马主ID(马主马)',
    MODIFY COLUMN responsible_coach_id BIGINT DEFAULT 0 NOT NULL COMMENT '责任教练ID',
    MODIFY COLUMN responsible_groom_id BIGINT DEFAULT 0 NOT NULL COMMENT '责任马工ID',
    MODIFY COLUMN boarding_start_date DATETIME DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '寄养开始日期时间',
    MODIFY COLUMN boarding_end_date DATETIME DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '寄养结束日期时间',
    MODIFY COLUMN health_status TINYINT DEFAULT 1 NOT NULL COMMENT '健康状态: 1-健康 2-观察 3-治疗',
    MODIFY COLUMN work_status TINYINT DEFAULT 1 NOT NULL COMMENT '工作状态: 1-可用 2-休息 3-治疗',
    MODIFY COLUMN height INT DEFAULT 0 NOT NULL COMMENT '身高(cm)',
    MODIFY COLUMN weight INT DEFAULT 0 NOT NULL COMMENT '体重(kg)',
    MODIFY COLUMN horse_data TEXT NOT NULL COMMENT '马匹扩展数据JSON格式';

-- 验证 m_horse 表更新结果
SELECT 'm_horse table updated successfully' AS status;

-- ========================================
-- 4. 更新 m_horse_health_plan 马匹健康计划表
-- ========================================

-- 使用条件更新现有的 NULL 值为默认值
UPDATE m_horse_health_plan SET cycle_days = 0 WHERE cycle_days IS NULL;
UPDATE m_horse_health_plan SET last_date = '1970-01-01 00:00:00' WHERE last_date IS NULL;
UPDATE m_horse_health_plan SET reminder_days = 7 WHERE reminder_days IS NULL;
UPDATE m_horse_health_plan SET plan_config = '{}' WHERE plan_config IS NULL;

-- 修改字段属性为 NOT NULL 并添加默认值
ALTER TABLE m_horse_health_plan 
    MODIFY COLUMN cycle_days INT DEFAULT 0 NOT NULL COMMENT '周期天数',
    MODIFY COLUMN last_date DATETIME DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '上次执行日期时间',
    MODIFY COLUMN reminder_days INT DEFAULT 7 NOT NULL COMMENT '提前提醒天数',
    MODIFY COLUMN plan_config TEXT NOT NULL COMMENT '计划配置JSON格式';

-- 验证 m_horse_health_plan 表更新结果
SELECT 'm_horse_health_plan table updated successfully' AS status;

-- ========================================
-- 5. 更新 m_horse_health_record 马匹健康记录表
-- ========================================

-- 使用条件更新现有的 NULL 值为默认值
UPDATE m_horse_health_record SET plan_id = 0 WHERE plan_id IS NULL;
UPDATE m_horse_health_record SET executor_id = 0 WHERE executor_id IS NULL;
UPDATE m_horse_health_record SET content = '' WHERE content IS NULL;
UPDATE m_horse_health_record SET img_url = '[]' WHERE img_url IS NULL;
UPDATE m_horse_health_record SET next_date = '1970-01-01 00:00:00' WHERE next_date IS NULL;
UPDATE m_horse_health_record SET record_data = '{}' WHERE record_data IS NULL;
UPDATE m_horse_health_record SET plan_type = '' WHERE plan_type IS NULL;

-- 修改字段属性为 NOT NULL 并添加默认值
ALTER TABLE m_horse_health_record 
    MODIFY COLUMN plan_id BIGINT DEFAULT 0 NOT NULL COMMENT '关联计划ID',
    MODIFY COLUMN executor_id BIGINT DEFAULT 0 NOT NULL COMMENT '执行人ID',
    MODIFY COLUMN content TEXT NOT NULL COMMENT '记录内容',
    MODIFY COLUMN img_url TEXT NOT NULL COMMENT '图片地址JSON格式',
    MODIFY COLUMN next_date DATETIME DEFAULT '1970-01-01 00:00:00' NOT NULL COMMENT '下次执行日期时间',
    MODIFY COLUMN record_data TEXT NOT NULL COMMENT '记录扩展数据JSON格式',
    MODIFY COLUMN plan_type VARCHAR(50) DEFAULT '' NOT NULL COMMENT '计划类型';

-- 验证 m_horse_health_record 表更新结果
SELECT 'm_horse_health_record table updated successfully' AS status;

-- ========================================
-- 脚本执行完成
-- ========================================

-- 恢复安全更新模式（如果之前禁用了）
-- SET SQL_SAFE_UPDATES = 1;

-- 最终验证
SELECT 'All database schema updates completed successfully!' AS final_status;

-- ========================================
-- 可选：查看表结构验证更新结果
-- ========================================
/*
-- 取消注释下面的语句来查看表结构
SHOW CREATE TABLE m_club;
SHOW CREATE TABLE m_coach;
SHOW CREATE TABLE m_horse;
SHOW CREATE TABLE m_horse_health_plan;
SHOW CREATE TABLE m_horse_health_record;

-- 或者查看字段详情
DESCRIBE m_club;
DESCRIBE m_coach;
DESCRIBE m_horse;
DESCRIBE m_horse_health_plan;
DESCRIBE m_horse_health_record;
*/