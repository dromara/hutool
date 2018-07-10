package cn.hutool.captcha.generator;

import cn.hutool.core.util.RandomUtil;

/**
 * 随机字符验证码生成器<br>
 * 可以通过传入的基础集合和长度随机生成验证码字符
 * 
 * @author looly
 * @since 4.1.2
 */
public class RandomGenerator implements CodeGenerator {

	/** 基础字符集合，用于随机获取字符串的字符集合 */
	private String baseStr;
	/** 验证码长度 */
	private int length;
	
	/**
	 * 构造，使用字母+数字做为基础
	 * 
	 * @param count 生成验证码长度
	 */
	public RandomGenerator(int count) {
		this(RandomUtil.BASE_CHAR_NUMBER, count);
	}

	/**
	 * 构造
	 * 
	 * @param baseStr 基础字符集合，用于随机获取字符串的字符集合
	 * @param length 生成验证码长度
	 */
	public RandomGenerator(String baseStr, int length) {
		this.baseStr = baseStr;
		this.length = length;
	}

	@Override
	public String generate() {
		return RandomUtil.randomString(this.baseStr, this.length);
	}
	
	@Override
	public int getLength() {
		return this.length;
	}

}
