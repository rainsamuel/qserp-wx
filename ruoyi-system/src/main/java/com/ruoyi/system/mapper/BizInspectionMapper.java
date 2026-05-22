package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizInspection;

/**
 * 巡检记录 数据层
 */
public interface BizInspectionMapper
{
    /**
     * 查询巡检记录列表
     */
    public List<BizInspection> selectInspectionList(BizInspection inspection);

    /**
     * 根据ID查询巡检记录
     */
    public BizInspection selectInspectionById(Long inspectionId);

    /**
     * 新增巡检记录
     */
    public int insertInspection(BizInspection inspection);

    /**
     * 修改巡检记录
     */
    public int updateInspection(BizInspection inspection);

    /**
     * 删除巡检记录
     */
    public int deleteInspectionById(Long inspectionId);

    /**
     * 批量删除巡检记录
     */
    public int deleteInspectionByIds(Long[] inspectionIds);
}
