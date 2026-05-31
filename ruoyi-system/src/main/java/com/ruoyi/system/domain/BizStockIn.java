package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 入库单对象 biz_stock_in
 */
public class BizStockIn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 入库单ID */
    private Long stockInId;

    /** 入库单号 */
    @Excel(name = "入库单号")
    private String stockInNo;

    /** 供应商 */
    @Excel(name = "供应商")
    private String supplier;

    /** 发票号 */
    @Excel(name = "发票号")
    private String invoiceNo;

    /** 发票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发票日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date invoiceDate;

    /** 发票金额 */
    @Excel(name = "发票金额")
    private BigDecimal invoiceAmount;

    /** 入库仓库ID */
    private Long warehouseId;

    /** 入库仓库名称 */
    @Excel(name = "入库仓库")
    private String warehouseName;

    /** 入库总金额 */
    @Excel(name = "入库总金额")
    private BigDecimal totalAmount;

    /** 入库总数量 */
    @Excel(name = "入库总数量")
    private Integer totalQuantity;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inDate;

    /** 状态（0待审核 1已审核 2已驳回） */
    @Excel(name = "状态", readConverterExp = "0=待审核,1=已审核,2=已驳回")
    private String status;

    /** 审核人 */
    private String auditBy;

    /** 审核时间 */
    private Date auditTime;

    /** 审核备注 */
    private String auditRemark;

    /** 入库明细列表 */
    private List<BizStockInDetail> detailList;

    public Long getStockInId() {
        return stockInId;
    }

    public void setStockInId(Long stockInId) {
        this.stockInId = stockInId;
    }

    public String getStockInNo() {
        return stockInNo;
    }

    public void setStockInNo(String stockInNo) {
        this.stockInNo = stockInNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public List<BizStockInDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<BizStockInDetail> detailList) {
        this.detailList = detailList;
    }
}
