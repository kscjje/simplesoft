package com.simplesoft.member.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simplesoft.mapper.member.AddressMapper;
import com.simplesoft.member.service.AddressService;
import com.simplesoft.member.service.AddressVO;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	AddressMapper addressMapper;
	
	@Transactional
	@Override
	public int insertAddress(AddressVO addressVO) {
		
		// 1. 배송지 등록 (먼저!)
		int cnt = addressMapper.insertAddress(addressVO);

		// 2. 대표 배송지 처리
		if ("Y".equals(addressVO.getChooseYn())) {
			updateAddressChoose(addressVO);
		}
		return cnt;
	}

	@Override
	public int updateAddress(AddressVO addressVO) {
		// 1. 배송지 등록 (먼저!)
		int cnt = addressMapper.updateAddress(addressVO);
	
		// 2. 대표 배송지 처리
		if ("Y".equals(addressVO.getChooseYn())) {
			updateAddressChoose(addressVO);
		}
		return cnt;
	}
	
	@Transactional
	@Override
	public int updateAddressChoose(AddressVO addressVO) {
		return addressMapper.updateAddressChoose(addressVO);
	}

	@Override
	public int deleteAddress(AddressVO addressVO) {
		return addressMapper.deleteAddress(addressVO);
	}
	
	@Override
	public List<Map<String, Object>> selectAddressList(Map<String, Object> paraMap) {
		return addressMapper.selectAddressList(paraMap);
	}

	@Override
	public int selectAddressNmCheck(AddressVO addressVO) {
		return addressMapper.selectAddressNmCheck(addressVO);
	}
}
