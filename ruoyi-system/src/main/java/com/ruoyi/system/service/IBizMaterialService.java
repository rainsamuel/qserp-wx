package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizMaterial;
import com.ruoyi.system.domain.BizMaterialCategory;

/**
 * 物资信息 服务层
 */
public interface IBizMaterialService
{
    /**
     * 查询物资列表
     */
    public List<BizMaterial> selectMaterialList(BizMaterial material);

    /**
     * 根据ID查询物资
     */
    public BizMaterial selectMaterialById(Long materialId);

    /**
     * 校验物资编码是否唯一
     */
    public boolean checkMaterialCodeUnique(BizMaterial material);

    /**
     * 新增物资
     */
    public int insertMaterial(BizMaterial material);

    /**
     * 修改物资
     */
    public int updateMaterial(BizMaterial material);

    /**
     * 删除物资
     */
    public int deleteMaterialById(Long materialId);

    /**
     * 批量删除物资
     */
    public int deleteMaterialByIds(Long[] materialIds);

    // ========== 物资分类 ==========

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
}
