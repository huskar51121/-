package com.example.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.user;
import com.example.others.Result;
import com.example.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/userrequest")
public class UserController {

	private final UserService userservice;

	public UserController(UserService userservice) {
		this.userservice = userservice;
	}

	// 这是用来注册的,输入注册信息，成功则返回“创建成功”，失败则返回"该用户已存在"
	@PostMapping("/register")
	String addoneusers(String username, String password, String mobile) {
		System.out.printf(LocalDate.now().toString());
		System.out.printf("786112605\n");
		// 这里暂时假装收到了一个来自前端的注册信息,之后要通过该函数的参数传入这个json
		JSONObject json = new JSONObject();
		json = new JSONObject();
		json.put("name", username);
		json.put("password", password);
		json.put("phone", mobile);
		return userservice.addoneusers(json);
	}

	// 这是管理员用来查看所有用户的，返回一个list，返回到网页中会自动变为json
	@PostMapping("/getallusers")
	List<Map<String, Object>> getallusers() {
		return userservice.getallusers();
	}

	// 这是用来登录的，输入用户名和密码，会返回“该用户不存在”，“密码错误”，“登录成功”
	@PostMapping("/login")
	ResponseEntity<String> login(String username, String password) {
		try {
			String result = userservice.login(username, password);
			if (result == "登录成功") {
				String obj = UserService.createToken(username);
				System.out.println(ResponseEntity.ok(obj));
				System.out.println("6543\n");
				return ResponseEntity.ok(obj);
			} else {
				return ResponseEntity.ok(result);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("登录失败：" + e.getMessage());
		}
	}

	@PostMapping("/changenta")
	ResponseEntity<String> changenta(String token, String name, String tagline, String avatarurl) {
		if (token == null) {
			return ResponseEntity.badRequest().build();
		}
		String lastname = UserService.deToken(token);

		if (lastname == "验证失败") {
			return ResponseEntity.badRequest().build();
		} else {

			int userid = (int) userservice.getuserbyname(lastname).get("id");
			String info1 = userservice.changenta(userid, name, tagline, avatarurl);
			if (info1 == "修改成功") {
				String token1 = UserService.createToken(name);
				return ResponseEntity.ok(token1);
			} else {
				return ResponseEntity.ok(info1);
			}

		}
	}

	@PostMapping("/addgold")
	ResponseEntity<String> addgold(String token) {
		if (token == null) {
			return ResponseEntity.badRequest().build();
		}
		String name = UserService.deToken(token);

		if (name == "验证失败") {
			return ResponseEntity.badRequest().build();
		} else {
			int userid = (int) userservice.getuserbyname(name).get("id");
			String info1 = userservice.addgold(userid);
			return ResponseEntity.ok(info1);
		}
	}

	@PostMapping("/changepassword")
	ResponseEntity<String> changepassword(String token, String lastpassword, String newpassword) {
		if (token == null) {
			return ResponseEntity.badRequest().build();
		}
		String name = UserService.deToken(token);

		if (name == "验证失败") {
			return ResponseEntity.badRequest().build();
		} else {
			String result = userservice.login(name, lastpassword);
			if (result == "登录成功") {
				int userid = (int) userservice.getuserbyname(name).get("id");
				return ResponseEntity.ok(userservice.changepassword(userid, newpassword));
			} else {
				return ResponseEntity.ok(result);
			}
		}
	}

	// 这是页面用来获取用户信息的，输入用户名，会返回该用户的所有信息，如果没有则返回null，类型是map，返回到网页中会自动变为json
	@PostMapping("/afterlogin")
	ResponseEntity<Map<String, Object>> getuserbytoken(String token) {
		if (token == null) {
			return ResponseEntity.badRequest().build();
		}
		String name = UserService.deToken(token);

		if (name == "验证失败") {
			return ResponseEntity.badRequest().build();
		} else {
			return ResponseEntity.ok(userservice.getuserbyname(name));
		}
	}
}
