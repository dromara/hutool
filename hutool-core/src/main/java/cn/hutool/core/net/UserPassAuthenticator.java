package cn.hutool.core.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 账号密码形式的{@link Authenticator} 实现。
 *
 * @author looly
 * @since 5.5.3
 */
public class UserPassAuthenticator extends Authenticator {

	/**
	 * 创建账号密码形式的{@link Authenticator} 实现。
	 *
	 * @param user 用户名
	 * @param pass 密码
	 * @return PassAuth
	 */
	public static UserPassAuthenticator of(final String user, final char[] pass) {
		return new UserPassAuthenticator(user, pass);
	}

	private final PasswordAuthentication auth;

	/**
	 * 构造
	 *
	 * @param user 用户名
	 * @param pass 密码
	 */
	public UserPassAuthenticator(final String user, final char[] pass) {
		auth = new PasswordAuthentication(user, pass);
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return auth;
	}
}
