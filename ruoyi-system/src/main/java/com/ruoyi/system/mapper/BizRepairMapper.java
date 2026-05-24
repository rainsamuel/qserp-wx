package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizRepair;

/**
 * 报修记录Mapper接口
 */
public interface BizRepairMapper
{
    /**
     * 查询报修记录
     */
    public BizRepair selectRepairById(Long repairId);

    /**
     * 查询报修记录列表
     */
    public List<BizRepair> selectRepairList(BizRepair repair);

    /**
     * 新增报修记录
     */
    public int insertRepair(BizRepair repair);

    /**
     * 修改报修记录
     */
    public int updateRepair(BizRepair repair);

    /**
     * 删除报修记录
     */
    public int deleteRepairById(Long repairId);

    /**
     * 批量删除报修记录
     */
    public int deleteRepairByIds(Long[] repairIds);
}
