package com.simplesoft.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.member.service.MemberService;
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
	
	@Autowired
	MemberService memberService;
	
	@Value("${clientKey}")
	String clientKey;
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
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/orderTest")
	public String orderTest(Model model) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> resultMap = menuboardService.selectMenuBoardList(paramMap);
		model.addAttribute("resultMap", resultMap);
		return "/order/listTest";
	}
	
	/**
	 * 장바구니
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/cart")
	public String cart(Model model, HttpSession session, HttpServletRequest request) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> cartList = new ArrayList<Map<String, Object>>();
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		String noneMember = (String)request.getParameter("noneMember");
		if(noneMember != null) session.setAttribute("noneMember", session.getId());
		if(loginInfo != null) {
			paramMap.put("userSession", "");
			paramMap.put("userNo", loginInfo.getUserNo());
		} else {
			session.setAttribute("loginInfo", null);
			paramMap.put("userSession", session.getId());
			paramMap.put("userNo", 0);
		}
		cartList = cartService.selectCartList(paramMap);
		model.addAttribute("cartList", cartList);
		return "/order/cart";
	}
	/**
	 * 장바구니
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/cartTest")
	public String cartTest(Model model, HttpSession session) {
		
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
		return "/order/cartTest";
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
			String sessionNone = (String)session.getAttribute("noneMember");
			if((!StringUtils.isEmpty(noneMember) && "Y".equals(noneMember)) || null != sessionNone) {
				paramMap.put("userSession", session.getId());
				paramMap.put("userNo", 0);
				userNm = "비회원";
				session.setAttribute("noneMember", session.getId());
			} else {
				//회원이면 cart로 비회원이면 request.getRequestURI().toString();로
				String returnUrl = "/cart";
				model.addAttribute("returnUrl", returnUrl);
				return GlobalVariable.REDIRECT_LOGIN;
			}
		}
		
		String setArray = request.getParameter("setArray");
		String[] setList = setArray.split("\\^");
		
		String cartArray = request.getParameter("cartArray");
		if("".equals(cartArray)) {
			model.addAttribute("PARAM_MESSAGE", "선택한 식단이 없습니다.");
			return GlobalVariable.REDIRECT_BACK;
		}
		String[] cartList = cartArray.split(",");
		paramMap.put("cartList", cartList);
		List<Map<String, Object>> cartShopList = cartService.selectOrderCartShop(paramMap);		//장바구니 상품이 판매가능한 상품인지 조회
		List<OrderProductVO> productList = new ArrayList<OrderProductVO>();
		//주문정보에서 상품리스트 수 만큼 저장
		if(cartShopList.size() > 0 ) {
			int totalPrice = 0;		//총상품금액
			int totalQty = 0;		//총상품개수
			int productAmt = 0;		//판매가
			int deliveryPrice = 0;	//배송비
			
			orderVO.setOrderStatus(GlobalVariable.ORDER_STATUS_WAIT);
			orderVO.setUserNo(Integer.parseInt("null".equals(String.valueOf(paramMap.get("userNo"))) ? "0" : String.valueOf(paramMap.get("userNo"))));
			orderVO.setUserSession(orderVO.getUserNo() == 0 ? (String)paramMap.get("userSession") : "");
			orderVO.setRegUser(userNm);
			
			int totalDcQty = 0;		//할인 가능한 개수(15개 이상 시 할인)
			for(Map<String, Object> map : cartShopList) {
				String orderSet = "";
				for(String cartNo : setList) {
					if(String.valueOf(map.get("cartNo")).equals(cartNo.split(",")[0])) {
						orderSet = cartNo.split(",")[1];
						break; // 찾았으면 루프 종료
					}
				}
				
				if("1000".equals(orderSet) || "3000".equals(orderSet)) {
					totalDcQty += Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty")));
				}
			}

			// totalDcQty에 따라 baseProductAmt 결정
			int baseProductAmt = GlobalVariable.PRODUCT_AMT_1;
			if (totalDcQty >= 15) { // 조건을 원하는 수량으로 변경하세요
				baseProductAmt = GlobalVariable.PRODUCT_AMT_1 - 1000;
			}
			for(Map<String, Object> map : cartShopList) {
				String menuDay = String.valueOf(map.get("menuDay")).split(" ")[0];
				if(!timeCheck(menuDay)) {
					model.addAttribute("PARAM_MESSAGE", "주문 가능한 시간이 지난 일자가 선택되어 있습니다.");
					model.addAttribute("returnUrl", "/cart");
					return GlobalVariable.REDIRECT_SUBMIT;
				}
				
				//선택한 세트에 따라 판매가 변경
				String orderSet = "";
				for(String cartNo : setList) {
					if(String.valueOf(map.get("cartNo")).equals(cartNo.split(",")[0])) orderSet = cartNo.split(",")[1];
				}
				int payAmt = 0;		//결제금액
				int qty = Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty")));
				String menuMsgDetail = String.valueOf(map.get("menuMsgDetail"));	//일반세트시 상품명 각각 수량
				String delivTime = String.valueOf(map.get("delivTime"));
				if("1000".equals(orderSet)) {
					productAmt = baseProductAmt;
					payAmt = productAmt * qty;
					payAmt += IntStream.range(0, menuMsgDetail.split("\\|").length)
									.map(i -> {
										int value = Integer.parseInt(menuMsgDetail.split("\\|")[i]);
										int positionValue = GlobalVariable.PROUDCT_MAP[i];
										if (value == qty) return 0; //
										if (value > qty) return (value - qty) * positionValue;
										return -(qty - value) * positionValue;
									})
									.sum();
					int minSum = qty * productAmt;
					payAmt = Math.max(payAmt, minSum);
				} else if ("2000".equals(orderSet)) {
					productAmt = GlobalVariable.PRODUCT_AMT_2;
					payAmt = productAmt * qty;
				} else {
					productAmt = baseProductAmt;
					payAmt = productAmt * qty;
				}
				
				totalPrice += payAmt;
				totalQty += qty;
				
				OrderProductVO opVo = new OrderProductVO();
				opVo.setMenuBoardSeq(Integer.parseInt("null".equals(String.valueOf(map.get("menuBoardSeq"))) ? "0" : String.valueOf(map.get("menuBoardSeq"))));
				opVo.setOrderQty(qty);
				opVo.setOrderSet(orderSet);
				opVo.setPayAmt(payAmt);
				opVo.setUsedPoint(opVo.getPayAmt() / 100);
				opVo.setMenuMsgDetail(menuMsgDetail);
				opVo.setDelivTime(delivTime);
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
				model.addAttribute("PARAM_MESSAGE", "식단정보가 없습니다.<br>계속 오류 발생시 고객센터에 문의 하여 주십시오.");
				return GlobalVariable.REDIRECT_BACK;
			}
		} else {
			model.addAttribute("PARAM_MESSAGE", "주문 가능한 식단이 없습니다.");
			model.addAttribute("returnUrl", "/cart");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
	}
	/**
	 * 주문서 작성 준비
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/payRegTest")
	public String payRegTest(HttpServletRequest request, Model model, HttpSession session) throws UnsupportedEncodingException {
		
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
			String sessionNone = (String)session.getAttribute("noneMember");
			if((!StringUtils.isEmpty(noneMember) && "Y".equals(noneMember)) || null != sessionNone) {
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
		
		
		String setArray = request.getParameter("setArray");
		String[] setList = setArray.split("\\^");
		
		String cartArray = request.getParameter("cartArray");
		if("".equals(cartArray)) {
			model.addAttribute("PARAM_MESSAGE", "선택한 식단이 없습니다.");
			return GlobalVariable.REDIRECT_BACK;
		}
		String[] cartList = cartArray.split(",");
		paramMap.put("cartList", cartList);
		List<Map<String, Object>> cartShopList = cartService.selectOrderCartShop(paramMap);		//장바구니 상품이 판매가능한 상품인지 조회
		List<OrderProductVO> productList = new ArrayList<OrderProductVO>();
		//주문정보에서 상품리스트 수 만큼 저장
		if(cartShopList.size() > 0 ) {
			int totalPrice = 0;		//총상품금액
			int totalQty = 0;		//총상품개수
			int productAmt = 0;		//판매가
			int deliveryPrice = 0;	//배송비
			
			orderVO.setOrderStatus(GlobalVariable.ORDER_STATUS_WAIT);
			orderVO.setUserNo(Integer.parseInt("null".equals(String.valueOf(paramMap.get("userNo"))) ? "0" : String.valueOf(paramMap.get("userNo"))));
			orderVO.setUserSession(orderVO.getUserNo() == 0 ? (String)paramMap.get("userSession") : "");
			orderVO.setRegUser(userNm);
			
			int totalDcQty = 0;		//할인 가능한 개수(15개 이상 시 할인)
			for(Map<String, Object> map : cartShopList) {
				String orderSet = "";
				for(String cartNo : setList) {
					if(String.valueOf(map.get("cartNo")).equals(cartNo.split(",")[0])) {
						orderSet = cartNo.split(",")[1];
						break; // 찾았으면 루프 종료
					}
				}
				
				if("1000".equals(orderSet) || "3000".equals(orderSet)) {
					totalDcQty += Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty")));
				}
			}
			
			// totalDcQty에 따라 baseProductAmt 결정
			int baseProductAmt = GlobalVariable.PRODUCT_AMT_1;
			if (totalDcQty >= 15) { // 조건을 원하는 수량으로 변경하세요
				baseProductAmt = GlobalVariable.PRODUCT_AMT_1 - 1000;
			}
			for(Map<String, Object> map : cartShopList) {
				String menuDay = String.valueOf(map.get("menuDay")).split(" ")[0];
				if(!timeCheck(menuDay)) {
					model.addAttribute("PARAM_MESSAGE", "주문 가능한 시간이 지난 일자가 선택되어 있습니다.");
					model.addAttribute("returnUrl", "/cart");
					return GlobalVariable.REDIRECT_SUBMIT;
				}
				
				//선택한 세트에 따라 판매가 변경
				String orderSet = "";
				for(String cartNo : setList) {
					if(String.valueOf(map.get("cartNo")).equals(cartNo.split(",")[0])) orderSet = cartNo.split(",")[1];
				}
				int payAmt = 0;		//결제금액
				int qty = Integer.parseInt("null".equals(String.valueOf(map.get("qty"))) ? "0" : String.valueOf(map.get("qty")));
				String menuMsgDetail = String.valueOf(map.get("menuMsgDetail"));	//일반세트시 상품명 각각 수량
				String delivTime = String.valueOf(map.get("delivTime"));
				if("1000".equals(orderSet)) {
					productAmt = baseProductAmt;
					payAmt = productAmt * qty;
					payAmt += IntStream.range(0, menuMsgDetail.split("\\|").length)
							.map(i -> {
								int value = Integer.parseInt(menuMsgDetail.split("\\|")[i]);
								int positionValue = GlobalVariable.PROUDCT_MAP[i];
								if (value == qty) return 0; //
								if (value > qty) return (value - qty) * positionValue;
								return -(qty - value) * positionValue;
							})
							.sum();
					int minSum = qty * productAmt;
					payAmt = Math.max(payAmt, minSum);
				} else if ("2000".equals(orderSet)) {
					productAmt = GlobalVariable.PRODUCT_AMT_2;
					payAmt = productAmt * qty;
				} else {
					productAmt = baseProductAmt;
					payAmt = productAmt * qty;
				}
				
				totalPrice += payAmt;
				totalQty += qty;
				
				OrderProductVO opVo = new OrderProductVO();
				opVo.setMenuBoardSeq(Integer.parseInt("null".equals(String.valueOf(map.get("menuBoardSeq"))) ? "0" : String.valueOf(map.get("menuBoardSeq"))));
				opVo.setOrderQty(qty);
				opVo.setOrderSet(orderSet);
				opVo.setPayAmt(payAmt);
				opVo.setUsedPoint(opVo.getPayAmt() / 100);
				opVo.setOrderSet(orderSet);
				opVo.setMenuMsgDetail(menuMsgDetail);
				opVo.setDelivTime(delivTime);
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
				model.addAttribute("returnUrl", "/"+orderVO.getOrderNo()+"/orderSheetTest");
				return GlobalVariable.REDIRECT_SUBMIT;
			} else {
				//구매상품정보가없음.
				model.addAttribute("PARAM_MESSAGE", "식단정보가 없습니다.<br>계속 오류 발생시 고객센터에 문의 하여 주십시오.");
				return GlobalVariable.REDIRECT_BACK;
			}
		} else {
			model.addAttribute("PARAM_MESSAGE", "주문 가능한 식단이 없습니다.");
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
	@PostMapping("/{orderNo}/orderSheet")
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
		vo.setOrderStatus(GlobalVariable.ORDER_STATUS_WAIT);
		OrderVO order = orderService.selectOrderApplyInfo(vo);
		
		if (order == null) {
			model.addAttribute("PARAM_MESSAGE", "주문 상품 정보가 없습니다.\n다시 진행해 주세요.");
			model.addAttribute("returnUrl", "/cart");
			return GlobalVariable.REDIRECT_SUBMIT;
		} else {
			for(OrderProductVO a : order.getProductList()) {
				String menuDay = String.valueOf(a.getMenuDay()).length() == 8 ? String.valueOf(a.getMenuDay()).substring(0,4)+"-"+String.valueOf(a.getMenuDay()).substring(4,6)+"-"+String.valueOf(a.getMenuDay()).substring(6,8) : "null";
				if(!timeCheck(menuDay) && !"null".equals(menuDay)) {
					model.addAttribute("PARAM_MESSAGE", "주문 가능한 시간이 지난 일자가 선택되어 있습니다.");
					model.addAttribute("returnUrl", "/cart");
					return GlobalVariable.REDIRECT_SUBMIT;
				}
			}
			model.addAttribute("loginInfo", loginInfo);
			model.addAttribute("orderNo", orderNo);
			model.addAttribute("order", order);
			model.addAttribute("clientKey", clientKey);
		}
		return "/order/payReg";
	}
	/**
	 * 주문서 작성
	 * @param request
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/{orderNo}/orderSheetTest")
	public String orderSheetTest(@PathVariable("orderNo") String orderNo, HttpServletRequest request, Model model, HttpSession session){
		
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
		vo.setOrderStatus(GlobalVariable.ORDER_STATUS_WAIT);
		OrderVO order = orderService.selectOrderApplyInfo(vo);
		
		if (order == null) {
			model.addAttribute("PARAM_MESSAGE", "주문 상품 정보가 없습니다.\n다시 진행해 주세요.");
			model.addAttribute("returnUrl", "/cart");
			return GlobalVariable.REDIRECT_SUBMIT;
		} else {
			for(OrderProductVO a : order.getProductList()) {
				String menuDay = String.valueOf(a.getMenuDay()).length() == 8 ? String.valueOf(a.getMenuDay()).substring(0,4)+"-"+String.valueOf(a.getMenuDay()).substring(4,6)+"-"+String.valueOf(a.getMenuDay()).substring(6,8) : "null";
				if(!timeCheck(menuDay) && !"null".equals(menuDay)) {
					model.addAttribute("PARAM_MESSAGE", "주문 가능한 시간이 지난 일자가 선택되어 있습니다.");
					model.addAttribute("returnUrl", "/cart");
					return GlobalVariable.REDIRECT_SUBMIT;
				}
			}
			model.addAttribute("orderNo", orderNo);
			model.addAttribute("order", order);
			model.addAttribute("clientKey", clientKey);
		}
		return "/order/payRegNew";
	}
	
	private boolean timeCheck(String dateString) {
		boolean result = true;
		
		LocalDateTime dateToCheck = LocalDateTime.parse(dateString + "T16:00:00");
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		LocalDate dateToCheckDate	= dateToCheck.toLocalDate();
		LocalDate currentDate		= currentDateTime.toLocalDate();
		LocalDate gubunDate			= null;		
		LocalTime timeToCheck 		= currentDateTime.toLocalTime();
		
		if (timeToCheck.isAfter(LocalTime.of(16, 00))) {
			gubunDate = currentDate.plusDays(2);
		} else {
			gubunDate = currentDate.plusDays(1);
		}
		if(gubunDate.isAfter(dateToCheckDate)) {
			result = false;
		}
		return result;
	}
}
