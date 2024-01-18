package com.simplesoft.util;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 일반 Utils
 */
public class StringUtils {
	
	/**
	 * 올림
	 */
	public static int getCeil(double num1, double num2) {
		return (int)Math.ceil(num1 / num2);
	}
	
	public static double getRound(double num, int position) {
		double ndb = Math.pow(10.0,  position);
		return (Math.round(num * ndb)/ndb);
	}
	
	/**
	 * source값이 null일 경우 defaultValue를 반환하고, null이 아닐 경우 source를 반환합니다.
	 */
	public static String ifNull(String source, String defaultValue)
	{
		if (source == null)
			return defaultValue;
		
		String sourceLow = source.toLowerCase();
		if (sourceLow.equals("null") || sourceLow.equals("(null)") || sourceLow.equals("") || sourceLow.equals("undefined"))
			return defaultValue;
		
		return source;
	}
	
	/**
	 *  length 길이만큼의 임의 문자열을 생성하여 반환합니다.
	 */
	public static String getRandomString(int length)
	{
		final char[] charaters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
				'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z'};
		StringBuffer sb = new StringBuffer();
		Random rn = new Random();
		
		for (int i = 0; i < length; i++)
			sb.append(charaters[rn.nextInt(charaters.length)]);
		return sb.toString();
	}
	
	/**
	 * Client IP 확인
	 */
	public static String getRemoteIP(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
 
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
	} 
	
	/**
	 * source가 정수형인지 여부를 확인합니다.
	 */
	public static Boolean isNumberic(String source)
	{
		try
		{
			Long.parseLong(source);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * source 문자열을 Integer형으로 변환합니다. 만약 문자열이 Integer타입이 아닌 경우 defaultValue를 반환합니다.
	 */
	public static Integer toInteger(String source, Integer defaultValue)
	{
		try
		{
			source = source != null ? source.replace(",", "") : source;
			return Integer.parseInt(source);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * source 문자열을 Long형으로 변환합니다. 만약 문자열이 Integer타입이 아닌 경우 defaultValue를 반환합니다.
	 */
	public static Long toLong(String source, long defaultValue)
	{
		try
		{
			source = source != null ? source.replace(",", "") : source;
			return Long.parseLong(source);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * 날짜 형식 체크
	 */
	public static boolean dateCheck(String date, String format) {
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(format, Locale.KOREA);
        dateFormatParser.setLenient(false);
        try {
            dateFormatParser.parse(date);
            return true;
        } catch (Exception Ex) {
            return false;
        }
    }
	
	/**
	 * Date -->  add days to format string date
	 */
	public static String toDateFormat(Date date, String pattern, int days, String gubun) {

		String format = "";
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(date);
		switch(gubun) {
		case "d":
			c.add(Calendar.DATE,  days);
			break;
		case "m":
			c.add(Calendar.MONTH,  days);
			break;
		case "y":
			c.add(Calendar.YEAR,  days);
			break;
		default :
			break;
		}
		
		date = c.getTime();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.KOREAN);
		format = dateFormat.format(date);

		return format;
	}	
	
	/**
	 * Date -->  add days to date
	 */
	public static Date toDateFormat(Date date, int num, String gubun) {

		Calendar c = Calendar.getInstance(); 
		c.setTime(date);
		switch(gubun) {
		case "d":
			c.add(Calendar.DATE,  num);
			break;
		case "m":
			c.add(Calendar.MONTH,  num);
			break;
		case "y":
			c.add(Calendar.YEAR,  num);
			break;
		default :
			break;
		}

		return c.getTime();
	}		
	
	/**
	 * Date --> format string date
	 */
	public static String toDateFormat(Date date, String pattern) {

		String format = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.KOREAN);
			format = dateFormat.format(date);			
		} catch(Exception ex) {
			format = "";
		}

		return format;
	}
	
	/**
	 * string date to Date()
	 */
	public static Date toDatetime(String date) {
	
		SimpleDateFormat simpleFormat;
		if (!ifNull(date, "").equals("")) {
			if(date.length() == 8) {
				simpleFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
			} else if(date.length() == 12) {
				simpleFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.KOREAN);
			} else if(date.length() == 14) {
				simpleFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
			} else if(date.length() == 19) {
				simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
			} else {
				simpleFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
			}

			try {
				return simpleFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * String to Date	
	 */
	public static Date toDate(String date, String pattern) {
		
		
		if (!ifNull(date, "").equals("")) {
			try {
				SimpleDateFormat simpleFormat = new SimpleDateFormat(pattern, Locale.KOREAN);
				
				return simpleFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * string date to string pattern date
	 */
	public static String toDateStringFormat(String date, String pattern) {
		try {
			Date datetime = toDatetime(date);
			return toDateFormat(datetime, pattern);			
		} catch(Exception ex) {
			return "";
		}
	}
	
	
	/**
	 * script 태그 삭제
	 */
	public static String getRemoveScript(String strContent){
		
		if (strContent == null || strContent.equals("")) {
			return "";
		}
		
		try {
			Pattern patternScript=Pattern.compile("(?i)\\<script(.*?)</script>");
			Pattern FSCommand=Pattern.compile("(?i) FSCommand=[\"']?([^>\"']+)[\"']*");
			Pattern onAbort=Pattern.compile("(?i) onAbort=[\"']?([^>\"']+)[\"']*");
			Pattern onActivate=Pattern.compile("(?i) onActivate=[\"']?([^>\"']+)[\"']*");
			Pattern onAfterPrint=Pattern.compile("(?i) onAfterPrint=[\"']?([^>\"']+)[\"']*");
			Pattern onAfterUpdate=Pattern.compile("(?i) onAfterUpdate=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeActivate=Pattern.compile("(?i) onBeforeActivate=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeCopy=Pattern.compile("(?i) onBeforeCopy=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeCut=Pattern.compile("(?i) onBeforeCut=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeDeactivate=Pattern.compile("(?i) onBeforeDeactivate=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeEditFocus=Pattern.compile("(?i) onBeforeEditFocus=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforePaste=Pattern.compile("(?i) onBeforePaste=[\"']?([^>\"']+)[\"']*");
			Pattern execCommand=Pattern.compile("(?i) execCommand=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforePrint=Pattern.compile("(?i) onBeforePrint=[\"']?([^>\"']+)[\"']*");
			Pattern onerror=Pattern.compile("(?i) onerror=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeUnload=Pattern.compile("(?i) onBeforeUnload=[\"']?([^>\"']+)[\"']*");
			Pattern onBeforeUpdate=Pattern.compile("(?i) onBeforeUpdate=[\"']?([^>\"']+)[\"']*");
			Pattern onBegin=Pattern.compile("(?i) onBegin=[\"']?([^>\"']+)[\"']*");
			Pattern onBlur=Pattern.compile("(?i) onBlur=[\"']?([^>\"']+)[\"']*");
			Pattern onBounce=Pattern.compile("(?i) onBounce=[\"']?([^>\"']+)[\"']*");
			Pattern onCellChange=Pattern.compile("(?i) onCellChange=[\"']?([^>\"']+)[\"']*");
			Pattern onChange=Pattern.compile("(?i) onChange=[\"']?([^>\"']+)[\"']*");
			Pattern onClick=Pattern.compile("(?i) onClick=[\"']?([^>\"']+)[\"']*");
			Pattern onContextMenu=Pattern.compile("(?i) onContextMenu=[\"']?([^>\"']+)[\"']*");
			Pattern onControlSelect=Pattern.compile("(?i) onControlSelect=[\"']?([^>\"']+)[\"']*");
			Pattern onCopy=Pattern.compile("(?i) onCopy=[\"']?([^>\"']+)[\"']*");
			Pattern onCut=Pattern.compile("(?i) onCut=[\"']?([^>\"']+)[\"']*");
			Pattern onDataAvailable=Pattern.compile("(?i) onDataAvailable=[\"']?([^>\"']+)[\"']*");
			Pattern onDataSetChanged=Pattern.compile("(?i) onDataSetChanged=[\"']?([^>\"']+)[\"']*");
			Pattern onDataSetComplete=Pattern.compile("(?i) onDataSetComplete=[\"']?([^>\"']+)[\"']*");
			Pattern onDblClick=Pattern.compile("(?i) onDblClick=[\"']?([^>\"']+)[\"']*");
			Pattern onDeactivate=Pattern.compile("(?i) onDeactivate=[\"']?([^>\"']+)[\"']*");
			Pattern onDrag=Pattern.compile("(?i) onDrag=[\"']?([^>\"']+)[\"']*");
			Pattern onDragEnd=Pattern.compile("(?i) onDragEnd=[\"']?([^>\"']+)[\"']*");
			Pattern onDragLeave=Pattern.compile("(?i) onDragLeave=[\"']?([^>\"']+)[\"']*");
			Pattern onDragEnter=Pattern.compile("(?i) onDragEnter=[\"']?([^>\"']+)[\"']*");
			Pattern onDragOver=Pattern.compile("(?i) onDragOver=[\"']?([^>\"']+)[\"']*");
			Pattern onDragDrop=Pattern.compile("(?i) onDragDrop=[\"']?([^>\"']+)[\"']*");
			Pattern onDragStart=Pattern.compile("(?i) onDragStart=[\"']?([^>\"']+)[\"']*");
			Pattern onDrop=Pattern.compile("(?i) onDrop=[\"']?([^>\"']+)[\"']*");
			Pattern onEnd=Pattern.compile("(?i) onEnd=[\"']?([^>\"']+)[\"']*");
			Pattern onError=Pattern.compile("(?i) onError=[\"']?([^>\"']+)[\"']*");
			Pattern onErrorUpdate=Pattern.compile("(?i) onErrorUpdate=[\"']?([^>\"']+)[\"']*");
			Pattern onFilterChange=Pattern.compile("(?i) onFilterChange=[\"']?([^>\"']+)[\"']*");
			Pattern onFinish=Pattern.compile("(?i) onFinish=[\"']?([^>\"']+)[\"']*");
			Pattern onFocus=Pattern.compile("(?i) onFocus=[\"']?([^>\"']+)[\"']*");
			Pattern onFocusIn=Pattern.compile("(?i) onFocusIn=[\"']?([^>\"']+)[\"']*");
			Pattern onFocusOut=Pattern.compile("(?i) onFocusOut=[\"']?([^>\"']+)[\"']*");
			Pattern onHashChange=Pattern.compile("(?i) onHashChange=[\"']?([^>\"']+)[\"']*");
			Pattern onHelp=Pattern.compile("(?i) onHelp=[\"']?([^>\"']+)[\"']*");
			Pattern onInput=Pattern.compile("(?i) onInput=[\"']?([^>\"']+)[\"']*");
			Pattern onKeyDown=Pattern.compile("(?i) onKeyDown=[\"']?([^>\"']+)[\"']*");
			Pattern onKeyPress=Pattern.compile("(?i) onKeyPress=[\"']?([^>\"']+)[\"']*");
			Pattern onKeyUp=Pattern.compile("(?i) onKeyUp=[\"']?([^>\"']+)[\"']*");
			Pattern onLayoutComplete=Pattern.compile("(?i) onLayoutComplete=[\"']?([^>\"']+)[\"']*");
			Pattern onLoad=Pattern.compile("(?i) onLoad=[\"']?([^>\"']+)[\"']*");
			Pattern onLoseCapture=Pattern.compile("(?i) onLoseCapture=[\"']?([^>\"']+)[\"']*");
			Pattern onMediaComplete=Pattern.compile("(?i) onMediaComplete=[\"']?([^>\"']+)[\"']*");
			Pattern onMediaError=Pattern.compile("(?i) onMediaError=[\"']?([^>\"']+)[\"']*");
			Pattern onMessage=Pattern.compile("(?i) onMessage=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseDown=Pattern.compile("(?i) onMouseDown=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseEnter=Pattern.compile("(?i) onMouseEnter=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseLeave=Pattern.compile("(?i) onMouseLeave=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseMove=Pattern.compile("(?i) onMouseMove=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseOut=Pattern.compile("(?i) onMouseOut=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseOver=Pattern.compile("(?i) onMouseOver=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseUp=Pattern.compile("(?i) onMouseUp=[\"']?([^>\"']+)[\"']*");
			Pattern onMouseWheel=Pattern.compile("(?i) onMouseWheel=[\"']?([^>\"']+)[\"']*");
			Pattern onMove=Pattern.compile("(?i) onMove=[\"']?([^>\"']+)[\"']*");
			Pattern onMoveEnd=Pattern.compile("(?i) onMoveEnd=[\"']?([^>\"']+)[\"']*");
			Pattern onMoveStart=Pattern.compile("(?i) onMoveStart=[\"']?([^>\"']+)[\"']*");
			Pattern onOffline=Pattern.compile("(?i) onOffline=[\"']?([^>\"']+)[\"']*");
			Pattern onOnline=Pattern.compile("(?i) onOnline=[\"']?([^>\"']+)[\"']*");
			Pattern onOutOfSync=Pattern.compile("(?i) onOutOfSync=[\"']?([^>\"']+)[\"']*");
			Pattern onPaste=Pattern.compile("(?i) onPaste=[\"']?([^>\"']+)[\"']*");
			Pattern onPopState=Pattern.compile("(?i) onPopState=[\"']?([^>\"']+)[\"']*");
			Pattern onProgress=Pattern.compile("(?i) onProgress=[\"']?([^>\"']+)[\"']*");
			Pattern onPropertyChange=Pattern.compile("(?i) onPropertyChange=[\"']?([^>\"']+)[\"']*");
			Pattern onReadyStateChange=Pattern.compile("(?i) onReadyStateChange=[\"']?([^>\"']+)[\"']*");
			Pattern onRedo=Pattern.compile("(?i) onRedo=[\"']?([^>\"']+)[\"']*");
			Pattern onRepeat=Pattern.compile("(?i) onRepeat=[\"']?([^>\"']+)[\"']*");
			Pattern onReset=Pattern.compile("(?i) onReset=[\"']?([^>\"']+)[\"']*");
			Pattern onResize=Pattern.compile("(?i) onResize=[\"']?([^>\"']+)[\"']*");
			Pattern onResizeEnd=Pattern.compile("(?i) onResizeEnd=[\"']?([^>\"']+)[\"']*");
			Pattern onResizeStart=Pattern.compile("(?i) onResizeStart=[\"']?([^>\"']+)[\"']*");
			Pattern onResume=Pattern.compile("(?i) onResume=[\"']?([^>\"']+)[\"']*");
			Pattern onReverse=Pattern.compile("(?i) onReverse=[\"']?([^>\"']+)[\"']*");
			Pattern onRowsEnter=Pattern.compile("(?i) onRowsEnter=[\"']?([^>\"']+)[\"']*");
			Pattern onRowExit=Pattern.compile("(?i) onRowExit=[\"']?([^>\"']+)[\"']*");
			Pattern onRowDelete=Pattern.compile("(?i) onRowDelete=[\"']?([^>\"']+)[\"']*");
			Pattern onRowInserted=Pattern.compile("(?i) onRowInserted=[\"']?([^>\"']+)[\"']*");
			Pattern onScroll=Pattern.compile("(?i) onScroll=[\"']?([^>\"']+)[\"']*");
			Pattern onSeek=Pattern.compile("(?i) onSeek=[\"']?([^>\"']+)[\"']*");
			Pattern onSelect=Pattern.compile("(?i) onSelect=[\"']?([^>\"']+)[\"']*");
			Pattern onSelectionChange=Pattern.compile("(?i) onSelectionChange=[\"']?([^>\"']+)[\"']*");
			Pattern onSelectStart=Pattern.compile("(?i) onSelectStart=[\"']?([^>\"']+)[\"']*");
			Pattern onStart=Pattern.compile("(?i) onStart=[\"']?([^>\"']+)[\"']*");
			Pattern onStop=Pattern.compile("(?i) onStop=[\"']?([^>\"']+)[\"']*");
			Pattern onStorage=Pattern.compile("(?i) onStorage=[\"']?([^>\"']+)[\"']*");
			Pattern onSyncRestored=Pattern.compile("(?i) onSyncRestored=[\"']?([^>\"']+)[\"']*");
			Pattern onSubmit=Pattern.compile("(?i) onSubmit=[\"']?([^>\"']+)[\"']*");
			Pattern onTimeError=Pattern.compile("(?i) onTimeError=[\"']?([^>\"']+)[\"']*");
			Pattern onTrackChange=Pattern.compile("(?i) onTrackChange=[\"']?([^>\"']+)[\"']*");
			Pattern onUndo=Pattern.compile("(?i) onUndo=[\"']?([^>\"']+)[\"']*");
			Pattern onUnload=Pattern.compile("(?i) onUnload=[\"']?([^>\"']+)[\"']*");
			Pattern onURLFlip=Pattern.compile("(?i) onURLFlip=[\"']?([^>\"']+)[\"']*");
			Pattern seekSegmentTime=Pattern.compile("(?i) seekSegmentTime=[\"']?([^>\"']+)[\"']*");
			
			Matcher matcherContent=patternScript.matcher(strContent);
			strContent=matcherContent.replaceAll("");
			
			Matcher matcherFSCommand=FSCommand.matcher(strContent);
			strContent=matcherFSCommand.replaceAll("");

			Matcher matcheronAbort=onAbort.matcher(strContent);
			strContent=matcheronAbort.replaceAll("");

			Matcher matcheronActivate=onActivate.matcher(strContent);
			strContent=matcheronActivate.replaceAll("");

			Matcher matcheronAfterPrint=onAfterPrint.matcher(strContent);
			strContent=matcheronAfterPrint.replaceAll("");

			Matcher matcheronAfterUpdate=onAfterUpdate.matcher(strContent);
			strContent=matcheronAfterUpdate.replaceAll("");

			Matcher matcheronBeforeActivate=onBeforeActivate.matcher(strContent);
			strContent=matcheronBeforeActivate.replaceAll("");

			Matcher matcheronBeforeCopy=onBeforeCopy.matcher(strContent);
			strContent=matcheronBeforeCopy.replaceAll("");

			Matcher matcheronBeforeCut=onBeforeCut.matcher(strContent);
			strContent=matcheronBeforeCut.replaceAll("");

			Matcher matcheronBeforeDeactivate=onBeforeDeactivate.matcher(strContent);
			strContent=matcheronBeforeDeactivate.replaceAll("");

			Matcher matcheronBeforeEditFocus=onBeforeEditFocus.matcher(strContent);
			strContent=matcheronBeforeEditFocus.replaceAll("");

			Matcher matcheronBeforePaste=onBeforePaste.matcher(strContent);
			strContent=matcheronBeforePaste.replaceAll("");

			Matcher matcherexecCommand=execCommand.matcher(strContent);
			strContent=matcherexecCommand.replaceAll("");

			Matcher matcheronBeforePrint=onBeforePrint.matcher(strContent);
			strContent=matcheronBeforePrint.replaceAll("");

			Matcher matcheronerror=onerror.matcher(strContent);
			strContent=matcheronerror.replaceAll("");

			Matcher matcheronBeforeUnload=onBeforeUnload.matcher(strContent);
			strContent=matcheronBeforeUnload.replaceAll("");

			Matcher matcheronBeforeUpdate=onBeforeUpdate.matcher(strContent);
			strContent=matcheronBeforeUpdate.replaceAll("");

			Matcher matcheronBegin=onBegin.matcher(strContent);
			strContent=matcheronBegin.replaceAll("");

			Matcher matcheronBlur=onBlur.matcher(strContent);
			strContent=matcheronBlur.replaceAll("");

			Matcher matcheronBounce=onBounce.matcher(strContent);
			strContent=matcheronBounce.replaceAll("");

			Matcher matcheronCellChange=onCellChange.matcher(strContent);
			strContent=matcheronCellChange.replaceAll("");

			Matcher matcheronChange=onChange.matcher(strContent);
			strContent=matcheronChange.replaceAll("");

			Matcher matcheronClick=onClick.matcher(strContent);
			strContent=matcheronClick.replaceAll("");

			Matcher matcheronContextMenu=onContextMenu.matcher(strContent);
			strContent=matcheronContextMenu.replaceAll("");

			Matcher matcheronControlSelect=onControlSelect.matcher(strContent);
			strContent=matcheronControlSelect.replaceAll("");

			Matcher matcheronCopy=onCopy.matcher(strContent);
			strContent=matcheronCopy.replaceAll("");

			Matcher matcheronCut=onCut.matcher(strContent);
			strContent=matcheronCut.replaceAll("");

			Matcher matcheronDataAvailable=onDataAvailable.matcher(strContent);
			strContent=matcheronDataAvailable.replaceAll("");

			Matcher matcheronDataSetChanged=onDataSetChanged.matcher(strContent);
			strContent=matcheronDataSetChanged.replaceAll("");

			Matcher matcheronDataSetComplete=onDataSetComplete.matcher(strContent);
			strContent=matcheronDataSetComplete.replaceAll("");

			Matcher matcheronDblClick=onDblClick.matcher(strContent);
			strContent=matcheronDblClick.replaceAll("");

			Matcher matcheronDeactivate=onDeactivate.matcher(strContent);
			strContent=matcheronDeactivate.replaceAll("");

			Matcher matcheronDrag=onDrag.matcher(strContent);
			strContent=matcheronDrag.replaceAll("");

			Matcher matcheronDragEnd=onDragEnd.matcher(strContent);
			strContent=matcheronDragEnd.replaceAll("");

			Matcher matcheronDragLeave=onDragLeave.matcher(strContent);
			strContent=matcheronDragLeave.replaceAll("");

			Matcher matcheronDragEnter=onDragEnter.matcher(strContent);
			strContent=matcheronDragEnter.replaceAll("");

			Matcher matcheronDragOver=onDragOver.matcher(strContent);
			strContent=matcheronDragOver.replaceAll("");

			Matcher matcheronDragDrop=onDragDrop.matcher(strContent);
			strContent=matcheronDragDrop.replaceAll("");

			Matcher matcheronDragStart=onDragStart.matcher(strContent);
			strContent=matcheronDragStart.replaceAll("");

			Matcher matcheronDrop=onDrop.matcher(strContent);
			strContent=matcheronDrop.replaceAll("");

			Matcher matcheronEnd=onEnd.matcher(strContent);
			strContent=matcheronEnd.replaceAll("");

			Matcher matcheronError=onError.matcher(strContent);
			strContent=matcheronError.replaceAll("");

			Matcher matcheronErrorUpdate=onErrorUpdate.matcher(strContent);
			strContent=matcheronErrorUpdate.replaceAll("");

			Matcher matcheronFilterChange=onFilterChange.matcher(strContent);
			strContent=matcheronFilterChange.replaceAll("");

			Matcher matcheronFinish=onFinish.matcher(strContent);
			strContent=matcheronFinish.replaceAll("");

			Matcher matcheronFocus=onFocus.matcher(strContent);
			strContent=matcheronFocus.replaceAll("");

			Matcher matcheronFocusIn=onFocusIn.matcher(strContent);
			strContent=matcheronFocusIn.replaceAll("");

			Matcher matcheronFocusOut=onFocusOut.matcher(strContent);
			strContent=matcheronFocusOut.replaceAll("");

			Matcher matcheronHashChange=onHashChange.matcher(strContent);
			strContent=matcheronHashChange.replaceAll("");

			Matcher matcheronHelp=onHelp.matcher(strContent);
			strContent=matcheronHelp.replaceAll("");

			Matcher matcheronInput=onInput.matcher(strContent);
			strContent=matcheronInput.replaceAll("");

			Matcher matcheronKeyDown=onKeyDown.matcher(strContent);
			strContent=matcheronKeyDown.replaceAll("");

			Matcher matcheronKeyPress=onKeyPress.matcher(strContent);
			strContent=matcheronKeyPress.replaceAll("");

			Matcher matcheronKeyUp=onKeyUp.matcher(strContent);
			strContent=matcheronKeyUp.replaceAll("");

			Matcher matcheronLayoutComplete=onLayoutComplete.matcher(strContent);
			strContent=matcheronLayoutComplete.replaceAll("");

			Matcher matcheronLoad=onLoad.matcher(strContent);
			strContent=matcheronLoad.replaceAll("");

			Matcher matcheronLoseCapture=onLoseCapture.matcher(strContent);
			strContent=matcheronLoseCapture.replaceAll("");

			Matcher matcheronMediaComplete=onMediaComplete.matcher(strContent);
			strContent=matcheronMediaComplete.replaceAll("");

			Matcher matcheronMediaError=onMediaError.matcher(strContent);
			strContent=matcheronMediaError.replaceAll("");

			Matcher matcheronMessage=onMessage.matcher(strContent);
			strContent=matcheronMessage.replaceAll("");

			Matcher matcheronMouseDown=onMouseDown.matcher(strContent);
			strContent=matcheronMouseDown.replaceAll("");

			Matcher matcheronMouseEnter=onMouseEnter.matcher(strContent);
			strContent=matcheronMouseEnter.replaceAll("");

			Matcher matcheronMouseLeave=onMouseLeave.matcher(strContent);
			strContent=matcheronMouseLeave.replaceAll("");

			Matcher matcheronMouseMove=onMouseMove.matcher(strContent);
			strContent=matcheronMouseMove.replaceAll("");

			Matcher matcheronMouseOut=onMouseOut.matcher(strContent);
			strContent=matcheronMouseOut.replaceAll("");

			Matcher matcheronMouseOver=onMouseOver.matcher(strContent);
			strContent=matcheronMouseOver.replaceAll("");

			Matcher matcheronMouseUp=onMouseUp.matcher(strContent);
			strContent=matcheronMouseUp.replaceAll("");

			Matcher matcheronMouseWheel=onMouseWheel.matcher(strContent);
			strContent=matcheronMouseWheel.replaceAll("");

			Matcher matcheronMove=onMove.matcher(strContent);
			strContent=matcheronMove.replaceAll("");

			Matcher matcheronMoveEnd=onMoveEnd.matcher(strContent);
			strContent=matcheronMoveEnd.replaceAll("");

			Matcher matcheronMoveStart=onMoveStart.matcher(strContent);
			strContent=matcheronMoveStart.replaceAll("");

			Matcher matcheronOffline=onOffline.matcher(strContent);
			strContent=matcheronOffline.replaceAll("");

			Matcher matcheronOnline=onOnline.matcher(strContent);
			strContent=matcheronOnline.replaceAll("");

			Matcher matcheronOutOfSync=onOutOfSync.matcher(strContent);
			strContent=matcheronOutOfSync.replaceAll("");

			Matcher matcheronPaste=onPaste.matcher(strContent);
			strContent=matcheronPaste.replaceAll("");

			Matcher matcheronPopState=onPopState.matcher(strContent);
			strContent=matcheronPopState.replaceAll("");

			Matcher matcheronProgress=onProgress.matcher(strContent);
			strContent=matcheronProgress.replaceAll("");

			Matcher matcheronPropertyChange=onPropertyChange.matcher(strContent);
			strContent=matcheronPropertyChange.replaceAll("");

			Matcher matcheronReadyStateChange=onReadyStateChange.matcher(strContent);
			strContent=matcheronReadyStateChange.replaceAll("");

			Matcher matcheronRedo=onRedo.matcher(strContent);
			strContent=matcheronRedo.replaceAll("");

			Matcher matcheronRepeat=onRepeat.matcher(strContent);
			strContent=matcheronRepeat.replaceAll("");

			Matcher matcheronReset=onReset.matcher(strContent);
			strContent=matcheronReset.replaceAll("");

			Matcher matcheronResize=onResize.matcher(strContent);
			strContent=matcheronResize.replaceAll("");

			Matcher matcheronResizeEnd=onResizeEnd.matcher(strContent);
			strContent=matcheronResizeEnd.replaceAll("");

			Matcher matcheronResizeStart=onResizeStart.matcher(strContent);
			strContent=matcheronResizeStart.replaceAll("");

			Matcher matcheronResume=onResume.matcher(strContent);
			strContent=matcheronResume.replaceAll("");

			Matcher matcheronReverse=onReverse.matcher(strContent);
			strContent=matcheronReverse.replaceAll("");

			Matcher matcheronRowsEnter=onRowsEnter.matcher(strContent);
			strContent=matcheronRowsEnter.replaceAll("");

			Matcher matcheronRowExit=onRowExit.matcher(strContent);
			strContent=matcheronRowExit.replaceAll("");

			Matcher matcheronRowDelete=onRowDelete.matcher(strContent);
			strContent=matcheronRowDelete.replaceAll("");

			Matcher matcheronRowInserted=onRowInserted.matcher(strContent);
			strContent=matcheronRowInserted.replaceAll("");

			Matcher matcheronScroll=onScroll.matcher(strContent);
			strContent=matcheronScroll.replaceAll("");

			Matcher matcheronSeek=onSeek.matcher(strContent);
			strContent=matcheronSeek.replaceAll("");

			Matcher matcheronSelect=onSelect.matcher(strContent);
			strContent=matcheronSelect.replaceAll("");

			Matcher matcheronSelectionChange=onSelectionChange.matcher(strContent);
			strContent=matcheronSelectionChange.replaceAll("");

			Matcher matcheronSelectStart=onSelectStart.matcher(strContent);
			strContent=matcheronSelectStart.replaceAll("");

			Matcher matcheronStart=onStart.matcher(strContent);
			strContent=matcheronStart.replaceAll("");

			Matcher matcheronStop=onStop.matcher(strContent);
			strContent=matcheronStop.replaceAll("");

			Matcher matcheronStorage=onStorage.matcher(strContent);
			strContent=matcheronStorage.replaceAll("");

			Matcher matcheronSyncRestored=onSyncRestored.matcher(strContent);
			strContent=matcheronSyncRestored.replaceAll("");

			Matcher matcheronSubmit=onSubmit.matcher(strContent);
			strContent=matcheronSubmit.replaceAll("");

			Matcher matcheronTimeError=onTimeError.matcher(strContent);
			strContent=matcheronTimeError.replaceAll("");

			Matcher matcheronTrackChange=onTrackChange.matcher(strContent);
			strContent=matcheronTrackChange.replaceAll("");

			Matcher matcheronUndo=onUndo.matcher(strContent);
			strContent=matcheronUndo.replaceAll("");

			Matcher matcheronUnload=onUnload.matcher(strContent);
			strContent=matcheronUnload.replaceAll("");

			Matcher matcheronURLFlip=onURLFlip.matcher(strContent);
			strContent=matcheronURLFlip.replaceAll("");

			Matcher matcherseekSegmentTime=seekSegmentTime.matcher(strContent);
			strContent=matcherseekSegmentTime.replaceAll("");
			
			strContent = getCleanXss(strContent);
						
			return strContent;
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * XSS 필터링
	 */
	public static String getCleanXss(String value) {
		
		if (value == null || value.equals("")) {
			return "";
		}
		
		value = value.replaceAll("eval\\((.*)\\)"                            , "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']" , "\"\"");
		value = value.replaceAll("[\\\"\\\'][\\s]*vbscript:(.*)[\\\"\\\']"   , "\"\"");
		value = value.replaceAll("(?i)<iframe"                                   , "");
		value = value.replaceAll("(?i)<object"                                   , "");
		value = value.replaceAll("(?i)<embed"                                    , "");
		value = value.replaceAll("(?i)document.cookie"                           , "");
		return value;
	}
	
	/**
	 * byte 길이 체크 
	 */
	public static boolean byteCheck(String txt, int standardByte) {
        if (ifNull(txt,"").equals("")) { return true; }
 
        // 바이트 체크 (영문 1, 한글 2, 특문 1)
        int en = 0;
        int ko = 0;
        int etc = 0;
 
        char[] txtChar = txt.toCharArray();
        for (int j = 0; j < txtChar.length; j++) {
            if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
                en++;
            } else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
                ko++;
                ko++;
            } else {
                etc++;
            }
        }
 
        int txtByte = en + ko + etc;
        if (txtByte > standardByte) {
            return false;
        } else {
            return true;
        }
    }
	
	/**
	 * newline to br
	 */
	public static String ReplaceNewLinetoBr(String strContent)
	{
		if (strContent == null || strContent.equals("")) {
			return "";
		}
		
		strContent = getRemoveScript(strContent);
		
	    return strContent.replaceAll("\n", "<br>");
	}	
	
	/**
	 * 허용한 HTML 태그를 제외하고 <,>를 &lt;,&gt;로 치환
	 */
	public static String ReplaceHTMLSpecialChars(String str, String strAllowTag)
	{
		
		if (str == null || str.equals("")) {
			return "";
		}
		
		String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
		String[] allowTags = strAllowTag.split(",");
	    StringBuilder buffer = new StringBuilder();

	    for (int i = 0; i < allowTags.length; i++) {
	    	buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
	    }
	    
	    pattern = pattern.replace("####", buffer.toString());
	    
	    return str.replaceAll(pattern, "");
	}
	
	/**
	 * 비밀번호 패턴 체크
	 */
	public static boolean passwdRuleCheck(String passwd, String predictableStr) {
		char[] passwdCharArr = passwd.toCharArray();
		int passwdCharArrLen = passwdCharArr.length;

		if (passwdCharArrLen < 8)
			return false;
		if (!passwd.matches("[A-Za-z0-9`~\\!@#\\$%\\^&\\*\\(\\)\\-\\+\\[\\]\\{\\}\\|;\\\\'\\:\"\\,\\.\\/<>\\?\\=_]*"))
			return false;
		for (int i = 3; i < passwdCharArrLen; i++) {
			if ((passwdCharArr[(i - 3)] == passwdCharArr[(i - 2)])
					&& (passwdCharArr[(i - 2)] == passwdCharArr[(i - 1)]))
				return false;
		}

		String[] predictableStrArr = predictableStr.split("\\|");
		if ((predictableStrArr != null) && (predictableStrArr.length > 0)) {
			for (int i = 0; i < predictableStrArr.length; i++) {
				if ((predictableStrArr[i] != null) && (predictableStrArr[i].length() > 3)) {
					char[] predictableStrCharArr = predictableStrArr[i].toCharArray();
					for (int j = 4; j < passwdCharArrLen + 1; j++) {
						String passwdStr = passwd.substring(j - 4, j);
						for (int k = 4; k < predictableStrCharArr.length + 1; k++) {
							if (passwdStr.equals(predictableStrArr[i].substring(k - 4, k)))
								return false;
						}
					}
				}
			}
		}

		int matchKind = 0;

		if (passwd.matches(".*[A-Z].*"))
			matchKind++;
		if (passwd.matches(".*[a-z].*"))
			matchKind++;
		if (passwd.matches(".*[0-9].*"))
			matchKind++;
		if (passwd.matches(".*[`~\\!@#\\$%\\^&\\*\\(\\)\\-\\+\\[\\]\\{\\}\\|;\\\\'\\:\"\\,\\.\\/<>\\?\\=_].*"))
			matchKind++;

		if (matchKind < 2)
			return false;
		if ((matchKind < 3) && (passwdCharArrLen < 10))
			return false;

		return true;
	}
	
	/**
	 * 프로토콜 확인
	 */
	public static String checkProtocol(URL url) {
		String protocol = "";
	    if ("https".equals(url.getProtocol())) {
	        protocol = "HTTPS";
	    } else {
	    	protocol = "HTTP";
	    }
	    return protocol;
	}
	
	
	/**
	 * 정상적인 이메일 인지 여부 체크
	 */
	public static boolean isValidEmail(String email) {
		boolean err = false; 
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"; 
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(email); 
		
		if(m.matches()) { 
			err = true; 
		} 
		return err;
	}
	
	/**
	 * 쿼리 문자 필터링
	 */
	public static String getRemoveFilter(String str, boolean checkType) {
		//xss 제거
		if (checkType) {
			str = getRemoveScript(str);
		}
		
		/* 특수문자 공백 처리 */
//		final Pattern SpecialChars = Pattern.compile("['\"#()@;=*/+]");
//		
//		String strInputText = SpecialChars.matcher(str).replaceAll("");
		String strInputText = str;
		
		final String regex = "( select | delete | update | insert | create | alter | drop |--)";

		final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		strInputText = pattern.matcher(strInputText).replaceAll("");
		
		return strInputText;
	}
	
	/**
	 * 현재 년월일시간을 문자열로 변환 출력
	 */
	public static String getTimestamp() {
		String rtnStr = null;
		
		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";
		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		rtnStr = sdfCurrent.format(ts.getTime());
		return rtnStr;
	}
	
	public static String getHttpServletRequestDomain(HttpServletRequest request) {
		return request.getServerName()+":"+request.getServerPort();
	}
	
	/**
	 * 콤마 추가
	 */
	public static String comma(int value) {
		DecimalFormat formatter = new DecimalFormat("###,###,###");
		return formatter.format(value);
	}
	
	/**
	 * 콤마 추가
	 */
	public static String comma(String value) {
		DecimalFormat formatter = new DecimalFormat("###,###,###");
		return formatter.format(Integer.parseInt(value));
	}	
	
	/**
     * 숫자 => 전화번호형식으로 변환
     */
    public static String formatPhoneNumber(String num, String mask) {

        String formatNum = "";
        if (ifNull(num, "").equals("")) return formatNum;
        num = num.replaceAll("-","");

        if (num.length() == 11) {
            if (mask.equals("Y")) {
                formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
            }else{
                formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
            }
        }else if(num.length()==8){
            formatNum = num.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
        }else{
            if(num.indexOf("02")==0){
                if(mask.equals("Y")){
                    formatNum = num.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-****-$3");
                }else{
                    formatNum = num.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                }
            }else{
                if(mask.equals("Y")){
                    formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
                }else{
                    formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
                }
            }
        }
        return formatNum;
    }
}
