package com.proto.client.api;

import com.proto.client.service.KeyValueClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create")
@AllArgsConstructor
public class KeyValueApi {

    KeyValueClientService keyValueClientService;
    @GetMapping("/{database}")
    public ResponseEntity<?> createDB(@PathVariable String database) {

        try {
            keyValueClientService.createDatabase(database);
        } catch (Exception e) {

        }
        return ResponseEntity.ok("");

    }
}
