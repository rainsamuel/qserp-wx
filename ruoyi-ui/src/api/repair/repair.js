import request from '@/utils/request'

// 查询报修记录列表
export function listRepair(query) {
  return request({
    url: '/repair/info/list',
    method: 'get',
    params: query
  })
}

// 查询报修记录详细
export function getRepair(repairId) {
  return request({
    url: '/repair/info/' + repairId,
    method: 'get'
  })
}

// 新增报修记录
export function addRepair(data) {
  return request({
    url: '/repair/info',
    method: 'post',
    data: data
  })
}

// 修改报修记录
export function updateRepair(data) {
  return request({
    url: '/repair/info',
    method: 'put',
    data: data
  })
}

// 变更报修状态
export function changeRepairStatus(data) {
  return request({
    url: '/repair/info/status',
    method: 'put',
    data: data
  })
}

// 删除报修记录
export function delRepair(repairIds) {
  return request({
    url: '/repair/info/' + repairIds,
    method: 'delete'
  })
}
