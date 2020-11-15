package com.fabriciolfj.github.client.customer.service;

import io.quarkus.scheduler.Scheduled;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@Slf4j
@Getter
@ApplicationScoped
public class TokenGenerator {

    private String token;

    //format s=segundos, m=minutes, h=horas, d=dias
    //@Scheduled(cron="* * * * * ?")
    @Scheduled(every = "5s")
    public void generateToken() {
        token = UUID.randomUUID().toString();
        log.info("New Token generated: {}", token);
    }
}
