package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizStockInDetail;

/**
 * 入库明细Mapper接口
 */
public interface BizStockInDetailMapper
{
    /**
     * 查询入库明细
     */
    public BizStockInDetail selectBizStockInDetailById(Long detailId);

    /**
     * 查询入库明细列表
     */
    public List<BizStockInDetail> selectBizStockInDetailByStockInId(Long stockInId);

    /**
     * 新增入库明细
     */
    public int insertBizStockInDetail(BizStockInDetail bizStockInDetail);

    /**
     * 批量新增入库明细
     */
    public int batchInsertBizStockInDetail(List<BizStockInDetail> list);

    /**
     * 修改入库明细
     */
    public int updateBizStockInDetail(BizStockInDetail bizStockInDetail);

    /**
     * 删除入库明细
     */
    public int deleteBizStockInDetailById(Long detailId);

    /**
     * 批量删除入库明细
     */
    public int deleteBizStockInDetailByIds(Long[] detailIds);

    /**
     * 根据入库单ID删除明细
     */
    public int deleteBizStockInDetailByStockInId(Long stockInId);
}
