package cn.hutool.captcha.generator;

import java.io.Serializable;

/**
 * 验证码文字生成器
 * 
 * @author looly
 * @since 4.1.2
 */
public interface CodeGenerator extends Serializable{

	/**
	 * 生成验证码
	 * 
	 * @return 验证码
	 */
	String generate();

	/**
	 * 验证用户输入的字符串是否与生成的验证码匹配<br>
	 * 用户通过实现此方法定义验证码匹配方式
	 * 
	 * @param code 生成的随机验证码
	 * @param userInputCode 用户输入的验证码
	 * @return 是否验证通过
	 */
	boolean verify(String code, String userInputCode);
}
