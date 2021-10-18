package cn.darkjrong.submeter.common.timer;

import cn.darkjrong.submeter.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class TimingTask {

    @Autowired
    private TableService tableService;

    @Scheduled(cron = "0 21,40,55,58 23 28,29,30,31 * ? ")
    public void createTable() {
        tableService.createTable();
    }


}
