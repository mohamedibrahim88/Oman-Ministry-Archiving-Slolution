package com.example.gateway.schedular;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class scheduler {

    @Scheduled(cron = "${cron.expression}")
    public void schedulerJob(){
        System.out.println("job run");
    }
}
