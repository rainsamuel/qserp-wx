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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizPmTemplate;
import com.ruoyi.system.domain.BizPmTemplateContent;
import com.ruoyi.system.service.IBizPmTemplateService;

/**
 * PM模板管理操作处理
 */
@RestController
@RequestMapping("/pm/template")
public class BizPmTemplateController extends BaseController
{
    @Autowired
    private IBizPmTemplateService templateService;

    /**
     * 查询PM模板列表
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPmTemplate bizPmTemplate)
    {
        startPage();
        List<BizPmTemplate> list = templateService.selectBizPmTemplateList(bizPmTemplate);
        return getDataTable(list);
    }

    /**
     * 导出PM模板列表
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:export')")
    @Log(title = "PM模板管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPmTemplate bizPmTemplate)
    {
        List<BizPmTemplate> list = templateService.selectBizPmTemplateList(bizPmTemplate);
        ExcelUtil<BizPmTemplate> util = new ExcelUtil<BizPmTemplate>(BizPmTemplate.class);
        util.exportExcel(response, list, "PM模板数据");
    }

    /**
     * 获取PM模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable Long templateId)
    {
        BizPmTemplate template = templateService.selectBizPmTemplateById(templateId);
        if (template != null)
        {
            template.setContentList(templateService.selectContentByTemplateId(templateId));
        }
        return success(template);
    }

    /**
     * 获取模板内容列表（匿名访问，小程序使用）
     */
    @Anonymous
    @GetMapping("/content/{templateId}")
    public AjaxResult getContentList(@PathVariable Long templateId)
    {
        List<BizPmTemplateContent> list = templateService.selectContentByTemplateId(templateId);
        return success(list);
    }

    /**
     * 新增PM模板
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:add')")
    @Log(title = "PM模板管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizPmTemplate bizPmTemplate)
    {
        bizPmTemplate.setCreateBy(getUsername());
        return toAjax(templateService.insertBizPmTemplate(bizPmTemplate));
    }

    /**
     * 修改PM模板
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:edit')")
    @Log(title = "PM模板管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizPmTemplate bizPmTemplate)
    {
        bizPmTemplate.setUpdateBy(getUsername());
        return toAjax(templateService.updateBizPmTemplate(bizPmTemplate));
    }

    /**
     * 删除PM模板
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:remove')")
    @Log(title = "PM模板管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds)
    {
        return toAjax(templateService.deleteBizPmTemplateByIds(templateIds));
    }

    /**
     * 导入PM模板
     */
    @PreAuthorize("@ss.hasPermi('asset:pmTemplate:import')")
    @Log(title = "PM模板导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile file)
    {
        try
        {
            org.apache.poi.ss.usermodel.Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(file.getInputStream());
            int count = templateService.importTemplate(workbook, getUsername());
            workbook.close();
            return success("导入成功，共同步" + count + "个PM模板");
        }
        catch (Exception e)
        {
            return error("导入失败：" + e.getMessage());
        }
    }
}
