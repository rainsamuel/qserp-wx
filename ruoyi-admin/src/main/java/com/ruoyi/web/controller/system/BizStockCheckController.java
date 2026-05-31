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
import com.ruoyi.system.domain.BizStockCheck;
import com.ruoyi.system.domain.BizStockCheckDetail;
import com.ruoyi.system.service.IBizStockCheckService;

/**
 * 盘点单操作处理
 */
@RestController
@RequestMapping("/warehouse/stockCheck")
public class BizStockCheckController extends BaseController
{
    @Autowired
    private IBizStockCheckService stockCheckService;

    /**
     * 查询盘点单列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizStockCheck stockCheck)
    {
        startPage();
        List<BizStockCheck> list = stockCheckService.selectBizStockCheckList(stockCheck);
        return getDataTable(list);
    }

    /**
     * 导出盘点单列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:export')")
    @Log(title = "库存盘点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizStockCheck stockCheck)
    {
        List<BizStockCheck> list = stockCheckService.selectBizStockCheckList(stockCheck);
        ExcelUtil<BizStockCheck> util = new ExcelUtil<BizStockCheck>(BizStockCheck.class);
        util.exportExcel(response, list, "盘点数据");
    }

    /**
     * 根据盘点单ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable Long checkId)
    {
        return success(stockCheckService.selectBizStockCheckById(checkId));
    }

    /**
     * 查询盘点明细列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:query')")
    @GetMapping("/detail/{checkId}")
    public AjaxResult getDetailList(@PathVariable Long checkId)
    {
        List<BizStockCheckDetail> list = stockCheckService.selectDetailByCheckId(checkId);
        return success(list);
    }

    /**
     * 新增盘点单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:add')")
    @Log(title = "库存盘点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizStockCheck stockCheck)
    {
        stockCheck.setCreateBy(getUsername());
        return toAjax(stockCheckService.insertBizStockCheck(stockCheck));
    }

    /**
     * 修改盘点单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:edit')")
    @Log(title = "库存盘点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizStockCheck stockCheck)
    {
        stockCheck.setUpdateBy(getUsername());
        return toAjax(stockCheckService.updateBizStockCheck(stockCheck));
    }

    /**
     * 删除盘点单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:remove')")
    @Log(title = "库存盘点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checkIds}")
    public AjaxResult remove(@PathVariable Long[] checkIds)
    {
        return toAjax(stockCheckService.deleteBizStockCheckByIds(checkIds));
    }

    /**
     * 完成盘点（更新库存）
     */
    @PreAuthorize("@ss.hasPermi('warehouse:stockCheck:complete')")
    @Log(title = "库存盘点", businessType = BusinessType.UPDATE)
    @PutMapping("/complete")
    public AjaxResult complete(@Validated @RequestBody BizStockCheck stockCheck)
    {
        stockCheck.setUpdateBy(getUsername());
        try
        {
            return toAjax(stockCheckService.completeBizStockCheck(stockCheck));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }
}
