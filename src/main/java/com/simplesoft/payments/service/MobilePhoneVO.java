package com.simplesoft.payments.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MobilePhoneVO {
	/**@param 구매자가 결제에 사용한 휴대폰 번호입니다.	 */
	private Object customerMobilePhone;
	/**@param 정산 상태입니다. 정산이 아직 되지 않았다면 INCOMPLETED, 정산이 완료됐다면 COMPLETED 값이 들어옵니다.	 */
	private String settlementStatus;
	/**@param 휴대폰 결제 내역 영수증을 확인할 수 있는 주소입니다.	 */
	private String receiptUrl;
}
