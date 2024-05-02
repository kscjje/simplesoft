package com.simplesoft.controller;

import java.util.HashMap;
import java.util.Map;

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
	 * 비회원 주문내역
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/noneOrderList")
	public String noneOrderList(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
//		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
//		model.addAttribute("resultMap", resultMap);
		return "/mypage/orderList";
	}
}
