package com.proto.client;

import com.proto.client.service.KeyValueClientService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		ApplicationContext ap = SpringApplication.run(ClientApplication.class, args);
	}

}
