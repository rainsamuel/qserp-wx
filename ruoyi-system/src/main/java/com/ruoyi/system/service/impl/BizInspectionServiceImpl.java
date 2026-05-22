package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.BizInspection;
import com.ruoyi.system.domain.BizInspectionDetail;
import com.ruoyi.system.domain.BizInspectionItem;
import com.ruoyi.system.mapper.BizInspectionDetailMapper;
import com.ruoyi.system.mapper.BizInspectionItemMapper;
import com.ruoyi.system.mapper.BizInspectionMapper;
import com.ruoyi.system.service.IBizInspectionService;

/**
 * 巡检管理 服务层处理
 */
@Service
public class BizInspectionServiceImpl implements IBizInspectionService
{
    @Autowired
    private BizInspectionMapper inspectionMapper;

    @Autowired
    private BizInspectionDetailMapper detailMapper;

    @Autowired
    private BizInspectionItemMapper itemMapper;

    // ========== 巡检记录 ==========

    @Override
    public List<BizInspection> selectInspectionList(BizInspection inspection)
    {
        return inspectionMapper.selectInspectionList(inspection);
    }

    @Override
    public BizInspection selectInspectionById(Long inspectionId)
    {
        BizInspection inspection = inspectionMapper.selectInspectionById(inspectionId);
        if (inspection != null)
        {
            List<BizInspectionDetail> details = detailMapper.selectDetailByInspectionId(inspectionId);
            inspection.setDetails(details);
        }
        return inspection;
    }

    @Override
    @Transactional
    public int insertInspection(BizInspection inspection)
    {
        // 插入巡检记录
        int rows = inspectionMapper.insertInspection(inspection);

        // 插入巡检明细
        List<Long> itemIds = inspection.getItemIds();
        if (itemIds != null && !itemIds.isEmpty())
        {
            List<BizInspectionDetail> details = new ArrayList<>();
            for (Long itemId : itemIds)
            {
                BizInspectionDetail detail = new BizInspectionDetail();
                detail.setInspectionId(inspection.getInspectionId());
                detail.setItemId(itemId);
                detail.setCheckResult("normal");
                details.add(detail);
            }
            detailMapper.batchInsertDetail(details);
        }

        // 处理明细中的检查结果和备注
        if (inspection.getDetails() != null)
        {
            for (BizInspectionDetail detail : inspection.getDetails())
            {
                detail.setInspectionId(inspection.getInspectionId());
            }
            // 如果有详细结果，更新明细
            updateDetailResults(inspection);
        }

        return rows;
    }

    @Override
    @Transactional
    public int updateInspection(BizInspection inspection)
    {
        // 更新巡检记录
        int rows = inspectionMapper.updateInspection(inspection);

        // 删除旧明细，插入新明细
        detailMapper.deleteDetailByInspectionId(inspection.getInspectionId());

        List<Long> itemIds = inspection.getItemIds();
        if (itemIds != null && !itemIds.isEmpty())
        {
            List<BizInspectionDetail> details = new ArrayList<>();
            for (Long itemId : itemIds)
            {
                BizInspectionDetail detail = new BizInspectionDetail();
                detail.setInspectionId(inspection.getInspectionId());
                detail.setItemId(itemId);
                detail.setCheckResult("normal");
                details.add(detail);
            }
            detailMapper.batchInsertDetail(details);
        }

        if (inspection.getDetails() != null)
        {
            updateDetailResults(inspection);
        }

        return rows;
    }

    @Override
    @Transactional
    public int deleteInspectionById(Long inspectionId)
    {
        detailMapper.deleteDetailByInspectionId(inspectionId);
        return inspectionMapper.deleteInspectionById(inspectionId);
    }

    @Override
    @Transactional
    public int deleteInspectionByIds(Long[] inspectionIds)
    {
        for (Long inspectionId : inspectionIds)
        {
            detailMapper.deleteDetailByInspectionId(inspectionId);
        }
        return inspectionMapper.deleteInspectionByIds(inspectionIds);
    }

    // ========== 巡检内容项 ==========

    @Override
    public List<BizInspectionItem> selectItemList(BizInspectionItem item)
    {
        return itemMapper.selectItemList(item);
    }

    @Override
    public List<BizInspectionItem> selectItemAll()
    {
        return itemMapper.selectItemAll();
    }

    @Override
    public BizInspectionItem selectItemById(Long itemId)
    {
        return itemMapper.selectItemById(itemId);
    }

    @Override
    public int insertItem(BizInspectionItem item)
    {
        return itemMapper.insertItem(item);
    }

    @Override
    public int updateItem(BizInspectionItem item)
    {
        return itemMapper.updateItem(item);
    }

    @Override
    public int deleteItemByIds(Long[] itemIds)
    {
        return itemMapper.deleteItemByIds(itemIds);
    }

    /**
     * 更新明细检查结果
     */
    private void updateDetailResults(BizInspection inspection)
    {
        List<BizInspectionDetail> details = detailMapper.selectDetailByInspectionId(inspection.getInspectionId());
        for (BizInspectionDetail newDetail : inspection.getDetails())
        {
            for (BizInspectionDetail existing : details)
            {
                if (existing.getItemId().equals(newDetail.getItemId()))
                {
                    existing.setCheckResult(newDetail.getCheckResult());
                    existing.setCheckRemark(newDetail.getCheckRemark());
                    // 此处可扩展为update语句，当前通过删除重插实现
                    break;
                }
            }
        }
    }
}
