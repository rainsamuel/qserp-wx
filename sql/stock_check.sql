-- =============================================
-- 库存盘点功能数据库脚本
-- =============================================

-- 1. 盘点单主表
DROP TABLE IF EXISTS biz_stock_check;
CREATE TABLE biz_stock_check (
    check_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '盘点单ID',
    check_no VARCHAR(64) NOT NULL COMMENT '盘点单号',
    warehouse_id BIGINT DEFAULT NULL COMMENT '盘点仓库ID',
    check_type CHAR(1) DEFAULT '0' COMMENT '盘点类型（0全盘 1抽盘）',
    check_date DATE DEFAULT NULL COMMENT '盘点日期',
    checker VARCHAR(64) DEFAULT NULL COMMENT '盘点人',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0盘点中 1已完成 2已作废）',
    total_diff_amount DECIMAL(14,2) DEFAULT 0.00 COMMENT '差异总金额',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (check_id),
    UNIQUE KEY uk_check_no (check_no),
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_status (status),
    KEY idx_check_date (check_date)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='盘点单主表';

-- 2. 盘点明细表
DROP TABLE IF EXISTS biz_stock_check_detail;
CREATE TABLE biz_stock_check_detail (
    detail_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    check_id BIGINT NOT NULL COMMENT '盘点单ID',
    material_id BIGINT DEFAULT NULL COMMENT '物资ID',
    material_code VARCHAR(64) DEFAULT NULL COMMENT '物资编码',
    material_name VARCHAR(200) DEFAULT NULL COMMENT '物资名称',
    category_id BIGINT DEFAULT NULL COMMENT '分类ID',
    spec VARCHAR(200) DEFAULT NULL COMMENT '规格型号',
    unit VARCHAR(50) DEFAULT NULL COMMENT '计量单位',
    system_quantity INT DEFAULT 0 COMMENT '系统库存数量',
    actual_quantity INT DEFAULT NULL COMMENT '实际盘点数量',
    diff_quantity INT DEFAULT 0 COMMENT '差异数量',
    unit_price DECIMAL(12,2) DEFAULT NULL COMMENT '单价',
    diff_amount DECIMAL(14,2) DEFAULT 0.00 COMMENT '差异金额',
    diff_reason VARCHAR(200) DEFAULT NULL COMMENT '差异原因',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (detail_id),
    KEY idx_check_id (check_id),
    KEY idx_material_id (material_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='盘点明细表';

-- 3. 盘点菜单（挂在仓库管理下）
-- 假设仓库管理菜单ID为（请根据实际数据库查询确认）
-- SELECT menu_id FROM sys_menu WHERE menu_name = '仓库管理' AND menu_type = 'M'

-- 盘点管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('库存盘点',
  (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '仓库管理' AND menu_type = 'M' LIMIT 1) AS t),
  6, 'stockCheck', 'warehouse/stockCheck/index', 1, 0, 'C', '0', '0', 'warehouse:stockCheck:list', 'check', 'admin', sysdate());

SET @stockCheckMenuId = LAST_INSERT_ID();

-- 盘点管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('盘点查询', @stockCheckMenuId, 1, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockCheck:query', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('盘点新增', @stockCheckMenuId, 2, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockCheck:add', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('盘点修改', @stockCheckMenuId, 3, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockCheck:edit', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('盘点删除', @stockCheckMenuId, 4, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockCheck:remove', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('盘点导出', @stockCheckMenuId, 5, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockCheck:export', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('完成盘点', @stockCheckMenuId, 6, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockCheck:complete', '#', 'admin', sysdate());
