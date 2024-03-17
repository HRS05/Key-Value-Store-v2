package com.proto.client;

import com.proto.client.service.KeyValueClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		ApplicationContext ap = SpringApplication.run(ClientApplication.class, args);
		KeyValueClientService kvcs = ap.getBean(KeyValueClientService.class);
		try {
//			kvcs.createDatabase("d2");
		//	kvcs.createTable("d2", "t1");
			for (int i=0; i< 432; i++)
			kvcs.setKey("d2", "t1", "hey-hhhgu-jjjjjjkjku"+i+i+i, "harsh kkk");
		} catch (Exception e) {

		}
	}

}
