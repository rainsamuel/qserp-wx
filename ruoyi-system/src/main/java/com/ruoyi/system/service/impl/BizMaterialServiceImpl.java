package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizMaterial;
import com.ruoyi.system.domain.BizMaterialCategory;
import com.ruoyi.system.mapper.BizMaterialCategoryMapper;
import com.ruoyi.system.mapper.BizMaterialMapper;
import com.ruoyi.system.service.IBizMaterialService;

/**
 * 物资信息 服务层处理
 */
@Service
public class BizMaterialServiceImpl implements IBizMaterialService
{
    private static final Logger log = LoggerFactory.getLogger(BizMaterialServiceImpl.class);

    @Autowired
    private BizMaterialMapper materialMapper;

    @Autowired
    private BizMaterialCategoryMapper categoryMapper;

    @Value("${third-party.oracle.url:}")
    private String oracleUrl;

    @Value("${third-party.oracle.username:}")
    private String oracleUsername;

    @Value("${third-party.oracle.password:}")
    private String oraclePassword;

    @Value("${third-party.oracle.driver-class-name:oracle.jdbc.OracleDriver}")
    private String oracleDriver;

    @Value("${third-party.oracle.asset-sql:SELECT ID, KPBH, WZMC, GGXH, DJ, KSMC, ZT, BAR_CODE, PPMC, BZ, BGRXM, GRRQ, FLID FROM IN_ASSET WHERE ZFBZ = 0}")
    private String assetSql;

    // ========== 物资管理 ==========

    @Override
    public List<BizMaterial> selectMaterialList(BizMaterial material)
    {
        return materialMapper.selectMaterialList(material);
    }

    @Override
    public BizMaterial selectMaterialById(Long materialId)
    {
        return materialMapper.selectMaterialById(materialId);
    }

    @Override
    public BizMaterial selectMaterialByAssetCode(String assetCode)
    {
        return materialMapper.selectMaterialByAssetCode(assetCode);
    }

    @Override
    public BizMaterial selectMaterialByKpbh(String kpbh)
    {
        return materialMapper.selectMaterialByKpbh(kpbh);
    }

    @Override
    public BizMaterial selectMaterialByQRCode(String qrCode)
    {
        if (StringUtils.isEmpty(qrCode))
        {
            return null;
        }

        // 解析二维码内容
        String codeType = "unknown";
        String code = qrCode;
        String syncId = null;
        String orgCode = null;

        // 1. 判断是否为URL格式（第三方同步的二维码）
        if (qrCode.startsWith("http://") || qrCode.startsWith("https://"))
        {
            codeType = "url";
            // 解析URL参数：http://qr.ygyfnet.com/?o=91330000MA27U06H83&t=4&i=4220
            try
            {
                java.net.URL url = new java.net.URL(qrCode);
                String query = url.getQuery();
                if (query != null)
                {
                    String[] params = query.split("&");
                    for (String param : params)
                    {
                        String[] kv = param.split("=");
                        if (kv.length == 2)
                        {
                            if ("i".equals(kv[0]))
                            {
                                syncId = kv[1];
                            }
                            else if ("o".equals(kv[0]))
                            {
                                orgCode = kv[1];
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                // URL解析失败，尝试其他方式
            }

            // 如果解析到syncId，使用syncId查询
            if (StringUtils.isNotEmpty(syncId))
            {
                BizMaterial material = materialMapper.selectMaterialBySyncId(syncId, orgCode);
                if (material != null)
                {
                    return material;
                }
            }
        }

        // 2. 判断是否为JSON格式（本系统生成的二维码）
        if (qrCode.startsWith("{"))
        {
            try
            {
                JSONObject json = JSON.parseObject(qrCode);
                if (json.containsKey("syncId"))
                {
                    syncId = json.getString("syncId");
                    orgCode = json.getString("orgCode");
                    if (StringUtils.isNotEmpty(syncId))
                    {
                        return materialMapper.selectMaterialBySyncId(syncId, orgCode);
                    }
                }
                else if (json.containsKey("code"))
                {
                    code = json.getString("code");
                }
            }
            catch (Exception e)
            {
                // JSON解析失败，使用原始值
            }
        }

        // 3. 尝试按资产编码查询
        BizMaterial material = materialMapper.selectMaterialByAssetCode(code);
        if (material != null)
        {
            return material;
        }

        // 4. 尝试按卡片编号查询
        material = materialMapper.selectMaterialByKpbh(code);
        if (material != null)
        {
            return material;
        }

        // 5. 尝试按物资编码查询
        material = materialMapper.selectMaterialByCode(code);
        return material;
    }

    @Override
    public boolean checkMaterialCodeUnique(BizMaterial material)
    {
        Long materialId = StringUtils.isNull(material.getMaterialId()) ? -1L : material.getMaterialId();
        BizMaterial info = materialMapper.selectMaterialByCode(material.getMaterialCode());
        if (StringUtils.isNotNull(info) && info.getMaterialId().longValue() != materialId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertMaterial(BizMaterial material)
    {
        return materialMapper.insertMaterial(material);
    }

    @Override
    public int updateMaterial(BizMaterial material)
    {
        return materialMapper.updateMaterial(material);
    }

    @Override
    public int deleteMaterialById(Long materialId)
    {
        return materialMapper.deleteMaterialById(materialId);
    }

    @Override
    public int deleteMaterialByIds(Long[] materialIds)
    {
        return materialMapper.deleteMaterialByIds(materialIds);
    }

    @Override
    @Transactional
    public int syncMaterialFromOracle()
    {
        if (StringUtils.isEmpty(oracleUrl))
        {
            throw new ServiceException("Oracle数据源未配置，请检查 application-third-party.yml 中的 third-party.oracle 配置");
        }

        int syncCount = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            Class.forName(oracleDriver);
            conn = DriverManager.getConnection(oracleUrl, oracleUsername, oraclePassword);
            ps = conn.prepareStatement(assetSql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String id = rs.getString("ID");
                String kpbh = rs.getString("KPBH");
                String wzmc = rs.getString("WZMC");
                String ggxh = rs.getString("GGXH");
                BigDecimal dj = rs.getBigDecimal("DJ");
                String ksmc = rs.getString("KSMC");
                String zt = rs.getString("ZT");
                String barCode = rs.getString("BAR_CODE");
                String ppmc = rs.getString("PPMC");
                String bz = rs.getString("BZ");
                String bgrxm = rs.getString("BGRXM");
                java.sql.Date grrq = rs.getDate("GRRQ");
                String flid = rs.getString("FLID");

                // 构建备注信息
                StringBuilder remarkBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(bz)) remarkBuilder.append("备注:").append(bz).append("; ");
                if (StringUtils.isNotEmpty(bgrxm)) remarkBuilder.append("保管人:").append(bgrxm).append("; ");
                if (grrq != null) remarkBuilder.append("购置日期:").append(grrq.toString()).append("; ");
                if (StringUtils.isNotEmpty(flid)) remarkBuilder.append("分类ID:").append(flid);

                // 状态映射：Oracle的ZT字段，"0"为正常，其他为停用
                String status = "0".equals(zt) ? "0" : "1";

                // 按syncId判断是否存在
                BizMaterial existing = materialMapper.selectMaterialBySyncId(id, null);
                if (StringUtils.isNotNull(existing))
                {
                    // 更新已存在的记录
                    existing.setKpbh(kpbh);
                    existing.setMaterialName(wzmc);
                    existing.setSpec(ggxh);
                    existing.setUnitPrice(dj);
                    existing.setUseDepartment(ksmc);
                    existing.setStatus(status);
                    existing.setAssetCode(barCode);
                    existing.setSupplier(ppmc);
                    existing.setRemark(remarkBuilder.toString());
                    existing.setDataSource("sync");
                    materialMapper.updateMaterial(existing);
                }
                else
                {
                    // 插入新记录
                    BizMaterial material = new BizMaterial();
                    material.setSyncId(id);
                    material.setKpbh(kpbh);
                    material.setMaterialCode("ORACLE-" + id);
                    material.setMaterialName(wzmc);
                    material.setSpec(ggxh);
                    material.setUnitPrice(dj);
                    material.setUseDepartment(ksmc);
                    material.setStatus(status);
                    material.setAssetCode(barCode);
                    material.setSupplier(ppmc);
                    material.setRemark(remarkBuilder.toString());
                    material.setDataSource("sync");
                    material.setStockQuantity(0);
                    materialMapper.insertMaterial(material);
                }
                syncCount++;
            }
            log.info("从Oracle数据库同步资产数据完成，共同步{}条记录", syncCount);
        }
        catch (Exception e)
        {
            log.error("同步Oracle资产数据失败", e);
            throw new ServiceException("同步Oracle资产数据失败: " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            }
            catch (Exception e)
            {
                log.error("关闭Oracle数据库连接失败", e);
            }
        }
        return syncCount;
    }

    // ========== 物资分类管理 ==========

    @Override
    public List<BizMaterialCategory> selectCategoryList(BizMaterialCategory category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    @Override
    public List<BizMaterialCategory> selectCategoryAll()
    {
        return categoryMapper.selectCategoryAll();
    }

    @Override
    public BizMaterialCategory selectCategoryById(Long categoryId)
    {
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public int insertCategory(BizMaterialCategory category)
    {
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(BizMaterialCategory category)
    {
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryById(Long categoryId)
    {
        if (categoryMapper.countChildCategoryById(categoryId) > 0)
        {
            throw new ServiceException("存在下级分类,不允许删除");
        }
        return categoryMapper.deleteCategoryById(categoryId);
    }

    @Override
    public int deleteCategoryByIds(Long[] categoryIds)
    {
        for (Long categoryId : categoryIds)
        {
            if (categoryMapper.countChildCategoryById(categoryId) > 0)
            {
                BizMaterialCategory category = categoryMapper.selectCategoryById(categoryId);
                throw new ServiceException("'" + category.getCategoryName() + "'存在下级分类,不允许删除");
            }
        }
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }
}
