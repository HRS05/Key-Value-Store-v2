package com.hrs.key.value.service;

import com.hrs.key.value.exception.KeyValueException;
import com.hrs.key.value.handler.KeyValueHandlerInterface;
import com.hrs.key.value.handler.KeyValueMaster;
import com.key.value.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
public class KeyValueService extends KeyValueServiceGrpc.KeyValueServiceImplBase {

    @Autowired
    private KeyValueMaster keyValueMaster;

    @Override
    public void createDatabase(CreateOrDeleteDatabase request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        try {
            keyValueMaster.createDataBase(database);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setMessage("Database created successfully").build();
        responseObserver.onNext(successDetail);
        responseObserver.onCompleted();
    }

    @Override
    public void createTable(CreateOrDeleteTable request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        String table = request.getTable();

        try {
            keyValueMaster.createTable(database, table);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setMessage("Table created successfully in database "+database).build();
        responseObserver.onNext(successDetail);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteDatabase(CreateOrDeleteDatabase request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        try {
            keyValueMaster.deleteDataBase(database);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setMessage("Database deleted successfully").build();
        responseObserver.onNext(successDetail);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTable(CreateOrDeleteTable request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        String table = request.getTable();

        try {
            keyValueMaster.deleteTable(database, table);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setMessage("Table deleted successfully in database "+database).build();
        responseObserver.onNext(successDetail);
        responseObserver.onCompleted();
    }

    @Override
    public void setOrUpdateKey(SetOrUpdateKey request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        String table = request.getTable();
        String key = request.getKey();
        String value = request.getValue();
        Long ttl = request.getTtl();
        try {
            KeyValueHandlerInterface kvh = keyValueMaster.getKeyValueHandler(database, table);
            if (ttl == 0L) ttl = null;
            kvh.set(key, value, ttl);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setMessage("Value added successfully").build();
        responseObserver.onNext(successDetail);
        responseObserver.onCompleted();
    }

    @Override
    public void getKey(GetOrDeleteKey request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        String table = request.getTable();
        String key = request.getKey();
        String value = null;
        try {
            KeyValueHandlerInterface kvh = keyValueMaster.getKeyValueHandler(database, table);
            value = kvh.get(key);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
            return;
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setData(value).build();
        responseObserver.onNext(successDetail);

        responseObserver.onCompleted();
    }

    @Override
    public void deleteKey(GetOrDeleteKey request, StreamObserver<SuccessDetail> responseObserver) {
        String database = request.getDatabase();
        String table = request.getTable();
        String key = request.getKey();
        String value = null;
        try {
            KeyValueHandlerInterface kvh = keyValueMaster.getKeyValueHandler(database, table);
            kvh.remove(key);
        } catch (KeyValueException e) {
            log.error("Error cause at grpc service -> {}"+ e.getMessage());
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
        SuccessDetail successDetail = SuccessDetail.newBuilder().setMessage("Key removed successfully").build();
        responseObserver.onNext(successDetail);
        responseObserver.onCompleted();
    }
}
