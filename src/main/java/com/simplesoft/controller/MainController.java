package com.simplesoft.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.menuboard.service.MenuBoardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	private static final Logger log = LogManager.getLogger(MainController.class);
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	CartService cartService;
	
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
	
	/**
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/order")
	public String order(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
		log.info(""+resultMap);
		
		model.addAttribute("resultMap", resultMap);
		return "/order/list";
	}
	
	/**
	 * 장바구니
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/cart")
	public String cart(Model model, HttpSession session) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> cartList = new ArrayList<Map<String, Object>>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("userNo", loginInfo.getUserNo());
		} else {
			paramMap.put("userSession", session.getId());
			paramMap.put("userNo", 0);
		}
		cartList = cartService.selectCartList(paramMap);		
		model.addAttribute("cartList", cartList);
		return "/order/cart";
	}
}
