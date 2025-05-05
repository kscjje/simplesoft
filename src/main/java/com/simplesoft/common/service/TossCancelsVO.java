package com.simplesoft.common.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Class Name : TossCancelsVO.java
 * @Description : 토스 결제 취소이력 객체 : 결제 취소 이력이 담기는 객체입니다.
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
public class TossCancelsVO {
	
	/**결제를 취소한 금액입니다.*/
	private int cancelAmount;
	
	/**결제를 취소한 이유입니다. 최대 길이는 200자입니다.*/
	private String cancelReason;
	
	/**취소된 금액 중 면세 금액입니다.*/
	private int taxFreeAmount;
	
	/**취소된 금액 중 과세 제외 금액(컵 보증금 등)입니다.*/
	private int taxExemptionAmount;
	
	/**결제 취소 후 환불 가능한 잔액입니다.*/
	private int refundableAmount;
	
	/**간편결제 서비스의 포인트, 쿠폰, 즉시할인과 같은 적립식 결제수단에서 취소된 금액입니다.*/
	private int easyPayDiscountAmount;
	
	/**결제 취소가 일어난 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm입니다. (e.g. 2022-01-01T00:00:00+09:00)*/
	private String canceledAt;
	
	/**취소 건의 키 값입니다. 여러 건의 취소 거래를 구분하는데 사용됩니다. 최대 길이는 64자입니다.*/
	private String transactionKey;
	
	/**취소 건의 현금영수증 키 값입니다. 최대 길이는 200자입니다*/
	private String receiptKey;
}

