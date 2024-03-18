package com.hrs.key.value.handler;

import com.hrs.key.value.data.KeyValueDataMaster;
import com.hrs.key.value.exception.KeyValueException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class KeyValueMaster implements KeyValueMasterInterface {

    private static KeyValueMaster keyValueMaster = null;
    private String path;
    private ConcurrentMap<String, ArrayList<String>> masterMap = null;
    private ConcurrentMap<String, ConcurrentMap<String, KeyValueHandler>> master = null;

    private KeyValueMaster(String path) throws KeyValueException {
        //check for path will add
        this.path = path;
        try {
            this.masterMap = new KeyValueDataMaster(this.path).populateMasterMap();
        } catch (KeyValueException exception) {
            System.out.println("Error : issue in populating master map --> " + exception.getMessage());
            throw new KeyValueException("Error : issue in populating master map --> " + exception.getMessage());
        }
        this.populateMaster();
    }

    public static KeyValueMaster getKeyValueMaster(String path) throws KeyValueException {
        if (keyValueMaster != null) return keyValueMaster;
        try {
            keyValueMaster = new KeyValueMaster(path);
        } catch (KeyValueException exception) {
            throw new KeyValueException("Error in populating the master ----> " + exception.getMessage());
        }

        return keyValueMaster;
    }

    private void populateMaster() throws KeyValueException {
        this.master = new ConcurrentHashMap<String, ConcurrentMap<String, KeyValueHandler>>();
        if (this.masterMap == null) return;

        Iterator<ConcurrentHashMap.Entry<String, ArrayList<String>>> masterMapIterator = this.masterMap.entrySet().iterator();
        while (masterMapIterator.hasNext()) {
            ConcurrentHashMap.Entry<String, ArrayList<String>> entry = masterMapIterator.next();
            String dataBaseName = entry.getKey();

            ConcurrentMap<String, KeyValueHandler> obj = new ConcurrentHashMap<String, KeyValueHandler>();
            for (String tableName : entry.getValue()) {
                try {
                    KeyValueHandler keyValueHandler = new KeyValueHandler(this.path + dataBaseName + File.separator + tableName + File.separator);
                    obj.put(tableName, keyValueHandler);
                } catch (KeyValueException exception) {
                    throw new KeyValueException("Error in populating master for " + this.path + dataBaseName + File.separator + tableName + " --> " + exception.getMessage());
                }
            }
            this.master.put(dataBaseName, obj);
        }

    }

    @Override
    public void createDataBase(String dataBase) throws KeyValueException {
        //check for path will add
        if(dataBase==null || dataBase.trim().length()==0) throw new KeyValueException("error : from createDataBase the data base name is empty/null");
        dataBase=dataBase.trim();
        try
        {
            new KeyValueDataMaster(path).createDataBase(dataBase);
            this.masterMap.put(dataBase,new ArrayList<String>());
            this.master.put(dataBase,new ConcurrentHashMap<String,KeyValueHandler>());
        }catch(KeyValueException exception)
        {
            log.error("error : in creating database --> {}", exception.getMessage());
            throw new KeyValueException("error : in creating database --> "+exception.getMessage());
        }
    }

    @Override
    public void createTable(String dataBase, String table) throws KeyValueException {
        if (dataBase == null || dataBase.trim().length() == 0)
            throw new KeyValueException("error : from createTable the data base name is empty/null");
        if (table == null || table.trim().length() == 0)
            throw new KeyValueException("error : from createTable the table name is empty/null");
        dataBase = dataBase.trim();
        table = table.trim();
        if (!this.master.containsKey(dataBase))
            throw new KeyValueException("error : from createTable the data base " + dataBase + " not exists to create table " + table + ".");
        ConcurrentMap<String, KeyValueHandler> obj = this.master.get(dataBase);
        if (obj.containsKey(table))
            throw new KeyValueException("error : from createTable the table " + table + " already exists.");
        try {
            new KeyValueDataMaster(path).createTable(dataBase, table);
            System.out.println("dataBase " + dataBase + "   " + "table  " + table);
            this.masterMap.get(dataBase).add(table);
            KeyValueHandler keyValueHandler = new KeyValueHandler(this.path + dataBase + File.separator + table + File.separator);
            this.master.get(dataBase).put(table, keyValueHandler);
        } catch (KeyValueException exception) {
            throw new KeyValueException("error : in creating database --> " + exception.getMessage());
        }
    }

    @Override
    public void deleteDataBase(String dataBase) throws KeyValueException {
        if (dataBase == null || dataBase.trim().length() == 0)
            throw new KeyValueException("error : from deleteDataBase the data base name is empty/null");
        if (!this.master.containsKey(dataBase))
            throw new KeyValueException("error : from deleteDataBase the data base " + dataBase + " not exists.");
        dataBase = dataBase.trim();
        try {
            this.master.remove(dataBase);
            this.masterMap.remove(dataBase);
            new KeyValueDataMaster(path).deleteDataBase(dataBase);
        } catch (KeyValueException exception) {
            throw new KeyValueException("error : in creating database --> " + exception.getMessage());
        }
    }

    @Override
    public void deleteTable(String dataBase, String table) throws KeyValueException {
        if (dataBase == null || dataBase.trim().length() == 0)
            throw new KeyValueException("error : from deleteTable the data base name is empty/null");
        if (table == null || table.trim().length() == 0)
            throw new KeyValueException("error : from deleteTable the table name is empty/null");
        dataBase = dataBase.trim();
        table = table.trim();
        if (!this.master.containsKey(dataBase))
            throw new KeyValueException("error : from deleteTable the data base " + dataBase + " not exists to delete table " + table + ".");
        ConcurrentMap<String, KeyValueHandler> obj = this.master.get(dataBase);
        if (!obj.containsKey(table))
            throw new KeyValueException("error : from deleteTable the table " + table + " not exists.");
        try {
            this.masterMap.get(dataBase).remove(table);
            this.master.get(dataBase).remove(table);
            new KeyValueDataMaster(path).deleteTable(dataBase, table);

        } catch (KeyValueException exception) {
            throw new KeyValueException("error : in creating database --> " + exception.getMessage());
        }
    }

    @Override
    public KeyValueHandlerInterface getKeyValueHandler(String dataBase, String table) throws KeyValueException {
        if (!this.masterMap.containsKey(dataBase))
            throw new KeyValueException("master Map does not contain dataBase " + dataBase);
        if (!this.master.containsKey(dataBase))
            throw new KeyValueException("master Map does not contain dataBase " + dataBase);
        if (!this.master.get(dataBase).containsKey(table))
            throw new KeyValueException("master Map does not contain table " + table + " in database " + dataBase);
        return this.master.get(dataBase).get(table);
    }
}
