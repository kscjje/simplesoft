package com.simplesoft.common.service;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Class Name : TossPaymentVO.java
 * @Description : 토스 결제 객체
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
public class TossPaymentVO {
	
	/**Payment 객체의 응답 버전입니다. 버전 2022-06-08부터 날짜 기반 버저닝을 사용합니다. */
	private String version;
	
	/**결제의 키 값입니다. 최대 길이는 200자입니다. */
	private String paymentKey;
	
	/**결제 타입 정보입니다. NORMAL(일반결제), BILLING(자동결제), BRANDPAY(브랜드페이) 중 하나입니다. */
	private String type;
	
	/**주문 ID입니다. 최소 길이는 6자, 최대 길이는 64자입니다. */
	private String orderId;
	
	/**주문명입니다. 예를 들면 생수 외 1건 같은 형식입니다. 최대 길이는 100자입니다. */
	private String orderName;
	
	/**상점아이디(MID)입니다. 토스페이먼츠에서 발급합니다. 최대 길이는 14자입니다. */
	private String mId;
	
	/**결제할 때 사용한 통화 단위입니다. 원화인 KRW만 사용합니다. */
	private String currency;
	
	/**결제할 때 사용한 결제수단입니다. 카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권 중 하나입니다. */
	private String method;
	
	/**총 결제 금액입니다. */
	private int totalAmount;
	
	/**취소할 수 있는 금액(잔고)입니다. */
	private int balanceAmount;
	
	/**결제 처리 상태입니다. 아래와 같은 상태 값을 가질 수 있습니다. 
	 * READY				: 결제를 생성하면 가지게 되는 초기 상태 입니다. 인증 전까지는 READY 상태를 유지합니다.
	 * IN_PROGRESS			: 결제수단 정보와 해당 결제수단의 소유자가 맞는지 인증을 마친 상태입니다. 결제 승인 API를 호출하면 결제가 완료됩니다.
	 * WAITING_FOR_DEPOSIT	: 가상계좌 결제 흐름에만 있는 상태로, 결제 고객이 발급된 가상계좌에 입금하는 것을 기다리고 있는 상태입니다.
	 * DONE					: 인증된 결제수단 정보, 고객 정보로 요청한 결제가 승인된 상태입니다.
	 * CANCELED				: 승인된 결제가 취소된 상태입니다.
	 * PARTIAL_CANCELED		: 승인된 결제가 부분 취소된 상태입니다.
	 * ABORTED				: 결제 승인이 실패한 상태입니다.
	 * EXPIRED				: 결제 유효 시간 30분이 지나 거래가 취소된 상태입니다. IN_PROGRESS 상태에서 결제 승인 API를 호출하지 않으면 EXPIRED가 됩니다.
	 */
	private String status;
	
	/**결제가 일어난 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm으로 돌아옵니다. (e.g. 2022-01-01T00:00:00+09:00) */
	private String requestedAt;
	
	/**결제 승인이 일어난 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm으로 돌아옵니다. (e.g. 2022-01-01T00:00:00+09:00) */
	private String approvedAt;
	
	/**에스크로 사용 여부입니다. */
	private boolean useEscrow;
	
	/**마지막 거래의 키 값입니다. 한 결제 건의 승인 거래와 취소 거래를 구분하는데 사용됩니다. 예를 들어 결제 승인 후 부분 취소를 두 번 했다면 마지막 부분 취소 거래의 키 값이 할당됩니다. 최대 길이는 64자입니다. */
	private String lastTransactionKey;
	
	/**공급가액입니다. */
	private int suppliedAmount;
	
	/**부가세입니다. (결제 금액 amount - 면세 금액 taxFreeAmount) / 11 후 소수점 첫째 자리에서 반올림해서 계산합니다. (e.g. 결제 금액이 10,000원이고, 면세 금액이 3,000원이라면 부가세는 (10000-3000)/11 = 636.3636..을 반올림한 값 636원입니다.) */
	private int vat;
	
	/**문화비(도서, 공연 티켓, 박물관·미술관 입장권 등) 지출 여부입니다. 계좌이체, 가상계좌를 사용할 때만 설정하세요. \r\n"
			+ "카드 결제는 항상 false로 돌아옵니다. 카드 결제 문화비는 카드사에 문화비 소득공제 전용 가맹점번호로 등록하면 자동으로 처리됩니다. */
	private boolean cultureExpense;
	
	/**결제 금액 중 면세 금액입니다. \r\n"
			+ "일반 상점일 때는 면세 금액으로 0이 돌아옵니다. 면세 상점, 복합 과세 상점일 때만 면세 금액이 돌아옵니다. 더 자세한 내용은 세금 처리하기에서 살펴보세요. */
	private int taxFreeAmount;
	
	/**결제 금액 중 과세 제외 금액(컵 보증금 등)입니다. \r\n"
			+ "과세 제외 금액이 있는 카드 결제는 부분 취소가 안 됩니다. */
	private int taxExemptionAmount;
	
	/**결제 취소 이력이 담기는 배열입니다. */
	private ArrayList<TossCancelsVO> cancels;
	
	/**부분 취소 가능 여부입니다. 이 값이 false이면 전액 취소만 가능합니다. */
	private boolean isPartialCancelable;
	
	/**카드로 결제하면 제공되는 카드 관련 정보입니다. */
	private TossCardVO cardVO;
	
	/**가상계좌로 결제하면 제공되는 가상계좌 관련 정보입니다. */
	private TossVirtualAccountVO virtualAccountVO;
	
	/**가상계좌 웹훅이 정상적인 요청인지 검증하는 값입니다. 이 값이 가상계좌 웹훅 이벤트 본문으로 돌아온 secret과 같으면 정상적인 요청입니다. 최대 길이는 50자입니다. */
	private String secret;
	
	/**휴대폰으로 결제하면 제공되는 휴대폰 결제 관련 정보입니다. */
	private TossMobilePhoneVO mobilePhone;
	
	/**계좌이체로 결제했을 때 이체 정보가 담기는 객체입니다. */
	private TossTransferVO transferVO;
	
	/**발행된 영수증 정보입니다.*/
	private Object receipt;
	
	/**결제창 정보입니다.*/
	private Object checkout;
	
	/**간편결제 정보입니다. 고객이 선택한 결제수단에 따라 amount, discountAmount가 달라집니다. 간편결제 응답 처리를 참고하세요.*/
	private TossEasyPayVO easyPayVO;
	
	/**결제한 국가 정보입니다. ISO-3166의 두 자리 국가 코드 형식입니다.*/
	private String country;
	
	/**현금영수증 정보입니다.*/
	private TossCashReceiptVO cashReceiptVO;
	
	/**현금영수증 발행 및 취소 이력이 담기는 배열입니다. 계좌이체는 결제 즉시 현금영수증 정보를 확인할 수 있습니다. 가상계좌는 고객이 입금을 완료하면 현금영수증 정보를 확인할 수 있습니다.*/
	private ArrayList<TossCashReceiptVO> cashReceiptsVO;
	
	/**카드사의 즉시 할인 프로모션 정보입니다. 즉시 할인 프로모션이 적용됐을 때만 생성됩니다.*/
	private Object discount;
}
