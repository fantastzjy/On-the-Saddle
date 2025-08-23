-- 体验课商品表 (m_product_experience)
-- 新增商品类型4：体验课
-- 特点：使用基础单价，不使用教练费+马匹费的复合定价方式

CREATE TABLE `m_product_experience`
(
    `id`               bigint         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id`       bigint         NOT NULL COMMENT '商品ID，关联m_product表',
    `price`            decimal(10, 2) NOT NULL COMMENT '基础单价（体验课统一定价）',
    `duration_minutes` int            NOT NULL COMMENT '时长（分钟）',
    `duration_periods` decimal(3, 1)  NOT NULL COMMENT '时长（鞍时）',
    `max_students`     int            NOT NULL COMMENT '最大人数',
    `create_by`        varchar(50)    NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(50)    NOT NULL DEFAULT '' COMMENT '更新人',
    `update_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_id` (`product_id`),
    KEY                `idx_price` (`price`),
    KEY                `idx_duration` (`duration_minutes`),
    CONSTRAINT `fk_experience_product` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='体验课商品表';

-- 样例数据
INSERT INTO `m_product_experience` (`product_id`, `price`, `duration_minutes`, `duration_periods`, `max_students`, `create_by`)
VALUES
    (1, 200.00, 60, 1.0, 1, 'admin'),
    (2, 150.00, 45, 0.75, 2, 'admin');