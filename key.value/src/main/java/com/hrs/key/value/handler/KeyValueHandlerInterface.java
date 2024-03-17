package com.hrs.key.value.handler;

import com.hrs.key.value.exception.KeyValueException;
public interface KeyValueHandlerInterface
{
    public void set(String key,String value) throws KeyValueException;
    public void remove(String key) throws KeyValueException;
    public String get(String key) throws KeyValueException;
}