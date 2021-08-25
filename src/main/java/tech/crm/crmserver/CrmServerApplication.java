package tech.crm.crmserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("tech.crm.crmserver.mapper")
public class CrmServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmServerApplication.class, args);
	}

}
