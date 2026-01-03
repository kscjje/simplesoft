package com.simplesoft.mapper.order;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.order.service.RefundVO;

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
	
	public int insertRefund(RefundVO vo);
	public int getRefundCheck(RefundVO vo);
	public RefundVO getRefundDetail(OrderVO vo);
	public List<Map<String, Object>> selectOrderList(OrderVO vo);
	public OrderVO selectOrderDetail(OrderVO vo);
}
