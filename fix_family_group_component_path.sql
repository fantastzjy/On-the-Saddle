-- =====================================================
-- 修正家庭组菜单组件路径的更新脚本
-- =====================================================

-- 更新家庭组列表菜单的组件路径，添加.vue扩展名
UPDATE t_menu 
SET component = '/business/family-group/family-group-list.vue'
WHERE menu_name = '家庭组列表' 
  AND path = 'list' 
  AND component = '/business/family-group/family-group-list'
  AND deleted_flag = 0;

-- 更新家庭组详情菜单的组件路径，添加.vue扩展名  
UPDATE t_menu 
SET component = '/business/family-group/family-group-detail.vue'
WHERE menu_name = '家庭组详情' 
  AND path = 'detail/:id' 
  AND component = '/business/family-group/family-group-detail'
  AND deleted_flag = 0;

-- 验证更新结果
SELECT menu_id, menu_name, path, component 
FROM t_menu 
WHERE menu_name IN ('家庭组列表', '家庭组详情')
  AND deleted_flag = 0;