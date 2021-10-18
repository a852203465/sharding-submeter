package cn.darkjrong.submeter;

import cn.darkjrong.submeter.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ShardingSubmeterApplicationRunner implements ApplicationRunner {

    @Autowired
    private TableService tableService;

    @Override
    public void run(ApplicationArguments args) {
        tableService.createTable();
    }
}
