-- PM巡检记录菜单
-- 请根据实际的菜单ID情况调整parent_id

-- 添加PM巡检记录菜单（假设资产管的parent_id为2000）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检记录', 0, 4, 'pm-inspection', 'asset/pm-inspection/index', NULL, 1, 0, 'C', '0', '0', 'inspection:info:list', 'inspection', 'admin', sysdate(), '', NULL, 'PM巡检记录管理菜单');

-- 获取刚插入的菜单ID
SET @menuId = LAST_INSERT_ID();

-- 添加按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检查询', @menuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检新增', @menuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检修改', @menuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检删除', @menuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检导出', @menuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:export', '#', 'admin', sysdate(), '', NULL, '');
