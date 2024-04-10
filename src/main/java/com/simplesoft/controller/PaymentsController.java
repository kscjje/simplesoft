package com.simplesoft.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.payments.service.PaymentsService;
import com.simplesoft.payments.service.PaymentsVO;
import com.simplesoft.reponse.BasicResponse;
import com.simplesoft.reponse.CommonResponse;
import com.simplesoft.util.GlobalVariable;
import com.simplesoft.util.MailTemplateSender;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PaymentsController {

	@Autowired
	PaymentsService paymentsService;
	
	@Autowired 
	OrderMapper orderMapper;
	
	@ResponseBody
	@PostMapping(value = "/confirm")
	public BasicResponse confirmPayment(HttpServletRequest request, @RequestBody String jsonBody) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JSONParser parser = new JSONParser();
		String orderId;
		String amount;
		String paymentKey;
		
		try {
			//클라이언트에서 받은 JSON 요청 바디입니다.
			JSONObject requestData = (JSONObject) parser.parse(jsonBody);
			paymentKey = (String) requestData.get("paymentKey");
			orderId = (String) requestData.get("orderId");
			amount = (String) requestData.get("amount");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("orderId", orderId);
		obj.put("amount", amount);
		obj.put("paymentKey", paymentKey);
	            
		//토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
		//비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
		String widgetSecretKey = "test_sk_0Poxy1XQL8RAj15OWvNr7nO5Wmlg";
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		// 결제를 승인하면 결제수단에서 금액이 차감돼요.
		URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", authorizations);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(obj.toString().getBytes("UTF-8"));

		int code = connection.getResponseCode();
		boolean isSuccess = code == 200;

		InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

		// 결제 성공 및 실패 비즈니스 로직을 구현하세요.
		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONObject jsonObject = (JSONObject) parser.parse(reader);
		responseStream.close();

		//결제 성공 비지니스
		paymentsService.getPayResult(request, jsonObject);
		
		result.put("resultCode", "SUCCESS");
		return new CommonResponse<Map<String, Object>>(result);
	}
	/**
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/success")
	public String success(Model model,
			@RequestParam String paymentKey,
			@RequestParam String orderId,
			@RequestParam String paymentType,
			@RequestParam int amount) {
		
		PaymentsVO vo = new PaymentsVO();
		vo.setOrderId(orderId);
		vo.setPaymentKey(paymentKey);
		vo.setPaymentType(paymentType);
		vo.setAmount(amount);
		
		//중복 결제 키 여부 조회
		if(paymentsService.selectPaymentsDetail(vo) == null) {
			//요청한 금액과 실결제 해야할 금액이 같은지 확인
			int totalMoney = orderMapper.selectOrderPayConfirm(orderId);
			if(totalMoney == amount) {
				//결제 키 저장
				if(paymentsService.insertPayments(vo) > 0) {
					return "/payments/success";
				} else {
					model.addAttribute("PARAM_MESSAGE", "결제 중 오류가 발생하였습니다.");
					model.addAttribute("returnUrl", "/main");
					return GlobalVariable.REDIRECT_SUBMIT;
				}
			} else {
				model.addAttribute("PARAM_MESSAGE", "결제 금액이 올바르지 않습니다.");
				model.addAttribute("returnUrl", "/main");
				return GlobalVariable.REDIRECT_SUBMIT;
			}
		} else {
			model.addAttribute("PARAM_MESSAGE", "이미 결제된 상품입니다.");
			model.addAttribute("returnUrl", "/main");
			return GlobalVariable.REDIRECT_SUBMIT;
		}
		
	}
	
	/**
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/fail")
	public String fail(Model model,@RequestParam Map<String, Object> paramMap) {
		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
		System.out.println("fail:"+paramMap);
		
		return "/payments/fail";
	}
}
