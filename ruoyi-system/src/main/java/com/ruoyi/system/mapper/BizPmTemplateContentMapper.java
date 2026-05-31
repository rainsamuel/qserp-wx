package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizPmTemplateContent;

/**
 * PM模板内容Mapper接口
 */
public interface BizPmTemplateContentMapper
{
    public BizPmTemplateContent selectBizPmTemplateContentById(Long contentId);
    public List<BizPmTemplateContent> selectBizPmTemplateContentList(BizPmTemplateContent detail);
    public List<BizPmTemplateContent> selectContentByTemplateId(Long templateId);
    public int insertBizPmTemplateContent(BizPmTemplateContent detail);
    public int insertBatch(List<BizPmTemplateContent> list);
    public int updateBizPmTemplateContent(BizPmTemplateContent detail);
    public int deleteBizPmTemplateContentById(Long contentId);
    public int deleteContentByTemplateId(Long templateId);
    public int deleteBizPmTemplateContentByIds(Long[] contentIds);
}
