package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizWarehouse;

/**
 * 仓库信息 服务层
 */
public interface IBizWarehouseService
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
     * 校验仓库编码是否唯一
     */
    public boolean checkWarehouseCodeUnique(BizWarehouse warehouse);

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

    /**
     * 从第三方数据库同步仓库数据
     */
    public int syncWarehouseFromThirdParty();
}
