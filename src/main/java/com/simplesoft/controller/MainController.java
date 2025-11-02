package com.simplesoft.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/main")
	public String main(Model model,HttpServletRequest request) {
		
		return "/main";
	}
	@GetMapping("/login")
	public String login(@RequestParam Map<String, Object> paramMap, Model model ) {
		if(paramMap.get("type") == null || !"noneMember".equals((String)paramMap.get("type"))) {
			return "/main";
		}
		model.addAttribute("returnUrl", paramMap.get("returnUrl"));
		model.addAttribute("type", paramMap.get("type"));
		return "/login";
	}
}
