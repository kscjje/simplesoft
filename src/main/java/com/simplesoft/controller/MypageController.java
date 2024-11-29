package com.simplesoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/mypage")
public class MypageController {
	
	/**
	 * 비회원 주문조회
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/noneOrderCheck")
	public String noneOrderCheck(Model model) {
		
		return "/mypage/orderCheck";
	}
	/**
	 * 비회원 주문내역
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/noneOrderDetail")
	public String noneOrderDetail(Model model) {
		
		return "/mypage/orderDetail";
	}
}
