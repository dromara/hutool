package com.xiaoleilu.hutool.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.lang.StrFormatter;
import com.xiaoleilu.hutool.lang.StrSpliter;

/**
 * 字符串工具类
 * 
 * @author xiaoleilu
 *
 */
public class StrUtil {

	public static final char C_SPACE = ' ';
	public static final char C_TAB = '	';
	public static final char C_DOT = '.';
	public static final char C_SLASH = '/';
	public static final char C_BACKSLASH = '\\';
	public static final char C_CR = '\r';
	public static final char C_LF = '\n';
	public static final char C_UNDERLINE = '_';
	public static final char C_COMMA = ',';
	public static final char C_DELIM_START = '{';
	public static final char C_DELIM_END = '}';
	public static final char C_BRACKET_START = '[';
	public static final char C_BRACKET_END = ']';
	public static final char C_COLON = ':';

	public static final String SPACE = " ";
	public static final String TAB = "	";
	public static final String DOT = ".";
	public static final String DOUBLE_DOT = "..";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String EMPTY = "";
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String CRLF = "\r\n";
	public static final String UNDERLINE = "_";
	public static final String COMMA = ",";
	public static final String DELIM_START = "{";
	public static final String DELIM_END = "}";
	public static final String BRACKET_START = "[";
	public static final String BRACKET_END = "]";
	public static final String COLON = ":";

	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	public static final String EMPTY_JSON = "{}";

	private StrUtil() {
	}
	
	// ------------------------------------------------------------------------ Blank
	/**
	 * 字符串是否为空白 空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(CharSequence str) {
		int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (false == NumberUtil.isBlankChar(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为不可见字符（如空格）<br>
	 * 3、不为""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotBlank(CharSequence str) {
		return false == isBlank(str);
	}

	/**
	 * 是否包含空字符串
	 * 
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasBlank(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 给定所有字符串是否为空白
	 * 
	 * @param strs 字符串
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllBlank(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isNotBlank(str)) {
				return false;
			}
		}
		return true;
	}

	// ------------------------------------------------------------------------ Empty
	/**
	 * 字符串是否为空，空的定义如下 1、为null <br>
	 * 2、为""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return false == isEmpty(str);
	}

	/**
	 * 当给定字符串为null时，转换为Empty
	 * 
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String nullToEmpty(CharSequence str) {
		return nullToDefault(str, EMPTY);
	}

	/**
	 * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
	 * 
	 * <pre>
	 * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
	 * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str 要转换的字符串
	 * @param defaultStr 默认字符串
	 * 
	 * @return 字符串本身或指定的默认字符串
	 */
	public static String nullToDefault(CharSequence str, String defaultStr) {
		return (str == null) ? defaultStr : str.toString();
	}

	/**
	 * 当给定字符串为空字符串时，转换为<code>null</code>
	 * 
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String emptyToNull(CharSequence str) {
		return isEmpty(str) ? null : str.toString();
	}

	/**
	 * 是否包含空字符串
	 * 
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasEmpty(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否全部为空字符串
	 * 
	 * @param strs 字符串列表
	 * @return 是否全部为空字符串
	 */
	public static boolean isAllEmpty(CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (CharSequence str : strs) {
			if (isNotEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	// ------------------------------------------------------------------------ Trim
	/**
	 * 除去字符串头尾部的空白，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>NumberUtil.isBlankChar</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * trim(null)          = null
	 * trim(&quot;&quot;)            = &quot;&quot;
	 * trim(&quot;     &quot;)       = &quot;&quot;
	 * trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(CharSequence str) {
		return (null == str) ? null : trim(str, 0);
	}

	/**
	 * 给定字符串数组全部做去首尾空格
	 * 
	 * @param strs 字符串数组
	 */
	public static void trim(String[] strs) {
		if (null == strs) {
			return;
		}
		String str;
		for (int i = 0; i < strs.length; i++) {
			str = strs[i];
			if (null != str) {
				strs[i] = str.trim();
			}
		}
	}

	/**
	 * 除去字符串头部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>NumberUtil.isBlankChar</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * trimStart(null)         = null
	 * trimStart(&quot;&quot;)           = &quot;&quot;
	 * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
	 * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
	 * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
	 * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
	 */
	public static String trimStart(CharSequence str) {
		return trim(str, -1);
	}

	/**
	 * 除去字符串尾部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>NumberUtil.isBlankChar</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * trimEnd(null)       = null
	 * trimEnd(&quot;&quot;)         = &quot;&quot;
	 * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
	 * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
	 * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
	 * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
	 */
	public static String trimEnd(CharSequence str) {
		return trim(str, 1);
	}

	/**
	 * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * @param str 要处理的字符串
	 * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部， <code>1</code>表示trimEnd
	 * 
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(CharSequence str, int mode) {
		if (str == null) {
			return null;
		}

		int length = str.length();
		int start = 0;
		int end = length;

		// 扫描字符串头部
		if (mode <= 0) {
			while ((start < end) && (NumberUtil.isBlankChar(str.charAt(start)))) {
				start++;
			}
		}

		// 扫描字符串尾部
		if (mode >= 0) {
			while ((start < end) && (NumberUtil.isBlankChar(str.charAt(end - 1)))) {
				end--;
			}
		}

		if ((start > 0) || (end < length)) {
			return str.toString().substring(start, end);
		}

		return str.toString();
	}
	
	/**
	 * 字符串是否以给定字符开始
	 * @param str 字符串
	 * @param c 字符
	 * @return 是否开始
	 */
	public static boolean startWith(CharSequence str, char c){
		return c == str.charAt(0);
	}

	/**
	 * 是否以指定字符串开头
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean isIgnoreCase) {
		if (isIgnoreCase) {
			return str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
		} else {
			return str.toString().startsWith(prefix.toString());
		}
	}
	
	/**
	 * 是否以指定字符串开头
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, false);
	}

	/**
	 * 是否以指定字符串开头，忽略大小写
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWithIgnoreCase(String str, String prefix) {
		return startWith(str, prefix, true);
	}

	/**
	 * 给定字符串是否以任何一个字符串开始
	 * 
	 * @param str 给定字符串
	 * @param prefixes 需要检测的开始字符串
	 * @return 给定字符串是否以任何一个字符串开始
	 * @since 3.0.6
	 */
	public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
			return false;
		}

		for (CharSequence suffix : prefixes) {
			if (startWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 字符串是否以给定字符结尾
	 * @param str 字符串
	 * @param c 字符
	 * @return 是否结尾
	 */
	public static boolean endWith(CharSequence str, char c){
		return c == str.charAt(str.length()-1);
	}

	/**
	 * 是否以指定字符串结尾
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
		if (isBlank(str) || isBlank(suffix)) {
			return false;
		}

		if (isIgnoreCase) {
			return str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
		} else {
			return str.toString().endsWith(suffix.toString());
		}
	}
	
	/**
	 * 是否以指定字符串结尾
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix) {
		return endWith(str, suffix, false);
	}

	/**
	 * 是否以指定字符串结尾，忽略大小写
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWithIgnoreCase(CharSequence str, String suffix) {
		return endWith(str, suffix, true);
	}

	/**
	 * 给定字符串是否以任何一个字符串结尾
	 * 
	 * @param str 给定字符串
	 * @param suffixes 需要检测的结尾字符串
	 * @return 给定字符串是否以任何一个字符串结尾
	 * @since 3.0.6
	 */
	public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (CharSequence suffix : suffixes) {
			if (endWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含特定字符，忽略大小写，如果给定两个参数都为<code>null</code>，返回true
	 * 
	 * @param str 被检测字符串
	 * @param testStr 被测试是否包含的字符串
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
		if (null == str) {
			// 如果被监测字符串和
			return null == testStr;
		}
		return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
	}

	/**
	 * 获得set或get方法对应的标准属性名<br>
	 * 例如：setName 返回 name
	 * 
	 * @param getOrSetMethodName Get或Set方法名
	 * @return 如果是set或get方法名，返回field， 否则null
	 */
	public static String getGeneralField(CharSequence getOrSetMethodName) {
		final String getOrSetMethodNameStr = getOrSetMethodName.toString();
		if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
			return removePreAndLowerFirst(getOrSetMethodName, 3);
		}
		return null;
	}

	/**
	 * 生成set方法名<br>
	 * 例如：name 返回 setName
	 * 
	 * @param fieldName 属性名
	 * @return setXxx
	 */
	public static String genSetter(CharSequence fieldName) {
		return upperFirstAndAddPre(fieldName, "set");
	}

	/**
	 * 生成get方法名
	 * 
	 * @param fieldName 属性名
	 * @return getXxx
	 */
	public static String genGetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "get");
	}

	/**
	 * 移除字符串中所有给定字符串<br>
	 * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
	 * 
	 * @param str 字符串
	 * @param strToRemove 被移除的字符串
	 * @return 移除后的字符串
	 */
	public static String removeAll(String str, CharSequence strToRemove) {
		return str.replace(strToRemove, EMPTY);
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, preLength=3 =》 return name
	 * 
	 * @param str 被处理的字符串
	 * @param preLength 去掉的长度
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(CharSequence str, int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.toString().substring(preLength + 1);
			}
			return String.valueOf(first);
		} else {
			return str.toString();
		}
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, prefix=set =》 return name
	 * 
	 * @param str 被处理的字符串
	 * @param prefix 前缀
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(CharSequence str, CharSequence prefix) {
		return lowerFirst(removePrefix(str, prefix));
	}

	/**
	 * 原字符串首字母大写并在其首部添加指定字符串 例如：str=name, preString=get =》 return getName
	 * 
	 * @param str 被处理的字符串
	 * @param preString 添加的首部
	 * @return 处理后的字符串
	 */
	public static String upperFirstAndAddPre(CharSequence str, String preString) {
		if (str == null || preString == null) {
			return null;
		}
		return preString + upperFirst(str);
	}

	/**
	 * 大写首字母<br>
	 * 例如：str = name, return Name
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String upperFirst(CharSequence str) {
		if (null == str) {
			return null;
		}
		if (str.length() > 0) {
			char firstChar = str.charAt(0);
			if (Character.isLowerCase(firstChar)) {
				return Character.toUpperCase(firstChar) + subSuf(str, 1);
			}
		}
		return str.toString();
	}

	/**
	 * 小写首字母<br>
	 * 例如：str = Name, return name
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String lowerFirst(CharSequence str) {
		if (null == str) {
			return null;
		}
		if (str.length() > 0) {
			char firstChar = str.charAt(0);
			if (Character.isUpperCase(firstChar)) {
				return Character.toLowerCase(firstChar) + subSuf(str, 1);
			}
		}
		return str.toString();
	}

	/**
	 * 去掉指定前缀
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str.toString();
		}

		final String str2 = str.toString();
		if (str2.startsWith(prefix.toString())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 忽略大小写去掉指定前缀
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefixIgnoreCase(CharSequence str, String prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str.toString();
		}

		final String str2 = str.toString();
		if (str2.toLowerCase().startsWith(prefix.toLowerCase())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str.toString();
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return subPre(str2, str2.length() - suffix.length());// 截取前半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀，并小写首字母
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
		return lowerFirst(removeSuffix(str, suffix));
	}

	/**
	 * 忽略大小写去掉指定后缀
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str.toString();
		}

		final String str2 = str.toString();
		if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
			return subPre(str2, str2.length() - suffix.length());
		}
		return str2;
	}

	/**
	 * 如果给定字符串不是以prefix开头的，在开头补充 prefix
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 补充后的字符串
	 */
	public static String addPrefixIfNot(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str.toString();
		}

		final String str2 = str.toString();
		final String prefix2 = prefix.toString();
		if (false == str2.startsWith(prefix2)) {
			return prefix2 + str2;
		}
		return prefix2;
	}

	/**
	 * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 补充后的字符串
	 */
	public static String addSuffixIfNot(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str.toString();
		}

		final String str2 = str.toString();
		final String suffix2 = suffix.toString();
		if (false == str2.endsWith(suffix2)) {
			return str2 + suffix2;
		}
		return str2;
	}

	/**
	 * 清理空白字符
	 * 
	 * @param str 被清理的字符串
	 * @return 清理后的字符串
	 */
	public static String cleanBlank(CharSequence str) {
		if (str == null) {
			return null;
		}

		int len = str.length();
		StringBuilder sb = new StringBuilder(str.length());
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (false == NumberUtil.isBlankChar(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 */
	public static String[] splitToArray(CharSequence str, char separator) {
		return splitToArray(str, separator, 0);
	}

	/**
	 * 切分字符串<br>
	 * a#b#c =》 [a,b,c] <br>
	 * a##b#c =》 [a,"",b,c]
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 */
	public static List<String> split(CharSequence str, char separator) {
		return split(str, separator, 0);
	}

	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数
	 * @return 切分后的集合
	 */
	public static String[] splitToArray(CharSequence str, char separator, int limit) {
		if(null == str){
			return new String[]{};
		}
		return StrSpliter.splitToArray(str.toString(), separator, limit, false, false);
	}
	
	/**
	 * 切分字符串，不去除切分后每个元素两边的空白符，不去除空白项
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数
	 * @return 切分后的集合
	 */
	public static List<String> split(CharSequence str, char separator, int limit) {
		return split(str, separator, limit, false, false);
	}
	
	/**
	 * 切分字符串，不限制分片数量
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit 限制分片数
	 * @param isTrim 是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		if(null == str){
			return new ArrayList<>(0);
		}
		return StrSpliter.split(str.toString(), separator, limit, isTrim, ignoreEmpty);
	}

	/**
	 * 切分字符串
	 * 
	 * @param str 被切分的字符串
	 * @param separator 分隔符
	 * @return 字符串
	 */
	public static String[] split(CharSequence str, CharSequence separator) {
		if (str == null) {
			return new String[]{};
		}
		
		final String separatorStr = (null == separator) ? null : separator.toString();
		return StrSpliter.splitToArray(str.toString(), separatorStr, 0, false, false);
	}

	/**
	 * 根据给定长度，将给定字符串截取为多个部分
	 * 
	 * @param str 字符串
	 * @param len 每一个小节的长度
	 * @return 截取后的字符串数组
	 * @see StrSpliter#splitByLength(String, int)
	 */
	public static String[] split(CharSequence str, int len) {
		if(null == str){
			return new String[]{};
		}
		return StrSpliter.splitByLength(str.toString(), len);
	}

	/**
	 * 改进JDK subString<br>
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" <br>
	 * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
	 * 如果经过修正的index中from大于to，则互换from和to example: <br>
	 * abcdefgh 2 3 =》 c <br>
	 * abcdefgh 2 -3 =》 cde <br>
	 * 
	 * @param string String
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex 结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(CharSequence string, int fromIndex, int toIndex) {
		int len = string.length();

		if (fromIndex < 0) {
			fromIndex = len + fromIndex;
			if (fromIndex < 0) {
				fromIndex = 0;
			}
		} else if (fromIndex > len) {
			fromIndex = len;
		}

		if (toIndex < 0) {
			toIndex = len + toIndex;
			if (toIndex < 0) {
				toIndex = len;
			}
		} else if (toIndex > len) {
			toIndex = len;
		}

		if (toIndex < fromIndex) {
			int tmp = fromIndex;
			fromIndex = toIndex;
			toIndex = tmp;
		}

		if (fromIndex == toIndex) {
			return EMPTY;
		}

		return string.toString().substring(fromIndex, toIndex);
	}

	/**
	 * 切割前部分
	 * 
	 * @param string 字符串
	 * @param toIndex 切割到的位置（不包括）
	 * @return 切割后的字符串
	 */
	public static String subPre(CharSequence string, int toIndex) {
		return sub(string, 0, toIndex);
	}

	/**
	 * 切割后部分
	 * 
	 * @param string 字符串
	 * @param fromIndex 切割开始的位置（包括）
	 * @return 切割后的字符串
	 */
	public static String subSuf(CharSequence string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	/**
	 * 给定字符串是否被字符包围
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否包围，空串不包围
	 */
	public static boolean isSurround(CharSequence str, CharSequence prefix, CharSequence suffix) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		if (str.length() < (prefix.length() + suffix.length())) {
			return false;
		}

		final String str2 = str.toString();
		return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
	}

	/**
	 * 给定字符串是否被字符包围
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否包围，空串不包围
	 */
	public static boolean isSurround(CharSequence str, char prefix, char suffix) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		if (str.length() < 2) {
			return false;
		}

		return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
	}

	/**
	 * 重复某个字符
	 * 
	 * @param c 被重复的字符
	 * @param count 重复的数目，如果小于等于0则返回""
	 * @return 重复字符字符串
	 */
	public static String repeat(char c, int count) {
		if (count <= 0) {
			return EMPTY;
		}

		char[] result = new char[count];
		for (int i = 0; i < count; i++) {
			result[i] = c;
		}
		return new String(result);
	}

	/**
	 * 重复某个字符串
	 * 
	 * @param str 被重复的字符
	 * @param count 重复的数目
	 * @return 重复字符字符串
	 */
	public static String repeat(CharSequence str, int count) {
		if (null == str) {
			return null;
		}
		if (count <= 0) {
			return EMPTY;
		}
		if (count == 1 || str.length() == 0) {
			return str.toString();
		}

		// 检查
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		final char[] array = new char[size];
		str.toString().getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * 比较两个字符串（大小写敏感）。
	 * 
	 * <pre>
	 * equals(null, null)   = true
	 * equals(null, &quot;abc&quot;)  = false
	 * equals(&quot;abc&quot;, null)  = false
	 * equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 * 
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * 
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equals(CharSequence str1, CharSequence str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.equals(str2);
	}

	/**
	 * 比较两个字符串（大小写不敏感）。
	 * 
	 * <pre>
	 * equalsIgnoreCase(null, null)   = true
	 * equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 * 
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * 
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.toString().equalsIgnoreCase(str2.toString());
	}

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
	 * 
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param params 参数值
	 * @return 格式化后的文本
	 */
	public static String format(CharSequence template, Object... params) {
		if (null == template) {
			return null;
		}
		if (ArrayUtil.isEmpty(params) || isBlank(template)) {
			return template.toString();
		}
		return StrFormatter.format(template.toString(), params);
	}

	/**
	 * 有序的格式化文本，使用{number}做为占位符<br>
	 * 例：<br>
	 * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
	 * 
	 * @param pattern 文本格式
	 * @param arguments 参数
	 * @return 格式化后的文本
	 */
	public static String indexedFormat(CharSequence pattern, Object... arguments) {
		return MessageFormat.format(pattern.toString(), arguments);
	}

	/**
	 * 格式化文本，使用 {varName} 占位<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
	 * 
	 * @param template 文本模板，被替换的部分用 {key} 表示
	 * @param map 参数值对
	 * @return 格式化后的文本
	 */
	public static String format(CharSequence template, Map<?, ?> map) {
		if (null == template) {
			return null;
		}
		if (null == map || map.isEmpty()) {
			return template.toString();
		}

		final String template2 = template.toString();
		for (Entry<?, ?> entry : map.entrySet()) {
			template = template2.replace("{" + entry.getKey() + "}", utf8Str(entry.getValue()));
		}
		return template2;
	}

	/**
	 * 编码字符串，编码为UTF-8
	 * 
	 * @param str 字符串
	 * @return 编码后的字节码
	 */
	public static byte[] utf8Bytes(CharSequence str) {
		return bytes(str, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符串<br>
	 * 使用系统默认编码
	 * 
	 * @param str 字符串
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(CharSequence str) {
		return bytes(str, Charset.defaultCharset());
	}

	/**
	 * 编码字符串
	 * 
	 * @param str 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(CharSequence str, String charset) {
		return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 编码字符串
	 * 
	 * @param str 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(CharSequence str, Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	/**
	 * 将对象转为字符串<br>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
	 * 
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String utf8Str(Object obj) {
		return str(obj, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将对象转为字符串<br>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
	 * 
	 * @param obj 对象
	 * @param charsetName 字符集
	 * @return 字符串
	 */
	public static String str(Object obj, String charsetName) {
		return str(obj, Charset.forName(charsetName));
	}

	/**
	 * 将对象转为字符串<br>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
	 * 
	 * @param obj 对象
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(Object obj, Charset charset) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof byte[]) {
			return str((byte[]) obj, charset);
		} else if (obj instanceof Byte[]) {
			return str((Byte[]) obj, charset);
		} else if (obj instanceof ByteBuffer) {
			return str((ByteBuffer) obj, charset);
		} else if (ArrayUtil.isArray(obj)) {
			return ArrayUtil.toString(obj);
		}

		return obj.toString();
	}

	/**
	 * 将byte数组转为字符串
	 * 
	 * @param bytes byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(byte[] bytes, String charset) {
		return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 解码字节码
	 * 
	 * @param data 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String str(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	/**
	 * 将Byte数组转为字符串
	 * 
	 * @param bytes byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(Byte[] bytes, String charset) {
		return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 解码字节码
	 * 
	 * @param data 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String str(Byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		byte[] bytes = new byte[data.length];
		Byte dataByte;
		for (int i = 0; i < data.length; i++) {
			dataByte = data[i];
			bytes[i] = (null == dataByte) ? -1 : dataByte.byteValue();
		}

		return str(bytes, charset);
	}

	/**
	 * 将编码的byteBuffer数据转换为字符串
	 * 
	 * @param data 数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(ByteBuffer data, String charset) {
		if (data == null) {
			return null;
		}

		return str(data, Charset.forName(charset));
	}

	/**
	 * 将编码的byteBuffer数据转换为字符串
	 * 
	 * @param data 数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(ByteBuffer data, Charset charset) {
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		return charset.decode(data).toString();
	}

	/**
	 * 字符串转换为byteBuffer
	 * 
	 * @param str 字符串
	 * @param charset 编码
	 * @return byteBuffer
	 */
	public static ByteBuffer byteBuffer(CharSequence str, String charset) {
		return ByteBuffer.wrap(bytes(str, charset));
	}

	/**
	 * 以 conjunction 为分隔符将多个对象转换为字符串
	 * 
	 * @see ArrayUtil#join(Object, CharSequence)
	 * 
	 * @param conjunction 分隔符
	 * @param objs 数组
	 * @return 连接后的字符串
	 */
	public static String join(CharSequence conjunction, Object... objs) {
		return ArrayUtil.join(objs, conjunction);
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 * 例如：HelloWorld=》hello_world
	 *
	 * @param camelCaseStr 转换前的驼峰式命名的字符串
	 * @return 转换后下划线大写方式命名的字符串
	 */
	public static String toUnderlineCase(CharSequence camelCaseStr) {
		if (camelCaseStr == null) {
			return null;
		}

		final int length = camelCaseStr.length();
		StringBuilder sb = new StringBuilder();
		char c;
		boolean isPreUpperCase = false;
		for (int i = 0; i < length; i++) {
			c = camelCaseStr.charAt(i);
			boolean isNextUpperCase = true;
			if (i < (length - 1)) {
				isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
			}
			if (Character.isUpperCase(c)) {
				if (!isPreUpperCase || !isNextUpperCase) {
					if (i > 0) sb.append(UNDERLINE);
				}
				isPreUpperCase = true;
			} else {
				isPreUpperCase = false;
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	/**
	 * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 例如：hello_world=》HelloWorld
	 *
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toCamelCase(CharSequence name) {
		if (null == name) {
			return null;
		}

		String name2 = name.toString();
		if (name2.contains(UNDERLINE)) {
			name2 = name2.toLowerCase();

			StringBuilder sb = new StringBuilder(name2.length());
			boolean upperCase = false;
			for (int i = 0; i < name2.length(); i++) {
				char c = name2.charAt(i);

				if (c == '_') {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else
			return name2;
	}

	/**
	 * 包装指定字符串
	 * 
	 * @param str 被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
		return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
	}
	
	/**
	 * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再
	 * 
	 * @param str 被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
		int len = 0;
		if(isNotEmpty(str)){
			len += str.length();
		}
		if(isNotEmpty(prefix)){
			len += str.length();
		}
		if(isNotEmpty(suffix)){
			len += str.length();
		}
		StringBuilder sb = new StringBuilder(len);
		if(isNotEmpty(prefix) && false == startWith(str, prefix)){
			sb.append(prefix);
		}
		if(isNotEmpty(str)){
			sb.append(str);
		}
		if(isNotEmpty(suffix) && false == endWith(str, suffix)){
			sb.append(suffix);
		}
		return sb.toString();
	}

	/**
	 * 指定字符串是否被包装
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, String prefix, String suffix) {
		if(ArrayUtil.hasNull(str, prefix, suffix)){
			return false;
		}
		final String str2 = str.toString();
		return str2.startsWith(prefix) && str2.endsWith(suffix);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 * 
	 * @param str 字符串
	 * @param wrapper 包装字符串
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 * 
	 * @param str 字符串
	 * @param wrapper 包装字符
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被包装
	 * 
	 * @param str 字符串
	 * @param prefixChar 前缀
	 * @param suffixChar 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
		if (null == str) {
			return false;
		}

		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}

	/**
	 * 补充字符串以满足最小长度 StrUtil.padPre("1", 3, '0');//"001"
	 * 
	 * @param str 字符串
	 * @param minLength 最小长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(CharSequence str, int minLength, char padChar) {
		if (null == str) {
			str = EMPTY;
		} else if (str.length() >= minLength) {
			return str.toString();
		}

		return repeat(padChar, minLength - str.length()).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度 StrUtil.padEnd("1", 3, '0');//"100"
	 * 
	 * @param str 字符串，如果为<code>null</code>，按照空串处理
	 * @param minLength 最小长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padEnd(CharSequence str, int minLength, char padChar) {
		if (null == str) {
			str = EMPTY;
		} else if (str.length() >= minLength) {
			return str.toString();
		}

		return str.toString().concat(repeat(padChar, minLength - str.length()));
	}

	/**
	 * 创建StringBuilder对象
	 * 
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder() {
		return new StringBuilder();
	}

	/**
	 * 创建StringBuilder对象
	 * 
	 * @param capacity 初始大小
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder(int capacity) {
		return new StringBuilder(capacity);
	}

	/**
	 * 创建StringBuilder对象
	 * 
	 * @param strs 初始字符串列表
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder(CharSequence... strs) {
		final StringBuilder sb = new StringBuilder();
		for (CharSequence str : strs) {
			sb.append(str);
		}
		return sb;
	}

	/**
	 * 获得StringReader
	 * 
	 * @param str 字符串
	 * @return StringReader
	 */
	public static StringReader getReader(CharSequence str) {
		if (null == str) {
			return null;
		}
		return new StringReader(str.toString());
	}

	/**
	 * 获得StringWriter
	 * 
	 * @return StringWriter
	 */
	public static StringWriter getWriter() {
		return new StringWriter();
	}

	/**
	 * 统计指定内容中包含指定字符串的数量<br>
	 * 参数为 {@code null} 或者 "" 返回 {@code 0}.
	 *
	 * <pre>
	 * StrUtil.count(null, *)       = 0
	 * StrUtil.count("", *)         = 0
	 * StrUtil.count("abba", null)  = 0
	 * StrUtil.count("abba", "")    = 0
	 * StrUtil.count("abba", "a")   = 2
	 * StrUtil.count("abba", "ab")  = 1
	 * StrUtil.count("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param content 被查找的字符串
	 * @param strForSearch 需要查找的字符串
	 * @return 查找到的个数
	 */
	public static int count(CharSequence content, CharSequence strForSearch) {
		if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		final String content2 = content.toString();
		final String strForSearch2 = strForSearch.toString();
		while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
			count++;
			idx += strForSearch.length();
		}
		return count;
	}

	/**
	 * 统计指定内容中包含指定字符的数量
	 * 
	 * @param content 内容
	 * @param charForSearch 被统计的字符
	 * @return 包含数量
	 */
	public static int count(CharSequence content, char charForSearch) {
		int count = 0;
		if (isEmpty(content)) {
			return 0;
		}
		int contentLength = content.length();
		for (int i = 0; i < contentLength; i++) {
			if (charForSearch == content.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 将字符串切分为N等份
	 * 
	 * @param str 字符串
	 * @param partLength 每等份的长度
	 * @return 切分后的数组
	 * @since 3.0.6
	 */
	public static String[] cut(CharSequence str, int partLength) {
		if (null == str) {
			return null;
		}
		int len = str.length();
		if (len < partLength) {
			return new String[] { str.toString() };
		}
		int part = NumberUtil.count(len, partLength);
		final String[] array = new String[part];

		final String str2 = str.toString();
		for (int i = 0; i < part; i++) {
			array[i] = str2.substring(i * partLength, (i == part - 1) ? len : (partLength + i * partLength));
		}
		return array;
	}

	/**
	 * 将给定字符串，变成 "xxx...xxx" 形式的字符串
	 * 
	 * @param str 字符串
	 * @param maxLength 最大长度
	 * @return 截取后的字符串
	 */
	public static String brief(CharSequence str, int maxLength) {
		if (null == str) {
			return null;
		}
		if ((str.length() + 3) <= maxLength) {
			return str.toString();
		}
		int w = maxLength / 2;
		int l = str.length();

		final String str2 = str.toString();
		return format("{}...{}", str2.substring(0, maxLength - w), str2.substring(l - w));
	}

	/**
	 * 比较两个字符串，用于排序
	 * 
	 * <pre>
	 * StrUtil.compare(null, null, *)     = 0
	 * StrUtil.compare(null , "a", true)  &lt; 0
	 * StrUtil.compare(null , "a", false) &gt; 0
	 * StrUtil.compare("a", null, true)   &gt; 0
	 * StrUtil.compare("a", null, false)  &lt; 0
	 * StrUtil.compare("abc", "abc", *)   = 0
	 * StrUtil.compare("a", "b", *)       &lt; 0
	 * StrUtil.compare("b", "a", *)       &gt; 0
	 * StrUtil.compare("a", "B", *)       &gt; 0
	 * StrUtil.compare("ab", "abc", *)    &lt; 0
	 * </pre>
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 < str2，正数：str1 > str2, 0：str1 == str2
	 */
	public static int compare(final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareTo(str2.toString());
	}

	/**
	 * 比较两个字符串，用于排序，大小写不敏感
	 * 
	 * <pre>
	 * StrUtil.compareIgnoreCase(null, null, *)     = 0
	 * StrUtil.compareIgnoreCase(null , "a", true)  &lt; 0
	 * StrUtil.compareIgnoreCase(null , "a", false) &gt; 0
	 * StrUtil.compareIgnoreCase("a", null, true)   &gt; 0
	 * StrUtil.compareIgnoreCase("a", null, false)  &lt; 0
	 * StrUtil.compareIgnoreCase("abc", "abc", *)   = 0
	 * StrUtil.compareIgnoreCase("abc", "ABC", *)   = 0
	 * StrUtil.compareIgnoreCase("a", "b", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("b", "a", *)       &gt; 0
	 * StrUtil.compareIgnoreCase("a", "B", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("A", "b", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("ab", "abc", *)    &lt; 0
	 * </pre>
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 < str2，正数：str1 > str2, 0：str1 == str2
	 */
	public static int compareIgnoreCase(final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareToIgnoreCase(str2.toString());
	}

	/**
	 * 指定范围内查找指定字符
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @return 位置
	 */
	public static int indexOf(final CharSequence str, char searchChar) {
		return indexOf(str, searchChar, 0);
	}

	/**
	 * 指定范围内查找指定字符
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @param start 起始位置，如果小于0，从0开始查找
	 * @return 位置
	 */
	public static int indexOf(final CharSequence str, char searchChar, int start) {
		if (str instanceof String) {
			return ((String) str).indexOf(searchChar, start);
		} else {
			return indexOf(str, searchChar, start, -1);
		}
	}

	/**
	 * 指定范围内查找指定字符
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @param start 起始位置，如果小于0，从0开始查找
	 * @param end 终止位置，如果超过str.length()则默认查找到字符串末尾
	 * @return 位置
	 */
	public static int indexOf(final CharSequence str, char searchChar, int start, int end) {
		final int len = str.length();
		if (start < 0 || start > len) {
			start = 0;
		}
		if (end > len || end < 0) {
			end = len;
		}
		for (int i = start; i < end; i++) {
			if (str.charAt(i) == searchChar) {
				return i;
			}
		}
		return -1;
	}
	
	//------------------------------------------------------------------------------------------------------------------ Append and prepend
	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
	 * 不忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param suffix 需要添加到结尾的字符串
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissing(final CharSequence str, final CharSequence suffix, final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}
	
	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
	 * 忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param suffix 需要添加到结尾的字符串
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissingIgnoreCase(final CharSequence str, final CharSequence suffix, final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, true, suffixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串
	 *
	 * @param str 被检查的字符串
	 * @param suffix 需要添加到结尾的字符串
	 * @param ignoreCase 检查结尾时是否忽略大小写
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissing(final CharSequence str, final CharSequence suffix, final boolean ignoreCase, final CharSequence... suffixes) {
		if (str == null || isEmpty(suffix) || endWith(str, suffix, ignoreCase)) {
			return str.toString();
		}
		if (suffixes != null && suffixes.length > 0) {
			for (final CharSequence s : suffixes) {
				if (endWith(str, s, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return str.toString().concat(suffix.toString());
	}
	
	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
	 * 不忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param prefix 需要添加到首部的字符串
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissing(final CharSequence str, final CharSequence prefix, final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, false, prefixes);
	}
	
	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
	 * 忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param prefix 需要添加到首部的字符串
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissingIgnoreCase(final CharSequence str, final CharSequence prefix, final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, true, prefixes);
	}
	
	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串
	 *
	 * @param str 被检查的字符串
	 * @param prefix 需要添加到首部的字符串
	 * @param ignoreCase 检查结尾时是否忽略大小写
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissing(final CharSequence str, final CharSequence prefix, final boolean ignoreCase, final CharSequence... prefixes) {
		if (str == null || isEmpty(prefix) || startWith(str, prefix, ignoreCase)) {
			return str.toString();
		}
		if (prefixes != null && prefixes.length > 0) {
			for (final CharSequence s : prefixes) {
				if (startWith(str, s, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return prefix.toString().concat(str.toString());
	}
}
