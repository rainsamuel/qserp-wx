-- ============================
-- 仓库信息表
-- ============================
DROP TABLE IF EXISTS biz_warehouse;
CREATE TABLE biz_warehouse (
    warehouse_id    BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
    warehouse_code  VARCHAR(64)   NOT NULL COMMENT '仓库编码',
    warehouse_name  VARCHAR(100)  NOT NULL COMMENT '仓库名称',
    address         VARCHAR(255)  DEFAULT NULL COMMENT '仓库地址',
    contact_person  VARCHAR(50)   DEFAULT NULL COMMENT '联系人',
    contact_phone   VARCHAR(20)   DEFAULT NULL COMMENT '联系电话',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    source_type     VARCHAR(20)   DEFAULT 'manual' COMMENT '数据来源（manual手动 sync同步）',
    source_id       VARCHAR(64)   DEFAULT NULL COMMENT '第三方系统原始ID',
    create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (warehouse_id),
    UNIQUE KEY uk_warehouse_code (warehouse_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='仓库信息表';

-- ============================
-- 物资分类表
-- ============================
DROP TABLE IF EXISTS biz_material_category;
CREATE TABLE biz_material_category (
    category_id     BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    parent_id       BIGINT(20)    DEFAULT 0 COMMENT '父分类ID',
    category_name   VARCHAR(100)  NOT NULL COMMENT '分类名称',
    order_num       INT(4)        DEFAULT 0 COMMENT '显示顺序',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (category_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='物资分类表';

-- ============================
-- 物资信息表
-- ============================
DROP TABLE IF EXISTS biz_material;
CREATE TABLE biz_material (
    material_id     BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '物资ID',
    material_code   VARCHAR(64)   NOT NULL COMMENT '物资编码',
    material_name   VARCHAR(200)  NOT NULL COMMENT '物资名称',
    category_id     BIGINT(20)    DEFAULT NULL COMMENT '物资分类ID',
    spec            VARCHAR(200)  DEFAULT NULL COMMENT '规格型号',
    unit            VARCHAR(20)   DEFAULT NULL COMMENT '计量单位',
    stock_quantity  INT(11)       DEFAULT 0 COMMENT '库存数量',
    warehouse_id    BIGINT(20)    DEFAULT NULL COMMENT '所在仓库ID',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (material_id),
    UNIQUE KEY uk_material_code (material_code),
    KEY idx_category_id (category_id),
    KEY idx_warehouse_id (warehouse_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='物资信息表';

-- ============================
-- 初始化物资分类数据
-- ============================
INSERT INTO biz_material_category VALUES (1, 0, '办公用品', 1, '0', 'admin', sysdate(), '', NULL, '办公用品分类');
INSERT INTO biz_material_category VALUES (2, 0, '电子设备', 2, '0', 'admin', sysdate(), '', NULL, '电子设备分类');
INSERT INTO biz_material_category VALUES (3, 0, '劳保用品', 3, '0', 'admin', sysdate(), '', NULL, '劳保用品分类');

-- ============================
-- 使用说明书表
-- ============================
DROP TABLE IF EXISTS biz_manual;
CREATE TABLE biz_manual (
    manual_id       BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '说明书ID',
    manual_name     VARCHAR(200)  NOT NULL COMMENT '说明书名称',
    manual_type     VARCHAR(20)   NOT NULL COMMENT '关联类型（warehouse仓库 material物资）',
    ref_id          BIGINT(20)    NOT NULL COMMENT '关联ID（仓库ID或物资ID）',
    file_name       VARCHAR(255)  DEFAULT NULL COMMENT '存储文件名',
    original_name   VARCHAR(255)  DEFAULT NULL COMMENT '原始文件名',
    file_path       VARCHAR(500)  DEFAULT NULL COMMENT '文件路径',
    file_size       BIGINT(20)    DEFAULT 0 COMMENT '文件大小（字节）',
    file_ext        VARCHAR(20)   DEFAULT NULL COMMENT '文件扩展名',
    version         VARCHAR(50)   DEFAULT '1.0' COMMENT '版本号',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (manual_id),
    KEY idx_manual_type (manual_type),
    KEY idx_ref_id (ref_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='使用说明书表';

-- ============================
-- 巡检内容项表（预设的巡检检查项）
-- ============================
DROP TABLE IF EXISTS biz_inspection_item;
CREATE TABLE biz_inspection_item (
    item_id         BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '检查项ID',
    item_name       VARCHAR(200)  NOT NULL COMMENT '检查项名称',
    item_group      VARCHAR(50)   DEFAULT NULL COMMENT '检查项分组（如：外观检查、功能检查、安全检查）',
    sort_order      INT(4)        DEFAULT 0 COMMENT '排序',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (item_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='巡检内容项表';

-- ============================
-- 巡检记录表
-- ============================
DROP TABLE IF EXISTS biz_inspection;
CREATE TABLE biz_inspection (
    inspection_id   BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '巡检ID',
    material_id     BIGINT(20)    NOT NULL COMMENT '物资ID',
    inspector       VARCHAR(64)   NOT NULL COMMENT '巡检人',
    inspection_time DATETIME      NOT NULL COMMENT '巡检时间',
    result          VARCHAR(20)   DEFAULT 'normal' COMMENT '巡检结果（normal正常 abnormal异常）',
    status          CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time     DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by       VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time     DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (inspection_id),
    KEY idx_material_id (material_id),
    KEY idx_inspection_time (inspection_time)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='巡检记录表';

-- ============================
-- 巡检明细表（关联巡检记录与检查项）
-- ============================
DROP TABLE IF EXISTS biz_inspection_detail;
CREATE TABLE biz_inspection_detail (
    detail_id       BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    inspection_id   BIGINT(20)    NOT NULL COMMENT '巡检ID',
    item_id         BIGINT(20)    NOT NULL COMMENT '检查项ID',
    check_result    VARCHAR(20)   DEFAULT 'normal' COMMENT '检查结果（normal正常 abnormal异常）',
    check_remark    VARCHAR(500)  DEFAULT NULL COMMENT '检查备注',
    PRIMARY KEY (detail_id),
    KEY idx_inspection_id (inspection_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='巡检明细表';

-- ============================
-- 初始化巡检内容项数据
-- ============================
INSERT INTO biz_inspection_item VALUES (1, '外观是否完好', '外观检查', 1, '0', 'admin', sysdate(), '', NULL, '检查物资外观是否有损坏');
INSERT INTO biz_inspection_item VALUES (2, '标识是否清晰', '外观检查', 2, '0', 'admin', sysdate(), '', NULL, '检查物资标识标签是否清晰可读');
INSERT INTO biz_inspection_item VALUES (3, '功能是否正常', '功能检查', 1, '0', 'admin', sysdate(), '', NULL, '检查物资功能是否正常运行');
INSERT INTO biz_inspection_item VALUES (4, '配件是否齐全', '功能检查', 2, '0', 'admin', sysdate(), '', NULL, '检查物资配件是否齐全');
INSERT INTO biz_inspection_item VALUES (5, '存放环境是否达标', '安全检查', 1, '0', 'admin', sysdate(), '', NULL, '检查存放温度、湿度等环境条件');
INSERT INTO biz_inspection_item VALUES (6, '安全防护是否到位', '安全检查', 2, '0', 'admin', sysdate(), '', NULL, '检查消防器材、防护设备是否到位');

-- ============================
-- 物资信息表增加资产编码字段（用于二维码）
-- ============================
ALTER TABLE biz_material ADD COLUMN asset_code VARCHAR(128) DEFAULT NULL COMMENT '资产编码（二维码内容）' AFTER material_code;

-- ============================
-- 资产流转记录表
-- ============================
DROP TABLE IF EXISTS biz_asset_record;
CREATE TABLE biz_asset_record (
    record_id         BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    material_id       BIGINT(20)    NOT NULL COMMENT '物资ID',
    asset_code        VARCHAR(128)  DEFAULT NULL COMMENT '资产编码',
    record_type       VARCHAR(20)   NOT NULL COMMENT '流转类型（IN入库 OUT出库 DAMAGE报损 SCRAP报废）',
    quantity           INT(11)       NOT NULL DEFAULT 1 COMMENT '数量',
    operator           VARCHAR(64)   DEFAULT NULL COMMENT '操作人',
    operate_time       DATETIME      NOT NULL COMMENT '操作时间',
    from_warehouse_id  BIGINT(20)    DEFAULT NULL COMMENT '来源仓库ID',
    to_warehouse_id    BIGINT(20)    DEFAULT NULL COMMENT '目标仓库ID',
    target_person      VARCHAR(64)   DEFAULT NULL COMMENT '领用人/接收人',
    reason             VARCHAR(500)  DEFAULT NULL COMMENT '原因/用途',
    status             CHAR(1)       DEFAULT '0' COMMENT '状态（0正常 1作废）',
    create_by          VARCHAR(64)   DEFAULT '' COMMENT '创建者',
    create_time        DATETIME      DEFAULT NULL COMMENT '创建时间',
    update_by          VARCHAR(64)   DEFAULT '' COMMENT '更新者',
    update_time        DATETIME      DEFAULT NULL COMMENT '更新时间',
    remark             VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (record_id),
    KEY idx_material_id (material_id),
    KEY idx_record_type (record_type),
    KEY idx_operate_time (operate_time),
    KEY idx_asset_code (asset_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='资产流转记录表';
