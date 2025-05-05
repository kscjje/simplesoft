package com.simplesoft.common.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Class Name : TossTransferVO.java
 * @Description : 토스 계좌이체 객체 : 계좌이체로 결제했을 때 이체 정보가 담기는 객체입니다
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
public class TossTransferVO {
	
	/**은행 숫자 코드입니다. 은행 코드를 참고하세요.*/
	private String bankCode;
	
	/**정산 상태입니다. 정산이 아직 되지 않았다면 INCOMPLETED, 정산이 완료됐다면 COMPLETED 값이 들어옵니다.*/
	private String settlementStatus;
}
