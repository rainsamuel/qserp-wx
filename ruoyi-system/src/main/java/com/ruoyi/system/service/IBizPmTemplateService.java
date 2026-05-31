package com.ruoyi.system.service;

import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import com.ruoyi.system.domain.BizPmTemplate;
import com.ruoyi.system.domain.BizPmTemplateContent;

/**
 * PM模板Service接口
 */
public interface IBizPmTemplateService
{
    public BizPmTemplate selectBizPmTemplateById(Long templateId);
    public List<BizPmTemplate> selectBizPmTemplateList(BizPmTemplate bizPmTemplate);
    public List<BizPmTemplateContent> selectContentByTemplateId(Long templateId);
    public int insertBizPmTemplate(BizPmTemplate bizPmTemplate);
    public int updateBizPmTemplate(BizPmTemplate bizPmTemplate);
    public int deleteBizPmTemplateByIds(Long[] templateIds);
    public int importTemplate(Workbook workbook, String createBy);
}
