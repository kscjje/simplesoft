package com.simplesoft.payments.service;

import java.util.Map;

import org.json.simple.JSONObject;

public interface PaymentsService {
	
	public String getPayResult(JSONObject param);
	public int insertPayments(Map<String, Object> paramMap);
}
