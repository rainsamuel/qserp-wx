<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="模板类型" prop="templateType">
        <el-select v-model="queryParams.templateType" placeholder="请选择类型" clearable>
          <el-option label="预防性维护" value="预防性维护" />
          <el-option label="日常巡检" value="日常巡检" />
        </el-select>
      </el-form-item>
      <el-form-item label="模板名称" prop="templateName">
        <el-input v-model="queryParams.templateName" placeholder="请输入模板名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['asset:pmTemplate:add']">新增模板</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['asset:pmTemplate:import']">Excel导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['asset:pmTemplate:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板类型" align="center" prop="templateType" width="120">
        <template slot-scope="scope">
          <el-tag :type="scope.row.templateType === '预防性维护' ? '' : 'success'" size="small">{{ scope.row.templateType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="模板名称" align="center" prop="templateName" show-overflow-tooltip />
      <el-table-column label="检查项数量" align="center" prop="contentCount" width="100" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="small">{{ scope.row.status === '0' ? '正常' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['asset:pmTemplate:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['asset:pmTemplate:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 详情对话框 -->
    <el-dialog title="PM模板详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="模板类型">{{ detailForm.templateType }}</el-descriptions-item>
        <el-descriptions-item label="模板名称">{{ detailForm.templateName }}</el-descriptions-item>
        <el-descriptions-item label="检查项数量">{{ detailList.length }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailForm.status === '0' ? '正常' : '停用' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">模板内容</el-divider>
      <el-table :data="detailList" border size="small" row-key="contentId">
        <el-table-column label="模板项目" align="center" prop="itemName" width="180" />
        <el-table-column label="模板内容" align="center" prop="contentName" show-overflow-tooltip />
        <el-table-column label="需要实测值" align="center" width="100">
          <template slot-scope="scope">{{ scope.row.needValue === '1' ? '是' : '否' }}</template>
        </el-table-column>
        <el-table-column label="实测值类型" align="center" prop="valueType" width="100">
          <template slot-scope="scope">{{ scope.row.valueType === 'dropdown' ? '下拉' : '文本' }}</template>
        </el-table-column>
        <el-table-column label="可取值" align="center" prop="valueOptions" show-overflow-tooltip />
        <el-table-column label="默认值" align="center" prop="defaultValue" width="80" />
        <el-table-column label="单位" align="center" prop="unit" width="60" />
      </el-table>
    </el-dialog>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="form.templateType" placeholder="请选择类型" style="width:100%">
                <el-option label="预防性维护" value="预防性维护" />
                <el-option label="日常巡检" value="日常巡检" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="form.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <el-divider content-position="left">模板内容</el-divider>
      <el-button type="primary" size="mini" icon="el-icon-plus" @click="addContentRow">添加检查项</el-button>
      <el-table :data="form.contentList" border size="small" style="margin-top: 10px">
        <el-table-column label="模板项目" width="160">
          <template slot-scope="scope">
            <el-input v-model="scope.row.itemName" size="small" placeholder="分组名" />
          </template>
        </el-table-column>
        <el-table-column label="模板内容" min-width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.contentName" size="small" placeholder="检查项名称" />
          </template>
        </el-table-column>
        <el-table-column label="需要实测值" width="90">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.needValue" active-value="1" inactive-value="0" />
          </template>
        </el-table-column>
        <el-table-column label="值类型" width="90">
          <template slot-scope="scope">
            <el-select v-model="scope.row.valueType" size="small" style="width:100%">
              <el-option label="下拉" value="dropdown" />
              <el-option label="文本" value="text" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="可取值" width="160">
          <template slot-scope="scope">
            <el-input v-model="scope.row.valueOptions" size="small" placeholder="0-无异常|1-异常" :disabled="scope.row.valueType === 'text'" />
          </template>
        </el-table-column>
        <el-table-column label="默认值" width="80">
          <template slot-scope="scope">
            <el-input v-model="scope.row.defaultValue" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="单位" width="70">
          <template slot-scope="scope">
            <el-input v-model="scope.row.unit" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="60" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="removeContentRow(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入PM模板" :visible.sync="importOpen" width="400px" append-to-body>
      <el-upload ref="upload" :auto-upload="false" :limit="1" :on-change="handleFileChange" :on-exceed="handleFileExceed" accept=".xlsx,.xls" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">仅支持xlsx/xls格式的PM模板文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitImport">确 定</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPmTemplate, getPmTemplate, addPmTemplate, updatePmTemplate, delPmTemplate, importTemplate } from '@/api/asset/pmTemplate'

export default {
  name: 'PmTemplate',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      templateList: [],
      detailList: [],
      title: '',
      open: false,
      detailOpen: false,
      importOpen: false,
      importFile: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        templateType: undefined,
        templateName: undefined,
        status: undefined
      },
      form: {},
      detailForm: {},
      rules: {
        templateType: [{ required: true, message: '请选择模板类型', trigger: 'change' }],
        templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listPmTemplate(this.queryParams).then(response => {
        this.templateList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.templateId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        templateId: undefined,
        templateType: '预防性维护',
        templateName: undefined,
        status: '0',
        remark: undefined,
        contentList: []
      }
      this.resetForm('form')
    },
    addContentRow() {
      if (!this.form.contentList) this.form.contentList = []
      this.form.contentList.push({
        itemName: '',
        contentName: '',
        needValue: '1',
        valueType: 'dropdown',
        valueOptions: '',
        defaultValue: '',
        unit: '',
        sortOrder: this.form.contentList.length + 1,
        status: '0'
      })
    },
    removeContentRow(index) {
      this.form.contentList.splice(index, 1)
    },
    handleAdd() {
      this.reset()
      this.form.contentList = []
      this.open = true
      this.title = '新增PM模板'
    },
    handleUpdate(row) {
      this.reset()
      const templateId = row.templateId || this.ids[0]
      getPmTemplate(templateId).then(response => {
        this.form = response.data
        if (!this.form.contentList) this.form.contentList = []
        this.open = true
        this.title = '修改PM模板'
      })
    },
    handleDetail(row) {
      getPmTemplate(row.templateId).then(response => {
        this.detailForm = response.data
        this.detailList = response.data.contentList || []
        this.detailOpen = true
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.templateId) {
            updatePmTemplate(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addPmTemplate(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const templateIds = row.templateId || this.ids
      this.$modal.confirm('是否确认删除PM模板编号为"' + templateIds + '"的数据项？').then(() => {
        return delPmTemplate(templateIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleImport() {
      this.importFile = null
      this.importOpen = true
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    handleFileExceed() {
      this.$modal.msgWarning('只能上传一个文件')
    },
    submitImport() {
      if (!this.importFile) {
        this.$modal.msgWarning('请选择要导入的文件')
        return
      }
      importTemplate(this.importFile).then(response => {
        this.$modal.msgSuccess(response.msg)
        this.importOpen = false
        this.getList()
      })
    },
    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>
