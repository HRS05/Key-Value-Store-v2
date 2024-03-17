package com.hrs.key.value.data;

import com.hrs.key.value.exception.KeyValueException;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KeyValueDataMaster implements KeyValueDataMasterInterface {

    private final String path;

    public KeyValueDataMaster(String path) {
        this.path = path;
    }

    private void haveSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    private ArrayList<String> getListOfTabel(String location) {
        ArrayList<String> list = new ArrayList<>();
        File tableFile = new File(location);
        for (File table : tableFile.listFiles()) {
            if (table.isDirectory()) list.add(table.getName());
        }
        return list;
    }

    private void deleteDirectory(File directory) // used to delete contents of given directory completely
    {
        for (File subFile : directory.listFiles()) {
            if (subFile.isDirectory()) deleteDirectory(subFile);
            subFile.delete();
        }
    }

    @Override
    public void createDataBase(String dataBase) throws KeyValueException {
        if (dataBase == null) throw new KeyValueException("error : database name empty");
        if (dataBase.length() == 0) throw new KeyValueException("error : database name empty");
        File folderDataBase = new File(this.path + File.separator + dataBase);
        if (folderDataBase.exists())
            throw new KeyValueException("error : database (" + dataBase + ") already exist");
        folderDataBase.mkdirs();
        haveSleep(10);
    }

    @Override
    public void deleteDataBase(String dataBase) throws KeyValueException {
        if (dataBase == null || dataBase.length() == 0) throw new KeyValueException("error : dataBase name empty");
        File folderDataBase = new File(this.path + File.separator + dataBase);
        if (!folderDataBase.exists())
            throw new KeyValueException("error: dataBase(" + dataBase + ") does not exists");
        this.deleteDirectory(folderDataBase);
        folderDataBase.delete();
        haveSleep(10);
    }

    @Override
    public void createTable(String dataBase, String table) throws KeyValueException {
        if (dataBase == null && table == null)
            throw new KeyValueException("error: Names of dataBase and table are empty");
        else if (dataBase == null || dataBase.length() == 0) throw new KeyValueException("error: dataBase name empty");
        else if (table == null || table.length() == 0) throw new KeyValueException("error : table name empty");

        File folderDataBase = new File(this.path + File.separator + dataBase);

        if (!folderDataBase.exists())
            throw new KeyValueException("error: dataBase (" + dataBase + ") not exits can not make table (" + table + ")");

        File folderTable = new File(this.path + File.separator + dataBase + File.separator + table);
        if (folderTable.exists()) throw new KeyValueException("error: table (" + table + ") already exists");
        folderTable.mkdirs();
        haveSleep(10);
    }

    @Override
    public void deleteTable(String dataBase, String table) throws KeyValueException {
        if (dataBase == null && table == null)
            throw new KeyValueException("error: Names of dataBase and table are empty");
        else if (dataBase == null || dataBase.length() == 0) throw new KeyValueException("error: dataBase name empty");
        else if (table == null || table.length() == 0) throw new KeyValueException("error : table name empty");

        File folderDataBase = new File(this.path + File.separator + dataBase);

        if (!folderDataBase.exists())
            throw new KeyValueException("error: dataBase (" + dataBase + ") not exits can not delete table (" + table + ")");

        File folderTable = new File(this.path + File.separator + dataBase + File.separator + table);
        if (!folderTable.exists())
            throw new KeyValueException("error: table (" + table + ")does not exists, can not delete table");
        this.deleteDirectory(folderTable);
        folderTable.delete();
        haveSleep(10);
    }

    @Override
    public ConcurrentMap<String, ArrayList<String>> populateMasterMap() throws KeyValueException {
        ConcurrentMap<String, ArrayList<String>> masterMap = new ConcurrentHashMap<>();
        File directoryFile = new File(this.path);

        for (File dataBase : directoryFile.listFiles()) {
            if (dataBase.isDirectory())
                masterMap.put(dataBase.getName(), getListOfTabel(this.path + File.separator + dataBase.getName()));
        }
        return masterMap;
    }
}
