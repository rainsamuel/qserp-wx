package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizStockCheck;
import com.ruoyi.system.domain.BizStockCheckDetail;
import com.ruoyi.system.domain.BizMaterial;
import com.ruoyi.system.mapper.BizStockCheckMapper;
import com.ruoyi.system.mapper.BizStockCheckDetailMapper;
import com.ruoyi.system.mapper.BizMaterialMapper;
import com.ruoyi.system.service.IBizStockCheckService;

/**
 * 盘点单Service业务层处理
 */
@Service
public class BizStockCheckServiceImpl implements IBizStockCheckService
{
    @Autowired
    private BizStockCheckMapper stockCheckMapper;

    @Autowired
    private BizStockCheckDetailMapper detailMapper;

    @Autowired
    private BizMaterialMapper materialMapper;

    @Override
    public BizStockCheck selectBizStockCheckById(Long checkId)
    {
        return stockCheckMapper.selectBizStockCheckById(checkId);
    }

    @Override
    public List<BizStockCheck> selectBizStockCheckList(BizStockCheck bizStockCheck)
    {
        return stockCheckMapper.selectBizStockCheckList(bizStockCheck);
    }

    @Override
    public List<BizStockCheckDetail> selectDetailByCheckId(Long checkId)
    {
        return detailMapper.selectDetailByCheckId(checkId);
    }

    @Override
    @Transactional
    public int insertBizStockCheck(BizStockCheck bizStockCheck)
    {
        // 生成盘点单号
        if (StringUtils.isEmpty(bizStockCheck.getCheckNo()))
        {
            bizStockCheck.setCheckNo("PD" + System.currentTimeMillis());
        }
        // 默认状态为盘点中
        if (StringUtils.isEmpty(bizStockCheck.getStatus()))
        {
            bizStockCheck.setStatus("0");
        }
        // 插入盘点单
        int rows = stockCheckMapper.insertBizStockCheck(bizStockCheck);
        // 插入盘点明细
        if (StringUtils.isNotNull(bizStockCheck.getDetailList()) && !bizStockCheck.getDetailList().isEmpty())
        {
            for (BizStockCheckDetail detail : bizStockCheck.getDetailList())
            {
                detail.setCheckId(bizStockCheck.getCheckId());
                detail.setCreateBy(bizStockCheck.getCreateBy());
                // 计算差异数量和金额
                if (detail.getActualQuantity() != null && detail.getSystemQuantity() != null)
                {
                    int diffQty = detail.getActualQuantity() - detail.getSystemQuantity();
                    detail.setDiffQuantity(diffQty);
                    if (detail.getUnitPrice() != null)
                    {
                        detail.setDiffAmount(detail.getUnitPrice().multiply(new BigDecimal(diffQty)));
                    }
                }
                detailMapper.insertBizStockCheckDetail(detail);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateBizStockCheck(BizStockCheck bizStockCheck)
    {
        // 先删除原有明细，再重新插入
        if (StringUtils.isNotNull(bizStockCheck.getDetailList()))
        {
            detailMapper.deleteDetailByCheckId(bizStockCheck.getCheckId());
            BigDecimal totalDiffAmount = BigDecimal.ZERO;
            for (BizStockCheckDetail detail : bizStockCheck.getDetailList())
            {
                detail.setCheckId(bizStockCheck.getCheckId());
                detail.setUpdateBy(bizStockCheck.getUpdateBy());
                // 计算差异数量和金额
                if (detail.getActualQuantity() != null && detail.getSystemQuantity() != null)
                {
                    int diffQty = detail.getActualQuantity() - detail.getSystemQuantity();
                    detail.setDiffQuantity(diffQty);
                    if (detail.getUnitPrice() != null)
                    {
                        BigDecimal diffAmt = detail.getUnitPrice().multiply(new BigDecimal(diffQty));
                        detail.setDiffAmount(diffAmt);
                        totalDiffAmount = totalDiffAmount.add(diffAmt);
                    }
                }
                detailMapper.insertBizStockCheckDetail(detail);
            }
            bizStockCheck.setTotalDiffAmount(totalDiffAmount);
        }
        return stockCheckMapper.updateBizStockCheck(bizStockCheck);
    }

    @Override
    @Transactional
    public int deleteBizStockCheckByIds(Long[] checkIds)
    {
        for (Long checkId : checkIds)
        {
            BizStockCheck check = stockCheckMapper.selectBizStockCheckById(checkId);
            if ("1".equals(check.getStatus()))
            {
                throw new ServiceException("已完成的盘点单不能删除");
            }
            // 先删除明细
            detailMapper.deleteDetailByCheckId(checkId);
        }
        return stockCheckMapper.deleteBizStockCheckByIds(checkIds);
    }

    @Override
    @Transactional
    public int completeBizStockCheck(BizStockCheck bizStockCheck)
    {
        BizStockCheck check = stockCheckMapper.selectBizStockCheckById(bizStockCheck.getCheckId());
        if (check == null)
        {
            throw new ServiceException("盘点单不存在");
        }
        if ("1".equals(check.getStatus()))
        {
            throw new ServiceException("盘点单已完成，不能重复操作");
        }

        List<BizStockCheckDetail> details = detailMapper.selectDetailByCheckId(bizStockCheck.getCheckId());
        BigDecimal totalDiffAmount = BigDecimal.ZERO;

        for (BizStockCheckDetail detail : details)
        {
            if (detail.getActualQuantity() == null)
            {
                throw new ServiceException("存在未录入实际数量的盘点项，请先完成盘点录入");
            }

            // 计算差异
            int diffQty = detail.getActualQuantity() - detail.getSystemQuantity();
            detail.setDiffQuantity(diffQty);
            if (detail.getUnitPrice() != null)
            {
                BigDecimal diffAmt = detail.getUnitPrice().multiply(new BigDecimal(diffQty));
                detail.setDiffAmount(diffAmt);
                totalDiffAmount = totalDiffAmount.add(diffAmt);
            }

            // 更新明细的差异信息
            detailMapper.updateBizStockCheckDetail(detail);

            // 更新物资库存数量（盘盈入库、盘亏出库）
            if (detail.getMaterialId() != null && diffQty != 0)
            {
                BizMaterial material = materialMapper.selectMaterialById(detail.getMaterialId());
                if (material != null)
                {
                    int newStock = (material.getStockQuantity() != null ? material.getStockQuantity() : 0) + diffQty;
                    if (newStock < 0)
                    {
                        newStock = 0;
                    }
                    material.setStockQuantity(newStock);
                    materialMapper.updateMaterial(material);
                }
            }
        }

        // 更新盘点单状态为已完成
        check.setStatus("1");
        check.setTotalDiffAmount(totalDiffAmount);
        check.setUpdateBy(bizStockCheck.getUpdateBy());
        return stockCheckMapper.updateBizStockCheck(check);
    }
}
