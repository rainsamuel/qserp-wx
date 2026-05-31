package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物资信息表 biz_material
 */
@ApiModel("物资信息")
public class BizMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物资ID */
    @ApiModelProperty("物资ID")
    @Excel(name = "物资序号", cellType = Excel.ColumnType.NUMERIC)
    private Long materialId;

    /** 物资编码 */
    @ApiModelProperty("物资编码")
    @Excel(name = "物资编码")
    private String materialCode;

    /** 资产编码（二维码内容） */
    @ApiModelProperty("资产编码（二维码内容）")
    @Excel(name = "资产编码")
    private String assetCode;

    /** 第三方同步ID（用于URL二维码解析） */
    @ApiModelProperty("第三方同步ID")
    @Excel(name = "同步ID")
    private String syncId;

    /** 卡片编号（KPBH） */
    @ApiModelProperty("卡片编号")
    @Excel(name = "卡片编号")
    private String kpbh;

    /** 第三方组织编码 */
    @ApiModelProperty("第三方组织编码")
    @Excel(name = "组织编码")
    private String syncOrgCode;

    /** 数据来源（manual手动 sync同步） */
    @ApiModelProperty("数据来源")
    @Excel(name = "数据来源", readConverterExp = "manual=手动录入,sync=第三方同步")
    private String dataSource;

    /** 物资名称 */
    @ApiModelProperty("物资名称")
    @Excel(name = "物资名称")
    private String materialName;

    /** 物资分类ID */
    @ApiModelProperty("物资分类ID")
    @Excel(name = "物资分类ID")
    private Long categoryId;

    /** 规格型号 */
    @ApiModelProperty("规格型号")
    @Excel(name = "规格型号")
    private String spec;

    /** 计量单位 */
    @ApiModelProperty("计量单位")
    @Excel(name = "计量单位")
    private String unit;

    /** 库存数量 */
    @ApiModelProperty("库存数量")
    @Excel(name = "库存数量")
    private Integer stockQuantity;

    /** 使用科室 */
    @ApiModelProperty("使用科室")
    @Excel(name = "使用科室")
    private String useDepartment;

    /** 位置 */
    @ApiModelProperty("位置")
    @Excel(name = "位置")
    private String location;

    /** 管理科室 */
    @ApiModelProperty("管理科室")
    @Excel(name = "管理科室")
    private String manageDepartment;

    /** 单价 */
    @ApiModelProperty("单价")
    @Excel(name = "单价")
    private java.math.BigDecimal unitPrice;

    /** 供应商 */
    @ApiModelProperty("供应商")
    @Excel(name = "供应商")
    private String supplier;

    /** 所在仓库ID */
    @ApiModelProperty("所在仓库ID")
    @Excel(name = "所在仓库ID")
    private Long warehouseId;

    /** 保修期限（天） */
    @ApiModelProperty("保修期限（天）")
    @Excel(name = "保修期限(天)")
    private Integer warrantyPeriod;

    /** PM模板ID */
    @ApiModelProperty("PM模板ID")
    private Long pmTemplateId;

    /** PM模板名称（关联查询） */
    @ApiModelProperty("PM模板名称（关联查询）")
    private String pmTemplateName;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 分类名称（关联查询） */
    @ApiModelProperty("分类名称（查询返回）")
    private String categoryName;

    /** 仓库名称（关联查询） */
    @ApiModelProperty("仓库名称（查询返回）")
    private String warehouseName;

    public Long getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    @NotBlank(message = "物资编码不能为空")
    @Size(min = 0, max = 64, message = "物资编码长度不能超过64个字符")
    public String getMaterialCode()
    {
        return materialCode;
    }

    public void setMaterialCode(String materialCode)
    {
        this.materialCode = materialCode;
    }

    public String getAssetCode()
    {
        return assetCode;
    }

    public void setAssetCode(String assetCode)
    {
        this.assetCode = assetCode;
    }

    public String getSyncId()
    {
        return syncId;
    }

    public void setSyncId(String syncId)
    {
        this.syncId = syncId;
    }

    public String getKpbh()
    {
        return kpbh;
    }

    public void setKpbh(String kpbh)
    {
        this.kpbh = kpbh;
    }

    public String getSyncOrgCode()
    {
        return syncOrgCode;
    }

    public void setSyncOrgCode(String syncOrgCode)
    {
        this.syncOrgCode = syncOrgCode;
    }

    public String getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }

    @NotBlank(message = "物资名称不能为空")
    @Size(min = 0, max = 200, message = "物资名称长度不能超过200个字符")
    public String getMaterialName()
    {
        return materialName;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getSpec()
    {
        return spec;
    }

    public void setSpec(String spec)
    {
        this.spec = spec;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public Integer getStockQuantity()
    {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity)
    {
        this.stockQuantity = stockQuantity;
    }

    public String getUseDepartment()
    {
        return useDepartment;
    }

    public void setUseDepartment(String useDepartment)
    {
        this.useDepartment = useDepartment;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getManageDepartment()
    {
        return manageDepartment;
    }

    public void setManageDepartment(String manageDepartment)
    {
        this.manageDepartment = manageDepartment;
    }

    public java.math.BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(java.math.BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String getSupplier()
    {
        return supplier;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public Long getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Integer getWarrantyPeriod()
    {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod)
    {
        this.warrantyPeriod = warrantyPeriod;
    }

    public Long getPmTemplateId()
    {
        return pmTemplateId;
    }

    public void setPmTemplateId(Long pmTemplateId)
    {
        this.pmTemplateId = pmTemplateId;
    }

    public String getPmTemplateName()
    {
        return pmTemplateName;
    }

    public void setPmTemplateName(String pmTemplateName)
    {
        this.pmTemplateName = pmTemplateName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getWarehouseName()
    {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName)
    {
        this.warehouseName = warehouseName;
    }
}
