const { get } = require('../../utils/request')

Page({
  data: {
    materialId: '',
    asset: null,
    manuals: [],
    inspections: [],
    changeRecords: [],
    showInspectionDetail: false,
    inspectionDetail: null
  },
  onLoad(options) {
    if (options.materialId) {
      this.setData({ materialId: options.materialId })
      this.loadAssetDetail(options.materialId)
      this.loadManuals(options.materialId)
      this.loadInspections(options.materialId)
      this.loadChangeRecords(options.materialId)
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
  async loadInspections(materialId) {
    try {
      const res = await get(`/inspection/info/material/${materialId}`)
      if (res.data) {
        this.setData({ inspections: res.data })
      }
    } catch (e) {
      console.error('加载巡检记录失败', e)
    }
  },
  async loadChangeRecords(materialId) {
    try {
      const res = await get(`/asset/change/material/${materialId}`)
      if (res.data) {
        this.setData({ changeRecords: res.data })
      }
    } catch (e) {
      console.error('加载变更记录失败', e)
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
  async viewInspectionDetail(e) {
    const inspectionId = e.currentTarget.dataset.id
    try {
      wx.showLoading({ title: '加载中...' })
      const res = await get(`/inspection/info/${inspectionId}`)
      wx.hideLoading()
      if (res.data) {
        const detail = res.data
        // 处理照片URL，补全base URL
        if (detail.photos) {
          const baseUrl = getApp().globalData.baseUrl
          detail.photoList = detail.photos.split(',').map(p => {
            return p.startsWith('http') ? p : `${baseUrl}${p}`
          })
        } else {
          detail.photoList = []
        }
        this.setData({
          inspectionDetail: detail,
          showInspectionDetail: true
        })
      }
    } catch (e) {
      wx.hideLoading()
    }
  },
  closeInspectionDetail() {
    this.setData({ showInspectionDetail: false, inspectionDetail: null })
  },
  previewInspectionPhoto(e) {
    const index = e.currentTarget.dataset.index
    const photos = this.inspectionDetail.photoList || []
    wx.previewImage({
      current: photos[index],
      urls: photos
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
            this.loadInspections(materialId)
            this.loadChangeRecords(materialId)
          } else {
            wx.showToast({ title: '未找到资产', icon: 'none' })
          }
        })
      }
    })
  }
})