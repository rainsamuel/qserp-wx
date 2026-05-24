const { get } = require('../../utils/request')

Page({
  data: {
    assetCode: '',
    pmCode: ''
  },
  onCodeInput(e) {
    this.setData({ assetCode: e.detail.value })
  },
  onPmCodeInput(e) {
    this.setData({ pmCode: e.detail.value })
  },
  scanAssetQuery() {
    this.setData({ mode: 'query' })
    this.scanCode()
  },
  scanPmReport() {
    this.setData({ mode: 'pm' })
    this.scanCode()
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
        } else {
          wx.navigateTo({
            url: `/pages/asset-detail/index?materialId=${asset.materialId}`
          })
        }
      } else {
        wx.showToast({
          title: '未找到资产',
          icon: 'none'
        })
      }
    } catch (e) {
      wx.hideLoading()
    }
  }
})