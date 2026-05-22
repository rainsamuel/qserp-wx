import request from '@/utils/request'

// 查询仓库列表
export function listWarehouse(query) {
  return request({
    url: '/warehouse/info/list',
    method: 'get',
    params: query
  })
}

// 查询仓库详细
export function getWarehouse(warehouseId) {
  return request({
    url: '/warehouse/info/' + warehouseId,
    method: 'get'
  })
}

// 新增仓库
export function addWarehouse(data) {
  return request({
    url: '/warehouse/info',
    method: 'post',
    data: data
  })
}

// 修改仓库
export function updateWarehouse(data) {
  return request({
    url: '/warehouse/info',
    method: 'put',
    data: data
  })
}

// 删除仓库
export function delWarehouse(warehouseId) {
  return request({
    url: '/warehouse/info/' + warehouseId,
    method: 'delete'
  })
}

// 同步仓库数据
export function syncWarehouse() {
  return request({
    url: '/warehouse/info/sync',
    method: 'post'
  })
}

// 获取仓库选择框列表
export function optionselectWarehouse() {
  return request({
    url: '/warehouse/info/optionselect',
    method: 'get'
  })
}
