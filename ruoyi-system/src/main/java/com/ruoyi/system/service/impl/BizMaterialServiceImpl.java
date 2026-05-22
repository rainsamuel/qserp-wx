package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizMaterial;
import com.ruoyi.system.domain.BizMaterialCategory;
import com.ruoyi.system.mapper.BizMaterialCategoryMapper;
import com.ruoyi.system.mapper.BizMaterialMapper;
import com.ruoyi.system.service.IBizMaterialService;

/**
 * 物资信息 服务层处理
 */
@Service
public class BizMaterialServiceImpl implements IBizMaterialService
{
    @Autowired
    private BizMaterialMapper materialMapper;

    @Autowired
    private BizMaterialCategoryMapper categoryMapper;

    // ========== 物资管理 ==========

    @Override
    public List<BizMaterial> selectMaterialList(BizMaterial material)
    {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public BizMaterial selectMaterialById(Long materialId)
    {
        return materialMapper.selectMaterialById(materialId);
    }

    @Override
    public boolean checkMaterialCodeUnique(BizMaterial material)
    {
        Long materialId = StringUtils.isNull(material.getMaterialId()) ? -1L : material.getMaterialId();
        BizMaterial info = materialMapper.selectMaterialByCode(material.getMaterialCode());
        if (StringUtils.isNotNull(info) && info.getMaterialId().longValue() != materialId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertMaterial(BizMaterial material)
    {
        return materialMapper.insertMaterial(material);
    }

    @Override
    public int updateMaterial(BizMaterial material)
    {
        return materialMapper.updateMaterial(material);
    }

    @Override
    public int deleteMaterialById(Long materialId)
    {
        return materialMapper.deleteMaterialById(materialId);
    }

    @Override
    public int deleteMaterialByIds(Long[] materialIds)
    {
        return materialMapper.deleteMaterialByIds(materialIds);
    }

    // ========== 物资分类管理 ==========

    @Override
    public List<BizMaterialCategory> selectCategoryList(BizMaterialCategory category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    @Override
    public List<BizMaterialCategory> selectCategoryAll()
    {
        return categoryMapper.selectCategoryAll();
    }

    @Override
    public BizMaterialCategory selectCategoryById(Long categoryId)
    {
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public int insertCategory(BizMaterialCategory category)
    {
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(BizMaterialCategory category)
    {
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryById(Long categoryId)
    {
        if (categoryMapper.countChildCategoryById(categoryId) > 0)
        {
            throw new ServiceException("存在下级分类,不允许删除");
        }
        return categoryMapper.deleteCategoryById(categoryId);
    }

    @Override
    public int deleteCategoryByIds(Long[] categoryIds)
    {
        for (Long categoryId : categoryIds)
        {
            if (categoryMapper.countChildCategoryById(categoryId) > 0)
            {
                BizMaterialCategory category = categoryMapper.selectCategoryById(categoryId);
                throw new ServiceException("'" + category.getCategoryName() + "'存在下级分类,不允许删除");
            }
        }
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }
}
