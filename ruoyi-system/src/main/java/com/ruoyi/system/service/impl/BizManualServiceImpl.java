package com.ruoyi.system.service.impl;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.BizManual;
import com.ruoyi.system.mapper.BizManualMapper;
import com.ruoyi.system.service.IBizManualService;

/**
 * 使用说明书 服务层处理
 */
@Service
public class BizManualServiceImpl implements IBizManualService
{
    /** 说明书文件上传目录 */
    private static final String MANUAL_UPLOAD_DIR = RuoYiConfig.getProfile() + "/manual";

    @Autowired
    private BizManualMapper manualMapper;

    @Override
    public List<BizManual> selectManualList(BizManual manual)
    {
        return manualMapper.selectManualList(manual);
    }

    @Override
    public BizManual selectManualById(Long manualId)
    {
        return manualMapper.selectManualById(manualId);
    }

    @Override
    public int insertManual(BizManual manual, MultipartFile file) throws Exception
    {
        if (file != null && !file.isEmpty())
        {
            uploadFile(manual, file);
        }
        return manualMapper.insertManual(manual);
    }

    @Override
    public int updateManual(BizManual manual, MultipartFile file) throws Exception
    {
        if (file != null && !file.isEmpty())
        {
            // 删除旧文件
            BizManual oldManual = manualMapper.selectManualById(manual.getManualId());
            if (oldManual != null && StringUtils.isNotEmpty(oldManual.getFilePath()))
            {
                deletePhysicalFile(oldManual.getFilePath());
            }
            uploadFile(manual, file);
        }
        return manualMapper.updateManual(manual);
    }

    @Override
    public int deleteManualById(Long manualId)
    {
        BizManual manual = manualMapper.selectManualById(manualId);
        if (manual != null && StringUtils.isNotEmpty(manual.getFilePath()))
        {
            deletePhysicalFile(manual.getFilePath());
        }
        return manualMapper.deleteManualById(manualId);
    }

    @Override
    public int deleteManualByIds(Long[] manualIds)
    {
        for (Long manualId : manualIds)
        {
            deleteManualById(manualId);
        }
        return manualMapper.deleteManualByIds(manualIds);
    }

    @Override
    public BizManual downloadManual(Long manualId)
    {
        BizManual manual = manualMapper.selectManualById(manualId);
        if (manual == null)
        {
            throw new ServiceException("说明书不存在");
        }
        if (StringUtils.isEmpty(manual.getFilePath()))
        {
            throw new ServiceException("该说明书未上传文件");
        }
        return manual;
    }

    /**
     * 上传文件并设置手动册属性
     */
    private void uploadFile(BizManual manual, MultipartFile file) throws Exception
    {
        String fileName = FileUploadUtils.upload(MANUAL_UPLOAD_DIR, file);
        manual.setFileName(FileUtils.getName(fileName));
        manual.setOriginalName(file.getOriginalFilename());
        manual.setFilePath(fileName);
        manual.setFileSize(file.getSize());
        manual.setFileExt(FilenameUtils.getExtension(file.getOriginalFilename()));
    }

    /**
     * 删除物理文件
     */
    private void deletePhysicalFile(String filePath)
    {
        try
        {
            String fullPath = RuoYiConfig.getProfile() + FileUtils.stripPrefix(filePath);
            FileUtils.deleteFile(fullPath);
        }
        catch (Exception e)
        {
            // 文件删除失败不影响业务
        }
    }
}
