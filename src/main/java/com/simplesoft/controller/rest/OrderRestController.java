package com.simplesoft.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplesoft.common.service.impl.TossServiceImpl;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.order.service.RefundVO;
import com.simplesoft.payments.service.PaymentsService;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.EncryptUtils;
import com.simplesoft.util.GlobalVariable;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/rest/order")
public class OrderRestController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	PaymentsService paymentsService;
	
	@Autowired
	TossServiceImpl tossService;
	
	/**
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping("/orderPayment")
	public BasicResponse order(Model model, HttpSession session, @ModelAttribute("orderVO") OrderVO vo) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		int userNo = 0;
		String userNm = "";
		
		MemberVO loginInfo = (MemberVO)session.getAttribute("loginInfo");
		if(loginInfo != null) {
			userNo = loginInfo.getUserNo();
			userNm = loginInfo.getUserNm();
		} else {
			String userSession = (String)session.getAttribute("noneMember");
			if(userSession != null) {
				vo.setUserSession(userSession);
				userNm = "비회원";
			} else {
				result.put("resultCode", "9999");
				result.put("resultMsg", "정보가 만료되었습니다.");
				result.put("returnUrl","/cart");
				return new CommonResponse<Map<String, Object>>(result);
			}
		}
		vo.setRegUser(userNm);
		vo.setUserNo(userNo);
//		vo.setOrderStatus(GlobalVariable.ORDER_STATUS_WAIT); 								//주문상태-주문대기
		
		OrderVO order = orderService.selectOrderApplyInfo(vo);
		if (order == null) {
			result.put("resultCode", "9999");
			result.put("resultMsg", "주문정보가 없습니다.");
			result.put("returnUrl","/cart");
		} else if ("0000".equals(order.getOrderStatus())) {
			result.put("resultCode", "9999");
			result.put("resultMsg", "이미 결제 완료된 주문입니다.");
			result.put("returnUrl","/cart");
		} else {
			try {
				vo.setOrderPhone(EncryptUtils.AES256_Encrypt(vo.getOrderPhone()));
				vo.setReceivePhone(EncryptUtils.AES256_Encrypt(vo.getReceivePhone()));
				if(!"".equals(vo.getOrderTel()) && vo.getOrderTel() != null){
					vo.setOrderTel(EncryptUtils.AES256_Encrypt(vo.getOrderTel()));
				}
				if(!"".equals(vo.getReceiveTel()) && vo.getReceiveTel() != null){
					vo.setReceiveTel(EncryptUtils.AES256_Encrypt(vo.getReceiveTel()));
				}
				vo.setOrderPwd(EncryptUtils.AES256_Encrypt(vo.getOrderPwd()));
				vo.setOrderStatus(GlobalVariable.ORDER_STATUS_WAIT); 								//주문상태-주문대기 (조건)
				orderService.updateOrderApplyInfo(vo);
				result.put("resultCode", "SUCCESS");
				
				int totalAmount = 0;
				if("1000".equals(vo.getPackaging())) totalAmount += GlobalVariable.PACKING_AMT;
				if("2000".equals(vo.getDelivKind())) {
					totalAmount += GlobalVariable.DELY_OFFICE_AMT;
				} else {
					totalAmount += order.getTotalDelyAmt();
				}
				totalAmount += order.getTotalAmount();
//				System.out.println("totalAmount:"+totalAmount);
				result.put("TOTAL_AMOUNT", totalAmount);
			}catch(Exception e) {
				log.error(e.getMessage());
				result.put("resultCode", "9999");
				result.put("resultMsg", "오류가 발생하였습니다.");
				result.put("returnUrl","/cart");
			}
		}
		return new CommonResponse<Map<String, Object>>(result);
	}
	
	/**
	 * 비회원 주문서조회
	 * 
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/getOrderAjax")
	public BasicResponse getOrderAjax(Model model, HttpSession session, @ModelAttribute("orderVO") OrderVO vo) throws Exception {
		
		Map<String, Object> returnData = new HashMap<String, Object>();
		String orderPwd = vo.getOrderPwd();
		if(orderPwd != null) {
			vo.setOrderPwd(EncryptUtils.AES256_Encrypt(orderPwd));
		} else {
			returnData.put("resultCode", "FAIL");
			return new CommonResponse<Map<String, Object>>(returnData);
		}
		OrderVO order = orderService.selectOrderCheck(vo);
		if(order == null) returnData.put("resultCode", "FAIL");
		else returnData.put("resultCode", "SUCCESS");
		
		return new CommonResponse<Map<String, Object>>(returnData);
	}
	//주문 결제 취소
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/payCancelAjax")
	public BasicResponse payCancelAjax(@RequestParam Map<String, Object> paramMap) throws ParseException {
		
		JSONObject returnData = new JSONObject();
		
		OrderVO vo = new OrderVO();
		vo.setOrderNo((String)paramMap.get("orderNo"));
		OrderVO returnVO = orderService.selectOrderApplyInfo(vo);
		if("0001".equals(returnVO.getOrderStatus())){
			returnData.put("code", "ERROR9999");
			returnData.put("message", "이미 취소된 주문입니다.");
			return new CommonResponse<JSONObject>(returnData);
		} else if(!"0000".equals(returnVO.getOrderStatus())){
			returnData.put("code", "ERROR9999");
			returnData.put("message", "취소할 수 없는 주문입니다..");
			return new CommonResponse<JSONObject>(returnData);
		}
		paramMap.put("cancelReason", "사용자 당일취소");
		paramMap.put("paymentKey",returnVO.getPaymentKey());
		returnData  = tossService.cancel(paramMap);
		if(returnData.get("cancels") != null) {
			paymentsService.getPayResultCancel(returnData);
		}
		return new CommonResponse<JSONObject>(returnData);
	}
	
	//환불요청
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/refundAjax")
	public BasicResponse refundAjax(@RequestParam Map<String, Object> paramMap) throws ParseException {
		
		JSONObject returnData = new JSONObject();
		
		String refundMsg = (String)paramMap.get("refundMsg");
		if(refundMsg == null) {
			returnData.put("code", "ERROR9999");
			returnData.put("message", "환불 사유를 입력해 주세요.");
			return new CommonResponse<JSONObject>(returnData);
		}
		
		OrderVO vo = new OrderVO();
		vo.setOrderNo((String)paramMap.get("orderNo"));
		OrderVO returnVO = orderService.selectOrderApplyInfo(vo);
		if("0001".equals(returnVO.getOrderStatus())){
			returnData.put("code", "ERROR9999");
			returnData.put("message", "이미 취소된 주문입니다.");
			return new CommonResponse<JSONObject>(returnData);
		} else if(!"0000".equals(returnVO.getOrderStatus())){
			returnData.put("code", "ERROR9999");
			returnData.put("message", "취소할 수 없는 주문입니다..");
			return new CommonResponse<JSONObject>(returnData);
		}
		RefundVO refund = new RefundVO();
		refund.setOrderNo((String)paramMap.get("orderNo"));
		refund.setRefundMsg((String)paramMap.get("refundMsg"));
		int a = orderService.insertRefund(refund);
		if(a == 9999) {
			returnData.put("code", "ERROR9999");
			returnData.put("message", "이미 환불 신청한 내역이 있습니다.");
		}else if(a > 0) {
			returnData.put("code", "0000");
			returnData.put("message", "환불 신청되었습니다.");
		}else {
			returnData.put("code", "ERROR9999");
			returnData.put("message", "고객센터에 문의해주세요.");
		}
		return new CommonResponse<JSONObject>(returnData);
	}
}
