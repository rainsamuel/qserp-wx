package com.ruoyi.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.system.service.IBizMaterialService;

/**
 * 资产数据同步定时任务
 *
 * 调用示例：assetSyncTask.syncOracleAsset()
 * 建议Cron表达式：0 0 2 * * ? （每天凌晨2点执行）
 *
 * @author ruoyi
 */
@Component("assetSyncTask")
public class AssetSyncTask
{
    private static final Logger log = LoggerFactory.getLogger(AssetSyncTask.class);

    @Autowired
    private IBizMaterialService materialService;

    /**
     * 从Oracle同步资产数据（无参）
     */
    public void syncOracleAsset()
    {
        log.info("开始执行Oracle资产数据同步任务...");
        try
        {
            int count = materialService.syncMaterialFromOracle();
            log.info("Oracle资产数据同步任务完成，共同步{}条记录", count);
        }
        catch (Exception e)
        {
            log.error("Oracle资产数据同步任务执行失败", e);
        }
    }
}
