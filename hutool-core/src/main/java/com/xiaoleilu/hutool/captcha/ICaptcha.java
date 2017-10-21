package com.xiaoleilu.hutool.captcha;

import java.io.OutputStream;

/**
 * 验证码接口，提供验证码对象接口定义
 * 
 * @author looly
 *
 */
public interface ICaptcha {

	/**
	 * 创建验证码
	 */
	void createCode();

	/**
	 * 获取验证码的文字内容
	 * 
	 * @return 验证码文字内容
	 */
	String getCode();

	/**
	 * 验证验证码是否正确，建议忽略大小写
	 * 
	 * @param userInputCode 用户输入的验证码
	 * @return 是否与生成的一直
	 */
	boolean verify(String userInputCode);

	/**
	 * 将验证码写出到目标流中
	 * 
	 * @param out 目标流
	 */
	void write(OutputStream out);
}
