package cn.darkjrong.submeter.algorithm;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Properties;

/**
 * 主键生成器
 *
 * @author Rong.Jia
 * @date 2021/10/18
 */
public class KeyGenerator implements ShardingKeyGenerator, ApplicationContextAware {

    @Getter
    @Setter
    private Properties properties;
    @Override
    public Comparable<?> generateKey() {
//        return valueOp.increment("id");
        return null;
    }


    /**
     * 设置在yaml内的名字
     */
    @Override
    public String getType() {
        return "auto_increment";
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }
}
