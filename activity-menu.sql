-- 活动管理菜单初始化脚本
-- 菜单ID从2750开始（按照用户提供的最大ID 2749）

-- 一级菜单：活动管理 (目录类型)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, api_perms, perms_type, web_perms, icon, context_menu_id, deleted_flag, create_time, create_user_id, update_time, update_user_id) 
VALUES (2750, '活动管理', 1, 0, 150, '/activity', NULL, 0, NULL, 0, 1, 0, NULL, 1, 'business:activity', 'calendar', NULL, 0, NOW(), 1, NOW(), 1);

-- 二级菜单：活动发布 (菜单类型)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, api_perms, perms_type, web_perms, icon, context_menu_id, deleted_flag, create_time, create_user_id, update_time, update_user_id)
VALUES (2751, '活动发布', 2, 2750, 1, '/activity/publish', 'business/activity/ActivityPublish', 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:publish', 'plus-circle', NULL, 0, NOW(), 1, NOW(), 1);

-- 二级菜单：活动列表 (菜单类型) 
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, api_perms, perms_type, web_perms, icon, context_menu_id, deleted_flag, create_time, create_user_id, update_time, update_user_id)
VALUES (2752, '活动列表', 2, 2750, 2, '/activity/list', 'business/activity/ActivityList', 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:query', 'unordered-list', NULL, 0, NOW(), 1, NOW(), 1);

-- 功能点权限
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, api_perms, perms_type, web_perms, icon, context_menu_id, deleted_flag, create_time, create_user_id, update_time, update_user_id)
VALUES 
(2753, '活动新增', 3, 2752, 1, NULL, NULL, 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:add', NULL, 2752, 0, NOW(), 1, NOW(), 1),
(2754, '活动编辑', 3, 2752, 2, NULL, NULL, 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:update', NULL, 2752, 0, NOW(), 1, NOW(), 1),
(2755, '活动删除', 3, 2752, 3, NULL, NULL, 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:delete', NULL, 2752, 0, NOW(), 1, NOW(), 1),
(2756, '活动详情', 3, 2752, 4, NULL, NULL, 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:detail', NULL, 2752, 0, NOW(), 1, NOW(), 1),
(2757, '活动状态管理', 3, 2752, 5, NULL, NULL, 0, NULL, 0, 1, 0, NULL, 1, 'business:activity:status', NULL, 2752, 0, NOW(), 1, NOW(), 1);

-- 为超级管理员角色分配活动管理权限（假设超级管理员角色ID为1）
INSERT INTO t_role_menu (role_id, menu_id) 
SELECT 1, menu_id FROM t_menu WHERE menu_id IN (2750, 2751, 2752, 2753, 2754, 2755, 2756, 2757);

-- 提交事务
COMMIT;