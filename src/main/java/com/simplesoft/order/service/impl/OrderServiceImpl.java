package com.simplesoft.order.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.order.service.DeliveryVO;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;
import com.simplesoft.order.service.RefundVO;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	OrderMapper orderMapper;
	
	//주문정보저장
	@Transactional
	@Override
	public int insertOrderMst(OrderVO vo) {
		if(orderMapper.insertOrderMst(vo) > 0) {
			for (OrderProductVO product : vo.getProductList()) {
				product.setOrderNo(vo.getOrderNo());
				product.setRegUser(vo.getRegUser());
				
				orderMapper.insertOrderProduct(product);
			}
		} else {
			throw new RuntimeException("ERR|ORDER|주문정보 저장 실패");
		}
		return 1;
	}
	
	//주문신청정보 
	@Override
	public OrderVO selectOrderApplyInfo(OrderVO vo) {
		return orderMapper.selectOrderApplyInfo(vo);
	}
	
	//비회원주문조회 
	@Override
	public OrderVO selectOrderCheck(OrderVO vo) {
		return orderMapper.selectOrderCheck(vo);
	}
	
	//주문정보 갱신
	@Override
	public int updateOrderApplyInfo(OrderVO vo) {
		return orderMapper.updateOrderApplyInfo(vo);
	}
	
	//주문정보 주문완료 업데이트
	@Override
	public int updateOrderStatusComplete(OrderVO vo) {
		return orderMapper.updateOrderStatusComplete(vo);
	}
	//주문상품정보
	@Override
	public List<OrderProductVO> getOrderProductInfo(OrderProductVO productVO){
		return orderMapper.getOrderProductInfo(productVO);
	}
	//배송정보 넣을 리스트 조회
	@Override
	public List<DeliveryVO> getOrderInfoDelivery(OrderProductVO vo) {
		return orderMapper.getOrderInfoDelivery(vo);
	}
	
	//환불요청저장
	@Override
	public int insertRefund(RefundVO vo) {
		int cnt = orderMapper.getRefundCheck(vo);
		if(cnt > 0) {
			//이미 환불 데이터가 있음
			return 9999;
		}
		return orderMapper.insertRefund(vo); 
	}
	//환불 정보 조회
	@Override
	public RefundVO getRefundDetail(OrderVO vo) {
		return orderMapper.getRefundDetail(vo);
	}
	
	//주문조회 리스트
	@Override
	public List<Map<String, Object>> selectOrderList(OrderVO vo){
		return orderMapper.selectOrderList(vo);
	}
	//주문조회 상세 
	@Override
	public OrderVO selectOrderDetail(OrderVO vo) {
		return orderMapper.selectOrderDetail(vo);
	}
}
