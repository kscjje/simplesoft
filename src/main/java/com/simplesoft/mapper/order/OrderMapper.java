package com.simplesoft.mapper.order;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderVO;

@MysqlConnMapper
public interface OrderMapper {
	public int insertOrderMst(OrderVO vo);
	public int insertOrderProduct(OrderProductVO vo);
	public OrderVO selectOrderApplyInfo(OrderVO vo);
	public int updateOrderApplyInfo(OrderVO vo);
	public int selectOrderPayConfirm(String orderId);
}
