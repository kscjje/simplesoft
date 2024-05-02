package com.simplesoft.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/adm")
public class AdminController {
	
	/**
	 * 관리자 로그인 화면
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public String adminLogin(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
//		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
//		model.addAttribute("resultMap", resultMap);
		return "/admin/login";
	}
	
	/**
	 * 관리자 메인
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/main")
	public String adminMain(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
//		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
//		model.addAttribute("resultMap", resultMap);
		return "/admin/main";
	}
}