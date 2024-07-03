package com.hrs.key.value.handler;

import com.hrs.key.value.common.OperationObject;
import com.hrs.key.value.common.Pair;
import com.hrs.key.value.data.KeyValueDataHandler;
import com.hrs.key.value.exception.KeyValueException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
public class KeyValueHandler implements KeyValueHandlerInterface {

    private final String directory;
    Comparator<OperationObject> comparator = Comparator.comparingLong(OperationObject::getTimestampValue);
    private String fileName;
    private ConcurrentMap<String, Pair> keyValueMap = null;
    private ConcurrentMap<String, Pair> ttlMap = null;
    private PriorityBlockingQueue<OperationObject> queue = null;


    public KeyValueHandler(String directory) throws KeyValueException {
        if (directory == null || directory.trim().length() == 0)
            throw new KeyValueException("directory is null/size=0");
        this.fileName = null;
        this.directory = directory;
        this.queue = new PriorityBlockingQueue<OperationObject>(1000000, comparator);
//        this.setDaemon(true);
//        start();
        Thread.ofVirtual().start(this::run);
        try {
            this.keyValueMap = new KeyValueDataHandler(this.directory).populateMap();
            this.initTTLMap();
        } catch (KeyValueException exception) {
            log.error("Error : issue in populating key value map for {}", this.directory, exception.getMessage());
            throw new KeyValueException("Error : issue in populating key value map for " + this.directory + " --> " + exception.getMessage());
        }
    }

    private void initTTLMap() {
        this.ttlMap = new ConcurrentHashMap<>();
        this.keyValueMap.entrySet().stream().forEach(entry -> {
            if (entry.getValue().isTtlKey()) {
                this.ttlMap.put(entry.getKey(), entry.getValue());
            }
        });
    }


    @Override
    public void set(String key, String value, Long ttl) throws KeyValueException {
        log.info("KeyValueHandler set operation got called -> key : " + key + " value : " + value);
        if (key == null || key.trim().length() == 0) throw new KeyValueException("Key data is null/size=0");
        if (value == null || value.trim().length() == 0) throw new KeyValueException("value data is null/size=0");
        key = key.trim();
        value = value.trim();
        if (this.keyValueMap.containsKey(key)) {
            Pair pair = this.keyValueMap.get(key);
            String oldFileName = pair.getKey();
            this.keyValueMap.replace(key, pair, new Pair(oldFileName, value, ttl));
            OperationObject OJ = new OperationObject();
            OJ.setFileName(oldFileName);
            OJ.setType("edit");
            OJ.setKey(key);
            OJ.setValue(value);
            OJ.setTtl(ttl);
            OJ.setTimestampValue(new Timestamp(System.currentTimeMillis()).getTime());
            this.queue.add(OJ);
        } else {
            this.keyValueMap.put(key, new Pair(null, value, ttl));
            OperationObject OJ = new OperationObject();
            OJ.setFileName(this.fileName);
            OJ.setType("add");
            OJ.setKey(key);
            OJ.setValue(value);
            OJ.setTtl(ttl);
            OJ.setTimestampValue(new Timestamp(System.currentTimeMillis()).getTime());
            this.queue.add(OJ);
        }

    }

    @Override
    public void remove(String key) throws KeyValueException {
        log.info("KeyValueHandler remove operation got called -> key : " + key);
        if (key == null || key.trim().length() == 0) throw new KeyValueException("Key data is null/size=0");
        key = key.trim();
        if (this.keyValueMap.containsKey(key)) {
            Pair pair = this.keyValueMap.get(key);
            String oldFileName = pair.getKey();
            String value = pair.getValue();
            this.keyValueMap.remove(key, pair);
            OperationObject OJ = new OperationObject();
            OJ.setFileName(oldFileName);
            OJ.setType("remove");
            OJ.setKey(key);
            OJ.setValue(value);
            OJ.setTimestampValue(new Timestamp(System.currentTimeMillis()).getTime());
            this.queue.add(OJ);
        } else {
            throw new KeyValueException("Key is not in store");
        }
    }

    @Override
    public String get(String key) throws KeyValueException {
        log.info("KeyValueHandler get operation got called -> key : " + key);
        if (this.keyValueMap.containsKey(key)) {
            Pair p = this.keyValueMap.get(key);
            Long currentTimeMillis = System.currentTimeMillis();
            if (p.getTtl() != null && p.getTtl() < currentTimeMillis) {
                this.remove(key);
                throw new KeyValueException("Key is not in store");
            }
            if (p.getValue() == null)
                throw new KeyValueException("Key is not in store");
            return p.getValue();
        } else {
            throw new KeyValueException("Key is not in store");
        }
    }

    public ConcurrentMap<String, Pair> getTtlMap() {
        return this.ttlMap;
    }

    public void run() {
        String threadName = "handler-thread";
        Thread.currentThread().setName(threadName);
        while (true) {
//            System.out.println("queue on call for table path -> " + this.directory);
            while (!queue.isEmpty()) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }

                OperationObject OJ = this.queue.peek();
                this.queue.remove();
                String type = OJ.getType();
                String fileName = OJ.getFileName();
                String key = OJ.getKey();
                String value = OJ.getValue();
                Long ttl = OJ.getTtl();

                if (type == "add") {
                    try {
                        //System.out.println("queue KeyValueHandler add operation got called -> key : " + key + " value : " + value + " fileName " + this.fileName);
                        this.fileName = new KeyValueDataHandler(this.directory).add(key, value, this.fileName, ttl);
                        //String CurrentValue = this.keyValueMap.get(key).getValue();
                        //this.keyValueMap.replace(key, new Pair(null, CurrentValue), new Pair(this.fileName, CurrentValue));
                        this.keyValueMap.put(key, new Pair(this.fileName, value, ttl));
                        if (ttl != null) this.ttlMap.put(key, new Pair(this.fileName, value, ttl));
                    } catch (Exception exception) {
                        this.queue.add(OJ);
                        log.error("Exception from thread (add operation) :-> " + exception.getMessage());
                    }
                } else if (type == "edit") {
                    try {
                        Pair pair;
                        if (fileName == null || fileName.length() == 0) {
                            pair = this.keyValueMap.get(key);
                            if (pair == null || pair.getKey() == null || pair.getKey().length() == 0) {
                                this.queue.add(OJ);
                                continue;
                            }
                            fileName = pair.getKey();
                            OJ.setFileName(fileName);
                        }
                        new KeyValueDataHandler(this.directory).edit(key, value, fileName, ttl);
                        if (ttl != null) this.ttlMap.put(key, new Pair(this.fileName, value, ttl));
                        if (this.ttlMap.containsKey(key) && ttl == null) this.ttlMap.remove(key);
                    } catch (Exception exception) {
                        this.queue.add(OJ);
                        log.error("Exception from thread (update operation) :-> " + exception.getMessage());
                    }
                } else if (type == "remove") {
                    try {
                        Pair pair;
                        if (fileName == null || fileName.length() == 0) {
                            pair = this.keyValueMap.get(key);
                            if (pair == null || pair.getKey() == null || pair.getKey().length() == 0) {
                                this.queue.add(OJ);
                                continue;
                            }
                            fileName = pair.getKey();
                            OJ.setFileName(fileName);
                        }
                        log.info("queue KeyValueHandler remove operation got called -> key : " + key + " value : " + value + " fileName " + fileName);
                        new KeyValueDataHandler(this.directory).delete(key, fileName);
                        if (this.ttlMap.containsKey(key) && ttl == null) this.ttlMap.remove(key);
                    } catch (Exception exception) {
                        this.queue.add(OJ);
                        log.error("Exception from thread (delete operation) :-> " + exception.getMessage());
                    }
                } else {
                    log.error("Invalid data in queue");
                }
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }
    }

}
