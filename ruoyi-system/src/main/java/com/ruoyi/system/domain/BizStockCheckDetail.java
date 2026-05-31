package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 盘点明细对象 biz_stock_check_detail
 */
public class BizStockCheckDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long detailId;

    /** 盘点单ID */
    private Long checkId;

    /** 物资ID */
    private Long materialId;

    /** 物资编码 */
    @Excel(name = "物资编码")
    private String materialCode;

    /** 物资名称 */
    @Excel(name = "物资名称")
    private String materialName;

    /** 分类ID */
    private Long categoryId;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String spec;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String unit;

    /** 系统库存数量 */
    @Excel(name = "系统数量")
    private Integer systemQuantity;

    /** 实际盘点数量 */
    @Excel(name = "实际数量")
    private Integer actualQuantity;

    /** 差异数量 */
    @Excel(name = "差异数量")
    private Integer diffQuantity;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /** 差异金额 */
    @Excel(name = "差异金额")
    private BigDecimal diffAmount;

    /** 差异原因 */
    @Excel(name = "差异原因")
    private String diffReason;

    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }

    public Long getCheckId() { return checkId; }
    public void setCheckId(Long checkId) { this.checkId = checkId; }

    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Integer getSystemQuantity() { return systemQuantity; }
    public void setSystemQuantity(Integer systemQuantity) { this.systemQuantity = systemQuantity; }

    public Integer getActualQuantity() { return actualQuantity; }
    public void setActualQuantity(Integer actualQuantity) { this.actualQuantity = actualQuantity; }

    public Integer getDiffQuantity() { return diffQuantity; }
    public void setDiffQuantity(Integer diffQuantity) { this.diffQuantity = diffQuantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getDiffAmount() { return diffAmount; }
    public void setDiffAmount(BigDecimal diffAmount) { this.diffAmount = diffAmount; }

    public String getDiffReason() { return diffReason; }
    public void setDiffReason(String diffReason) { this.diffReason = diffReason; }
}
