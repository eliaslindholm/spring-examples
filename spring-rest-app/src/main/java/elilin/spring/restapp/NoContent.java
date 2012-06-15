package elilin.spring.restapp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class NoContent extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
