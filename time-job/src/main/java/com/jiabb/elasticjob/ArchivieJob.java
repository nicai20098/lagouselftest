package com.jiabb.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @description: 定时任务业务逻辑处理类
 * @author: jia_b
 * @date: 2024/1/21 12:51
 * @since: 1.0
 */
public class ArchivieJob implements SimpleJob {
    /**
     * 业务逻辑(execute方法每次定时任务执行都会执行一次)
     */
    @Override
    public void execute(ShardingContext shardingContext) {

        System.out.println("执行定时任务...");

    }
}