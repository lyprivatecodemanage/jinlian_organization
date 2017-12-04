package com.xiangshangban.organization.util;

import java.util.UUID;

public class FormatUtil {

	/**
	 * 生成UUID
	 * @return
	 */
	public static String createUuid(){
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
	
	public static void main(String[] args) {
		for(int i=0;i<72;i++){
			System.out.println(createUuid());
		}
	}
}
