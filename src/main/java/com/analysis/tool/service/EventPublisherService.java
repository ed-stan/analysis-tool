package com.analysis.tool.service;

import com.analysis.tool.common.event.AnalysisEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public EventPublisherService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }


    public void publishEvent(AnalysisEvent event) {
        publisher.publishEvent(event);
    }

    @Async
    public void publishEventSync(AnalysisEvent event) {
        publishEvent(event);
    }
}