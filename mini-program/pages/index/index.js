const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    assetCode: '',
    pmCode: '',
    repairCode: '',
    isLogin: false
  },
  onShow() {
    this.setData({
      isLogin: !!app.globalData.token
    })
  },
  onCodeInput(e) {
    this.setData({ assetCode: e.detail.value })
  },
  onPmCodeInput(e) {
    this.setData({ pmCode: e.detail.value })
  },
  onRepairCodeInput(e) {
    this.setData({ repairCode: e.detail.value })
  },
  checkLogin() {
    if (!app.globalData.token) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再进行PM维护上报',
        confirmText: '去登录',
        success: (res) => {
          if (res.confirm) {
            wx.switchTab({ url: '/pages/mine/index' })
          }
        }
      })
      return false
    }
    return true
  },
  scanAssetQuery() {
    this.setData({ mode: 'query' })
    this.scanCode()
  },
  scanPmReport() {
    if (!this.checkLogin()) return
    this.setData({ mode: 'pm' })
    this.scanCode()
  },
  scanRepair() {
    this.setData({ mode: 'repair' })
    this.scanCode()
  },
  repairByCode() {
    if (this.data.repairCode) {
      this.queryAsset(this.data.repairCode, 'repair')
    } else {
      wx.navigateTo({ url: '/pages/repair/index' })
    }
  },
  scanCode() {
    wx.scanCode({
      success: (res) => {
        const assetCode = res.result
        this.queryAsset(assetCode, this.data.mode)
      },
      fail: () => {
        wx.showToast({
          title: '扫码失败，请手动输入',
          icon: 'none'
        })
      }
    })
  },
  queryByCode() {
    if (!this.data.assetCode) {
      wx.showToast({ title: '请输入资产编码', icon: 'none' })
      return
    }
    this.queryAsset(this.data.assetCode, 'query')
  },
  pmByCode() {
    if (!this.checkLogin()) return
    if (!this.data.pmCode) {
      wx.showToast({ title: '请输入资产编码', icon: 'none' })
      return
    }
    this.queryAsset(this.data.pmCode, 'pm')
  },
  async queryAsset(assetCode, mode) {
    try {
      wx.showLoading({ title: '查询中...' })
      const res = await get('/material/info/list', {
        assetCode: assetCode,
        pageNum: 1,
        pageSize: 10
      })
      wx.hideLoading()
      if (res.rows && res.rows.length > 0) {
        const asset = res.rows[0]
        if (mode === 'pm') {
          wx.navigateTo({
            url: `/pages/pm-report/index?materialId=${asset.materialId}&materialName=${encodeURIComponent(asset.materialName)}&assetCode=${asset.assetCode || assetCode}`
          })
        } else if (mode === 'repair') {
          wx.navigateTo({
            url: `/pages/repair/index?assetCode=${encodeURIComponent(asset.assetCode || assetCode)}&assetName=${encodeURIComponent(asset.materialName)}`
          })
        } else {
          wx.navigateTo({
            url: `/pages/asset-detail/index?materialId=${asset.materialId}`
          })
        }
      } else {
        if (mode === 'repair') {
          wx.navigateTo({
            url: `/pages/repair/index?assetCode=${encodeURIComponent(assetCode)}`
          })
        } else {
          wx.showToast({
            title: '未找到资产',
            icon: 'none'
          })
        }
      }
    } catch (e) {
      wx.hideLoading()
    }
  }
})