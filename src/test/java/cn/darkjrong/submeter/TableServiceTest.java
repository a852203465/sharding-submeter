package cn.darkjrong.submeter;

import cn.darkjrong.submeter.service.TableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableServiceTest extends ShardingSubmeterApplicationTest{

    @Autowired
    private TableService tableService;

    @Test
    void createTable() {

        tableService.createTable();

    }

}
