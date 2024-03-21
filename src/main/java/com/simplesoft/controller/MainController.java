package com.simplesoft.controller;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.util.RequestConvertUtil;

import jakarta.servlet.http.HttpServletRequest;
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
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/main")
	public String main() {
		return "/main";
	}
	@GetMapping("/login")
	public String login(@RequestParam Map<String, Object> paramMap, Model model ) {
		model.addAttribute("returnUrl", paramMap.get("returnUrl"));
		return "/login";
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
	
	/**
	 * 주문서 작성
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@PostMapping("/payReg")
	public String payReg(HttpServletRequest request, Model model, HttpSession session) throws UnsupportedEncodingException {
		
		String returnUrl = request.getRequestURI().toString();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		OrderVO orderVO = new OrderVO();
		orderService.insertOrderMst(orderVO);
		if(loginInfo != null) {
		} else {
			returnUrl += RequestConvertUtil.convertMapToParam(request.getParameterMap());
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			model.addAttribute("PARAM_MAP", paramMap);
			model.addAttribute("returnUrl", returnUrl);
//			return GlobalVariable.REDIRECT_LOGIN;
		}
		return "/order/payReg";
	}
}
