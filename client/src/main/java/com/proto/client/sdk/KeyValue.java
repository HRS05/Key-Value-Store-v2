package com.proto.client.sdk;
import com.proto.client.exception.KeyValueException;
import com.proto.client.service.KeyValueClientService;

public class KeyValue {
    private static KeyValueClientService keyValueClientService = null;

    public static void setKeyValueClientService(KeyValueClientService kvcs) {
        keyValueClientService = kvcs;
    }

    private String database;
    private String table;

    public KeyValue(String database, String table) {
        this.database = database;
        this.table = table;
    }

    public static void createDatabase(String database) throws KeyValueException {
        keyValueClientService.createDatabase(database);
    }
    public static void createTable(String database, String table) throws KeyValueException {
        keyValueClientService.createTable(database,table);
    }

    public static void deleteDatabase(String database) throws KeyValueException {
        keyValueClientService.deleteDatabase(database);
    }
    public static void deleteTable(String database, String table) throws KeyValueException {
        keyValueClientService.deleteTable(database,table);
    }

    public void set(String key, String value) throws KeyValueException {
        keyValueClientService.setKey(this.database, this.table, key, value);
    }
    public void delete(String key) throws KeyValueException {
        keyValueClientService.deleteKey(this.database, this.table, key);
    }
    public String get(String key) throws KeyValueException {
        return keyValueClientService.getKey(this.database, this.table, key);
    }
}
