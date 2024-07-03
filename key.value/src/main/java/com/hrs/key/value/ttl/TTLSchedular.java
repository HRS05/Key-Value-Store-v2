package com.hrs.key.value.ttl;

import com.hrs.key.value.common.Pair;
import com.hrs.key.value.exception.KeyValueException;
import com.hrs.key.value.handler.KeyValueHandler;
import com.hrs.key.value.handler.KeyValueMaster;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class TTLSchedular {

    @Autowired
    private KeyValueMaster keyValueMaster;

    private final ExecutorService scheduler = Executors.newVirtualThreadPerTaskExecutor();
    private static final long PERIOD_MS = 1 * 60 * 1000;

    @PostConstruct
    public void init() {
        log.info("TTL scheduler started");
        startScheduler();
    }

    public void startScheduler() {
        scheduler.submit(() -> {
            Thread.currentThread().setName("ttl-scheduler-thread");
            while (!Thread.currentThread().isInterrupted()) {
                schedule();
                try {
                    Thread.sleep(PERIOD_MS);
                } catch (InterruptedException e) {
                    log.error("TTL scheduler error {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void schedule() {
        try {
            ConcurrentMap<String, ConcurrentMap<String, KeyValueHandler>> masterMap = keyValueMaster.getKeyValueHandlerMasterMap();
            masterMap.entrySet().stream()
                    .forEach(dbDataMapEntry -> {
                        log.info("TTL scheduler checking in db: {}", dbDataMapEntry.getKey());
                        ConcurrentMap<String, KeyValueHandler> tableHandlerMap = dbDataMapEntry.getValue();
                        tableHandlerMap.entrySet().stream()
                                .forEach(handlerEntry -> {
                                    log.info("TTL scheduler checking in table: {}", handlerEntry.getKey());
                                    KeyValueHandler kvh = handlerEntry.getValue();
                                    ConcurrentMap<String, Pair> ttlMap = kvh.getTtlMap();
                                    ttlMap.entrySet().stream()
                                            .forEach(entry -> {
                                                Pair p = entry.getValue();
                                                Long currentTimeMillis = System.currentTimeMillis();
                                                if (p.getTtl() != null && p.getTtl() < currentTimeMillis) {
                                                    try {
                                                        kvh.remove(entry.getKey());
                                                    } catch (KeyValueException e) {
                                                        log.error("TTL scheduler error {}", e.getMessage());
                                                    }
                                                }
                                            });
                                });
                    });
        } catch (KeyValueException e) {
            log.error("TTL scheduler error {}", e.getMessage());
        }
    }

    public void stopScheduler() {
        scheduler.shutdownNow();
    }
}
