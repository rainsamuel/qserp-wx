package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizManual;

/**
 * 使用说明书 数据层
 */
public interface BizManualMapper
{
    /**
     * 查询说明书列表
     */
    public List<BizManual> selectManualList(BizManual manual);

    /**
     * 根据ID查询说明书
     */
    public BizManual selectManualById(Long manualId);

    /**
     * 新增说明书
     */
    public int insertManual(BizManual manual);

    /**
     * 修改说明书
     */
    public int updateManual(BizManual manual);

    /**
     * 删除说明书
     */
    public int deleteManualById(Long manualId);

    /**
     * 批量删除说明书
     */
    public int deleteManualByIds(Long[] manualIds);
}
