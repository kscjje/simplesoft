package com.simplesoft.common.service;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Class Name : BizBatchVO.java
 * @Description : 메시지 BATCH정보 VO
 * @Modification Information
 *               수정일 수정자 수정내용
 *               ------- ------- -------------------
 *               2023. 02. 02. 정세진
 * @author 정세진
 * @since 2023. 02. 02.
 * @version
 * @see
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BizBatchVO implements Serializable{
	private static final long serialVersionUID = 1L;

	//메세지전송배치 ID(PK)
	private String bizId;
	//메세지그룹고유번호
	private String msgGrpNo;
	//메세지순번
	private String bizBatchNo;
	//메세지 타입 (SMS,LMS,MMS,AT,FT,RCS)
	private String msgType;
	//수신번호
	private String destTel;
	//수신자명
	private String destNm;
	//발신번호
	private String sendTel;
	//발신자명
	private String sendNm;
	//LMS용제목
	private String subject;
	//메세지내용
	private String sendMsg;
	//발송예정시간(UNIXTIME
	private String sendtime;
	//고객사에서 부여한 키
	private String refkey;
	//발신프로필키
	private String senderKey;
	//템플릿 코드
	private String templateCode;
	//대체 메세지 사용 여부
	private String reType;
	//대체 메세지 내용
	private String reBody;
	//발송결과
	private String status;
	//배치처리여부
	private String batchYn;
	//배치처리시간
	private String batchDt;
	//메세지키
	private String messagekey;
	//결과메세지
	private String resultMsg;
	//등록일시
 	private String regdate;			/*등록일시*/
	//등록자
 	private String reguser;			/*등록자*/
	//메세지 유형
	private String device;
	//메시지 키
	private String cmsgid;
	//비즈뿌리오 메시지 키
	private String msgid;
	//수신번호
	private String phone;
	//실제 발송된 메시지 상세유형
	private String media;
	//발송시간
	private String sendertime;
	//이통사/카카오/RCS결과 코드
	private String resultCd; 
	//정산용 부서 코드
	private String userdata;
	//이통사/카카오 정보
	private String wapinfo;
	//이통사 대체 전송 결과
	private String telres;
	//이통사 대체 전송 시간
	private String teltime;
	//대체 전송 정보
	private String retryFlag;
	//대체 전송 메세지 유형
	private String resendFlag;
	
	//응답코드
	private String retCode;
	//응답메세지
	private String retMsg;
	
}
