package cn.hutool.extra.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

/**
 * jakarta用户名密码验证器
 *
 * @author looly
 * @since 5.8.30
 */
public class JakartaUserPassAuthenticator extends Authenticator {

	private final String user;
	private final String pass;

	/**
	 * 构造
	 *
	 * @param user 用户名
	 * @param pass 密码
	 */
	public JakartaUserPassAuthenticator(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.user, this.pass);
	}

}
