package com.simplesoft.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberController  {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/joinStep1")
	public String joinStep1(HttpSession session) {
		String serviceYn = (String)session.getAttribute("serviceYn");
		if(serviceYn != null && "Y".equals(serviceYn)){
			return "member/joinStep2";
		}
		return "member/joinStep1";
	}
	@RequestMapping("/joinStep2")
	public String joinStep2(HttpSession session, @RequestParam Map<String, Object> paramMap) {
		String serviceYn = (String)paramMap.get("serviceYn");
		String serviceYnSesion = (String)session.getAttribute("serviceYn");
		if(serviceYnSesion != null && "Y".equals(serviceYnSesion)){
			return "member/joinStep2";
		} else if(serviceYn != null && "Y".equals(serviceYn)){
			session.setAttribute("serviceYn", "Y");
			return "member/joinStep2";
		} else {
			return "member/joinStep1";
		}
	}
}
