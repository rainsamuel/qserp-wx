-- =============================================
-- 资产标签数据同步脚本
-- =============================================

-- 1. 物资表增加第三方同步相关字段
ALTER TABLE biz_material
ADD COLUMN sync_id VARCHAR(64) DEFAULT NULL COMMENT '第三方同步ID（用于URL二维码解析）' AFTER asset_code,
ADD COLUMN kpbh VARCHAR(64) DEFAULT NULL COMMENT '卡片编号（KPBH）' AFTER sync_id,
ADD COLUMN sync_org_code VARCHAR(64) DEFAULT NULL COMMENT '第三方组织编码' AFTER kpbh,
ADD COLUMN data_source VARCHAR(20) DEFAULT 'manual' COMMENT '数据来源（manual手动 sync同步）' AFTER sync_org_code;

-- 添加索引
ALTER TABLE biz_material
ADD INDEX idx_sync_id (sync_id),
ADD INDEX idx_kpbh (kpbh);

-- 2. 创建第三方仓库映射表
DROP TABLE IF EXISTS biz_warehouse_mapping;
CREATE TABLE biz_warehouse_mapping (
    mapping_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '映射ID',
    local_warehouse_id BIGINT(20) NOT NULL COMMENT '本地仓库ID',
    remote_warehouse_code VARCHAR(64) NOT NULL COMMENT '第三方库房编号',
    remote_org_code VARCHAR(64) DEFAULT NULL COMMENT '第三方组织编码',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (mapping_id),
    UNIQUE KEY uk_remote (remote_warehouse_code, remote_org_code),
    KEY idx_local_warehouse (local_warehouse_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='仓库映射表';

-- 3. 导入示例映射数据（根据实际情况调整）
-- 假设本地仓库ID 1023 对应第三方库房编号 19
INSERT INTO biz_warehouse_mapping (local_warehouse_id, remote_warehouse_code, create_by, create_time)
VALUES (1023, '19', 'admin', sysdate());

INSERT INTO biz_warehouse_mapping (local_warehouse_id, remote_warehouse_code, create_by, create_time)
VALUES (1024, '19', 'admin', sysdate());

-- 4. 从CSV导入数据到物资表的存储过程
-- 注意：实际使用时需要根据CSV数据结构调整

-- 示例：插入一条同步数据
-- INSERT INTO biz_material (material_code, material_name, spec, unit, warehouse_id, sync_id, kpbh, sync_org_code, data_source, status, create_by, create_time)
-- VALUES ('1505032022031000682', '换药车', '', '台', 1023, '4220', '1505032022031000682', '91330000MA27U06H83', 'sync', '0', 'admin', sysdate());

-- 5. 创建URL二维码解析函数（用于查询）
-- 解析URL格式：http://qr.ygyfnet.com/?o=91330000MA27U06H83&t=4&i=4220
-- 提取 i 参数作为 sync_id 查询

-- 6. 查询物资的视图（支持多种查询方式）
CREATE OR REPLACE VIEW v_material_query AS
SELECT
    m.material_id,
    m.material_code,
    m.asset_code,
    m.sync_id,
    m.kpbh,
    m.material_name,
    m.spec,
    m.unit,
    m.stock_quantity,
    m.warehouse_id,
    w.warehouse_name,
    m.status,
    m.data_source,
    -- 生成标准二维码内容
    CASE
        WHEN m.asset_code IS NOT NULL AND m.asset_code != '' THEN m.asset_code
        WHEN m.sync_id IS NOT NULL THEN CONCAT('{"syncId":"', m.sync_id, '","orgCode":"', IFNULL(m.sync_org_code, ''), '"}')
        ELSE m.material_code
    END AS qr_code_content
FROM biz_material m
LEFT JOIN biz_warehouse w ON m.warehouse_id = w.warehouse_id;
