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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
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
  </div>
</template>

<script>
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/warehouse/material"
import { allCategory } from "@/api/warehouse/materialCategory"
import { optionselectWarehouse } from "@/api/warehouse/warehouse"
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
      title: "",
      open: false,
      qrcodeOpen: false,
      qrcodeData: {},
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
      const printWindow = window.open('', '_blank')
      printWindow.document.write(`
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
      printWindow.document.close()
      printWindow.onload = function() { printWindow.print(); printWindow.close() }
    }
  }
}
</script>
