package org.atm.dc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class App {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

}
