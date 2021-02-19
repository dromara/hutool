package cn.hutool.extra.mail;

import org.junit.Assert;
import org.junit.Test;

/**
 * 默认邮件帐户设置测试
 * @author looly
 *
 */
public class MailAccountTest {
	
	@Test
	public void parseSettingTest() {
		MailAccount account = GlobalMailAccount.INSTANCE.getAccount();
		account.getSmtpProps();
		
		Assert.assertNotNull(account.getCharset());
		Assert.assertTrue(account.isSslEnable());
	}
}
