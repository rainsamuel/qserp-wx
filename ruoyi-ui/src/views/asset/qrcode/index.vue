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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-printer" size="mini" :disabled="multiple" @click="handleBatchPrint">批量打印标签</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物资编号" align="center" prop="materialId" width="80" />
      <el-table-column label="物资编码" align="center" prop="materialCode" />
      <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
      <el-table-column label="分类" align="center" prop="categoryName" />
      <el-table-column label="规格型号" align="center" prop="spec" show-overflow-tooltip />
      <el-table-column label="库存" align="center" prop="stockQuantity" width="80" />
      <el-table-column label="所在仓库" align="center" prop="warehouseName" />
      <el-table-column label="二维码" align="center" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="previewQRCode(scope.row)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-printer" @click="printSingle(scope.row)">打印</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 二维码预览对话框 -->
    <el-dialog title="资产标签预览" :visible.sync="previewOpen" width="400px" append-to-body>
      <div class="label-preview" ref="singleLabel">
        <div class="label-container">
          <div class="label-header">资产标签</div>
          <div class="label-qrcode" ref="qrcodeSingle"></div>
          <div class="label-info">
            <div class="label-row"><span class="label-key">编码：</span><span class="label-val">{{ previewData.materialCode }}</span></div>
            <div class="label-row"><span class="label-key">名称：</span><span class="label-val">{{ previewData.materialName }}</span></div>
            <div class="label-row"><span class="label-key">规格：</span><span class="label-val">{{ previewData.spec || '-' }}</span></div>
            <div class="label-row"><span class="label-key">仓库：</span><span class="label-val">{{ previewData.warehouseName || '-' }}</span></div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="doPrintSingle">打 印</el-button>
        <el-button @click="previewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 批量打印预览对话框 -->
    <el-dialog title="批量打印预览" :visible.sync="batchOpen" width="800px" append-to-body>
      <div ref="batchLabels" class="batch-labels">
        <div v-for="(item, index) in selectedMaterials" :key="index" class="label-container label-inline">
          <div class="label-header">资产标签</div>
          <div class="label-qrcode" :ref="'qrcodeBatch' + index"></div>
          <div class="label-info">
            <div class="label-row"><span class="label-key">编码：</span><span class="label-val">{{ item.materialCode }}</span></div>
            <div class="label-row"><span class="label-key">名称：</span><span class="label-val">{{ item.materialName }}</span></div>
            <div class="label-row"><span class="label-key">规格：</span><span class="label-val">{{ item.spec || '-' }}</span></div>
            <div class="label-row"><span class="label-key">仓库：</span><span class="label-val">{{ item.warehouseName || '-' }}</span></div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" icon="el-icon-printer" @click="doBatchPrint">批量打 印</el-button>
        <el-button @click="batchOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaterial } from "@/api/warehouse/material"
import { allCategory } from "@/api/warehouse/materialCategory"
import QRCode from 'qrcodejs2'

export default {
  name: "AssetQRCode",
  data() {
    return {
      loading: true,
      showSearch: true,
      multiple: true,
      total: 0,
      materialList: [],
      categoryOptions: [],
      selectedMaterials: [],
      previewOpen: false,
      batchOpen: false,
      previewData: {},
      qrcodeObj: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: undefined,
        materialName: undefined,
        categoryId: undefined
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryOptions()
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
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.selectedMaterials = selection
      this.multiple = !selection.length
    },
    generateQRCode(text, el) {
      if (el) {
        el.innerHTML = ''
      }
      return new QRCode(el, {
        text: text,
        width: 150,
        height: 150,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
      })
    },
    getQRCodeText(material) {
      return JSON.stringify({
        code: material.materialCode,
        name: material.materialName,
        spec: material.spec || '',
        id: material.materialId
      })
    },
    previewQRCode(row) {
      this.previewData = row
      this.previewOpen = true
      this.$nextTick(() => {
        this.generateQRCode(this.getQRCodeText(row), this.$refs.qrcodeSingle)
      })
    },
    printSingle(row) {
      this.previewData = row
      this.previewOpen = true
      this.$nextTick(() => {
        this.generateQRCode(this.getQRCodeText(row), this.$refs.qrcodeSingle)
        this.$nextTick(() => {
          this.doPrintSingle()
        })
      })
    },
    doPrintSingle() {
      const content = this.$refs.singleLabel.innerHTML
      this.printContent(content)
    },
    handleBatchPrint() {
      this.batchOpen = true
      this.$nextTick(() => {
        this.selectedMaterials.forEach((item, index) => {
          const refKey = 'qrcodeBatch' + index
          const el = this.$refs[refKey]
          if (el && el[0]) {
            this.generateQRCode(this.getQRCodeText(item), el[0])
          }
        })
      })
    },
    doBatchPrint() {
      const content = this.$refs.batchLabels.innerHTML
      this.printContent(content)
    },
    printContent(content) {
      const printWindow = window.open('', '_blank')
      printWindow.document.write(`
        <html>
        <head>
          <title>资产标签打印</title>
          <style>
            body { margin: 0; padding: 10px; font-family: 'Microsoft YaHei', sans-serif; }
            .label-container {
              width: 300px;
              border: 1px solid #333;
              padding: 10px;
              margin: 10px;
              display: inline-block;
              vertical-align: top;
              page-break-inside: avoid;
            }
            .label-header {
              text-align: center;
              font-size: 16px;
              font-weight: bold;
              border-bottom: 1px solid #333;
              padding-bottom: 5px;
              margin-bottom: 8px;
            }
            .label-qrcode {
              text-align: center;
              margin: 8px 0;
            }
            .label-qrcode img {
              width: 120px;
              height: 120px;
            }
            .label-info { font-size: 12px; }
            .label-row { margin: 3px 0; display: flex; }
            .label-key { font-weight: bold; min-width: 45px; }
            .label-val { flex: 1; word-break: break-all; }
            @media print {
              body { margin: 0; }
              .label-container { margin: 5px; }
            }
          </style>
        </head>
        <body>${content}</body>
        </html>
      `)
      printWindow.document.close()
      printWindow.onload = function() {
        printWindow.print()
        printWindow.close()
      }
    }
  }
}
</script>

<style scoped>
.label-preview {
  display: flex;
  justify-content: center;
}
.label-container {
  width: 300px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 15px;
  background: #fff;
}
.label-header {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #dcdfe6;
  padding-bottom: 8px;
  margin-bottom: 10px;
}
.label-qrcode {
  text-align: center;
  margin: 10px 0;
}
.label-info {
  font-size: 13px;
}
.label-row {
  margin: 5px 0;
}
.label-key {
  font-weight: bold;
  color: #606266;
}
.label-val {
  color: #303133;
}
.batch-labels {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.label-inline {
  display: inline-block;
  vertical-align: top;
}
</style>
