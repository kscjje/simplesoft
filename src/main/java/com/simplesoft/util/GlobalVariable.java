package com.simplesoft.util;

/**
 * 
 * @author 		kscjje
 * @description	상수 정의
 * @since		2024.01.18
 */
public class GlobalVariable {
	
	public static final int PRODUCT_AMT_1 = 18000;				//상품금액(고정)
	public static final int PRODUCT_AMT_2 = 9000;				//상품금액(고정)
	public static final int DELY_AMT = 2000;					//배송비(고정)
	public static final int DELY_OFFICE_AMT = 4000;				//우체국택배(고정)
	public static final int PACKING_AMT = 5000;					//보냉가방(고정)
	
	public static final int[] PROUDCT_MAP = { 7500, 4500, 2500, 3500, 2500 };	//메뉴별 가격
	public static final String REDIRECT_LOGIN 	= "/common/login";
	public static final String REDIRECT_MESSAGE	= "/common/message";
	public static final String REDIRECT_BACK	= "/common/back";
	public static final String REDIRECT_SUBMIT	= "/common/submit";
	
	public static final String ORDER_STATUS_WAIT 			= "1000";	//주문대기
	public static final String ORDER_STATUS_VIRTUAL			= "1001";	//가상계좌 입금대기
	public static final String ORDER_STATUS_COMPLETE 		= "0000";	//주문완료
	public static final String ORDER_STATUS_CANCEL_COMPLETE	= "0001";	//주문취소
	public static final String ORDER_STATUS_REFUND	 		= "0002";	//주문환불요청
	public static final String ORDER_STATUS_REFUND_COMPLETE = "0003";	//주문환불완료
	
	public static final String DELIVERY_STATUS_WAIT 		= "1000";	//배송전대기
	public static final String DELIVERY_STATUS_COMPLETE		= "0000";	//배송완료
	
	public static final String DELIVERY_KIND_1000		= "1000";		//당일배송(안산)
	public static final String DELIVERY_KIND_2000		= "2000";		//우체국 택배(안산 외)
	
	public static final String PACKAGING_1000		= "1000";			//보냉가방(신규)
	public static final String PACKAGING_2000		= "2000";			//보냉가방(기존)
	public static final String PACKAGING_3000		= "3000";			//종이가방
	
	public static final String DELIV_TIME_1000		= "1000";			//오전배달(8~10시)
	public static final String DELIV_TIME_2000		= "2000";			//오후배달(1~4시)
	
	//ADMIN PORTAL 서버 LOG 감시 설정 이메일 관련 코드
	public static final String LOG_RESULT_CODE_SUCCESS = "20000200"; 					//성공
    public static final String LOG_RESULT_CODE_MAIL_TO_ADDRESS_ERROR = "51000401"; 		//받는사람 메일이 잘못됨
    public static final String LOG_RESULT_CODE_MAIL_FROM_ADDRESS_ERROR = "51000402"; 	//보내는사람 메일이 잘못됨
    public static final String LOG_RESULT_CODE_MAIL_SERVER_AUTH_FAIL = "51000403"; 		//메일서버 접근 권한이 없음
    public static final String LOG_RESULT_CODE_MAIL_SERVER_NOTFOUND = "51000404"; 		//메일서버(HOST)를 찾을수 없음
    public static final String LOG_RESULT_CODE_MAIL_SERVER_ERROR = "51000500"; 			//서버오류 발생으로 발송 실패
}
