package com.simplesoft.common.service;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Class Name : TossEasyPayVO.java
 * @Description : 토스 간편결제 객체 : 간편결제 정보입니다. 고객이 선택한 결제수단에 따라 amount, discountAmount가 달라집니다. 간편결제 응답 처리를 참고하세요.
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
public class TossEasyPayVO {
	
	/**선택한 간편결제사 코드입니다.*/
	public String provider;
	
	/**간편결제 서비스에 등록된 계좌 혹은 현금성 포인트로 결제한 금액입니다.*/
	public int amount;
	
	/**간편결제 서비스의 적립 포인트나 쿠폰 등으로 즉시 할인된 금액입니다.*/
	public int discountAmount;
}

