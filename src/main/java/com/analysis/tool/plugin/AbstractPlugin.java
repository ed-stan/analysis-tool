package com.analysis.tool.plugin;

import java.util.Map;
import java.util.Objects;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:07
 */
public interface AbstractPlugin {
    Map<String, Object>  execute(Map<String, Object> params);
}
