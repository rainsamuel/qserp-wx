package com.ruoyi.web.controller.repair;

import java.util.Date;
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
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizRepair;
import com.ruoyi.system.service.IBizRepairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 报修记录操作处理
 */
@Api(tags = "报修管理模块")
@RestController
@RequestMapping("/repair/info")
public class BizRepairController extends BaseController
{
    @Autowired
    private IBizRepairService repairService;

    /**
     * 查询报修记录列表
     */
    @ApiOperation("查询报修记录列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "assetCode", value = "资产编码", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "assetName", value = "资产名称", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "reporter", value = "报修人", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "priority", value = "优先级", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "handler", value = "处理人", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('repair:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizRepair repair)
    {
        startPage();
        List<BizRepair> list = repairService.selectRepairList(repair);
        return getDataTable(list);
    }

    /**
     * 根据报修人查询报修记录（小程序专用，匿名访问）
     */
    @ApiOperation("根据报修人查询报修记录")
    @ApiImplicitParam(name = "reporter", value = "报修人", required = true, dataType = "String", dataTypeClass = String.class)
    @Anonymous
    @GetMapping("/reporter/{reporter}")
    public AjaxResult listByReporter(@PathVariable String reporter)
    {
        BizRepair query = new BizRepair();
        query.setReporter(reporter);
        List<BizRepair> list = repairService.selectRepairList(query);
        return success(list);
    }

    /**
     * 导出报修记录列表
     */
    @ApiOperation("导出报修记录列表")
    @PreAuthorize("@ss.hasPermi('repair:info:export')")
    @Log(title = "报修管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizRepair repair)
    {
        List<BizRepair> list = repairService.selectRepairList(repair);
        ExcelUtil<BizRepair> util = new ExcelUtil<BizRepair>(BizRepair.class);
        util.exportExcel(response, list, "报修记录数据");
    }

    /**
     * 根据ID获取报修记录详细信息
     */
    @ApiOperation("获取报修记录详细信息")
    @ApiImplicitParam(name = "repairId", value = "报修ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('repair:info:query')")
    @GetMapping(value = "/{repairId}")
    public AjaxResult getInfo(@PathVariable Long repairId)
    {
        return success(repairService.selectRepairById(repairId));
    }

    /**
     * 新增报修记录（小程序专用，匿名访问）
     */
    @ApiOperation("新增报修记录")
    @Anonymous
    @PostMapping("/report")
    public AjaxResult report(@Validated @RequestBody BizRepair repair)
    {
        repair.setStatus("pending");
        repair.setCreateBy(repair.getReporter());
        return toAjax(repairService.insertRepair(repair));
    }

    /**
     * 新增报修记录
     */
    @ApiOperation("新增报修记录")
    @PreAuthorize("@ss.hasPermi('repair:info:add')")
    @Log(title = "报修管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizRepair repair)
    {
        repair.setCreateBy(getUsername());
        if (repair.getStatus() == null) {
            repair.setStatus("pending");
        }
        return toAjax(repairService.insertRepair(repair));
    }

    /**
     * 修改报修记录
     */
    @ApiOperation("修改报修记录")
    @PreAuthorize("@ss.hasPermi('repair:info:edit')")
    @Log(title = "报修管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizRepair repair)
    {
        repair.setUpdateBy(getUsername());
        return toAjax(repairService.updateRepair(repair));
    }

    /**
     * 变更报修状态（工程师使用）
     */
    @ApiOperation("变更报修状态")
    @PreAuthorize("@ss.hasPermi('repair:info:edit')")
    @Log(title = "报修状态变更", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult changeStatus(@RequestBody BizRepair repair)
    {
        BizRepair existing = repairService.selectRepairById(repair.getRepairId());
        if (existing == null) {
            return error("报修记录不存在");
        }
        existing.setStatus(repair.getStatus());
        existing.setHandler(repair.getHandler() != null ? repair.getHandler() : getUsername());
        existing.setHandleTime(new Date());
        existing.setHandleResult(repair.getHandleResult());
        existing.setUpdateBy(getUsername());
        return toAjax(repairService.updateRepair(existing));
    }

    /**
     * 查询待处理报修记录（小程序工程师使用，匿名访问）
     */
    @ApiOperation("查询待处理报修记录")
    @Anonymous
    @GetMapping("/pending")
    public AjaxResult listPending()
    {
        BizRepair query = new BizRepair();
        query.setStatus("pending");
        List<BizRepair> list = repairService.selectRepairList(query);
        return success(list);
    }

    /**
     * 查询处理中的报修记录（小程序工程师使用，匿名访问）
     */
    @ApiOperation("查询处理中的报修记录")
    @Anonymous
    @GetMapping("/processing")
    public AjaxResult listProcessing()
    {
        BizRepair query = new BizRepair();
        query.setStatus("processing");
        List<BizRepair> list = repairService.selectRepairList(query);
        return success(list);
    }

    /**
     * 工程师接单处理报修（小程序使用，匿名访问）
     */
    @ApiOperation("工程师接单处理报修")
    @Anonymous
    @PutMapping("/handle")
    public AjaxResult handleRepair(@RequestBody BizRepair repair)
    {
        BizRepair existing = repairService.selectRepairById(repair.getRepairId());
        if (existing == null) {
            return error("报修记录不存在");
        }
        existing.setStatus(repair.getStatus());
        existing.setHandler(repair.getHandler());
        existing.setHandleTime(new Date());
        existing.setHandleResult(repair.getHandleResult());
        return toAjax(repairService.updateRepair(existing));
    }

    /**
     * 删除报修记录
     */
    @ApiOperation("删除报修记录")
    @ApiImplicitParam(name = "repairIds", value = "报修ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('repair:info:remove')")
    @Log(title = "报修管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{repairIds}")
    public AjaxResult remove(@PathVariable Long[] repairIds)
    {
        return toAjax(repairService.deleteRepairByIds(repairIds));
    }
}
