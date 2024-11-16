package com.simplesoft.controller.rest;

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
	 * @param session
	 * @return
	 */
	@PostMapping("/cart/save")
	public BasicResponse cartSave(@RequestParam Map<String, Object> paramMap, Model model,HttpSession session) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("regUser", loginInfo.getUserId());
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
	
	/**
	 * 장바구니 수량 수정
	 * @param paramMap
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/cart/qty/update")
	public BasicResponse cartQtyUpdate(@RequestParam Map<String, Object> paramMap, Model model,HttpSession session) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> cartDetail = new HashMap<String, Object>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("userNo", loginInfo.getUserNo());
		} else {
			paramMap.put("userSession", session.getId());
			paramMap.put("userNo", 0);
		}
		cartDetail = cartService.selectCartDetail(paramMap);
		if(cartDetail != null) {
			int suc = cartService.updateCartQty(paramMap);
			if(suc > 0) {
				result.put("resultCode", "SUCCESS");
			}
		} else {
			result.put("resultCode", "NoData");
		}
		
		return new CommonResponse<Map<String, Object>>(result);
	}
	/**
	 * 장바구니 세트 수정
	 * @param paramMap
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/cart/orderSet/update")
	public BasicResponse cartOrderSetUpdate(@RequestParam Map<String, Object> paramMap, Model model,HttpSession session) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> cartDetail = new HashMap<String, Object>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("userNo", loginInfo.getUserNo());
		} else {
			paramMap.put("userSession", session.getId());
			paramMap.put("userNo", 0);
		}
		cartDetail = cartService.selectCartDetail(paramMap);
		if(cartDetail != null) {
			int suc = cartService.updateCartOrderSet(paramMap);
			if(suc > 0) {
				result.put("resultCode", "SUCCESS");
			}
		} else {
			result.put("resultCode", "NoData");
		}
		
		return new CommonResponse<Map<String, Object>>(result);
	}
	/**
	 * 장바구니 삭제
	 * @param paramMap
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/cart/delete")
	public BasicResponse cartDelete(@RequestParam Map<String, Object> paramMap, Model model,HttpSession session) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("userNo", loginInfo.getUserNo());
		} else {
			paramMap.put("userSession", session.getId());
			paramMap.put("userNo", 0);
		}
		String[] arrCartNo = ((String)paramMap.get("checkArray")).split(",");
		paramMap.put("arrCartNo", arrCartNo);
		
		int suc = cartService.deleteCart(paramMap);
		if(suc > 0) {
			result.put("resultCode", "SUCCESS");
		}
		
		return new CommonResponse<Map<String, Object>>(result);
	}
}
