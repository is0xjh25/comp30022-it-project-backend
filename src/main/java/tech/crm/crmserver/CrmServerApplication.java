package tech.crm.crmserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmServerApplication.class, args);
	}

}
