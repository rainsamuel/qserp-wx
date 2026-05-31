#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
CSV数据解析脚本
用于将资产标签CSV数据转换为SQL导入语句
"""

import csv
import re
from urllib.parse import urlparse, parse_qs

def parse_url(url_str):
    """
    解析URL格式的二维码
    格式：http://qr.ygyfnet.com/?o=91330000MA27U06H83&t=4&i=4220
    返回：(sync_id, org_code)
    """
    if not url_str or not url_str.startswith('http'):
        return None, None

    try:
        parsed = urlparse(url_str)
        params = parse_qs(parsed.query)
        sync_id = params.get('i', [None])[0]
        org_code = params.get('o', [None])[0]
        return sync_id, org_code
    except Exception:
        return None, None

def parse_zjkpbh(zjkpbh):
    """
    解析ZJKPBH字段，提取组织编码
    格式：91330000MA27U06H831505032019041000364
    前18位是组织编码，后面是KPBH
    """
    if not zjkpbh or len(zjkpbh) < 18:
        return None, zjkpbh

    # 前18位是组织编码
    org_code = zjkpbh[:18]
    return org_code, zjkpbh

def escape_sql(value):
    """转义SQL字符串"""
    if value is None:
        return 'NULL'
    value = str(value).replace("'", "''")
    return f"'{value}'"

def main():
    csv_file = '副本zcbq.csv'
    output_file = 'import_data.sql'

    with open(csv_file, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        rows = list(reader)

    print(f"读取到 {len(rows)} 条记录")

    # 生成SQL
    sql_lines = []
    sql_lines.append("-- 自动生成的导入SQL")
    sql_lines.append("-- 执行前请先执行 sync_zcbq.sql 添加字段")
    sql_lines.append("")
    sql_lines.append("-- 批量插入语句")
    sql_lines.append("INSERT INTO biz_material (")
    sql_lines.append("    material_code, material_name, spec, unit, warehouse_id,")
    sql_lines.append("    kpbh, sync_org_code, data_source, status, create_by, create_time")
    sql_lines.append(") VALUES")

    values = []
    for row in rows:
        kpbh = row.get('KPBH', '').strip()
        zbmc = row.get('ZBMC', '').strip()
        wzmc = row.get('WZMC', '').strip()
        ggxh = row.get('GGXH', '').strip()
        zxdw = row.get('ZXDW', '').strip()
        kfid = row.get('KFID', '').strip()
        zjkpbh = row.get('ZJKPBH', '').strip()

        # 物资名称优先使用WZMC，如果没有则使用ZBMC
        material_name = wzmc if wzmc else zbmc

        if not kpbh:
            continue

        # 解析ZJKPBH获取组织编码
        org_code, _ = parse_zjkpbh(zjkpbh)

        # 仓库ID处理
        warehouse_id = kfid if kfid else 'NULL'

        value = f"({escape_sql(kpbh)}, {escape_sql(material_name)}, {escape_sql(ggxh)}, {escape_sql(zxdw)}, {warehouse_id}, {escape_sql(kpbh)}, {escape_sql(org_code)}, 'sync', '0', 'admin', sysdate())"
        values.append(value)

    # 分批插入（每100条一批）
    batch_size = 100
    for i in range(0, len(values), batch_size):
        batch = values[i:i+batch_size]
        if i > 0:
            sql_lines.append("")
            sql_lines.append("INSERT INTO biz_material (")
            sql_lines.append("    material_code, material_name, spec, unit, warehouse_id,")
            sql_lines.append("    kpbh, sync_org_code, data_source, status, create_by, create_time")
            sql_lines.append(") VALUES")

        sql_lines.append(",\n".join(batch) + ";")

    # 添加更新语句（处理已有数据）
    sql_lines.append("")
    sql_lines.append("-- 更新已有记录的同步信息")
    sql_lines.append("UPDATE biz_material m")
    sql_lines.append("SET m.data_source = 'sync', m.update_time = sysdate()")
    sql_lines.append("WHERE m.kpbh IS NOT NULL AND m.data_source = 'manual';")

    # 添加索引创建语句
    sql_lines.append("")
    sql_lines.append("-- 确保索引存在")
    sql_lines.append("CREATE INDEX IF NOT EXISTS idx_kpbh ON biz_material(kpbh);")
    sql_lines.append("CREATE INDEX IF NOT EXISTS idx_sync_id ON biz_material(sync_id);")

    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("\n".join(sql_lines))

    print(f"SQL文件已生成：{output_file}")
    print(f"共 {len(values)} 条记录")

if __name__ == '__main__':
    main()
