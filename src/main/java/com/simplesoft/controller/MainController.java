package com.simplesoft.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.simplesoft.member.service.MemberService;
import com.simplesoft.menuboard.service.MenuBoardService;

@Controller
public class MainController {
	private static final Logger LOGGER = LogManager.getLogger(MainController.class);
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@GetMapping("/main")
	public String main() {
//		MemberVO vo = new MemberVO();
//		vo.setUserId("user");
//		vo.setPwd("1234");
//		vo.setGender("M");
//		vo.setMemNm("홍길동");
//		int a = memberService.insertMember(vo);
		return "/main";
	}
	
	@GetMapping("/order")
	public String order(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
		LOGGER.info(""+resultMap);
		
		model.addAttribute("resultMap", resultMap);
		return "/order/list";
	}
	
	@GetMapping("/cart")
	public String cart(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> resultMap = memberService.selectMemberList(paramMap);
		LOGGER.info(""+resultMap);
		
		paramMap.put("userNo",1);
		Map<String, Object> resultMap2 = memberService.selectMemberDetail(paramMap);
		LOGGER.info(""+resultMap2);
		
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("resultMap2", resultMap2);
		return "/order/cart";
	}
}
