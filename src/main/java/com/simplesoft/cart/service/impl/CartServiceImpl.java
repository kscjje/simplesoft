package com.simplesoft.cart.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.cart.service.CartService;
import com.simplesoft.mapper.cart.CartMapper;
import com.simplesoft.menuboard.service.MenuBoardService;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	CartMapper cartMapper;
	
	@Autowired
	MenuBoardService menuBoardService;
	
	/*
	 * 20250804 장바구니 배송유형 추가하기 전
	@Override
	public void insertCart(Map<String, Object> paramMap) {
		
		String menuBoardSeq = (String)paramMap.get("menuBoardSeq");
		String[] arrMenuBoardSeq = menuBoardSeq.split(",");
		//선택한 개수만큼 장바구니에 담기
		for(int i = 0; i < arrMenuBoardSeq.length; i ++) {
			paramMap.put("menuBoardSeq", arrMenuBoardSeq[i]);
			if (selectCartList(paramMap).size() > 0) {
				//이미 장바구니에 있다면 수량 +1
				cartMapper.updateCartAdd(paramMap);
			} else {
				cartMapper.insertCart(paramMap);
			}
		}
	}
	*/
	@Override
	public void insertCart(Map<String, Object> paramMap) {
		
		String menuBoardSeq = (String)paramMap.get("menuBoardSeq");
		String[] arrMenuBoardSeq = menuBoardSeq.split(",");
		//선택한 개수만큼 장바구니에 담기
		for(int i = 0; i < arrMenuBoardSeq.length; i ++) {
			paramMap.put("menuBoardSeq", arrMenuBoardSeq[i]);
			if (selectCartList(paramMap).size() > 0) {
				//이미 장바구니에 있다면 수량 +1
				cartMapper.updateCartAdd(paramMap);
			} else {
				Map<String, Object> delivOption = menuBoardService.selectMenuBoardDetail(paramMap);
				if (delivOption != null) {
					String strDelivOption = (String) delivOption.get("delivOption");
				    String newValue = "1000"; // 기본값
				    if ("0000".equals(strDelivOption)) {
				        newValue = "1000";
				    } else if ("0001".equals(strDelivOption)) {
				        newValue = "2000";
				    }
				    paramMap.put("delivTime", newValue);
				}
				cartMapper.insertCart(paramMap);
			}
		}
	}
	
	@Override
	public Map<String, Object> selectCartDetail(Map<String, Object> paramMap){
		return cartMapper.selectCartDetail(paramMap);
	}
	@Override
	public List<Map<String, Object>> selectCartList(Map<String, Object> paramMap){
		return cartMapper.selectCartList(paramMap);
	}
	@Override
	public List<Map<String, Object>> selectOrderCartShop(Map<String, Object> paramMap){
		return cartMapper.selectOrderCartShop(paramMap);
	}
	@Override
	public int updateCartQty(Map<String, Object> paramMap) {
		return cartMapper.updateCartQty(paramMap);
	}
	@Override
	public int updateCartMenuMsgQty(Map<String, Object> paramMap) {
		return cartMapper.updateCartMenuMsgQty(paramMap);
	}
	@Override
	public int updateCartOrderSet(Map<String, Object> paramMap) {
		return cartMapper.updateCartOrderSet(paramMap);
	}
	@Override
	public int updateCartOrderDelivTime(Map<String, Object> paramMap) {
		return cartMapper.updateCartOrderDelivTime(paramMap);
	}
	@Override
	public int deleteCart(Map<String, Object> paramMap) {
		return cartMapper.deleteCart(paramMap);
	}
}