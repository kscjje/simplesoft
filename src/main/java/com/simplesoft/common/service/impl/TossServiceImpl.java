package com.simplesoft.common.service.impl;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.simplesoft.common.service.PaymentVO;
import com.simplesoft.common.service.TossPaymentVO;
import com.simplesoft.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
/**
 *  참고URL
 *  결제 관련 : https://docs.tosspayments.com/reference
 *	웹훅 관련 : https://docs.tosspayments.com/guides/webhook
 * 
 * @author kyc
 *
 */
@Slf4j
@Service("TossServiceImpl")
public class TossServiceImpl {
	
	/**
	 * (공통) 웹훅 이벤트 관련 Service API 호출
	 * 참고 URL : https://docs.tosspayments.com/guides/webhook#payment_status_changed
	 * API 버전 : 2022-11-16
	 * @param  각 이벤트에서 정의
	 * @return
	 */
	@Value("${secretKey}")
	String auth;
	
	@Transactional
	public Map<String,Object> returnEventService(String comcd, String dbcomcd, Map<String,Object> paramMap) {
		TossPaymentVO p = new TossPaymentVO();
		Map<String,Object> responseMap = new HashMap<String, Object>();		//API 결과
		
		try {
			String eventType = (String)paramMap.get("eventType"); 		//return받은 Event Type
			String secret = (String)paramMap.get("secret"); 			//return받은 Event Type
			if(eventType == null && secret != null) {
				eventType = "DEPOSIT_CALLBACK";
			}
			
			if(!"DONE".equals(paramMap.get("status"))) {	
				responseMap.put("resultMessage","");
				responseMap.put("resultCode","0000");		
			}
			switch (eventType) {
			
				case "PAYMENT_STATUS_CHANGED":
					/* 결제 상태 변경 이벤트입니다. 모든 결제 수단(카드, 계좌이체, 휴대폰, 상품권)에 사용 가능합니다.
					 * parameter -----------------------
						eventType		(string): 웹훅 이벤트 타입입니다.
						createdAt 		(string): 웹훅이 생성된 시간입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss.SSSSSS 사용합니다.
						data			(obj)	: 상태가 변경된 Payment 객체입니다.
					 * STATUS 상태--------------------
					 * 유효 시간(30분) 안에 고객이 결제창에서 인증을 하지 않거나, 고객의 결제 인증 이후 상점에서 결제 승인 API를 호출하지 않으면 결제 상태가 EXPIRED로 변경됩니다.
					 * 고객이 결제창을 닫으면 결제 상태가 바뀌지 않기 때문에 웹훅도 전송되지 않습니다.
						DONE   				: 승인성공
						EXPIRED				: 유효기간만료
						ABORTED				: 승인실패
						CANCELED 			: 결제취소
						PARTIAL_CANCELED 	: 결제부분취소
					 * -------------------------------
					 */
					log.info(eventType+"");
					
					break;
					
				case "DEPOSIT_CALLBACK":
					
					/* 가상계좌 입금 및 입금 취소 이벤트
					 * parameter -----------------------
						createdAt		(string): 웹훅이 생성된 시간입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss.SSSSSS 사용합니다.
						secret			(string): 가상계좌 웹훅 요청이 정상적인 요청인지 검증하는 값입니다. 결제 승인 API의 응답으로 돌아온 secret과 같으면 정상적인 요청입니다.
						status 			(string): 결제 상태입니다.
						transactionKey	(string): 상태가 변경된 가상계좌 거래를 특정하는 키입니다.
						orderId 		(string): 주문 ID입니다.
					 * STATUS 상태--------------------
						DONE    				: 입금완료
						WAITING_FOR_DEPOSIT  	: 가상계좌 발급
						CANCELED 				: 결제취소
						PARTIAL_CANCELED 		: 결제부분취소
					 * -------------------------------
					 */
					
					log.info(eventType+"");
					//입금완료 처리
					if("DONE".equals(paramMap.get("status"))) {	
						JSONParser jsonParser = new JSONParser();

						PaymentVO paymentVO = new PaymentVO();
						paymentVO.setOid(com.simplesoft.util.StringUtils.isNullToString(paramMap.get("orderId")));
						paymentVO.setSecret(com.simplesoft.util.StringUtils.isNullToString(paramMap.get("secret")));
						paymentVO.setApprovedAt(com.simplesoft.util.StringUtils.isNullToString(paramMap.get("createdAt")));
						
						//EgovMap map = payRsvnDAO.selectVbankInfoKeyByOid(paymentVO);						
						//paymentVO.setAuthKey(com.simplesoft.util.StringUtils.isNullToString(map.get("authKey")));
						// 주문정보 toss 에서 호출
						JSONObject responseJsonObj = paymentQueryOrderId(paymentVO);
						
						paymentVO.setMid(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("mId")));
						
						//정상적으로 오지 않았으면 오류 처리
						if(paymentVO.getMid().equals("")) {
						//	paymentVO.setRetCode(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("code")));
						//	paymentVO.setRetMsg(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("message")));						
						}else {
							paymentVO.setLastTransactionKey(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("lastTransactionKey")));
							paymentVO.setRequestedAt(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("requestedAt")));
							paymentVO.setApprovedAt(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("approvedAt")));
							paymentVO.setPayDate(paymentVO.getApprovedAt().substring(0,10).replaceAll("-", ""));
							paymentVO.setPayTime(paymentVO.getApprovedAt().toString().substring(11,19).replaceAll(":", ""));
							
							paymentVO.setPaymentKey(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("paymentKey")));
							paymentVO.setStatus(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("status")));
							paymentVO.setOid(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("orderId")));
							paymentVO.setPayAmt(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("totalAmount")));
							paymentVO.setAmount(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("totalAmount")));
							
							System.out.println("setPayAmt : "+paymentVO.getPayAmt());
							System.out.println("totalAmount : "+responseJsonObj.get("totalAmount"));
							
							paymentVO.setType(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("type")));
							paymentVO.setTotalAmount(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("totalAmount")));
							paymentVO.setBalanceAmount(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("balanceAmount")));
							paymentVO.setSuppliedAmount(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("suppliedAmount")));
							paymentVO.setVat(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("vat")));
							paymentVO.setTaxFreeAmount(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("taxFreeAmount")));
							paymentVO.setMethod(com.simplesoft.util.StringUtils.isNullToString(responseJsonObj.get("method")));
							
							Object bankObj = jsonParser.parse(responseJsonObj.get("virtualAccount").toString());
							JSONObject bankJsonObj = (JSONObject) bankObj;				
							paymentVO.setBankCode(com.simplesoft.util.StringUtils.isNullToString(bankJsonObj.get("bankCode")));
							paymentVO.setInstallmentPlanMonths("0");
							paymentVO.setIssuerCode(com.simplesoft.util.StringUtils.isNullToString(bankJsonObj.get("bankCode")));
							//paymentVO.setApproveNo("00000000");
							paymentVO.setApproveNo(paymentVO.getLastTransactionKey()); // 승인번호는 TID 로 한다
							//payRsvnDAO.OnlinePayProcess(paymentVO);
							//payRsvnDAO.updateVbankInfo(paymentVO);
						}
						//responseMap.put("resultMessage",paymentVO.getRetMsg());
						//responseMap.put("resultCode",paymentVO.getRetCode());						
					}
					
					break;
					
				case "PAYOUT_STATUS_CHANGED":
						
					/* 서브몰 지급대행 성공 또는 실패 이벤트
					 * parameter -----------------------
						eventType		(string): 웹훅 이벤트 타입입니다.
						createdAt 		(string): 웹훅이 생성된 시간입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss.SSSSSS 사용합니다.
						data			(obj)	: 상태가 변경된 Payment 객체입니다.
					 * STATUS 상태--------------------
						REQUESTED   : 지급이 요청된 상태입니다.
						COMPLETED  	: 서브몰에 지급이 완료된 상태입니다.
						FAILED 		: 지급 요청이 실패한 상태입니다.
						CANCELED 	: 지급 요청을 취소한 상태입니다.
					 * -------------------------------
					 */
					log.info(eventType+"");
					
					break;
					
				case "METHOD_UPDATED":
					/* 브랜드페이 고객 결제 수단 변경 이벤트트
					 * parameter -----------------------
						eventType		(string): 웹훅 이벤트 타입입니다.
						createdAt 		(string): 웹훅이 생성된 시간입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss.SSSSSS 사용합니다.
						data			(obj)	: 아래 세 가지 필드가 돌아옵니다.
						 - customerKey	: 상점에서 만든 고객의 고유 ID입니다.
						 - methodKey	: 결제수단을 특정하는 키입니다.
						 - status		: 결제수단의 상태입니다.
					 * STATUS 상태--------------------
					 	ENABLED 			: 결제 수단이 등록되어 사용할 수 있게 된 상태
					 	DISABLED			: 결제 수단이 삭제되어 사용할 수 없게 된 상태
					 	ALIAS_UPDATED	: 등록되어 있는 결제 수단의 별명이 변경된 상태
					 * -------------------------------
					 */
					log.info(eventType+"");
					
					break;
					
				case "CUSTOMER_STATUS_CHANGED":
					/* 브랜드페이 고객 상태 변경 이벤트
					 * parameter -----------------------
						eventType		(string): 웹훅 이벤트 타입입니다.
						createdAt 		(string): 웹훅이 생성된 시간입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss.SSSSSS 사용합니다.
						data			(obj)	: 아래 세 가지 필드가 돌아옵니다.
						 - customerKey	: 상점에서 만든 고객의 고유 ID입니다.
						 - status		: 결제수단의 상태입니다.
						 - changedAt	: 변경이 일어난 시점입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm를 사용합니다.
					 * STATUS 상태--------------------
					 * CREATED 					: 간편결제에 가입한 상태
					 * REMOVED					: 간판결제에 탈퇴한 상태
					 * PASSWORD_CHANGED			: 비밀번호를 변경한상태
					 * ONE_TOUCH_ACTIVATED		: 원터치결제를   활성화한 상태
					 * ONE_TOUCH_DEACTIVATED	: 원터치결제를 비활성화한 상태
					 * -------------------------------
					 */
					log.info(eventType+"");
					
					break;
					
					
				default:
					log.error("알 수 없는 오류:"+eventType);
			}
		} catch (Exception e) {
			responseMap.put("resultCode","ERROR");
			responseMap.put("resultMessage",e.getMessage());
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	/**
	 * (공통) 결제승인
	 * @param paymentKey	(string)(필수): 결제의 키 값입니다. 최대 길이는 200자입니다.
	 * @param orderId		(string)(필수): 주문 ID입니다. 충분히 무작위한 값을 직접 생성해서 사용하세요. 영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열이어야 합니다.
	 * @param amount		(number)(필수): 결제할 금액입니다.
	 * 		
	 * @return 결제 승인에 성공했다면 Payment 객체 / 결제 승인에 실패했다면 HTTP 상태 코드와 함께 에러 객체
	 */
	public JSONObject getConfirm(Map<String, Object> param) throws ParseException {
		log.debug("getConfirm():"+param);
		JSONObject jsonObject = getJsonStringFromMap(param);

		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
			    .header("Authorization", "Basic "+new String(Base64.encodeBase64(auth.getBytes())))
			    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString(jsonObject.toJSONString()))
			    .build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONParser jsonParser = new JSONParser();
		//3. To Object
		Object obj = jsonParser.parse(response.body());
		//4. To JsonObject
		JSONObject jsonObj = (JSONObject) obj;
		
		log.debug("response :"+response.body());
		return jsonObj;
	}
	
	/**
	 * (공통) 결제취소
	 * @param cancelReason(string)(필수)			: 취소사유
	 * @param cancelAmount(number)(선택)			: 취소금액-미입력시 전체환불
	 * @param refundReceiveAccount(Obj)(선택)	: 결제 취소 후 금액이 환불될 계좌의 정보입니다. 가상계좌 결제에만 필수입니다. 다른 결제수단으로 이루어진 결제를 취소할 때는 사용하지 않습니다.
	 * 			-bank         (string)(필수)		: 취소 금액을 환불받을 계좌의 은행 코드입니다. 은행 코드를 참고하세요.
	 * 			-accountNumber(string)(필수)		: 취소 금액을 환불받을 계좌의 계좌 번호 입니다. - 없이 숫자만 넣어야 합니다. 최대 길이는 20자입니다.
	 * 			-holderName   (string)(필수)		: 취소 금액을 환불받을 계좌의 예금주입니다. 최대 길이는 60자입니다.
	 * @param taxFreeAmount   (number)(선택)		: 취소할 금액 중 면세 금액입니다. 값을 넣지 않으면 기본값인 0으로 설정됩니다. (면세 상점 혹은 복합 과세 상점일 때만 설정한 금액이 적용되고, 일반 과세 상점에는 적용되지 않습니다.)(선택)
	 * 		
	 * @return 결제 취소에 성공했다면 Payment 객체의 cancels 필드에 취소 객체가 배열 / 결제 취소에 실패했다면 HTTP 상태 코드와 함께 에러 객체
	 */
	public JSONObject cancel(Map<String, Object> param) throws ParseException {
		log.info("결제취소 요청 : {}",param);
		JSONObject jsonObject = getJsonStringFromMap(param);
		
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+(String)param.get("paymentKey")+"/cancel"))
			    .header("Authorization", "Basic "+new String(Base64.encodeBase64(auth.getBytes())))
			    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
			    .method("POST", HttpRequest.BodyPublishers.ofString(jsonObject.toJSONString()))
			    .build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONParser jsonParser = new JSONParser();
		//3. To Object
		Object obj = jsonParser.parse(response.body());
		//4. To JsonObject
		JSONObject jsonObj = (JSONObject) obj;
		
		log.info("결제취소 응답 : "+response.body());
		return jsonObj;
	}
	
	/**
	 * (payment) 결제조회
	 * @param paymentKey(string)(필수)	: 결제의 키 값입니다. 최대 길이는 200자입니다.
	 * 		
	 * @return 결제 조회에 성공했다면 Payment 객체 / 결제 취소에 실패했다면 HTTP 상태 코드와 함께 에러 객체
	 */
	public JSONObject paymentQueryKey(String paymentKey) throws ParseException {
		
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+paymentKey))
			    .header("Authorization", "Basic "+new String(Base64.encodeBase64(auth.getBytes())))
			    .header("Content-Type", "application/json")
			    .method("GET", HttpRequest.BodyPublishers.noBody())
			    .build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONParser jsonParser = new JSONParser();
		//3. To Object
		Object obj = jsonParser.parse(response.body());
		//4. To JsonObject
		JSONObject jsonObj = (JSONObject) obj;
		
		log.info("response :"+response.body());
		return jsonObj;
	}
	
	/**
	 * (orderId) 결제조회
	 * @param orderId(string)(필수)	: 주문 ID입니다. 충분히 무작위한 값을 직접 생성해서 사용하세요. 영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열이어야 합니다.
	 * 		
	 * @return 결제 조회에 성공했다면 Payment 객체 / 결제 취소에 실패했다면 HTTP 상태 코드와 함께 에러 객체
	 */
	public JSONObject paymentQueryOrderId(PaymentVO paymentVO) throws ParseException {
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/payments/orders/"+paymentVO.getOid()))
				.header("Authorization", "Basic "+new String(Base64.encodeBase64(paymentVO.getAuthKey().getBytes())))
				.header("Content-Type", "application/json")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONParser jsonParser = new JSONParser();
		//3. To Object
		Object obj = jsonParser.parse(response.body());
		//4. To JsonObject
		JSONObject jsonObj = (JSONObject) obj;
		log.info("response :"+response.body());
		return jsonObj;
	}
	
	public JSONObject paymentQueryOrderId(String orderID) throws ParseException {	
		PaymentVO paymentVO = new PaymentVO();
		paymentVO.setOid(orderID);
		paymentVO.setAuthKey(auth);
		
		return paymentQueryOrderId(paymentVO);
	}
	
	/**
	 * 거래내역 조회
	 * @param startDate			(string)(필수)	: 조회를 시작하고 싶은 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'hh:mm:ss를 사용합니다. 날짜 정보만 입력하면 시간은 자동으로 00:00:00으로 설정됩니다. (e.g. 2022-01-01T00:00:00)
	 * @param endDate			(string)(필수)	: 조회를 마치고   싶은 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'hh:mm:ss를 사용합니다. 날짜 정보만 입력하면 시간은 자동으로 00:00:00으로 설정됩니다. (e.g. 2022-01-02T23:59:59)
	 * @param startingAfter 	(string)(선택)	: 특정 결제 건 이후의 기록을 조회할 때 사용합니다. transactionKey 값을 전달합니다. 많은 양의 기록을 페이지 단위로 나누어 처리할 때 사용할 수 있습니다.
	 * @param limit  			(string)(선택)	: 한 번에 응답받을 기록의 개수입니다. 기본값은 100이고 설정할 수 있는 최대값은 10000입니다.
	 * 		
	 * @return 결제 조회에 성공했다면  Transaction 객체 / 결제 취소에 실패했다면 HTTP 상태 코드와 함께 에러 객체
	 */
	public Object paymentQueryDate(Map<String, Object> param) throws ParseException {

		String startDate = (String)param.get("startDate");
		String endDate = (String)param.get("endDate");
		String parameter = "";
		parameter += StringUtils.hasText((String)param.get("startingAfter")) ? "&startingAfter="+(String)param.get("startingAfter") : ""; 
		parameter += StringUtils.hasText((String)param.get("limit")) ? "&limit="+Integer.parseInt((String)param.get("limit")) : "";
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/transactions?startDate="+startDate+"&endDate="+endDate+parameter))
				.header("Authorization", "Basic "+new String(Base64.encodeBase64(auth.getBytes())))
				.header("Content-Type", "application/json")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		log.info("response :"+response.body());
		Object obj = new Object();
		try {
			JSONParser jsonParser = new JSONParser();
			obj = jsonParser.parse(response.body());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	/**
	 * 카드번호로 카드결제(개발용)
	 */
	public JSONObject getCardNumber(Map<String, Object> param) throws ParseException {
		log.debug("getVbankSuc():"+param);
		JSONObject jsonObject = getJsonStringFromMap(param);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/payments/key-in"))
				.header("Authorization", "Basic "+new String(Base64.encodeBase64(auth.getBytes())))
				.header("Content-Type", "application/json")
				.method("POST", HttpRequest.BodyPublishers.ofString(jsonObject.toJSONString()))
			    .build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONParser jsonParser = new JSONParser();
		//3. To Object
		Object obj = jsonParser.parse(response.body());
		//4. To JsonObject
		JSONObject jsonObj = (JSONObject) obj;
		
		log.debug("response :"+response.body());
		return jsonObj;
	}

	/**
	 * 카드 자동결제 승인요청
	 * @param billingKey			(string)(필수): 발급된 빌링키 정보입니다. 고객의 결제 정보로 사용됩니다.
	 * @param amount				(int)	(필수): 결제할 금액입니다.
	 * @param customerKey			(string)(필수): 고객 ID입니다. 충분히 무작위한 값을 직접 생성해서 사용하세요. 빌링키와 연결됩니다. 영문 대소문자, 숫자, 특수문자 -, _, =, ., @로 이루어진 최소 2자 이상 최대 300자 이하의 문자열입니다.
	 * @param orderId  				(string)(필수): 주문 ID입니다. 충분히 무작위한 값을 직접 생성해서 사용하세요. 영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열이어야 합니다.
	 * @param orderName  			(string)(필수): 주문명입니다. 예를 들면 생수 외 1건 같은 형식입니다. 최대 길이는 100자입니다.
	 * @param customerEmail 		(string)(옵션): 고객의 이메일 주소입니다. 결제 결과를 알려줄 때 사용합니다. 최대 길이는 100자입니다.
	 * @param customerName  		(string)(옵션): 고객 이름입니다. 최대 길이는 100자입니다.
	 * @param customerMobilePhone   (string)(옵션): 고객의 휴대폰 번호입니다. 가상계좌 입금 안내가 전송되는 번호입니다.
	 * @param taxFreeAmount    		(int)	(옵션): 면세 금액입니다. 값을 넣지 않으면 기본값인 0으로 설정됩니다. 면세 상점 혹은 복합 과세 상점일 때만 설정한 금액이 적용되고, 일반 과세 상점에는 적용되지 않습니다.
	 * @param cardInstallmentPlan 	(int)	(옵션): 할부 개월 수입니다. 값은 2부터 12까지 사용할 수 있습니다. 0이 들어가면 할부가 아닌 일시불로 결제됩니다. 값을 넣지 않으면 일시불입니다.
	 * 		
	 * @return 카드 자동결제 승인에 성공했다면 card 필드에 값이 있는 Payment 객체 / 카드 자동결제 승인에 실패했다면 HTTP 상태 코드와 함께 에러 객체
	 */
	public JSONObject cardAutoPayConfirm(Map<String, Object> param) throws ParseException {
		
		JSONObject jsonObject = getJsonStringFromMap(param);
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/billing/"+(String)param.get("billingKey")))
				.header("Authorization", "Basic "+new String(Base64.encodeBase64(auth.getBytes())))
				.header("Content-Type", "application/json")
				.method("POST", HttpRequest.BodyPublishers.ofString(jsonObject.toJSONString()))
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONParser jsonParser = new JSONParser();
		//3. To Object
		Object obj = jsonParser.parse(response.body());
		//4. To JsonObject
		JSONObject jsonObj = (JSONObject) obj;
		
		log.debug("response :"+response.body());
		return jsonObj;
	}
	
	/**
     * 서버 실제 경로 찾기
     *
     * @return
     */
    public static String GetRealRootPath() {
        return DateUtil.class.getResource("").getPath().substring(1, DateUtil.class.getResource("").getPath().lastIndexOf("rest_api"));

    }
    
	/**
	 * Map -> Json 변환
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonStringFromMap( Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        
        return jsonObject;
    }
	
}
