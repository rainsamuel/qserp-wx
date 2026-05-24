-- 资产变更记录表
CREATE TABLE IF NOT EXISTS `biz_asset_change` (
  `change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '变更ID',
  `material_id` bigint(20) NOT NULL COMMENT '物资ID',
  `change_type` varchar(50) NOT NULL COMMENT '变更类型（location位置变更 department科室变更 status状态变更 other其他）',
  `change_content` varchar(500) NOT NULL COMMENT '变更内容',
  `old_value` varchar(200) DEFAULT NULL COMMENT '变更前值',
  `new_value` varchar(200) DEFAULT NULL COMMENT '变更后值',
  `operator` varchar(50) NOT NULL COMMENT '操作人',
  `change_time` datetime NOT NULL COMMENT '变更时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`change_id`),
  KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产变更记录表';
