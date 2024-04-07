package com.simplesoft.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.order.service.OrderProductVO;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;

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
	public int updateOrderApplyInfo(OrderVO vo) {
		return orderMapper.updateOrderApplyInfo(vo);
	}
	
}
