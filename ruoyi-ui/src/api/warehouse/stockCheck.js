import request from '@/utils/request'

// 查询盘点单列表
export function listStockCheck(query) {
  return request({
    url: '/warehouse/stockCheck/list',
    method: 'get',
    params: query
  })
}

// 查询盘点单详细
export function getStockCheck(checkId) {
  return request({
    url: '/warehouse/stockCheck/' + checkId,
    method: 'get'
  })
}

// 查询盘点明细列表
export function getDetailList(checkId) {
  return request({
    url: '/warehouse/stockCheck/detail/' + checkId,
    method: 'get'
  })
}

// 新增盘点单
export function addStockCheck(data) {
  return request({
    url: '/warehouse/stockCheck',
    method: 'post',
    data: data
  })
}

// 修改盘点单
export function updateStockCheck(data) {
  return request({
    url: '/warehouse/stockCheck',
    method: 'put',
    data: data
  })
}

// 删除盘点单
export function delStockCheck(checkId) {
  return request({
    url: '/warehouse/stockCheck/' + checkId,
    method: 'delete'
  })
}

// 完成盘点
export function completeStockCheck(data) {
  return request({
    url: '/warehouse/stockCheck/complete',
    method: 'put',
    data: data
  })
}
