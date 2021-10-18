package cn.darkjrong.submeter.service.impl;

import cn.darkjrong.submeter.common.config.TableRuleConfig;
import cn.darkjrong.submeter.common.enums.DateFormatEnum;
import cn.darkjrong.submeter.common.enums.ScheduledCronEnum;
import cn.darkjrong.submeter.common.pojo.dto.QuartzJobModule;
import cn.darkjrong.submeter.common.utils.QuartzUtils;
import cn.darkjrong.submeter.quartz.TableTask;
import cn.darkjrong.submeter.service.TableService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 表业务层实现
 *
 * @author Rong.Jia
 * @date 2021/10/18
 */
@Slf4j
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SpringBootShardingRuleConfigurationProperties springBootShardingRuleConfigurationProperties;

    @Autowired
    private TableRuleConfig tableRuleConfig;

    @Autowired
    private QuartzUtils quartzUtils;

    @Override
    @Async
    public void createTable() {
        Connection conn = null;
        try {

            Set<String> tables = springBootShardingRuleConfigurationProperties.getTables().keySet();
            if (CollectionUtil.isNotEmpty(tables) && CollectionUtil.isNotEmpty(tableRuleConfig.getRules())) {
                conn = jdbcTemplate.getDataSource().getConnection();
                Map<String, String> tableNames = getTables();
                for (Map.Entry<String, String> entry : tableNames.entrySet()) {
//                    ResultSet rs = null;
                    String key = entry.getKey();
                    try {
//                        rs = conn.getMetaData().getTables(null, null, key, null);
                        jdbcTemplate.execute(entry.getValue());
                        log.info("create table success!");
                    } catch (Exception e) {
                        log.error("Table {} is abnormal and the exception information is {}", key, e.getMessage());
                    } finally {
//                        IoUtil.close(rs);
                        IoUtil.close(conn);
                    }
                }
            }
        } catch (Exception e) {
            log.error("createTable {}", e.getMessage());
        } finally {
            IoUtil.close(conn);
        }
    }

    /**
     * 获取表集合
     *
     * @return {@link Map}<{@link String}, {@link String}> key: 表名，value: 建表语句
     */
    private Map<String, String> getTables() {

        // key: 表名，value: 建表语句
        Map<String, String> tableNames = MapUtil.newHashMap();

        Set<String> tables = springBootShardingRuleConfigurationProperties.getTables().keySet();
        if (CollectionUtil.isNotEmpty(tables) && CollectionUtil.isNotEmpty(tableRuleConfig.getRules())) {
            Map<String, TableRuleConfig.TableInfo> rules = tableRuleConfig.getRules();
            for (String table : tables) {
                if (rules.containsKey(table)) {
                    tableNames.putAll(assemblyTableName(table, rules.get(table)));
                }
            }
        }

        return tableNames;
    }

    /**
     * 组装表名
     *
     * @param logicalTable 逻辑表名
     * @param tableInfo    表规则
     * @return {@link Map}<{@link String}, {@link String}> key: 表名，value: 建表语句
     */
    private Map<String, String> assemblyTableName(String logicalTable, TableRuleConfig.TableInfo tableInfo) {

        DateFormatEnum timeFormat = tableInfo.getTimeFormat();
        String accurateFormat = tableInfo.getAccurateFormat();
        String rangeFormat = tableInfo.getRangeFormat();

        // key: 表名，value: 建表语句
        Map<String, String> tableNames = MapUtil.newHashMap();

        if (ObjectUtil.isNotNull(timeFormat)) {
            String tableName = jointName(logicalTable, DateUtil.format(new Date(), timeFormat.getType()));
            tableNames.put(tableName, jointSql(logicalTable, tableName, tableInfo.getTableSql()));

            // 启动定期创建定时器
            startTask(timeFormat);
        } else if (StrUtil.isNotBlank(accurateFormat)) {
            List<String> suffixs = StrUtil.split(accurateFormat, StrUtil.C_COMMA);
            suffixs.forEach(a -> {
                String tableName = jointName(logicalTable, a);
                tableNames.put(tableName, jointSql(logicalTable, tableName, tableInfo.getTableSql()));
            });
        } else if (StrUtil.isNotBlank(rangeFormat)) {
            List<String> suffixs = StrUtil.split(rangeFormat, StrUtil.C_COMMA);
            for (String suffix : suffixs) {
                List<Integer> ranges = StrUtil.split(suffix, "-").stream().map(Convert::toInt).collect(Collectors.toList());
                Integer index = ranges.get(0);
                Integer max = ranges.get(1);
                while (true) {
                    String tableName = jointName(logicalTable, Convert.toStr(index));
                    tableNames.put(tableName, jointSql(logicalTable, tableName, tableInfo.getTableSql()));
                    index++;
                    if (index > max) {
                        break;
                    }
                }
            }
        }

        return tableNames;
    }


    /**
     * 开始任务
     *
     * @param timeFormat 时间格式
     */
    private void startTask(DateFormatEnum timeFormat) {

        QuartzJobModule quartzJobModule = new QuartzJobModule();
        quartzJobModule.setJobName(TableTask.class.getName());
        quartzJobModule.setJobClass(TableTask.class);

        if (StrUtil.equals(DateFormatEnum.YYYY_MM.getType(), timeFormat.getType())) {
            quartzJobModule.setCron(ScheduledCronEnum.YYYY_MM.getValue());
        }else if (StrUtil.equals(DateFormatEnum.YYYY_MM_DD.getType(), timeFormat.getType())) {
            quartzJobModule.setCron(ScheduledCronEnum.YYYY_MM_DD.getValue());
        }else if (StrUtil.equals(DateFormatEnum.YYYY.getType(), timeFormat.getType())) {
            quartzJobModule.setCron(ScheduledCronEnum.YYYY.getValue());
        }

        if (quartzUtils.addJob(quartzJobModule)) {
            log.info("Description Creating a scheduled task succeeded");
        }
    }

    /**
     * 拼接名字
     *
     * @param logicalTableName 逻辑表名
     * @param suffix           后缀
     * @return {@link String}
     */
    private String jointName(String logicalTableName, String suffix) {
        return StrUtil.EMPTY + logicalTableName + StrUtil.UNDERLINE + suffix;
    }

    /**
     * 拼接sql
     *
     * @param oldTable 旧表
     * @param newTable 新表
     * @param sql      sql
     * @return {@link String}
     */
    private String jointSql(String oldTable, String newTable, String sql) {
        return StrUtil.replace(sql, oldTable, newTable);
    }


}

