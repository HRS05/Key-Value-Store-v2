package com.hrs.key.value.handler;

import com.hrs.key.value.common.OperationObject;
import com.hrs.key.value.exception.KeyValueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.concurrent.*;

public class TaskExecutorHandler {

    @Value("${key.value.thread.poolSize}")
    private Integer threadPoolSize;
    private PriorityBlockingQueue<OperationObject> queue = null;
    private ExecutorService executorService = null;
    private static TaskExecutorHandler taskExecutorHandler = null;
    private ConcurrentMap<String, String> operationBlockingCheck = null;
    Comparator<OperationObject> comparator = Comparator.comparingLong(OperationObject::getTimestampValue);


    private TaskExecutorHandler() throws KeyValueException {
        if (threadPoolSize == null || threadPoolSize == 0)  threadPoolSize = 50;
        this.operationBlockingCheck = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.queue = new PriorityBlockingQueue<OperationObject>(10000000, comparator);
        this.execute();
    }

    public static TaskExecutorHandler getTaskExecutorHandler() throws KeyValueException {
        if (taskExecutorHandler != null) return taskExecutorHandler;
        taskExecutorHandler = new TaskExecutorHandler();
        return taskExecutorHandler;
    }

    public void addTask(OperationObject oo) throws KeyValueException {
        if (oo == null) {
            throw new KeyValueException("Operation object can not be null");
        }
        this.queue.add(oo);
    }

    public void execute() {
        if (this.executorService == null) {
            return;
        }
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {

                }
            }
        });
    }

}
