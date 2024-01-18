package com.simplesoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author kscjje
 * @description 공통 컨트롤러
 * @since 2024.01.14
 */

@Controller
public class HomeController {
	
	@GetMapping({ "/" , "index" })
	public String home() {
		return "login";
	}
}
