package cn.darkjrong.submeter.common.config;

import cn.darkjrong.submeter.common.enums.DateFormatEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 表规则配置
 *
 * @author Rong.Jia
 * @date 2021/10/18
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.shardingsphere.sharding.create-table-rules")
public class TableRuleConfig {

    /**
     * 创建规则
     */
    private Map<String, TableInfo> rules = new HashMap<>();

    /**
     * 表信息
     *
     * @author Rong.Jia
     * @date 2021/10/18
     */
    @Data
    public static class TableInfo {

        /**
         * 时间格式
         */
        private DateFormatEnum timeFormat;

        /**
         * 精确格式 如：1,2,3,4
         * 允许多个, 用“，”隔开
         */
        private String accurateFormat;

        /**
         * 范围格式： 1-3
         * 允许多个, 用“，”隔开
         */
        private String rangeFormat;

        /**
         * 创表sql
         */
        private String tableSql;


    }


}
