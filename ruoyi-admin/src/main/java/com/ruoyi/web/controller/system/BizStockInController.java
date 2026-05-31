package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizStockIn;
import com.ruoyi.system.domain.BizStockInDetail;
import com.ruoyi.system.service.IBizStockInService;

/**
 * 入库单操作处理
 */
@RestController
@RequestMapping("/warehouse/stockIn")
public class BizStockInController extends BaseController
{
    @Autowired
    private IBizStockInService stockInService;

    /**
     * 查询入库单列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizStockIn stockIn)
    {
        startPage();
        List<BizStockIn> list = stockInService.selectStockInList(stockIn);
        return getDataTable(list);
    }

    /**
     * 导出入库单列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:export')")
    @Log(title = "入库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizStockIn stockIn)
    {
        List<BizStockIn> list = stockInService.selectStockInList(stockIn);
        ExcelUtil<BizStockIn> util = new ExcelUtil<BizStockIn>(BizStockIn.class);
        util.exportExcel(response, list, "入库数据");
    }

    /**
     * 根据入库单ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:query')")
    @GetMapping(value = "/{stockInId}")
    public AjaxResult getInfo(@PathVariable Long stockInId)
    {
        return success(stockInService.selectStockInById(stockInId));
    }

    /**
     * 查询入库明细列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:query')")
    @GetMapping("/detail/{stockInId}")
    public AjaxResult getDetailList(@PathVariable Long stockInId)
    {
        List<BizStockInDetail> list = stockInService.selectDetailByStockInId(stockInId);
        return success(list);
    }

    /**
     * 新增入库单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:add')")
    @Log(title = "入库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizStockIn stockIn)
    {
        stockIn.setCreateBy(getUsername());
        stockIn.setStatus("0");
        // 生成入库单号
        if (stockIn.getStockInNo() == null || stockIn.getStockInNo().isEmpty())
        {
            stockIn.setStockInNo("RK" + System.currentTimeMillis());
        }
        return toAjax(stockInService.insertStockIn(stockIn));
    }

    /**
     * 修改入库单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:edit')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizStockIn stockIn)
    {
        stockIn.setUpdateBy(getUsername());
        return toAjax(stockInService.updateStockIn(stockIn));
    }

    /**
     * 删除入库单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:remove')")
    @Log(title = "入库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{stockInIds}")
    public AjaxResult remove(@PathVariable Long[] stockInIds)
    {
        return toAjax(stockInService.deleteStockInByIds(stockInIds));
    }

    /**
     * 审核入库单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockIn:audit')")
    @Log(title = "入库管理", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@Validated @RequestBody BizStockIn stockIn)
    {
        stockIn.setAuditBy(getUsername());
        return toAjax(stockInService.auditStockIn(stockIn));
    }
}
