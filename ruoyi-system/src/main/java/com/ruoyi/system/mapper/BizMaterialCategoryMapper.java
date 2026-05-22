package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizMaterialCategory;

/**
 * 物资分类 数据层
 */
public interface BizMaterialCategoryMapper
{
    /**
     * 查询分类列表
     */
    public List<BizMaterialCategory> selectCategoryList(BizMaterialCategory category);

    /**
     * 查询所有分类
     */
    public List<BizMaterialCategory> selectCategoryAll();

    /**
     * 根据ID查询分类
     */
    public BizMaterialCategory selectCategoryById(Long categoryId);

    /**
     * 新增分类
     */
    public int insertCategory(BizMaterialCategory category);

    /**
     * 修改分类
     */
    public int updateCategory(BizMaterialCategory category);

    /**
     * 删除分类
     */
    public int deleteCategoryById(Long categoryId);

    /**
     * 批量删除分类
     */
    public int deleteCategoryByIds(Long[] categoryIds);

    /**
     * 查询子分类数量
     */
    public int countChildCategoryById(Long categoryId);
}
