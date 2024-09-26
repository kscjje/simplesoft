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
	public int insertMenuBoard(Map<String, Object> paraMap) {
		return menuBoardMapper.insertMenuBoard(paraMap);
	}
	@Override
	public int updateMenuBoard(Map<String, Object> paraMap) {
		return menuBoardMapper.updateMenuBoard(paraMap);
	}
	@Override
	public List<Map<String, Object>> selectMenuBoardList(Map<String, Object> paraMap) {
		return menuBoardMapper.selectMenuBoardList(paraMap);
	}
	@Override
	public Map<String, Object> selectMenuBoardDetail(Map<String, Object> paraMap) {
		return menuBoardMapper.selectMenuBoardDetail(paraMap);
	}
}
