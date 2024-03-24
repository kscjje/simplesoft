package com.simplesoft.util;

/**
 * 
 * @author 		kscjje
 * @description	상수 정의
 * @since		2024.01.18
 */
public class GlobalVariable {
	
	public static final int PRODUCT_AMT = 18000;					//상품금액(고정)
	public static final int DELY_AMT = 6000;						//배송비(고정)
	
	public static final String REDIRECT_LOGIN 	= "/common/login";
	public static final String REDIRECT_MESSAGE	= "/common/message";
	public static final String REDIRECT_BACK	= "/common/back";
	public static final String REDIRECT_SUBMIT	= "/common/submit";
	
	public static final String ORDER_STATUS_APPLY 		= "1000";	//주문접수
	public static final String ORDER_STATUS_COMPLETE 	= "0000";	//주문완료
}
