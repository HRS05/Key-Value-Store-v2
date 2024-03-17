package com.hrs.key.value.api;

import com.hrs.key.value.dto.ResponseDTO;
import com.hrs.key.value.exception.KeyValueException;
import com.hrs.key.value.handler.KeyValueMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/master")
public class MasterAPI {

    @Autowired
    private KeyValueMaster keyValueMaster;
    @GetMapping("/create/{database}")
    public ResponseEntity<?> createDatabase(@PathVariable String database) throws KeyValueException {
        keyValueMaster.createDataBase(database);
        ResponseDTO rs = new ResponseDTO();
        rs.setMessage("Database created successfully");
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/create/{database}/{table}")
    public ResponseEntity<?> createTable(@PathVariable("database") String database, @PathVariable("table") String table) throws  KeyValueException {
        keyValueMaster.createTable(database, table);
        return null;
    }

    @DeleteMapping("/delete/{database}")
    public ResponseEntity<?> deleteDatabase(@PathVariable String database) throws KeyValueException {
        keyValueMaster.deleteDataBase(database);
        return null;
    }

    @DeleteMapping ("/delete/{database}/{table}")
    public ResponseEntity<?> deleteTable(@PathVariable("database") String database, @PathVariable("table") String table) throws  KeyValueException {
        keyValueMaster.deleteTable(database, table);
        return null;
    }

}
