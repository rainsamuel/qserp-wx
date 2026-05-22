package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 巡检明细表 biz_inspection_detail
 */
@ApiModel("巡检明细")
public class BizInspectionDetail
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    @ApiModelProperty("明细ID")
    private Long detailId;

    /** 巡检ID */
    @ApiModelProperty("巡检ID")
    private Long inspectionId;

    /** 检查项ID */
    @ApiModelProperty("检查项ID")
    private Long itemId;

    /** 检查结果（normal正常 abnormal异常） */
    @ApiModelProperty("检查结果（normal正常 abnormal异常）")
    private String checkResult;

    /** 检查备注 */
    @ApiModelProperty("检查备注")
    private String checkRemark;

    /** 检查项名称（关联查询） */
    @ApiModelProperty("检查项名称（查询返回）")
    private String itemName;

    /** 检查项分组（关联查询） */
    @ApiModelProperty("检查项分组（查询返回）")
    private String itemGroup;

    public Long getDetailId()
    {
        return detailId;
    }

    public void setDetailId(Long detailId)
    {
        this.detailId = detailId;
    }

    public Long getInspectionId()
    {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId)
    {
        this.inspectionId = inspectionId;
    }

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public String getCheckResult()
    {
        return checkResult;
    }

    public void setCheckResult(String checkResult)
    {
        this.checkResult = checkResult;
    }

    public String getCheckRemark()
    {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark)
    {
        this.checkRemark = checkRemark;
    }

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
}
