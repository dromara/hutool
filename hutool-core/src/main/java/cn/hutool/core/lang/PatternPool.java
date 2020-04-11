package cn.hutool.core.lang;

import cn.hutool.core.util.ReUtil;

import java.util.regex.Pattern;

/**
 * 常用正则表达式集合
 *
 * @author Looly
 */
public class PatternPool {

	/**
	 * 英文字母 、数字和下划线
	 */
	public final static Pattern GENERAL = Pattern.compile("^\\w+$");
	/**
	 * 数字
	 */
	public final static Pattern NUMBERS = Pattern.compile("\\d+");
	/**
	 * 字母
	 */
	public final static Pattern WORD = Pattern.compile("[a-zA-Z]+");
	/**
	 * 单个中文汉字
	 */
	public final static Pattern CHINESE = Pattern.compile(ReUtil.RE_CHINESE);
	/**
	 * 中文汉字
	 */
	public final static Pattern CHINESES = Pattern.compile(ReUtil.RE_CHINESES);
	/**
	 * 分组
	 */
	public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
	/**
	 * IP v4
	 */
	public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
	/**
	 * IP v6
	 */
	public final static Pattern IPV6 = Pattern.compile("(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))");
	/**
	 * 货币
	 */
	public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
	/**
	 * 邮件，符合RFC 5322规范，正则来自：http://emailregex.com/
	 */
	// public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
	public final static Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", Pattern.CASE_INSENSITIVE);
	/**
	 * 移动电话
	 */
	public final static Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3456789]\\d{9}");
	/**
	 * 18位身份证号码
	 */
	public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)");
	/**
	 * 邮编
	 */
	public final static Pattern ZIP_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");
	/**
	 * 生日
	 */
	public final static Pattern BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$");
	/**
	 * URL
	 */
	public final static Pattern URL = Pattern.compile("[a-zA-z]+://[^\\s]*");
	/**
	 * Http URL
	 */
	public final static Pattern URL_HTTP = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(:\\d+)*(/[\\w- ./?%&=]*)?");
	/**
	 * 中文字、英文字母、数字和下划线
	 */
	public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\u4E00-\u9FFF\\w]+$");
	/**
	 * UUID
	 */
	public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
	/**
	 * 不带横线的UUID
	 */
	public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
	/**
	 * MAC地址正则
	 */
	public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);
	/**
	 * 16进制字符串
	 */
	public static final Pattern HEX = Pattern.compile("^[a-f0-9]+$", Pattern.CASE_INSENSITIVE);
	/**
	 * 时间正则
	 */
	public static final Pattern TIME = Pattern.compile("\\d{1,2}:\\d{1,2}(:\\d{1,2})?");
	/**
	 * 中国车牌号码（兼容新能源车牌）
	 */
	public final static Pattern PLATE_NUMBER = Pattern.compile(
			//https://gitee.com/loolly/hutool/issues/I1B77H?from=project-issue
			"^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|" +
					//https://gitee.com/loolly/hutool/issues/I1BJHE?from=project-issue
					"([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|" +
					"([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$");


	/**
	 * 社会统一信用代码
	 * <pre>
	 * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
	 * 第二部分：机构类别代码1位 (数字或大写英文字母)
	 * 第三部分：登记管理机关行政区划码6位 (数字)
	 * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
	 * 第五部分：校验码1位 (数字或大写英文字母)
	 * </pre>
	 */
	public static final Pattern CREDIT_CODE = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Pattern池
	 */
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
	 */
	private static class RegexWithFlag {
		private final String regex;
		private final int flag;

		/**
		 * 构造
		 *
		 * @param regex 正则
		 * @param flag  标识
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
				return other.regex == null;
			} else {
				return regex.equals(other.regex);
			}
		}

	}
}
