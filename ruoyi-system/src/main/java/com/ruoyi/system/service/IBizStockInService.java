package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizStockIn;
import com.ruoyi.system.domain.BizStockInDetail;

/**
 * 入库单 服务层
 */
public interface IBizStockInService
{
    /**
     * 查询入库单列表
     */
    public List<BizStockIn> selectStockInList(BizStockIn stockIn);

    /**
     * 根据ID查询入库单
     */
    public BizStockIn selectStockInById(Long stockInId);

    /**
     * 新增入库单（含明细）
     */
    public int insertStockIn(BizStockIn stockIn);

    /**
     * 修改入库单（含明细）
     */
    public int updateStockIn(BizStockIn stockIn);

    /**
     * 删除入库单（含明细）
     */
    public int deleteStockInById(Long stockInId);

    /**
     * 批量删除入库单
     */
    public int deleteStockInByIds(Long[] stockInIds);

    /**
     * 审核入库单
     */
    public int auditStockIn(BizStockIn stockIn);

    /**
     * 查询入库明细列表
     */
    public List<BizStockInDetail> selectDetailByStockInId(Long stockInId);
}
