package com.simplesoft.mapper.payments;

import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;

@MysqlConnMapper
public interface PaymentsMapper {
	public int insertPayments(Map<String, Object> paramMap);
}
