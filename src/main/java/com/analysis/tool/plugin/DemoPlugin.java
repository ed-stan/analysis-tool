package com.analysis.tool.plugin;

import com.alibaba.fastjson2.JSONObject;
import com.analysis.tool.util.ThreadIdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:08
 */
@Component("demoPlugin")
@Slf4j
public class DemoPlugin implements AbstractPlugin {
    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("params", JSONObject.toJSONString(params));
        result.put("event", "demoPlugin");
        log.info("taskId is:{}", ThreadIdGeneratorUtil.getThreadId());
        return result;
    }
}
