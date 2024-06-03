package com.simplesoft.util;

/**
 * 
 * @author 		kscjje
 * @description	상수 정의
 * @since		2024.01.18
 */
public class GlobalVariable {
	
	public static final int PRODUCT_AMT = 18000;					//상품금액(고정)
	public static final int DELY_AMT = 2000;						//배송비(고정)
	
	public static final String REDIRECT_LOGIN 	= "/common/login";
	public static final String REDIRECT_MESSAGE	= "/common/message";
	public static final String REDIRECT_BACK	= "/common/back";
	public static final String REDIRECT_SUBMIT	= "/common/submit";
	
	public static final String ORDER_STATUS_WAIT 		= "1000";	//주문대기
	public static final String ORDER_STATUS_COMPLETE 	= "0000";	//주문완료
	public static final String ORDER_STATUS_CANCEL	 	= "0001";	//주문취소
	
	//ADMIN PORTAL 서버 LOG 감시 설정 이메일 관련 코드
	public static final String LOG_RESULT_CODE_SUCCESS = "20000200"; 					//성공
    public static final String LOG_RESULT_CODE_MAIL_TO_ADDRESS_ERROR = "51000401"; 		//받는사람 메일이 잘못됨
    public static final String LOG_RESULT_CODE_MAIL_FROM_ADDRESS_ERROR = "51000402"; 	//보내는사람 메일이 잘못됨
    public static final String LOG_RESULT_CODE_MAIL_SERVER_AUTH_FAIL = "51000403"; 		//메일서버 접근 권한이 없음
    public static final String LOG_RESULT_CODE_MAIL_SERVER_NOTFOUND = "51000404"; 		//메일서버(HOST)를 찾을수 없음
    public static final String LOG_RESULT_CODE_MAIL_SERVER_ERROR = "51000500"; 			//서버오류 발생으로 발송 실패
}
