package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
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
     * 根据资产编码查询物资
     */
    public BizMaterial selectMaterialByAssetCode(String assetCode);

    /**
     * 根据第三方同步ID查询物资
     */
    public BizMaterial selectMaterialBySyncId(@Param("syncId") String syncId, @Param("orgCode") String orgCode);

    /**
     * 根据第三方同步ID查询物资（仅按syncId）
     */
    public BizMaterial selectMaterialBySyncIdOnly(@Param("syncId") String syncId);

    /**
     * 根据卡片编号查询物资
     */
    public BizMaterial selectMaterialByKpbh(String kpbh);

    /**
     * 根据二维码查询物资（支持多种格式）
     */
    public BizMaterial selectMaterialByQRCode(@Param("code") String code, @Param("codeType") String codeType, @Param("syncId") String syncId, @Param("orgCode") String orgCode);

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
