package com.simplesoft.common.service;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @Class Name : PaymentVO.java
 * @Description : 온라인결제처리 VO
 * @Modification Information
 *               수정일 수정자 수정내용
 *               ------- ------- -------------------
 *               2023. 03. 22. 정세진
 * @author 정세진
 * @since 2023. 03. 22.
 * @version
 * @see
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentVO implements Serializable{
	private static final long serialVersionUID = 1L;

	// (description = "거래번호")
	private String oid;
	// (description = "결제금액")
	private String payAmt;
	// (description = "상점아이디(MID)입니다. 토스페이먼츠에서 발급합니다. 최대 길이는 14자입니다.")
	private String mid;
	// (description = "마지막 거래의 키 값입니다. 한 결제 건의 승인 거래와 취소 거래를 구분하는데 사용됩니다. 예를 들어 결제 승인 후 부분 취소를 두 번 했다면 마지막 부분 취소 거래의 키 값이 할당됩니다. 최대 길이는 64자입니다.")
	private String lastTransactionKey;
	// (description = "결제가 일어난 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm으로 돌아옵니다.")
	private String requestedAt;
	// (description = "결제 승인이 일어난 날짜와 시간 정보입니다. ISO 8601 형식인 yyyy-MM-dd'T'HH:mm:ss±hh:mm으로 돌아옵니다.")
	private String approvedAt;
	// (description = "결제키값")
	private String paymentKey;
	// (description = "결제 처리 상태입니다. 아래와 같은 상태 값을 가질 수 있습니다. 상태 변화 흐름이 궁금하다면 흐름도를 살펴보세요.")
	private String status;
	// (description = "가상계좌 웹훅 요청이 정상적인 요청인지 검증하는 값입니다. 결제 승인 API의 응답으로 돌아온 secret과 같으면 정상적인 요청입니다.")
	private String secret;
	// (description = "카드 발급사 숫자 코드입니다. 카드사 코드를 참고하세요.")
	private String issuerCode;
	// (description = "카드 매입사 숫자 코드입니다. 카드사 코드를 참고하세요.")
	private String acquirerCode;
	// (description = "카드 번호입니다. 번호의 일부는 마스킹 되어 있습니다. 최대 길이는 20자입니다.")
	private String number;
	// (description = "할부 개월 수입니다. 일시불이면 0입니다.")
	private String installmentPlanMonths;
	// (description = "카드사 승인 번호입니다. 최대 길이는 8자입니다.")
	private String approveNo;
	// (description = "카드 종류입니다. 신용, 체크, 기프트 중 하나입니다.")
	private String cardType;
	// (description = "카드의 소유자 타입 개인,법인")
	private String ownerType;
	// (description = "은행코드(실시간계좌이체)")	
	private String bankCode;
	// (description = "카드 결제의 매입 상태입니다. 아래와 같은 상태 값을 가질 수 있습니다.")
	private String acquireStatus;
	// (description = "결제할금액")
	private String amount;
	// (description = "결제 타입 정보입니다. NORMAL(일반 결제), BILLING(자동 결제), BRANDPAY(브랜드페이) 중 하나입니다.")
	private String type;
	// (description = "총 결제 금액입니다.")
	private String totalAmount;
	// (description = "취소할 수 있는 금액(잔고)입니다.")
	private String balanceAmount;
	// (description = "공급가액입니다")
	private String suppliedAmount;
	// (description = "부가세입니다. (결제 금액 amount - 면세 금액 taxFreeAmount) / 11 후 소수점 첫째 자리에서 반올림해서 계산합니다.")
	private String vat;
	// (description = "결제 금액 중 면세 금액입니다.")
	private String taxFreeAmount;
	// (description = "결제 수단입니다.카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권 중 하나입니다.")
	private String method;
	// (description = "매출일자YYYYMMDD")
	private String payDate;
	// (description = "매출시간HHMMSS")
	private String payTime;
	
	// (description = "가상계좌결제업무구분")
	private String vbankReqType;
	// (description = "발급된 계좌 번호입니다. 최대 길이는 20자입니다")
	private String accountNumber;
	// (description = "가상계좌 타입을 나타냅니다. 일반, 고정 중 하나입니다. 값이 없으면 일반 가상계좌로 발급됩니다.")
	private String accountType;
	// (description = "가상계좌를 발급한 고객 이름입니다. 최대 길이는 100자입니다.")
	private String customerName;
	// (description = "입금 기한입니다.")
	private String dueDate;
	// (description = "가상계좌가 만료되었는지 여부입니다.")
	private String expired;
	// (description = "정산 상태입니다. 정산이 아직 되지 않았다면 INCOMPLETED, 정산이 완료됐다면 COMPLETED 값이 들어옵니다.")
	private String settlementStatus;
	// (description = "영수증번호")
	private String receiptNo;
	
	// (description = "취소일시")
	private String canceledAt;
	// (description = "취소금액")
	private String cancelAmount;
	// (description = "취소사유")
	private String cancelReason;	
	// (description = "취소상품 고유정보")
	private String cancelItemList; /*예약번호;결제금액;이용료;위약금;환불금액;|*/
	
	// (description = "취소 사유 코드")
	private String refundReasonCd;
	// (description = "환불은행코드")
	private String refundBankCd;
	// (description = "환불계좌번호")
	private String refundAccNo;
	// (description = "환불예금주")
	private String refundAccNm;
	
	
	// (description = "결제 취소용 토스 key")
	private String authKey;
	
	private String paymentsStoreidKey;
}
