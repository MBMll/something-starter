package org.github.mbmll.example.quartz.configuration;

import org.github.mbmll.example.quartz.QuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean quartzJobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(QuartzJob.class);
        factoryBean.setName("quartzJobDetail");
        factoryBean.setGroup("group1");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean() {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(quartzJobDetailFactoryBean().getObject());
        factoryBean.setName("simpleTriggerFactoryBean");
        factoryBean.setGroup("group1");
        factoryBean.setRepeatInterval(5000);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);

        return factoryBean;
}
    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(QuartzJob.class).withIdentity("quartzJobDetail").storeDurably().build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withIdentity("trigger1")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
