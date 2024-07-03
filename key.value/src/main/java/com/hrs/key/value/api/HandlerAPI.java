package com.hrs.key.value.api;

import com.hrs.key.value.dto.KeyValueDTO;
import com.hrs.key.value.dto.ResponseDTO;
import com.hrs.key.value.exception.KeyValueException;
import com.hrs.key.value.handler.KeyValueMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hrs.key.value.handler.KeyValueHandlerInterface;

@RestController
@RequestMapping("/handler")
public class HandlerAPI {

    @Autowired
    private KeyValueMaster keyValueMaster;

    @PostMapping("/{database}/{table}")
    public ResponseEntity<?> setKey(@PathVariable("database") String database, @PathVariable("table") String table, @RequestBody KeyValueDTO keyValueDTO) throws KeyValueException {
        KeyValueHandlerInterface kvh = keyValueMaster.getKeyValueHandler(database, table);
        kvh.set(keyValueDTO.getKey(), keyValueDTO.getValue(), keyValueDTO.getTtl());
        ResponseDTO rs = new ResponseDTO();
        rs.setMessage("Value added in Table -> "+table+" successfully");
        return ResponseEntity.ok(rs);
    }

    @DeleteMapping("/{database}/{table}")
    public ResponseEntity<?> removeKey(@PathVariable("database") String database, @PathVariable("table") String table, @PathVariable("key") String key) throws KeyValueException {
        KeyValueHandlerInterface kvh = keyValueMaster.getKeyValueHandler(database, table);
        kvh.remove(key);
        ResponseDTO rs = new ResponseDTO();
        rs.setMessage("Value removed from Table -> "+table+" successfully");
        return ResponseEntity.ok(rs);    }

    @GetMapping("/{database}/{table}/{key}")
    public ResponseEntity<?> getKey(@PathVariable("database") String database, @PathVariable("table") String table, @PathVariable("key") String key) throws KeyValueException {
        KeyValueHandlerInterface kvh = keyValueMaster.getKeyValueHandler(database, table);
        String value = kvh.get(key);
        ResponseDTO rs = new ResponseDTO();
        rs.addData("value", value);
        return ResponseEntity.ok(rs);
    }
}
