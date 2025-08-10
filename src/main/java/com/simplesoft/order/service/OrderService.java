package com.simplesoft.order.service;

import java.util.List;
import java.util.Map;

public interface OrderService {
	public RefundVO selectOrderCheck(RefundVO vo);
	public int insertUser(RefundVO vo);
	public int updateTime(RefundVO vo);
	public int updateHint(RefundVO vo);
	public int success(RefundVO vo);
	public List<Map<String,Object>> selectAll(RefundVO vo);
}