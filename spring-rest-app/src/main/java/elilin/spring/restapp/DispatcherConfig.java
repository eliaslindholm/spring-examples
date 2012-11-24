package elilin.spring.restapp;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @author Elias Lindholm
 *
 */
@Configuration
@EnableWebMvc
public class DispatcherConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		converters.add(jsonMessageConverter());
	}

	private HttpMessageConverter<?> jsonMessageConverter() {
		MappingJacksonHttpMessageConverter c = new MappingJacksonHttpMessageConverter();
		c.setObjectMapper(new CustomObjectMapper());
		return c;
	}

}
