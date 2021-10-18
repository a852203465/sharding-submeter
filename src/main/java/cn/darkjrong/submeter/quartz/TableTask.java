package cn.darkjrong.submeter.quartz;

import cn.darkjrong.submeter.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 建表的任务
 *
 * @author Rong.Jia
 * @date 2021/10/14
 */
@Slf4j
public class TableTask extends QuartzJobBean {

    @Autowired
    private TableService tableService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        tableService.createTable();
    }
}
