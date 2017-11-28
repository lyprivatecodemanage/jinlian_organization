package com.xiangshangban.organization.util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ImportUtil {
	public static boolean isBlankRow(Row row){
		if(row == null){
			return true;
		}
		boolean result = true;
		for(int i = row.getFirstCellNum();i<row.getLastCellNum();i++){
			Cell cell = row.getCell(i,HSSFRow.RETURN_BLANK_AS_NULL);
					String value ="";
			if(cell != null){
				switch(cell.getCellType()){
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
                    value = String.valueOf((int) cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = String.valueOf(cell.getCellFormula());
                    break;
                //case Cell.CELL_TYPE_BLANK:
                //    break;
                default:
                    break;
				}
				if(!value.trim().equals("")){
                    result = false;
                    break;
                }
			}
		}
		return result;
	}
}
