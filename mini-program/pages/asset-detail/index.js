const { get } = require('../../utils/request')

Page({
  data: {
    materialId: '',
    asset: null,
    manuals: []
  },
  onLoad(options) {
    if (options.materialId) {
      this.setData({ materialId: options.materialId })
      this.loadAssetDetail(options.materialId)
      this.loadManuals(options.materialId)
    }
  },
  async loadAssetDetail(materialId) {
    try {
      wx.showLoading({ title: '加载中...' })
      const res = await get(`/material/info/${materialId}`)
      wx.hideLoading()
      if (res.data) {
        this.setData({ asset: res.data })
      }
    } catch (e) {
      wx.hideLoading()
    }
  },
  async loadManuals(materialId) {
    try {
      const res = await get('/manual/info/list', {
        materialId: materialId,
        pageNum: 1,
        pageSize: 50
      })
      if (res.rows) {
        this.setData({ manuals: res.rows })
      }
    } catch (e) {
      console.error('加载说明书失败', e)
    }
  },
  previewManual(e) {
    const manualId = e.currentTarget.dataset.id
    wx.showLoading({ title: '获取文件...' })
    get(`/manual/info/preview/${manualId}`).then(res => {
      wx.hideLoading()
      if (res.filePath) {
        const fileUrl = `${getApp().globalData.baseUrl}${res.filePath}`
        wx.downloadFile({
          url: fileUrl,
          success: (downloadRes) => {
            if (downloadRes.statusCode === 200) {
              wx.openDocument({
                filePath: downloadRes.tempFilePath,
                showMenu: true,
                success: () => {},
                fail: () => {
                  wx.showToast({ title: '无法打开文件', icon: 'none' })
                }
              })
            }
          },
          fail: () => {
            wx.showToast({ title: '下载失败', icon: 'none' })
          }
        })
      }
    }).catch(() => {
      wx.hideLoading()
    })
  },
  scanAgain() {
    wx.scanCode({
      success: (res) => {
        const assetCode = res.result
        get('/material/info/list', {
          assetCode: assetCode,
          pageNum: 1,
          pageSize: 10
        }).then(listRes => {
          if (listRes.rows && listRes.rows.length > 0) {
            const materialId = listRes.rows[0].materialId
            this.setData({ materialId })
            this.loadAssetDetail(materialId)
            this.loadManuals(materialId)
          } else {
            wx.showToast({ title: '未找到资产', icon: 'none' })
          }
        })
      }
    })
  }
})