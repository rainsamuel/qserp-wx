package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizRepair;

/**
 * 报修记录Service接口
 */
public interface IBizRepairService
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
     * 批量删除报修记录
     */
    public int deleteRepairByIds(Long[] repairIds);

    /**
     * 删除报修记录信息
     */
    public int deleteRepairById(Long repairId);
}
