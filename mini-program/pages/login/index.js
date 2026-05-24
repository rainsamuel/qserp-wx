const { post } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    username: '',
    password: '',
    loading: false
  },
  onUsernameInput(e) {
    this.setData({ username: e.detail.value })
  },
  onPasswordInput(e) {
    this.setData({ password: e.detail.value })
  },
  handleLogin() {
    const { username, password } = this.data
    if (!username) {
      wx.showToast({ title: '请输入工号', icon: 'none' })
      return
    }
    if (!password) {
      wx.showToast({ title: '请输入密码', icon: 'none' })
      return
    }

    this.setData({ loading: true })
    post('/login', { username, password, code: '', uuid: '' }).then(res => {
      this.setData({ loading: false })
      if (res.token) {
        wx.setStorageSync('token', res.token)
        app.globalData.token = res.token
        app.globalData.userInfo = { username }
        wx.showToast({ title: '登录成功', icon: 'success' })
        setTimeout(() => {
          wx.navigateBack({ delta: 1 })
        }, 1500)
      }
    }).catch(err => {
      this.setData({ loading: false })
      console.error('登录失败', err)
    })
  }
})