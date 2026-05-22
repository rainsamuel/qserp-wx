package com.ruoyi.web.controller.system;

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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BizMaterialCategory;
import com.ruoyi.system.service.IBizMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 物资分类操作处理
 */
@Api(tags = "物资分类模块")
@RestController
@RequestMapping("/material/category")
public class BizMaterialCategoryController extends BaseController
{
    @Autowired
    private IBizMaterialService materialService;

    /**
     * 查询分类列表
     */
    @ApiOperation("查询物资分类列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryName", value = "分类名称", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "status", value = "状态（0正常 1停用）", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('material:category:list')")
    @GetMapping("/list")
    public AjaxResult list(BizMaterialCategory category)
    {
        List<BizMaterialCategory> list = materialService.selectCategoryList(category);
        return success(list);
    }

    /**
     * 查询所有分类
     */
    @ApiOperation("查询所有物资分类（下拉选择用）")
    @GetMapping("/all")
    public AjaxResult all()
    {
        List<BizMaterialCategory> list = materialService.selectCategoryAll();
        return success(list);
    }

    /**
     * 根据分类编号获取详细信息
     */
    @ApiOperation("获取物资分类详细信息")
    @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('material:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId)
    {
        return success(materialService.selectCategoryById(categoryId));
    }

    /**
     * 新增分类
     */
    @ApiOperation("新增物资分类")
    @PreAuthorize("@ss.hasPermi('material:category:add')")
    @Log(title = "物资分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizMaterialCategory category)
    {
        category.setCreateBy(getUsername());
        return toAjax(materialService.insertCategory(category));
    }

    /**
     * 修改分类
     */
    @ApiOperation("修改物资分类")
    @PreAuthorize("@ss.hasPermi('material:category:edit')")
    @Log(title = "物资分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizMaterialCategory category)
    {
        category.setUpdateBy(getUsername());
        return toAjax(materialService.updateCategory(category));
    }

    /**
     * 删除分类
     */
    @ApiOperation("删除物资分类")
    @ApiImplicitParam(name = "categoryIds", value = "分类ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('material:category:remove')")
    @Log(title = "物资分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(materialService.deleteCategoryByIds(categoryIds));
    }
}
