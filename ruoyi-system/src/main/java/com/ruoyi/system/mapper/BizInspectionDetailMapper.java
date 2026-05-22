package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizInspectionDetail;

/**
 * 巡检明细 数据层
 */
public interface BizInspectionDetailMapper
{
    /**
     * 根据巡检ID查询明细列表
     */
    public List<BizInspectionDetail> selectDetailByInspectionId(Long inspectionId);

    /**
     * 新增明细
     */
    public int insertDetail(BizInspectionDetail detail);

    /**
     * 批量新增明细
     */
    public int batchInsertDetail(List<BizInspectionDetail> details);

    /**
     * 根据巡检ID删除明细
     */
    public int deleteDetailByInspectionId(Long inspectionId);
}
