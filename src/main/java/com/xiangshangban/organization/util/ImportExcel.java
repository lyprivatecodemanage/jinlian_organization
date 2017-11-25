package com.xiangshangban.organization.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportExcel {
    public static Map<String,String> getDataFromExcel(String filePath)
    {
    	Map<String,String> result = new HashMap<String,String>();
        //String filePath = "E:\\123.xlsx";
        
        //判断是否为excel类型文件
        if(!filePath.endsWith(".xls")&&!filePath.endsWith(".xlsx"))
        {
            System.out.println("文件不是excel类型");
        }
        
        FileInputStream fis =null;
        Workbook wookbook = null;
        
        try
        {
            //获取一个绝对地址的流
              fis = new FileInputStream(filePath);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       
        try 
        {
            //2003版本的excel，用.xls结尾
            wookbook = new HSSFWorkbook(fis);//得到工作簿
             
        } 
        catch (Exception ex) 
        {
            //ex.printStackTrace();
            try
            {
                //2007版本的excel，用.xlsx结尾
                
                wookbook = new XSSFWorkbook(fis);//得到工作簿
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        int lastSheetNum = wookbook.getNumberOfSheets();
        for(int i=0;i<lastSheetNum;i++){
        //得到一个工作表
        Sheet sheet = wookbook.getSheetAt(i);
        //获得表头
        Row rowHead = sheet.getRow(0);
        //判断表头是否正确
        if(rowHead.getPhysicalNumberOfCells() != 17)
        {
            System.out.println("表头的数量不对!");
        }
        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        //要获得属性
        String name = "";
        int latitude = 0;
       //获得所有数据
        for(int j = 1 ; j <= totalRowNum ; j++)
        {
            //获得第i行对象
            Row row = sheet.getRow(j);
            int lastRowNum = row.getLastCellNum();
            	for(int k=0;k<lastRowNum;k++){
            		Cell cell = row.getCell(k);
            		if(k==2 || k==3 || k==4 || k==6 || k==7 ||k==9||k==10||k==11||k==12){
            			String value = cell.getStringCellValue();
            			if(StringUtils.isEmpty(value)){
            				
            				result.put("message", "");
            			}
            		}
            	}
            //获得获得第i行第0列的 String类型对象
            Cell cell = row.getCell((short)0);
            name = cell.getStringCellValue().toString();
            
            //获得一个数字类型的数据
            cell = row.getCell((short)1);
            latitude = (int) cell.getNumericCellValue();
            
            System.out.println("名字："+name+",经纬度："+latitude);
            
        }
        }
        return result;
    }
}
