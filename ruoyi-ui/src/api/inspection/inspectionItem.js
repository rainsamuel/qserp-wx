import request from '@/utils/request'

// 查询巡检内容项列表
export function listItem(query) {
  return request({
    url: '/inspection/item/list',
    method: 'get',
    params: query
  })
}

// 查询所有正常检查项
export function allItem() {
  return request({
    url: '/inspection/item/all',
    method: 'get'
  })
}

// 查询检查项详细
export function getItem(itemId) {
  return request({
    url: '/inspection/item/' + itemId,
    method: 'get'
  })
}

// 新增检查项
export function addItem(data) {
  return request({
    url: '/inspection/item',
    method: 'post',
    data: data
  })
}

// 修改检查项
export function updateItem(data) {
  return request({
    url: '/inspection/item',
    method: 'put',
    data: data
  })
}

// 删除检查项
export function delItem(itemId) {
  return request({
    url: '/inspection/item/' + itemId,
    method: 'delete'
  })
}
