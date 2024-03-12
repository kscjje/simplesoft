package com.simplesoft.cart.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public interface CartService {
	@Transactional
	public void insertCart(Map<String, Object> paraMap);
	public List<Map<String, Object>> selectCartList(Map<String, Object> paraMap);
	public Map<String, Object> selectCartDetail(Map<String, Object> paraMap);
}
