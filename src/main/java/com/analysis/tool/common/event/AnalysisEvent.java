package com.analysis.tool.common.event;

import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:22
 */
public class AnalysisEvent  extends ApplicationEvent {
    private final Map<String, Object> param;

    public AnalysisEvent(Object source, Map<String, Object> params) {
        super(source);
        this.param = params;
    }

    public Map<String, Object> getParam() {
        return Objects.isNull(param)? new HashMap<>() : param;
    }
}
