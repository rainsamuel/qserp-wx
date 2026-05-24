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
 * 资产变更记录表 biz_asset_change
 */
@ApiModel("资产变更记录")
public class BizAssetChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 变更ID */
    @ApiModelProperty("变更ID")
    @Excel(name = "变更序号", cellType = Excel.ColumnType.NUMERIC)
    private Long changeId;

    /** 物资ID */
    @ApiModelProperty("物资ID")
    private Long materialId;

    /** 变更类型（location位置变更 department科室变更 status状态变更 other其他） */
    @ApiModelProperty("变更类型")
    @Excel(name = "变更类型", readConverterExp = "location=位置变更,department=科室变更,status=状态变更,other=其他")
    private String changeType;

    /** 变更内容 */
    @ApiModelProperty("变更内容")
    @Excel(name = "变更内容")
    private String changeContent;

    /** 变更前值 */
    @ApiModelProperty("变更前值")
    @Excel(name = "变更前值")
    private String oldValue;

    /** 变更后值 */
    @ApiModelProperty("变更后值")
    @Excel(name = "变更后值")
    private String newValue;

    /** 操作人 */
    @ApiModelProperty("操作人")
    @Excel(name = "操作人")
    private String operator;

    /** 变更时间 */
    @ApiModelProperty("变更时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "变更时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date changeTime;

    /** 物资名称（关联查询） */
    @ApiModelProperty("物资名称（查询返回）")
    private String materialName;

    /** 物资编码（关联查询） */
    @ApiModelProperty("物资编码（查询返回）")
    private String materialCode;

    public Long getChangeId()
    {
        return changeId;
    }

    public void setChangeId(Long changeId)
    {
        this.changeId = changeId;
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

    @NotBlank(message = "变更类型不能为空")
    public String getChangeType()
    {
        return changeType;
    }

    public void setChangeType(String changeType)
    {
        this.changeType = changeType;
    }

    @NotBlank(message = "变更内容不能为空")
    public String getChangeContent()
    {
        return changeContent;
    }

    public void setChangeContent(String changeContent)
    {
        this.changeContent = changeContent;
    }

    public String getOldValue()
    {
        return oldValue;
    }

    public void setOldValue(String oldValue)
    {
        this.oldValue = oldValue;
    }

    public String getNewValue()
    {
        return newValue;
    }

    public void setNewValue(String newValue)
    {
        this.newValue = newValue;
    }

    @NotBlank(message = "操作人不能为空")
    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    @NotNull(message = "变更时间不能为空")
    public Date getChangeTime()
    {
        return changeTime;
    }

    public void setChangeTime(Date changeTime)
    {
        this.changeTime = changeTime;
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
}
