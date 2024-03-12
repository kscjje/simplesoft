package com.simplesoft.reponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * REST 공통 응답 메세지
 * @param <T>
 */
@Getter @Setter
public class CommonResponse<T> extends BasicResponse {
	private int dataLength;
	private String responseCode;
	private String responseMessage;
	private T data;
	
	public CommonResponse(T data) {
		this.data = data;
		if(data instanceof List) {
			this.dataLength = ((List<?>)data).size();
		} else {
			if(data == null) {
				this.dataLength = 0;	
			} else {
				this.dataLength = 1;
			}
		}
		
		this.responseCode = "SUCCESS";
		this.responseMessage = "";
	}
}