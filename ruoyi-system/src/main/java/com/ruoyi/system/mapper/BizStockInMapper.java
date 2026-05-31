package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizStockIn;

/**
 * 入库单Mapper接口
 */
public interface BizStockInMapper
{
    /**
     * 查询入库单
     */
    public BizStockIn selectBizStockInById(Long stockInId);

    /**
     * 查询入库单列表
     */
    public List<BizStockIn> selectBizStockInList(BizStockIn bizStockIn);

    /**
     * 新增入库单
     */
    public int insertBizStockIn(BizStockIn bizStockIn);

    /**
     * 修改入库单
     */
    public int updateBizStockIn(BizStockIn bizStockIn);

    /**
     * 删除入库单
     */
    public int deleteBizStockInById(Long stockInId);

    /**
     * 批量删除入库单
     */
    public int deleteBizStockInByIds(Long[] stockInIds);
}
