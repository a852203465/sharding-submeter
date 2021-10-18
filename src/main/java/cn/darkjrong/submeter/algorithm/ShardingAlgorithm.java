package cn.darkjrong.submeter.algorithm;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * 分片算法类
 *
 * @author Rong.Jia
 * @date 2021/10/15
 */
@Slf4j
@Component
public class ShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * sql 中 = 操作时，table的映射
     * 根据传进来的日期命名表名称
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> shardingValue) {

        log.info("tableName : {}, columnName : {}, value : {}", shardingValue.getLogicTableName(), shardingValue.getColumnName(), shardingValue.getValue());

        //preciseShardingValue就是当前插入的字段值
        //collection 内就是所有的逻辑表
        //获取字段值
        Long time = shardingValue.getValue();
        String year = DateUtil.format(new Date(time), DatePattern.SIMPLE_MONTH_PATTERN);
        return shardingValue.getLogicTableName().concat(StrUtil.UNDERLINE).concat(year);
    }
}
