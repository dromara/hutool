package com.xiaoleilu.hutool.extra.mail;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 邮件发送测试
 * @author looly
 *
 */
public class MailTest {
	
	@Test
	@Ignore
	public void sendTest() {
		MailUtil.send("914104645@qq.com", "测试", "邮件来自Hutool测试", false);
	}
}
