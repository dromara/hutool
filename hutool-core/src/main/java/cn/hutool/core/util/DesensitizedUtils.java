package cn.hutool.core.util;

/**
 * 脱敏utils
 * @author dazer and neusoft and qiaomu
 * @date 2021/3/20 09:55
 * @since 5.6.2
 */
public class DesensitizedUtils {
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
		PASSWORD;
	}

	/**
	 *  【用户id】不对外提供userId
	 * @return 脱敏后的主键
	 */
	public static Long userId(){
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
			return "";
		}
		String name = StrUtil.left(fullName, 1);
		return StrUtil.rightPad(name, StrUtil.length(fullName), "*");
	}

	/**
	 * 【身份证号】前1位 和后2位
	 *
	 * @param idCardNum 身份证
	 * @param front 保留：前面的front位；从：1开始；
	 * @param end 保留：后面的end位；从1：开始；
	 * @return 脱敏后的身份证
	 */
	public static String idCardNum(String idCardNum, int front, int end) {
		//身份证不能为空
		if (StrUtil.isEmpty(idCardNum)) {
			return "";
		}
		//需要截取的长度不能大于身份证号长度
		if ((front + end) > idCardNum.length()) {
			return "";
		}
		//需要截取的不能小于0
		if (front < 0 || end < 0) {
			return "";
		}
		//计算*的数量
		int asteriskCount = idCardNum.length() - (front + end);
		StringBuffer asteriskStr = new StringBuffer();
		for (int i = 0; i < asteriskCount; i++) {
			asteriskStr.append("*");
		}
		String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
		return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
	}

	/**
	 * 【固定电话 前四位，后两位
	 *
	 * @param num 固定电话
	 * @return 脱敏后的固定电话；
	 */
	public static String fixedPhone(String num) {
		if (StrUtil.isBlank(num)) {
			return "";
		}
		return StrUtil.left(num, 4).concat(StrUtil.removePrefix(StrUtil.leftPad(StrUtil.right(num, 2), StrUtil.length(num), "*"), "****"));
	}

	/**
	 * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
	 *
	 * @param num 移动电话；
	 * @return 脱敏后的移动电话；
	 */
	public static String mobilePhone(String num) {
		if (StrUtil.isBlank(num)) {
			return "";
		}
		return StrUtil.left(num, 3).concat(StrUtil.removePrefix(StrUtil.leftPad(StrUtil.right(num, 4), StrUtil.length(num), "*"), "***"));
	}

	/**
	 * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
	 *
	 * @param address 家庭住址
	 * @param sensitiveSize 敏感信息长度
	 * @return 脱敏后的家庭地址
	 */
	public static String address(String address, int sensitiveSize) {
		if (StrUtil.isBlank(address)) {
			return "";
		}
		int length = StrUtil.length(address);
		return StrUtil.rightPad(StrUtil.left(address, length - sensitiveSize), length, "*");
	}

	/**
	 * 【电子邮箱 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com>
	 *
	 * @param email 邮箱
	 * @return 脱敏后的邮箱
	 */
	public static String email(String email) {
		if (StrUtil.isBlank(email)) {
			return "";
		}
		int index = StrUtil.indexOf(email, '@');
		if (index <= 1) {
			return email;
		} else {
			return StrUtil.rightPad(StrUtil.left(email, 1), index, "*").concat(StrUtil.mid(email, index, StrUtil.length(email)));
		}
	}

	/**
	 * 【密码】密码的全部字符都用*代替，比如：******
	 *
	 * @param password 密码
	 * @return 脱敏后的邮箱
	 */
	public static String password(String password) {
		if (StrUtil.isBlank(password)) {
			return "";
		}
		String pwd = StrUtil.left(password, 0);
		return StrUtil.rightPad(pwd, StrUtil.length(password), "*");
	}
}
