package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 盘点单对象 biz_stock_check
 */
public class BizStockCheck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 盘点单ID */
    private Long checkId;

    /** 盘点单号 */
    @Excel(name = "盘点单号")
    private String checkNo;

    /** 盘点仓库ID */
    private Long warehouseId;

    /** 盘点仓库名称 */
    @Excel(name = "盘点仓库")
    private String warehouseName;

    /** 盘点类型（0全盘 1抽盘） */
    @Excel(name = "盘点类型", readConverterExp = "0=全盘,1=抽盘")
    private String checkType;

    /** 盘点日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkDate;

    /** 盘点人 */
    @Excel(name = "盘点人")
    private String checker;

    /** 状态（0盘点中 1已完成 2已作废） */
    @Excel(name = "状态", readConverterExp = "0=盘点中,1=已完成,2=已作废")
    private String status;

    /** 差异总金额 */
    @Excel(name = "差异总金额")
    private BigDecimal totalDiffAmount;

    /** 盘点明细列表 */
    private List<BizStockCheckDetail> detailList;

    public Long getCheckId() { return checkId; }
    public void setCheckId(Long checkId) { this.checkId = checkId; }

    public String getCheckNo() { return checkNo; }
    public void setCheckNo(String checkNo) { this.checkNo = checkNo; }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getCheckType() { return checkType; }
    public void setCheckType(String checkType) { this.checkType = checkType; }

    public Date getCheckDate() { return checkDate; }
    public void setCheckDate(Date checkDate) { this.checkDate = checkDate; }

    public String getChecker() { return checker; }
    public void setChecker(String checker) { this.checker = checker; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalDiffAmount() { return totalDiffAmount; }
    public void setTotalDiffAmount(BigDecimal totalDiffAmount) { this.totalDiffAmount = totalDiffAmount; }

    public List<BizStockCheckDetail> getDetailList() { return detailList; }
    public void setDetailList(List<BizStockCheckDetail> detailList) { this.detailList = detailList; }
}
