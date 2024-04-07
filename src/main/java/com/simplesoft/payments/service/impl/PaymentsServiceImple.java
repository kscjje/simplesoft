package com.simplesoft.payments.service.impl;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.payments.PaymentsMapper;
import com.simplesoft.payments.service.PaymentsService;

@Service
public class PaymentsServiceImple implements PaymentsService{
	@Autowired
	PaymentsMapper paymentsMapper;
	
	public String getPayResult(JSONObject param) {
		
		return "";
	}
	public int insertPayments(Map<String, Object> paramMap) {
		return paymentsMapper.insertPayments(paramMap);
	}
}
