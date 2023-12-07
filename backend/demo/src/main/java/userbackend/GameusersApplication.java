package userbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan("gameuser")
public class GameusersApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameusersApplication.class, args);
	}

}
