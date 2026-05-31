<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="物资名称" prop="materialId">
        <el-select v-model="queryParams.materialId" placeholder="请选择物资" filterable clearable>
          <el-option v-for="item in materialOptions" :key="item.materialId" :label="item.materialName" :value="item.materialId" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检人" prop="inspector">
        <el-input v-model="queryParams.inspector" placeholder="请输入巡检人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="巡检结果" prop="result">
        <el-select v-model="queryParams.result" placeholder="巡检结果" clearable>
          <el-option label="正常" value="normal" />
          <el-option label="异常" value="abnormal" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检周期" prop="inspectionCycle">
        <el-select v-model="queryParams.inspectionCycle" placeholder="巡检周期" clearable>
          <el-option label="每日" value="daily" />
          <el-option label="每周" value="weekly" />
          <el-option label="每月" value="monthly" />
          <el-option label="每季度" value="quarterly" />
          <el-option label="每年" value="yearly" />
        </el-select>
      </el-form-item>
      <el-form-item label="巡检时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inspection:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['inspection:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inspection:info:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inspection:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange" row-key="inspectionId">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div class="expand-container">
            <el-descriptions :column="3" border size="small" class="expand-descriptions">
              <el-descriptions-item label="物资编码">{{ scope.row.materialCode }}</el-descriptions-item>
              <el-descriptions-item label="巡检周期">
                {{ scope.row.inspectionCycle === 'daily' ? '每日' : scope.row.inspectionCycle === 'weekly' ? '每周' : scope.row.inspectionCycle === 'monthly' ? '每月' : scope.row.inspectionCycle === 'quarterly' ? '每季度' : scope.row.inspectionCycle === 'yearly' ? '每年' : '-' }}
              </el-descriptions-item>
              <el-descriptions-item label="状态">
                <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
              </el-descriptions-item>
              <el-descriptions-item label="备注" :span="3">{{ scope.row.remark || '-' }}</el-descriptions-item>
            </el-descriptions>

            <div class="expand-section">
              <div class="expand-title">巡检照片</div>
              <div v-if="scope.row.photos" class="photo-list">
                <el-image v-for="(photo, index) in scope.row.photos.split(',')" :key="index" :src="photo" :preview-src-list="scope.row.photos.split(',')" style="width: 80px; height: 80px; margin-right: 8px;" fit="cover" />
              </div>
              <div v-else class="empty-text">暂无照片</div>
            </div>

            <div class="expand-section">
              <div class="expand-title">检查项明细</div>
              <el-table :data="scope.row.details || []" border size="mini" max-height="200">
                <el-table-column label="检查项" prop="itemName" min-width="120" />
                <el-table-column label="检查分组" prop="itemGroup" width="100" />
                <el-table-column label="检查结果" width="100">
                  <template slot-scope="detailScope">
                    <el-tag :type="detailScope.row.checkResult === 'normal' ? 'success' : 'danger'" size="mini">
                      {{ detailScope.row.checkResult === 'normal' ? '正常' : '异常' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="备注" prop="checkRemark" show-overflow-tooltip />
              </el-table>
              <div v-if="!scope.row.details || scope.row.details.length === 0" class="empty-text">暂无检查项明细</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="编号" align="center" prop="inspectionId" width="80" />
      <el-table-column label="物资名称" align="center" prop="materialName" show-overflow-tooltip />
      <el-table-column label="物资编码" align="center" prop="materialCode" />
      <el-table-column label="巡检人" align="center" prop="inspector" width="100" />
      <el-table-column label="巡检时间" align="center" prop="inspectionTime" width="180" />
      <el-table-column label="巡检周期" align="center" width="100">
        <template slot-scope="scope">
          {{ scope.row.inspectionCycle === 'daily' ? '每日' : scope.row.inspectionCycle === 'weekly' ? '每周' : scope.row.inspectionCycle === 'monthly' ? '每月' : scope.row.inspectionCycle === 'quarterly' ? '每季度' : scope.row.inspectionCycle === 'yearly' ? '每年' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="巡检结果" align="center" prop="result" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.result === 'normal' ? 'success' : 'danger'" size="small">
            {{ scope.row.result === 'normal' ? '正常' : '异常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="照片" align="center" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.photos" type="success" size="mini">有</el-tag>
          <el-tag v-else type="info" size="mini">无</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['inspection:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inspection:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改巡检记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="巡检物资" prop="materialId">
              <el-select v-model="form.materialId" placeholder="请选择物资" filterable style="width: 100%">
                <el-option v-for="item in materialOptions" :key="item.materialId" :label="item.materialName" :value="item.materialId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检人" prop="inspector">
              <el-input v-model="form.inspector" placeholder="请输入巡检人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="巡检时间" prop="inspectionTime">
              <el-date-picker v-model="form.inspectionTime" type="datetime" placeholder="请选择巡检时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="巡检周期" prop="inspectionCycle">
              <el-select v-model="form.inspectionCycle" placeholder="请选择巡检周期" style="width: 100%">
                <el-option label="每日" value="daily" />
                <el-option label="每周" value="weekly" />
                <el-option label="每月" value="monthly" />
                <el-option label="每季度" value="quarterly" />
                <el-option label="每年" value="yearly" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="巡检结果" prop="result">
          <el-radio-group v-model="form.result">
            <el-radio label="normal">正常</el-radio>
            <el-radio label="abnormal">异常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检查项">
          <div v-if="pmTemplateContents.length > 0">
            <div v-for="(group, groupName) in groupedPmContents" :key="groupName" style="margin-bottom: 10px;">
              <div style="font-weight: bold; color: #409eff; margin-bottom: 5px;">{{ groupName }}</div>
              <div v-for="item in group" :key="item.contentId" style="margin-bottom: 5px; padding: 5px 10px; background: #f5f7fa; border-radius: 4px;">
                <el-checkbox v-model="item.checked" :label="item.contentId">
                  {{ item.contentName }}
                </el-checkbox>
                <span v-if="item.needValue === '1'" style="margin-left: 10px;">
                  <el-select v-if="item.valueType === 'dropdown'" v-model="item.actualValue" size="mini" style="width: 150px;" placeholder="请选择">
                    <el-option v-for="opt in parseOptions(item.valueOptions)" :key="opt.value" :label="opt.label" :value="opt.value" />
                  </el-select>
                  <el-input v-else v-model="item.actualValue" size="mini" style="width: 150px;" :placeholder="'请输入' + (item.unit || '')" />
                </span>
                <span v-if="item.unit" style="margin-left: 5px; color: #909399; font-size: 12px;">{{ item.unit }}</span>
              </div>
            </div>
          </div>
          <div v-else>
            <el-checkbox-group v-model="form.itemIds">
              <el-checkbox v-for="item in inspectionItemOptions" :key="item.itemId" :label="item.itemId">{{ item.itemName }}</el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
        <el-form-item label="巡检照片">
          <image-upload v-model="form.photos" :limit="5" />
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
import { listInspection, getInspection, addInspection, updateInspection, delInspection } from "@/api/inspection/inspection"
import { allItem } from "@/api/inspection/inspectionItem"
import { listMaterial, getMaterial } from "@/api/warehouse/material"
import { getContentList } from "@/api/asset/pmTemplate"
import ImageUpload from "@/components/ImageUpload"

export default {
  name: "PmInspection",
  components: { ImageUpload },
  dicts: ['sys_normal_disable'],
  computed: {
    groupedPmContents() {
      const groups = {}
      this.pmTemplateContents.forEach(item => {
        const group = item.itemName || '其他'
        if (!groups[group]) groups[group] = []
        groups[group].push(item)
      })
      return groups
    }
  },
  watch: {
    'form.materialId'(val) {
      if (val) {
        this.loadPmTemplateContents(val)
      } else {
        this.pmTemplateContents = []
      }
    }
  },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      inspectionList: [],
      materialOptions: [],
      inspectionItemOptions: [],
      pmTemplateContents: [],
      dateRange: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        materialId: undefined,
        inspector: undefined,
        result: undefined,
        inspectionCycle: undefined
      },
      form: {},
      rules: {
        materialId: [
          { required: true, message: "巡检物资不能为空", trigger: "change" }
        ],
        inspector: [
          { required: true, message: "巡检人不能为空", trigger: "blur" }
        ],
        inspectionTime: [
          { required: true, message: "巡检时间不能为空", trigger: "change" }
        ],
        result: [
          { required: true, message: "巡检结果不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getMaterialOptions()
    this.getInspectionItemOptions()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      }
      listInspection(params).then(response => {
        // 加载每条记录的详情（含检查项）
        const list = response.rows || []
        this.total = response.total
        // 批量加载详情
        Promise.all(list.map(item => getInspection(item.inspectionId))).then(details => {
          details.forEach((res, index) => {
            if (res.data) {
              list[index].details = res.data.details || []
              list[index].photos = res.data.photos
              list[index].inspectionCycle = res.data.inspectionCycle
            }
          })
          this.inspectionList = list
          this.loading = false
        }).catch(() => {
          this.inspectionList = list
          this.loading = false
        })
      })
    },
    getMaterialOptions() {
      listMaterial({ pageSize: 1000 }).then(response => {
        this.materialOptions = response.rows
      })
    },
    getInspectionItemOptions() {
      allItem().then(response => {
        this.inspectionItemOptions = response.data
      })
    },
    loadPmTemplateContents(materialId) {
      // 先获取物资信息，找到绑定的PM模板
      getMaterial(materialId).then(res => {
        const material = res.data
        if (material && material.pmTemplateId) {
          // 加载PM模板内容
          getContentList(material.pmTemplateId).then(contentRes => {
            this.pmTemplateContents = (contentRes.data || []).map(item => ({
              ...item,
              checked: true,  // 默认全部选中
              actualValue: item.defaultValue || '0'
            }))
          })
        } else {
          this.pmTemplateContents = []
        }
      }).catch(() => {
        this.pmTemplateContents = []
      })
    },
    parseOptions(optionsStr) {
      if (!optionsStr) return []
      return optionsStr.split('|').map(opt => {
        const parts = opt.split('-')
        return { value: parts[0], label: parts.slice(1).join('-') || parts[0] }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      const now = new Date()
      const timeStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
      this.form = {
        inspectionId: undefined,
        materialId: undefined,
        inspector: this.$store.state.user.name,
        inspectionTime: timeStr,
        inspectionCycle: undefined,
        result: "normal",
        status: "0",
        itemIds: [],
        details: [],
        photos: undefined,
        remark: undefined
      }
      this.pmTemplateContents = []
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.inspectionId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加PM巡检记录"
    },
    handleUpdate(row) {
      this.reset()
      const inspectionId = row.inspectionId || this.ids
      getInspection(inspectionId).then(response => {
        this.form = response.data
        if (!this.form.itemIds) {
          this.form.itemIds = []
        }
        this.open = true
        this.title = "修改PM巡检记录"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 如果有PM模板检查项，将其转换为details格式（全部提交）
          if (this.pmTemplateContents.length > 0) {
            this.form.details = this.pmTemplateContents.map(item => ({
              itemId: item.contentId,
              checkResult: item.actualValue === '1' ? 'abnormal' : 'normal',
              checkRemark: ''  // PM模板不存备注，只存结果
            }))
            // 根据检查项结果自动判断总体结果
            const hasAbnormal = this.pmTemplateContents.some(item => item.actualValue === '1')
            this.form.result = hasAbnormal ? 'abnormal' : 'normal'
          }
          if (this.form.inspectionId != undefined) {
            updateInspection(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInspection(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const inspectionIds = row.inspectionId || this.ids
      this.$modal.confirm('是否确认删除巡检记录编号为"' + inspectionIds + '"的数据项？').then(function() {
        return delInspection(inspectionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('inspection/info/export', { ...this.queryParams }, `pm_inspection_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.expand-container {
  padding: 10px 20px;
  background: #fafafa;
}
.expand-descriptions {
  margin-bottom: 15px;
}
.expand-section {
  margin-bottom: 15px;
}
.expand-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
  padding-left: 10px;
  border-left: 3px solid #409eff;
}
.photo-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.empty-text {
  color: #909399;
  font-size: 13px;
  padding: 10px 0;
}
</style>
