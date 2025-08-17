-- 课表管理模块权限补充SQL
-- 基于已实现的后端API功能，补充具体的操作权限

-- 课表详情权限 
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2722, '课表详情', 3, 2601, 8, NULL, NULL, 1, 'business:schedule:detail', 'business:schedule:detail', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 批量更新课表时间权限（对应batchUpdateTime接口）
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2723, '批量更新时间', 3, 2601, 9, NULL, NULL, 1, 'business:schedule:batchUpdateTime', 'business:schedule:batchUpdateTime', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 获取教练列表权限（对应getCoachList接口）
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2724, '获取教练列表', 3, 2601, 10, NULL, NULL, 1, 'business:schedule:coaches', 'business:schedule:coaches', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 检查时间冲突权限（对应checkConflict接口）
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2725, '检查冲突详情', 3, 2601, 11, NULL, NULL, 1, 'business:schedule:checkConflict', 'business:schedule:checkConflict', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 获取推荐时间段权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2726, '获取推荐时间段', 3, 2601, 12, NULL, NULL, 1, 'business:schedule:recommendedTimeSlots', 'business:schedule:recommendedTimeSlots', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 获取资源状态权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2727, '获取资源状态', 3, 2601, 13, NULL, NULL, 1, 'business:schedule:resourceStatus', 'business:schedule:resourceStatus', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 解决冲突权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2728, '解决冲突', 3, 2601, 14, NULL, NULL, 1, 'business:schedule:resolveConflict', 'business:schedule:resolveConflict', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 更新课表时间权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2729, '更新课表时间', 3, 2601, 15, NULL, NULL, 1, 'business:schedule:updateTime', 'business:schedule:updateTime', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 开始上课权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2730, '开始上课', 3, 2601, 16, NULL, NULL, 1, 'business:schedule:start', 'business:schedule:start', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 结束上课权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2731, '结束上课', 3, 2601, 17, NULL, NULL, 1, 'business:schedule:finish', 'business:schedule:finish', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 取消课程权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2732, '取消课程', 3, 2601, 18, NULL, NULL, 1, 'business:schedule:cancel', 'business:schedule:cancel', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 删除课表权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (2733, '删除课表', 3, 2601, 19, NULL, NULL, 1, 'business:schedule:delete', 'business:schedule:delete', NULL, 2601, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());