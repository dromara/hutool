package cn.hutool.extra.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.security.GeneralSecurityException;

/**
 * 默认邮件帐户设置测试
 *
 * @author looly
 */
public class MailAccountTest {

	@Test
	public void parseSettingTest() {
		MailAccount account = GlobalMailAccount.INSTANCE.getAccount();
		account.getSmtpProps();

		Assert.assertNotNull(account.getCharset());
		Assert.assertTrue(account.isSslEnable());
	}

	/**
	 * 测试案例：使用QQ邮箱、AOL邮箱，如果不改SocketFactory实例，会报错（unable to find valid certification path to requested target），
	 * hutool mail中仅提供了'mail.smtp.socketFactory.class'属性，但是没提供'mail.smtp.ssl.socketFactory'属性
	 * 参见 com.sun.mail.util.SocketFetcher.getSocket(java.lang.String, int, java.util.Properties, java.lang.String, boolean)
	 * <p>
	 * 已经测试通过
	 */
	@Test
	@Ignore
	public void customPropertyTest() throws GeneralSecurityException {
		MailAccount mailAccount = new MailAccount();
		mailAccount.setFrom("xxx@xxx.com");
		mailAccount.setPass("xxxxxx");

		mailAccount.setHost("smtp.aol.com");

		// 使用其他配置属性
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		mailAccount.setCustomProperty("mail.smtp.ssl.socketFactory", sf);

		mailAccount.setAuth(true);
		mailAccount.setSslEnable(true);

		Mail mail = Mail.create(mailAccount)
				.setTos("xx@xx.com")
				.setTitle("邮箱验证")
				.setContent("您的验证码是：<h3>2333</h3>")
				.setHtml(true);

		mail.send();
	}
}
