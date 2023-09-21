/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.mail;

import org.eclipse.angus.mail.util.MailSSLSocketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;

/**
 * 默认邮件帐户设置测试
 *
 * @author looly
 */
public class MailAccountTest {

	@Test
	public void parseSettingTest() {
		final MailAccount account = GlobalMailAccount.INSTANCE.getAccount();
		account.getSmtpProps();

		Assertions.assertNotNull(account.getPass());
		Assertions.assertNotNull(account.getCharset());
		Assertions.assertTrue(account.isSslEnable());
	}

	/**
	 * 测试案例：使用QQ邮箱、AOL邮箱，如果不改SocketFactory实例，会报错（unable to find valid certification path to requested target），
	 * hutool mail中仅提供了'mail.smtp.socketFactory.class'属性，但是没提供'mail.smtp.ssl.socketFactory'属性
	 * 参见 com.sun.mail.util.SocketFetcher.getSocket(java.lang.String, int, java.util.Properties, java.lang.String, boolean)
	 * <p>
	 * 已经测试通过
	 */
	@Test
	@Disabled
	public void customPropertyTest() throws GeneralSecurityException {
		final MailAccount mailAccount = new MailAccount();
		mailAccount.setFrom("xxx@xxx.com");
		mailAccount.setPass("xxxxxx".toCharArray());

		mailAccount.setHost("smtp.aol.com");

		// 使用其他配置属性
		final MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		mailAccount.setCustomProperty("mail.smtp.ssl.socketFactory", sf);

		mailAccount.setAuth(true);
		mailAccount.setSslEnable(true);

		final Mail mail = Mail.of(mailAccount)
				.setTos("xx@xx.com")
				.setTitle("邮箱验证")
				.setContent("您的验证码是：<h3>2333</h3>")
				.setHtml(true);

		mail.send();
	}
}
