package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizStockCheck;

/**
 * 盘点单Mapper接口
 */
public interface BizStockCheckMapper
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
    public int deleteBizStockCheckById(Long checkId);

    /**
     * 批量删除盘点单
     */
    public int deleteBizStockCheckByIds(Long[] checkIds);
}
