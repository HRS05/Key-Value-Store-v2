package com.proto.client.service;

import com.key.value.grpc.*;
import com.proto.client.exception.KeyValueException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class KeyValueClientService {

    @GrpcClient("grpc-service")
    KeyValueServiceGrpc.KeyValueServiceBlockingStub synchronousClient;

    public void createDatabase(String database) throws KeyValueException {
        SuccessDetail sd = SuccessDetail.newBuilder().build();
        try {
            CreateDatabase createDatabase = CreateDatabase.newBuilder().setDatabase(database).build();
            sd = synchronousClient.createDatabase(createDatabase);
        } catch (StatusRuntimeException sre) {
            System.out.println(sre.getMessage());
            throw new KeyValueException(sre.getMessage());
        }
    }

    public void createTable(String database, String table) throws KeyValueException  {
        SuccessDetail sd = SuccessDetail.newBuilder().build();
        try {
            CreateTable createTable = CreateTable.newBuilder().setDatabase(database).setTable(table).build();
            sd = synchronousClient.createTable(createTable);
        } catch (StatusRuntimeException sre) {
            System.out.println(sre.getMessage());
            throw new KeyValueException(sre.getMessage());
        }
    }


    public void setKey(String database, String table, String key, String value) throws KeyValueException  {
        SuccessDetail sd = SuccessDetail.newBuilder().build();
        try {
            SetOrUpdateKey setOrUpdateKey = SetOrUpdateKey.newBuilder()
                    .setDatabase(database)
                    .setTable(table)
                    .setKey(key)
                    .setValue(value)
                    .build();
            sd = synchronousClient.setOrUpdateKey(setOrUpdateKey);
        } catch (StatusRuntimeException sre) {
            System.out.println(sre.getMessage());
            throw new KeyValueException(sre.getMessage());
        }
    }

    public String getKey(String database, String table, String key) throws KeyValueException  {
        SuccessDetail sd;
        String value = null;
        try {
            GetOrDeleteKey getOrDeleteKey = GetOrDeleteKey.newBuilder()
                    .setDatabase(database)
                    .setTable(table)
                    .setKey(key)
                    .build();
            sd = synchronousClient.getKey(getOrDeleteKey);
            value = sd.getData();
        } catch (StatusRuntimeException sre) {
            System.out.println(sre.getMessage());
            throw new KeyValueException(sre.getMessage());
        }
        return value;
    }

    public void deleteKey(String database, String table, String key) throws KeyValueException  {
        SuccessDetail sd;
        try {
            GetOrDeleteKey getOrDeleteKey = GetOrDeleteKey.newBuilder()
                    .setDatabase(database)
                    .setTable(table)
                    .setKey(key)
                    .build();
            sd = synchronousClient.deleteKey(getOrDeleteKey);
        } catch (StatusRuntimeException sre) {
            System.out.println(sre.getMessage());
            throw new KeyValueException(sre.getMessage());
        }
    }

}
