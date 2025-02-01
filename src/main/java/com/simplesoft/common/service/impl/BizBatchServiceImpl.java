package com.simplesoft.common.service.impl;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.common.service.BizBatchService;
import com.simplesoft.common.service.BizBatchVO;
import com.simplesoft.mapper.bizBatch.BizBatchMapper;
import com.simplesoft.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BizBatchServiceImpl implements BizBatchService {
	
	@Autowired
	BizBatchMapper bizBatchMapper;
	
	private String url;
	@Override
	public void updateBizBatchReport(Map<String, Object> params) {
		BizBatchVO bizBatchVO = new BizBatchVO();
		try {
			bizBatchVO = getParamSet(params);
			int i = bizBatchMapper.updateBizBatchReport(bizBatchVO);
			if (i < 1 ) {
				log.error("메세지 리포트결과 갱신 실패");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("메세지 리포트결과 갱신 중 오류:" +e.getMessage());
			
		}
	}
	/**
	 * 리포트 업데이트시 파라미터 세팅
	 * @param params
	 * @return
	 * @throws Exception
	 */
	
	private BizBatchVO getParamSet(Map<String, Object> params) throws Exception {
		BizBatchVO bizBatchVO = new BizBatchVO();
		
		bizBatchVO.setDevice(String.valueOf(params.get("DEVICE")));
		bizBatchVO.setCmsgid(String.valueOf(params.get("CMSGID")));
		bizBatchVO.setMsgid(String.valueOf(params.get("MSGID")));
		bizBatchVO.setPhone(String.valueOf(params.get("PHONE")));
		bizBatchVO.setMedia(String.valueOf(params.get("MEDIA")));
//		long unixtime = Long.valueOf((String)params.get("UNIXTIME"));
		String unixtime = String.valueOf(params.get("UNIXTIME"));
		bizBatchVO.setSendertime(DateUtil.getTimestampToDate(unixtime));
		bizBatchVO.setResultCd(String.valueOf(params.get("RESULT")));
		bizBatchVO.setWapinfo(String.valueOf(params.get("WAPINFO")));
		bizBatchVO.setUserdata(String.valueOf(params.get("USERDATA")));
		if(!"null".equals(String.valueOf(params.get("TELTIME")))) {
			String teltime = String.valueOf(params.get("TELTIME"));
			if(!"0".equals(teltime)) {
				bizBatchVO.setTelres(String.valueOf(params.get("TELRES")));
				bizBatchVO.setTeltime(DateUtil.getTimestampToDate(teltime));
			}
		}
		if(!"null".equals(String.valueOf(params.get("RETRY_FLAG")))) {
			bizBatchVO.setRetryFlag(String.valueOf(params.get("RETRY_FLAG")));
		}
		if(!"null".equals(String.valueOf(params.get("RESEND_FLAG")))) {
			bizBatchVO.setResendFlag(String.valueOf(params.get("RESEND_FLAG")));
		}
		
		return bizBatchVO;
		
	}
	
	/**
	 * @summary [인증토큰 발급]
	 * API 서비스를 이용하기 위해서 인증 토큰을 발급하기 위한 기능입니다.
	 * 인증 토큰의 유효 시간은 24시간이며, 이후에는 재발급이 필요합니다.
	 * Authorization 헤더에 비즈뿌리오 계정과 암호를 Base64 인코딩한 문자열을 입력합니다.
	 * Request {
	 * 		Content-Type : "application/json; charset=utf-8"
	 * 		Authorization : "Basic 계정:암호(Base64 인코딩)"
	 * }
	 * Response {
	 * 		accesstokent : Basic 계정:암호(Base64 인코딩)
	 * 		type : "application/json; charset=utf-8"
	 * 		expired : 토큰만료시점
	 * }
	 * @return JSONObject
	 * @throws ParseException
	 */
	public JSONObject getToken(byte[] author) throws ParseException {
		url = "https://api.bizppurio.com";
		
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create(url+"/v1/token"))
			    .header("Content-Type", "application/json; charset=utf-8")
			    .header("Authorization", "Basic "+new String(Base64.encodeBase64(author)))
			    .method("POST", HttpRequest.BodyPublishers.noBody())
			    .timeout(Duration.ofSeconds(15))
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
		
		
		return jsonObj;
	}
	
	
	/**
	 * @summary [메시지 발송]
	 * 메시지 발송을 요청하는 기능입니다.
	 * @param param
	 * @param header {
	 * 		type 		(String)	: application/json; charset=utf-8 
	 * 		accesstoken	(String)	: Basic 계정:암호(Base64 인코딩)
	 * 		expired		(String)	: 토큰 만료 시점
	 * }
	 * @return JSONObject
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public JSONObject SendMessage(Map<String, Object> param , Map<String, Object> header) throws ParseException {
		
//		url = "https://api.bizppurio.com";				//실제발송
		url ="https://dev-api.bizppurio.com";			//테스트
		
		JSONObject jsonParam = new JSONObject();
		JSONObject jsonParam2 = new JSONObject();
		JSONObject jsonParam3 = new JSONObject();
		
		String message = (String)param.get("sendMsg");
		String msgType = (String)param.get("msgType");
		String userinfo = (String)param.get("userinfo");
		
		//넘어온 파라미터에서 전송할 데이터만 추출하여 적재
		Map<String, Object> body = dataSet(param);
		
		if ("at".equals(msgType)) {
//			kakao 메세지(알림톡)
			jsonParam2.put("senderkey", (String)param.get("senderKey"));
			jsonParam2.put("templatecode", (String)param.get("templateCode"));
			jsonParam2.put("message", message);
			
			jsonParam.put(msgType, jsonParam2);
			body.put("userinfo",userinfo);
			body.put("content",jsonParam);
			
			if("Y".equals((String)param.get("reType"))) {
				
				jsonParam = new JSONObject();
				String reMsgType = message.getBytes().length > 90 ? "lms" : "sms";
				
				jsonParam.put("first", reMsgType);
				body.put("resend", jsonParam);
				jsonParam3.put("message", message);
				
				jsonParam = new JSONObject();
				jsonParam.put(reMsgType, jsonParam3);
				body.put("recontent", jsonParam);
			}
		} else {
//			문자 메세지	
			jsonParam2.put("message", message);
			jsonParam.put(msgType, jsonParam2);
			body.put("content",jsonParam);
		}
		
		String from = (String)param.get("sendTel"); 
		String to = (String)param.get("destTel");
		body.put("from",from.replaceAll("-", ""));
		body.put("to",to.replaceAll("-", ""));
		
		JSONObject jsonObject = getJsonStringFromMap(body);
		

		System.out.println("================================================== debug");
		System.out.println("reType go --> " + (String)param.get("reType"));
		System.out.println("msgType go --> " + msgType);
		System.out.println("message go --> " + message);
		System.out.println("message go --> " + jsonObject.toString());
		System.out.println("message go --> " + URI.create(url+"/v3/message"));
		System.out.println("================================================== debug");
		
		
		
		String authorization = header.get("type")+" "+header.get("accesstoken");
		
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create(url+"/v3/message"))
			    .header("Authorization", authorization)
			    .header("Content-Type", "application/json; charset=utf-8")
			    .method("POST", HttpRequest.BodyPublishers.ofString(jsonObject.toJSONString()))
			    .timeout(Duration.ofSeconds(15))
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
		
		
		return jsonObj;
	}
	
	/**
	 * 필수 값 체크
	 */
	private String isnullCheck(BizBatchVO bizBatchVO) {
		String msg = "";
		String sendMsg = (String)bizBatchVO.getSendMsg();
		String destTel = (String)bizBatchVO.getDestTel();
		String sendTel = (String)bizBatchVO.getSendTel();
		String refkey = (String)bizBatchVO.getRefkey();
		
		if (sendMsg == null || sendMsg.isEmpty()) {
			msg = "메세지내용(sendMsg)이 누락되었습니다.";
		} else if (destTel == null || destTel.isEmpty()) {
			msg = "수신번호(destTel)가 누락되었습니다.";
		} else if (sendTel == null || sendTel.isEmpty()) {
			msg = "발신번호(sendTel)가 누락되었습니다.";
		} else if (refkey == null || refkey.isEmpty()) {
			msg = "고객사에서 부여한 키(refkey)가 누락되었습니다.";
		} 
		return msg;
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
	
	/**
	 * 데이터 셋팅
	 * @param 	map
	 * @return 	map
	 */
	private Map<String, Object> dataSet(Map<String, Object> param) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("account", (String)param.get("account"));
		body.put("type", (String)param.get("msgType"));
		body.put("refkey", (String)param.get("refkey"));
		body.put("sendtime", (String)param.get("sendtime"));
		return body;
	}
}
