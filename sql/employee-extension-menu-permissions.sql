-- 员工扩展功能菜单和权限SQL脚本
-- 包含：学历管理、工作经历管理、简历上传功能的菜单项和权限设置

SET NAMES utf8mb4;

-- ----------------------------
-- 员工扩展功能权限菜单项
-- ----------------------------

-- 学历管理权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES 
(2435, '添加学历信息', 3, 46, NULL, NULL, NULL, 1, 'system:employee:education:add', 'system:employee:education:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2436, '编辑学历信息', 3, 46, NULL, NULL, NULL, 1, 'system:employee:education:edit', 'system:employee:education:edit', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2437, '删除学历信息', 3, 46, NULL, NULL, NULL, 1, 'system:employee:education:delete', 'system:employee:education:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 工作经历管理权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES 
(2438, '添加工作经历', 3, 46, NULL, NULL, NULL, 1, 'system:employee:workExperience:add', 'system:employee:workExperience:add', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2439, '编辑工作经历', 3, 46, NULL, NULL, NULL, 1, 'system:employee:workExperience:edit', 'system:employee:workExperience:edit', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2440, '删除工作经历', 3, 46, NULL, NULL, NULL, 1, 'system:employee:workExperience:delete', 'system:employee:workExperience:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- 简历管理权限
INSERT INTO `t_menu` (`menu_id`, `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`, `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, `deleted_flag`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES 
(2441, '查询简历文件', 3, 46, NULL, NULL, NULL, 1, 'system:employee:resume:query', 'system:employee:resume:query', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2442, '上传简历文件', 3, 46, NULL, NULL, NULL, 1, 'system:employee:resume:upload', 'system:employee:resume:upload', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2443, '编辑简历信息', 3, 46, NULL, NULL, NULL, 1, 'system:employee:resume:edit', 'system:employee:resume:edit', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2444, '删除简历文件', 3, 46, NULL, NULL, NULL, 1, 'system:employee:resume:delete', 'system:employee:resume:delete', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW()),
(2445, '下载简历文件', 3, 46, NULL, NULL, NULL, 1, 'system:employee:resume:download', 'system:employee:resume:download', NULL, NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), 1, NOW());

-- ----------------------------
-- 为超级管理员角色添加权限
-- 假设role_id=1是超级管理员角色
-- ----------------------------

INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `update_time`, `create_time`) VALUES 
(1, 2435, NOW(), NOW()),
(1, 2436, NOW(), NOW()),
(1, 2437, NOW(), NOW()),
(1, 2438, NOW(), NOW()),
(1, 2439, NOW(), NOW()),
(1, 2440, NOW(), NOW()),
(1, 2441, NOW(), NOW()),
(1, 2442, NOW(), NOW()),
(1, 2443, NOW(), NOW()),
(1, 2444, NOW(), NOW()),
(1, 2445, NOW(), NOW());

-- ----------------------------
-- 说明
-- ----------------------------
/*
菜单说明：
1. menu_type = 3: 表示功能权限点，不是实际的菜单页面
2. parent_id = 46: 关联到员工管理菜单下
3. perms_type = 1: 表示后端权限控制

权限说明：
- system:employee:education:add: 添加学历信息
- system:employee:education:edit: 编辑学历信息  
- system:employee:education:delete: 删除学历信息
- system:employee:workExperience:add: 添加工作经历
- system:employee:workExperience:edit: 编辑工作经历
- system:employee:workExperience:delete: 删除工作经历
- system:employee:resume:query: 查询简历文件
- system:employee:resume:upload: 上传简历文件
- system:employee:resume:edit: 编辑简历信息
- system:employee:resume:delete: 删除简历文件
- system:employee:resume:download: 下载简历文件

这些权限对应后端Controller中的@SaCheckPermission注解值。
*/