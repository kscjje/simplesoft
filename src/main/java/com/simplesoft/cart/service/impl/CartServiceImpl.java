package com.simplesoft.cart.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.mapper.cart.CartMapper;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	CartMapper CartMapper;
	
	@Override
	public void insertCart(Map<String, Object> paramMap) {
		
		String menuBoardSeq = (String)paramMap.get("menuBoardSeq");
		String[] arrMenuBoardSeq = menuBoardSeq.split(",");
		//선택한 개수만큼 장바구니에 담기
		for(int i = 0; i < arrMenuBoardSeq.length; i ++) {
			paramMap.put("menuBoardSeq", arrMenuBoardSeq[i]);
			if (selectCartList(paramMap).size() > 0) {
				//이미 장바구니에 있다면 수량 +1
				System.out.println(paramMap);
				CartMapper.updateCartAdd(paramMap);
			} else {
				System.out.println(paramMap);
				CartMapper.insertCart(paramMap);
			}
		}
	}
	
	@Override
	public Map<String, Object> selectCartDetail(Map<String, Object> paramMap){
		return CartMapper.selectCartDetail(paramMap);
	}
	@Override
	public List<Map<String, Object>> selectCartList(Map<String, Object> paramMap){
		return CartMapper.selectCartList(paramMap);
	}
	@Override
	public int updateCartQty(Map<String, Object> paramMap) {
		return CartMapper.updateCartQty(paramMap);
	}
	@Override
	public int deleteCart(Map<String, Object> paramMap) {
		return CartMapper.deleteCart(paramMap);
	}
}