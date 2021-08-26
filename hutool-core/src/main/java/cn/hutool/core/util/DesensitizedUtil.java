package cn.hutool.core.util;

/**
 * 脱敏工具类，支持以下类型信息的脱敏自动处理：
 *
 * <ul>
 *     <li>用户ID</li>
 *     <li>中文名</li>
 *     <li>身份证</li>
 *     <li>座机号</li>
 *     <li>手机号</li>
 *     <li>地址</li>
 *     <li>电子邮件</li>
 *     <li>密码</li>
 *     <li>车牌</li>
 *     <li>银行卡号</li>
 * </ul>
 *
 * @author dazer and neusoft and qiaomu
 * @since 5.6.2
 */
public class DesensitizedUtil {

	/**
	 * 支持的脱敏类型枚举
	 *
	 * @author dazer and neusoft and qiaomu
	 */
	public enum DesensitizedType {
		//用户id
		USER_ID,
		//中文名
		CHINESE_NAME,
		//身份证号
		ID_CARD,
		//座机号
		FIXED_PHONE,
		//手机号
		MOBILE_PHONE,
		//地址
		ADDRESS,
		//电子邮件
		EMAIL,
		//密码
		PASSWORD,
		//中国大陆车牌，包含普通车辆、新能源车辆
		CAR_LICENSE,
		//银行卡
		BANK_CARD
	}

	/**
	 * 脱敏，使用默认的脱敏策略
	 * <pre>
	 * DesensitizedUtil.desensitized("100", DesensitizedUtils.DesensitizedType.USER_ID)) =  "0"
	 * DesensitizedUtil.desensitized("段正淳", DesensitizedUtils.DesensitizedType.CHINESE_NAME)) = "段**"
	 * DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtils.DesensitizedType.ID_CARD)) = "5***************1X"
	 * DesensitizedUtil.desensitized("09157518479", DesensitizedUtils.DesensitizedType.FIXED_PHONE)) = "0915*****79"
	 * DesensitizedUtil.desensitized("18049531999", DesensitizedUtils.DesensitizedType.MOBILE_PHONE)) = "180****1999"
	 * DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtils.DesensitizedType.ADDRESS)) = "北京市海淀区马********"
	 * DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtils.DesensitizedType.EMAIL)) = "d*************@gmail.com.cn"
	 * DesensitizedUtil.desensitized("1234567890", DesensitizedUtils.DesensitizedType.PASSWORD)) = "**********"
	 * DesensitizedUtil.desensitized("苏D40000", DesensitizedUtils.DesensitizedType.CAR_LICENSE)) = "苏D4***0"
	 * DesensitizedUtil.desensitized("11011111222233333256", DesensitizedUtils.DesensitizedType.BANK_CARD)) = "1101 **** **** **** 3256"
	 * </pre>
	 *
	 * @param str              字符串
	 * @param desensitizedType 脱敏类型;可以脱敏：用户id、中文名、身份证号、座机号、手机号、地址、电子邮件、密码
	 * @return 脱敏之后的字符串
	 * @author dazer and neusoft and qiaomu
	 * @since 5.6.2
	 */
	public static String desensitized(CharSequence str, DesensitizedUtil.DesensitizedType desensitizedType) {
		if (StrUtil.isBlank(str)) {
			return StrUtil.EMPTY;
		}
		String newStr = String.valueOf(str);
		switch (desensitizedType) {
			case USER_ID:
				newStr = String.valueOf(DesensitizedUtil.userId());
				break;
			case CHINESE_NAME:
				newStr = DesensitizedUtil.chineseName(String.valueOf(str));
				break;
			case ID_CARD:
				newStr = DesensitizedUtil.idCardNum(String.valueOf(str), 1, 2);
				break;
			case FIXED_PHONE:
				newStr = DesensitizedUtil.fixedPhone(String.valueOf(str));
				break;
			case MOBILE_PHONE:
				newStr = DesensitizedUtil.mobilePhone(String.valueOf(str));
				break;
			case ADDRESS:
				newStr = DesensitizedUtil.address(String.valueOf(str), 8);
				break;
			case EMAIL:
				newStr = DesensitizedUtil.email(String.valueOf(str));
				break;
			case PASSWORD:
				newStr = DesensitizedUtil.password(String.valueOf(str));
				break;
			case CAR_LICENSE:
				newStr = DesensitizedUtil.carLicense(String.valueOf(str));
				break;
			case BANK_CARD:
				newStr = DesensitizedUtil.bankCard(String.valueOf(str));
				break;
			default:
		}
		return newStr;
	}

	/**
	 * 【用户id】不对外提供userId
	 *
	 * @return 脱敏后的主键
	 */
	public static Long userId() {
		return 0L;
	}

	/**
	 * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
	 *
	 * @param fullName 姓名
	 * @return 脱敏后的姓名
	 */
	public static String chineseName(String fullName) {
		if (StrUtil.isBlank(fullName)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(fullName, 1, fullName.length());
	}

	/**
	 * 【身份证号】前1位 和后2位
	 *
	 * @param idCardNum 身份证
	 * @param front     保留：前面的front位数；从1开始
	 * @param end       保留：后面的end位数；从1开始
	 * @return 脱敏后的身份证
	 */
	public static String idCardNum(String idCardNum, int front, int end) {
		//身份证不能为空
		if (StrUtil.isBlank(idCardNum)) {
			return StrUtil.EMPTY;
		}
		//需要截取的长度不能大于身份证号长度
		if ((front + end) > idCardNum.length()) {
			return StrUtil.EMPTY;
		}
		//需要截取的不能小于0
		if (front < 0 || end < 0) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(idCardNum, front, idCardNum.length() - end);
	}

	/**
	 * 【固定电话 前四位，后两位
	 *
	 * @param num 固定电话
	 * @return 脱敏后的固定电话；
	 */
	public static String fixedPhone(String num) {
		if (StrUtil.isBlank(num)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(num, 4, num.length() - 2);
	}

	/**
	 * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
	 *
	 * @param num 移动电话；
	 * @return 脱敏后的移动电话；
	 */
	public static String mobilePhone(String num) {
		if (StrUtil.isBlank(num)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(num, 3, num.length() - 4);
	}

	/**
	 * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
	 *
	 * @param address       家庭住址
	 * @param sensitiveSize 敏感信息长度
	 * @return 脱敏后的家庭地址
	 */
	public static String address(String address, int sensitiveSize) {
		if (StrUtil.isBlank(address)) {
			return StrUtil.EMPTY;
		}
		int length = address.length();
		return StrUtil.hide(address, length - sensitiveSize, length);
	}

	/**
	 * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
	 *
	 * @param email 邮箱
	 * @return 脱敏后的邮箱
	 */
	public static String email(String email) {
		if (StrUtil.isBlank(email)) {
			return StrUtil.EMPTY;
		}
		int index = StrUtil.indexOf(email, '@');
		if (index <= 1) {
			return email;
		}
		return StrUtil.hide(email, 1, index);
	}

	/**
	 * 【密码】密码的全部字符都用*代替，比如：******
	 *
	 * @param password 密码
	 * @return 脱敏后的密码
	 */
	public static String password(String password) {
		if (StrUtil.isBlank(password)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.repeat('*', password.length());
	}

	/**
	 * 【中国车牌】车牌中间用*代替
	 * eg1：null       -》 ""
	 * eg1：""         -》 ""
	 * eg3：苏D40000   -》 苏D4***0
	 * eg4：陕A12345D  -》 陕A1****D
	 * eg5：京A123     -》 京A123     如果是错误的车牌，不处理
	 *
	 * @param carLicense 完整的车牌号
	 * @return 脱敏后的车牌
	 */
	public static String carLicense(String carLicense) {
		if (StrUtil.isBlank(carLicense)) {
			return StrUtil.EMPTY;
		}
		// 普通车牌
		if (carLicense.length() == 7) {
			carLicense = StrUtil.hide(carLicense, 3, 6);
		} else if (carLicense.length() == 8) {
			// 新能源车牌
			carLicense = StrUtil.hide(carLicense, 3, 7);
		}
		return carLicense;
	}

	/**
	 * 银行卡号脱敏
	 * eg: 1101 **** **** **** 3256
	 *
	 * @param bankCardNo 银行卡号
	 * @return 脱敏之后的银行卡号
	 * @since 5.6.3
	 */
	public static String bankCard(String bankCardNo) {
		if (StrUtil.isBlank(bankCardNo)) {
			return bankCardNo;
		}
		bankCardNo = StrUtil.trim(bankCardNo);
		if (bankCardNo.length() < 9) {
			return bankCardNo;
		}

		final int length = bankCardNo.length();
		final int midLength = length - 8;
		final StringBuilder buf = new StringBuilder();

		buf.append(bankCardNo, 0, 4);
		for (int i = 0; i < midLength; ++i) {
			if (i % 4 == 0) {
				buf.append(CharUtil.SPACE);
			}
			buf.append('*');
		}
		buf.append(CharUtil.SPACE).append(bankCardNo, length - 4, length);
		return buf.toString();
	}
}
