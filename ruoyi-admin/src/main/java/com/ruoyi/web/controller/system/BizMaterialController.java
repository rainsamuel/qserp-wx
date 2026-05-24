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
import com.ruoyi.system.domain.BizMaterial;
import com.ruoyi.system.service.IBizMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 物资信息操作处理
 */
@Api(tags = "物资管理模块")
@RestController
@RequestMapping("/material/info")
public class BizMaterialController extends BaseController
{
    @Autowired
    private IBizMaterialService materialService;

    /**
     * 查询物资列表
     */
    @ApiOperation("查询物资列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "materialCode", value = "物资编码", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "materialName", value = "物资名称", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "categoryId", value = "物资分类ID", dataType = "Long", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "warehouseId", value = "所在仓库ID", dataType = "Long", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "status", value = "状态（0正常 1停用）", dataType = "String", dataTypeClass = String.class)
    })
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(BizMaterial material)
    {
        startPage();
        List<BizMaterial> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

    /**
     * 导出物资列表
     */
    @ApiOperation("导出物资列表")
    @PreAuthorize("@ss.hasPermi('material:info:export')")
    @Log(title = "物资管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizMaterial material)
    {
        List<BizMaterial> list = materialService.selectMaterialList(material);
        ExcelUtil<BizMaterial> util = new ExcelUtil<BizMaterial>(BizMaterial.class);
        util.exportExcel(response, list, "物资数据");
    }

    /**
     * 根据物资编号获取详细信息
     */
    @ApiOperation("获取物资详细信息")
    @ApiImplicitParam(name = "materialId", value = "物资ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @Anonymous
    @GetMapping(value = "/{materialId}")
    public AjaxResult getInfo(@PathVariable Long materialId)
    {
        return success(materialService.selectMaterialById(materialId));
    }

    /**
     * 新增物资
     */
    @ApiOperation("新增物资")
    @PreAuthorize("@ss.hasPermi('material:info:add')")
    @Log(title = "物资管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizMaterial material)
    {
        if (!materialService.checkMaterialCodeUnique(material))
        {
            return error("新增物资'" + material.getMaterialName() + "'失败，物资编码已存在");
        }
        material.setCreateBy(getUsername());
        return toAjax(materialService.insertMaterial(material));
    }

    /**
     * 修改物资
     */
    @ApiOperation("修改物资")
    @PreAuthorize("@ss.hasPermi('material:info:edit')")
    @Log(title = "物资管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizMaterial material)
    {
        if (!materialService.checkMaterialCodeUnique(material))
        {
            return error("修改物资'" + material.getMaterialName() + "'失败，物资编码已存在");
        }
        material.setUpdateBy(getUsername());
        return toAjax(materialService.updateMaterial(material));
    }

    /**
     * 删除物资
     */
    @ApiOperation("删除物资")
    @ApiImplicitParam(name = "materialIds", value = "物资ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('material:info:remove')")
    @Log(title = "物资管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{materialIds}")
    public AjaxResult remove(@PathVariable Long[] materialIds)
    {
        return toAjax(materialService.deleteMaterialByIds(materialIds));
    }
}
