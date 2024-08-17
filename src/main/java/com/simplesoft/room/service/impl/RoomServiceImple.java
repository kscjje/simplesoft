package com.simplesoft.room.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.room.RoomMapper;
import com.simplesoft.room.service.RoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomServiceImple implements RoomService{
	
	@Autowired
	RoomMapper roomMapper;

	@Override
	public int insertRoom(Map<String, Object> paramMap) {
		return roomMapper.insertRoom(paramMap);
	}
	@Override
	public int updateRoom(Map<String, Object> paramMap) {
		return roomMapper.updateRoom(paramMap);
	}
	@Override
	public Map<String, Object> selectRoomDetail(Map<String, Object> paramMap) {
		return roomMapper.selectRoomDetail(paramMap);
	}
	public List<Map<String, Object>> list(){
		return roomMapper.list();
	}
}
