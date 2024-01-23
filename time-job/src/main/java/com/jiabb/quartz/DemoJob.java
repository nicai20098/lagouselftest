package com.jiabb.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/21 12:18
 * @since: 1.0
 */
public class DemoJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("我是一个任务执行器...");
    }
}