package com.simplesoft.mapper.cart;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.simplesoft.config.MysqlConnMapper;

@MysqlConnMapper
public interface CartMapper {
	public int insertCart(Map<String, Object> paramMap);
	public int updateCartAdd(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> selectCartList(Map<String, Object> paramMap);
	public Map<String, Object> selectCartDetail(Map<String, Object> paramMap);
}
