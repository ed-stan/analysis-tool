package com.analysis.tool.plugin;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:08
 */
@Component("demoPlugin")
public class DemoPlugin implements AbstractPlugin{
    @Override
    public Map<String, Object>  execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("params", params);

        result.put("result", "demoPlugin");
        return result;
    }
}
