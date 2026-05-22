package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizMaterial;

/**
 * 物资信息 数据层
 */
public interface BizMaterialMapper
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
     * 根据编码查询物资
     */
    public BizMaterial selectMaterialByCode(String materialCode);

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
}
