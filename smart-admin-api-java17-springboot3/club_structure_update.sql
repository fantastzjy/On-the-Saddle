-- 俱乐部表结构修改脚本
-- 删除经纬度、法人信息、到期时间字段
-- 将banner_url改为carousel_images存储轮播图列表

-- 1. 删除不需要的字段
ALTER TABLE `m_club` DROP COLUMN `latitude`;
ALTER TABLE `m_club` DROP COLUMN `longitude`;
ALTER TABLE `m_club` DROP COLUMN `legal_person`;
ALTER TABLE `m_club` DROP COLUMN `expire_date`;

-- 2. 修改置顶图片字段为轮播图片列表
ALTER TABLE `m_club` CHANGE COLUMN `banner_url` `carousel_images` TEXT COMMENT '轮播图片地址列表JSON格式';

-- 3. 查看修改后的表结构
-- DESCRIBE `m_club`;