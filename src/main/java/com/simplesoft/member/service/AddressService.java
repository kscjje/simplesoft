package com.simplesoft.member.service;

import java.util.List;
import java.util.Map;

public interface AddressService {
	public int insertAddress(AddressVO addressVO);
	public int updateAddress(AddressVO addressVO);
	public int updateAddressChoose(AddressVO addressVO);
	public int deleteAddress(AddressVO addressVO);
	public List<Map<String, Object>> selectAddressList(Map<String, Object> paraMap);
	public int selectAddressNmCheck(AddressVO addressVO);
}
