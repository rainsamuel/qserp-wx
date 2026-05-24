package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 巡检记录表 biz_inspection
 */
@ApiModel("巡检记录")
public class BizInspection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 巡检ID */
    @ApiModelProperty("巡检ID")
    @Excel(name = "巡检序号", cellType = Excel.ColumnType.NUMERIC)
    private Long inspectionId;

    /** 物资ID */
    @ApiModelProperty("物资ID")
    private Long materialId;

    /** 巡检人 */
    @ApiModelProperty("巡检人")
    @Excel(name = "巡检人")
    private String inspector;

    /** 巡检时间 */
    @ApiModelProperty("巡检时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "巡检时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionTime;

    /** 巡检结果（normal正常 abnormal异常） */
    @ApiModelProperty("巡检结果（normal正常 abnormal异常）")
    @Excel(name = "巡检结果", readConverterExp = "normal=正常,abnormal=异常")
    private String result;

    /** 巡检周期（daily每周 monthly每季度 yearly每年） */
    @ApiModelProperty("巡检周期")
    @Excel(name = "巡检周期", readConverterExp = "daily=每日,weekly=每周,monthly=每月,quarterly=每季度,yearly=每年")
    private String inspectionCycle;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 物资名称（关联查询） */
    @ApiModelProperty("物资名称（查询返回）")
    private String materialName;

    /** 物资编码（关联查询） */
    @ApiModelProperty("物资编码（查询返回）")
    private String materialCode;

    /** 巡检明细列表 */
    @ApiModelProperty("巡检明细列表")
    private List<BizInspectionDetail> details;

    /** 选中的检查项ID列表（新增时使用） */
    @ApiModelProperty("选中的检查项ID列表")
    private List<Long> itemIds;

    public Long getInspectionId()
    {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId)
    {
        this.inspectionId = inspectionId;
    }

    @NotNull(message = "物资不能为空")
    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    @NotBlank(message = "巡检人不能为空")
    public String getInspector()
    {
        return inspector;
    }

    public void setInspector(String inspector)
    {
        this.inspector = inspector;
    }

    @NotNull(message = "巡检时间不能为空")
    public Date getInspectionTime()
    {
        return inspectionTime;
    }

    public void setInspectionTime(Date inspectionTime)
    {
        this.inspectionTime = inspectionTime;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getInspectionCycle()
    {
        return inspectionCycle;
    }

    public void setInspectionCycle(String inspectionCycle)
    {
        this.inspectionCycle = inspectionCycle;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMaterialName()
    {
        return materialName;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public String getMaterialCode()
    {
        return materialCode;
    }

    public void setMaterialCode(String materialCode)
    {
        this.materialCode = materialCode;
    }

    public List<BizInspectionDetail> getDetails()
    {
        return details;
    }

    public void setDetails(List<BizInspectionDetail> details)
    {
        this.details = details;
    }

    public List<Long> getItemIds()
    {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds)
    {
        this.itemIds = itemIds;
    }
}
