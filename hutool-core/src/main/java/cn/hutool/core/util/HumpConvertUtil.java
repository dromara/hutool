package com.rendu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName HumpConvertUtil   字符串驼峰与下划线的互相转换
 * @Description: TODO
 * @Author: li
 * @Date: 2021/4/12 22:42
 * @Version v1.0
 **/
public class HumpConvertUtil {
    
    private static Pattern linePattern = Pattern.compile("_(\\w)");
	
	private static Pattern humpPattern = Pattern.compile("[A-Z]");
    
    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    
    
    /** 驼峰转下划线 */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
