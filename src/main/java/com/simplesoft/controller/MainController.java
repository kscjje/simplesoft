package com.simplesoft.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderService;

@Controller
public class MainController {
	private static final Logger log = LogManager.getLogger(MainController.class);
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/main")
	public String main() {
		return "/main";
	}
	@GetMapping("/login")
	public String login(@RequestParam Map<String, Object> paramMap, Model model ) {
		model.addAttribute("returnUrl", paramMap.get("returnUrl"));
		model.addAttribute("type", paramMap.get("type"));
		return "/login";
	}
}
