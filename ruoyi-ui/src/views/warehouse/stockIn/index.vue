<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="入库单号" prop="stockInNo">
        <el-input v-model="queryParams.stockInNo" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="供应商" prop="supplier">
        <el-input v-model="queryParams.supplier" placeholder="请输入供应商" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="入库仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable>
          <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待审核" value="0" />
          <el-option label="已审核" value="1" />
          <el-option label="已驳回" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="入库日期">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['warehouse:stockIn:add']">新增入库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['warehouse:stockIn:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['warehouse:stockIn:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockInList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="入库单号" align="center" prop="stockInNo" width="160" />
      <el-table-column label="供应商" align="center" prop="supplier" show-overflow-tooltip />
      <el-table-column label="入库仓库" align="center" prop="warehouseName" />
      <el-table-column label="入库数量" align="center" prop="totalQuantity" width="80" />
      <el-table-column label="入库金额" align="center" prop="totalAmount" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.totalAmount ? '¥' + scope.row.totalAmount : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入库日期" align="center" prop="inDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.inDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : scope.row.status === '2' ? 'danger' : 'warning'">
            {{ scope.row.status === '1' ? '已审核' : scope.row.status === '2' ? '已驳回' : '待审核' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['warehouse:stockIn:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleAudit(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['warehouse:stockIn:audit']">审核</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['warehouse:stockIn:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 入库单详情对话框 -->
    <el-dialog title="入库单详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="入库单号">{{ detailForm.stockInNo }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ detailForm.supplier }}</el-descriptions-item>
        <el-descriptions-item label="入库仓库">{{ detailForm.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="入库日期">{{ parseTime(detailForm.inDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="发票号">{{ detailForm.invoiceNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发票金额">{{ detailForm.invoiceAmount ? '¥' + detailForm.invoiceAmount : '-' }}</el-descriptions-item>
        <el-descriptions-item label="入库总数量">{{ detailForm.totalQuantity }}</el-descriptions-item>
        <el-descriptions-item label="入库总金额">{{ detailForm.totalAmount ? '¥' + detailForm.totalAmount : '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailForm.status === '1' ? 'success' : detailForm.status === '2' ? 'danger' : 'warning'">
            {{ detailForm.status === '1' ? '已审核' : detailForm.status === '2' ? '已驳回' : '待审核' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">入库明细</el-divider>
      <el-table :data="detailList" border size="small">
        <el-table-column label="物资编码" align="center" prop="materialCode" />
        <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
        <el-table-column label="规格型号" align="center" prop="spec" />
        <el-table-column label="单位" align="center" prop="unit" width="60" />
        <el-table-column label="数量" align="center" prop="quantity" width="70" />
        <el-table-column label="单价" align="center" prop="unitPrice" width="80" />
        <el-table-column label="金额" align="center" prop="amount" width="90" />
        <el-table-column label="批次号" align="center" prop="batchNo" />
        <el-table-column label="保修期限(天)" align="center" prop="warrantyPeriod" width="100" />
      </el-table>
    </el-dialog>

    <!-- 添加或修改入库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="入库单号" prop="stockInNo">
              <el-input v-model="form.stockInNo" placeholder="自动生成" :disabled="!!form.stockInId" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="供应商" prop="supplier">
              <el-input v-model="form.supplier" placeholder="请输入供应商" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="入库仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width:100%">
                <el-option v-for="item in warehouseOptions" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="入库日期" prop="inDate">
              <el-date-picker v-model="form.inDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发票号" prop="invoiceNo">
              <el-input v-model="form.invoiceNo" placeholder="请输入发票号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发票金额" prop="invoiceAmount">
              <el-input-number v-model="form.invoiceAmount" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <el-divider content-position="left">入库明细</el-divider>
      <el-button type="primary" size="mini" icon="el-icon-plus" @click="addDetailRow">添加物资</el-button>

      <el-table :data="form.detailList" border size="small" style="margin-top: 10px">
        <el-table-column label="物资" min-width="180">
          <template slot-scope="scope">
            <el-select v-model="scope.row.materialId" filterable placeholder="选择物资" @change="(val) => onMaterialChange(val, scope.row)" style="width:100%">
              <el-option v-for="item in materialOptions" :key="item.materialId" :label="item.materialCode + ' - ' + item.materialName" :value="item.materialId" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="规格型号" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.spec" size="small" disabled />
          </template>
        </el-table-column>
        <el-table-column label="单位" width="70">
          <template slot-scope="scope">
            <el-input v-model="scope.row.unit" size="small" disabled />
          </template>
        </el-table-column>
        <el-table-column label="数量" width="100">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.quantity" size="small" :min="1" controls-position="right" style="width:80px" />
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.unitPrice" size="small" :precision="2" :min="0" controls-position="right" style="width:100px" />
          </template>
        </el-table-column>
        <el-table-column label="批次号" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.batchNo" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="保修期限(天)" width="110">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.warrantyPeriod" size="small" :min="0" controls-position="right" style="width:90px" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="removeDetailRow(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog title="审核入库单" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="auditForm.status">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="auditRemark">
          <el-input v-model="auditForm.auditRemark" type="textarea" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">确 定</el-button>
        <el-button @click="auditOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockIn, getStockIn, addStockIn, updateStockIn, delStockIn, auditStockIn } from '@/api/warehouse/stockIn'
import { listMaterial } from '@/api/warehouse/material'
import { listWarehouse } from '@/api/warehouse/warehouse'

export default {
  name: 'StockIn',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      stockInList: [],
      detailList: [],
      title: '',
      open: false,
      detailOpen: false,
      auditOpen: false,
      dateRange: [],
      warehouseOptions: [],
      materialOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stockInNo: undefined,
        supplier: undefined,
        warehouseId: undefined,
        status: undefined
      },
      form: {},
      detailForm: {},
      auditForm: {},
      rules: {
        warehouseId: [{ required: true, message: '请选择入库仓库', trigger: 'change' }],
        inDate: [{ required: true, message: '请选择入库日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getWarehouseOptions()
    this.getMaterialOptions()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.params = {
          beginInDate: this.dateRange[0],
          endInDate: this.dateRange[1]
        }
      }
      listStockIn(params).then(response => {
        this.stockInList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getWarehouseOptions() {
      listWarehouse().then(response => {
        this.warehouseOptions = response.rows
      })
    },
    getMaterialOptions() {
      listMaterial({ pageSize: 1000 }).then(response => {
        this.materialOptions = response.rows
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.stockInId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        stockInId: undefined,
        stockInNo: undefined,
        supplier: undefined,
        invoiceNo: undefined,
        invoiceDate: undefined,
        invoiceAmount: undefined,
        warehouseId: undefined,
        totalAmount: undefined,
        totalQuantity: undefined,
        inDate: undefined,
        remark: undefined,
        detailList: []
      }
      this.resetForm('form')
    },
    handleAdd() {
      this.reset()
      this.form.detailList = []
      this.open = true
      this.title = '新增入库单'
    },
    handleUpdate(row) {
      this.reset()
      const stockInId = row.stockInId || this.ids[0]
      getStockIn(stockInId).then(response => {
        this.form = response.data
        if (!this.form.detailList) {
          this.form.detailList = []
        }
        this.open = true
        this.title = '修改入库单'
      })
    },
    handleDetail(row) {
      getStockIn(row.stockInId).then(response => {
        this.detailForm = response.data
        this.detailList = response.data.detailList || []
        this.detailOpen = true
      })
    },
    handleAudit(row) {
      this.auditForm = {
        stockInId: row.stockInId,
        status: '1',
        auditRemark: undefined
      }
      this.auditOpen = true
    },
    addDetailRow() {
      this.form.detailList.push({
        materialId: undefined,
        materialCode: undefined,
        materialName: undefined,
        categoryId: undefined,
        categoryName: undefined,
        spec: undefined,
        unit: undefined,
        quantity: 1,
        unitPrice: undefined,
        amount: undefined,
        batchNo: undefined,
        productionDate: undefined,
        expiryDate: undefined,
        warrantyPeriod: undefined,
        assetCode: undefined
      })
    },
    removeDetailRow(index) {
      this.form.detailList.splice(index, 1)
    },
    onMaterialChange(val, row) {
      const material = this.materialOptions.find(item => item.materialId === val)
      if (material) {
        row.materialCode = material.materialCode
        row.materialName = material.materialName
        row.categoryId = material.categoryId
        row.categoryName = material.categoryName
        row.spec = material.spec
        row.unit = material.unit
        row.unitPrice = material.unitPrice
        row.warrantyPeriod = material.warrantyPeriod
        row.assetCode = material.assetCode
      }
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (!this.form.detailList || this.form.detailList.length === 0) {
            this.$modal.msgError('请至少添加一条入库明细')
            return
          }
          if (this.form.stockInId) {
            updateStockIn(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addStockIn(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    submitAudit() {
      auditStockIn(this.auditForm).then(() => {
        this.$modal.msgSuccess('审核成功')
        this.auditOpen = false
        this.getList()
      })
    },
    handleDelete(row) {
      const stockInIds = row.stockInId || this.ids
      this.$modal.confirm('是否确认删除入库单编号为"' + stockInIds + '"的数据项？').then(() => {
        return delStockIn(stockInIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('warehouse/stockIn/export', { ...this.queryParams }, `stockIn_${new Date().getTime()}.xlsx`)
    },
    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>
