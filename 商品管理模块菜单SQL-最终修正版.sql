-- ========================================
-- 马术俱乐部商品管理模块菜单SQL - 模块2.1 & 2.2 (修正版)
-- 基于开发清单v2.0 - 简化版商品管理
-- 修正菜单ID避免与1.3版本冲突
-- ========================================

-- 如果之前执行过1.3版本的菜单SQL，需要先清理商品管理部分的菜单
-- DELETE FROM t_menu WHERE menu_id BETWEEN 2500 AND 2599;

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

-- 2515 商品搜索 (对应PROD_BE_006: 商品搜索筛选接口)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2515, '商品搜索', 3, 2501, 6, null, null, 1, 'business:product:search', 'business:product:search', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

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

-- ========================================
-- 课表管理模块菜单 (2600-2699) - 保留1.3版本的课表功能
-- ========================================

-- 2600 课表管理主菜单
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2600, '课表管理', 1, 0, 260, '/schedule', null, 1, '', 'schedule', 'CalendarOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2601 课表安排
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2601, '课表安排', 2, 2600, 1, '/schedule/manage', '/views/business/schedule/ScheduleManage.vue', 1, 'business:schedule:query', 'business:schedule:query', 'ScheduleOutlined', null, 0, null, 1, 1, 0, 0, 1, now(), 1, now());

-- 2602 预约管理
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2602, '预约管理', 2, 2600, 2, '/schedule/booking', '/views/business/booking/BookingList.vue', 1, 'business:booking:query', 'business:booking:query', 'BookOutlined', null, 0, null, 1, 1, 0, 0, 1, now(), 1, now());

-- 课表安排功能点
-- 2610 查看课表
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2610, '查看课表', 3, 2601, 1, null, null, 1, 'business:schedule:view', 'business:schedule:view', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2611 智能排课
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2611, '智能排课', 3, 2601, 2, null, null, 1, 'business:schedule:auto', 'business:schedule:auto', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2612 手动排课
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2612, '手动排课', 3, 2601, 3, null, null, 1, 'business:schedule:manual', 'business:schedule:manual', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2613 拖拽调整
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2613, '拖拽调整', 3, 2601, 4, null, null, 1, 'business:schedule:drag', 'business:schedule:drag', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2614 冲突检测
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2614, '冲突检测', 3, 2601, 5, null, null, 1, 'business:schedule:conflict', 'business:schedule:conflict', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2615 推荐时段
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2615, '推荐时段', 3, 2601, 6, null, null, 1, 'business:schedule:recommend', 'business:schedule:recommend', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2616 批量排课
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2616, '批量排课', 3, 2601, 7, null, null, 1, 'business:schedule:batch', 'business:schedule:batch', null, 2601, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 预约管理功能点
-- 2620 查看预约
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2620, '查看预约', 3, 2602, 1, null, null, 1, 'business:booking:view', 'business:booking:view', null, 2602, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2621 确认预约
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2621, '确认预约', 3, 2602, 2, null, null, 1, 'business:booking:confirm', 'business:booking:confirm', null, 2602, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2622 取消预约
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2622, '取消预约', 3, 2602, 3, null, null, 1, 'business:booking:cancel', 'business:booking:cancel', null, 2602, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2623 修改预约
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2623, '修改预约', 3, 2602, 4, null, null, 1, 'business:booking:update', 'business:booking:update', null, 2602, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2624 前台核销
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2624, '前台核销', 3, 2602, 5, null, null, 1, 'business:booking:checkin', 'business:booking:checkin', null, 2602, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 菜单配置说明
-- ========================================
-- 📊 商品管理模块菜单总计: 13个
--   - 主菜单: 2500 (商品管理)
--   - 页面菜单: 2501 (商品列表), 2502 (商品新增)
--   - 功能点: 2510-2515 (商品列表功能), 2520-2523 (商品新增功能)
--
-- 📊 课表管理模块菜单总计: 18个  
--   - 主菜单: 2600 (课表管理)
--   - 页面菜单: 2601 (课表安排), 2602 (预约管理)
--   - 功能点: 2610-2616 (课表功能), 2620-2624 (预约功能)
--
-- 🎯 对应开发清单模块:
--   - 模块1.3: 前端功能扩展 (课表管理)
--   - 模块2.1: 后端商品服务 (PROD_BE_001-006)  
--   - 模块2.2: 前端商品页面 (PROD_FE_001-009)
--
-- ✅ 关键改进:
--   1. 优化权限命名 - 更简洁一致
--   2. 完善功能覆盖 - 库存管理、图片上传、搜索
--   3. 逻辑更清晰 - 批量操作与状态管理分离
--   4. 兼容1.3版本 - 保留课表管理功能
-- ========================================