package com.ruoyi.web.controller.manual;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizManual;
import com.ruoyi.system.service.IBizManualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 使用说明书操作处理（独立模块）
 */
@Api(tags = "使用说明书模块")
@RestController
@RequestMapping("/manual/info")
public class BizManualController extends BaseController
{
    @Autowired
    private IBizManualService manualService;

    /**
     * 查询说明书列表
     */
    @ApiOperation("查询说明书列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "manualName", value = "说明书名称", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "manualType", value = "关联类型（warehouse仓库 material物资）", dataType = "String", dataTypeClass = String.class),
        @ApiImplicitParam(name = "refId", value = "关联ID", dataType = "Long", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "status", value = "状态（0正常 1停用）", dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('manual:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizManual manual)
    {
        startPage();
        List<BizManual> list = manualService.selectManualList(manual);
        return getDataTable(list);
    }

    /**
     * 导出说明书列表
     */
    @ApiOperation("导出说明书列表")
    @PreAuthorize("@ss.hasPermi('manual:info:export')")
    @Log(title = "使用说明书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizManual manual)
    {
        List<BizManual> list = manualService.selectManualList(manual);
        ExcelUtil<BizManual> util = new ExcelUtil<BizManual>(BizManual.class);
        util.exportExcel(response, list, "说明书数据");
    }

    /**
     * 根据ID获取说明书详细信息
     */
    @ApiOperation("获取说明书详细信息")
    @ApiImplicitParam(name = "manualId", value = "说明书ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('manual:info:query')")
    @GetMapping(value = "/{manualId}")
    public AjaxResult getInfo(@PathVariable Long manualId)
    {
        return success(manualService.selectManualById(manualId));
    }

    /**
     * 新增说明书（含文件上传）
     */
    @ApiOperation("新增说明书（含文件上传）")
    @PreAuthorize("@ss.hasPermi('manual:info:add')")
    @Log(title = "使用说明书", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated BizManual manual, @RequestParam(value = "file", required = false) MultipartFile file)
    {
        manual.setCreateBy(getUsername());
        try
        {
            return toAjax(manualService.insertManual(manual, file));
        }
        catch (Exception e)
        {
            return error("上传文件失败：" + e.getMessage());
        }
    }

    /**
     * 修改说明书（含文件上传）
     */
    @ApiOperation("修改说明书（含文件上传）")
    @PreAuthorize("@ss.hasPermi('manual:info:edit')")
    @Log(title = "使用说明书", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated BizManual manual, @RequestParam(value = "file", required = false) MultipartFile file)
    {
        manual.setUpdateBy(getUsername());
        try
        {
            return toAjax(manualService.updateManual(manual, file));
        }
        catch (Exception e)
        {
            return error("上传文件失败：" + e.getMessage());
        }
    }

    /**
     * 删除说明书
     */
    @ApiOperation("删除说明书")
    @ApiImplicitParam(name = "manualIds", value = "说明书ID串（逗号分隔）", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('manual:info:remove')")
    @Log(title = "使用说明书", businessType = BusinessType.DELETE)
    @DeleteMapping("/{manualIds}")
    public AjaxResult remove(@PathVariable Long[] manualIds)
    {
        return toAjax(manualService.deleteManualByIds(manualIds));
    }

    /**
     * 下载说明书文件
     */
    @ApiOperation("下载说明书文件")
    @ApiImplicitParam(name = "manualId", value = "说明书ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('manual:info:query')")
    @GetMapping("/download/{manualId}")
    public void download(@PathVariable Long manualId, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            BizManual manual = manualService.downloadManual(manualId);
            String filePath = RuoYiConfig.getProfile() + FileUtils.stripPrefix(manual.getFilePath());
            String downloadName = StringUtils.substringAfterLast(filePath, "/");
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader(response, manual.getOriginalName());
            FileUtils.writeBytes(filePath, response.getOutputStream());
        }
        catch (Exception e)
        {
            logger.error("下载说明书文件失败", e);
        }
    }

    /**
     * 预览说明书文件
     */
    @ApiOperation("预览说明书文件")
    @ApiImplicitParam(name = "manualId", value = "说明书ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('manual:info:query')")
    @GetMapping("/preview/{manualId}")
    public AjaxResult preview(@PathVariable Long manualId)
    {
        BizManual manual = manualService.selectManualById(manualId);
        if (manual == null)
        {
            return error("说明书不存在");
        }
        if (StringUtils.isEmpty(manual.getFilePath()))
        {
            return error("该说明书未上传文件");
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("filePath", manual.getFilePath());
        ajax.put("originalName", manual.getOriginalName());
        ajax.put("fileExt", manual.getFileExt());
        ajax.put("fileSize", manual.getFileSize());
        return ajax;
    }
}
