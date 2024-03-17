package com.hrs.key.value.handler;

import com.hrs.key.value.exception.KeyValueException;

public interface KeyValueMasterInterface
{
    public void createDataBase(String dataBase) throws KeyValueException;
    public void createTable(String dataBase,String table) throws KeyValueException;
    public void deleteDataBase(String dataBase) throws KeyValueException;
    public void deleteTable(String dataBase,String table) throws KeyValueException;
    public KeyValueHandlerInterface getKeyValueHandler(String dataBase,String table) throws KeyValueException;
}