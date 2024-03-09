package com.simplesoft.mapper;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.member.service.MemberVO;

@MysqlConnMapper
public interface MemberMapper {
	public int insertMember(MemberVO memberVO);
	
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paramMap);
	public Map<String, Object> selectMemberDetail(Map<String, Object> paramMap);
}
