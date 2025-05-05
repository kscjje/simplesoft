package com.simplesoft.mapper.payments;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.payments.service.PaymentsVO;

@MysqlConnMapper
public interface PaymentsMapper {
	public int insertPayments(PaymentsVO vo);
	public int insertPaymentsCancel(PaymentsVO vo);
	public PaymentsVO selectPaymentsDetail(PaymentsVO vo);
	public int updatePayments(PaymentsVO vo);
}
