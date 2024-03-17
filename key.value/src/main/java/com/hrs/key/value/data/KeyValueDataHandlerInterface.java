package com.hrs.key.value.data;

import com.hrs.key.value.common.Pair;
import com.hrs.key.value.exception.KeyValueException;

import java.util.*;
import java.util.concurrent.*;
public interface KeyValueDataHandlerInterface
{
    public String add(String key,String value,String fileName) throws KeyValueException;
    public void edit(String key,String value,String fileName) throws KeyValueException;
    public void delete(String key,String fileName) throws KeyValueException;
    public String get(String key,String fileName) throws KeyValueException;
    public ConcurrentMap<String, Pair> populateMap() throws KeyValueException;
}