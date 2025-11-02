package com.simplesoft.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member")
public class MemberRestController {

	@Autowired
	MemberService memberService;
	
	/**
	 * 아이디 중복체크
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/idCheck")
	public BasicResponse getIdCheck(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		Map<?,?> id = memberService.selectIdCheck(vo);
		returnData.put("id", id);
		
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	
	/**
	 * 회원 저장
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/save")
	public BasicResponse memberInsert(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO vo) throws Exception {
		System.out.println(vo.toString());
		Map<?,?> returnData = memberService.memberSave(vo);
		if("0000".equals(String.valueOf(returnData.get("RESULT")))){
			session.setAttribute("serviceYn", "");
		}
		return new CommonResponse<Map<?, ?>>(returnData);
	}
}
