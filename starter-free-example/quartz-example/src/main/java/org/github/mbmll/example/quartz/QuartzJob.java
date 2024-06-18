package org.github.mbmll.example.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author xlc
 * @Description
 * @Date 2024/6/19 00:18:36
 */
@Slf4j
public class QuartzJob extends QuartzJobBean {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("start job at time: {}", sdf.format(new Date()));
    }
}
