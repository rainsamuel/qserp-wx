package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizStockCheckDetail;

/**
 * 盘点明细Mapper接口
 */
public interface BizStockCheckDetailMapper
{
    /**
     * 查询盘点明细
     */
    public BizStockCheckDetail selectBizStockCheckDetailById(Long detailId);

    /**
     * 查询盘点明细列表
     */
    public List<BizStockCheckDetail> selectBizStockCheckDetailList(BizStockCheckDetail detail);

    /**
     * 根据盘点单ID查询明细列表
     */
    public List<BizStockCheckDetail> selectDetailByCheckId(Long checkId);

    /**
     * 新增盘点明细
     */
    public int insertBizStockCheckDetail(BizStockCheckDetail detail);

    /**
     * 修改盘点明细
     */
    public int updateBizStockCheckDetail(BizStockCheckDetail detail);

    /**
     * 删除盘点明细
     */
    public int deleteBizStockCheckDetailById(Long detailId);

    /**
     * 根据盘点单ID删除明细
     */
    public int deleteDetailByCheckId(Long checkId);

    /**
     * 批量删除盘点明细
     */
    public int deleteBizStockCheckDetailByIds(Long[] detailIds);
}
