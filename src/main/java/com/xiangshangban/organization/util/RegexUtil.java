package com.xiangshangban.organization.util;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @author 韦友弟
 *
 */
public class RegexUtil {
	/**
	 * 校验手机号
	 * @param phone
	 * @return
	 */
	public static boolean matchPhone(String phone){
		return Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", phone);
	}
	/**
	 * 匹配日期yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static boolean matchDate(String date){
		return Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", date);
	}
}
