package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizWarehouse;

/**
 * 仓库信息 数据层
 */
public interface BizWarehouseMapper
{
    /**
     * 查询仓库列表
     */
    public List<BizWarehouse> selectWarehouseList(BizWarehouse warehouse);

    /**
     * 查询所有仓库
     */
    public List<BizWarehouse> selectWarehouseAll();

    /**
     * 根据ID查询仓库
     */
    public BizWarehouse selectWarehouseById(Long warehouseId);

    /**
     * 根据编码查询仓库
     */
    public BizWarehouse selectWarehouseByCode(String warehouseCode);

    /**
     * 根据第三方原始ID查询仓库
     */
    public BizWarehouse selectWarehouseBySourceId(String sourceId);

    /**
     * 新增仓库
     */
    public int insertWarehouse(BizWarehouse warehouse);

    /**
     * 修改仓库
     */
    public int updateWarehouse(BizWarehouse warehouse);

    /**
     * 删除仓库
     */
    public int deleteWarehouseById(Long warehouseId);

    /**
     * 批量删除仓库
     */
    public int deleteWarehouseByIds(Long[] warehouseIds);
}
