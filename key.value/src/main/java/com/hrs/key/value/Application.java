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
//			KeyValueMaster kvm = KeyValueMaster.getKeyValueMaster(path);
//			System.out.println("****************************");
//			System.out.println(kvm);
//			System.out.println("****************************");
			//kvm.createDataBase("mydb");
//			kvm.createTable("mydb","mytable1");
//			kvm.createTable("mydb","mytable2");
//			kvm.createTable("mydb","mytable3");
//			kvm.createTable("mydb","mytable5");
//			KeyValueHandlerInterface kvh = kvm.getKeyValueHandler("mydb", "mytable1");
//			KeyValueHandlerInterface kvh2 = kvm.getKeyValueHandler("mydb", "mytable2");
//			try {
//				Thread.sleep(3000);
//			} catch (Exception e) {
//			}
//			for (int i = 0; i< 100; i++) {
//				kvh.set("harsh__qo_2_12"+i, "harsh is great");
//				kvh.set("harsh__qo_00_12"+i, "harsh is great");
//
//				kvh2.set("harsh___qo_2_12"+i, "harsh is great");
//				kvh2.set("harsh___qo_jd_12"+i, "harsh is great");
//
//
//			}



		} catch (Exception e) {
			System.out.println("Exception : ------> " + e);
		}
	}

}
