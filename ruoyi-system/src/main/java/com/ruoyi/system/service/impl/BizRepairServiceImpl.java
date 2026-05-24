package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizRepairMapper;
import com.ruoyi.system.domain.BizRepair;
import com.ruoyi.system.service.IBizRepairService;

/**
 * 报修记录Service业务层处理
 */
@Service
public class BizRepairServiceImpl implements IBizRepairService
{
    @Autowired
    private BizRepairMapper repairMapper;

    /**
     * 查询报修记录
     */
    @Override
    public BizRepair selectRepairById(Long repairId)
    {
        return repairMapper.selectRepairById(repairId);
    }

    /**
     * 查询报修记录列表
     */
    @Override
    public List<BizRepair> selectRepairList(BizRepair repair)
    {
        return repairMapper.selectRepairList(repair);
    }

    /**
     * 新增报修记录
     */
    @Override
    public int insertRepair(BizRepair repair)
    {
        return repairMapper.insertRepair(repair);
    }

    /**
     * 修改报修记录
     */
    @Override
    public int updateRepair(BizRepair repair)
    {
        return repairMapper.updateRepair(repair);
    }

    /**
     * 批量删除报修记录
     */
    @Override
    public int deleteRepairByIds(Long[] repairIds)
    {
        return repairMapper.deleteRepairByIds(repairIds);
    }

    /**
     * 删除报修记录信息
     */
    @Override
    public int deleteRepairById(Long repairId)
    {
        return repairMapper.deleteRepairById(repairId);
    }
}
