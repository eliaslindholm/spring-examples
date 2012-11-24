package elilin.spring.restapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @author Elias Lindholm
 *
 */
@Configuration
public class AppConfig {
	
	@Bean
	public MessageService messageService() {
		return new MessageService();
	}

}
