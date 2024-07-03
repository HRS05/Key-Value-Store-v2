package com.hrs.key.value;

import com.hrs.key.value.handler.KeyValueHandler;
import com.hrs.key.value.handler.KeyValueHandlerInterface;
import com.hrs.key.value.handler.KeyValueMaster;
import com.hrs.key.value.handler.KeyValueMasterInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private final static String path = "/Users/harshsharma/harsh/key-value/KeyValue/common/key.value/data/";
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		try {


		} catch (Exception e) {
			System.out.println("Exception : ------> " + e);
		}
	}

}
