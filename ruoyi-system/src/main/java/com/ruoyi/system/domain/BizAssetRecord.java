package com.ruoyi.system.domain;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资产流转记录表 biz_asset_record
 */
@ApiModel("资产流转记录")
public class BizAssetRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @ApiModelProperty("记录ID")
    @Excel(name = "记录序号", cellType = Excel.ColumnType.NUMERIC)
    private Long recordId;

    /** 物资ID */
    @ApiModelProperty("物资ID")
    private Long materialId;

    /** 资产编码 */
    @ApiModelProperty("资产编码")
    @Excel(name = "资产编码")
    private String assetCode;

    /** 流转类型 */
    @ApiModelProperty("流转类型（IN入库 OUT出库 DAMAGE报损 SCRAP报废）")
    @Excel(name = "流转类型", readConverterExp = "IN=入库,OUT=出库,DAMAGE=报损,SCRAP=报废")
    private String recordType;

    /** 数量 */
    @ApiModelProperty("数量")
    @Excel(name = "数量")
    private Integer quantity;

    /** 操作人 */
    @ApiModelProperty("操作人")
    @Excel(name = "操作人")
    private String operator;

    /** 操作时间 */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /** 来源仓库ID */
    @ApiModelProperty("来源仓库ID")
    private Long fromWarehouseId;

    /** 目标仓库ID */
    @ApiModelProperty("目标仓库ID")
    private Long toWarehouseId;

    /** 领用人/接收人 */
    @ApiModelProperty("领用人/接收人")
    @Excel(name = "领用人")
    private String targetPerson;

    /** 原因/用途 */
    @ApiModelProperty("原因/用途")
    @Excel(name = "原因/用途")
    private String reason;

    /** 状态 */
    @ApiModelProperty("状态（0正常 1作废）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=作废")
    private String status;

    // ===== 关联查询字段 =====

    /** 物资名称 */
    @ApiModelProperty("物资名称")
    @Excel(name = "物资名称")
    private String materialName;

    /** 物资编码 */
    @ApiModelProperty("物资编码")
    @Excel(name = "物资编码")
    private String materialCode;

    /** 计量单位 */
    @ApiModelProperty("计量单位")
    private String unit;

    /** 来源仓库名称 */
    @ApiModelProperty("来源仓库名称")
    @Excel(name = "来源仓库")
    private String fromWarehouseName;

    /** 目标仓库名称 */
    @ApiModelProperty("目标仓库名称")
    @Excel(name = "目标仓库")
    private String toWarehouseName;

    // ===== 查询条件 =====

    /** 开始时间 */
    @ApiModelProperty("开始时间（查询条件）")
    private String beginTime;

    /** 结束时间 */
    @ApiModelProperty("结束时间（查询条件）")
    private String endTime;

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }

    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }

    @NotNull(message = "流转类型不能为空")
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    @NotNull(message = "数量不能为空")
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }

    public Long getFromWarehouseId() { return fromWarehouseId; }
    public void setFromWarehouseId(Long fromWarehouseId) { this.fromWarehouseId = fromWarehouseId; }

    public Long getToWarehouseId() { return toWarehouseId; }
    public void setToWarehouseId(Long toWarehouseId) { this.toWarehouseId = toWarehouseId; }

    public String getTargetPerson() { return targetPerson; }
    public void setTargetPerson(String targetPerson) { this.targetPerson = targetPerson; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getFromWarehouseName() { return fromWarehouseName; }
    public void setFromWarehouseName(String fromWarehouseName) { this.fromWarehouseName = fromWarehouseName; }

    public String getToWarehouseName() { return toWarehouseName; }
    public void setToWarehouseName(String toWarehouseName) { this.toWarehouseName = toWarehouseName; }

    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
