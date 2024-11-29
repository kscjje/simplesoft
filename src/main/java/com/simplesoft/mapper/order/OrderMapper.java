package com.simplesoft.mapper.order;

import java.util.List;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderVO;

@MysqlConnMapper
public interface OrderMapper {
	public int insertOrderMst(OrderVO vo);
	public int insertOrderProduct(OrderProductVO vo);
	public OrderVO selectOrderApplyInfo(OrderVO vo);
	public OrderVO selectOrderCheck(OrderVO vo);
	public int updateOrderApplyInfo(OrderVO vo);
	public int selectOrderPayConfirm(String orderId);
	public int updateOrderStatusComplete(OrderVO vo);
	public int insertDelivery(DeliveryVO vo);
	public List<OrderProductVO> getOrderProductInfo(OrderProductVO productVO);
	public List<DeliveryVO> getOrderInfoDelivery(OrderProductVO vo);
}
