package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizAssetChangeMapper;
import com.ruoyi.system.domain.BizAssetChange;
import com.ruoyi.system.service.IBizAssetChangeService;

/**
 * 资产变更记录Service业务层处理
 */
@Service
public class BizAssetChangeServiceImpl implements IBizAssetChangeService
{
    @Autowired
    private BizAssetChangeMapper assetChangeMapper;

    /**
     * 查询资产变更记录
     */
    @Override
    public BizAssetChange selectAssetChangeById(Long changeId)
    {
        return assetChangeMapper.selectAssetChangeById(changeId);
    }

    /**
     * 查询资产变更记录列表
     */
    @Override
    public List<BizAssetChange> selectAssetChangeList(BizAssetChange assetChange)
    {
        return assetChangeMapper.selectAssetChangeList(assetChange);
    }

    /**
     * 新增资产变更记录
     */
    @Override
    public int insertAssetChange(BizAssetChange assetChange)
    {
        return assetChangeMapper.insertAssetChange(assetChange);
    }

    /**
     * 修改资产变更记录
     */
    @Override
    public int updateAssetChange(BizAssetChange assetChange)
    {
        return assetChangeMapper.updateAssetChange(assetChange);
    }

    /**
     * 批量删除资产变更记录
     */
    @Override
    public int deleteAssetChangeByIds(Long[] changeIds)
    {
        return assetChangeMapper.deleteAssetChangeByIds(changeIds);
    }

    /**
     * 删除资产变更记录信息
     */
    @Override
    public int deleteAssetChangeById(Long changeId)
    {
        return assetChangeMapper.deleteAssetChangeById(changeId);
    }
}
