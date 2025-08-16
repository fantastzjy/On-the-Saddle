-- ========================================
-- 马术俱乐部商品订单管理系统 - 菜单SQL v1.0
-- 基于现有菜单体系扩展，新增商品管理和课表管理模块
-- ========================================

-- ========================================
-- 商品管理模块菜单 (2500-2599)
-- ========================================

-- 2500 商品管理主菜单
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2500, '商品管理', 1, 0, 250, '/product', null, 1, '', 'product', 'ShoppingOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2501 商品列表
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2501, '商品列表', 2, 2500, 1, '/product/list', '/views/business/product/ProductList.vue', 1, 'business:product:query', 'business:product:query', 'UnorderedListOutlined', null, 0, null, 1, 1, 0, 0, 1, now(), 1, now());

-- 2502 新增商品
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2502, '新增商品', 2, 2500, 2, '/product/add', '/views/business/product/ProductAdd.vue', 1, 'business:product:add', 'business:product:add', 'PlusOutlined', null, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 商品列表功能点
-- 2510 查看详情
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2510, '查看详情', 3, 2501, 1, null, null, 1, 'business:product:detail', 'business:product:detail', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2511 编辑商品
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2511, '编辑商品', 3, 2501, 2, null, null, 1, 'business:product:update', 'business:product:update', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2512 删除商品
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2512, '删除商品', 3, 2501, 3, null, null, 1, 'business:product:delete', 'business:product:delete', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2513 上架下架
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2513, '上架下架', 3, 2501, 4, null, null, 1, 'business:product:status', 'business:product:status', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2514 批量操作
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2514, '批量操作', 3, 2501, 5, null, null, 1, 'business:product:batch', 'business:product:batch', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2515 价格计算
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2515, '价格计算', 3, 2501, 6, null, null, 1, 'business:product:price', 'business:product:price', null, 2501, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 新增商品功能点
-- 2520 获取表单配置
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2520, '获取表单配置', 3, 2502, 1, null, null, 1, 'business:product:form:config', 'business:product:form:config', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2521 表单验证
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2521, '表单验证', 3, 2502, 2, null, null, 1, 'business:product:form:validate', 'business:product:form:validate', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- 2522 文件上传
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, perms_type, api_perms, web_perms, icon, context_menu_id, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, deleted_flag, create_user_id, create_time, update_user_id, update_time) 
VALUES (2522, '文件上传', 3, 2502, 3, null, null, 1, 'business:product:file:upload', 'business:product:file:upload', null, 2502, 0, null, 0, 1, 0, 0, 1, now(), 1, now());

-- ========================================
-- 课表管理模块菜单 (2600-2699)
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
-- 菜单类型说明
-- ========================================
-- menu_type: 1=目录, 2=菜单, 3=功能点
-- perms_type: 1=授权
-- frame_flag: 0=否, 1=是
-- cache_flag: 0=不缓存, 1=缓存
-- visible_flag: 0=隐藏, 1=显示
-- disabled_flag: 0=启用, 1=禁用
-- deleted_flag: 0=未删除, 1=已删除

-- ========================================
-- 权限字符串说明
-- ========================================
-- 商品管理权限前缀: business:product:
-- 课表管理权限前缀: business:schedule:
-- 预约管理权限前缀: business:booking:
-- 
-- 基础权限: query(查询), add(新增), update(修改), delete(删除)
-- 特殊权限: 
--   - status(状态管理), batch(批量操作), price(价格计算)
--   - auto(智能排课), manual(手动排课), drag(拖拽调整)
--   - conflict(冲突检测), recommend(推荐时段), checkin(前台核销)

-- ========================================
-- 菜单总结
-- ========================================
-- 商品管理模块: 
--   - 主菜单: 2500
--   - 页面菜单: 2501-2502 (2个)
--   - 功能点: 2510-2522 (13个)
--   - 小计: 16个菜单
--
-- 课表管理模块:
--   - 主菜单: 2600  
--   - 页面菜单: 2601-2602 (2个)
--   - 功能点: 2610-2624 (15个)
--   - 小计: 18个菜单
--
-- 总计: 34个菜单项

-- ========================================
-- 执行说明
-- ========================================
-- 1. 请在执行前备份菜单表
-- 2. 确认menu_id不与现有菜单冲突
-- 3. 根据实际需求调整权限配置
-- 4. 执行完成后重启应用刷新菜单缓存