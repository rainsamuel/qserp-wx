<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="物资编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物资编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物资名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物资名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物资分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
          <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="所在仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable>
          <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="物资状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['material:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['material:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['material:info:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['material:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物资编号" align="center" prop="materialId" width="80" />
      <el-table-column label="物资编码" align="center" prop="materialCode" />
      <el-table-column label="资产编码" align="center" prop="assetCode" show-overflow-tooltip />
      <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
      <el-table-column label="分类" align="center" prop="categoryName" />
      <el-table-column label="规格型号" align="center" prop="spec" show-overflow-tooltip />
      <el-table-column label="单位" align="center" prop="unit" width="60" />
      <el-table-column label="库存" align="center" prop="stockQuantity" width="80" />
      <el-table-column label="所在仓库" align="center" prop="warehouseName" />
      <el-table-column label="使用科室" align="center" prop="useDepartment" show-overflow-tooltip />
      <el-table-column label="位置" align="center" prop="location" show-overflow-tooltip />
      <el-table-column label="管理科室" align="center" prop="manageDepartment" show-overflow-tooltip />
      <el-table-column label="保修期限(天)" align="center" prop="warrantyPeriod" width="100" />
      <el-table-column label="单价" align="center" prop="unitPrice" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.unitPrice ? '¥' + scope.row.unitPrice : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供应商" align="center" prop="supplier" show-overflow-tooltip />
      <el-table-column label="PM模板" align="center" prop="pmTemplateName" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['material:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-printer" @click="handleQRCode(scope.row)">标签</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['material:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改物资对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物资编码" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="请输入物资编码" />
        </el-form-item>
        <el-form-item label="物资名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="物资分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable>
            <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格型号" prop="spec">
          <el-input v-model="form.spec" placeholder="请输入规格型号" />
        </el-form-item>
        <el-form-item label="计量单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="库存数量" prop="stockQuantity">
          <el-input-number v-model="form.stockQuantity" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="所在仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" clearable>
            <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="使用科室" prop="useDepartment">
          <el-input v-model="form.useDepartment" placeholder="请输入使用科室" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入位置" />
        </el-form-item>
        <el-form-item label="管理科室" prop="manageDepartment">
          <el-input v-model="form.manageDepartment" placeholder="请输入管理科室" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="form.unitPrice" :min="0" :precision="2" controls-position="right" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="保修期限(天)" prop="warrantyPeriod">
          <el-input-number v-model="form.warrantyPeriod" :min="0" controls-position="right" placeholder="请输入保修期限" />
        </el-form-item>
        <el-form-item label="PM模板" prop="pmTemplateId">
          <el-select v-model="form.pmTemplateId" placeholder="请选择PM巡检模板" clearable filterable>
            <el-option v-for="item in pmTemplateOptions" :key="item.templateId" :label="item.templateType + ' - ' + item.templateName" :value="item.templateId" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商" prop="supplier">
          <el-input v-model="form.supplier" placeholder="请输入供应商" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 资产标签预览 -->
    <el-dialog title="资产标签" :visible.sync="qrcodeOpen" width="400px" append-to-body>
      <div style="text-align: center;">
        <div style="border: 1px solid #dcdfe6; display: inline-block; padding: 15px; border-radius: 4px;">
          <div style="font-size: 16px; font-weight: bold; margin-bottom: 10px;">资产标签</div>
          <div ref="qrcodeEl" style="margin: 10px 0;"></div>
          <div style="font-size: 13px; text-align: left; margin-top: 10px;">
            <div>编码：{{ qrcodeData.materialCode }}</div>
            <div>名称：{{ qrcodeData.materialName }}</div>
            <div>规格：{{ qrcodeData.spec || '-' }}</div>
            <div>仓库：{{ qrcodeData.warehouseName || '-' }}</div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="doPrintLabel">打 印</el-button>
        <el-button @click="qrcodeOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 物资详情对话框 -->
    <el-dialog title="物资详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-tabs v-model="detailTab">
        <el-tab-pane label="基本信息" name="info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="物资编码">{{ detailData.materialCode }}</el-descriptions-item>
            <el-descriptions-item label="资产编码">{{ detailData.assetCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="物资名称">{{ detailData.materialName }}</el-descriptions-item>
            <el-descriptions-item label="物资分类">{{ detailData.categoryName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="规格型号">{{ detailData.spec || '-' }}</el-descriptions-item>
            <el-descriptions-item label="计量单位">{{ detailData.unit || '-' }}</el-descriptions-item>
            <el-descriptions-item label="库存数量">{{ detailData.stockQuantity }}</el-descriptions-item>
            <el-descriptions-item label="所在仓库">{{ detailData.warehouseName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="使用科室">{{ detailData.useDepartment || '-' }}</el-descriptions-item>
            <el-descriptions-item label="位置">{{ detailData.location || '-' }}</el-descriptions-item>
            <el-descriptions-item label="管理科室">{{ detailData.manageDepartment || '-' }}</el-descriptions-item>
            <el-descriptions-item label="单价">{{ detailData.unitPrice ? '¥' + detailData.unitPrice : '-' }}</el-descriptions-item>
            <el-descriptions-item label="保修期限">{{ detailData.warrantyPeriod ? detailData.warrantyPeriod + '天' : '-' }}</el-descriptions-item>
            <el-descriptions-item label="PM模板">{{ detailData.pmTemplateName || '未绑定' }}</el-descriptions-item>
            <el-descriptions-item label="供应商">{{ detailData.supplier || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <dict-tag :options="dict.type.sys_normal_disable" :value="detailData.status" />
            </el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="PM巡检记录" name="inspection">
          <el-table :data="inspectionRecords" border size="small" max-height="400">
            <el-table-column label="巡检时间" prop="inspectionTime" width="180" />
            <el-table-column label="巡检人" prop="inspector" width="100" />
            <el-table-column label="巡检结果" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.result === 'normal' ? 'success' : 'danger'" size="small">
                  {{ scope.row.result === 'normal' ? '正常' : '异常' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="巡检周期" width="100">
              <template slot-scope="scope">
                {{ scope.row.inspectionCycle === 'daily' ? '每日' : scope.row.inspectionCycle === 'weekly' ? '每周' : scope.row.inspectionCycle === 'monthly' ? '每月' : scope.row.inspectionCycle === 'quarterly' ? '每季度' : scope.row.inspectionCycle === 'yearly' ? '每年' : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="remark" show-overflow-tooltip />
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-view" @click="handleInspectionDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div v-if="inspectionRecords.length === 0" style="text-align: center; padding: 20px; color: #909399;">暂无巡检记录</div>
        </el-tab-pane>
        <el-tab-pane label="资产变更记录" name="change">
          <el-table :data="changeRecords" border size="small" max-height="400">
            <el-table-column label="变更时间" prop="changeTime" width="180" />
            <el-table-column label="变更类型" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.changeType === 'location' ? '' : scope.row.changeType === 'department' ? 'warning' : scope.row.changeType === 'status' ? 'danger' : 'info'" size="small">
                  {{ scope.row.changeType === 'location' ? '位置变更' : scope.row.changeType === 'department' ? '科室变更' : scope.row.changeType === 'status' ? '状态变更' : '其他' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="变更内容" prop="changeContent" show-overflow-tooltip />
            <el-table-column label="变更前" prop="oldValue" width="120" />
            <el-table-column label="变更后" prop="newValue" width="120" />
            <el-table-column label="操作人" prop="operator" width="100" />
          </el-table>
          <div v-if="changeRecords.length === 0" style="text-align: center; padding: 20px; color: #909399;">暂无变更记录</div>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 巡检详情对话框 -->
    <el-dialog title="巡检详情" :visible.sync="inspectionDetailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="物资名称">{{ inspectionDetailData.materialName }}</el-descriptions-item>
        <el-descriptions-item label="物资编码">{{ inspectionDetailData.materialCode }}</el-descriptions-item>
        <el-descriptions-item label="巡检人">{{ inspectionDetailData.inspector }}</el-descriptions-item>
        <el-descriptions-item label="巡检时间">{{ inspectionDetailData.inspectionTime }}</el-descriptions-item>
        <el-descriptions-item label="巡检结果">
          <el-tag :type="inspectionDetailData.result === 'normal' ? 'success' : 'danger'" size="small">
            {{ inspectionDetailData.result === 'normal' ? '正常' : '异常' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="巡检周期">
          {{ inspectionDetailData.inspectionCycle === 'daily' ? '每日' : inspectionDetailData.inspectionCycle === 'weekly' ? '每周' : inspectionDetailData.inspectionCycle === 'monthly' ? '每月' : inspectionDetailData.inspectionCycle === 'quarterly' ? '每季度' : inspectionDetailData.inspectionCycle === 'yearly' ? '每年' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="dict.type.sys_normal_disable" :value="inspectionDetailData.status" />
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ inspectionDetailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">巡检照片</el-divider>
      <div v-if="inspectionDetailData.photos" style="display: flex; flex-wrap: wrap; gap: 8px;">
        <el-image v-for="(photo, index) in inspectionDetailData.photos.split(',')" :key="index" :src="photo" :preview-src-list="inspectionDetailData.photos.split(',')" style="width: 100px; height: 100px;" fit="cover" />
      </div>
      <div v-else style="color: #909399;">暂无照片</div>
      <el-divider content-position="left">检查项明细</el-divider>
      <el-table :data="inspectionDetailData.details || []" border size="small">
        <el-table-column label="检查项" prop="itemName" />
        <el-table-column label="检查分组" prop="itemGroup" />
        <el-table-column label="检查结果" prop="checkResult">
          <template slot-scope="scope">
            <el-tag :type="scope.row.checkResult === 'normal' ? 'success' : 'danger'" size="small">
              {{ scope.row.checkResult === 'normal' ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="checkRemark" show-overflow-tooltip />
      </el-table>
      <div slot="footer">
        <el-button @click="inspectionDetailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/warehouse/material"
import { allCategory } from "@/api/warehouse/materialCategory"
import { optionselectWarehouse } from "@/api/warehouse/warehouse"
import { listPmTemplate } from "@/api/asset/pmTemplate"
import { getInspection, getInspectionByMaterial } from "@/api/inspection/inspection"
import { getChangeByMaterial } from "@/api/asset/assetChange"
import QRCode from 'qrcodejs2'

export default {
  name: "Material",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      materialList: [],
      categoryOptions: [],
      warehouseOptions: [],
      pmTemplateOptions: [],
      title: "",
      open: false,
      qrcodeOpen: false,
      qrcodeData: {},
      detailOpen: false,
      detailTab: "info",
      detailData: {},
      inspectionRecords: [],
      changeRecords: [],
      inspectionDetailOpen: false,
      inspectionDetailData: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: undefined,
        materialName: undefined,
        categoryId: undefined,
        warehouseId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        materialCode: [
          { required: true, message: "物资编码不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "物资名称不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryOptions()
    this.getWarehouseOptions()
    this.getPmTemplateOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listMaterial(this.queryParams).then(response => {
        this.materialList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getCategoryOptions() {
      allCategory().then(response => {
        this.categoryOptions = response.data
      })
    },
    getWarehouseOptions() {
      optionselectWarehouse().then(response => {
        this.warehouseOptions = response.data
      })
    },
    getPmTemplateOptions() {
      listPmTemplate({ pageSize: 1000, status: '0' }).then(response => {
        this.pmTemplateOptions = response.rows || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        materialId: undefined,
        materialCode: undefined,
        materialName: undefined,
        categoryId: undefined,
        spec: undefined,
        unit: undefined,
        stockQuantity: 0,
        warehouseId: undefined,
        useDepartment: undefined,
        location: undefined,
        manageDepartment: undefined,
        unitPrice: undefined,
        supplier: undefined,
        warrantyPeriod: undefined,
        status: "0",
        remark: undefined
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.materialId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加物资"
    },
    handleUpdate(row) {
      this.reset()
      const materialId = row.materialId || this.ids
      getMaterial(materialId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改物资"
      })
    },
    handleDetail(row) {
      this.detailData = row
      this.detailTab = "info"
      this.detailOpen = true
      this.loadInspectionRecords(row.materialId)
      this.loadChangeRecords(row.materialId)
    },
    loadInspectionRecords(materialId) {
      getInspectionByMaterial(materialId).then(response => {
        this.inspectionRecords = response.data || []
      })
    },
    loadChangeRecords(materialId) {
      getChangeByMaterial(materialId).then(response => {
        this.changeRecords = response.data || []
      })
    },
    handleInspectionDetail(row) {
      getInspection(row.inspectionId).then(response => {
        this.inspectionDetailData = response.data
        this.inspectionDetailOpen = true
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.materialId != undefined) {
            updateMaterial(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMaterial(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const materialIds = row.materialId || this.ids
      this.$modal.confirm('是否确认删除物资编号为"' + materialIds + '"的数据项？').then(function() {
        return delMaterial(materialIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('material/info/export', { ...this.queryParams }, `material_${new Date().getTime()}.xlsx`)
    },
    handleQRCode(row) {
      this.qrcodeData = row
      this.qrcodeOpen = true
      this.$nextTick(() => {
        this.$refs.qrcodeEl.innerHTML = ''
        new QRCode(this.$refs.qrcodeEl, {
          text: JSON.stringify({ code: row.materialCode, name: row.materialName, spec: row.spec || '', id: row.materialId }),
          width: 150,
          height: 150,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
      })
    },
    doPrintLabel() {
      const content = this.$refs.qrcodeEl.parentElement.innerHTML
      const iframe = document.createElement('iframe')
      iframe.style.position = 'absolute'
      iframe.style.width = '0'
      iframe.style.height = '0'
      iframe.style.border = 'none'
      document.body.appendChild(iframe)
      const doc = iframe.contentDocument || iframe.contentWindow.document
      doc.open()
      doc.write(`
        <html><head><title>资产标签</title>
        <style>
          body { margin: 0; padding: 20px; font-family: 'Microsoft YaHei', sans-serif; text-align: center; }
          .label-box { border: 1px solid #333; display: inline-block; padding: 15px; }
          .title { font-size: 16px; font-weight: bold; margin-bottom: 10px; }
          .info { font-size: 12px; text-align: left; margin-top: 10px; }
          .info div { margin: 3px 0; }
          img { width: 120px; height: 120px; }
        </style></head>
        <body><div class="label-box">${content}</div></body></html>
      `)
      doc.close()
      iframe.contentWindow.focus()
      iframe.contentWindow.print()
      setTimeout(() => document.body.removeChild(iframe), 1000)
    }
  }
}
</script>
