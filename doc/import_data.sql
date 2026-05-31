-- 自动生成的导入SQL
-- 执行前请先执行 sync_zcbq.sql 添加字段

-- 批量插入语句
INSERT INTO biz_material (
    material_code, material_name, spec, unit, warehouse_id,
    kpbh, sync_org_code, data_source, status, create_by, create_time
) VALUES
('1505032022031000682', '换药车', '', '台', 1, '1505032022031000682', NULL, 'sync', '0', 'admin', sysdate()),
('1505032019041000364', '晨间护理车', '', '台', 1, '1505032019041000364', NULL, 'sync', '0', 'admin', sysdate()),
('0805062019041000363', '喉镜', '69690', '台', 1, '0805062019041000363', NULL, 'sync', '0', 'admin', sysdate()),
('0803012019041000362', '除颤监护仪', 'BeneHeart D2', '台', 1, '0803012019041000362', NULL, 'sync', '0', 'admin', sysdate()),
('2301042022031000681', '除湿机', 'KC-40', '台', 1, '2301042022031000681', NULL, 'sync', '0', 'admin', sysdate()),
('0703032022031000680', '电子血压计', 'HEM-7136', '台', 1, '0703032022031000680', NULL, 'sync', '0', 'admin', sysdate()),
('0601072021121000361', '移动式X射线机', 'MobiEye 700T', '台', 1, '0601072021121000361', NULL, 'sync', '0', 'admin', sysdate()),
('1505032022031000679', '白蓝ABS护理车', '', '台', 1, '1505032022031000679', NULL, 'sync', '0', 'admin', sysdate()),
('1505032019041000360', '抢救车', 'ET-85071A', '台', 1, '1505032019041000360', NULL, 'sync', '0', 'admin', sysdate()),
('2301042022031000529', '洗眼器', '', '台', 1, '2301042022031000529', NULL, 'sync', '0', 'admin', sysdate());

-- 更新已有记录的同步信息
UPDATE biz_material m
SET m.data_source = 'sync', m.update_time = sysdate()
WHERE m.kpbh IS NOT NULL AND m.data_source = 'manual';

-- 确保索引存在
CREATE INDEX IF NOT EXISTS idx_kpbh ON biz_material(kpbh);
CREATE INDEX IF NOT EXISTS idx_sync_id ON biz_material(sync_id);