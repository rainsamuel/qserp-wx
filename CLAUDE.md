# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

基于 RuoYi v3.9.2 框架的企业物资管理系统，包含后端（Spring Boot 2.5 + MyBatis）、前端（Vue 2 + Element UI）和微信小程序三部分。

## 常用命令

### 后端构建与运行
```bash
# 构建整个项目
mvn clean package -DskipTests

# 仅构建某个模块
mvn clean package -pl ruoyi-admin -am -DskipTests

# 运行后端（端口8080）
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

### 前端开发
```bash
cd ruoyi-ui
npm install
npm run dev        # 开发服务器（端口80，默认代理到 localhost:8080）
npm run build:prod # 生产构建
```

### 小程序开发
使用微信开发者工具打开 `mini-program/` 目录，后端地址配置在 `mini-program/app.js` 的 `globalData.baseUrl`。

## 项目架构

### Maven 模块结构

| 模块 | 职责 |
|---|---|
| `ruoyi-admin` | Web 入口、Controller 层、配置文件 |
| `ruoyi-system` | 业务 Domain、Mapper、Service 实现 |
| `ruoyi-framework` | Spring Security、跨域、资源映射等框架配置 |
| `ruoyi-common` | 工具类、注解（`@Anonymous`、`@Log`）、常量 |
| `ruoyi-quartz` | 定时任务（Quartz），任务类在 `task/` 包下 |
| `ruoyi-generator` | 代码生成器 |
| `ruoyi-ui` | Vue 前端 |

### 后端代码分层

```
com.ruoyi
├── web.controller.system    # REST 接口（Controller）
├── system.domain            # 实体类（继承 BaseEntity）
├── system.mapper            # MyBatis Mapper 接口
├── system.service           # Service 接口
├── system.service.impl      # Service 实现
└── quartz.task              # 定时任务 Bean
```

### 关键约定

- **匿名访问**：Controller 方法加 `@Anonymous` 注解可跳过认证（小程序接口必须加）
- **权限控制**：PC 端接口用 `@PreAuthorize("@ss.hasPermi('xxx:xxx:xxx')")` 控制
- **操作日志**：写操作加 `@Log(title = "xxx", businessType = BusinessType.INSERT)`
- **Mapper XML**：位于 `ruoyi-system/src/main/resources/mapper/system/`，SQL 中关联查询常用 `left join` 获取名称字段
- **配置文件**：`application.yml` 主配置，`application-druid.yml` 数据源，`application-third-party.yml` 第三方数据源

### 数据库

- MySQL 主库：`192.168.9.14:3306/qserp`
- Oracle 第三方：`192.200.45.173:1521:orcl`（资产同步用）
- Redis：`192.168.9.21:6379`
- 文件上传路径：`ruoyi.profile` 配置项（当前 `/Users/wulingkai/uploadPath`）
- 静态资源映射：`/profile/**` 映射到上传目录

### 前端结构

```
ruoyi-ui/src/
├── api/           # API 请求封装（按模块分目录）
├── views/         # 页面组件（路径与菜单 component 对应）
├── components/    # 公共组件（ImageUpload、RightToolbar 等）
└── utils/         # 工具函数（request.js 封装 axios）
```

### 小程序结构

```
mini-program/
├── app.js         # 全局配置（baseUrl、token）
├── utils/request.js  # 请求封装（自动带 Authorization）
└── pages/
    ├── index/         # 首页（扫码入口）
    ├── asset-detail/  # 资产详情（含说明书、巡检记录）
    ├── pm-report/     # PM巡检上报
    ├── repair/        # 报修
    └── login/         # 登录
```

## 业务模块

| 模块 | 表前缀 | 说明 |
|---|---|---|
| 物资管理 | `biz_material` | 物资档案，支持 Oracle 同步 |
| 仓库管理 | `biz_warehouse` | 仓库信息 |
| 入库管理 | `biz_stock_in` | 入库单+明细 |
| 库存盘点 | `biz_stock_check` | 盘点单+明细，完成后自动更新库存 |
| 资产流转 | `biz_asset_record` | 入库/出库/报损/报废 |
| 资产变更 | `biz_asset_change` | 位置/科室/状态变更 |
| 报修管理 | `biz_repair` | 小程序报修 |
| 巡检管理 | `biz_inspection` | 巡检记录+明细 |
| PM模板 | `biz_pm_template` | PM检查模板+内容，物资绑定后巡检使用 |
| 说明书 | `biz_manual` | 文件上传管理，支持预览/下载 |

## 定时任务

在若依后台「系统监控 → 定时任务」配置，调用目标格式：`beanName.methodName()`

| Bean | 方法 | 说明 |
|---|---|---|
| `assetSyncTask` | `syncOracleAsset()` | Oracle 资产数据同步（建议 `0 0 2 * * ?`） |

## 新增功能开发模式

1. **SQL**：在 `sql/` 目录创建建表脚本和菜单 SQL
2. **Domain**：`ruoyi-system/.../domain/` 创建实体类，继承 `BaseEntity`
3. **Mapper**：接口 + XML（放 `resources/mapper/system/`）
4. **Service**：接口 + 实现类
5. **Controller**：`ruoyi-admin/.../controller/system/` 创建 REST 接口
6. **前端页面**：`ruoyi-ui/src/views/` 对应目录下创建 `index.vue`
7. **前端 API**：`ruoyi-ui/src/api/` 对应目录下创建 JS 文件

参考已有的「入库管理」（`BizStockIn`）或「PM模板管理」（`BizPmTemplate`）模块。
