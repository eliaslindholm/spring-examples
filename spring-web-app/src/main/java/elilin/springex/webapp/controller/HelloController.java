package elilin.springex.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Elias Lindholm
 *
 */
@Controller
public class HelloController {
	
	@RequestMapping("/hello")
	public ModelAndView get() {
		ModelAndView mav = new ModelAndView("hello");
		mav.addObject("msg", "Hello World!");
		return mav;
	}
	
}
