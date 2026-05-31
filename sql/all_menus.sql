-- =============================================
-- 物资管理系统完整菜单权限配置
-- =============================================

-- ============================
-- 1. 仓库管理模块
-- ============================
-- 仓库管理目录
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库管理', 0, 1, 'warehouse', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'warehouse', 'admin', sysdate(), '', NULL, '仓库管理目录');

SET @warehouseMenuId = LAST_INSERT_ID();

-- 仓库信息菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库信息', @warehouseMenuId, 1, 'warehouse', 'warehouse/warehouse/index', NULL, 1, 0, 'C', '0', '0', 'warehouse:info:list', 'warehouse', 'admin', sysdate(), '', NULL, '仓库信息菜单');

SET @warehouseInfoMenuId = LAST_INSERT_ID();

-- 仓库信息按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库查询', @warehouseInfoMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库新增', @warehouseInfoMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库修改', @warehouseInfoMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库删除', @warehouseInfoMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('仓库导出', @warehouseInfoMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:info:export', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 2. 物资管理模块
-- ============================
-- 物资管理目录
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资管理', 0, 2, 'material', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'material', 'admin', sysdate(), '', NULL, '物资管理目录');

SET @materialMenuId = LAST_INSERT_ID();

-- 物资分类菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资分类', @materialMenuId, 1, 'category', 'warehouse/materialCategory/index', NULL, 1, 0, 'C', '0', '0', 'material:category:list', 'category', 'admin', sysdate(), '', NULL, '物资分类菜单');

SET @materialCategoryMenuId = LAST_INSERT_ID();

-- 物资分类按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类查询', @materialCategoryMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:category:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类新增', @materialCategoryMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:category:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类修改', @materialCategoryMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:category:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('分类删除', @materialCategoryMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:category:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 物资档案菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资档案', @materialMenuId, 2, 'material', 'warehouse/material/index', NULL, 1, 0, 'C', '0', '0', 'material:info:list', 'list', 'admin', sysdate(), '', NULL, '物资档案菜单');

SET @materialInfoMenuId = LAST_INSERT_ID();

-- 物资档案按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资查询', @materialInfoMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资新增', @materialInfoMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资修改', @materialInfoMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资删除', @materialInfoMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资导出', @materialInfoMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'material:info:export', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 3. 入库管理模块（已有SQL，这里补充为完整结构）
-- ============================
-- 入库管理目录
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库管理', 0, 3, 'stockIn', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'shopping', 'admin', sysdate(), '', NULL, '入库管理目录');

SET @stockInMenuId = LAST_INSERT_ID();

-- 入库单管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库单管理', @stockInMenuId, 1, 'stockIn', 'warehouse/stockIn/index', NULL, 1, 0, 'C', '0', '0', 'warehouse:stockIn:list', 'list', 'admin', sysdate(), '', NULL, '入库单管理菜单');

SET @stockInManageMenuId = LAST_INSERT_ID();

-- 入库单管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库查询', @stockInManageMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:stockIn:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库新增', @stockInManageMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:stockIn:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库修改', @stockInManageMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:stockIn:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库删除', @stockInManageMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:stockIn:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库导出', @stockInManageMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:stockIn:export', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库审核', @stockInManageMenuId, 6, '#', '', NULL, 1, 0, 'F', '0', '0', 'warehouse:stockIn:audit', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 4. 资产管理模块
-- ============================
-- 资产管理目录
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('资产管理', 0, 4, 'asset', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'asset', 'admin', sysdate(), '', NULL, '资产管理目录');

SET @assetMenuId = LAST_INSERT_ID();

-- 资产流转记录菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('资产流转', @assetMenuId, 1, 'record', 'asset/record/index', NULL, 1, 0, 'C', '0', '0', 'asset:record:list', 'record', 'admin', sysdate(), '', NULL, '资产流转记录菜单');

SET @assetRecordMenuId = LAST_INSERT_ID();

-- 资产流转按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('流转查询', @assetRecordMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:record:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('流转新增', @assetRecordMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:record:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('流转修改', @assetRecordMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:record:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('流转删除', @assetRecordMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:record:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('流转导出', @assetRecordMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:record:export', '#', 'admin', sysdate(), '', NULL, '');

-- 资产变更记录菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('资产变更', @assetMenuId, 2, 'change', 'asset/change/index', NULL, 1, 0, 'C', '0', '0', 'asset:change:list', 'change', 'admin', sysdate(), '', NULL, '资产变更记录菜单');

SET @assetChangeMenuId = LAST_INSERT_ID();

-- 资产变更按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('变更查询', @assetChangeMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:change:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('变更新增', @assetChangeMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:change:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('变更修改', @assetChangeMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:change:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('变更删除', @assetChangeMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:change:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('变更导出', @assetChangeMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:change:export', '#', 'admin', sysdate(), '', NULL, '');

-- 二维码管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('二维码管理', @assetMenuId, 3, 'qrcode', 'asset/qrcode/index', NULL, 1, 0, 'C', '0', '0', 'asset:qrcode:list', 'qrcode', 'admin', sysdate(), '', NULL, '二维码管理菜单');

SET @qrcodeMenuId = LAST_INSERT_ID();

-- 二维码管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('二维码查询', @qrcodeMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:qrcode:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('二维码生成', @qrcodeMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:qrcode:generate', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('二维码打印', @qrcodeMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'asset:qrcode:print', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 5. 巡检管理模块
-- ============================
-- 巡检管理目录
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检管理', 0, 5, 'inspection', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'inspection', 'admin', sysdate(), '', NULL, '巡检管理目录');

SET @inspectionMenuId = LAST_INSERT_ID();

-- 巡检项目菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检项目', @inspectionMenuId, 1, 'item', 'inspection/inspectionItem/index', NULL, 1, 0, 'C', '0', '0', 'inspection:item:list', 'list', 'admin', sysdate(), '', NULL, '巡检项目菜单');

SET @inspectionItemMenuId = LAST_INSERT_ID();

-- 巡检项目按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目查询', @inspectionItemMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:item:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目新增', @inspectionItemMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:item:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目修改', @inspectionItemMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:item:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('项目删除', @inspectionItemMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:item:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 巡检记录菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检记录', @inspectionMenuId, 2, 'inspection', 'inspection/inspection/index', NULL, 1, 0, 'C', '0', '0', 'inspection:info:list', 'form', 'admin', sysdate(), '', NULL, '巡检记录菜单');

SET @inspectionInfoMenuId = LAST_INSERT_ID();

-- 巡检记录按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检查询', @inspectionInfoMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检新增', @inspectionInfoMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检修改', @inspectionInfoMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检删除', @inspectionInfoMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('巡检导出', @inspectionInfoMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:info:export', '#', 'admin', sysdate(), '', NULL, '');

-- PM巡检记录菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检记录', @inspectionMenuId, 3, 'pm-inspection', 'asset/pm-inspection/index', NULL, 1, 0, 'C', '0', '0', 'inspection:pm:list', 'inspection', 'admin', sysdate(), '', NULL, 'PM巡检记录菜单');

SET @pmInspectionMenuId = LAST_INSERT_ID();

-- PM巡检记录按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检查询', @pmInspectionMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:pm:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检新增', @pmInspectionMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:pm:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检修改', @pmInspectionMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:pm:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检删除', @pmInspectionMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:pm:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('PM巡检导出', @pmInspectionMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'inspection:pm:export', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 6. 报修管理模块
-- ============================
-- 报修管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修管理', 0, 6, 'repair', 'repair/repair/index', NULL, 1, 0, 'C', '0', '0', 'repair:info:list', 'form', 'admin', sysdate(), '', NULL, '报修管理菜单');

SET @repairMenuId = LAST_INSERT_ID();

-- 报修管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修查询', @repairMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修新增', @repairMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修修改', @repairMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修删除', @repairMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('报修导出', @repairMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'repair:info:export', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 7. 使用说明书模块
-- ============================
-- 使用说明书菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('使用说明书', 0, 7, 'manual', 'manual/manual/index', NULL, 1, 0, 'C', '0', '0', 'manual:info:list', 'documentation', 'admin', sysdate(), '', NULL, '使用说明书菜单');

SET @manualMenuId = LAST_INSERT_ID();

-- 使用说明书按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('说明书查询', @manualMenuId, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'manual:info:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('说明书新增', @manualMenuId, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'manual:info:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('说明书修改', @manualMenuId, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'manual:info:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('说明书删除', @manualMenuId, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'manual:info:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('说明书导出', @manualMenuId, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'manual:info:export', '#', 'admin', sysdate(), '', NULL, '');

-- ============================
-- 8. 为admin角色分配所有菜单权限
-- ============================
-- 获取admin角色ID（假设为1）
SET @adminRoleId = 1;

-- 插入角色菜单关联（使用INSERT IGNORE避免重复）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT @adminRoleId, menu_id FROM sys_menu WHERE menu_id >= @warehouseMenuId;

-- 完成提示
SELECT '菜单权限配置完成！' AS message;
