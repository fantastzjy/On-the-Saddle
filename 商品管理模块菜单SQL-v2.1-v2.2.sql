-- ========================================
-- 马术俱乐部商品管理模块菜单SQL - 模块2.1 & 2.2
-- 基于开发清单v2.0 - 简化版商品管理
-- 对应PROD_BE_001-006 后端服务和PROD_FE_001-009 前端页面
-- ========================================

-- 2500 商品管理主菜单
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2500, '商品管理', 1, 0, 250, '/product', null, 1, '', 'product', 'ShoppingOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2501 商品列表 (对应PROD_FE_001: 商品列表页面)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2501, '商品列表', 2, 2500, 1, '/product/list', '/views/business/product/ProductList.vue', 1, 'business:product:query', 'business:product:query', 'UnorderedListOutlined', null, 0, null, 1, 1, 0, 0, 1, now(), 1, now());

-- 2502 商品新增 (对应PROD_FE_002: 智能商品新增页面)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2502, '商品新增', 2, 2500, 2, '/product/add', '/views/business/product/ProductAdd.vue', 1, 'business:product:add', 'business:product:add', 'PlusOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 商品列表功能点 (对应PROD_BE_001: 商品基础CRUD接口)
-- ========================================

-- 2510 查看详情
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2510, '查看详情', 3, 2501, 1, null, null, 1, 'business:product:detail', 'business:product:detail', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2511 编辑商品
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2511, '编辑商品', 3, 2501, 2, null, null, 1, 'business:product:update', 'business:product:update', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2512 删除商品
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2512, '删除商品', 3, 2501, 3, null, null, 1, 'business:product:delete', 'business:product:delete', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2513 批量操作 (对应PROD_BE_001: 批量处理功能)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2513, '批量操作', 3, 2501, 4, null, null, 1, 'business:product:batch', 'business:product:batch', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2514 商品状态管理
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2514, '商品状态', 3, 2501, 5, null, null, 1, 'business:product:status', 'business:product:status', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 商品新增功能点 (对应PROD_BE_002-006: 动态配置和相关服务)
-- ========================================

-- 2520 动态表单配置 (对应PROD_BE_002: 商品类型动态配置接口)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2520, '表单配置', 3, 2502, 1, null, null, 1, 'business:product:config', 'business:product:config', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2521 价格计算 (对应PROD_BE_003: 商品价格计算服务)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2521, '价格计算', 3, 2502, 2, null, null, 1, 'business:product:price', 'business:product:price', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2522 库存管理 (对应PROD_BE_004: 商品库存管理服务)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2522, '库存管理', 3, 2502, 3, null, null, 1, 'business:product:stock', 'business:product:stock', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2523 图片上传 (对应PROD_BE_005: 商品图片上传处理)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2523, '图片上传', 3, 2502, 4, null, null, 1, 'business:product:upload', 'business:product:upload', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2524 商品搜索 (对应PROD_BE_006: 商品搜索筛选接口)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2524, '商品搜索', 3, 2501, 6, null, null, 1, 'business:product:search', 'business:product:search', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 菜单配置说明
-- ========================================
-- 商品管理模块菜单总计: 12个
--   - 主菜单: 2500 (商品管理)
--   - 页面菜单: 2501 (商品列表), 2502 (商品新增)
--   - 功能点: 2510-2514 (商品列表功能), 2520-2524 (商品新增功能)
--
-- 对应开发清单模块:
--   - 模块2.1: 后端商品服务 (PROD_BE_001-006)
--   - 模块2.2: 前端商品页面 (PROD_FE_001-009)
--
-- 关键功能:
--   1. 简化的商品管理 - 在新增页面选择类型后切换表单
--   2. 不包含会员和折扣功能
--   3. 支持课程、课时包、活动三种商品类型
--   4. 动态表单配置，根据商品类型显示不同字段
-- ========================================