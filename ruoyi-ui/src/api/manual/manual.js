import request from '@/utils/request'

// 查询说明书列表
export function listManual(query) {
  return request({
    url: '/manual/info/list',
    method: 'get',
    params: query
  })
}

// 查询说明书详细
export function getManual(manualId) {
  return request({
    url: '/manual/info/' + manualId,
    method: 'get'
  })
}

// 新增说明书
export function addManual(data) {
  return request({
    url: '/manual/info',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data
  })
}

// 修改说明书
export function updateManual(data) {
  return request({
    url: '/manual/info',
    method: 'put',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data
  })
}

// 删除说明书
export function delManual(manualId) {
  return request({
    url: '/manual/info/' + manualId,
    method: 'delete'
  })
}

// 预览说明书
export function previewManual(manualId) {
  return request({
    url: '/manual/info/preview/' + manualId,
    method: 'get'
  })
}
