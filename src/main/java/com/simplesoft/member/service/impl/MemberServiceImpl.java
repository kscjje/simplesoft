package com.simplesoft.member.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simplesoft.mapper.member.MemberMapper;
import com.simplesoft.member.service.AddressService;
import com.simplesoft.member.service.AddressVO;
import com.simplesoft.member.service.MemberService;
import com.simplesoft.member.service.MemberVO;
import com.simplesoft.util.EncryptUtils;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	AddressService addressService;
	
	@Override
	public int insertMember(MemberVO memberVO) {
		return memberMapper.insertMember(memberVO);
	}
	@Override
	public int deleteMember(MemberVO memberVO) {
		return memberMapper.deleteMember(memberVO);
	}
	@Override
	public int loginSucess(MemberVO memberVO) {
		return memberMapper.loginSucess(memberVO);
	}
	@Override
	public MemberVO selectMemberDetail(MemberVO vo){
		return memberMapper.selectMemberDetail(vo);
	}
	@Override
	public MemberVO selectMemberDetailSns(MemberVO vo){
		return memberMapper.selectMemberDetailSns(vo);
	}
	@Override
	public Map<?,?> selectIdCheck(MemberVO vo){
		return memberMapper.selectIdCheck(vo);
	}
	@Override
	public List<Map<String, Object>> selectMemberList(Map<String, Object> paraMap){
		return memberMapper.selectMemberList(paraMap);
	}
	
	@Transactional
	@Override
	public Map<?,?> memberSave(MemberVO vo){
		Map<String, Object> returnData = new HashMap<String, Object>();
		int totCnt = ((Number) selectIdCheck(vo).get("totCnt")).intValue();
		if(totCnt > 0) {
			returnData.put("RESULT", "9990");
			returnData.put("message", "이미 회원가입된 아이디입니다.");
			return returnData;
		}
		try {
			vo.setUserPw(EncryptUtils.SHA512_Encrypt(vo.getUserPw()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("비밀번호 암호화 실패", e);
		}
		// 2. 회원 INSERT
		int memberCnt = insertMember(vo);
		if (memberCnt <= 0) {
			throw new RuntimeException("회원 INSERT 실패");
		}

		// 3. 주소 INSERT (대표배송지)
		AddressVO addr = new AddressVO();
		addr.setUserNo(vo.getUserNo());   // ← 시퀀스로 이미 값 존재
		addr.setAddressNm("기본 배송지");
		addr.setZipcode(vo.getZipcode());
		addr.setAddress(vo.getAddress());
		addr.setAddressDetail(vo.getAddressDetail());
		addr.setCommonPwd(vo.getCommonPwd());
		addr.setBigo(vo.getBigo());
		addr.setChooseYn("Y");

		int addrCnt = addressService.insertAddress(addr);
		if (addrCnt <= 0) {
			throw new RuntimeException("주소 INSERT 실패");
		}
		
		// 4. 정상
		returnData.put("RESULT", "0000");
		returnData.put("message", "회원가입이 완료되었습니다. \n로그인 페이지로 이동합니다.");
		
		return returnData;
	}
	
	@Override
	public Map<?,?> selectFindIdCheck(MemberVO vo){
		return memberMapper.selectFindIdCheck(vo);
	}
	@Override
	public Map<?,?> selectFindPasswordCheck(MemberVO vo){
		return memberMapper.selectFindPasswordCheck(vo);
	}
	@Override
	public int updateOneTimePassword(MemberVO memberVO) {
		return memberMapper.updateOneTimePassword(memberVO);
	}
}
