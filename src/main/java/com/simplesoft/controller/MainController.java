package com.simplesoft.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.util.MailTemplateSender;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	
	@Value("${Globals.frnt.domain}")
	private String domain;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
    private MailTemplateSender mailTemplateSender;
	
	@GetMapping("/main")
	public String main(Model model,HttpServletRequest request) {
		return "/main";
	}
	@GetMapping("/login")
	public String login(@RequestParam Map<String, Object> paramMap, Model model,HttpServletRequest request) {
		model.addAttribute("returnUrl", paramMap.get("returnUrl"));
		model.addAttribute("type", paramMap.get("type"));
		model.addAttribute("domain", domain);
		request.getSession().invalidate();
		return "/login";
	}
	@GetMapping("/findId")
	public String findId(@RequestParam Map<String, Object> paramMap, Model model ) {
		return "/member/findId";
	}
	@GetMapping("/findPassword")
	public String findPassword(@RequestParam Map<String, Object> paramMap, Model model ) {
		return "/member/findPassword";
	}
	@GetMapping("/findIdResult")
	public String getFindIdResult(@RequestParam Map<String, Object> paramMap, Model model ) {
		return "/member/findId";
	}
	@PostMapping("/findIdResult")
	public String postFindIdResult(@RequestParam Map<String, Object> paramMap, Model model ) {
		String userId = String.valueOf(paramMap.get("userId"));
		String regDt = String.valueOf(paramMap.get("regDt"));
		model.addAttribute("userId", userId);
		model.addAttribute("regDt", regDt);
		return "/member/findIdResult";
	}
	@PostMapping("/findPasswordResult")
	public String postFindPasswordResult(@RequestParam Map<String, Object> paramMap, Model model, HttpServletRequest request) {
		return "/member/findPasswordResult";
	}
	
}
