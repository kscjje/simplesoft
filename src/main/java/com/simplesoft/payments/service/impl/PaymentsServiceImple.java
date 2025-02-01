package com.simplesoft.payments.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesoft.common.service.BizBatchVO;
import com.simplesoft.mapper.bizBatch.BizBatchMapper;
import com.simplesoft.mapper.cart.CartMapper;
import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.mapper.payments.PaymentsMapper;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.payments.service.PaymentsService;
import com.simplesoft.payments.service.PaymentsVO;
import com.simplesoft.util.EncryptUtils;
import com.simplesoft.util.MailTemplateSender;
import com.simplesoft.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentsServiceImple implements PaymentsService{
	
	@Autowired
	PaymentsMapper paymentsMapper;
	
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
	public void getPayResult(HttpServletRequest request, JSONObject param) {
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
			paymentsMapper.updatePayments(vo);

			// 주문완료 메일 전송
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
	
	@Override
	public int insertPayments(PaymentsVO vo) {
		return paymentsMapper.insertPayments(vo);
	}
	@Override
	public PaymentsVO selectPaymentsDetail(PaymentsVO vo) {
		return paymentsMapper.selectPaymentsDetail(vo);
	}
}
