import request from '@/utils/request'

// 查询巡检记录列表
export function listInspection(query) {
  return request({
    url: '/inspection/info/list',
    method: 'get',
    params: query
  })
}

// 查询巡检记录详细
export function getInspection(inspectionId) {
  return request({
    url: '/inspection/info/' + inspectionId,
    method: 'get'
  })
}

// 新增巡检记录
export function addInspection(data) {
  return request({
    url: '/inspection/info',
    method: 'post',
    data: data
  })
}

// 修改巡检记录
export function updateInspection(data) {
  return request({
    url: '/inspection/info',
    method: 'put',
    data: data
  })
}

// 删除巡检记录
export function delInspection(inspectionId) {
  return request({
    url: '/inspection/info/' + inspectionId,
    method: 'delete'
  })
}
