import request from '@/utils/request'

// 查询PM模板列表
export function listPmTemplate(query) {
  return request({
    url: '/pm/template/list',
    method: 'get',
    params: query
  })
}

// 查询PM模板详细
export function getPmTemplate(templateId) {
  return request({
    url: '/pm/template/' + templateId,
    method: 'get'
  })
}

// 查询模板内容列表
export function getContentList(templateId) {
  return request({
    url: '/pm/template/content/' + templateId,
    method: 'get'
  })
}

// 新增PM模板
export function addPmTemplate(data) {
  return request({
    url: '/pm/template',
    method: 'post',
    data: data
  })
}

// 修改PM模板
export function updatePmTemplate(data) {
  return request({
    url: '/pm/template',
    method: 'put',
    data: data
  })
}

// 删除PM模板
export function delPmTemplate(templateId) {
  return request({
    url: '/pm/template/' + templateId,
    method: 'delete'
  })
}

// 导入PM模板
export function importTemplate(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/pm/template/importData',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
