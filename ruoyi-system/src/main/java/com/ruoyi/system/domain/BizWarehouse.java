package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库信息表 biz_warehouse
 */
@ApiModel("仓库信息")
public class BizWarehouse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 仓库ID */
    @ApiModelProperty("仓库ID")
    @Excel(name = "仓库序号", cellType = Excel.ColumnType.NUMERIC)
    private Long warehouseId;

    /** 仓库编码 */
    @ApiModelProperty("仓库编码")
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 仓库名称 */
    @ApiModelProperty("仓库名称")
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 仓库地址 */
    @ApiModelProperty("仓库地址")
    @Excel(name = "仓库地址")
    private String address;

    /** 联系人 */
    @ApiModelProperty("联系人")
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 数据来源（manual手动 sync同步） */
    @ApiModelProperty("数据来源（manual手动 sync同步）")
    private String sourceType;

    /** 第三方系统原始ID */
    @ApiModelProperty("第三方系统原始ID")
    private String sourceId;

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    @NotBlank(message = "仓库编码不能为空")
    @Size(min = 0, max = 64, message = "仓库编码长度不能超过64个字符")
    public String getWarehouseCode()
    {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode)
    {
        this.warehouseCode = warehouseCode;
    }

    @NotBlank(message = "仓库名称不能为空")
    @Size(min = 0, max = 100, message = "仓库名称长度不能超过100个字符")
    public String getWarehouseName()
    {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName)
    {
        this.warehouseName = warehouseName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getSourceType()
    {
        return sourceType;
    }

    public void setSourceType(String sourceType)
    {
        this.sourceType = sourceType;
    }

    public String getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }
}
