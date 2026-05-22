package com.ruoyi.web.controller.inspection;

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
import com.ruoyi.system.domain.BizInspection;
import com.ruoyi.system.service.IBizInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 物资巡检操作处理（独立模块）
 */
@Api(tags = "物资巡检模块")
@RestController
@RequestMapping("/inspection/info")
public class BizInspectionController extends BaseController
{
    @Autowired
    private IBizInspectionService inspectionService;

    /**
     * 查询巡检记录列表
     */
    @ApiOperation("查询巡检记录列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "materialId", value = "物资ID", dataType = "Long", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "inspector", value = "巡检人", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "result", value = "巡检结果（normal正常 abnormal异常）", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('inspection:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizInspection inspection)
    {
        startPage();
        List<BizInspection> list = inspectionService.selectInspectionList(inspection);
        return getDataTable(list);
    }

    /**
     * 导出巡检记录列表
     */
    @ApiOperation("导出巡检记录列表")
    @PreAuthorize("@ss.hasPermi('inspection:info:export')")
    @Log(title = "物资巡检", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizInspection inspection)
    {
        List<BizInspection> list = inspectionService.selectInspectionList(inspection);
        ExcelUtil<BizInspection> util = new ExcelUtil<BizInspection>(BizInspection.class);
        util.exportExcel(response, list, "巡检记录数据");
    }

    /**
     * 根据ID获取巡检记录详细信息（含明细）
     */
    @ApiOperation("获取巡检记录详细信息")
    @ApiImplicitParam(name = "inspectionId", value = "巡检ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('inspection:info:query')")
    @GetMapping(value = "/{inspectionId}")
    public AjaxResult getInfo(@PathVariable Long inspectionId)
    {
        return success(inspectionService.selectInspectionById(inspectionId));
    }

    /**
     * 新增巡检记录
     */
    @ApiOperation("新增巡检记录")
    @PreAuthorize("@ss.hasPermi('inspection:info:add')")
    @Log(title = "物资巡检", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizInspection inspection)
    {
        inspection.setCreateBy(getUsername());
        return toAjax(inspectionService.insertInspection(inspection));
    }

    /**
     * 修改巡检记录
     */
    @ApiOperation("修改巡检记录")
    @PreAuthorize("@ss.hasPermi('inspection:info:edit')")
    @Log(title = "物资巡检", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizInspection inspection)
    {
        inspection.setUpdateBy(getUsername());
        return toAjax(inspectionService.updateInspection(inspection));
    }

    /**
     * 删除巡检记录
     */
    @ApiOperation("删除巡检记录")
    @ApiImplicitParam(name = "inspectionIds", value = "巡检ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('inspection:info:remove')")
    @Log(title = "物资巡检", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectionIds}")
    public AjaxResult remove(@PathVariable Long[] inspectionIds)
    {
        return toAjax(inspectionService.deleteInspectionByIds(inspectionIds));
    }
}
