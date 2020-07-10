# hbase-helper

> 华为云表格存储迁移和删除功能。

## 1.启动命令

```bash
java -jar hbase-helper-0.1.0-SNAPSHOT.jar
```

## 2.配置信息

> 同jar目录下的config目录，例如：config/application-dev.yml

```yaml
# Incarcloud settings
incarcloud:
  # HBase配置
  hbase:
    # 表格存储zk信息
    zookeeper:
      quorum: ${HBASE_ZOOKEEPER_QUORUM:10.0.11.34,10.0.11.35,10.0.11.39}
      property:
        clientPort: ${HBASE_ZOOKEEPER_CLIENT_PORT:2181}

# Helper settings
helper:
  # 迁移功能
  data-migration:
    # 数据表配置
    table:
      from: zs:telemetry_jtt808
      to: zs:telemetry_jtt808_bak
    # 迁移vin列表
    vin-list:
      - TESTGPS0000000001
      - TESTGPS0000000002
  # 删除功能
  data-deletion:
    # 数据表配置
    table: zs:telemetry_jtt808
    # 删除vin列表
    vin-list:
      - TESTGPS0000000003
      - TESTGPS0000000004
      - TESTGPS0000000005
```
