package com.simplesoft.payments.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesoft.mapper.cart.CartMapper;
import com.simplesoft.mapper.payments.PaymentsMapper;
import com.simplesoft.payments.service.PaymentsService;
import com.simplesoft.payments.service.PaymentsVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class PaymentsServiceImple implements PaymentsService{
	
	@Autowired
	PaymentsMapper paymentsMapper;
	
	@Autowired
	CartMapper cartMapper;
	
	public String getPayResult(JSONObject param) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			PaymentsVO vo = mapper.readValue(param.toString(), PaymentsVO.class);
			vo.setReceipt(vo.getReceipt().toString());
			switch ((String)param.get("method")) {
				case "카드":
					
					break;
				case "가상계좌":
					
					break;
				case "간편결제":
					if(vo.getEasyPay() != null) {
						vo.setEasyPay(vo.getEasyPay().toString());
					}
					break;
				case "휴대폰":
					
					break;
				case "계좌이체":
					
					break;
				default:
					System.out.println("지원하지 않는 결제수단입니다.");
					break;
			}
			paymentsMapper.updatePayments(vo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		/*결제한 장바구니 목록 삭제*/
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] arrCartNo = String.valueOf(session.getAttribute("cartList")).split(",");
		paramMap.put("arrCartNo", arrCartNo);
		cartMapper.deleteCart(paramMap);
		
		return "";
	}
	@Override
	public int insertPayments(PaymentsVO vo) {
		return paymentsMapper.insertPayments(vo);
	}
	@Override
	public PaymentsVO selectPaymentsDetail(PaymentsVO vo) {
		return paymentsMapper.selectPaymentsDetail(vo);
	}
}
