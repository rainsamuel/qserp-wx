# 仓库、物资、使用说明书与巡检管理模块 API接口文档

> 基础路径: `/dev-api`
>
> 请求头: `Authorization: Bearer <token>`
>
> Swagger文档地址: `http://localhost:8080/swagger-ui/index.html`

---

## 一、仓库管理模块

### 1.1 查询仓库列表

- **接口地址**: `GET /warehouse/info/list`
- **权限标识**: `warehouse:info:list`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| warehouseCode | String | 否 | 仓库编码（模糊查询） |
| warehouseName | String | 否 | 仓库名称（模糊查询） |
| status | String | 否 | 状态（0正常 1停用） |
| sourceType | String | 否 | 数据来源（manual手动 sync同步） |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

- **响应示例**:
```json
{
  "total": 10,
  "rows": [
    {
      "warehouseId": 1,
      "warehouseCode": "WH001",
      "warehouseName": "主仓库",
      "address": "XX路XX号",
      "contactPerson": "张三",
      "contactPhone": "13800138000",
      "status": "0",
      "sourceType": "manual",
      "sourceId": null,
      "createBy": "admin",
      "createTime": "2026-05-22 10:00:00",
      "remark": null
    }
  ],
  "code": 200,
  "msg": "查询成功"
}
```

---

### 1.2 获取仓库详细信息

- **接口地址**: `GET /warehouse/info/{warehouseId}`
- **权限标识**: `warehouse:info:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| warehouseId | Long | 是 | 仓库ID |

- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": {
    "warehouseId": 1,
    "warehouseCode": "WH001",
    "warehouseName": "主仓库",
    "address": "XX路XX号",
    "contactPerson": "张三",
    "contactPhone": "13800138000",
    "status": "0",
    "sourceType": "manual",
    "sourceId": null,
    "createBy": "admin",
    "createTime": "2026-05-22 10:00:00",
    "updateBy": null,
    "updateTime": null,
    "remark": null
  }
}
```

---

### 1.3 新增仓库

- **接口地址**: `POST /warehouse/info`
- **权限标识**: `warehouse:info:add`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| warehouseCode | String | 是 | 仓库编码（唯一） |
| warehouseName | String | 是 | 仓库名称 |
| address | String | 否 | 仓库地址 |
| contactPerson | String | 否 | 联系人 |
| contactPhone | String | 否 | 联系电话 |
| status | String | 否 | 状态，默认"0" |
| remark | String | 否 | 备注 |

- **请求示例**:
```json
{
  "warehouseCode": "WH002",
  "warehouseName": "备用仓库",
  "address": "YY路YY号",
  "contactPerson": "李四",
  "contactPhone": "13900139000",
  "status": "0"
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 1.4 修改仓库

- **接口地址**: `PUT /warehouse/info`
- **权限标识**: `warehouse:info:edit`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| warehouseId | Long | 是 | 仓库ID |
| warehouseCode | String | 是 | 仓库编码（唯一） |
| warehouseName | String | 是 | 仓库名称 |
| address | String | 否 | 仓库地址 |
| contactPerson | String | 否 | 联系人 |
| contactPhone | String | 否 | 联系电话 |
| status | String | 否 | 状态 |
| remark | String | 否 | 备注 |

- **请求示例**:
```json
{
  "warehouseId": 1,
  "warehouseCode": "WH001",
  "warehouseName": "主仓库（已更新）",
  "address": "新地址XX路",
  "contactPerson": "王五",
  "contactPhone": "13700137000",
  "status": "0"
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 1.5 删除仓库

- **接口地址**: `DELETE /warehouse/info/{warehouseIds}`
- **权限标识**: `warehouse:info:remove`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| warehouseIds | Long[] | 是 | 仓库ID，多个用逗号分隔 |

- **请求示例**: `DELETE /warehouse/info/1,2,3`
- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 1.6 从第三方数据库同步仓库

- **接口地址**: `POST /warehouse/info/sync`
- **权限标识**: `warehouse:info:sync`
- **请求参数**: 无
- **说明**: 读取`application-third-party.yml`中配置的第三方数据库，按配置的SQL语句查询仓库数据，根据`sourceId`判断：已存在的更新，不存在的新增。
- **响应示例**:
```json
{ "msg": "同步成功，共同步15条仓库数据", "code": 200 }
```

---

### 1.7 获取仓库选择框列表

- **接口地址**: `GET /warehouse/info/optionselect`
- **权限标识**: 无需权限
- **说明**: 返回所有状态正常的仓库，用于下拉选择框。
- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": [
    { "warehouseId": 1, "warehouseCode": "WH001", "warehouseName": "主仓库" },
    { "warehouseId": 2, "warehouseCode": "WH002", "warehouseName": "备用仓库" }
  ]
}
```

---

### 1.8 导出仓库数据

- **接口地址**: `POST /warehouse/info/export`
- **权限标识**: `warehouse:info:export`
- **说明**: 将查询结果导出为Excel文件，查询参数同"查询仓库列表"。

---

## 二、物资管理模块

### 2.1 查询物资列表

- **接口地址**: `GET /material/info/list`
- **权限标识**: `material:info:list`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialCode | String | 否 | 物资编码（模糊查询） |
| materialName | String | 否 | 物资名称（模糊查询） |
| categoryId | Long | 否 | 物资分类ID |
| warehouseId | Long | 否 | 所在仓库ID |
| status | String | 否 | 状态（0正常 1停用） |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

- **响应示例**:
```json
{
  "total": 50,
  "rows": [
    {
      "materialId": 1,
      "materialCode": "M001",
      "materialName": "A4打印纸",
      "categoryId": 1,
      "categoryName": "办公用品",
      "spec": "500张/包",
      "unit": "包",
      "stockQuantity": 100,
      "warehouseId": 1,
      "warehouseName": "主仓库",
      "status": "0",
      "createBy": "admin",
      "createTime": "2026-05-22 10:00:00",
      "remark": null
    }
  ],
  "code": 200,
  "msg": "查询成功"
}
```

---

### 2.2 获取物资详细信息

- **接口地址**: `GET /material/info/{materialId}`
- **权限标识**: `material:info:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialId | Long | 是 | 物资ID |

- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": {
    "materialId": 1,
    "materialCode": "M001",
    "materialName": "A4打印纸",
    "categoryId": 1,
    "categoryName": "办公用品",
    "spec": "500张/包",
    "unit": "包",
    "stockQuantity": 100,
    "warehouseId": 1,
    "warehouseName": "主仓库",
    "status": "0",
    "createBy": "admin",
    "createTime": "2026-05-22 10:00:00",
    "updateBy": null,
    "updateTime": null,
    "remark": null
  }
}
```

---

### 2.3 新增物资

- **接口地址**: `POST /material/info`
- **权限标识**: `material:info:add`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialCode | String | 是 | 物资编码（唯一） |
| materialName | String | 是 | 物资名称 |
| categoryId | Long | 否 | 物资分类ID |
| spec | String | 否 | 规格型号 |
| unit | String | 否 | 计量单位 |
| stockQuantity | Integer | 否 | 库存数量，默认0 |
| warehouseId | Long | 否 | 所在仓库ID |
| status | String | 否 | 状态，默认"0" |
| remark | String | 否 | 备注 |

- **请求示例**:
```json
{
  "materialCode": "M002",
  "materialName": "签字笔",
  "categoryId": 1,
  "spec": "0.5mm黑色",
  "unit": "支",
  "stockQuantity": 200,
  "warehouseId": 1,
  "status": "0"
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 2.4 修改物资

- **接口地址**: `PUT /material/info`
- **权限标识**: `material:info:edit`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialId | Long | 是 | 物资ID |
| materialCode | String | 是 | 物资编码（唯一） |
| materialName | String | 是 | 物资名称 |
| categoryId | Long | 否 | 物资分类ID |
| spec | String | 否 | 规格型号 |
| unit | String | 否 | 计量单位 |
| stockQuantity | Integer | 否 | 库存数量 |
| warehouseId | Long | 否 | 所在仓库ID |
| status | String | 否 | 状态 |
| remark | String | 否 | 备注 |

- **请求示例**:
```json
{
  "materialId": 1,
  "materialCode": "M001",
  "materialName": "A4打印纸",
  "spec": "500张/包",
  "unit": "包",
  "stockQuantity": 150,
  "warehouseId": 1,
  "status": "0"
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 2.5 删除物资

- **接口地址**: `DELETE /material/info/{materialIds}`
- **权限标识**: `material:info:remove`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialIds | Long[] | 是 | 物资ID，多个用逗号分隔 |

- **请求示例**: `DELETE /material/info/1,2,3`
- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 2.6 导出物资数据

- **接口地址**: `POST /material/info/export`
- **权限标识**: `material:info:export`
- **说明**: 将查询结果导出为Excel文件，查询参数同"查询物资列表"。

---

## 三、物资分类模块

### 3.1 查询分类列表

- **接口地址**: `GET /material/category/list`
- **权限标识**: `material:category:list`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryName | String | 否 | 分类名称（模糊查询） |
| status | String | 否 | 状态（0正常 1停用） |

- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": [
    {
      "categoryId": 1,
      "parentId": 0,
      "categoryName": "办公用品",
      "orderNum": 1,
      "status": "0",
      "createBy": "admin",
      "createTime": "2026-05-22 10:00:00",
      "remark": "办公用品分类"
    },
    {
      "categoryId": 2,
      "parentId": 0,
      "categoryName": "电子设备",
      "orderNum": 2,
      "status": "0",
      "createBy": "admin",
      "createTime": "2026-05-22 10:00:00",
      "remark": "电子设备分类"
    }
  ]
}
```

---

### 3.2 查询所有分类（下拉选择用）

- **接口地址**: `GET /material/category/all`
- **权限标识**: 无需权限
- **说明**: 返回所有状态正常的分类，用于下拉选择框。

---

### 3.3 获取分类详细信息

- **接口地址**: `GET /material/category/{categoryId}`
- **权限标识**: `material:category:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 是 | 分类ID |

---

### 3.4 新增分类

- **接口地址**: `POST /material/category`
- **权限标识**: `material:category:add`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| parentId | Long | 否 | 父分类ID，默认0 |
| categoryName | String | 是 | 分类名称 |
| orderNum | Integer | 否 | 显示顺序，默认0 |
| status | String | 否 | 状态，默认"0" |
| remark | String | 否 | 备注 |

- **请求示例**:
```json
{
  "parentId": 0,
  "categoryName": "清洁用品",
  "orderNum": 4,
  "status": "0"
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 3.5 修改分类

- **接口地址**: `PUT /material/category`
- **权限标识**: `material:category:edit`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 是 | 分类ID |
| parentId | Long | 否 | 父分类ID |
| categoryName | String | 是 | 分类名称 |
| orderNum | Integer | 否 | 显示顺序 |
| status | String | 否 | 状态 |
| remark | String | 否 | 备注 |

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 3.6 删除分类

- **接口地址**: `DELETE /material/category/{categoryIds}`
- **权限标识**: `material:category:remove`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryIds | Long[] | 是 | 分类ID，多个用逗号分隔 |

- **注意**: 存在下级分类时不允许删除。
- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

## 四、使用说明书模块

### 4.1 查询说明书列表

- **接口地址**: `GET /manual/info/list`
- **权限标识**: `manual:info:list`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualName | String | 否 | 说明书名称（模糊查询） |
| manualType | String | 否 | 关联类型（warehouse仓库 material物资） |
| refId | Long | 否 | 关联ID（仓库ID或物资ID） |
| status | String | 否 | 状态（0正常 1停用） |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

- **响应示例**:
```json
{
  "total": 5,
  "rows": [
    {
      "manualId": 1,
      "manualName": "主仓库使用说明书",
      "manualType": "warehouse",
      "refId": 1,
      "refName": "主仓库",
      "fileName": "2026/05/22/abc123.pdf",
      "originalName": "仓库说明书V1.0.pdf",
      "filePath": "/profile/manual/2026/05/22/abc123.pdf",
      "fileSize": 1024000,
      "fileExt": "pdf",
      "version": "1.0",
      "status": "0",
      "createBy": "admin",
      "createTime": "2026-05-22 10:00:00"
    }
  ],
  "code": 200,
  "msg": "查询成功"
}
```

---

### 4.2 获取说明书详细信息

- **接口地址**: `GET /manual/info/{manualId}`
- **权限标识**: `manual:info:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualId | Long | 是 | 说明书ID |

- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": {
    "manualId": 1,
    "manualName": "主仓库使用说明书",
    "manualType": "warehouse",
    "refId": 1,
    "refName": "主仓库",
    "fileName": "2026/05/22/abc123.pdf",
    "originalName": "仓库说明书V1.0.pdf",
    "filePath": "/profile/manual/2026/05/22/abc123.pdf",
    "fileSize": 1024000,
    "fileExt": "pdf",
    "version": "1.0",
    "status": "0",
    "createBy": "admin",
    "createTime": "2026-05-22 10:00:00"
  }
}
```

---

### 4.3 新增说明书（含文件上传）

- **接口地址**: `POST /manual/info`
- **权限标识**: `manual:info:add`
- **请求方式**: `multipart/form-data`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualName | String | 是 | 说明书名称 |
| manualType | String | 是 | 关联类型（warehouse仓库 material物资） |
| refId | Long | 是 | 关联ID（仓库ID或物资ID） |
| version | String | 否 | 版本号，默认"1.0" |
| status | String | 否 | 状态，默认"0" |
| remark | String | 否 | 备注 |
| file | File | 否 | 说明书文件（支持pdf, doc, docx, xls, xlsx, ppt, pptx, txt, jpg, png等） |

- **cURL示例**:
```bash
curl -X POST "http://localhost:8080/manual/info" \
  -H "Authorization: Bearer <token>" \
  -F "manualName=主仓库使用说明书" \
  -F "manualType=warehouse" \
  -F "refId=1" \
  -F "version=1.0" \
  -F "file=@/path/to/manual.pdf"
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 4.4 修改说明书（含文件上传）

- **接口地址**: `PUT /manual/info`
- **权限标识**: `manual:info:edit`
- **请求方式**: `multipart/form-data`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualId | Long | 是 | 说明书ID |
| manualName | String | 是 | 说明书名称 |
| manualType | String | 是 | 关联类型 |
| refId | Long | 是 | 关联ID |
| version | String | 否 | 版本号 |
| status | String | 否 | 状态 |
| remark | String | 否 | 备注 |
| file | File | 否 | 新文件（不传则保留原文件） |

- **cURL示例**:
```bash
curl -X PUT "http://localhost:8080/manual/info" \
  -H "Authorization: Bearer <token>" \
  -F "manualId=1" \
  -F "manualName=主仓库使用说明书V2.0" \
  -F "manualType=warehouse" \
  -F "refId=1" \
  -F "version=2.0" \
  -F "file=@/path/to/manual_v2.pdf"
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 4.5 删除说明书

- **接口地址**: `DELETE /manual/info/{manualIds}`
- **权限标识**: `manual:info:remove`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualIds | Long[] | 是 | 说明书ID，多个用逗号分隔 |

- **说明**: 删除时会同时删除已上传的物理文件。
- **请求示例**: `DELETE /manual/info/1,2,3`
- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 4.6 下载说明书文件

- **接口地址**: `GET /manual/info/download/{manualId}`
- **权限标识**: `manual:info:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualId | Long | 是 | 说明书ID |

- **说明**: 直接返回文件流，浏览器会触发下载。文件名为原始上传文件名。
- **响应**: `application/octet-stream` 二进制文件流

---

### 4.7 预览说明书文件

- **接口地址**: `GET /manual/info/preview/{manualId}`
- **权限标识**: `manual:info:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| manualId | Long | 是 | 说明书ID |

- **说明**: 返回文件路径信息，前端可根据文件类型进行预览（PDF直接打开，图片内联显示等）。
- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "filePath": "/profile/manual/2026/05/22/abc123.pdf",
  "originalName": "仓库说明书V1.0.pdf",
  "fileExt": "pdf",
  "fileSize": 1024000
}
```

---

### 4.8 导出说明书数据

- **接口地址**: `POST /manual/info/export`
- **权限标识**: `manual:info:export`
- **说明**: 将查询结果导出为Excel文件，查询参数同"查询说明书列表"。

---

## 五、物资巡检模块

### 5.1 查询巡检记录列表

- **接口地址**: `GET /inspection/info/list`
- **权限标识**: `inspection:info:list`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialId | Long | 否 | 物资ID |
| inspector | String | 否 | 巡检人（模糊查询） |
| result | String | 否 | 巡检结果（normal正常 abnormal异常） |
| status | String | 否 | 状态（0正常 1停用） |
| params.beginTime | String | 否 | 巡检开始时间（yyyy-MM-dd HH:mm:ss） |
| params.endTime | String | 否 | 巡检结束时间（yyyy-MM-dd HH:mm:ss） |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

- **响应示例**:
```json
{
  "total": 20,
  "rows": [
    {
      "inspectionId": 1,
      "materialId": 1,
      "materialName": "A4打印纸",
      "materialCode": "M001",
      "inspector": "张三",
      "inspectionTime": "2026-05-22 14:30:00",
      "result": "normal",
      "status": "0",
      "createBy": "admin",
      "createTime": "2026-05-22 14:30:00"
    }
  ],
  "code": 200,
  "msg": "查询成功"
}
```

---

### 5.2 获取巡检记录详细信息（含明细）

- **接口地址**: `GET /inspection/info/{inspectionId}`
- **权限标识**: `inspection:info:query`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| inspectionId | Long | 是 | 巡检ID |

- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": {
    "inspectionId": 1,
    "materialId": 1,
    "materialName": "A4打印纸",
    "materialCode": "M001",
    "inspector": "张三",
    "inspectionTime": "2026-05-22 14:30:00",
    "result": "normal",
    "status": "0",
    "createBy": "admin",
    "createTime": "2026-05-22 14:30:00",
    "details": [
      {
        "detailId": 1,
        "inspectionId": 1,
        "itemId": 1,
        "itemName": "外观是否完好",
        "itemGroup": "外观检查",
        "checkResult": "normal",
        "checkRemark": null
      },
      {
        "detailId": 2,
        "inspectionId": 1,
        "itemId": 3,
        "itemName": "功能是否正常",
        "itemGroup": "功能检查",
        "checkResult": "normal",
        "checkRemark": null
      }
    ]
  }
}
```

---

### 5.3 新增巡检记录

- **接口地址**: `POST /inspection/info`
- **权限标识**: `inspection:info:add`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialId | Long | 是 | 物资ID |
| inspector | String | 是 | 巡检人 |
| inspectionTime | String | 是 | 巡检时间（yyyy-MM-dd HH:mm:ss） |
| result | String | 否 | 巡检结果，默认"normal" |
| remark | String | 否 | 备注 |
| itemIds | Long[] | 是 | 选中的检查项ID列表 |
| details | Array | 否 | 巡检明细（含每项检查结果和备注） |

- **请求示例（简单模式 - 只选检查项）**:
```json
{
  "materialId": 1,
  "inspector": "张三",
  "inspectionTime": "2026-05-22 14:30:00",
  "result": "normal",
  "itemIds": [1, 2, 3, 4, 5, 6]
}
```

- **请求示例（详细模式 - 含每项结果）**:
```json
{
  "materialId": 1,
  "inspector": "张三",
  "inspectionTime": "2026-05-22 14:30:00",
  "result": "abnormal",
  "itemIds": [1, 2, 3],
  "details": [
    {"itemId": 1, "checkResult": "normal", "checkRemark": null},
    {"itemId": 2, "checkResult": "normal", "checkRemark": null},
    {"itemId": 3, "checkResult": "abnormal", "checkRemark": "功能异常，需维修"}
  ]
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 5.4 修改巡检记录

- **接口地址**: `PUT /inspection/info`
- **权限标识**: `inspection:info:edit`
- **请求体**: 同新增，额外需要 `inspectionId`

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| inspectionId | Long | 是 | 巡检ID |

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 5.5 删除巡检记录

- **接口地址**: `DELETE /inspection/info/{inspectionIds}`
- **权限标识**: `inspection:info:remove`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| inspectionIds | Long[] | 是 | 巡检ID，多个用逗号分隔 |

- **说明**: 删除时会同时删除关联的巡检明细。
- **请求示例**: `DELETE /inspection/info/1,2,3`
- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 5.6 导出巡检记录

- **接口地址**: `POST /inspection/info/export`
- **权限标识**: `inspection:info:export`
- **说明**: 将查询结果导出为Excel文件。

---

### 5.7 查询巡检内容项列表

- **接口地址**: `GET /inspection/item/list`
- **权限标识**: `inspection:item:list`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| itemName | String | 否 | 检查项名称（模糊查询） |
| itemGroup | String | 否 | 检查项分组 |
| status | String | 否 | 状态 |

- **响应示例**:
```json
{
  "msg": "操作成功",
  "code": 200,
  "data": [
    {
      "itemId": 1,
      "itemName": "外观是否完好",
      "itemGroup": "外观检查",
      "sortOrder": 1,
      "status": "0",
      "remark": "检查物资外观是否有损坏"
    },
    {
      "itemId": 2,
      "itemName": "标识是否清晰",
      "itemGroup": "外观检查",
      "sortOrder": 2,
      "status": "0",
      "remark": "检查物资标识标签是否清晰可读"
    }
  ]
}
```

---

### 5.8 查询所有正常检查项（下拉选择用）

- **接口地址**: `GET /inspection/item/all`
- **权限标识**: 无需权限
- **说明**: 返回所有状态正常的检查项，用于巡检时勾选。

---

### 5.9 新增巡检内容项

- **接口地址**: `POST /inspection/item`
- **权限标识**: `inspection:item:add`
- **请求体**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| itemName | String | 是 | 检查项名称 |
| itemGroup | String | 否 | 检查项分组（如：外观检查、功能检查、安全检查） |
| sortOrder | Integer | 否 | 排序，默认0 |
| status | String | 否 | 状态，默认"0" |
| remark | String | 否 | 备注 |

- **请求示例**:
```json
{
  "itemName": "电源线是否完好",
  "itemGroup": "安全检查",
  "sortOrder": 3,
  "status": "0"
}
```

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 5.10 修改巡检内容项

- **接口地址**: `PUT /inspection/item`
- **权限标识**: `inspection:item:edit`
- **请求体**: 同新增，额外需要 `itemId`

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

### 5.11 删除巡检内容项

- **接口地址**: `DELETE /inspection/item/{itemIds}`
- **权限标识**: `inspection:item:remove`
- **路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| itemIds | Long[] | 是 | 检查项ID，多个用逗号分隔 |

- **响应示例**:
```json
{ "msg": "操作成功", "code": 200 }
```

---

## 六、通用响应格式

### 成功响应
```json
{ "code": 200, "msg": "操作成功" }
```

### 分页响应
```json
{ "code": 200, "msg": "查询成功", "total": 100, "rows": [...] }
```

### 错误响应
```json
{ "code": 500, "msg": "错误信息" }
```

### 无权限响应
```json
{ "code": 403, "msg": "没有权限，请联系管理员授权" }
```

---

## 七、权限标识汇总

| 模块 | 权限标识 | 说明 |
|------|---------|------|
| 仓库管理 | `warehouse:info:list` | 仓库列表查询 |
| 仓库管理 | `warehouse:info:query` | 仓库详情查询 |
| 仓库管理 | `warehouse:info:add` | 仓库新增 |
| 仓库管理 | `warehouse:info:edit` | 仓库修改 |
| 仓库管理 | `warehouse:info:remove` | 仓库删除 |
| 仓库管理 | `warehouse:info:export` | 仓库导出 |
| 仓库管理 | `warehouse:info:sync` | 仓库同步 |
| 物资管理 | `material:info:list` | 物资列表查询 |
| 物资管理 | `material:info:query` | 物资详情查询 |
| 物资管理 | `material:info:add` | 物资新增 |
| 物资管理 | `material:info:edit` | 物资修改 |
| 物资管理 | `material:info:remove` | 物资删除 |
| 物资管理 | `material:info:export` | 物资导出 |
| 物资分类 | `material:category:list` | 分类列表查询 |
| 物资分类 | `material:category:query` | 分类详情查询 |
| 物资分类 | `material:category:add` | 分类新增 |
| 物资分类 | `material:category:edit` | 分类修改 |
| 物资分类 | `material:category:remove` | 分类删除 |
| 使用说明书 | `manual:info:list` | 说明书列表查询 |
| 使用说明书 | `manual:info:query` | 说明书详情查询/下载/预览 |
| 使用说明书 | `manual:info:add` | 说明书新增 |
| 使用说明书 | `manual:info:edit` | 说明书修改 |
| 使用说明书 | `manual:info:remove` | 说明书删除 |
| 使用说明书 | `manual:info:export` | 说明书导出 |
| 物资巡检 | `inspection:info:list` | 巡检记录列表查询 |
| 物资巡检 | `inspection:info:query` | 巡检记录详情查询 |
| 物资巡检 | `inspection:info:add` | 巡检记录新增 |
| 物资巡检 | `inspection:info:edit` | 巡检记录修改 |
| 物资巡检 | `inspection:info:remove` | 巡检记录删除 |
| 物资巡检 | `inspection:info:export` | 巡检记录导出 |
| 巡检内容项 | `inspection:item:list` | 内容项列表查询 |
| 巡检内容项 | `inspection:item:query` | 内容项详情查询 |
| 巡检内容项 | `inspection:item:add` | 内容项新增 |
| 巡检内容项 | `inspection:item:edit` | 内容项修改 |
| 巡检内容项 | `inspection:item:remove` | 内容项删除 |

---

## 八、第三方数据源配置说明

配置文件: `application-third-party.yml`

```yaml
third-party:
  datasource:
    url: jdbc:mysql://localhost:3306/third_party_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    warehouse-sql: >
      SELECT id, code, name, address, contact_person, contact_phone, status
      FROM warehouse
      WHERE status = '0'
```

**同步SQL字段别名要求**:

| 别名 | 说明 | 必须 |
|------|------|------|
| id | 第三方系统仓库唯一标识 | 是 |
| code | 仓库编码 | 是 |
| name | 仓库名称 | 是 |
| address | 仓库地址 | 否 |
| contact_person | 联系人 | 否 |
| contact_phone | 联系电话 | 否 |
| status | 状态 | 否 |

**同步逻辑**: 根据`id`（sourceId）判断记录是否已存在，存在则更新，不存在则新增。同步来源标记为`sourceType=sync`。
