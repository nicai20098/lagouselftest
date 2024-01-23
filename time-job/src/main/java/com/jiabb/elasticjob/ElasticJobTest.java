package com.jiabb.elasticjob;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.jute.compiler.JBoolean;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/21 13:08
 * @since: 1.0
 */
public class ElasticJobTest {

    public static void main(String[] args) {

        // 配置分布式协调服务
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration("localhost:2181","elastic-client");
        ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        zookeeperRegistryCenter.init();
        // 配置任务
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder("archive-job", "*/2 * * * * ?", 1).build();
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, ArchivieJob.class.getName());

        JobScheduler jobScheduler = new JobScheduler(zookeeperRegistryCenter, LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build());
        jobScheduler.init();


    }


}