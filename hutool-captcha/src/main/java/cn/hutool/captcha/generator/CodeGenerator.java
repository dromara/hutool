package cn.hutool.captcha.generator;

/**
 * 验证码文字生成器
 * 
 * @author looly
 * @since 4.1.2
 */
public interface CodeGenerator {
	/**
	 * 生成验证码
	 * 
	 * @return 验证码
	 */
	public String generate();

	/**
	 * 获取验证码长度
	 * 
	 * @return 验证码长度
	 */
	public int getLength();

	/**
	 * 验证用户输入的字符串是否与生成的验证码匹配<br>
	 * 用户通过实现此方法定义验证码匹配方式
	 * 
	 * @param code 生成的随机验证码
	 * @param userInputCode 用户输入的验证码
	 * @return 是否验证通过
	 */
	public boolean verify(String code, String userInputCode);
}
