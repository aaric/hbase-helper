# hbase-helper

> 华为云表格存储迁移和删除功能。

## 1.启动命令

```bash
java -jar hbase-helper-0.2.0-SNAPSHOT.jar
```

## 2.配置信息

> 同jar目录下的config目录，例如：config/application-dev.yml

```yaml
# Incarcloud settings
incarcloud:
  # HBase配置
  hbase:
    # 表格存储zk信息（正式库）
    zookeeper:
      quorum: 10.0.11.33,10.0.11.34,10.0.11.35

# Helper settings
helper:
  # 迁移功能
  data-migration:
    # 数据表配置
    table-from:
      name: zspress:telemetry_ic
      # 压测库配置
      zookeeper:
        quorum: 10.0.11.33,10.0.11.34,10.0.11.35
    table-to:
      name: zs:telemetry_ic
    # 迁移vin列表
    vin-list:
      - TESTBOX0000000001
  # 删除功能
  data-deletion:
    # 数据表列表
    table:
      name: zs:telemetry_jtt808
    # 删除vin列表
    vin-list:
      - TESTGPS0000000001
```
