/*
 * 会员管理模块菜单权限配置SQL
 * 
 * @Author: Claude Code  
 * @Date: 2025-01-11
 * @Copyright: 马术俱乐部管理系统
 */

SET NAMES utf8mb4;

-- ----------------------------
-- 会员管理菜单权限配置
-- ----------------------------

-- 1. 会员管理主菜单（目录级别）
INSERT INTO `t_menu` (
    `menu_name`, 
    `menu_type`, 
    `parent_id`, 
    `sort`, 
    `path`, 
    `component`, 
    `perms_type`, 
    `api_perms`, 
    `web_perms`, 
    `icon`, 
    `context_menu_id`, 
    `frame_flag`, 
    `frame_url`, 
    `cache_flag`, 
    `visible_flag`, 
    `disabled_flag`, 
    `deleted_flag`, 
    `create_user_id`, 
    `create_time`, 
    `update_time`
) VALUES (
    '会员管理',           -- 菜单名称
    1,                   -- 菜单类型：1=目录
    0,                   -- 父级ID：0=顶级菜单
    6,                   -- 排序：在教练管理(5)之后
    '/member',           -- 路径
    'Layout',            -- 组件
    1,                   -- 权限类型：1=前后端都需要
    '',                  -- 后端权限标识（目录级别为空）
    'club:member',       -- 前端权限标识
    'TeamOutlined',      -- 图标
    NULL,                -- 右键菜单ID
    0,                   -- 是否为iframe：0=否
    '',                  -- iframe地址
    0,                   -- 是否缓存：0=否  
    1,                   -- 是否可见：1=是
    0,                   -- 是否禁用：0=否
    0,                   -- 是否删除：0=否
    1,                   -- 创建人ID：1=admin
    NOW(),               -- 创建时间
    NOW()                -- 更新时间
);

-- 获取刚插入的会员管理主菜单ID
SET @member_menu_id = LAST_INSERT_ID();

-- 2. 会员列表菜单（页面级别）
INSERT INTO `t_menu` (
    `menu_name`, 
    `menu_type`, 
    `parent_id`, 
    `sort`, 
    `path`, 
    `component`, 
    `perms_type`, 
    `api_perms`, 
    `web_perms`, 
    `icon`, 
    `context_menu_id`, 
    `frame_flag`, 
    `frame_url`, 
    `cache_flag`, 
    `visible_flag`, 
    `disabled_flag`, 
    `deleted_flag`, 
    `create_user_id`, 
    `create_time`, 
    `update_time`
) VALUES (
    '会员列表',                              -- 菜单名称
    2,                                      -- 菜单类型：2=菜单
    @member_menu_id,                        -- 父级ID：会员管理主菜单
    1,                                      -- 排序
    '/member/list',                         -- 路径
    '/business/member/member-list.vue',     -- 组件路径
    1,                                      -- 权限类型：1=前后端都需要
    'club:member:query',                    -- 后端权限标识
    'club:member:query',                    -- 前端权限标识
    'UserOutlined',                         -- 图标
    NULL,                                   -- 右键菜单ID
    0,                                      -- 是否为iframe：0=否
    '',                                     -- iframe地址
    1,                                      -- 是否缓存：1=是
    1,                                      -- 是否可见：1=是
    0,                                      -- 是否禁用：0=否
    0,                                      -- 是否删除：0=否
    1,                                      -- 创建人ID：1=admin
    NOW(),                                  -- 创建时间
    NOW()                                   -- 更新时间
);

-- 获取会员列表菜单ID
SET @member_list_menu_id = LAST_INSERT_ID();

-- 3. 会员详情页面（隐藏菜单，用于路由注册）
INSERT INTO `t_menu` (
    `menu_name`, 
    `menu_type`, 
    `parent_id`, 
    `sort`, 
    `path`, 
    `component`, 
    `perms_type`, 
    `api_perms`, 
    `web_perms`, 
    `icon`, 
    `context_menu_id`, 
    `frame_flag`, 
    `frame_url`, 
    `cache_flag`, 
    `visible_flag`, 
    `disabled_flag`, 
    `deleted_flag`, 
    `create_user_id`, 
    `create_time`, 
    `update_time`
) VALUES (
    '会员详情',                              -- 菜单名称
    2,                                      -- 菜单类型：2=菜单
    @member_list_menu_id,                   -- 父级ID：会员列表菜单
    1,                                      -- 排序
    '/member/detail/:memberId',             -- 路径（带参数）
    '/business/member/member-detail.vue',   -- 组件路径
    1,                                      -- 权限类型：1=前后端都需要
    'club:member:detail',                   -- 后端权限标识
    'club:member:detail',                   -- 前端权限标识
    '',                                     -- 图标（隐藏菜单无需图标）
    NULL,                                   -- 右键菜单ID
    0,                                      -- 是否为iframe：0=否
    '',                                     -- iframe地址
    1,                                      -- 是否缓存：1=是
    0,                                      -- 是否可见：0=否（隐藏菜单）
    0,                                      -- 是否禁用：0=否
    0,                                      -- 是否删除：0=否
    1,                                      -- 创建人ID：1=admin
    NOW(),                                  -- 创建时间
    NOW()                                   -- 更新时间
);

-- 4. 会员管理相关功能权限（按钮级别）
-- 4.1 新增会员
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '新增会员', 3, @member_list_menu_id, 1, '', '', 
    1, 'club:member:add', 'club:member:add',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.2 编辑会员
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '编辑会员', 3, @member_list_menu_id, 2, '', '', 
    1, 'club:member:update', 'club:member:update',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.3 删除会员
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '删除会员', 3, @member_list_menu_id, 3, '', '', 
    1, 'club:member:delete', 'club:member:delete',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.4 启用/禁用会员
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '启用/禁用会员', 3, @member_list_menu_id, 4, '', '', 
    1, 'club:member:update-status', 'club:member:update-status',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.5 重置密码
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '重置密码', 3, @member_list_menu_id, 5, '', '', 
    1, 'club:member:reset-password', 'club:member:reset-password',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.6 家庭成员管理
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '家庭成员管理', 3, @member_list_menu_id, 6, '', '', 
    1, 'club:member:family', 'club:member:family',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.7 会籍管理
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '会籍管理', 3, @member_list_menu_id, 7, '', '', 
    1, 'club:member:membership', 'club:member:membership',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.8 导出会员数据
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '导出会员数据', 3, @member_list_menu_id, 8, '', '', 
    1, 'club:member:export', 'club:member:export',
    1, 0, 0, 1, NOW(), NOW()
);

-- 4.9 批量操作
INSERT INTO `t_menu` (
    `menu_name`, `menu_type`, `parent_id`, `sort`, `path`, `component`, 
    `perms_type`, `api_perms`, `web_perms`, 
    `visible_flag`, `disabled_flag`, `deleted_flag`, 
    `create_user_id`, `create_time`, `update_time`
) VALUES (
    '批量操作', 3, @member_list_menu_id, 9, '', '', 
    1, 'club:member:batch', 'club:member:batch',
    1, 0, 0, 1, NOW(), NOW()
);

-- ----------------------------
-- 为超级管理员分配会员管理权限
-- ----------------------------

-- 查询超级管理员角色ID（假设角色名为'超级管理员'或'admin'）
SET @admin_role_id = (SELECT role_id FROM t_role WHERE role_name IN ('超级管理员', 'admin', 'Admin') LIMIT 1);

-- 如果没有找到，使用ID为1的角色（通常是管理员角色）
SET @admin_role_id = IFNULL(@admin_role_id, 1);

-- 获取所有新添加的会员管理菜单ID
SET @menu_ids = (
    SELECT GROUP_CONCAT(menu_id) 
    FROM t_menu 
    WHERE menu_name IN (
        '会员管理', '会员列表', '会员详情', 
        '新增会员', '编辑会员', '删除会员', '启用/禁用会员', 
        '重置密码', '家庭成员管理', '会籍管理', '导出会员数据', '批量操作'
    ) 
    AND create_time >= DATE_SUB(NOW(), INTERVAL 1 MINUTE)
);

-- 为管理员角色分配会员管理相关权限
INSERT INTO `t_role_menu` (`role_id`, `menu_id`, `create_user_id`, `create_time`, `update_time`)
SELECT @admin_role_id, menu_id, 1, NOW(), NOW()
FROM t_menu 
WHERE menu_name IN (
    '会员管理', '会员列表', '会员详情', 
    '新增会员', '编辑会员', '删除会员', '启用/禁用会员', 
    '重置密码', '家庭成员管理', '会籍管理', '导出会员数据', '批量操作'
) 
AND create_time >= DATE_SUB(NOW(), INTERVAL 1 MINUTE);

-- 输出结果信息
SELECT 
    CONCAT('✅ 会员管理菜单权限配置完成，共添加 ', COUNT(*), ' 个菜单项') AS result
FROM t_menu 
WHERE menu_name IN (
    '会员管理', '会员列表', '会员详情', 
    '新增会员', '编辑会员', '删除会员', '启用/禁用会员', 
    '重置密码', '家庭成员管理', '会籍管理', '导出会员数据', '批量操作'
) 
AND create_time >= DATE_SUB(NOW(), INTERVAL 1 MINUTE);