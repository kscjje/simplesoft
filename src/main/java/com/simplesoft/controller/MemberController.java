package com.simplesoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderService;

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
	public String joinStep1() {
		return "member/joinStep1";
	}
	@GetMapping("/joinStep2")
	public String joinStep2() {
		return "member/joinStep2";
	}
}
