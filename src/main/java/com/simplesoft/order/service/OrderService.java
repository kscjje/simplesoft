package com.simplesoft.order.service;

public interface OrderService {
	public int insertOrderMst(OrderVO vo);
	public OrderVO selectOrderApplyInfo(OrderVO vo);
	public int updateOrderApplyInfo(OrderVO vo);
	public int updateOrderStatusComplete(OrderVO vo);
}
