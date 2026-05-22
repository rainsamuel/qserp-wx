package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 巡检内容项表 biz_inspection_item
 */
@ApiModel("巡检内容项")
public class BizInspectionItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 检查项ID */
    @ApiModelProperty("检查项ID")
    @Excel(name = "检查项序号", cellType = Excel.ColumnType.NUMERIC)
    private Long itemId;

    /** 检查项名称 */
    @ApiModelProperty("检查项名称")
    @Excel(name = "检查项名称")
    private String itemName;

    /** 检查项分组 */
    @ApiModelProperty("检查项分组")
    @Excel(name = "分组")
    private String itemGroup;

    /** 排序 */
    @ApiModelProperty("排序")
    @Excel(name = "排序")
    private Integer sortOrder;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    @NotBlank(message = "检查项名称不能为空")
    @Size(min = 0, max = 200, message = "检查项名称长度不能超过200个字符")
    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemGroup()
    {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup)
    {
        this.itemGroup = itemGroup;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
