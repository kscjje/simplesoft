package com.simplesoft.order.service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.simplesoft.common.dao.CommonVO;
import com.simplesoft.common.service.TossEasyPayVO;
import com.simplesoft.payments.service.CancelsVO;
import com.simplesoft.payments.service.CardVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO extends CommonVO{
	private String orderNo;				//주문번호
	private int userNo;					//회원번호
	private String orderStatus;			//주문상태
	private String refundStatus;		//환불상태
	private String userSession;			//비회원세션정보
	private String orderPayType;		//주문결제방법
	private int totalProductAmt;		//총상품금액
	private int totalDisAmt;			//총상품할인금액
	private int totalDelyAmt;			//총배송비
	private String marketingAgreeFg;	//마케팅동의여부
	private String delivKind;			//배송유형
	private String packaging;			//포장가방
	private String delivTime;			//배달시간
	
	private String orderName;			//주문자 이름
	private String orderPwd;			//주문서 조회 시 필요한 비회원 비밀번호
	private String orderTel;			//주문자 전화번호
	private String orderPhone;			//주문자 핸드폰
	private String orderPostNum;		//주문자 우편번호
	private String orderAddr;			//주문자 기본주소
	private String orderAddrDetail;		//주문자 상세주소
	private String orderEmail;			//주문자 이메일
	private String receiveName;			//받는사람 이름
	private String receiveTel;			//받는사람 전화번호
	private String receivePhone;		//받는사람 핸드폰
	private String receivePostNum;		//받는사람 우편번호
	private String receiveAddr;			//받는사람 기본주소
	private String receiveAddrDetail;	//받는사람 상세주소
	private String commonPwd;			//공동현관 비밀번호
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mi:ss")
	private String orderPayDt;			//주문일시
	private String bigo;				//전하실말씀
	private String regUser;				//등록자
	private Date regDt;					//등록일시
	
	private List<OrderProductVO> productList;	//주문상품목록
	
	//Payments 테이블
	private String payMethod;			//결제수단
	private String productName;			//상품명
	private String paymentKey;			//결제키
	private int totalAmount;			//상품금액
	
	private String deliveryStatus;		//배송상태
	private String deliveryStatusNm;	//배송상태명
	private String managerNo;			//배송담당자
	private Date deliveryDt;			//배송일시
	
	private List<OrderVO> orderList;	//주문목록
	private int orderCount;				//주문목록 카운트
	
	private int menuDay;				//주문내역 전용
	private int orderQty;				//주문내역 전용
	private String card;				//DB카드정보
	private CardVO cardVO;				//카드정보 포맷
	private String easyPay;				//DB간편결제
	private TossEasyPayVO easyPayVO;	//간편결제 포맷
	private String cancels;				//DB취소정보
	private CancelsVO cancelsVO;		//취소정보 포맷

	@Override
	public String toString() {
		return "OrderVO [orderNo=" + orderNo + ", userNo=" + userNo + ", orderStatus=" + orderStatus + ", userSession="
				+ userSession + ", orderPayType=" + orderPayType + ", totalProductAmt=" + totalProductAmt
				+ ", totalDisAmt=" + totalDisAmt + ", totalDelyAmt=" + totalDelyAmt + ", marketingAgreeFg="
				+ marketingAgreeFg + ", delivKind=" + delivKind + ", packaging=" + packaging
				+ ", delivTime=" + delivTime + ", orderName=" + orderName + ", orderPwd=" + orderPwd + ", orderTel="
				+ orderTel + ", orderPhone=" + orderPhone + ", orderPostNum=" + orderPostNum + ", orderAddr="
				+ orderAddr + ", orderAddrDetail=" + orderAddrDetail + ", orderEmail=" + orderEmail + ", receiveName="
				+ receiveName + ", receiveTel=" + receiveTel + ", receivePhone=" + receivePhone + ", receivePostNum="
				+ receivePostNum + ", receiveAddr=" + receiveAddr + ", receiveAddrDetail=" + receiveAddrDetail
				+ ", commonPwd=" + commonPwd + ", orderPayDt=" + orderPayDt + ", bigo=" + bigo + ", regUser=" + regUser
				+ ", regDt=" + regDt + ", productList=" + productList + ", payMethod=" + payMethod + ", productName="
				+ productName + ", totalAmount=" + totalAmount + ", deliveryStatus=" + deliveryStatus
				+ ", deliveryStatusNm=" + deliveryStatusNm + ", managerNo=" + managerNo + ", deliveryDt=" + deliveryDt
				+ ", orderList=" + orderList + ", orderCount=" + orderCount + ", menuDay=" + menuDay + ", orderQty=" + orderQty + ", card=" + card + "]";
	}
	
	public void setCard(String card) {
		this.card = card;
		this.cardVO = convertCard(); // setter에서 파싱
	}

	public CardVO convertCard() {
		if (card == null || !card.startsWith("{")) return null;
		String body = card.substring(1, card.length() - 1); // 중괄호 제거
		String[] pairs = body.split(", ");
		CardVO cardVO = new CardVO();
		for (String pair : pairs) {
			String[] kv = pair.split("=", 2);
			if (kv.length != 2) continue;
			String key = kv[0];
			String value = kv[1];

			switch (key) {
				case "ownerType": cardVO.ownerType = value; break;
				case "number": cardVO.number = value; break;
				case "amount": cardVO.amount = Integer.parseInt(value); break;
				case "acquireStatus": cardVO.acquireStatus = value; break;
				case "isInterestFree": cardVO.isInterestFree = Boolean.parseBoolean(value); break;
				case "cardType": cardVO.cardType = value; break;
				case "approveNo": cardVO.approveNo = value; break;
				case "installmentPlanMonths": cardVO.installmentPlanMonths = Integer.parseInt(value); break;
				case "interestPayer": cardVO.interestPayer = value.equals("null") ? null : value; break;
				case "issuerCode": cardVO.issuerCode = value; break;
				case "acquirerCode": cardVO.acquirerCode = value; break;
				case "useCardPoint": cardVO.useCardPoint = Boolean.parseBoolean(value); break;
			}
		}
		return cardVO;
	}
	
	public void setEasyPay(String easyPay) {
		this.easyPay = easyPay;
		this.easyPayVO = convertEasyPay(); // setter에서 파싱
	}

	public TossEasyPayVO convertEasyPay() {
		if (easyPay == null || !easyPay.startsWith("{")) return null;
		String body = easyPay.substring(1, easyPay.length() - 1); // 중괄호 제거
		String[] pairs = body.split(", ");
		TossEasyPayVO easyPayVO = new TossEasyPayVO();
		for (String pair : pairs) {
			String[] kv = pair.split("=", 2);
			if (kv.length != 2) continue;
			String key = kv[0];
			String value = kv[1];

			switch (key) {
				case "provider": easyPayVO.provider = value; break;
				case "amount": easyPayVO.amount = Integer.parseInt(value); break;
				case "discountAmount": easyPayVO.discountAmount = Integer.parseInt(value); break;
			}
		}
		return easyPayVO;
	}
	
	public void setCancels(String cancels) {
		this.cancels = cancels;
		this.cancelsVO = convertCancels(); // setter에서 파싱
	}
	public CancelsVO convertCancels() {
		if (cancels == null || !cancels.startsWith("[{")) return null;
		String body = cancels.substring(1, cancels.length() - 1); // 중괄호 제거
		String[] pairs = body.split(", ");
		CancelsVO cancelsVO = new CancelsVO();
		for (String pair : pairs) {
			String[] kv = pair.split("=", 2);
			if (kv.length != 2) continue;
			String key = kv[0];
			String value = kv[1];
			
			switch (key) {
				case "canceledAt": cancelsVO.canceledAt = formatIsoDate(value); break;
				case "cancelReason": cancelsVO.cancelReason = value; break;
			}
		}
		return cancelsVO;
	}
	public static String formatIsoDate(String isoDate) {
		OffsetDateTime dateTime = OffsetDateTime.parse(isoDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return dateTime.format(formatter);
	}
}
