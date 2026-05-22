package com.ruoyi.system.service.impl;

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
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BizWarehouse;
import com.ruoyi.system.mapper.BizWarehouseMapper;
import com.ruoyi.system.service.IBizWarehouseService;

/**
 * 仓库信息 服务层处理
 */
@Service
public class BizWarehouseServiceImpl implements IBizWarehouseService
{
    private static final Logger log = LoggerFactory.getLogger(BizWarehouseServiceImpl.class);

    @Autowired
    private BizWarehouseMapper warehouseMapper;

    @Value("${third-party.datasource.url:}")
    private String thirdPartyUrl;

    @Value("${third-party.datasource.username:}")
    private String thirdPartyUsername;

    @Value("${third-party.datasource.password:}")
    private String thirdPartyPassword;

    @Value("${third-party.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String thirdPartyDriver;

    @Value("${third-party.datasource.warehouse-sql:SELECT id, code, name, address, contact_person, contact_phone, status FROM warehouse}")
    private String warehouseSql;

    @Override
    public List<BizWarehouse> selectWarehouseList(BizWarehouse warehouse)
    {
        return warehouseMapper.selectWarehouseList(warehouse);
    }

    @Override
    public List<BizWarehouse> selectWarehouseAll()
    {
        return warehouseMapper.selectWarehouseAll();
    }

    @Override
    public BizWarehouse selectWarehouseById(Long warehouseId)
    {
        return warehouseMapper.selectWarehouseById(warehouseId);
    }

    @Override
    public boolean checkWarehouseCodeUnique(BizWarehouse warehouse)
    {
        Long warehouseId = StringUtils.isNull(warehouse.getWarehouseId()) ? -1L : warehouse.getWarehouseId();
        BizWarehouse info = warehouseMapper.selectWarehouseByCode(warehouse.getWarehouseCode());
        if (StringUtils.isNotNull(info) && info.getWarehouseId().longValue() != warehouseId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertWarehouse(BizWarehouse warehouse)
    {
        warehouse.setSourceType("manual");
        return warehouseMapper.insertWarehouse(warehouse);
    }

    @Override
    public int updateWarehouse(BizWarehouse warehouse)
    {
        return warehouseMapper.updateWarehouse(warehouse);
    }

    @Override
    public int deleteWarehouseById(Long warehouseId)
    {
        return warehouseMapper.deleteWarehouseById(warehouseId);
    }

    @Override
    public int deleteWarehouseByIds(Long[] warehouseIds)
    {
        return warehouseMapper.deleteWarehouseByIds(warehouseIds);
    }

    @Override
    @Transactional
    public int syncWarehouseFromThirdParty()
    {
        if (StringUtils.isEmpty(thirdPartyUrl))
        {
            throw new ServiceException("第三方数据源未配置，请检查 application-third-party.yml 配置");
        }

        int syncCount = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            Class.forName(thirdPartyDriver);
            conn = DriverManager.getConnection(thirdPartyUrl, thirdPartyUsername, thirdPartyPassword);
            ps = conn.prepareStatement(warehouseSql);
            rs = ps.executeQuery();

            while (rs.next())
            {
                String sourceId = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String contactPerson = rs.getString("contact_person");
                String contactPhone = rs.getString("contact_phone");
                String status = rs.getString("status");

                // 根据第三方原始ID判断是否存在
                BizWarehouse existing = warehouseMapper.selectWarehouseBySourceId(sourceId);
                if (StringUtils.isNotNull(existing))
                {
                    // 更新已存在的记录
                    existing.setWarehouseCode(code);
                    existing.setWarehouseName(name);
                    existing.setAddress(address);
                    existing.setContactPerson(contactPerson);
                    existing.setContactPhone(contactPhone);
                    existing.setStatus(StringUtils.isEmpty(status) ? "0" : status);
                    warehouseMapper.updateWarehouse(existing);
                }
                else
                {
                    // 插入新记录
                    BizWarehouse warehouse = new BizWarehouse();
                    warehouse.setWarehouseCode(code);
                    warehouse.setWarehouseName(name);
                    warehouse.setAddress(address);
                    warehouse.setContactPerson(contactPerson);
                    warehouse.setContactPhone(contactPhone);
                    warehouse.setStatus(StringUtils.isEmpty(status) ? "0" : status);
                    warehouse.setSourceType("sync");
                    warehouse.setSourceId(sourceId);
                    warehouseMapper.insertWarehouse(warehouse);
                }
                syncCount++;
            }
            log.info("从第三方数据库同步仓库数据完成，共同步{}条记录", syncCount);
        }
        catch (Exception e)
        {
            log.error("同步第三方仓库数据失败", e);
            throw new ServiceException("同步第三方仓库数据失败: " + e.getMessage());
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
                log.error("关闭数据库连接失败", e);
            }
        }
        return syncCount;
    }
}
