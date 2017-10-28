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
}
