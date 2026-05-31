import request from '@/utils/request'

// 查询入库单列表
export function listStockIn(query) {
  return request({
    url: '/warehouse/stockIn/list',
    method: 'get',
    params: query
  })
}

// 查询入库单详细
export function getStockIn(stockInId) {
  return request({
    url: '/warehouse/stockIn/' + stockInId,
    method: 'get'
  })
}

// 查询入库明细
export function getStockInDetail(stockInId) {
  return request({
    url: '/warehouse/stockIn/detail/' + stockInId,
    method: 'get'
  })
}

// 新增入库单
export function addStockIn(data) {
  return request({
    url: '/warehouse/stockIn',
    method: 'post',
    data: data
  })
}

// 修改入库单
export function updateStockIn(data) {
  return request({
    url: '/warehouse/stockIn',
    method: 'put',
    data: data
  })
}

// 删除入库单
export function delStockIn(stockInId) {
  return request({
    url: '/warehouse/stockIn/' + stockInId,
    method: 'delete'
  })
}

// 审核入库单
export function auditStockIn(data) {
  return request({
    url: '/warehouse/stockIn/audit',
    method: 'put',
    data: data
  })
}
