package com.simplesoft.menuboard.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.menuboard.MenuBoardMapper;
import com.simplesoft.menuboard.service.MenuBoardService;

@Service
public class MenuBoardServiceImpl implements MenuBoardService{
	@Autowired
	MenuBoardMapper menuBoardMapper;
	
	@Override
	public int insertMenuBoard(Map<String, Object> paramMap) {
		return menuBoardMapper.insertMenuBoard(paramMap);
	}
	@Override
	public int updateMenuBoard(Map<String, Object> paramMap) {
		return menuBoardMapper.updateMenuBoard(paramMap);
	}
	@Override
	public int deleteMenuBoard(Map<String, Object> paramMap) {
		return menuBoardMapper.deleteMenuBoard(paramMap);
	}
	@Override
	public int updateMenuBoardUseYn(Map<String, Object> paramMap) {
		return menuBoardMapper.updateMenuBoardUseYn(paramMap);
	}
	@Override
	public List<Map<String, Object>> selectMenuBoardList(Map<String, Object> paramMap) {
		return menuBoardMapper.selectMenuBoardList(paramMap);
	}
	@Override
	public Map<String, Object> selectMenuBoardDetail(Map<String, Object> paramMap) {
		return menuBoardMapper.selectMenuBoardDetail(paramMap);
	}
	@Override
	public Map<String, Object> selectMenuBoardDuple(Map<String, Object> paramMap) {
		return menuBoardMapper.selectMenuBoardDuple(paramMap);
	}
}
