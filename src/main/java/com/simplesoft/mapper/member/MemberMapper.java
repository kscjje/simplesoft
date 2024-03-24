package com.simplesoft.mapper.member;

import java.util.List;
import java.util.Map;

import com.simplesoft.config.MysqlConnMapper;
import com.simplesoft.member.service.MemberVO;

@MysqlConnMapper
public interface MemberMapper {
	public int insertMember(MemberVO vo);
	
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paramMap);
	public MemberVO selectMemberDetail(MemberVO vo);
}
