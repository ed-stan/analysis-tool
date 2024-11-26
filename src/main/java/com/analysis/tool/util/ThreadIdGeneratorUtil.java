package com.analysis.tool.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author DingYulong
 * @Date 2024/11/26 11:37
 */
public class ThreadIdGeneratorUtil {
    private static final ThreadLocal<Long> THREAD_LOCAL_ID = new ThreadLocal<>();

    public static long getThreadId() {
        Long id = THREAD_LOCAL_ID.get();
        if (id == null) {
            id = generateThreadId();
            THREAD_LOCAL_ID.set(id);
        }
        return id;
    }

    private static long generateThreadId() {
        return SnowflakeUtil.getId();
    }
}
