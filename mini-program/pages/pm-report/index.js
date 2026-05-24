const { get, post } = require('../../utils/request')

Page({
  data: {
    materialId: '',
    materialName: '',
    assetCode: '',
    inspectionItems: [],
    selectedItems: [],
    inspector: '',
    inspectionTime: '',
    result: 'normal',
    inspectionCycle: '',
    cycleOptions: [
      { value: 'daily', label: '每日' },
      { value: 'weekly', label: '每周' },
      { value: 'monthly', label: '每月' },
      { value: 'quarterly', label: '每季度' },
      { value: 'yearly', label: '每年' }
    ],
    cycleIndex: 0,
    remark: '',
    details: {}
  },
  onLoad(options) {
    const now = new Date()
    const timeStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
    this.setData({
      materialId: options.materialId || '',
      materialName: decodeURIComponent(options.materialName || ''),
      assetCode: options.assetCode || '',
      inspectionTime: timeStr
    })
    this.loadInspectionItems()
    this.loadCycleDict()
  },
  async loadCycleDict() {
    try {
      const res = await get('/system/dict/data/type/biz_inspection_cycle')
      if (res.data && res.data.length > 0) {
        this.setData({
          cycleOptions: res.data.map(item => ({
            value: item.dictValue,
            label: item.dictLabel
          }))
        })
      }
    } catch (e) {
      console.log('加载字典失败，使用默认值')
    }
  },
  async loadInspectionItems() {
    try {
      const res = await get('/inspection/item/all')
      if (res.data) {
        this.setData({ inspectionItems: res.data })
      }
    } catch (e) {
      console.error('加载检查项失败', e)
    }
  },
  onCycleChange(e) {
    const index = e.detail.value
    this.setData({
      cycleIndex: index,
      inspectionCycle: this.data.cycleOptions[index].value
    })
  },
  toggleItem(e) {
    const itemId = e.currentTarget.dataset.id
    let selectedItems = [...this.data.selectedItems]
    const index = selectedItems.indexOf(itemId)
    if (index > -1) {
      selectedItems.splice(index, 1)
    } else {
      selectedItems.push(itemId)
    }
    this.setData({ selectedItems })
  },
  isItemSelected(itemId) {
    return this.data.selectedItems.indexOf(itemId) > -1
  },
  onResultChange(e) {
    const itemId = e.currentTarget.dataset.id
    const value = e.detail.value
    let details = { ...this.data.details }
    if (!details[itemId]) {
      details[itemId] = {}
    }
    details[itemId].checkResult = value
    this.setData({ details })
  },
  onRemarkInput(e) {
    const itemId = e.currentTarget.dataset.id
    const value = e.detail.value
    let details = { ...this.data.details }
    if (!details[itemId]) {
      details[itemId] = {}
    }
    details[itemId].checkRemark = value
    this.setData({ details })
  },
  onInspectorInput(e) {
    this.setData({ inspector: e.detail.value })
  },
  onRemarkGlobalInput(e) {
    this.setData({ remark: e.detail.value })
  },
  onResultGlobalChange(e) {
    this.setData({ result: e.detail.value })
  },
  async submitReport() {
    if (!this.data.inspector) {
      wx.showToast({ title: '请填写巡检人', icon: 'none' })
      return
    }
    if (this.data.selectedItems.length === 0) {
      wx.showToast({ title: '请至少选择一项检查内容', icon: 'none' })
      return
    }

    const details = []
    for (const itemId of this.data.selectedItems) {
      const detail = this.data.details[itemId] || {}
      details.push({
        itemId: parseInt(itemId),
        checkResult: detail.checkResult || 'normal',
        checkRemark: detail.checkRemark || ''
      })
    }

    const postData = {
      materialId: parseInt(this.data.materialId),
      inspector: this.data.inspector,
      inspectionTime: this.data.inspectionTime,
      result: this.data.result,
      inspectionCycle: this.data.inspectionCycle,
      remark: this.data.remark,
      itemIds: this.data.selectedItems.map(id => parseInt(id)),
      details: details
    }

    try {
      wx.showLoading({ title: '提交中...' })
      await post('/inspection/info', postData)
      wx.hideLoading()
      wx.showToast({
        title: '上报成功',
        icon: 'success',
        duration: 2000
      })
      setTimeout(() => {
        wx.navigateBack()
      }, 2000)
    } catch (e) {
      wx.hideLoading()
    }
  }
})