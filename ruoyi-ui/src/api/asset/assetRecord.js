import request from '@/utils/request'

// 查询资产流转记录列表
export function listRecord(query) {
  return request({
    url: '/asset/record/list',
    method: 'get',
    params: query
  })
}

// 查询资产流转记录详细
export function getRecord(recordId) {
  return request({
    url: '/asset/record/' + recordId,
    method: 'get'
  })
}

// 根据物资ID查询流转记录
export function getRecordByMaterial(materialId) {
  return request({
    url: '/asset/record/material/' + materialId,
    method: 'get'
  })
}

// 资产入库
export function stockIn(data) {
  return request({
    url: '/asset/record/in',
    method: 'post',
    data: data
  })
}

// 资产出库
export function stockOut(data) {
  return request({
    url: '/asset/record/out',
    method: 'post',
    data: data
  })
}

// 资产报损
export function doDamage(data) {
  return request({
    url: '/asset/record/damage',
    method: 'post',
    data: data
  })
}

// 资产报废
export function doScrap(data) {
  return request({
    url: '/asset/record/scrap',
    method: 'post',
    data: data
  })
}

// 修改资产流转记录
export function updateRecord(data) {
  return request({
    url: '/asset/record',
    method: 'put',
    data: data
  })
}

// 删除资产流转记录
export function delRecord(recordIds) {
  return request({
    url: '/asset/record/' + recordIds,
    method: 'delete'
  })
}

// 获取物资流转统计
export function getRecordStats(materialId) {
  if (materialId) {
    return request({
      url: '/asset/record/stats/' + materialId,
      method: 'get'
    })
  }
  return request({
    url: '/asset/record/stats',
    method: 'get'
  })
}
