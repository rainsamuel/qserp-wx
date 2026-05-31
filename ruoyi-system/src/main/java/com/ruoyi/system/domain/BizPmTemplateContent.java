package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * PM模板内容表 biz_pm_template_content
 */
public class BizPmTemplateContent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 内容ID */
    @Excel(name = "内容序号", cellType = Excel.ColumnType.NUMERIC)
    private Long contentId;

    /** 所属模板ID */
    private Long templateId;

    /** 模板项目（分组名） */
    @Excel(name = "模板项目")
    private String itemName;

    /** 模板内容（检查项） */
    @Excel(name = "模板内容")
    private String contentName;

    /** 需要实测值（0否 1是） */
    @Excel(name = "需要实测值", readConverterExp = "0=否,1=是")
    private String needValue;

    /** 实测值类型（dropdown下拉/text文本） */
    @Excel(name = "实测值类型")
    private String valueType;

    /** 下拉可取值 */
    @Excel(name = "下拉可取值")
    private String valueOptions;

    /** 默认取值 */
    @Excel(name = "默认取值")
    private String defaultValue;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getContentName() { return contentName; }
    public void setContentName(String contentName) { this.contentName = contentName; }

    public String getNeedValue() { return needValue; }
    public void setNeedValue(String needValue) { this.needValue = needValue; }

    public String getValueType() { return valueType; }
    public void setValueType(String valueType) { this.valueType = valueType; }

    public String getValueOptions() { return valueOptions; }
    public void setValueOptions(String valueOptions) { this.valueOptions = valueOptions; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
