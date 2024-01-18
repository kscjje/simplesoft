package com.simplesoft.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplesoft.mapper.MemberMapper;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public int insertMember(MemberVO memberVO) {
		
		return memberMapper.insertMember(memberVO);
	}
}
