-- =============================================
-- PM模板管理功能数据库脚本
-- =============================================

-- 1. PM模板主表
DROP TABLE IF EXISTS biz_pm_template;
CREATE TABLE biz_pm_template (
    template_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    template_type VARCHAR(50) DEFAULT NULL COMMENT '模板类型（预防性维护/日常巡检）',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (template_id),
    UNIQUE KEY uk_template_name (template_name),
    KEY idx_template_type (template_type),
    KEY idx_status (status)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='PM模板主表';

-- 2. PM模板内容表
DROP TABLE IF EXISTS biz_pm_template_content;
CREATE TABLE biz_pm_template_content (
    content_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '内容ID',
    template_id BIGINT NOT NULL COMMENT '所属模板ID',
    item_name VARCHAR(200) DEFAULT NULL COMMENT '模板项目（分组名）',
    content_name VARCHAR(500) NOT NULL COMMENT '模板内容（检查项）',
    need_value CHAR(1) DEFAULT '1' COMMENT '需要实测值（0否 1是）',
    value_type VARCHAR(20) DEFAULT 'dropdown' COMMENT '实测值类型（dropdown下拉/text文本）',
    value_options VARCHAR(500) DEFAULT NULL COMMENT '下拉可取值（格式：值-名称|值-名称）',
    default_value VARCHAR(100) DEFAULT NULL COMMENT '默认取值',
    unit VARCHAR(50) DEFAULT NULL COMMENT '单位',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (content_id),
    KEY idx_template_id (template_id),
    KEY idx_item_name (item_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='PM模板内容表';

-- 3. 物资表增加PM模板关联字段
ALTER TABLE biz_material ADD COLUMN pm_template_id BIGINT DEFAULT NULL COMMENT 'PM模板ID' AFTER warranty_period;

-- 4. PM模板菜单（挂在巡检管理下）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板管理',
  (SELECT menu_id FROM (SELECT menu_id FROM sys_menu WHERE menu_name = '巡检管理' AND menu_type = 'M' LIMIT 1) AS t),
  1, 'pmTemplate', 'asset/pmTemplate/index', 1, 0, 'C', '0', '0', 'asset:pmTemplate:list', 'template', 'admin', sysdate());

SET @pmTemplateMenuId = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板查询', @pmTemplateMenuId, 1, '#', '', 1, 0, 'F', '0', '0', 'asset:pmTemplate:query', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板新增', @pmTemplateMenuId, 2, '#', '', 1, 0, 'F', '0', '0', 'asset:pmTemplate:add', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板修改', @pmTemplateMenuId, 3, '#', '', 1, 0, 'F', '0', '0', 'asset:pmTemplate:edit', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板删除', @pmTemplateMenuId, 4, '#', '', 1, 0, 'F', '0', '0', 'asset:pmTemplate:remove', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板导入', @pmTemplateMenuId, 5, '#', '', 1, 0, 'F', '0', '0', 'asset:pmTemplate:import', '#', 'admin', sysdate());

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES ('PM模板导出', @pmTemplateMenuId, 6, '#', '', 1, 0, 'F', '0', '0', 'asset:pmTemplate:export', '#', 'admin', sysdate());
