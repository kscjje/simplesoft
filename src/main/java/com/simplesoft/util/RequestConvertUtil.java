package com.simplesoft.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class RequestConvertUtil {

	public static String convertMapToParam(Map<String, String[]> paramMap) throws UnsupportedEncodingException{
		String resultString = "";
		for (Map.Entry<String, String[]> mapEntry : paramMap.entrySet()) {
			for (String value : mapEntry.getValue()) {
				resultString += (resultString.isEmpty() ? "?" : "&") + mapEntry.getKey() + "=" + URLEncoder.encode(value, "UTF-8");
			}
		}
		return resultString;
	}
}
