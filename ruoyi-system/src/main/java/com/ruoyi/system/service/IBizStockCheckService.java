package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizStockCheck;
import com.ruoyi.system.domain.BizStockCheckDetail;

/**
 * 盘点单Service接口
 */
public interface IBizStockCheckService
{
    /**
     * 查询盘点单
     */
    public BizStockCheck selectBizStockCheckById(Long checkId);

    /**
     * 查询盘点单列表
     */
    public List<BizStockCheck> selectBizStockCheckList(BizStockCheck bizStockCheck);

    /**
     * 查询盘点明细列表
     */
    public List<BizStockCheckDetail> selectDetailByCheckId(Long checkId);

    /**
     * 新增盘点单
     */
    public int insertBizStockCheck(BizStockCheck bizStockCheck);

    /**
     * 修改盘点单
     */
    public int updateBizStockCheck(BizStockCheck bizStockCheck);

    /**
     * 删除盘点单
     */
    public int deleteBizStockCheckByIds(Long[] checkIds);

    /**
     * 完成盘点（更新库存）
     */
    public int completeBizStockCheck(BizStockCheck bizStockCheck);
}
