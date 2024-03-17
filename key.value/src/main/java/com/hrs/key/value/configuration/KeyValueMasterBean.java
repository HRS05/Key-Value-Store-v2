package com.hrs.key.value.configuration;

import com.hrs.key.value.exception.KeyValueException;
import com.hrs.key.value.handler.KeyValueMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class KeyValueMasterBean {
    @Value("${key.value.dataStorePath}")
    private String path;

    @Bean
    public KeyValueMaster getKeyValueMaster() throws KeyValueException {
        return KeyValueMaster.getKeyValueMaster(path);
    }

}
