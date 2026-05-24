const app = getApp()

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = app.globalData.token
    wx.request({
      url: `${app.globalData.baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200 || res.data.code === 0) {
            resolve(res.data)
          } else {
            wx.showToast({
              title: res.data.msg || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        } else if (res.statusCode === 401) {
          wx.navigateTo({
            url: '/pages/index/index'
          })
          reject(res.data)
        } else {
          wx.showToast({
            title: '服务器错误',
            icon: 'none'
          })
          reject(res.data)
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

const get = (url, data) => {
  return request({ url, method: 'GET', data })
}

const post = (url, data) => {
  return request({ url, method: 'POST', data })
}

const put = (url, data) => {
  return request({ url, method: 'PUT', data })
}

const del = (url, data) => {
  return request({ url, method: 'DELETE', data })
}

module.exports = {
  request,
  get,
  post,
  put,
  del
}