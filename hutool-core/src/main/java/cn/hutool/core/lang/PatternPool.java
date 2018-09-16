package cn.hutool.core.lang;

import java.util.regex.Pattern;

import cn.hutool.core.util.ReUtil;

/**
 * 常用正则表达式集合
 * 
 * @author Looly
 *
 */
public class PatternPool {

	/** 英文字母 、数字和下划线 */
	public final static Pattern GENERAL = Pattern.compile("^\\w+$");
	/** 数字 */
	public final static Pattern NUMBERS = Pattern.compile("\\d+");
	/** 字母 */
	public final static Pattern WORD = Pattern.compile("[a-zA-Z]+");
	/** 单个中文汉字 */
	public final static Pattern CHINESE = Pattern.compile(ReUtil.RE_CHINESE);
	/** 中文汉字 */
	public final static Pattern CHINESES = Pattern.compile(ReUtil.RE_CHINESES);
	/** 分组 */
	public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
	/** IP v4 */
	public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
	/** 货币 */
	public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
	/** 邮件，符合RFC 5322规范，正则来自：http://emailregex.com/ */
	// public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
	public final static Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
	/** 移动电话 */
	public final static Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3456789]\\d{9}");
	/** 18位身份证号码 */
	public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
	/** 邮编 */
	public final static Pattern ZIP_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");
	/** 生日 */
	public final static Pattern BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-\\.年]?)(\\d{1,2})([/\\-\\.月]?)(\\d{1,2})日?$");
	/** URL */
	public final static Pattern URL = Pattern.compile("[a-zA-z]+://[^\\s]*");
	/** Http URL */
	public final static Pattern URL_HTTP = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
	/** 中文字、英文字母、数字和下划线 */
	public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\u4E00-\u9FFF\\w]+$");
	/** UUID */
	public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
	/** 不带横线的UUID */
	public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
	/** 中国车牌号码 */
	public final static Pattern PLATE_NUMBER = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$");
	/** MAC地址正则 */
	public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/** Pattern池 */
	private static final SimpleCache<RegexWithFlag, Pattern> POOL = new SimpleCache<>();

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 * 
	 * @param regex 正则表达式
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex) {
		return get(regex, 0);
	}

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 * 
	 * @param regex 正则表达式
	 * @param flags 正则标识位集合 {@link Pattern}
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex, int flags) {
		final RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);

		Pattern pattern = POOL.get(regexWithFlag);
		if (null == pattern) {
			pattern = Pattern.compile(regex, flags);
			POOL.put(regexWithFlag, pattern);
		}
		return pattern;
	}

	/**
	 * 移除缓存
	 * 
	 * @param regex 正则
	 * @param flags 标识
	 * @return 移除的{@link Pattern}，可能为{@code null}
	 */
	public static Pattern remove(String regex, int flags) {
		return POOL.remove(new RegexWithFlag(regex, flags));
	}

	/**
	 * 清空缓存池
	 */
	public static void clear() {
		POOL.clear();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 正则表达式和正则标识位的包装
	 * 
	 * @author Looly
	 *
	 */
	private static class RegexWithFlag {
		private String regex;
		private int flag;

		/**
		 * 构造
		 * 
		 * @param regex 正则
		 * @param flag 标识
		 */
		public RegexWithFlag(String regex, int flag) {
			this.regex = regex;
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + flag;
			result = prime * result + ((regex == null) ? 0 : regex.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			RegexWithFlag other = (RegexWithFlag) obj;
			if (flag != other.flag) {
				return false;
			}
			if (regex == null) {
				if (other.regex != null) {
					return false;
				}
			} else if (!regex.equals(other.regex)) {
				return false;
			}
			return true;
		}

	}
}
