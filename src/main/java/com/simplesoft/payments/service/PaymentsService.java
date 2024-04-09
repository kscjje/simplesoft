package com.simplesoft.payments.service;

import org.json.simple.JSONObject;

public interface PaymentsService {
	
	public String getPayResult(JSONObject param);
	public int insertPayments(PaymentsVO vo);
	public PaymentsVO selectPaymentsDetail(PaymentsVO vo);
}
