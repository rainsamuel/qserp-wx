package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.BizAssetRecord;
import com.ruoyi.system.domain.BizMaterial;
import com.ruoyi.system.mapper.BizAssetRecordMapper;
import com.ruoyi.system.mapper.BizMaterialMapper;
import com.ruoyi.system.service.IBizAssetRecordService;

/**
 * 资产流转记录 服务实现
 */
@Service
public class BizAssetRecordServiceImpl implements IBizAssetRecordService
{
    @Autowired
    private BizAssetRecordMapper assetRecordMapper;

    @Autowired
    private BizMaterialMapper materialMapper;

    @Override
    public List<BizAssetRecord> selectAssetRecordList(BizAssetRecord record)
    {
        return assetRecordMapper.selectAssetRecordList(record);
    }

    @Override
    public BizAssetRecord selectAssetRecordById(Long recordId)
    {
        return assetRecordMapper.selectAssetRecordById(recordId);
    }

    @Override
    public List<BizAssetRecord> selectAssetRecordByMaterialId(Long materialId)
    {
        return assetRecordMapper.selectAssetRecordByMaterialId(materialId);
    }

    @Override
    public int insertAssetRecord(BizAssetRecord record)
    {
        return assetRecordMapper.insertAssetRecord(record);
    }

    @Override
    public int updateAssetRecord(BizAssetRecord record)
    {
        return assetRecordMapper.updateAssetRecord(record);
    }

    @Override
    public int deleteAssetRecordById(Long recordId)
    {
        return assetRecordMapper.deleteAssetRecordById(recordId);
    }

    @Override
    public int deleteAssetRecordByIds(Long[] recordIds)
    {
        return assetRecordMapper.deleteAssetRecordByIds(recordIds);
    }

    @Override
    @Transactional
    public int doStockIn(BizAssetRecord record)
    {
        record.setRecordType("IN");
        // 入库：增加目标仓库库存
        BizMaterial material = materialMapper.selectMaterialById(record.getMaterialId());
        if (material == null)
        {
            throw new RuntimeException("物资不存在");
        }
        // 更新物资库存
        BizMaterial update = new BizMaterial();
        update.setMaterialId(record.getMaterialId());
        update.setStockQuantity(material.getStockQuantity() + record.getQuantity());
        // 如果指定了目标仓库，更新仓库
        if (record.getToWarehouseId() != null)
        {
            update.setWarehouseId(record.getToWarehouseId());
        }
        materialMapper.updateMaterial(update);
        return assetRecordMapper.insertAssetRecord(record);
    }

    @Override
    @Transactional
    public int doStockOut(BizAssetRecord record)
    {
        record.setRecordType("OUT");
        BizMaterial material = materialMapper.selectMaterialById(record.getMaterialId());
        if (material == null)
        {
            throw new RuntimeException("物资不存在");
        }
        if (material.getStockQuantity() < record.getQuantity())
        {
            throw new RuntimeException("库存不足，当前库存：" + material.getStockQuantity());
        }
        // 减少库存
        BizMaterial update = new BizMaterial();
        update.setMaterialId(record.getMaterialId());
        update.setStockQuantity(material.getStockQuantity() - record.getQuantity());
        materialMapper.updateMaterial(update);
        // 记录来源仓库
        if (record.getFromWarehouseId() == null)
        {
            record.setFromWarehouseId(material.getWarehouseId());
        }
        return assetRecordMapper.insertAssetRecord(record);
    }

    @Override
    @Transactional
    public int doDamage(BizAssetRecord record)
    {
        record.setRecordType("DAMAGE");
        BizMaterial material = materialMapper.selectMaterialById(record.getMaterialId());
        if (material == null)
        {
            throw new RuntimeException("物资不存在");
        }
        if (material.getStockQuantity() < record.getQuantity())
        {
            throw new RuntimeException("库存不足，当前库存：" + material.getStockQuantity());
        }
        // 报损减少库存
        BizMaterial update = new BizMaterial();
        update.setMaterialId(record.getMaterialId());
        update.setStockQuantity(material.getStockQuantity() - record.getQuantity());
        materialMapper.updateMaterial(update);
        if (record.getFromWarehouseId() == null)
        {
            record.setFromWarehouseId(material.getWarehouseId());
        }
        return assetRecordMapper.insertAssetRecord(record);
    }

    @Override
    @Transactional
    public int doScrap(BizAssetRecord record)
    {
        record.setRecordType("SCRAP");
        BizMaterial material = materialMapper.selectMaterialById(record.getMaterialId());
        if (material == null)
        {
            throw new RuntimeException("物资不存在");
        }
        if (material.getStockQuantity() < record.getQuantity())
        {
            throw new RuntimeException("库存不足，当前库存：" + material.getStockQuantity());
        }
        // 报废减少库存
        BizMaterial update = new BizMaterial();
        update.setMaterialId(record.getMaterialId());
        update.setStockQuantity(material.getStockQuantity() - record.getQuantity());
        materialMapper.updateMaterial(update);
        if (record.getFromWarehouseId() == null)
        {
            record.setFromWarehouseId(material.getWarehouseId());
        }
        return assetRecordMapper.insertAssetRecord(record);
    }

    @Override
    public List<BizAssetRecord> selectRecordTypeStats(Long materialId)
    {
        BizAssetRecord query = new BizAssetRecord();
        query.setMaterialId(materialId);
        return assetRecordMapper.selectRecordTypeStats(query);
    }
}
