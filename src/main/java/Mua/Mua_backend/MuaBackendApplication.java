package Mua.Mua_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class MuaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuaBackendApplication.class, args);
	}

}
