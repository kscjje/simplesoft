package com.simplesoft.admin.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
	public String adminLogin(HttpServletRequest request, Model model) {
		
		request.getSession().invalidate();
		return "/admin/login";
	}
	
	/**
	 * 관리자 메인
	 * 
	 * @param model
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@GetMapping("/main")
	public String adminMain(Model model,HttpSession session) throws NoSuchAlgorithmException {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		ManagerVO loginVO = (ManagerVO)session.getAttribute("adminLoginInfo");
		
		if(loginVO == null) {
			model.addAttribute("returnUrl", "/adm/login");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		return "/admin/main";
	}
}