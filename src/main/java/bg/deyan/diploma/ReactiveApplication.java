package bg.deyan.diploma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveApplication implements CommandLineRunner {	 
	
	@Autowired
	private RequestEntryService requestEntryService;

	public static void main(String[] args) {
		SpringApplication.run(ReactiveApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		requestEntryService.processTraditional();
		
		requestEntryService.processReactive();
	}

}
