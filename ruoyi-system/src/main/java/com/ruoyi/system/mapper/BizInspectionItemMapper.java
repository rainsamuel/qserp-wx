package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizInspectionItem;

/**
 * 巡检内容项 数据层
 */
public interface BizInspectionItemMapper
{
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
    public int deleteItemById(Long itemId);

    /**
     * 批量删除检查项
     */
    public int deleteItemByIds(Long[] itemIds);
}
