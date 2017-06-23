package com.xiaoleilu.hutool.io.resource;

import java.util.regex.Pattern;

import com.xiaoleilu.hutool.lang.Matcher;
import com.xiaoleilu.hutool.lang.PatternPool;
import com.xiaoleilu.hutool.lang.StrSpliter;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Ant风格路径匹配器<br>
 * 路径支持通配符，通配符格式如下：
 * <pre>
 * 1、? 匹配一个字符
 * 2、* 匹配0个或多个字符
 * 3、** 匹配0个或多个目录
 * <pre>
 * 
 * @author Looly
 *
 */
public class AntPathMatcher implements Matcher<String>{
	
	public static final char SEPERATOR = StrUtil.C_SLASH;
	
	private String pattern;
	String[] patterns;
	
	public AntPathMatcher(String pattern) {
		this.pattern = pattern;
		this.patterns = StrSpliter.splitPathToArray(pattern);
	}
	
	@Override
	public boolean match(final String path) {
		final String[] paths = StrSpliter.splitPathToArray(path);
		
		int patternStart = 0;
		int patternEnd = patterns.length;
		int pathStart = 0;
		int pathEnd = paths.length;
		
		//从前向后遍历
		String subPattern;
		while(patternStart < patternEnd && pathStart < pathEnd){
			subPattern = patterns[patternStart];
			if("**".equals(subPattern)){
				break;
			}
			
			if(false == isSubPathMatch(subPattern, paths[pathStart])){
				//某个子路径不匹配，则全局不匹配
				return false;
			}
			patternStart++;
			pathStart++;
		}
		
		//判断是否遍历结束
		if(pathStart >= pathEnd){
			if(patternStart >= patternEnd){
				//路径和表达式同时遍历结束，则检查末尾路劲分隔符是否一致，一致表示匹配
				return StrUtil.endWith(pattern, SEPERATOR) ? StrUtil.endWith(path, SEPERATOR) : !StrUtil.endWith(path, SEPERATOR);
			}else if(patternStart == patternEnd - 1 && patterns[patternStart].equals("*") && StrUtil.endWith(path, SEPERATOR)){
				//边界情况：当路径完整遍历，但是表达式还剩一项，此时表示最后一项的*可以与分界符匹配
				//既：pattern: /aaa/bbb/* path: /aaa/bbb/ 这两者是匹配的
				return true;
			}else{
				//表达式并未结束情况下，如果剩余表达式非"**"，表示不匹配
				for (int i = patternStart; i < patternEnd; i++) {
					if (false == patterns[i].equals("**")) {
						return false;
					}
				}
			}
		}else if(patternStart >= patternEnd){
			return false;//表达式遍历结束但是路径未遍历结束，非匹配
		}
		
		//遍历未结束，说明在路径中间有**，此时从后向前遍历
		while(patternStart < patternEnd && pathStart < pathEnd){
			subPattern = patterns[patternEnd];
			if("**".equals(subPattern)){
				break;
			}
			
			if(false == isSubPathMatch(subPattern, paths[pathEnd])){
				//某个子路径不匹配，则全局不匹配
				return false;
			}
			patternEnd--;
			pathEnd--;
		}
		
		//TODO 中间部分匹配
		
		return false;
	}

	/**
	 * 指定路径是否匹其Ant表达式
	 * @param antPattern Ant表达式
	 * @param subPath 子路径
	 * @return 是否匹配
	 */
	private boolean isSubPathMatch(String antPattern, String subPath){
		return toPattern(antPattern).matcher(subPath).matches();
	}
	
	/**
	 * 将Ant风格表达式转为正则{@link Pattern}
	 * @param antPattern Ant风格表达式
	 * @return {@link Pattern}
	 */
	private Pattern toPattern(String antPattern){
		int len = antPattern.length();
		final StringBuilder sb = new StringBuilder(len << 2);
		char c;
		for(int i = 0; i < len; i++){
			c = antPattern.charAt(i);
			if(c == '?'){
				sb.append(".?");
			}else if(c == '*'){
				sb.append(".*?");
			}else{
				sb.append(Pattern.quote(String.valueOf(c)));
			}
		}
		return PatternPool.get(sb.toString());
	}
}
