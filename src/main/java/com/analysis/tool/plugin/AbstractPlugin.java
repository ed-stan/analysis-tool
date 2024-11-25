package com.analysis.tool.plugin;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:07
 */
@Component
public interface AbstractPlugin {
    Map<String, Object>  execute(Map<String, Object> params);
}
