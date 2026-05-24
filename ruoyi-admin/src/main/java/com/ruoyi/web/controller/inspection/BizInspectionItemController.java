package com.ruoyi.web.controller.inspection;

import java.util.List;
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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BizInspectionItem;
import com.ruoyi.system.service.IBizInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 巡检内容项操作处理（独立模块）
 */
@Api(tags = "巡检内容项模块")
@RestController
@RequestMapping("/inspection/item")
public class BizInspectionItemController extends BaseController
{
    @Autowired
    private IBizInspectionService inspectionService;

    /**
     * 查询检查项列表
     */
    @ApiOperation("查询巡检内容项列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "itemName", value = "检查项名称", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "itemGroup", value = "检查项分组", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "status", value = "状态", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('inspection:item:list')")
    @GetMapping("/list")
    public AjaxResult list(BizInspectionItem item)
    {
        List<BizInspectionItem> list = inspectionService.selectItemList(item);
        return success(list);
    }

    /**
     * 查询所有正常检查项（下拉选择用）
     */
    @ApiOperation("查询所有正常检查项")
    @Anonymous
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<BizInspectionItem> list = inspectionService.selectItemAll();
        return success(list);
    }

    /**
     * 根据ID获取检查项详细信息
     */
    @ApiOperation("获取检查项详细信息")
    @ApiImplicitParam(name = "itemId", value = "检查项ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('inspection:item:query')")
    @GetMapping(value = "/{itemId}")
    public AjaxResult getInfo(@PathVariable Long itemId)
    {
        return success(inspectionService.selectItemById(itemId));
    }

    /**
     * 新增检查项
     */
    @ApiOperation("新增巡检内容项")
    @PreAuthorize("@ss.hasPermi('inspection:item:add')")
    @Log(title = "巡检内容项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizInspectionItem item)
    {
        item.setCreateBy(getUsername());
        return toAjax(inspectionService.insertItem(item));
    }

    /**
     * 修改检查项
     */
    @ApiOperation("修改巡检内容项")
    @PreAuthorize("@ss.hasPermi('inspection:item:edit')")
    @Log(title = "巡检内容项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizInspectionItem item)
    {
        item.setUpdateBy(getUsername());
        return toAjax(inspectionService.updateItem(item));
    }

    /**
     * 删除检查项
     */
    @ApiOperation("删除巡检内容项")
    @ApiImplicitParam(name = "itemIds", value = "检查项ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('inspection:item:remove')")
    @Log(title = "巡检内容项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemIds}")
    public AjaxResult remove(@PathVariable Long[] itemIds)
    {
        return toAjax(inspectionService.deleteItemByIds(itemIds));
    }
}
