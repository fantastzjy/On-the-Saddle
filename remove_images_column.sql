-- 移除商品表中的images字段
-- 执行前请备份数据库

-- 移除 m_product 表中的 images 字段
ALTER TABLE `m_product` DROP COLUMN `images`;

-- 查看修改后的表结构
DESCRIBE `m_product`;