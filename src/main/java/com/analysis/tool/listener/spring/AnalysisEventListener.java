package com.analysis.tool.listener.spring;

import com.analysis.tool.common.event.AnalysisEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:24
 */
@Component
public class AnalysisEventListener implements ApplicationListener<AnalysisEvent> {

    @Override
    public void onApplicationEvent(AnalysisEvent event) {
        System.out.println("Received event: " + event.getParam());
        //读取执行规则

        //


    }

}
