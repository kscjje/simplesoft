package com.simplesoft.payments.service;

import org.json.simple.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentsService {
	
	public void getPayResult(HttpServletRequest request, JSONObject param);
	public int insertPayments(PaymentsVO vo);
	public PaymentsVO selectPaymentsDetail(PaymentsVO vo);
}
