package com.simplesoft.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.menuboard.service.MenuBoardService;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.util.GlobalVariable;
import com.simplesoft.util.RequestConvertUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrderController {
	
	@Autowired
	MenuBoardService menuboardService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
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
	 * 주문서 작성 준비
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/payReg")
	public String payReg(HttpServletRequest request, Model model, HttpSession session) throws UnsupportedEncodingException {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		OrderVO orderVO = new OrderVO();
		String userNm = "";
		
		if(loginInfo != null) {
			paramMap.put("userNo", loginInfo.getUserNo());
			userNm = loginInfo.getUserNm();
		} else {
			String noneMember = request.getParameter("noneMember");
			if(!StringUtils.isEmpty(noneMember) && "Y".equals(noneMember)) {
				paramMap.put("userSession", session.getId());
				paramMap.put("userNo", 0);
				userNm = "비회원";
				session.setAttribute("noneMember", session.getId());
			} else {
				//비회원 로그인 확인하기 
				String returnUrl = request.getRequestURI().toString();
				returnUrl += RequestConvertUtil.convertMapToParam(request.getParameterMap());
				model.addAttribute("type", "noneMember");
				model.addAttribute("returnUrl", returnUrl);
				return GlobalVariable.REDIRECT_LOGIN;
			}
		}
		
		String cartArray = request.getParameter("cartArray");
		if("".equals(cartArray)) {
			model.addAttribute("PARAM_MESSAGE", "선택한 상품이 없습니다.");
			return GlobalVariable.REDIRECT_BACK;
		}
		
		String[] cartList = cartArray.split(",");
		paramMap.put("cartList", cartList);
		List<Map<String, Object>> cartShopList = cartService.selectOrderCartShop(paramMap);		//장바구니 상품이 판매가능한 상품인지 조회
		List<OrderProductVO> productList = new ArrayList<OrderProductVO>();
		
		if(cartShopList.size() > 0 ) {
			int totalPrice = 0;		//총상품금액
			int totalQty = 0;		//총상품개수
			int deliveryPrice = 0;	//배송비
			
			orderVO.setOrderStatus(GlobalVariable.ORDER_STATUS_APPLY);
			orderVO.setUserNo(Integer.parseInt("null".equals(String.valueOf(paramMap.get("userNo"))) ? "0" : String.valueOf(paramMap.get("userNo"))));
			orderVO.setUserSession(orderVO.getUserNo() == 0 ? (String)paramMap.get("userSession") : "");
			orderVO.setRegUser(userNm);
			
			for(Map<String, Object> map : cartShopList) {
				totalPrice += GlobalVariable.PRODUCT_AMT * Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty")));
				totalQty += Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty")));
				
				OrderProductVO opVo = new OrderProductVO();
				opVo.setMenuBoardSeq(Integer.parseInt("null".equals(String.valueOf(map.get("menuBoardSeq"))) ? "0" : String.valueOf(map.get("menuBoardSeq"))));
				opVo.setOrderQty(Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty"))));
				opVo.setPayAmt(GlobalVariable.PRODUCT_AMT * Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty"))));
				opVo.setUsedPoint(opVo.getPayAmt() / 100);
				productList.add(opVo);
			}
			deliveryPrice = totalQty >= 3 ? 0 : GlobalVariable.DELY_AMT;
			orderVO.setTotalProductAmt(totalPrice);
			orderVO.setTotalDisAmt(0);
			orderVO.setTotalDelyAmt(deliveryPrice);
			orderVO.setProductList(productList);
			if(productList != null && productList.size() > 0) {
				if(orderService.insertOrderMst(orderVO) < -1) {
					model.addAttribute("PARAM_MESSAGE", "주문서 작성 중 오류가 발생하였습니다.");
					return GlobalVariable.REDIRECT_BACK;
				} else {
					//장바구니 번호 세션에 담기
					session.setAttribute("cartList", cartArray);
				}
				model.addAttribute("PARAM_MAP", param.put("order", orderVO.getOrderNo()));
				model.addAttribute("METHOD", "post");
				model.addAttribute("returnUrl", "/"+orderVO.getOrderNo()+"/orderSheet");
				return GlobalVariable.REDIRECT_SUBMIT;
			} else {
				//구매상품정보가없음.
				model.addAttribute("PARAM_MESSAGE", "상품정보가 없습니다.<br>계속 오류 발생시 고객센터에 문의 하여 주십시오");
				return GlobalVariable.REDIRECT_BACK;
			}
		} else {
			//model.addAttribute("PARAM_MESSAGE", "주문 가능한 상품이 없습니다.");
			model.addAttribute("returnUrl", "/cart");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
	}
	
	/**
	 * 주문서 작성
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/{orderNo}/orderSheet")
	public String orderSheet(@PathVariable("orderNo") String orderNo, HttpServletRequest request, Model model, HttpSession session){
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		OrderVO vo = new OrderVO();
		int userNo = 0;
		if(loginInfo != null) {
			userNo = loginInfo.getUserNo();
		} else {
			if(session.getAttribute("noneMember") != null) {
				//비회원일경우
				vo.setUserSession((String)session.getAttribute("noneMember"));
			} else {
				model.addAttribute("returnUrl", "/cart");
				return GlobalVariable.REDIRECT_SUBMIT;
			}
		}
		vo.setOrderNo(orderNo);
		vo.setUserNo(userNo);
		vo.setOrderStatus(GlobalVariable.ORDER_STATUS_APPLY);
		OrderVO order = orderService.selectOrderApplyInfo(vo);
		if (order == null) {
			model.addAttribute("PARAM_MESSAGE", "주문 상품 정보가 없습니다.\n다시 진행해 주세요.");
			model.addAttribute("returnUrl", "/cart");
			return GlobalVariable.REDIRECT_SUBMIT;
		} else {
			model.addAttribute("orderNo", orderNo);
			model.addAttribute("order", order);
		}
		return "/order/payReg";
	}
}
