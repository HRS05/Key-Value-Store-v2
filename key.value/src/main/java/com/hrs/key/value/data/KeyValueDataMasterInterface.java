package com.hrs.key.value.data;
import com.hrs.key.value.exception.KeyValueException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
public interface KeyValueDataMasterInterface
{
    public void createDataBase(String dataBase) throws KeyValueException;
    public void deleteDataBase(String dataBase) throws KeyValueException;
    public void createTable(String dataBase,String table) throws KeyValueException;
    public void deleteTable(String dataBase,String table) throws KeyValueException;
    public ConcurrentMap<String,ArrayList<String> > populateMasterMap() throws KeyValueException;
}