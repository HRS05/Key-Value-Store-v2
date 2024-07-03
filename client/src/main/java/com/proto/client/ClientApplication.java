package com.proto.client;

import com.proto.client.exception.KeyValueException;
import com.proto.client.sdk.KeyValue;
import com.proto.client.service.KeyValueClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ApplicationContext ap = SpringApplication.run(ClientApplication.class, args);
		KeyValueClientService kvcs = ap.getBean(KeyValueClientService.class);
		KeyValue.setKeyValueClientService(kvcs);
		KeyValue kv;
		try {
//			KeyValue.createDatabase("d2");
//			KeyValue.createTable("d2", "t1");
//			KeyValue.createTable("d2", "t2");
			KeyValue kv1 = new KeyValue("d2", "t1");
			KeyValue kv2 = new KeyValue("d2", "t2");

			for (int i = 0; i < 1000; i++) {
				kv1.set("qabbasccwfdKey" + i + i + i, "qvalue"+i, 180000L);
				kv2.set("qabbasccwdfkey" + i + i + i, "qvalue"+i);
			}

//			for (int i = 0; i < 1000; i++) {
//				try {
//					String k = kv1.get("Key" + i + i + i);
//					System.out.println("value -> "+ k);
//				} catch (KeyValueException e) {
//				}
//
//			}
		}catch (Exception e) {

		}

	}

}
