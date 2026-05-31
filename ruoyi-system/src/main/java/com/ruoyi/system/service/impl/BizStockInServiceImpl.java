package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizStockIn;
import com.ruoyi.system.domain.BizStockInDetail;
import com.ruoyi.system.mapper.BizStockInDetailMapper;
import com.ruoyi.system.mapper.BizStockInMapper;
import com.ruoyi.system.service.IBizStockInService;

/**
 * 入库单 服务层处理
 */
@Service
public class BizStockInServiceImpl implements IBizStockInService
{
    @Autowired
    private BizStockInMapper stockInMapper;

    @Autowired
    private BizStockInDetailMapper stockInDetailMapper;

    @Override
    public List<BizStockIn> selectStockInList(BizStockIn stockIn)
    {
        return stockInMapper.selectBizStockInList(stockIn);
    }

    @Override
    public BizStockIn selectStockInById(Long stockInId)
    {
        BizStockIn stockIn = stockInMapper.selectBizStockInById(stockInId);
        if (StringUtils.isNotNull(stockIn))
        {
            stockIn.setDetailList(stockInDetailMapper.selectBizStockInDetailByStockInId(stockInId));
        }
        return stockIn;
    }

    @Override
    @Transactional
    public int insertStockIn(BizStockIn stockIn)
    {
        // 计算总数量和总金额
        calculateTotal(stockIn);
        int rows = stockInMapper.insertBizStockIn(stockIn);
        if (rows > 0 && StringUtils.isNotEmpty(stockIn.getDetailList()))
        {
            for (BizStockInDetail detail : stockIn.getDetailList())
            {
                detail.setStockInId(stockIn.getStockInId());
                detail.setCreateBy(stockIn.getCreateBy());
            }
            stockInDetailMapper.batchInsertBizStockInDetail(stockIn.getDetailList());
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateStockIn(BizStockIn stockIn)
    {
        BizStockIn existing = stockInMapper.selectBizStockInById(stockIn.getStockInId());
        if (StringUtils.isNull(existing))
        {
            throw new ServiceException("入库单不存在");
        }
        if ("1".equals(existing.getStatus()))
        {
            throw new ServiceException("已审核的入库单不允许修改");
        }

        // 删除原有明细，重新插入
        stockInDetailMapper.deleteBizStockInDetailByStockInId(stockIn.getStockInId());
        calculateTotal(stockIn);
        int rows = stockInMapper.updateBizStockIn(stockIn);
        if (rows > 0 && StringUtils.isNotEmpty(stockIn.getDetailList()))
        {
            for (BizStockInDetail detail : stockIn.getDetailList())
            {
                detail.setStockInId(stockIn.getStockInId());
                detail.setCreateBy(stockIn.getUpdateBy());
            }
            stockInDetailMapper.batchInsertBizStockInDetail(stockIn.getDetailList());
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteStockInById(Long stockInId)
    {
        BizStockIn existing = stockInMapper.selectBizStockInById(stockInId);
        if (StringUtils.isNull(existing))
        {
            throw new ServiceException("入库单不存在");
        }
        if ("1".equals(existing.getStatus()))
        {
            throw new ServiceException("已审核的入库单不允许删除");
        }
        stockInDetailMapper.deleteBizStockInDetailByStockInId(stockInId);
        return stockInMapper.deleteBizStockInById(stockInId);
    }

    @Override
    @Transactional
    public int deleteStockInByIds(Long[] stockInIds)
    {
        for (Long stockInId : stockInIds)
        {
            BizStockIn existing = stockInMapper.selectBizStockInById(stockInId);
            if (StringUtils.isNotNull(existing) && "1".equals(existing.getStatus()))
            {
                BizStockIn temp = stockInMapper.selectBizStockInById(stockInId);
                throw new ServiceException("'" + temp.getStockInNo() + "'已审核，不允许删除");
            }
            stockInDetailMapper.deleteBizStockInDetailByStockInId(stockInId);
        }
        return stockInMapper.deleteBizStockInByIds(stockInIds);
    }

    @Override
    public int auditStockIn(BizStockIn stockIn)
    {
        BizStockIn existing = stockInMapper.selectBizStockInById(stockIn.getStockInId());
        if (StringUtils.isNull(existing))
        {
            throw new ServiceException("入库单不存在");
        }
        if (!"0".equals(existing.getStatus()))
        {
            throw new ServiceException("只有待审核状态的入库单才能审核");
        }
        return stockInMapper.updateBizStockIn(stockIn);
    }

    @Override
    public List<BizStockInDetail> selectDetailByStockInId(Long stockInId)
    {
        return stockInDetailMapper.selectBizStockInDetailByStockInId(stockInId);
    }

    /**
     * 计算入库单总数量和总金额
     */
    private void calculateTotal(BizStockIn stockIn)
    {
        if (StringUtils.isEmpty(stockIn.getDetailList()))
        {
            return;
        }
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (BizStockInDetail detail : stockIn.getDetailList())
        {
            totalQuantity += (detail.getQuantity() != null ? detail.getQuantity() : 0);
            if (detail.getUnitPrice() != null && detail.getQuantity() != null)
            {
                BigDecimal amount = detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity()));
                detail.setAmount(amount);
                totalAmount = totalAmount.add(amount);
            }
        }
        stockIn.setTotalQuantity(totalQuantity);
        stockIn.setTotalAmount(totalAmount);
    }
}
