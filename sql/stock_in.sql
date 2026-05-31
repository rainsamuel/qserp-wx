-- =============================================
-- 物资入库功能数据库脚本
-- =============================================

-- 1. 物资表增加保修期限字段
ALTER TABLE biz_material ADD COLUMN warranty_period INT DEFAULT NULL COMMENT '保修期限（天）' AFTER supplier;

-- 2. 入库单主表
DROP TABLE IF EXISTS biz_stock_in;
CREATE TABLE biz_stock_in (
    stock_in_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '入库单ID',
    stock_in_no VARCHAR(64) NOT NULL COMMENT '入库单号',
    supplier VARCHAR(200) DEFAULT NULL COMMENT '供应商',
    invoice_no VARCHAR(100) DEFAULT NULL COMMENT '发票号',
    invoice_date DATE DEFAULT NULL COMMENT '发票日期',
    invoice_amount DECIMAL(14,2) DEFAULT NULL COMMENT '发票金额',
    warehouse_id BIGINT DEFAULT NULL COMMENT '入库仓库ID',
    total_amount DECIMAL(14,2) DEFAULT NULL COMMENT '入库总金额',
    total_quantity INT DEFAULT 0 COMMENT '入库总数量',
    in_date DATE DEFAULT NULL COMMENT '入库日期',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0待审核 1已审核 2已驳回）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    audit_by VARCHAR(64) DEFAULT NULL COMMENT '审核人',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    audit_remark VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
    PRIMARY KEY (stock_in_id),
    UNIQUE KEY uk_stock_in_no (stock_in_no),
    KEY idx_in_date (in_date),
    KEY idx_status (status),
    KEY idx_warehouse_id (warehouse_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='入库单主表';

-- 3. 入库明细表
DROP TABLE IF EXISTS biz_stock_in_detail;
CREATE TABLE biz_stock_in_detail (
    detail_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    stock_in_id BIGINT NOT NULL COMMENT '入库单ID',
    material_id BIGINT DEFAULT NULL COMMENT '物资ID（已有物资）',
    material_code VARCHAR(64) DEFAULT NULL COMMENT '物资编码',
    material_name VARCHAR(200) NOT NULL COMMENT '物资名称',
    category_id BIGINT DEFAULT NULL COMMENT '分类ID',
    spec VARCHAR(200) DEFAULT NULL COMMENT '规格型号',
    unit VARCHAR(50) DEFAULT NULL COMMENT '计量单位',
    quantity INT NOT NULL DEFAULT 0 COMMENT '入库数量',
    unit_price DECIMAL(12,2) DEFAULT NULL COMMENT '单价',
    amount DECIMAL(14,2) DEFAULT NULL COMMENT '金额',
    batch_no VARCHAR(100) DEFAULT NULL COMMENT '批次号',
    production_date DATE DEFAULT NULL COMMENT '生产日期',
    expiry_date DATE DEFAULT NULL COMMENT '过期日期',
    warranty_period INT DEFAULT NULL COMMENT '保修期限（天）',
    asset_code VARCHAR(100) DEFAULT NULL COMMENT '资产编码',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (detail_id),
    KEY idx_stock_in_id (stock_in_id),
    KEY idx_material_id (material_id),
    KEY idx_material_code (material_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='入库明细表';

-- 4. 入库菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物资入库', 0, 5, 'stockIn', NULL, 1, 0, 'M', '0', '0', NULL, 'shopping', 'admin', sysdate(), '', NULL, '物资入库菜单');

-- 获取入库菜单ID
SET @stockInMenuId = LAST_INSERT_ID();

-- 入库单列表菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库管理', @stockInMenuId, 1, 'stockInManage', 'warehouse/stockIn/index', 1, 0, 'C', '0', '0', 'warehouse:stockIn:list', 'list', 'admin', sysdate(), '', NULL, '入库管理菜单');

SET @stockInManageMenuId = LAST_INSERT_ID();

-- 入库管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库查询', @stockInManageMenuId, 1, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockIn:query', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库新增', @stockInManageMenuId, 2, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockIn:add', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库修改', @stockInManageMenuId, 3, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockIn:edit', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库删除', @stockInManageMenuId, 4, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockIn:remove', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库导出', @stockInManageMenuId, 5, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockIn:export', '#', 'admin', sysdate(), '', NULL, '');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('入库审核', @stockInManageMenuId, 6, '#', '', 1, 0, 'F', '0', '0', 'warehouse:stockIn:audit', '#', 'admin', sysdate(), '', NULL, '');
