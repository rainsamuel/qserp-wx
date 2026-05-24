const { get, post } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    assetCode: '',
    assetName: '',
    location: '',
    faultDesc: '',
    reporter: '',
    reporterPhone: '',
    priority: 'normal',
    priorityOptions: [
      { value: 'low', label: '低' },
      { value: 'normal', label: '普通' },
      { value: 'high', label: '高' },
      { value: 'urgent', label: '紧急' }
    ],
    priorityIndex: 1,
    photos: [],
    myRepairs: [],
    showMyRepairs: false
  },
  onLoad(options) {
    const username = app.globalData.userInfo ? app.globalData.userInfo.username : ''
    this.setData({ reporter: username })
    if (options.assetCode) {
      this.setData({ assetCode: decodeURIComponent(options.assetCode) })
      this.loadAssetInfo(options.assetCode)
    }
    if (options.assetName) {
      this.setData({ assetName: decodeURIComponent(options.assetName) })
    }
  },
  async loadAssetInfo(assetCode) {
    try {
      const res = await get('/material/info/list', { assetCode, pageNum: 1, pageSize: 1 })
      if (res.rows && res.rows.length > 0) {
        const asset = res.rows[0]
        this.setData({
          assetName: asset.materialName,
          location: asset.location || ''
        })
      }
    } catch (e) {
      console.error('加载资产信息失败', e)
    }
  },
  onAssetCodeInput(e) {
    this.setData({ assetCode: e.detail.value })
  },
  onAssetNameInput(e) {
    this.setData({ assetName: e.detail.value })
  },
  onLocationInput(e) {
    this.setData({ location: e.detail.value })
  },
  onFaultDescInput(e) {
    this.setData({ faultDesc: e.detail.value })
  },
  onReporterInput(e) {
    this.setData({ reporter: e.detail.value })
  },
  onPhoneInput(e) {
    this.setData({ reporterPhone: e.detail.value })
  },
  onPriorityChange(e) {
    const index = e.detail.value
    this.setData({
      priorityIndex: index,
      priority: this.data.priorityOptions[index].value
    })
  },
  choosePhoto() {
    const remaining = 5 - this.data.photos.length
    if (remaining <= 0) {
      wx.showToast({ title: '最多上传5张照片', icon: 'none' })
      return
    }
    wx.chooseImage({
      count: remaining,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const newPhotos = [...this.data.photos, ...res.tempFilePaths]
        this.setData({ photos: newPhotos })
      }
    })
  },
  previewPhoto(e) {
    const index = e.currentTarget.dataset.index
    wx.previewImage({
      current: this.data.photos[index],
      urls: this.data.photos
    })
  },
  deletePhoto(e) {
    const index = e.currentTarget.dataset.index
    const photos = [...this.data.photos]
    photos.splice(index, 1)
    this.setData({ photos })
  },
  async uploadPhotos() {
    const uploadedUrls = []
    for (const filePath of this.data.photos) {
      try {
        const res = await new Promise((resolve, reject) => {
          wx.uploadFile({
            url: `${app.globalData.baseUrl}/common/upload`,
            filePath: filePath,
            name: 'file',
            header: {
              'Authorization': app.globalData.token ? `Bearer ${app.globalData.token}` : ''
            },
            success: (res) => {
              if (res.statusCode === 200) {
                const data = JSON.parse(res.data)
                if (data.code === 200) {
                  resolve(data)
                } else {
                  reject(new Error(data.msg))
                }
              } else {
                reject(new Error('上传失败'))
              }
            },
            fail: reject
          })
        })
        uploadedUrls.push(res.fileName)
      } catch (e) {
        console.error('照片上传失败', e)
      }
    }
    return uploadedUrls.join(',')
  },
  async submitRepair() {
    if (!this.data.assetName) {
      wx.showToast({ title: '请输入资产名称', icon: 'none' })
      return
    }
    if (!this.data.faultDesc) {
      wx.showToast({ title: '请描述故障情况', icon: 'none' })
      return
    }
    if (!this.data.reporter) {
      wx.showToast({ title: '请输入报修人', icon: 'none' })
      return
    }

    try {
      wx.showLoading({ title: '提交中...' })

      let photos = ''
      if (this.data.photos.length > 0) {
        photos = await this.uploadPhotos()
      }

      const now = new Date()
      const faultTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`

      const postData = {
        assetCode: this.data.assetCode,
        assetName: this.data.assetName,
        faultDesc: this.data.faultDesc,
        faultTime: faultTime,
        reporter: this.data.reporter,
        reporterPhone: this.data.reporterPhone,
        location: this.data.location,
        priority: this.data.priority,
        photos: photos
      }

      await post('/repair/info/report', postData)
      wx.hideLoading()
      wx.showToast({
        title: '报修成功',
        icon: 'success',
        duration: 2000
      })
      setTimeout(() => {
        wx.navigateBack()
      }, 2000)
    } catch (e) {
      wx.hideLoading()
    }
  },
  async loadMyRepairs() {
    if (!this.data.reporter) {
      wx.showToast({ title: '请先输入报修人', icon: 'none' })
      return
    }
    try {
      wx.showLoading({ title: '加载中...' })
      const res = await get(`/repair/info/reporter/${this.data.reporter}`)
      wx.hideLoading()
      if (res.data) {
        this.setData({
          myRepairs: res.data,
          showMyRepairs: true
        })
      }
    } catch (e) {
      wx.hideLoading()
    }
  },
  closeMyRepairs() {
    this.setData({ showMyRepairs: false })
  },
  scanAsset() {
    wx.scanCode({
      success: (res) => {
        const assetCode = res.result
        this.setData({ assetCode })
        this.loadAssetInfo(assetCode)
      }
    })
  }
})
