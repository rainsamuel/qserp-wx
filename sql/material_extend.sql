-- 物资信息表增加字段
ALTER TABLE biz_material ADD COLUMN use_department VARCHAR(100) DEFAULT NULL COMMENT '使用科室' AFTER stock_quantity;
ALTER TABLE biz_material ADD COLUMN location VARCHAR(255) DEFAULT NULL COMMENT '位置' AFTER use_department;
ALTER TABLE biz_material ADD COLUMN manage_department VARCHAR(100) DEFAULT NULL COMMENT '管理科室' AFTER location;
ALTER TABLE biz_material ADD COLUMN unit_price DECIMAL(12,2) DEFAULT NULL COMMENT '单价' AFTER manage_department;
ALTER TABLE biz_material ADD COLUMN supplier VARCHAR(200) DEFAULT NULL COMMENT '供应商' AFTER unit_price;
