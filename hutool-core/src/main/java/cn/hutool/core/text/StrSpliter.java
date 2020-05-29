package cn.hutool.core.text;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串切分器
 * @author Looly
 *
 */
public class StrSpliter {
	
	//---------------------------------------------------------------------------------------------- Split by char
	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * 
	 * @param str 被切分的字符串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitPath(String str){
		return splitPath(str, 0);
	}
	
	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * 
	 * @param str 被切分的字符串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitPathToArray(String str){
		return toArray(splitPath(str));
	}
	
	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * 
	 * @param str 被切分的字符串
	 * @param limit 限制分片数
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitPath(String str, int limit){
		return split(str, StrUtil.C_SLASH, limit, true, true);
	}
	
	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * 
	 * @param str 被切分的字符串
	 * @param limit 限制分片数
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitPathToArray(String str, int limit){
		return toArray(splitPath(str, limit));
	}
	
	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrim(String str, char separator, boolean ignoreEmpty){
		return split(str, separator, 0, true, ignoreEmpty);
	}
	
	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(String str, char separator, boolean isTrim, boolean ignoreEmpty){
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}
	
	/**
	 * 切分字符串，大小写敏感，去除每个元素两边空白符
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数，-1不限制
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitTrim(String str, char separator, int limit, boolean ignoreEmpty){
		return split(str, separator, limit, true, ignoreEmpty, false);
	}
	
	/**
	 * 切分字符串，大小写敏感
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数，-1不限制
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty){
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}
	
	/**
	 * 切分字符串，忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数，-1不限制
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitIgnoreCase(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty){
		return split(str, separator, limit, isTrim, ignoreEmpty, true);
	}
	
	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数，-1不限制
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase 是否忽略大小写
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> split(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase){
		if(StrUtil.isEmpty(str)){
			return new ArrayList<>(0);
		}
		if(limit == 1){
			return addToList(new ArrayList<>(1), str, isTrim, ignoreEmpty);
		}
		
		final ArrayList<String> list = new ArrayList<>(limit > 0 ? limit : 16);
		int len = str.length();
		int start = 0;//切分后每个部分的起始
		for(int i = 0; i < len; i++){
			if(NumberUtil.equals(separator, str.charAt(i), ignoreCase)){
				addToList(list, str.substring(start, i), isTrim, ignoreEmpty);
				start = i+1;//i+1同时将start与i保持一致
				
				//检查是否超出范围（最大允许limit-1个，剩下一个留给末尾字符串）
				if(limit > 0 && list.size() > limit-2){
					break;
				}
			}
		}
		return addToList(list, str.substring(start, len), isTrim, ignoreEmpty);//收尾
	}
	
	/**
	 * 切分字符串为字符串数组
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(String str, char separator, int limit, boolean isTrim, boolean ignoreEmpty){
		return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
	}
	
	//---------------------------------------------------------------------------------------------- Split by String
	
	/**
	 * 切分字符串，不忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(String str, String separator, boolean isTrim, boolean ignoreEmpty){
		return split(str, separator, -1, isTrim, ignoreEmpty, false);
	}
	
	/**
	 * 切分字符串，去除每个元素两边空格，忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrim(String str, String separator, boolean ignoreEmpty){
		return split(str, separator, true, ignoreEmpty);
	}
	
	/**
	 * 切分字符串，不忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty){
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}
	
	/**
	 * 切分字符串，去除每个元素两边空格，忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param limit 限制分片数
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrim(String str, String separator, int limit, boolean ignoreEmpty){
		return split(str, separator, limit, true, ignoreEmpty);
	}
	
	/**
	 * 切分字符串，忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitIgnoreCase(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty){
		return split(str, separator, limit, isTrim, ignoreEmpty, true);
	}
	
	/**
	 * 切分字符串，去除每个元素两边空格，忽略大小写
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param limit 限制分片数
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrimIgnoreCase(String str, String separator, int limit, boolean ignoreEmpty){
		return split(str, separator, limit, true, ignoreEmpty, true);
	}
	
	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符串
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase 是否忽略大小写
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> split(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty, boolean ignoreCase){
		if(StrUtil.isEmpty(str)){
			return new ArrayList<>(0);
		}
		if(limit == 1){
			return addToList(new ArrayList<>(1), str, isTrim, ignoreEmpty);
		}
		
		if(StrUtil.isEmpty(separator)){//分隔符为空时按照空白符切分
			return split(str, limit);
		}else if(separator.length() == 1){//分隔符只有一个字符长度时按照单分隔符切分
			return split(str, separator.charAt(0), limit, isTrim, ignoreEmpty, ignoreCase);
		}
		
		final ArrayList<String> list = new ArrayList<>();
		int len = str.length();
		int separatorLen = separator.length();
		int start = 0;
		int i = 0;
		while(i < len){
			i = StrUtil.indexOf(str, separator, start, ignoreCase);
			if(i > -1){
				addToList(list, str.substring(start, i), isTrim, ignoreEmpty);
				start = i + separatorLen;
				
				//检查是否超出范围（最大允许limit-1个，剩下一个留给末尾字符串）
				if(limit > 0 && list.size() > limit-2){
					break;
				}
			}else{
				break;
			}
		}
		return addToList(list, str.substring(start, len), isTrim, ignoreEmpty);
	}
	
	/**
	 * 切分字符串为字符串数组
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(String str, String separator, int limit, boolean isTrim, boolean ignoreEmpty){
		return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
	}
	
	//---------------------------------------------------------------------------------------------- Split by Whitespace
	
	/**
	 * 使用空白符切分字符串<br>
	 * 切分后的字符串两边不包含空白符，空串或空白符串并不做为元素之一
	 * 
	 * @param str 被切分的字符串
	 * @param limit 限制分片数
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(String str, int limit){
		if(StrUtil.isEmpty(str)){
			return new ArrayList<>(0);
		}
		if(limit == 1){
			return addToList(new ArrayList<>(1), str, true, true);
		}
		
		final ArrayList<String> list = new ArrayList<>();
		int len = str.length();
		int start = 0;//切分后每个部分的起始
		for(int i = 0; i < len; i++){
			if(CharUtil.isBlankChar(str.charAt(i))){
				addToList(list, str.substring(start, i), true, true);
				start = i+1;//i+1同时将start与i保持一致
				
				//检查是否超出范围（最大允许limit-1个，剩下一个留给末尾字符串）
				if(limit > 0 && list.size() > limit-2){
					break;
				}
			}
		}
		return addToList(list, str.substring(start, len), true, true);//收尾
	}
	
	/**
	 * 切分字符串为字符串数组
	 * 
	 * @param str 被切分的字符串
	 * @param limit 限制分片数
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(String str, int limit){
		return toArray(split(str, limit));
	}
	
	//---------------------------------------------------------------------------------------------- Split by regex
	/**
	 * 通过正则切分字符串
	 * @param str 字符串
	 * @param separatorRegex 分隔符正则
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitByRegex(String str, String separatorRegex, int limit, boolean isTrim, boolean ignoreEmpty){
		final Pattern pattern = PatternPool.get(separatorRegex);
		return split(str, pattern, limit, isTrim, ignoreEmpty);
	}
	
	/**
	 * 通过正则切分字符串
	 * @param str 字符串
	 * @param separatorPattern 分隔符正则{@link Pattern}
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(String str, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty){
		if(StrUtil.isEmpty(str)){
			return new ArrayList<>(0);
		}
		if(limit == 1){
			return addToList(new ArrayList<>(1), str, isTrim, ignoreEmpty);
		}
		
		if(null == separatorPattern){//分隔符为空时按照空白符切分
			return split(str, limit);
		}
		
		final Matcher matcher = separatorPattern.matcher(str);
		final ArrayList<String> list = new ArrayList<>();
		int len = str.length();
		int start = 0;
		while(matcher.find()){
			addToList(list, str.substring(start, matcher.start()), isTrim, ignoreEmpty);
			start = matcher.end();
			
			//检查是否超出范围（最大允许limit-1个，剩下一个留给末尾字符串）
			if(limit > 0 && list.size() > limit-2){
				break;
			}
		}
		return addToList(list, str.substring(start, len), isTrim, ignoreEmpty);
	}
	
	/**
	 * 通过正则切分字符串为字符串数组
	 * 
	 * @param str 被切分的字符串
	 * @param separatorPattern 分隔符正则{@link Pattern}
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(String str, Pattern separatorPattern, int limit, boolean isTrim, boolean ignoreEmpty){
		return toArray(split(str, separatorPattern, limit, isTrim, ignoreEmpty));
	}
	
	//---------------------------------------------------------------------------------------------- Split by length
	
	/**
	 * 根据给定长度，将给定字符串截取为多个部分
	 * 
	 * @param str 字符串
	 * @param len 每一个小节的长度
	 * @return 截取后的字符串数组
	 */
	public static String[] splitByLength(String str, int len) {
		int partCount = str.length() / len;
		int lastPartCount = str.length() % len;
		int fixPart = 0;
		if (lastPartCount != 0) {
			fixPart = 1;
		}

		final String[] strs = new String[partCount + fixPart];
		for (int i = 0; i < partCount + fixPart; i++) {
			if (i == partCount + fixPart - 1 && lastPartCount != 0) {
				strs[i] = str.substring(i * len, i * len + lastPartCount);
			} else {
				strs[i] = str.substring(i * len, i * len + len);
			}
		}
		return strs;
	}
	
	//---------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 将字符串加入List中
	 * @param list 列表
	 * @param part 被加入的部分
	 * @param isTrim 是否去除两端空白符
	 * @param ignoreEmpty 是否略过空字符串（空字符串不做为一个元素）
	 * @return 列表
	 */
	private static List<String> addToList(List<String> list, String part, boolean isTrim, boolean ignoreEmpty){
		if(isTrim){
			part = StrUtil.trim(part);
		}
		if(false == ignoreEmpty || false == part.isEmpty()){
			list.add(part);
		}
		return list;
	}
	
	/**
	 * List转Array
	 * @param list List
	 * @return Array
	 */
	private static String[] toArray(List<String> list){
		return list.toArray(new String[0]);
	}
	//---------------------------------------------------------------------------------------------------------- Private method end
}
