package com.jiabb.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @description: quartz 示例
 * @author: jia_b
 * @date: 2024/1/21 12:07
 * @since: 1.0
 */
public class QuartzTest {

    // 创建任务调度器
    public static Scheduler createScheduler() throws SchedulerException {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        return stdSchedulerFactory.getScheduler();
    }

    // 创建任务
    public static JobDetail createJob() {
        JobBuilder jobBuilder = JobBuilder.newJob(DemoJob.class);
        jobBuilder.withIdentity("jobName", "myJob");
        return jobBuilder.build();
    }

    // 任务时间触发器 每2秒执行一次
    public static Trigger createTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("triggerName", "myTrigger")
                .startNow().withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ?")).build();
    }

    public static void main(String[] args) throws SchedulerException {
        // 创建任务调度器
        Scheduler scheduler = QuartzTest.createScheduler();
        // 创建任务
        JobDetail job = QuartzTest.createJob();
        // 任务时间触发器
        Trigger trigger = QuartzTest.createTrigger();
        // 使用任务调度器根据时间触发器执行任务
        scheduler.scheduleJob(job, trigger);
        scheduler.start();

    }

}