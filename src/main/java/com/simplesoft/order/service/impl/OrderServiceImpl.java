package com.simplesoft.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.OrderVO;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	OrderMapper orderMapper;
	
	@Override
	public int insertOrderMst(OrderVO vo) {
		if(orderMapper.insertOrderMst(vo) > 0) {
			System.out.println("성공");
		}
		return 0;
	}
}
