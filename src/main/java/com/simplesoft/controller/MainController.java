package com.simplesoft.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.simplesoft.mapper.MemberMapper;
import com.simplesoft.member.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Autowired
	MemberService memberService;
	
	@Autowired 
	MemberMapper memberMapper;
	
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
		
		List<Map<String, Object>> resultMap = memberService.selectMemberList(paramMap);
		log.info(""+resultMap);
		
		paramMap.put("userNo",1);
		Map<String, Object> resultMap2 = memberService.selectMemberDetail(paramMap);
		log.info(""+resultMap2);
		
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("resultMap2", resultMap2);
		return "/order/list";
	}
}
