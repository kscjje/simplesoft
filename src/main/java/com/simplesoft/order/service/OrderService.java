package com.simplesoft.order.service;

import java.util.List;

public interface OrderService {
	public int insertOrderMst(OrderVO vo);
	public OrderVO selectOrderApplyInfo(OrderVO vo);
	public int updateOrderApplyInfo(OrderVO vo);
	public int updateOrderStatusComplete(OrderVO vo);
	public List<OrderProductVO> getOrderProductInfo(OrderProductVO productVO);
	public List<DeliveryVO> getOrderInfoDelivery(OrderProductVO productVO);
}
