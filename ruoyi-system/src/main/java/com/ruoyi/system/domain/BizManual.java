package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 使用说明书表 biz_manual
 */
@ApiModel("使用说明书信息")
public class BizManual extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 说明书ID */
    @ApiModelProperty("说明书ID")
    @Excel(name = "说明书序号", cellType = Excel.ColumnType.NUMERIC)
    private Long manualId;

    /** 说明书名称 */
    @ApiModelProperty("说明书名称")
    @Excel(name = "说明书名称")
    private String manualName;

    /** 关联类型（warehouse仓库 material物资） */
    @ApiModelProperty("关联类型（warehouse仓库 material物资）")
    @Excel(name = "关联类型", readConverterExp = "warehouse=仓库,material=物资")
    private String manualType;

    /** 关联ID（仓库ID或物资ID） */
    @ApiModelProperty("关联ID（仓库ID或物资ID）")
    @Excel(name = "关联ID")
    private Long refId;

    /** 存储文件名 */
    @ApiModelProperty("存储文件名")
    private String fileName;

    /** 原始文件名 */
    @ApiModelProperty("原始文件名")
    @Excel(name = "文件名")
    private String originalName;

    /** 文件路径 */
    @ApiModelProperty("文件路径")
    private String filePath;

    /** 文件大小（字节） */
    @ApiModelProperty("文件大小（字节）")
    private Long fileSize;

    /** 文件扩展名 */
    @ApiModelProperty("文件扩展名")
    private String fileExt;

    /** 版本号 */
    @ApiModelProperty("版本号")
    @Excel(name = "版本号")
    private String version;

    /** 状态（0正常 1停用） */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 关联名称（仓库名称或物资名称，查询返回） */
    @ApiModelProperty("关联名称（查询返回）")
    private String refName;

    public Long getManualId()
    {
        return manualId;
    }

    public void setManualId(Long manualId)
    {
        this.manualId = manualId;
    }

    @NotBlank(message = "说明书名称不能为空")
    @Size(min = 0, max = 200, message = "说明书名称长度不能超过200个字符")
    public String getManualName()
    {
        return manualName;
    }

    public void setManualName(String manualName)
    {
        this.manualName = manualName;
    }

    @NotBlank(message = "关联类型不能为空")
    public String getManualType()
    {
        return manualType;
    }

    public void setManualType(String manualType)
    {
        this.manualType = manualType;
    }

    @NotNull(message = "关联ID不能为空")
    public Long getRefId()
    {
        return refId;
    }

    public void setRefId(Long refId)
    {
        this.refId = refId;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public void setOriginalName(String originalName)
    {
        this.originalName = originalName;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public String getFileExt()
    {
        return fileExt;
    }

    public void setFileExt(String fileExt)
    {
        this.fileExt = fileExt;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRefName()
    {
        return refName;
    }

    public void setRefName(String refName)
    {
        this.refName = refName;
    }
}
