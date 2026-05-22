package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物资分类表 biz_material_category
 */
@ApiModel("物资分类信息")
public class BizMaterialCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @ApiModelProperty("分类ID")
    @Excel(name = "分类序号", cellType = Excel.ColumnType.NUMERIC)
    private Long categoryId;

    /** 父分类ID */
    @ApiModelProperty("父分类ID")
    private Long parentId;

    /** 分类名称 */
    @ApiModelProperty("分类名称")
    @Excel(name = "分类名称")
    private String categoryName;

    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "排序")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    @NotBlank(message = "分类名称不能为空")
    @Size(min = 0, max = 100, message = "分类名称长度不能超过100个字符")
    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
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
