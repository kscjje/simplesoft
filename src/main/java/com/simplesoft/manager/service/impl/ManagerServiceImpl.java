package com.simplesoft.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.manager.service.ManagerService;
import com.simplesoft.manager.service.ManagerVO;
import com.simplesoft.mapper.manager.ManagerMapper;

@Service
public class ManagerServiceImpl implements ManagerService {
	
	@Autowired
	ManagerMapper managerMapper;
	
//	@Override
//	public int insertMenuBoard(Map<String, Object> paraMap) {
//		return menuBoardMapper.insertMenuBoard(paraMap);
//	}
//	@Override
//	public List<Map<String, Object>> selectMenuBoardList(Map<String, Object> paraMap) {
//		return menuBoardMapper.selectMenuBoardList(paraMap);
//	}
	@Override
	public ManagerVO selectManagerDetail(ManagerVO vo) {
		return managerMapper.selectManagerDetail(vo);
	}
	@Override
	public List<Map<String, Object>> selectManagerList(ManagerVO vo){
		return managerMapper.selectManagerList(vo);
	}
}
