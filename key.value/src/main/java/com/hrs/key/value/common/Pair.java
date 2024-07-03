package com.hrs.key.value.common;
public class Pair
{
    private String key;
    private String value;

    private Long ttl;
    public Pair(String key,String value)
    {
        this.key=key;
        this.value=value;
        this.ttl=null;
    }

    public Pair(String key,String value, Long ttl)
    {
        this.key=key;
        this.value=value;
        this.ttl=ttl;
    }
    public String getKey()
    {
        return this.key;
    }
    public String getValue()
    {
        return this.value;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public void setKey(String key)
    {
        this.key=key;
    }
    public void setValue(String value)
    {
        this.value=value;
    }

    public boolean isTtlKey() {
        return this.ttl != null;
    }
}