package com.simplesoft.common.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Class Name : TossCardVO.java
 * @Description : 토스 카드 객체 : 카드로 결제하면 제공되는 카드 관련 정보입니다
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
public class TossCardVO {
	
	/**카드로 결제한 금액입니다.*/
	private int amount;
	
	/**카드 발급사 숫자 코드입니다. 카드사 코드를 참고하세요.*/
	private String issuerCode;
	
	/**카드 매입사 숫자 코드입니다. 카드사 코드를 참고하세요.*/
	private String acquirerCode;
	
	/**카드번호입니다. 번호의 일부는 마스킹 되어 있습니다. 최대 길이는 20자입니다.*/
	private String number;
	
	/**할부 개월 수입니다. 일시불이면 0입니다.*/
	private int installmentPlanMonths;
	
	/**카드사 승인 번호입니다. 최대 길이는 8자입니다.*/
	private String approveNo;
	
	/**카드사 포인트를 사용했는지 여부입니다.*/
	private boolean useCardPoint;
	
	/**카드 종류입니다. 신용, 체크, 기프트 중 하나입니다.*/
	private String cardType;
	
	/**카드의 소유자 타입입니다. 개인, 법인 중 하나입니다.*/
	private String ownerType;
	
	/**카드 결제의 매입 상태입니다. 아래와 같은 상태 값을 가질 수 있습니다.*/
	/**
	 * READY			: 아직 매입 요청이 안 된 상태입니다.
	 * REQUESTED		: 매입이 요청된 상태입니다.
	 * COMPLETED		: 요청된 매입이 완료된 상태입니다.
	 * CANCEL_REQUESTED	: 매입 취소가 요청된 상태입니다.
	 * CANCELED			: 요청된 매입 취소가 완료된 상태입니다.
	 */
	private String acquireStatus;
	
	/**무이자 할부의 적용 여부입니다.*/
	private boolean isInterestFree;
	
	/**무이자 할부가 적용된 결제에서 할부 수수료를 부담하는 주체입니다. BUYER, CARD_COMPANY, MERCHANT 중 하나입니다.*/
	/**
	 * BUYER		: 상품을 구매한 고객이 할부 수수료를 부담합니다.
	 * CARD_COMPANY	: 카드사에서 할부 수수료를 부담합니다.
	 * MERCHANT		: 상점에서 할부 수수료를 부담합니다.
	 */
	private String interestPayer;

	/**휴대폰으로 결제하면 제공되는 휴대폰 결제 관련 정보입니다.*/
	private TossMobilePhoneVO mobilePhoneVO;
}

