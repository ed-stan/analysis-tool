package com.analysis.tool.util;

public class SnowflakeUtil {

    // 时间戳起始偏移量
    private final static long TW_EPOCH = 1577836800000L; // 2020-01-01 00:00:00 UTC

    // 每一部分所占的位数
    private final static long WORKER_ID_BITS = 5L;
    private final static long DATA_CENTER_ID_BITS = 5L;
    private final static long SEQUENCE_BITS = 12L;

    // 每一部分的最大值
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private final static long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 每一部分向左移的位数
    private final static long WORKER_ID_LEFT_SHIFT = SEQUENCE_BITS;
    private final static long DATA_CENTER_ID_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    // 工作机器 ID
    private long workerId;
    // 数据中心 ID
    private long dataCenterId;
    // 序列号
    private long sequence = 0L;

    // 上次生成 ID 的时间戳
    private long lastTimestamp = -1L;

    public SnowflakeUtil(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("data center Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT) |
                (dataCenterId << DATA_CENTER_ID_LEFT_SHIFT) |
                (workerId << WORKER_ID_LEFT_SHIFT) |
                sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static long getId() {
        SnowflakeUtil snowflake = new SnowflakeUtil(1, 1);
        return snowflake.nextId();
    }
}