-- 报修管理菜单

-- 添加报修管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修管理', 0, 5, 'repair', 'repair/repair/index', NULL, 1, 0, 'C', '0', '0', 'repair:info:list', 'form', 'admin', sysdate(), '', NULL, '报修管理菜单');

-- 获取刚插入的菜单ID
SET @menuId = LAST_INSERT_ID();

-- 添加按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修查询', @menuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修新增', @menuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修修改', @menuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修删除', @menuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修导出', @menuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:export', '#', 'admin', sysdate(), '', NULL, '');
