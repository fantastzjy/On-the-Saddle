-- ========================================
-- 体验课商品表创建SQL
-- 新增商品类型4：体验课支持
-- ========================================

-- 创建体验课商品表 (m_product_experience)
CREATE TABLE `m_product_experience`
(
    `id`               bigint         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`       bigint         NOT NULL COMMENT '商品ID，关联m_product表',
    `price`            decimal(10, 2) NOT NULL COMMENT '体验课单价（基础定价，不使用教练费+马匹费模式）',
    `duration_minutes` int            NOT NULL COMMENT '体验课时长（分钟）',
    `duration_periods` decimal(3, 1)  NOT NULL COMMENT '体验课时长（鞍时）',
    `max_students`     int            NOT NULL COMMENT '最大学员人数',
    `create_by`        varchar(50)    NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(50)    NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`),
    KEY                `idx_price` (`price`),
    KEY                `idx_duration_minutes` (`duration_minutes`),
    KEY                `idx_create_time` (`create_time`),
    CONSTRAINT `fk_experience_product` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='体验课商品表';

-- ========================================
-- 插入测试数据（可选）
-- ========================================

-- 注意：需要先确保 m_product 表中存在对应的商品记录，且 product_type = 4
-- 以下是示例数据，实际使用时需要根据具体的 product_id 调整

-- INSERT INTO `m_product_experience` (`product_id`, `price`, `duration_minutes`, `duration_periods`, `max_students`, `create_by`)
-- VALUES
--     (1, 200.00, 60, 1.0, 1, 'admin'),   -- 60分钟体验课，1人，200元
--     (2, 150.00, 45, 0.75, 2, 'admin'),  -- 45分钟体验课，2人，150元
--     (3, 300.00, 90, 1.5, 1, 'admin');   -- 90分钟高级体验课，1人，300元

-- ========================================
-- 验证表结构
-- ========================================

-- 查看表结构
-- DESC m_product_experience;

-- 查看表创建语句
-- SHOW CREATE TABLE m_product_experience;