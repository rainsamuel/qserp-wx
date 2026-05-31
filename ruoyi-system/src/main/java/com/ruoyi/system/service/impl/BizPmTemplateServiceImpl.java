package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizPmTemplate;
import com.ruoyi.system.domain.BizPmTemplateContent;
import com.ruoyi.system.mapper.BizPmTemplateMapper;
import com.ruoyi.system.mapper.BizPmTemplateContentMapper;
import com.ruoyi.system.service.IBizPmTemplateService;

/**
 * PM模板Service业务层处理
 */
@Service
public class BizPmTemplateServiceImpl implements IBizPmTemplateService
{
    private static final Logger log = LoggerFactory.getLogger(BizPmTemplateServiceImpl.class);

    @Autowired
    private BizPmTemplateMapper templateMapper;

    @Autowired
    private BizPmTemplateContentMapper contentMapper;

    @Override
    public BizPmTemplate selectBizPmTemplateById(Long templateId)
    {
        return templateMapper.selectBizPmTemplateById(templateId);
    }

    @Override
    public List<BizPmTemplate> selectBizPmTemplateList(BizPmTemplate bizPmTemplate)
    {
        return templateMapper.selectBizPmTemplateList(bizPmTemplate);
    }

    @Override
    public List<BizPmTemplateContent> selectContentByTemplateId(Long templateId)
    {
        return contentMapper.selectContentByTemplateId(templateId);
    }

    @Override
    @Transactional
    public int insertBizPmTemplate(BizPmTemplate bizPmTemplate)
    {
        int rows = templateMapper.insertBizPmTemplate(bizPmTemplate);
        // 插入模板内容
        if (StringUtils.isNotNull(bizPmTemplate.getContentList()) && !bizPmTemplate.getContentList().isEmpty())
        {
            for (BizPmTemplateContent content : bizPmTemplate.getContentList())
            {
                content.setTemplateId(bizPmTemplate.getTemplateId());
                content.setCreateBy(bizPmTemplate.getCreateBy());
            }
            contentMapper.insertBatch(bizPmTemplate.getContentList());
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateBizPmTemplate(BizPmTemplate bizPmTemplate)
    {
        // 先删除原有内容，再重新插入
        contentMapper.deleteContentByTemplateId(bizPmTemplate.getTemplateId());
        if (StringUtils.isNotNull(bizPmTemplate.getContentList()) && !bizPmTemplate.getContentList().isEmpty())
        {
            for (BizPmTemplateContent content : bizPmTemplate.getContentList())
            {
                content.setTemplateId(bizPmTemplate.getTemplateId());
                content.setCreateBy(bizPmTemplate.getUpdateBy());
            }
            contentMapper.insertBatch(bizPmTemplate.getContentList());
        }
        return templateMapper.updateBizPmTemplate(bizPmTemplate);
    }

    @Override
    @Transactional
    public int deleteBizPmTemplateByIds(Long[] templateIds)
    {
        for (Long templateId : templateIds)
        {
            contentMapper.deleteContentByTemplateId(templateId);
        }
        return templateMapper.deleteBizPmTemplateByIds(templateIds);
    }

    @Override
    @Transactional
    public int importTemplate(Workbook workbook, String createBy)
    {
        int successCount = 0;
        // 按模板名称分组处理（Excel中同一模板名称的行归为同一个模板）
        Map<String, List<BizPmTemplateContent>> templateContentMap = new LinkedHashMap<>();
        Map<String, String> templateTypeMap = new LinkedHashMap<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet == null) continue;

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++)
            {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                String templateType = getCellStringValue(row.getCell(0));
                String templateName = getCellStringValue(row.getCell(1));
                String itemName = getCellStringValue(row.getCell(2));
                String contentName = getCellStringValue(row.getCell(3));
                String needValueStr = getCellStringValue(row.getCell(4));
                String valueType = getCellStringValue(row.getCell(5));
                String valueOptions = getCellStringValue(row.getCell(6));
                String defaultValue = getCellStringValue(row.getCell(7));
                String unit = getCellStringValue(row.getCell(8));

                if (StringUtils.isEmpty(templateName) || StringUtils.isEmpty(contentName))
                {
                    continue;
                }

                // 记录模板类型
                templateTypeMap.put(templateName, templateType);

                // 按模板名称分组
                List<BizPmTemplateContent> contentList = templateContentMap.computeIfAbsent(templateName, k -> new ArrayList<>());
                BizPmTemplateContent content = new BizPmTemplateContent();
                content.setItemName(itemName);
                content.setContentName(contentName);
                content.setNeedValue("是".equals(needValueStr) ? "1" : "0");
                content.setValueType("文本".equals(valueType) ? "text" : "dropdown");
                content.setValueOptions(valueOptions);
                content.setDefaultValue(defaultValue);
                content.setUnit(unit);
                content.setSortOrder(contentList.size() + 1);
                content.setStatus("0");
                content.setCreateBy(createBy);
                contentList.add(content);
            }
        }

        // 逐个模板保存
        for (Map.Entry<String, List<BizPmTemplateContent>> entry : templateContentMap.entrySet())
        {
            String templateName = entry.getKey();
            List<BizPmTemplateContent> contents = entry.getValue();
            String templateType = templateTypeMap.getOrDefault(templateName, "预防性维护");

            // 检查是否已存在同名模板
            BizPmTemplate query = new BizPmTemplate();
            query.setTemplateName(templateName);
            List<BizPmTemplate> existing = templateMapper.selectBizPmTemplateList(query);

            Long templateId;
            if (existing != null && !existing.isEmpty())
            {
                // 已存在，更新模板类型
                templateId = existing.get(0).getTemplateId();
                BizPmTemplate update = new BizPmTemplate();
                update.setTemplateId(templateId);
                update.setTemplateType(templateType);
                templateMapper.updateBizPmTemplate(update);
                // 删除旧内容
                contentMapper.deleteContentByTemplateId(templateId);
            }
            else
            {
                // 新增模板
                BizPmTemplate template = new BizPmTemplate();
                template.setTemplateType(templateType);
                template.setTemplateName(templateName);
                template.setStatus("0");
                template.setCreateBy(createBy);
                templateMapper.insertBizPmTemplate(template);
                templateId = template.getTemplateId();
            }

            // 插入内容
            for (BizPmTemplateContent content : contents)
            {
                content.setTemplateId(templateId);
            }
            contentMapper.insertBatch(contents);
            successCount++;
        }

        log.info("PM模板导入完成，共同步{}个模板", successCount);
        return successCount;
    }

    /**
     * 获取单元格字符串值
     */
    private String getCellStringValue(Cell cell)
    {
        if (cell == null)
        {
            return "";
        }
        if (cell.getCellType() == CellType.STRING)
        {
            return cell.getStringCellValue().trim();
        }
        else if (cell.getCellType() == CellType.NUMERIC)
        {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        else if (cell.getCellType() == CellType.BOOLEAN)
        {
            return String.valueOf(cell.getBooleanCellValue());
        }
        return "";
    }
}
