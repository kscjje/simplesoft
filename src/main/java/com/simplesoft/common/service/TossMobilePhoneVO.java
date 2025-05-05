package com.simplesoft.common.service;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * @Class Name : TossMobilePhoneVO.java
 * @Description : 토스 휴대폰 객체 : 휴대폰으로 결제하면 제공되는 휴대폰 결제 관련 정보입니다
 * @Modification Information
 *               수정일 수정자 수정내용
 *               ------- ------- -------------------
 *               2023. 04. 28. 김영철
 * @author 김영철
 * @since 2023. 04. 28.
 * @version
 * @see
 */

@Getter
@Setter
@ToString
public class TossMobilePhoneVO {
	
	/**결제에 사용한 휴대폰 번호입니다.*/
	private String customerMobilePhone;
	
	/**정산 상태입니다. 정산이 아직 되지 않았다면 INCOMPLETED, 정산이 완료됐다면 COMPLETED 값이 들어옵니다.*/
	private String settlementStatus;

	/**휴대폰 결제 내역 영수증을 확인할 수 있는 주소입니다.*/
	private String receiptUrl;
}

