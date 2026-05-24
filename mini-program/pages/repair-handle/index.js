const { get, put } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    activeTab: 'pending',
    pendingList: [],
    processingList: [],
    showDetail: false,
    currentRepair: null,
    handleResult: '',
    statusOptions: [
      { value: 'processing', label: '接单处理', color: '#FA8C16' },
      { value: 'completed', label: '完成处理', color: '#52C41A' },
      { value: 'rejected', label: '驳回报修', color: '#FF4D4F' }
    ],
    selectedStatus: 'processing'
  },
  onLoad() {
    this.loadRepairs()
  },
  onPullDownRefresh() {
    this.loadRepairs().then(() => {
      wx.stopPullDownRefresh()
    })
  },
  async loadRepairs() {
    try {
      wx.showLoading({ title: '加载中...' })
      const [pendingRes, processingRes] = await Promise.all([
        get('/repair/info/pending'),
        get('/repair/info/processing')
      ])
      wx.hideLoading()
      this.setData({
        pendingList: pendingRes.data || [],
        processingList: processingRes.data || []
      })
    } catch (e) {
      wx.hideLoading()
      wx.showToast({ title: '加载失败', icon: 'none' })
    }
  },
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ activeTab: tab })
  },
  viewDetail(e) {
    const id = e.currentTarget.dataset.id
    const list = this.data.activeTab === 'pending' ? this.data.pendingList : this.data.processingList
    const repair = list.find(item => item.repairId === id)
    if (repair) {
      const baseUrl = app.globalData.baseUrl
      let photoList = []
      if (repair.photos) {
        photoList = repair.photos.split(',').map(p => {
          return p.startsWith('http') ? p : `${baseUrl}${p}`
        })
      }
      this.setData({
        showDetail: true,
        currentRepair: { ...repair, photoList },
        handleResult: '',
        selectedStatus: repair.status === 'pending' ? 'processing' : 'completed'
      })
    }
  },
  closeDetail() {
    this.setData({ showDetail: false, currentRepair: null })
  },
  onHandleResultInput(e) {
    this.setData({ handleResult: e.detail.value })
  },
  noop() {},
  selectStatus(e) {
    const status = e.currentTarget.dataset.status
    this.setData({ selectedStatus: status })
  },
  previewPhoto(e) {
    const index = e.currentTarget.dataset.index
    wx.previewImage({
      current: this.data.currentRepair.photoList[index],
      urls: this.data.currentRepair.photoList
    })
  },
  async submitHandle() {
    if (!this.data.currentRepair) return

    const { selectedStatus, handleResult, currentRepair } = this.data

    if (selectedStatus === 'rejected' && !handleResult) {
      wx.showToast({ title: '请填写驳回原因', icon: 'none' })
      return
    }

    try {
      wx.showLoading({ title: '提交中...' })
      const handler = app.globalData.userInfo ? app.globalData.userInfo.username : ''

      await put('/repair/info/handle', {
        repairId: currentRepair.repairId,
        status: selectedStatus,
        handler: handler,
        handleResult: handleResult || (selectedStatus === 'completed' ? '已处理完成' : '已接单处理')
      })

      wx.hideLoading()
      wx.showToast({ title: '操作成功', icon: 'success' })

      this.closeDetail()
      this.loadRepairs()
    } catch (e) {
      wx.hideLoading()
      wx.showToast({ title: '操作失败', icon: 'none' })
    }
  },
  getPriorityText(priority) {
    const map = { low: '低', normal: '普通', high: '高', urgent: '紧急' }
    return map[priority] || priority
  },
  getStatusText(status) {
    const map = { pending: '待处理', processing: '处理中', completed: '已完成', rejected: '已驳回', cancelled: '已取消' }
    return map[status] || status
  }
})
