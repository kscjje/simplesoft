package com.simplesoft.payments.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesoft.mapper.admOrder.AdmOrderMapper;
import com.simplesoft.mapper.bizBatch.BizBatchMapper;
import com.simplesoft.mapper.cart.CartMapper;
import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.mapper.payments.PaymentsMapper;
import com.simplesoft.payments.service.PaymentsService;
import com.simplesoft.payments.service.PaymentsVO;
import com.simplesoft.util.MailTemplateSender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentsServiceImple implements PaymentsService{
	
	@Autowired
	PaymentsMapper paymentsMapper;
	
	@Autowired
	AdmOrderMapper admOrderMapper;
	
	@Autowired
	BizBatchMapper bizBatchMapper;
	
	@Autowired
	CartMapper cartMapper;
	
	@Autowired 
	OrderMapper orderMapper;
	
	@Autowired
    private MailTemplateSender mailTemplateSender;
	
	//결제정보 저장 후 장바구니 내역 삭제
	@Override
	@Transactional
	public void getPayResult(HttpServletRequest request, JSONObject param) {
		try {
			// null 체크 추가
		    if (param == null) {
		        throw new IllegalArgumentException("결제 파라미터가 null입니다.");
		    }
			log.info("결제 정보 : {} ", param);
			
			ObjectMapper mapper = new ObjectMapper();
			PaymentsVO vo = mapper.readValue(param.toString(), PaymentsVO.class);
			
			// null 체크 및 안전한 변환
		    if (vo.getReceipt() != null) {
		        vo.setReceipt(vo.getReceipt().toString());
		    }
		    
		    // method 파라미터 null 체크 및 안전한 캐스팅
		    Object methodObj = param.get("method");
		    if (methodObj == null) {
		        throw new IllegalArgumentException("결제 방법이 지정되지 않았습니다.");
		    }
		    
		    String method = methodObj.toString().trim();
		    if (method.isEmpty()) {
		        throw new IllegalArgumentException("결제 방법이 비어있습니다.");
		    }
		    
		    // 열거형 사용을 위한 메서드 정규화
		    PaymentMethod paymentMethod = PaymentMethod.fromString(method);
		    switch (paymentMethod) {
		        case CARD:
		            if (vo.getCard() != null) {
		                vo.setCard(vo.getCard().toString());
		            }
		            break;
		            
		        case VIRTUAL_ACCOUNT:
		            if (vo.getVirtualAccount() != null) {
		                vo.setVirtualAccount(vo.getVirtualAccount().toString());
		            }
		            break;
		            
		        case EASY_PAY:
		            if (vo.getEasyPay() != null) {
		                vo.setEasyPay(vo.getEasyPay().toString());
		            }
		            // 간편결제의 경우 카드 정보도 함께 처리
		            if (vo.getCard() != null) {
		                vo.setCard(vo.getCard().toString());
		            }
		            break;
		            
		        case MOBILE_PHONE:
		            if (vo.getMobilePhone() != null) {
		                vo.setMobilePhone(vo.getMobilePhone().toString());
		            }
		            break;
		            
		        case TRANSFER:
		            if (vo.getTransfer() != null) {
		                vo.setTransfer(vo.getTransfer().toString());
		            }
		            break;
		            
		        default:
		            throw new UnsupportedOperationException("지원하지 않는 결제수단입니다: " + method);
		    }
		    validatePaymentData(vo);
		    
		    int updateResult = paymentsMapper.updatePayments(vo);
		    if (updateResult == 0) {
		        throw new RuntimeException("결제 정보 업데이트에 실패했습니다.");
		    }

			paymentsMapper.updatePayments(vo);

			// 주문완료 메일 전송
			/*
			OrderVO order = new OrderVO();
			order.setOrderNo((String)param.get("orderId"));
			order = orderMapper.selectOrderApplyInfo(order);
			if(order.getOrderEmail() != null) {
				Map<String, Object> mailMap = new HashMap<String, Object>();
				mailMap.put("orderName", order.getOrderName());
				mailMap.put("orderNo", order.getOrderNo());
				mailMap.put("orderEmail", order.getOrderEmail());
				mailMap.put("receiveName", order.getReceiveName());
				mailMap.put("receivePhone", EncryptUtils.AES256_Decrypt(order.getReceivePhone()));
				mailMap.put("receivePostNum", order.getReceivePostNum());
				mailMap.put("receiveAddr", order.getReceiveAddr());
				mailMap.put("receiveAddrDetail", order.getReceiveAddrDetail());
				mailMap.put("bigo", order.getBigo());
				//mailTemplateSender.orderCompleteMail(request, mailMap);
			}
			// 메세지 발송
			Map<String, Object> messageMap = new HashMap<String, Object>();
			messageMap.put("msgCd", "1001");
			Map<String, Object> message = bizBatchMapper.selectBizMessage(messageMap);
			
			BizBatchVO bizVO = new BizBatchVO();
			bizVO.setMsgType("at");
			bizVO.setDestTel(EncryptUtils.AES256_Decrypt(order.getOrderPhone()).replace("-", ""));
			bizVO.setDestNm(order.getOrderName());
			bizVO.setSendTel(String.valueOf(message.get("sendTel")));
			bizVO.setSendNm("밥수니반찬");
			bizVO.setSubject(String.valueOf(message.get("msgNm")));
			
			String msg = String.valueOf(message.get("sendMsg"));
			msg = msg.replace("#{주문자명}", order.getOrderName()).
					replace("#{주문번호}",order.getOrderNo()).
					replace("#{식단정보}",vo.getOrderName()).
					replace("#{결제금액}",String.valueOf(StringUtils.comma(vo.getTotalAmount()))).
					replace("#{문의전화}",String.valueOf(message.get("sendTel"))).
					replace("#{callbackUrl}","https://babsooni.shop/mypage/noneOrderCheck");
			bizVO.setSendMsg(msg);
			
			bizVO.setSenderKey("a66d373de515818295b262a6030d72430b29ecf0");
			bizVO.setTemplateCode(String.valueOf(message.get("kkoMessageTemplateId")));
			bizVO.setReType("Y");
			bizVO.setReBody(msg);
			bizVO.setRefkey("babsooni");
			bizVO.setStatus("0000");
			System.out.println("MSG:");
			System.out.println(msg);
			bizBatchMapper.insertBizBatch(bizVO);
			log.info("메세지 결과코드: {} ",bizVO.getRetCode());
			log.info("메세지 결과메세지: {} ",bizVO.getRetMsg());
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		/*결제한 장바구니 목록 삭제*/
		HttpSession session = request.getSession();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] arrCartNo = String.valueOf(session.getAttribute("cartList")).split(",");
		paramMap.put("arrCartNo", arrCartNo);
		cartMapper.deleteCart(paramMap);
	}
	
	// 결제 데이터 유효성 검증 메서드
	private void validatePaymentData(PaymentsVO vo) {
	    if (vo == null) {
	        throw new IllegalArgumentException("결제 정보가 null입니다.");
	    }
	    
	    // 필수 필드 검증 (예시)
	    if (vo.getPaymentKey() == null || vo.getPaymentKey().trim().isEmpty()) {
	        throw new IllegalArgumentException("결제 ID가 누락되었습니다.");
	    }
	    
	    if (vo.getTotalAmount() <= 0) {
	        throw new IllegalArgumentException("결제 금액이 유효하지 않습니다.");
	    }
	    
	    // 추가 검증 로직...
	}
	
	// 결제 방법 열거형
	public enum PaymentMethod {
	    CARD("카드"),
	    VIRTUAL_ACCOUNT("가상계좌"),
	    EASY_PAY("간편결제"),
	    MOBILE_PHONE("휴대폰"),
	    TRANSFER("계좌이체");
	    
	    private final String koreanName;
	    
	    PaymentMethod(String koreanName) {
	        this.koreanName = koreanName;
	    }
	    
	    public static PaymentMethod fromString(String method) {
	        for (PaymentMethod pm : PaymentMethod.values()) {
	            if (pm.koreanName.equals(method)) {
	                return pm;
	            }
	        }
	        throw new IllegalArgumentException("지원하지 않는 결제수단: " + method);
	    }
	    
	    public String getKoreanName() {
	        return koreanName;
	    }
	}
	
	@Override
	public int insertPayments(PaymentsVO vo) {
		return paymentsMapper.insertPayments(vo);
	}
	@Override
	public int insertPaymentsCancel(PaymentsVO vo) {
		return paymentsMapper.insertPaymentsCancel(vo);
	}
	@Override
	public PaymentsVO selectPaymentsDetail(PaymentsVO vo) {
		return paymentsMapper.selectPaymentsDetail(vo);
	}
	@Override
	public void getPayResultCancel(JSONObject param) {
		log.info("결제 정보 : {} ", param);
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			PaymentsVO vo = mapper.readValue(param.toString(), PaymentsVO.class);
			vo.setReceipt(vo.getReceipt().toString());
			switch ((String)param.get("method")) {
				//String으로 변환하여 담기
				case "카드":
					if(vo.getCard() != null) {
						vo.setCard(vo.getCard().toString());
					}
					break;
				case "가상계좌":
					if(vo.getVirtualAccount() != null) {
						vo.setVirtualAccount(vo.getVirtualAccount().toString());
					}
					break;
				case "간편결제":
					if(vo.getEasyPay() != null) {
						vo.setEasyPay(vo.getEasyPay().toString());
					}
					if(vo.getCard() != null) {
						vo.setCard(vo.getCard().toString());
					}
					break;
				case "휴대폰":
					if(vo.getMobilePhone() != null) {
						vo.setMobilePhone(vo.getMobilePhone().toString());
					}
					break;
				case "계좌이체":
					if(vo.getTransfer() != null) {
						vo.setTransfer(vo.getTransfer().toString());
					}
					break;
				default:
					System.out.println("지원하지 않는 결제수단입니다.");
					break;
			}
			vo.setCancels(vo.getCancels().toString());
			paymentsMapper.insertPaymentsCancel(vo);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("orderNo",vo.getOrderId());
			admOrderMapper.updateOrderCancel(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
