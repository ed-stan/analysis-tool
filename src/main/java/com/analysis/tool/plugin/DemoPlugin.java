package com.analysis.tool.plugin;

import com.analysis.tool.common.constant.CommonConstant;
import com.analysis.tool.util.ThreadIdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:08
 */
@Component("demoPlugin")
@Slf4j
public class DemoPlugin implements AbstractPlugin {

    private static final String TIME_PARAM = "timeParam";
    private static final String LOCATION_PARAM = "locationParam";
    private static final String PARAMS = "pluginParam";
    private static final String EVENT = "event";


    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        log.info("DemoPlugin execute start,taskId is {}", ThreadIdGeneratorUtil.getThreadId());
        Map<String, Object> result = new HashMap<>();
        try {
            if (Objects.isNull(params) || params.isEmpty()) {
                log.error("params is null,taskId is {}", ThreadIdGeneratorUtil.getThreadId());
                throw new RuntimeException("params is null");
            }
            if (!params.containsKey(TIME_PARAM)) {
                log.error("params is not contains key timeParam,taskId is {}", ThreadIdGeneratorUtil.getThreadId());
                throw new RuntimeException("params is not contains key timeParam");
            }
            if (!params.containsKey(LOCATION_PARAM)) {
                log.error("params is not contains key locationParam,taskId is {}", ThreadIdGeneratorUtil.getThreadId());
                throw new RuntimeException("params is not contains key locationParam");
            }
            String time = String.valueOf(params.get(TIME_PARAM));
            String location = String.valueOf(params.get(LOCATION_PARAM));


            result.put(PARAMS, time + CommonConstant.SEPARATOR + location);
            result.put(EVENT, "demoPlugin");
        } catch (Exception e) {
            log.error("DemoPlugin execute error,taskId is {},errorMsg is {}", ThreadIdGeneratorUtil.getThreadId(), e.getMessage());
            throw new RuntimeException("DemoPlugin execute error");
        }
        return result;
    }
}
