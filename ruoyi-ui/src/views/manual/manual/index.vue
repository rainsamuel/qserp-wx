<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="说明书名称" prop="manualName">
        <el-input v-model="queryParams.manualName" placeholder="请输入说明书名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="关联类型" prop="manualType">
        <el-select v-model="queryParams.manualType" placeholder="请选择类型" clearable>
          <el-option label="仓库说明书" value="warehouse" />
          <el-option label="物资说明书" value="material" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['manual:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['manual:info:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['manual:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="manualList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="manualId" width="80" />
      <el-table-column label="说明书名称" align="center" prop="manualName" show-overflow-tooltip />
      <el-table-column label="关联类型" align="center" prop="manualType" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.manualType === 'warehouse' ? '' : 'success'" size="small">
            {{ scope.row.manualType === 'warehouse' ? '仓库' : '物资' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="关联对象" align="center" prop="refName" show-overflow-tooltip />
      <el-table-column label="文件名" align="center" prop="originalName" show-overflow-tooltip />
      <el-table-column label="版本" align="center" prop="version" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownload(scope.row)">下载</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['manual:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['manual:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改说明书对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="说明书名称" prop="manualName">
          <el-input v-model="form.manualName" placeholder="请输入说明书名称" />
        </el-form-item>
        <el-form-item label="关联类型" prop="manualType">
          <el-select v-model="form.manualType" placeholder="请选择关联类型" @change="handleTypeChange">
            <el-option label="仓库说明书" value="warehouse" />
            <el-option label="物资说明书" value="material" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联对象" prop="refId">
          <el-select v-model="form.refId" placeholder="请选择关联对象" filterable>
            <el-option v-for="item in refOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="版本" prop="version">
          <el-input v-model="form.version" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="上传文件" prop="file">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :limit="1"
            :on-exceed="handleExceed"
            :on-change="handleFileChange"
            :file-list="fileList"
            accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.jpg,.png"
            drag>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">支持 pdf、doc、xls、ppt、txt、图片格式文件</div>
          </el-upload>
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
  </div>
</template>

<script>
import { listManual, getManual, addManual, updateManual, delManual } from "@/api/manual/manual"
import { optionselectWarehouse } from "@/api/warehouse/warehouse"
import { listMaterial } from "@/api/warehouse/material"

export default {
  name: "Manual",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      manualList: [],
      refOptions: [],
      fileList: [],
      uploadFile: null,
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        manualName: undefined,
        manualType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        manualName: [
          { required: true, message: "说明书名称不能为空", trigger: "blur" }
        ],
        manualType: [
          { required: true, message: "关联类型不能为空", trigger: "change" }
        ],
        refId: [
          { required: true, message: "关联对象不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listManual(this.queryParams).then(response => {
        this.manualList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleTypeChange(val) {
      this.form.refId = undefined
      this.refOptions = []
      if (val === 'warehouse') {
        optionselectWarehouse().then(response => {
          this.refOptions = response.data.map(item => ({
            id: item.warehouseId,
            name: item.warehouseName
          }))
        })
      } else if (val === 'material') {
        listMaterial({ pageSize: 1000 }).then(response => {
          this.refOptions = response.rows.map(item => ({
            id: item.materialId,
            name: item.materialName
          }))
        })
      }
    },
    handleExceed() {
      this.$modal.msgWarning("只能上传一个文件，请先移除已选文件")
    },
    handleFileChange(file) {
      this.uploadFile = file.raw
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        manualId: undefined,
        manualName: undefined,
        manualType: undefined,
        refId: undefined,
        version: undefined,
        status: "0",
        remark: undefined
      }
      this.fileList = []
      this.uploadFile = null
      this.refOptions = []
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
      this.ids = selection.map(item => item.manualId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加说明书"
    },
    handleUpdate(row) {
      this.reset()
      const manualId = row.manualId || this.ids
      getManual(manualId).then(response => {
        this.form = response.data
        this.handleTypeChange(this.form.manualType)
        this.open = true
        this.title = "修改说明书"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const formData = new FormData()
          Object.keys(this.form).forEach(key => {
            if (this.form[key] !== undefined && this.form[key] !== null) {
              formData.append(key, this.form[key])
            }
          })
          if (this.uploadFile) {
            formData.append('file', this.uploadFile)
          }
          if (this.form.manualId != undefined) {
            updateManual(formData).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addManual(formData).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const manualIds = row.manualId || this.ids
      this.$modal.confirm('是否确认删除说明书编号为"' + manualIds + '"的数据项？').then(function() {
        return delManual(manualIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleDownload(row) {
      const url = process.env.VUE_APP_BASE_API + '/manual/info/download/' + row.manualId
      const link = document.createElement('a')
      link.href = url
      link.download = row.originalName || ''
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    handlePreview(row) {
      const url = process.env.VUE_APP_BASE_API + '/manual/info/preview/' + row.manualId
      window.open(url, '_blank')
    },
    handleExport() {
      this.download('manual/info/export', { ...this.queryParams }, `manual_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
