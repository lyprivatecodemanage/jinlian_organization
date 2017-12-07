package com.xiangshangban.organization.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

public class POIUtil {
	/**
	 * 2003EXCEL。
	 * 功能描述：获取excel单元中的数据
	 * @param cell excel的单元
	 * @return 单元中的数据
	 */
	public static String getExcelCellValue(Cell cell) {
		DecimalFormat df = new DecimalFormat("#");
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			}
			return df.format(cell.getNumericCellValue());
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue() + "";
		}
		return "";
	}
	/**
	 * 2007EXCEL。
	 * 功能描述：获取excel单元中的数据
	 * @param cell excel的单元
	 * @return 单元中的数据
	 *//*
	public static String getExcelCellValue(Cell cell) {
		DecimalFormat df = new DecimalFormat("#");
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			}
			return df.format(cell.getNumericCellValue());
		case XSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case XSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case XSSFCell.CELL_TYPE_BLANK:
			return "";
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case XSSFCell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue() + "";
		}
		return "";
	}*/
}
