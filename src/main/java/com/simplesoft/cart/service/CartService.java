package com.simplesoft.cart.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public interface CartService {
	@Transactional
	public void insertCart(Map<String, Object> paramMap);
	public List<Map<String, Object>> selectCartList(Map<String, Object> paramMap);
	
	public int updateCartQty(Map<String, Object> paramMap);
	public int deleteCart(Map<String, Object> paramMap);
	public Map<String, Object> selectCartDetail(Map<String, Object> paramMap);
	public List<Map<String, Object>> selectOrderCartShop(Map<String, Object> paramMap);
}
