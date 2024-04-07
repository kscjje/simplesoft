package com.simplesoft.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simplesoft.payments.service.PaymentsService;

@Controller
public class PaymentsController {

	@Autowired
	PaymentsService paymentsService;
	
	@RequestMapping(value = "/confirm")

	public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {
		 JSONParser parser = new JSONParser();
	        String orderId;
	        String amount;
	        String paymentKey;
	        try {
	            // 클라이언트에서 받은 JSON 요청 바디입니다.
	            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
	            paymentKey = (String) requestData.get("paymentKey");
	            orderId = (String) requestData.get("orderId");
	            amount = (String) requestData.get("amount");
	        } catch (ParseException e) {
	            throw new RuntimeException(e);
	        };
	        JSONObject obj = new JSONObject();
	        obj.put("orderId", orderId);
	        obj.put("amount", amount);
	        obj.put("paymentKey", paymentKey);
	            
	        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
	        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
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

	        paymentsService.getPayResult(jsonObject);
	        
	        return ResponseEntity.status(code).body(jsonObject);
	}
	/**
	 * 주문하기
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/success")
	public String success(Model model,@RequestParam Map<String, Object> paramMap) {
		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
		System.out.println("success:"+paramMap);
		int i = paymentsService.insertPayments(paramMap);
		System.out.println(i);
		return "/payments/success";
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
