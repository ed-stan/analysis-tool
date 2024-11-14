package com.analysis.tool.controller;

import com.analysis.tool.common.event.AnalysisEvent;
import com.analysis.tool.service.EventPublisherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:52
 */
@RestController
@RequestMapping("/analysis/tool")
public class TestController {

    @Resource
    private EventPublisherService eventPublisherService;

    @RequestMapping("/execute")
    public String execute(@RequestBody Map<String, Object> params){

        System.out.println(params);
        eventPublisherService.publishEvent(new AnalysisEvent(this, params));
        return "hello world";
    }
}
