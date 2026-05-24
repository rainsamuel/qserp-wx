const app = getApp()

Page({
  data: {
    isLogin: false,
    username: ''
  },
  onShow() {
    this.checkLoginStatus()
  },
  checkLoginStatus() {
    try {
      const token = wx.getStorageSync('token')
      if (token && app.globalData.token) {
        this.setData({
          isLogin: true,
          username: app.globalData.userInfo ? app.globalData.userInfo.username : ''
        })
      } else {
        this.setData({ isLogin: false, username: '' })
      }
    } catch (e) {
      this.setData({ isLogin: false, username: '' })
    }
  },
  goLogin() {
    wx.navigateTo({ url: '/pages/login/index' })
  },
  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token')
          app.globalData.token = null
          app.globalData.userInfo = null
          this.setData({ isLogin: false, username: '' })
        }
      }
    })
  },
  goRepairHandle() {
    wx.navigateTo({ url: '/pages/repair-handle/index' })
  }
})