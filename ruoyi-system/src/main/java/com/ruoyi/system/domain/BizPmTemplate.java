package com.ruoyi.system.domain;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * PM模板主表 biz_pm_template
 */
public class BizPmTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    @Excel(name = "模板序号", cellType = Excel.ColumnType.NUMERIC)
    private Long templateId;

    /** 模板类型 */
    @Excel(name = "模板类型")
    private String templateType;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String templateName;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 模板内容列表 */
    private List<BizPmTemplateContent> contentList;

    /** 内容数量（查询返回） */
    private Integer contentCount;

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }

    @NotBlank(message = "模板类型不能为空")
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }

    @NotBlank(message = "模板名称不能为空")
    @Size(min = 0, max = 100, message = "模板名称长度不能超过100个字符")
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<BizPmTemplateContent> getContentList() { return contentList; }
    public void setContentList(List<BizPmTemplateContent> contentList) { this.contentList = contentList; }

    public Integer getContentCount() { return contentCount; }
    public void setContentCount(Integer contentCount) { this.contentCount = contentCount; }
}
