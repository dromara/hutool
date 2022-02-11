package cn.hutool.core.convert;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 数字转中文类<br>
 * 包括：
 * <pre>
 * 1. 数字转中文大写形式，比如一百二十一
 * 2. 数字转金额用的大写形式，比如：壹佰贰拾壹
 * 3. 转金额形式，比如：壹佰贰拾壹整
 * </pre>
 *
 * @author fanqun, looly
 **/
public class NumberChineseFormatter {

	/**
	 * 中文形式，奇数位置是简体，偶数位置是记账繁体，0共用<br>
	 * 使用混合数组提高效率和数组复用
	 **/
	private static final char[] DIGITS = {'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍',
			'六', '陆', '七', '柒', '八', '捌', '九', '玖'};

	/**
	 * 汉字转阿拉伯数字的
	 */
	private static final ChineseUnit[] CHINESE_NAME_VALUE = {
			new ChineseUnit(' ', 1, false),
			new ChineseUnit('十', 10, false),
			new ChineseUnit('拾', 10, false),
			new ChineseUnit('百', 100, false),
			new ChineseUnit('佰', 100, false),
			new ChineseUnit('千', 1000, false),
			new ChineseUnit('仟', 1000, false),
			new ChineseUnit('万', 1_0000, true),
			new ChineseUnit('亿', 1_0000_0000, true),
	};


	/**
	 * 阿拉伯数字转换成中文,小数点后四舍五入保留两位. 使用于整数、小数的转换.
	 *
	 * @param amount           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 中文
	 */
	public static String format(double amount, boolean isUseTraditional) {
		return format(amount, isUseTraditional, false);
	}

	/**
	 * 阿拉伯数字转换成中文,小数点后四舍五入保留两位. 使用于整数、小数的转换.
	 *
	 * @param amount           数字
	 * @param isUseTraditional 是否使用繁体
	 * @param isMoneyMode      是否为金额模式
	 * @return 中文
	 */
	public static String format(double amount, boolean isUseTraditional, boolean isMoneyMode) {
		if(0 == amount){
			return "零";
		}
		Assert.checkBetween(amount, -99_9999_9999_9999.99, 99_9999_9999_9999.99,
				"Number support only: (-99999999999999.99 ~ 99999999999999.99)！");

		final StringBuilder chineseStr = new StringBuilder();

		// 负数
		if (amount < 0) {
			chineseStr.append("负");
			amount = -amount;
		}

		long yuan = Math.round(amount * 100);
		final int fen = (int) (yuan % 10);
		yuan = yuan / 10;
		final int jiao = (int) (yuan % 10);
		yuan = yuan / 10;

		// 元
		if(false == isMoneyMode || 0 != yuan){
			// 金额模式下，无需“零元”
			chineseStr.append(longToChinese(yuan, isUseTraditional));
			if(isMoneyMode){
				chineseStr.append("元");
			}
		}

		if(0 == jiao && 0 == fen){
			//无小数部分的金额结尾
			if(isMoneyMode){
				chineseStr.append("整");
			}
			return chineseStr.toString();
		}

		// 小数部分
		if(false == isMoneyMode){
			chineseStr.append("点");
		}

		// 角
		if(0 == yuan && 0 == jiao){
			// 元和角都为0时，只有非金额模式下补“零”
			if(false == isMoneyMode){
				chineseStr.append("零");
			}
		}else{
			chineseStr.append(numberToChinese(jiao, isUseTraditional));
			if(isMoneyMode && 0 != jiao){
				chineseStr.append("角");
			}
		}

		// 分
		if(0 != fen){
			chineseStr.append(numberToChinese(fen, isUseTraditional));
			if(isMoneyMode){
				chineseStr.append("分");
			}
		}

		return chineseStr.toString();
	}

	/**
	 * 阿拉伯数字（支持正负整数）转换成中文
	 *
	 * @param amount 数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 中文
	 * @since 5.7.17
	 */
	public static String format(long amount, boolean isUseTraditional){
		if(0 == amount){
			return "零";
		}
		Assert.checkBetween(amount, -99_9999_9999_9999.99, 99_9999_9999_9999.99,
				"Number support only: (-99999999999999.99 ~ 99999999999999.99)！");

		final StringBuilder chineseStr = new StringBuilder();

		// 负数
		if (amount < 0) {
			chineseStr.append("负");
			amount = -amount;
		}

		chineseStr.append(longToChinese(amount, isUseTraditional));
		return chineseStr.toString();
	}

	/**
	 * 阿拉伯数字（支持正负整数）四舍五入后转换成中文节权位简洁计数单位，例如 -5_5555 =》 -5.56万
	 *
	 * @param amount 数字
	 * @return 中文
	 */
	public static String formatSimple(long amount) {
		if (amount < 1_0000 && amount > -1_0000) {
			return String.valueOf(amount);
		}
		String res;
		if (amount < 1_0000_0000 && amount > -1_0000_0000) {
			res = NumberUtil.div(amount, 1_0000, 2) + "万";
		} else if (amount < 1_0000_0000_0000L && amount > -1_0000_0000_0000L) {
			res = NumberUtil.div(amount, 1_0000_0000, 2) + "亿";
		} else {
			res = NumberUtil.div(amount, 1_0000_0000_0000L, 2) + "万亿";
		}
		return res;
	}

	/**
	 * 格式化-999~999之间的数字<br>
	 * 这个方法显示10~19以下的数字时使用"十一"而非"一十一"。
	 *
	 * @param amount 数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 中文
	 * @since 5.7.17
	 */
	public static String formatThousand(int amount, boolean isUseTraditional){
		Assert.checkBetween(amount, -999, 999, "Number support only: (-999 ~ 999)！");

		final String chinese = thousandToChinese(amount, isUseTraditional);
		if(amount < 20 && amount > 10){
			// "十一"而非"一十一"
			return chinese.substring(1);
		}
		return chinese;
	}

	/**
	 * 数字字符转中文，非数字字符原样返回
	 *
	 * @param c                数字字符
	 * @param isUseTraditional 是否繁体
	 * @return 中文字符
	 * @since 5.3.9
	 */
	public static String numberCharToChinese(char c, boolean isUseTraditional) {
		if (c < '0' || c > '9') {
			return String.valueOf(c);
		}
		return String.valueOf(numberToChinese(c - '0', isUseTraditional));
	}

	/**
	 * 阿拉伯数字整数部分转换成中文，只支持正数
	 *
	 * @param amount           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 中文
	 */
	private static String longToChinese(long amount, boolean isUseTraditional) {
		if(0 == amount){
			return "零";
		}

		//将数字以万为单位分为多份
		int[] parts = new int[4];
		for (int i = 0; amount != 0; i++) {
			parts[i] = (int) (amount % 10000);
			amount = amount / 10000;
		}

		final StringBuilder chineseStr = new StringBuilder();
		int partValue;
		String partChinese;

		// 千
		partValue = parts[0];
		if(partValue > 0){
			partChinese = thousandToChinese(partValue, isUseTraditional);
			chineseStr.insert(0, partChinese);

			if(partValue < 1000){
				// 和万位之间空0，则补零，如一万零三百
				addPreZero(chineseStr);
			}
		}

		// 万
		partValue = parts[1];
		if(partValue > 0){
			if((partValue % 10 == 0 && parts[0] > 0)){
				// 如果"万"的个位是0，则补零，如十万零八千
				addPreZero(chineseStr);
			}
			partChinese = thousandToChinese(partValue, isUseTraditional);
			chineseStr.insert(0, partChinese + "万");

			if(partValue < 1000){
				// 和亿位之间空0，则补零，如一亿零三百万
				addPreZero(chineseStr);
			}
		} else{
			addPreZero(chineseStr);
		}

		// 亿
		partValue = parts[2];
		if(partValue > 0){
			if((partValue % 10 == 0 && parts[1] > 0)){
				// 如果"万"的个位是0，则补零，如十万零八千
				addPreZero(chineseStr);
			}

			partChinese = thousandToChinese(partValue, isUseTraditional);
			chineseStr.insert(0, partChinese + "亿");

			if(partValue < 1000){
				// 和万亿位之间空0，则补零，如一万亿零三百亿
				addPreZero(chineseStr);
			}
		} else{
			addPreZero(chineseStr);
		}

		// 万亿
		partValue = parts[3];
		if(partValue > 0){
			if(parts[2] == 0){
				chineseStr.insert(0, "亿");
			}
			partChinese = thousandToChinese(partValue, isUseTraditional);
			chineseStr.insert(0, partChinese + "万");
		}

		if(StrUtil.isNotEmpty(chineseStr) && '零' == chineseStr.charAt(0)){
			return chineseStr.substring(1);
		}

		return chineseStr.toString();
	}

	/**
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
	 *
	 * @param amountPart       数字部分
	 * @param isUseTraditional 是否使用繁体单位
	 * @return 转换后的汉字
	 */
	private static String thousandToChinese(int amountPart, boolean isUseTraditional) {
		if (amountPart == 0) {
			// issue#I4R92H@Gitee
			return String.valueOf(DIGITS[0]);
		}

		int temp = amountPart;

		StringBuilder chineseStr = new StringBuilder();
		boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
		for (int i = 0; temp > 0; i++) {
			int digit = temp % 10;
			if (digit == 0) { // 取到的数字为 0
				if (false == lastIsZero) {
					// 前一个数字不是 0，则在当前汉字串前加“零”字;
					chineseStr.insert(0, "零");
				}
				lastIsZero = true;
			} else { // 取到的数字不是 0
				chineseStr.insert(0, numberToChinese(digit, isUseTraditional) + getUnitName(i, isUseTraditional));
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr.toString();
	}

	/**
	 * 把中文转换为数字 如 二百二十 220<br>
	 * 见：https://www.d5.nz/read/sfdlq/text-part0000_split_030.html
	 * <ul>
	 *     <li>一百一十二 -》 112</li>
	 *     <li>一千零一十二 -》 1012</li>
	 * </ul>
	 *
	 * @param chinese 中文字符
	 * @return 数字
	 * @since 5.6.0
	 */
	public static int chineseToNumber(String chinese) {
		final int length = chinese.length();
		int result = 0;

		// 节总和
		int section = 0;
		int number = 0;
		ChineseUnit unit = null;
		char c;
		for (int i = 0; i < length; i++) {
			c = chinese.charAt(i);
			final int num = chineseToNumber(c);
			if (num >= 0) {
				if (num == 0) {
					// 遇到零时节结束，权位失效，比如两万二零一十
					if (number > 0 && null != unit) {
						section += number * (unit.value / 10);
					}
					unit = null;
				} else if (number > 0) {
					// 多个数字同时出现，报错
					throw new IllegalArgumentException(StrUtil.format("Bad number '{}{}' at: {}", chinese.charAt(i - 1), c, i));
				}
				// 普通数字
				number = num;
			} else {
				unit = chineseToUnit(c);
				if (null == unit) {
					// 出现非法字符
					throw new IllegalArgumentException(StrUtil.format("Unknown unit '{}' at: {}", c, i));
				}

				//单位
				if (unit.secUnit) {
					// 节单位，按照节求和
					section = (section + number) * unit.value;
					result += section;
					section = 0;
				} else {
					// 非节单位，和单位前的单数字组合为值
					int unitNumber = number;
					if(0 == number && 0 == i){
						// issue#1726，对于单位开头的数组，默认赋予1
						// 十二 -> 一十二
						// 百二 -> 一百二
						unitNumber = 1;
					}
					section += (unitNumber * unit.value);
				}
				number = 0;
			}
		}

		if (number > 0 && null != unit) {
			number = number * (unit.value / 10);
		}

		return result + section + number;
	}

	/**
	 * 查找对应的权对象
	 *
	 * @param chinese 中文权位名
	 * @return 权对象
	 */
	private static ChineseUnit chineseToUnit(char chinese) {
		for (ChineseUnit chineseNameValue : CHINESE_NAME_VALUE) {
			if (chineseNameValue.name == chinese) {
				return chineseNameValue;
			}
		}
		return null;
	}

	/**
	 * 将汉字单个数字转换为int类型数字
	 *
	 * @param chinese 汉字数字，支持简体和繁体
	 * @return 数字，-1表示未找到
	 * @since 5.6.4
	 */
	private static int chineseToNumber(char chinese) {
		if ('两' == chinese) {
			// 口语纠正
			chinese = '二';
		}
		final int i = ArrayUtil.indexOf(DIGITS, chinese);
		if (i > 0) {
			return (i + 1) / 2;
		}
		return i;
	}

	/**
	 * 单个数字转汉字
	 *
	 * @param number           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 汉字
	 */
	private static char numberToChinese(int number, boolean isUseTraditional) {
		if (0 == number) {
			return DIGITS[0];
		}
		return DIGITS[number * 2 - (isUseTraditional ? 0 : 1)];
	}

	/**
	 * 获取对应级别的单位
	 *
	 * @param index            级别，0表示各位，1表示十位，2表示百位，以此类推
	 * @param isUseTraditional 是否使用繁体
	 * @return 单位
	 */
	private static String getUnitName(int index, boolean isUseTraditional) {
		if (0 == index) {
			return StrUtil.EMPTY;
		}
		return String.valueOf(CHINESE_NAME_VALUE[index * 2 - (isUseTraditional ? 0 : 1)].name);
	}

	/**
	 * 权位
	 *
	 * @author totalo
	 * @since 5.6.0
	 */
	private static class ChineseUnit {
		/**
		 * 中文权名称
		 */
		private final char name;
		/**
		 * 10的倍数值
		 */
		private final int value;
		/**
		 * 是否为节权位，它不是与之相邻的数字的倍数，而是整个小节的倍数。<br>
		 * 例如二十三万，万是节权位，与三无关，而和二十三关联
		 */
		private final boolean secUnit;

		/**
		 * 构造
		 *
		 * @param name    名称
		 * @param value   值，即10的倍数
		 * @param secUnit 是否为节权位
		 */
		public ChineseUnit(char name, int value, boolean secUnit) {
			this.name = name;
			this.value = value;
			this.secUnit = secUnit;
		}
	}

	private static void addPreZero(StringBuilder chineseStr){
		if(StrUtil.isEmpty(chineseStr)){
			return;
		}
		final char c = chineseStr.charAt(0);
		if('零' != c){
			chineseStr.insert(0, '零');
		}
	}
}
