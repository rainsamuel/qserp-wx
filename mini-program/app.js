App({
  globalData: {
    baseUrl: 'http://localhost:8080',
    token: null,
    userInfo: null
  },
  onLaunch() {
    this.checkLogin()
  },
  checkLogin() {
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
    }
  }
})