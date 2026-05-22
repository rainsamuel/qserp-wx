package com.ruoyi.system.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.system.domain.BizManual;

/**
 * 使用说明书 服务层
 */
public interface IBizManualService
{
    /**
     * 查询说明书列表
     */
    public List<BizManual> selectManualList(BizManual manual);

    /**
     * 根据ID查询说明书
     */
    public BizManual selectManualById(Long manualId);

    /**
     * 新增说明书（含文件上传）
     */
    public int insertManual(BizManual manual, MultipartFile file) throws Exception;

    /**
     * 修改说明书（含文件上传）
     */
    public int updateManual(BizManual manual, MultipartFile file) throws Exception;

    /**
     * 删除说明书（含文件删除）
     */
    public int deleteManualById(Long manualId);

    /**
     * 批量删除说明书
     */
    public int deleteManualByIds(Long[] manualIds);

    /**
     * 下载说明书文件
     */
    public BizManual downloadManual(Long manualId);
}
