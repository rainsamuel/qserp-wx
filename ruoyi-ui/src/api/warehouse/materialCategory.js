import request from '@/utils/request'

// 查询物资分类列表
export function listCategory(query) {
  return request({
    url: '/material/category/list',
    method: 'get',
    params: query
  })
}

// 查询所有分类
export function allCategory() {
  return request({
    url: '/material/category/all',
    method: 'get'
  })
}

// 查询分类详细
export function getCategory(categoryId) {
  return request({
    url: '/material/category/' + categoryId,
    method: 'get'
  })
}

// 新增分类
export function addCategory(data) {
  return request({
    url: '/material/category',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategory(data) {
  return request({
    url: '/material/category',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delCategory(categoryId) {
  return request({
    url: '/material/category/' + categoryId,
    method: 'delete'
  })
}
