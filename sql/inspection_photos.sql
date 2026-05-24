-- 给巡检记录表添加照片字段
ALTER TABLE biz_inspection ADD COLUMN photos VARCHAR(1000) DEFAULT NULL COMMENT '巡检照片（逗号分隔的文件路径）' AFTER result;
