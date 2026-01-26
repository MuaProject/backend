package Mua.Mua_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MuaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuaBackendApplication.class, args);
	}

}
