-- ========================================
-- 马术俱乐部商品管理系统 - 前台+后台完整菜单SQL
-- 基于1.3版本(前台)扩展，新增2.1/2.2版本(后台管理)
-- 避免ID冲突，后台管理使用2700-2799区间
-- ========================================

-- ========================================
-- 后台商品管理模块菜单 (2700-2799) - 新增部分
-- ========================================

-- 2700 后台商品管理主菜单
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2700, '后台商品管理', 1, 0, 270, '/admin/product', null, 1, '', 'admin:product', 'SettingOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2701 商品管理面板 (对应PROD_FE_001: 商品列表页面 - 后台版)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2701, '商品管理面板', 2, 2700, 1, '/admin/product/manage', '/views/business/product/ProductManage.vue', 1, 'admin:product:manage', 'admin:product:manage', 'DashboardOutlined', null, 0, null, 1, 1, 0, 0, 1, now(), 1, now());

-- 2702 商品配置管理 (对应PROD_FE_002: 智能商品新增页面 - 后台版)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2702, '商品配置管理', 2, 2700, 2, '/admin/product/config', '/views/business/product/ProductConfig.vue', 1, 'admin:product:config', 'admin:product:config', 'ToolOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 商品管理面板功能点 (对应PROD_BE_001: 商品基础CRUD接口 - 后台版)
-- ========================================

-- 2710 批量管理商品 (对应PROD_BE_001: 批量处理功能)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2710, '批量管理', 3, 2701, 1, null, null, 1, 'admin:product:batch', 'admin:product:batch', null, 2701, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2711 商品状态管理
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2711, '状态管理', 3, 2701, 2, null, null, 1, 'admin:product:status', 'admin:product:status', null, 2701, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2712 商品搜索管理 (对应PROD_BE_006: 商品搜索筛选接口)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2712, '搜索管理', 3, 2701, 3, null, null, 1, 'admin:product:search', 'admin:product:search', null, 2701, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2713 数据分析
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2713, '数据分析', 3, 2701, 4, null, null, 1, 'admin:product:analytics', 'admin:product:analytics', null, 2701, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 商品配置管理功能点 (对应PROD_BE_002-006: 动态配置和相关服务)
-- ========================================

-- 2720 动态表单配置 (对应PROD_BE_002: 商品类型动态配置接口)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2720, '表单配置', 3, 2702, 1, null, null, 1, 'admin:product:form:config', 'admin:product:form:config', null, 2702, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2721 价格策略配置 (对应PROD_BE_003: 商品价格计算服务)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2721, '价格策略', 3, 2702, 2, null, null, 1, 'admin:product:price:config', 'admin:product:price:config', null, 2702, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2722 库存策略配置 (对应PROD_BE_004: 商品库存管理服务)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2722, '库存策略', 3, 2702, 3, null, null, 1, 'admin:product:stock:config', 'admin:product:stock:config', null, 2702, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2723 上传策略配置 (对应PROD_BE_005: 商品图片上传处理)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2723, '上传策略', 3, 2702, 4, null, null, 1, 'admin:product:upload:config', 'admin:product:upload:config', null, 2702, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2724 系统参数配置
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2724, '系统参数', 3, 2702, 5, null, null, 1, 'admin:product:system:config', 'admin:product:system:config', null, 2702, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 菜单配置说明
-- ========================================
-- 📊 完整商品管理系统菜单总计: 
--   
-- 前台商品管理 (2500-2599，已执行):
--   - 主菜单: 2500 (商品管理)
--   - 页面菜单: 2501 (商品列表), 2502 (新增商品)
--   - 功能点: 2510-2522 (13个前台功能)
--   - 小计: 16个菜单
--
-- 课表管理 (2600-2699，已执行):
--   - 主菜单: 2600 (课表管理)
--   - 页面菜单: 2601 (课表安排), 2602 (预约管理)
--   - 功能点: 2610-2624 (15个课表功能)
--   - 小计: 18个菜单
--
-- 后台商品管理 (2700-2799，本次新增):
--   - 主菜单: 2700 (后台商品管理)
--   - 页面菜单: 2701 (商品管理面板), 2702 (商品配置管理)
--   - 功能点: 2710-2724 (9个后台管理功能)
--   - 小计: 12个菜单
--
-- 🎯 功能分工明确:
--   - 前台(2500-2599): 业务操作、表单处理、文件上传
--   - 课表(2600-2699): 排课管理、预约处理
--   - 后台(2700-2799): 批量管理、配置管理、数据分析
--
-- ✅ 权限体系:
--   - 前台权限前缀: business:product:
--   - 后台权限前缀: admin:product:
--   - 课表权限前缀: business:schedule: / business:booking:
--
-- 📋 执行说明:
--   1. 前台商品管理(2500-2599)和课表管理(2600-2699)已执行，无需重复
--   2. 本次只需执行后台商品管理(2700-2799)部分
--   3. 避免了所有ID冲突问题
--   4. 前台后台功能互补，权限分离
-- ========================================