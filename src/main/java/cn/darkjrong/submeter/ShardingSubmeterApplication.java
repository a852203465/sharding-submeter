package cn.darkjrong.submeter;

import cn.hutool.extra.spring.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Import(SpringUtil.class)
@MapperScan(basePackages = "cn.darkjrong.submeter.mapper")
@SpringBootApplication
public class ShardingSubmeterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingSubmeterApplication.class, args);
    }



}
