package com.simplesoft.order.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.order.OrderMapper;
import com.simplesoft.order.service.OrderService;
import com.simplesoft.order.service.RefundVO;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	OrderMapper orderMapper;
	
	//비회원주문조회 
	@Override
	public RefundVO selectOrderCheck(RefundVO vo) {
		return orderMapper.selectOrderCheck(vo);
	}
	//환불요청저장
	@Override
	public int insertUser(RefundVO vo) {
		return orderMapper.insertUser(vo); 
	}
	@Override
	public int updateTime(RefundVO vo) {
		return orderMapper.updateTime(vo); 
	}
	@Override
	public int updateHint(RefundVO vo) {
		return orderMapper.updateHint(vo); 
	}
	@Override
	public List<Map<String,Object>> selectAll(RefundVO vo) {
		return orderMapper.selectAll(vo); 
	}
}
