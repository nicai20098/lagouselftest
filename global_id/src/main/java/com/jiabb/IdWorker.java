package com.jiabb;

import java.sql.SQLData;
import java.sql.Timestamp;

/**
 * @description: 雪花算法案例
 * 官方推出,Scala编程语言来实现的
 * @author: jia_b
 * @date: 2024/1/21 10:53
 * @since: 1.0
 */
public class IdWorker {

    // 两个每个5位, 加起来10位的工作机器id
    private long workerId; //工作id

    private long datacenterId;// 数据id

    //12位序列号
    private long sequence;

    public IdWorker(long workerId, long datacenterId, long sequence) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter id can't be greater than %d or less than 0", maxDatacenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerId %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    // 初始时间戳
    private long twepoch = 1288834974657L;
    //长度为5
    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;
    //最大值
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 序列号id 长度
    private long sequenceBits = 12L;
    // 序列号最大长度
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    // 工作id需要左移的位数, 12位
    private long workerIdShift = sequenceBits;
    // 数据id需要左移位数 12+5 = 17位
    private long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间戳需要左移位数 12+5+5 =22位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdShift;

    // 上次时间戳, 初始值为负数
    private long lastTimestamp = -1L;

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }



    // 下一个ID生成算法
    public synchronized long nextId() {
        long timestamp = timeGen();
        // 获取当前时间戳如果小于上次时间戳, 则表示时间戳获取出现异常
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards. Rejecting request until %d ", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp = timestamp));
        }
        // 获取当前时间戳如果等于上一次时间戳
        // 说明:还处于同一毫秒, 则在序号加一, 否则序列号赋值为0 从0开始
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        // 将上次时间戳刷新
        lastTimestamp = timestamp;

        /**
         * 返回结果
         * (timestamp -twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳, 再左移相应位数
         * (datacenterId << datacenterIdShift)         表示将数据id左移相应位数
         * (workerId << workerIdShift)                 表示将工作id左移相应位数
         *  | 是按位或运算
         * 因为各个部分只有相应位上的值有意义, 其他位上都是0, 所以将各部分的进行|运算就能得到最终拼接好的id
         */
        return ((timestamp -twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;

    }

    // 获取时间戳, 并与上次时间戳比较
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    // 获取系统时间戳
    public long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {

        IdWorker idWorker = new IdWorker(21, 10, 0);
        for (int i = 0; i < 100; i++) {
            System.out.println(idWorker.nextId());
        }

    }


}