package com.hrs.key.value.common;
public class OperationObject {
    private String type;
    private String key;
    private String fileName;
    private String value;
    private long timestampValue;
    public void setType(String type)
    {
        this.type=type;
    }
    public String getType()
    {
        return this.type;
    }
    public void setKey(String key)
    {
        this.key=key;
    }
    public String getKey()
    {
        return this.key;
    }
    public void setFileName(String fileName)
    {
        this.fileName=fileName;
    }
    public String getFileName()
    {
        return this.fileName;
    }
    public void setValue(String value)
    {
        this.value=value;
    }
    public String getValue()
    {
        return this.value;
    }
    public void setTimestampValue(long timestampValue)
    {
        this.timestampValue=timestampValue;
    }
    public long getTimestampValue()
    {
        return this.timestampValue;
    }
}