package com.example.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class UserController2 {

	@PostMapping("/addFriend")
	String home1(String userid, String upid) {
		System.out.println("12345654");
		System.out.println(userid);
		System.out.println(upid);
		return "11111";
	}

}
