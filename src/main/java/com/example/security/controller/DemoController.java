package com.example.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@GetMapping("/")
	public String hello() {
		return "Hello World";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "Hello admin";
	}
	
	@GetMapping("/user")
	public String user() {
		return "Hello user";
	}
}
