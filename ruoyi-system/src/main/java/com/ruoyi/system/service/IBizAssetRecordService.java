package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizAssetRecord;

/**
 * 资产流转记录 服务层
 */
public interface IBizAssetRecordService
{
    /**
     * 查询资产流转记录列表
     */
    public List<BizAssetRecord> selectAssetRecordList(BizAssetRecord record);

    /**
     * 根据ID查询资产流转记录
     */
    public BizAssetRecord selectAssetRecordById(Long recordId);

    /**
     * 根据物资ID查询流转记录
     */
    public List<BizAssetRecord> selectAssetRecordByMaterialId(Long materialId);

    /**
     * 新增资产流转记录
     */
    public int insertAssetRecord(BizAssetRecord record);

    /**
     * 修改资产流转记录
     */
    public int updateAssetRecord(BizAssetRecord record);

    /**
     * 删除资产流转记录
     */
    public int deleteAssetRecordById(Long recordId);

    /**
     * 批量删除资产流转记录
     */
    public int deleteAssetRecordByIds(Long[] recordIds);

    /**
     * 资产入库
     */
    public int doStockIn(BizAssetRecord record);

    /**
     * 资产出库
     */
    public int doStockOut(BizAssetRecord record);

    /**
     * 资产报损
     */
    public int doDamage(BizAssetRecord record);

    /**
     * 资产报废
     */
    public int doScrap(BizAssetRecord record);

    /**
     * 统计各类型流转数量
     */
    public List<BizAssetRecord> selectRecordTypeStats(Long materialId);
}
