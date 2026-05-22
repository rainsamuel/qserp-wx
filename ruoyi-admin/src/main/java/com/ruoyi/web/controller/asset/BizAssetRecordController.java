package com.ruoyi.web.controller.asset;

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
import com.ruoyi.system.domain.BizAssetRecord;
import com.ruoyi.system.service.IBizAssetRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 资产流转记录操作处理
 */
@Api(tags = "资产流转管理模块")
@RestController
@RequestMapping("/asset/record")
public class BizAssetRecordController extends BaseController
{
    @Autowired
    private IBizAssetRecordService assetRecordService;

    /**
     * 查询资产流转记录列表
     */
    @ApiOperation("查询资产流转记录列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "materialId", value = "物资ID", dataType = "Long", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "recordType", value = "流转类型", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "operator", value = "操作人", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "beginTime", value = "开始时间", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('asset:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizAssetRecord record)
    {
        startPage();
        List<BizAssetRecord> list = assetRecordService.selectAssetRecordList(record);
        return getDataTable(list);
    }

    /**
     * 导出资产流转记录列表
     */
    @ApiOperation("导出资产流转记录列表")
    @PreAuthorize("@ss.hasPermi('asset:record:export')")
    @Log(title = "资产流转管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizAssetRecord record)
    {
        List<BizAssetRecord> list = assetRecordService.selectAssetRecordList(record);
        ExcelUtil<BizAssetRecord> util = new ExcelUtil<BizAssetRecord>(BizAssetRecord.class);
        util.exportExcel(response, list, "资产流转数据");
    }

    /**
     * 根据记录编号获取详细信息
     */
    @ApiOperation("获取资产流转记录详细信息")
    @ApiImplicitParam(name = "recordId", value = "记录ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('asset:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId)
    {
        return success(assetRecordService.selectAssetRecordById(recordId));
    }

    /**
     * 根据物资ID获取流转记录
     */
    @ApiOperation("根据物资ID获取流转记录")
    @ApiImplicitParam(name = "materialId", value = "物资ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('asset:record:list')")
    @GetMapping(value = "/material/{materialId}")
    public AjaxResult getByMaterialId(@PathVariable Long materialId)
    {
        return success(assetRecordService.selectAssetRecordByMaterialId(materialId));
    }

    /**
     * 资产入库
     */
    @ApiOperation("资产入库")
    @PreAuthorize("@ss.hasPermi('asset:record:in')")
    @Log(title = "资产入库", businessType = BusinessType.INSERT)
    @PostMapping("/in")
    public AjaxResult stockIn(@Validated @RequestBody BizAssetRecord record)
    {
        record.setCreateBy(getUsername());
        record.setOperator(getUsername());
        try
        {
            return toAjax(assetRecordService.doStockIn(record));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 资产出库
     */
    @ApiOperation("资产出库")
    @PreAuthorize("@ss.hasPermi('asset:record:out')")
    @Log(title = "资产出库", businessType = BusinessType.INSERT)
    @PostMapping("/out")
    public AjaxResult stockOut(@Validated @RequestBody BizAssetRecord record)
    {
        record.setCreateBy(getUsername());
        record.setOperator(getUsername());
        try
        {
            return toAjax(assetRecordService.doStockOut(record));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 资产报损
     */
    @ApiOperation("资产报损")
    @PreAuthorize("@ss.hasPermi('asset:record:damage')")
    @Log(title = "资产报损", businessType = BusinessType.INSERT)
    @PostMapping("/damage")
    public AjaxResult damage(@Validated @RequestBody BizAssetRecord record)
    {
        record.setCreateBy(getUsername());
        record.setOperator(getUsername());
        try
        {
            return toAjax(assetRecordService.doDamage(record));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 资产报废
     */
    @ApiOperation("资产报废")
    @PreAuthorize("@ss.hasPermi('asset:record:scrap')")
    @Log(title = "资产报废", businessType = BusinessType.INSERT)
    @PostMapping("/scrap")
    public AjaxResult scrap(@Validated @RequestBody BizAssetRecord record)
    {
        record.setCreateBy(getUsername());
        record.setOperator(getUsername());
        try
        {
            return toAjax(assetRecordService.doScrap(record));
        }
        catch (RuntimeException e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 修改资产流转记录
     */
    @ApiOperation("修改资产流转记录")
    @PreAuthorize("@ss.hasPermi('asset:record:edit')")
    @Log(title = "资产流转管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizAssetRecord record)
    {
        record.setUpdateBy(getUsername());
        return toAjax(assetRecordService.updateAssetRecord(record));
    }

    /**
     * 删除资产流转记录
     */
    @ApiOperation("删除资产流转记录")
    @ApiImplicitParam(name = "recordIds", value = "记录ID串", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('asset:record:remove')")
    @Log(title = "资产流转管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(assetRecordService.deleteAssetRecordByIds(recordIds));
    }

    /**
     * 获取物资流转统计
     */
    @ApiOperation("获取物资流转统计")
    @ApiImplicitParam(name = "materialId", value = "物资ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('asset:record:list')")
    @GetMapping("/stats/{materialId}")
    public AjaxResult stats(@PathVariable Long materialId)
    {
        return success(assetRecordService.selectRecordTypeStats(materialId));
    }
}
