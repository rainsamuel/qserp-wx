-- 巡检周期字典类型
INSERT INTO sys_dict_type VALUES (100, '巡检周期', 'biz_inspection_cycle', '0', 'admin', sysdate(), '', NULL, '巡检周期列表');

-- 巡检周期字典数据
INSERT INTO sys_dict_data VALUES (100, 1, '每日', 'daily', 'biz_inspection_cycle', '', 'primary', 'N', '0', 'admin', sysdate(), '', NULL, '每日巡检');
INSERT INTO sys_dict_data VALUES (101, 2, '每周', 'weekly', 'biz_inspection_cycle', '', 'success', 'N', '0', 'admin', sysdate(), '', NULL, '每周巡检');
INSERT INTO sys_dict_data VALUES (102, 3, '每月', 'monthly', 'biz_inspection_cycle', '', 'info', 'N', '0', 'admin', sysdate(), '', NULL, '每月巡检');
INSERT INTO sys_dict_data VALUES (103, 4, '每季度', 'quarterly', 'biz_inspection_cycle', '', 'warning', 'N', '0', 'admin', sysdate(), '', NULL, '每季度巡检');
INSERT INTO sys_dict_data VALUES (104, 5, '每年', 'yearly', 'biz_inspection_cycle', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '每年巡检');

-- 巡检记录表增加周期字段
ALTER TABLE biz_inspection ADD COLUMN inspection_cycle VARCHAR(20) DEFAULT NULL COMMENT '巡检周期（daily weekly monthly quarterly yearly）' AFTER result;
