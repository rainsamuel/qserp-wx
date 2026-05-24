package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizAssetChange;

/**
 * 资产变更记录Service接口
 */
public interface IBizAssetChangeService
{
    /**
     * 查询资产变更记录
     */
    public BizAssetChange selectAssetChangeById(Long changeId);

    /**
     * 查询资产变更记录列表
     */
    public List<BizAssetChange> selectAssetChangeList(BizAssetChange assetChange);

    /**
     * 新增资产变更记录
     */
    public int insertAssetChange(BizAssetChange assetChange);

    /**
     * 修改资产变更记录
     */
    public int updateAssetChange(BizAssetChange assetChange);

    /**
     * 批量删除资产变更记录
     */
    public int deleteAssetChangeByIds(Long[] changeIds);

    /**
     * 删除资产变更记录信息
     */
    public int deleteAssetChangeById(Long changeId);
}
