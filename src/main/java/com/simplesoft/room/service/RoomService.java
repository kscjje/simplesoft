package com.simplesoft.room.service;

import java.util.Map;

import org.json.simple.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public interface RoomService {
	
	public int insertRoom(Map<String, Object> paramMap);
	public Map<String, Object> selectRoomDetail(Map<String, Object> paramMap);
	public int updateRoom(Map<String, Object> paramMap);
}
