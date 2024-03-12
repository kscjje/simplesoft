package com.simplesoft.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/rest")
@RestController
public class RestMainController {
	
	private static final Logger log = LogManager.getLogger(RestMainController.class);
	
	@Autowired
	CartService cartService;
	
	/**
	 * 장바구니 담기
	 * @param paramMap
	 * @param model
	 * @return
	 */
	@PostMapping("/cart/save")
	public BasicResponse cart(@RequestParam Map<String, Object> paramMap, Model model,HttpSession session) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("regUser", loginInfo.getMemNm());
			paramMap.put("userNo", loginInfo.getUserNo());
		} else {
			paramMap.put("userSession", session.getId());
			paramMap.put("regUser", "비회원");
			paramMap.put("userNo", 0);
		}
		cartService.insertCart(paramMap);
		
		result.put("resultCode", "SUCCESS");
		return new CommonResponse<Map<String, Object>>(result);
	}
}
