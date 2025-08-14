-- =====================================================
-- 修正家庭组详情页面路由路径配置
-- =====================================================

-- 更新家庭组详情菜单的路径配置
-- 从相对路径 'detail/:id' 改为绝对路径 '/family-group/detail/:id'
UPDATE t_menu 
SET path = '/family-group/detail/:id'
WHERE menu_name = '家庭组详情' 
  AND path = 'detail/:id' 
  AND component = '/business/family-group/family-group-detail.vue'
  AND deleted_flag = 0;

-- 验证更新结果
SELECT menu_id, menu_name, path, component, parent_id
FROM t_menu 
WHERE menu_name IN ('家庭管理', '家庭组列表', '家庭组详情')
  AND deleted_flag = 0
ORDER BY parent_id, sort;