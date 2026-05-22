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
import com.ruoyi.system.domain.BizWarehouse;
import com.ruoyi.system.service.IBizWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 仓库信息操作处理
 */
@Api(tags = "仓库管理模块")
@RestController
@RequestMapping("/warehouse/info")
public class BizWarehouseController extends BaseController
{
    @Autowired
    private IBizWarehouseService warehouseService;

    /**
     * 查询仓库列表
     */
    @ApiOperation("查询仓库列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "warehouseCode", value = "仓库编码", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "warehouseName", value = "仓库名称", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "status", value = "状态（0正常 1停用）", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "sourceType", value = "数据来源（manual手动 sync同步）", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('warehouse:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizWarehouse warehouse)
    {
        startPage();
        List<BizWarehouse> list = warehouseService.selectWarehouseList(warehouse);
        return getDataTable(list);
    }

    /**
     * 导出仓库列表
     */
    @ApiOperation("导出仓库列表")
    @PreAuthorize("@ss.hasPermi('warehouse:info:export')")
    @Log(title = "仓库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizWarehouse warehouse)
    {
        List<BizWarehouse> list = warehouseService.selectWarehouseList(warehouse);
        ExcelUtil<BizWarehouse> util = new ExcelUtil<BizWarehouse>(BizWarehouse.class);
        util.exportExcel(response, list, "仓库数据");
    }

    /**
     * 根据仓库编号获取详细信息
     */
    @ApiOperation("获取仓库详细信息")
    @ApiImplicitParam(name = "warehouseId", value = "仓库ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('warehouse:info:query')")
    @GetMapping(value = "/{warehouseId}")
    public AjaxResult getInfo(@PathVariable Long warehouseId)
    {
        return success(warehouseService.selectWarehouseById(warehouseId));
    }

    /**
     * 新增仓库
     */
    @ApiOperation("新增仓库")
    @PreAuthorize("@ss.hasPermi('warehouse:info:add')")
    @Log(title = "仓库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizWarehouse warehouse)
    {
        if (!warehouseService.checkWarehouseCodeUnique(warehouse))
        {
            return error("新增仓库'" + warehouse.getWarehouseName() + "'失败，仓库编码已存在");
        }
        warehouse.setCreateBy(getUsername());
        return toAjax(warehouseService.insertWarehouse(warehouse));
    }

    /**
     * 修改仓库
     */
    @ApiOperation("修改仓库")
    @PreAuthorize("@ss.hasPermi('warehouse:info:edit')")
    @Log(title = "仓库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizWarehouse warehouse)
    {
        if (!warehouseService.checkWarehouseCodeUnique(warehouse))
        {
            return error("修改仓库'" + warehouse.getWarehouseName() + "'失败，仓库编码已存在");
        }
        warehouse.setUpdateBy(getUsername());
        return toAjax(warehouseService.updateWarehouse(warehouse));
    }

    /**
     * 删除仓库
     */
    @ApiOperation("删除仓库")
    @ApiImplicitParam(name = "warehouseIds", value = "仓库ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('warehouse:info:remove')")
    @Log(title = "仓库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{warehouseIds}")
    public AjaxResult remove(@PathVariable Long[] warehouseIds)
    {
        return toAjax(warehouseService.deleteWarehouseByIds(warehouseIds));
    }

    /**
     * 从第三方数据库同步仓库数据
     */
    @ApiOperation("从第三方数据库同步仓库数据")
    @PreAuthorize("@ss.hasPermi('warehouse:info:sync')")
    @Log(title = "仓库同步", businessType = BusinessType.IMPORT)
    @PostMapping("/sync")
    public AjaxResult sync()
    {
        try
        {
            int count = warehouseService.syncWarehouseFromThirdParty();
            return success("同步成功，共同步" + count + "条仓库数据");
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 获取仓库选择框列表
     */
    @ApiOperation("获取仓库选择框列表")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<BizWarehouse> warehouses = warehouseService.selectWarehouseAll();
        return success(warehouses);
    }
}
