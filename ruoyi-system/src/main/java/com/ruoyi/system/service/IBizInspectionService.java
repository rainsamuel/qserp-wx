package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizInspection;
import com.ruoyi.system.domain.BizInspectionItem;

/**
 * 巡检管理 服务层
 */
public interface IBizInspectionService
{
    // ========== 巡检记录 ==========

    /**
     * 查询巡检记录列表
     */
    public List<BizInspection> selectInspectionList(BizInspection inspection);

    /**
     * 根据ID查询巡检记录（含明细）
     */
    public BizInspection selectInspectionById(Long inspectionId);

    /**
     * 新增巡检记录（含明细）
     */
    public int insertInspection(BizInspection inspection);

    /**
     * 修改巡检记录（含明细）
     */
    public int updateInspection(BizInspection inspection);

    /**
     * 删除巡检记录（含明细）
     */
    public int deleteInspectionById(Long inspectionId);

    /**
     * 批量删除巡检记录
     */
    public int deleteInspectionByIds(Long[] inspectionIds);

    // ========== 巡检内容项 ==========

    /**
     * 查询检查项列表
     */
    public List<BizInspectionItem> selectItemList(BizInspectionItem item);

    /**
     * 查询所有正常检查项
     */
    public List<BizInspectionItem> selectItemAll();

    /**
     * 根据ID查询检查项
     */
    public BizInspectionItem selectItemById(Long itemId);

    /**
     * 新增检查项
     */
    public int insertItem(BizInspectionItem item);

    /**
     * 修改检查项
     */
    public int updateItem(BizInspectionItem item);

    /**
     * 删除检查项
     */
    public int deleteItemByIds(Long[] itemIds);
}
