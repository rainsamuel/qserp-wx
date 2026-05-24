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
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizAssetChange;
import com.ruoyi.system.service.IBizAssetChangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 资产变更记录操作处理
 */
@Api(tags = "资产变更记录模块")
@RestController
@RequestMapping("/asset/change")
public class BizAssetChangeController extends BaseController
{
    @Autowired
    private IBizAssetChangeService assetChangeService;

    /**
     * 查询资产变更记录列表
     */
    @ApiOperation("查询资产变更记录列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "materialId", value = "物资ID", dataType = "Long", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "changeType", value = "变更类型", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "operator", value = "操作人", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('asset:change:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizAssetChange assetChange)
    {
        startPage();
        List<BizAssetChange> list = assetChangeService.selectAssetChangeList(assetChange);
        return getDataTable(list);
    }

    /**
     * 根据物资ID查询变更记录（小程序专用，匿名访问）
     */
    @ApiOperation("根据物资ID查询变更记录")
    @ApiImplicitParam(name = "materialId", value = "物资ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @Anonymous
    @GetMapping("/material/{materialId}")
    public AjaxResult listByMaterialId(@PathVariable Long materialId)
    {
        BizAssetChange query = new BizAssetChange();
        query.setMaterialId(materialId);
        List<BizAssetChange> list = assetChangeService.selectAssetChangeList(query);
        return success(list);
    }

    /**
     * 导出资产变更记录列表
     */
    @ApiOperation("导出资产变更记录列表")
    @PreAuthorize("@ss.hasPermi('asset:change:export')")
    @Log(title = "资产变更记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizAssetChange assetChange)
    {
        List<BizAssetChange> list = assetChangeService.selectAssetChangeList(assetChange);
        ExcelUtil<BizAssetChange> util = new ExcelUtil<BizAssetChange>(BizAssetChange.class);
        util.exportExcel(response, list, "资产变更记录数据");
    }

    /**
     * 根据ID获取资产变更记录详细信息
     */
    @ApiOperation("获取资产变更记录详细信息")
    @ApiImplicitParam(name = "changeId", value = "变更ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('asset:change:query')")
    @GetMapping(value = "/{changeId}")
    public AjaxResult getInfo(@PathVariable Long changeId)
    {
        return success(assetChangeService.selectAssetChangeById(changeId));
    }

    /**
     * 新增资产变更记录
     */
    @ApiOperation("新增资产变更记录")
    @PreAuthorize("@ss.hasPermi('asset:change:add')")
    @Log(title = "资产变更记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizAssetChange assetChange)
    {
        assetChange.setCreateBy(getUsername());
        return toAjax(assetChangeService.insertAssetChange(assetChange));
    }

    /**
     * 修改资产变更记录
     */
    @ApiOperation("修改资产变更记录")
    @PreAuthorize("@ss.hasPermi('asset:change:edit')")
    @Log(title = "资产变更记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizAssetChange assetChange)
    {
        assetChange.setUpdateBy(getUsername());
        return toAjax(assetChangeService.updateAssetChange(assetChange));
    }

    /**
     * 删除资产变更记录
     */
    @ApiOperation("删除资产变更记录")
    @ApiImplicitParam(name = "changeIds", value = "变更ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('asset:change:remove')")
    @Log(title = "资产变更记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{changeIds}")
    public AjaxResult remove(@PathVariable Long[] changeIds)
    {
        return toAjax(assetChangeService.deleteAssetChangeByIds(changeIds));
    }
}
