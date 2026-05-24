package com.ruoyi.system.domain;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报修记录表 biz_repair
 */
@ApiModel("报修记录")
public class BizRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报修ID */
    @ApiModelProperty("报修ID")
    @Excel(name = "报修序号", cellType = Excel.ColumnType.NUMERIC)
    private Long repairId;

    /** 关联物资ID */
    @ApiModelProperty("关联物资ID")
    private Long materialId;

    /** 资产编码 */
    @ApiModelProperty("资产编码")
    @Excel(name = "资产编码")
    private String assetCode;

    /** 资产名称 */
    @ApiModelProperty("资产名称")
    @Excel(name = "资产名称")
    private String assetName;

    /** 故障描述 */
    @ApiModelProperty("故障描述")
    @Excel(name = "故障描述")
    private String faultDesc;

    /** 故障发生时间 */
    @ApiModelProperty("故障发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "故障时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date faultTime;

    /** 报修人 */
    @ApiModelProperty("报修人")
    @Excel(name = "报修人")
    private String reporter;

    /** 联系电话 */
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String reporterPhone;

    /** 故障位置 */
    @ApiModelProperty("故障位置")
    @Excel(name = "故障位置")
    private String location;

    /** 优先级（low低 normal普通 high高 urgent紧急） */
    @ApiModelProperty("优先级")
    @Excel(name = "优先级", readConverterExp = "low=低,normal=普通,high=高,urgent=紧急")
    private String priority;

    /** 状态（pending待处理 processing处理中 completed已完成 rejected已驳回 cancelled已取消） */
    @ApiModelProperty("状态")
    @Excel(name = "状态", readConverterExp = "pending=待处理,processing=处理中,completed=已完成,rejected=已驳回,cancelled=已取消")
    private String status;

    /** 处理人 */
    @ApiModelProperty("处理人")
    @Excel(name = "处理人")
    private String handler;

    /** 处理时间 */
    @ApiModelProperty("处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 处理结果 */
    @ApiModelProperty("处理结果")
    @Excel(name = "处理结果")
    private String handleResult;

    /** 故障照片（逗号分隔） */
    @ApiModelProperty("故障照片")
    private String photos;

    public Long getRepairId()
    {
        return repairId;
    }

    public void setRepairId(Long repairId)
    {
        this.repairId = repairId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public String getAssetCode()
    {
        return assetCode;
    }

    public void setAssetCode(String assetCode)
    {
        this.assetCode = assetCode;
    }

    @NotBlank(message = "资产名称不能为空")
    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    @NotBlank(message = "故障描述不能为空")
    public String getFaultDesc()
    {
        return faultDesc;
    }

    public void setFaultDesc(String faultDesc)
    {
        this.faultDesc = faultDesc;
    }

    @NotNull(message = "故障时间不能为空")
    public Date getFaultTime()
    {
        return faultTime;
    }

    public void setFaultTime(Date faultTime)
    {
        this.faultTime = faultTime;
    }

    @NotBlank(message = "报修人不能为空")
    public String getReporter()
    {
        return reporter;
    }

    public void setReporter(String reporter)
    {
        this.reporter = reporter;
    }

    public String getReporterPhone()
    {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone)
    {
        this.reporterPhone = reporterPhone;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getHandler()
    {
        return handler;
    }

    public void setHandler(String handler)
    {
        this.handler = handler;
    }

    public Date getHandleTime()
    {
        return handleTime;
    }

    public void setHandleTime(Date handleTime)
    {
        this.handleTime = handleTime;
    }

    public String getHandleResult()
    {
        return handleResult;
    }

    public void setHandleResult(String handleResult)
    {
        this.handleResult = handleResult;
    }

    public String getPhotos()
    {
        return photos;
    }

    public void setPhotos(String photos)
    {
        this.photos = photos;
    }
}
