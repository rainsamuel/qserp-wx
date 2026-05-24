import request from '@/utils/request'

// 查询资产变更记录列表
export function listChange(query) {
  return request({
    url: '/asset/change/list',
    method: 'get',
    params: query
  })
}

// 查询资产变更记录详细
export function getChange(changeId) {
  return request({
    url: '/asset/change/' + changeId,
    method: 'get'
  })
}

// 根据物资ID查询变更记录
export function getChangeByMaterial(materialId) {
  return request({
    url: '/asset/change/material/' + materialId,
    method: 'get'
  })
}

// 新增资产变更记录
export function addChange(data) {
  return request({
    url: '/asset/change',
    method: 'post',
    data: data
  })
}

// 修改资产变更记录
export function updateChange(data) {
  return request({
    url: '/asset/change',
    method: 'put',
    data: data
  })
}

// 删除资产变更记录
export function delChange(changeIds) {
  return request({
    url: '/asset/change/' + changeIds,
    method: 'delete'
  })
}
