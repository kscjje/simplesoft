package com.simplesoft.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletResponse;


/**
 * 엑셀 관련 Utils
 * @author mksong
 * @Date 2021. 12. 10
 *
 */
public class ExcelUtils {
	
	/**
	 * 엑셀 생성 및 기본 세팅
	* @methodName : createExcel
	* @author     : mksong
	* @date       : 2022.01.04
	*
	* @param response
	* @param sheetName
	* @param columnList
	* @return
	 */
	public static XSSFWorkbook createExcel(HttpServletResponse response, String sheetName, String [] columnList) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		
		//행
		Row row = null;					
		//셀
		Cell cell = null;
		
		//sheet 생성
		Sheet  sheet = workbook.createSheet(sheetName);
		
		//스타일
		XSSFCellStyle styleHd =  (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFCellStyle styleStringCenter =  (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		XSSFCellStyle styleDate =  (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		
		Font font = workbook.createFont();
		font.setBold(false);
		
		styleHd.setFont(font);
//		styleHd.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
//		styleHd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHd.setBorderTop(BorderStyle.MEDIUM);
		styleHd.setBorderBottom(BorderStyle.MEDIUM);
		styleHd.setBorderLeft(BorderStyle.THIN);
		styleHd.setBorderRight(BorderStyle.THIN);
		styleHd.setAlignment(HorizontalAlignment.CENTER);
		styleHd.setVerticalAlignment (VerticalAlignment.CENTER);
		
		Font font2 = workbook.createFont();
		font2.setBold(false);
		styleStringCenter.setBorderBottom(BorderStyle.THIN);
		styleStringCenter.setBorderLeft(BorderStyle.THIN);
		styleStringCenter.setBorderRight(BorderStyle.THIN);
		styleStringCenter.setFont(font2);
		styleStringCenter.setAlignment(HorizontalAlignment.CENTER);
		styleStringCenter.setVerticalAlignment (VerticalAlignment.CENTER);
		styleStringCenter.setDataFormat(0x31);
		
		Font font3 = workbook.createFont();
		font3.setBold(false);
		styleDate.setBorderBottom(BorderStyle.THIN);
		styleDate.setBorderLeft(BorderStyle.THIN);
		styleDate.setBorderRight(BorderStyle.THIN);
		styleDate.setFont(font3);
		styleDate.setAlignment(HorizontalAlignment.CENTER);
		styleDate.setVerticalAlignment (VerticalAlignment.CENTER);
		styleDate.setDataFormat((short) 0);
		
		// TODO 2022-09-29 :: 셀 줄바꿈 허용 => \n
		styleHd.setWrapText(true);
		
		row = sheet.createRow(0);
		for(int i=0;i<columnList.length;i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnList[i]);
			cell.setCellStyle(styleHd);
		}
		
		return workbook;
	}

	/**
	 * 엑셀 Write
	* @methodName : writeExcel
	* @author     : mksong
	* @date       : 2021.12.01
	*
	* @param response
	* @param workbook
	* @param excelFileName
	 */
	public static void writeExcel(HttpServletResponse response, XSSFWorkbook workbook, String excelFileName) {
		try {
			response.setHeader("Set-Cookie", "fileDownload=true,true; path=/");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelFileName, "UTF-8"));
			
			workbook.write(response.getOutputStream());
		} catch (Exception ex) {
        	response.setHeader("Set-Cookie", "fileDownload=true,false; path=/");
        	response.setContentType("application/vnd.ms-excel;charset=utf-8");
        	response.setHeader("Content-Disposition", "attachment;filename=error.xlsx");
        	
 	        //throw new RuntimeException(ex.getMessage());
		} finally {
			try {
				response.flushBuffer();
				if(workbook !=null) workbook.close();
			} catch (Exception ex) {
				response.setHeader("Set-Cookie", "fileDownload=true,false; path=/");
	        	response.setHeader("Set-Cookie", "fileError=true; path=/");
	        	response.setHeader("Content-Disposition", "attachment;filename=error.xlsx");
	 	        
//	 	        throw new RuntimeException(ex.getMessage());		
			}
		}
	}
	
	public static List<HashMap<String, String>> readExcelToList(final MultipartFile multipartFile, String[] keyNm) throws IOException, InvalidFormatException {
		final Workbook workbook = readWorkbook(multipartFile);
		
		String origName = multipartFile.getOriginalFilename(); // 원본파일명
		origName = origName.substring(origName.lastIndexOf("\\") + 1);		//ie 대응
		
		List<HashMap<String, String>> excelDateList = null;

		if (origName.endsWith(ExcelConstant.XLSX) || origName.endsWith(ExcelConstant.XLSX.toUpperCase())) {
			excelDateList = readExcelXlsx(workbook, keyNm, 1);
		} else if (origName.endsWith(ExcelConstant.XLS) || origName.endsWith(ExcelConstant.XLS.toUpperCase())) {
			excelDateList = readExcelXls(workbook, keyNm, 1);
		}

		return excelDateList;
	}

	public String getSheetNm(final MultipartFile multipartFile) throws IOException, InvalidFormatException {
		final Workbook workbook = readWorkbook(multipartFile);
		return workbook.getSheetName(0);
	}

	private static Workbook readWorkbook(final MultipartFile multipartFile) throws IOException, InvalidFormatException {
		verifyFileExtension(multipartFile);
		return multipartFileToWorkbook(multipartFile);
	}

	private static void verifyFileExtension(MultipartFile multipartFile) throws InvalidFormatException {
		if (!isExcelExtension(multipartFile.getOriginalFilename())) {
			throw new InvalidFormatException("This file extension is not verify", multipartFile, null);
		}
	}

	private static boolean isExcelExtension(String fileName) {
		return fileName.endsWith(ExcelConstant.XLS) || fileName.endsWith(ExcelConstant.XLSX);
	}

	private static boolean isExcelXls(String fileName) {
		return fileName.endsWith(ExcelConstant.XLS);
	}

	private static Workbook multipartFileToWorkbook(MultipartFile multipartFile) throws IOException {
		if (isExcelXls(multipartFile.getOriginalFilename())) {
			return new HSSFWorkbook(multipartFile.getInputStream());
		} else {
			return new XSSFWorkbook(multipartFile.getInputStream());
		}
	}

	/**
	 * 업로드 엑셀이 확장자가 xlsx 일경우
	 *
	 * @param savedFile
	 * @param keyNm
	 * @return
	 * @throws IOException
	 */
	private static List<HashMap<String, String>> readExcelXlsx(Workbook workbook, String[] keyNm, int rowIndex) throws IOException {

		XSSFWorkbook wb = (XSSFWorkbook) workbook;

		List<HashMap<String, String>> excelDate = new ArrayList<HashMap<String, String>>();

		try {

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				XSSFSheet sheet = wb.getSheetAt(i);
				XSSFRow row;
				XSSFCell cell;

				int rows = sheet.getPhysicalNumberOfRows(); // 행
                int cells = sheet.getRow(i).getPhysicalNumberOfCells(); // 셀

                for (int r = 0; r < rows; r++) {
                	
                	if(r < rowIndex) {
                		continue;
                	}
                	
                    row = sheet.getRow(r); // row 가져오기

                    if (row != null) {
                    	HashMap<String, String> dateMap = new HashMap<String, String>();
                    	String strVal = "";

                        for (int c = 0; c < cells; c++) {
                           	
                        	if(c >= keyNm.length) {
                        		continue;
                        	}
                        	
                        	cell = row.getCell(c);

                            if (cell != null) {
                                switch (cell.getCellType()) {

	                                case STRING:
	    								//System.out.print(cell.getRichStringCellValue().getString());
	    								strVal = cell.getRichStringCellValue().getString();
	    								break;
	    							case NUMERIC:
	    								if (DateUtil.isCellDateFormatted(cell)) {
	    									java.util.Date dateValue = cell.getDateCellValue();
	    									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    									//System.out.print(format.format(dateValue)); // 2009-05-29
	    									strVal = format.format(dateValue);
	    								} else {
	    									//System.out.print(Double.valueOf(cell.getNumericCellValue()).intValue());
	    									//strVal = Double.valueOf(cell.getNumericCellValue()).intValue() + "";
	    									long l = (long) cell.getNumericCellValue();
	    									double dd = (double)l;
	    									strVal = cell.getNumericCellValue() == dd ? l+"" : cell.getNumericCellValue()+"";

	    								}
	    								break;
	    							case FORMULA:
	    								//System.out.print(cell.getCellFormula());
	    								strVal = cell.getCellFormula();
	    								break;
	    							case BOOLEAN:
	    								//System.out.print(cell.getBooleanCellValue());
	    								if (cell.getBooleanCellValue()) {
	    									strVal = "true";
	    								} else {
	    									strVal = "false";
	    								}
	    								break;
	    							case ERROR:
	    								//System.out.print(cell.getErrorCellValue());
	    								break;
	    							case BLANK:
	    								strVal = null;
	    								break;
	    							default:
	    								break;

                                }
                                dateMap.put(keyNm[c], strVal);
                            } else {
                            	dateMap.put(keyNm[c], null);
                            }

                        }

                        excelDate.add(dateMap);
                    }
                }
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return excelDate;
	}

	/**
	 * 
	  * @Method Name : readExcelXls
	  * @작성일 : 2020. 5. 5.
	  * @작성자 : CSP
	  * @변경이력 : 
	  * @Method 설명 :업로드 엑셀이 확장자가 xls 일경우
	  * @param workbook
	  * @param keyNm
	  * @return
	  * @throws IOException
	 */
	private static List<HashMap<String, String>> readExcelXls(Workbook workbook, String[] keyNm, int rowIndex) throws IOException {

		HSSFWorkbook wb = (HSSFWorkbook) workbook;

		List<HashMap<String, String>> excelDate = new ArrayList<HashMap<String, String>>();

		try {

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				HSSFRow row;
				HSSFCell cell;

				int rows = sheet.getPhysicalNumberOfRows(); // 행
                int cells = sheet.getRow(i).getPhysicalNumberOfCells(); // 셀

                for (int r = 0; r < rows; r++) {
                	
                	if(r < rowIndex) {
                		continue;
                	}
                	
                    row = sheet.getRow(r); // row 가져오기

                    if (row != null) {
                    	HashMap<String, String> dateMap = new HashMap<String, String>();
                    	String strVal = "";

                        for (int c = 0; c < cells; c++) {
                        	
                        	if(c >= keyNm.length) {
                        		continue;
                        	}
                        	
                        	cell = row.getCell(c);

                            if (cell != null) {
                                switch (cell.getCellType()) {

	                                case STRING:
	    								//System.out.print(cell.getRichStringCellValue().getString());
	    								strVal = cell.getRichStringCellValue().getString();
	    								break;
	    							case NUMERIC:
	    								if (DateUtil.isCellDateFormatted(cell)) {
	    									java.util.Date dateValue = cell.getDateCellValue();
	    									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    									//System.out.print(format.format(dateValue)); // 2009-05-29
	    									strVal = format.format(dateValue);
	    								} else {
	    									//System.out.print(Double.valueOf(cell.getNumericCellValue()).intValue());
	    									//strVal = Double.valueOf(cell.getNumericCellValue()).intValue() + "";
	    									long l = (long) cell.getNumericCellValue();
	    									double dd = (double)l;
	    									strVal = cell.getNumericCellValue() == dd ? l+"" : cell.getNumericCellValue()+"";

	    								}
	    								break;
	    							case FORMULA:
	    								//System.out.print(cell.getCellFormula());
	    								strVal = cell.getCellFormula();
	    								break;
	    							case BOOLEAN:
	    								//System.out.print(cell.getBooleanCellValue());
	    								if (cell.getBooleanCellValue()) {
	    									strVal = "true";
	    								} else {
	    									strVal = "false";
	    								}
	    								break;
	    							case ERROR:
	    								//System.out.print(cell.getErrorCellValue());
	    								break;
	    							case BLANK:
	    								strVal = null;
	    								break;
	    							default:
	    								break;

                                }
                                dateMap.put(keyNm[c], strVal);
                            } else {
                            	dateMap.put(keyNm[c], null);
                            }

                        }

                        excelDate.add(dateMap);
                    }
                }
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return excelDate;
	}
	
	/**
	 * 이미지 셀 삽입
	* @methodName : insertImageCell
	* @author     : mksong
	* @date       : 2022.01.04
	*
	* @param wb
	* @param sheet
	* @param imgPath
	* @param row
	* @param col
	 */
	public static void insertImageCell(XSSFWorkbook wb, XSSFSheet sheet, String imgPath, int row, int col) {
		try {
			InputStream iStream = new FileInputStream(imgPath);
			byte[] bytes;
			bytes = IOUtils.toByteArray(iStream);
			
			int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			XSSFDrawing patriarch = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = new XSSFClientAnchor();
			anchor.setCol1(col);
			anchor.setRow1(row);
			anchor.setCol2(col+1);
			anchor.setRow2(row+1);
			
			anchor.setDx1(1);
			anchor.setDx2((int)(256 * 1.14388 * 15));
			anchor.setDy1(1);
			anchor.setDy2(255);
			
			patriarch.createPicture(anchor, pictureIdx);
			iStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 이미지 셀 삽입
	* @methodName : insertImageCell
	* @author     : mksong
	* @date       : 2022.01.04
	*
	* @param wb
	* @param sheet
	* @param imgPath
	* @param row
	* @param col
	 */
	public static void insertImageCell(XSSFWorkbook wb, XSSFSheet sheet, int row, int col, byte[] qrToTistory) {
		byte[] bytes;
		bytes = qrToTistory;
		
		int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
		XSSFDrawing patriarch = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = new XSSFClientAnchor();
		anchor.setCol1(col);
		anchor.setRow1(row);
		anchor.setCol2(col+1);
		anchor.setRow2(row+1);
		
		anchor.setDx1(1);
		anchor.setDx2((int)(256 * 1.14388 * 15));
		anchor.setDy1(1);
		anchor.setDy2(255);
		
		patriarch.createPicture(anchor, pictureIdx);
	}
}
