package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizPmTemplate;

/**
 * PM模板Mapper接口
 */
public interface BizPmTemplateMapper
{
    public BizPmTemplate selectBizPmTemplateById(Long templateId);
    public List<BizPmTemplate> selectBizPmTemplateList(BizPmTemplate bizPmTemplate);
    public int insertBizPmTemplate(BizPmTemplate bizPmTemplate);
    public int updateBizPmTemplate(BizPmTemplate bizPmTemplate);
    public int deleteBizPmTemplateById(Long templateId);
    public int deleteBizPmTemplateByIds(Long[] templateIds);
}
