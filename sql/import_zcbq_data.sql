-- =============================================
-- 资产标签数据导入脚本
-- 用于将CSV数据导入到物资表中
-- =============================================

-- 使用说明：
-- 1. 先执行 sync_zcbq.sql 添加字段
-- 2. 将CSV数据导入到临时表
-- 3. 执行本脚本进行数据迁移

-- 创建临时表
DROP TABLE IF EXISTS temp_zcbq_import;
CREATE TEMPORARY TABLE temp_zcbq_import (
    id INT,
    kpbh VARCHAR(64),
    zbmc VARCHAR(200),
    wzmc VARCHAR(200),
    ggxh VARCHAR(200),
    zxdw VARCHAR(50),
    kfid INT,
    ksmc VARCHAR(100),
    zjkpbh VARCHAR(128),
    barcode VARCHAR(128),
    sync_id VARCHAR(64),
    sync_org_code VARCHAR(64)
);

-- 示例：手动插入测试数据（实际使用时从CSV导入）
-- INSERT INTO temp_zcbq_import (id, kpbh, zbmc, wzmc, ggxh, zxdw, kfid, ksmc, zjkpbh, sync_id, sync_org_code)
-- VALUES (1289, '1505032022031000682', '换药车', '换药车', '', '台', 1023, '八病区', '91330000MA27U06H831505032022031000682', NULL, NULL);

-- 从CSV数据解析URL中的sync_id和org_code
-- URL格式：http://qr.ygyfnet.com/?o=91330000MA27U06H83&t=4&i=4220
-- 需要提取 i=4220 作为 sync_id，o=91330000MA27U06H83 作为 org_code

-- 更新临时表中的sync_id和sync_org_code（从zjkpbh字段解析）
UPDATE temp_zcbq_import
SET sync_org_code = LEFT(zjkpbh, 18)
WHERE zjkpbh IS NOT NULL AND LENGTH(zjkpbh) >= 18;

-- 插入数据到物资表
INSERT INTO biz_material (
    material_code,
    material_name,
    spec,
    unit,
    warehouse_id,
    kpbh,
    sync_org_code,
    data_source,
    status,
    create_by,
    create_time
)
SELECT
    kpbh,
    COALESCE(wzmc, zbmc),
    ggxh,
    zxdw,
    kfid,
    kpbh,
    sync_org_code,
    'sync',
    '0',
    'admin',
    sysdate()
FROM temp_zcbq_import
WHERE kpbh IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM biz_material m WHERE m.kpbh = temp_zcbq_import.kpbh
);

-- 更新已有记录的同步信息
UPDATE biz_material m
INNER JOIN temp_zcbq_import t ON m.kpbh = t.kpbh
SET
    m.sync_org_code = t.sync_org_code,
    m.data_source = 'sync',
    m.update_time = sysdate(),
    m.update_by = 'admin'
WHERE t.sync_org_code IS NOT NULL;

-- 清理临时表
DROP TABLE IF EXISTS temp_zcbq_import;

-- 验证导入结果
SELECT
    data_source,
    COUNT(*) as count,
    SUM(CASE WHEN sync_id IS NOT NULL THEN 1 ELSE 0 END) as with_sync_id,
    SUM(CASE WHEN kpbh IS NOT NULL THEN 1 ELSE 0 END) as with_kpbh,
    SUM(CASE WHEN sync_org_code IS NOT NULL THEN 1 ELSE 0 END) as with_org_code
FROM biz_material
GROUP BY data_source;
