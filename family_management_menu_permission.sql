-- =====================================================
-- 家庭管理模块菜单和权限SQL脚本
-- =====================================================

-- 1. 插入家庭管理主菜单（作为会员管理的子菜单）
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`,
    `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, 
    `deleted_flag`, `create_user_id`, `create_time`, `update_time`
) VALUES 
('家庭管理', 1, (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name LIKE '%会员%' AND menu_type = 1 AND deleted_flag = 0 LIMIT 1) AS temp), 20, '/family-group', '', 1, 'family-group:query', 'family-group:query', 'team', NULL, 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW());

-- 获取刚插入的家庭管理菜单ID
SET @family_menu_id = LAST_INSERT_ID();

-- 2. 插入家庭组列表子菜单
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`,
    `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, 
    `deleted_flag`, `create_user_id`, `create_time`, `update_time`
) VALUES 
('家庭组列表', 1, @family_menu_id, 1, 'list', '/business/family-group/family-group-list.vue', 1, 'family-group:query', 'family-group:query', '', NULL, 0, NULL, 1, 1, 0, 0, 1, NOW(), NOW()),
('家庭组详情', 1, @family_menu_id, 2, '/family-group/detail/:id', '/business/family-group/family-group-detail.vue', 1, 'family-group:detail', 'family-group:detail', '', NULL, 0, NULL, 1, 0, 0, 0, 1, NOW(), NOW());

-- 3. 插入家庭组管理相关功能点
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, `icon`, `context_menu_id`,
    `frame_flag`, `frame_url`, `cache_flag`, `visible_flag`, `disabled_flag`, 
    `deleted_flag`, `create_user_id`, `create_time`, `update_time`
) VALUES 
-- 家庭组基础功能点
('查询家庭组', 3, @family_menu_id, 1, '', '', 2, '/admin/family-group/page/query,/admin/family-group/{familyGroupId}', 'family-group:query', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组列表' AND parent_id = @family_menu_id) AS temp1), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('编辑家庭组', 3, @family_menu_id, 2, '', '', 2, '/admin/family-group/update', 'family-group:update', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组列表' AND parent_id = @family_menu_id) AS temp2), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('删除家庭组', 3, @family_menu_id, 3, '', '', 2, '/admin/family-group/batch/delete', 'family-group:delete', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组列表' AND parent_id = @family_menu_id) AS temp3), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('恢复家庭组', 3, @family_menu_id, 4, '', '', 2, '/admin/family-group/batch/restore', 'family-group:restore', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组列表' AND parent_id = @family_menu_id) AS temp4), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('导出家庭组数据', 3, @family_menu_id, 5, '', '', 2, '/admin/family-group/export', 'family-group:export', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组列表' AND parent_id = @family_menu_id) AS temp5), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

-- 家庭组详情页功能点
('查看家庭组详情', 3, @family_menu_id, 10, '', '', 2, '/admin/family-group/{familyGroupId}', 'family-group:detail', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组详情' AND parent_id = @family_menu_id) AS temp6), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

-- 家庭成员管理功能点
('管理家庭成员', 3, @family_menu_id, 20, '', '', 2, '/club/family/addMember,/club/family/joinMember', 'family-member:manage', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组详情' AND parent_id = @family_menu_id) AS temp7), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('添加家庭成员', 3, @family_menu_id, 21, '', '', 2, '/club/family/addMember,/club/family/joinMember,/club/member/page/query', 'family-member:add', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组详情' AND parent_id = @family_menu_id) AS temp8), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('移除家庭成员', 3, @family_menu_id, 22, '', '', 2, '/club/family/{familyGroupId}/member/{memberId}', 'family-member:remove', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组详情' AND parent_id = @family_menu_id) AS temp9), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW()),

('转移监护权', 3, @family_menu_id, 23, '', '', 2, '/club/family/transferGuardianship', 'family-member:transfer-guardian', '', (SELECT menu_id FROM (SELECT menu_id FROM t_menu WHERE menu_name = '家庭组详情' AND parent_id = @family_menu_id) AS temp10), 0, NULL, 0, 1, 0, 0, 1, NOW(), NOW());

-- 4. 为超级管理员角色分配家庭管理权限
-- 假设超级管理员角色ID为1，如果不是请修改
SET @admin_role_id = 1;

-- 获取所有家庭管理相关的菜单ID并分配给角色
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`)
SELECT @admin_role_id, menu_id, NOW(), NOW()
FROM `t_menu` 
WHERE (`menu_id` = @family_menu_id OR `parent_id` = @family_menu_id)
  AND `deleted_flag` = 0;

-- =====================================================
-- 验证SQL - 检查插入结果
-- =====================================================

-- 查看家庭管理模块菜单结构
SELECT 
    m1.menu_id,
    m1.menu_name,
    CASE 
        WHEN m1.menu_type = 1 THEN '菜单'
        WHEN m1.menu_type = 2 THEN '按钮'
        WHEN m1.menu_type = 3 THEN '功能点'
        ELSE '其他'
    END as menu_type_name,
    m1.path,
    m1.component,
    m1.api_perms,
    m1.web_perms,
    m1.sort,
    CASE 
        WHEN m1.visible_flag = 1 THEN '显示' 
        ELSE '隐藏' 
    END as visible_status,
    CASE 
        WHEN m1.disabled_flag = 0 THEN '启用' 
        ELSE '禁用' 
    END as status_name,
    m2.menu_name as parent_name,
    m3.menu_name as context_menu_name
FROM `t_menu` m1
LEFT JOIN `t_menu` m2 ON m1.parent_id = m2.menu_id
LEFT JOIN `t_menu` m3 ON m1.context_menu_id = m3.menu_id
WHERE (m1.menu_id = @family_menu_id OR m1.parent_id = @family_menu_id)
  AND m1.deleted_flag = 0
ORDER BY m1.parent_id, m1.sort;

-- 查看权限分配情况（针对角色ID=1）
SELECT 
    r.role_name,
    m.menu_name,
    m.api_perms,
    m.web_perms,
    CASE 
        WHEN m.menu_type = 1 THEN '菜单'
        WHEN m.menu_type = 2 THEN '按钮'  
        WHEN m.menu_type = 3 THEN '功能点'
        ELSE '其他'
    END as menu_type_name
FROM `t_role_menu` rm
JOIN `t_role` r ON rm.role_id = r.role_id
JOIN `t_menu` m ON rm.menu_id = m.menu_id
WHERE rm.role_id = @admin_role_id
  AND (m.menu_id = @family_menu_id OR m.parent_id = @family_menu_id)
  AND m.deleted_flag = 0
ORDER BY m.sort;

-- =====================================================
-- 清理SQL（如果需要删除，请取消注释并执行）
-- =====================================================
/*
-- 删除角色菜单关联
DELETE FROM t_role_menu WHERE menu_id IN (
    SELECT menu_id FROM t_menu 
    WHERE (menu_id = @family_menu_id OR parent_id = @family_menu_id)
    AND deleted_flag = 0
);

-- 软删除菜单（推荐）
UPDATE t_menu SET deleted_flag = 1, update_time = NOW() 
WHERE (menu_id = @family_menu_id OR parent_id = @family_menu_id);

-- 或者物理删除菜单（慎用）
DELETE FROM t_menu 
WHERE (menu_id = @family_menu_id OR parent_id = @family_menu_id);
*/

-- =====================================================
-- 说明文档
-- =====================================================
/*
字段说明：
- menu_type: 1=菜单, 2=按钮, 3=功能点
- perms_type: 1=前端权限, 2=后端权限, 3=前后端权限
- visible_flag: 1=显示, 0=隐藏
- disabled_flag: 0=启用, 1=禁用
- deleted_flag: 0=正常, 1=删除
- frame_flag: 0=内部页面, 1=外链
- cache_flag: 0=不缓存, 1=缓存

权限码说明：
API权限（后端接口）：
- /admin/family-group/page/query: 分页查询家庭组
- /admin/family-group/{familyGroupId}: 获取家庭组详情
- /admin/family-group/update: 更新家庭组
- /admin/family-group/batch/delete: 批量删除
- /admin/family-group/batch/restore: 批量恢复
- /club/family/addMember: 添加家庭成员
- /club/family/joinMember: 现有会员加入家庭组

Web权限（前端权限）：
- family-group:query: 查询权限
- family-group:detail: 详情权限  
- family-group:update: 编辑权限
- family-group:delete: 删除权限
- family-group:restore: 恢复权限
- family-member:manage: 成员管理权限
- family-member:add: 添加成员权限
- family-member:remove: 移除成员权限

使用说明：
1. 执行前请确保已有会员管理主菜单
2. 请根据实际情况修改 @admin_role_id 变量（超级管理员角色ID）
3. 可根据需要调整菜单排序 sort 字段
4. 家庭组详情页面默认隐藏（visible_flag=0），在列表页通过路由跳转访问
5. 功能点通过 context_menu_id 关联到具体的菜单页面
*/