package com.xiaoleilu.hutool.extra.mail;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.io.FileUtil;

/**
 * 邮件发送测试
 * @author looly
 *
 */
public class MailTest {
	
	@Test
	@Ignore
	public void sendTest() {
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true, FileUtil.file("d:/aaa.xml"));
	}
	
	@Test
	@Ignore
	public void sendByAccountTest() {
		MailAccount account = new MailAccount();
		account.setHost("smtp.yeah.net");
		account.setPort(25);
		account.setFrom("hutool@yeah.net");
		account.setUser("hutool");
		account.setPass("q1w2e3");
		MailUtil.send(account, CollUtil.newArrayList("914104645@qq.com"), "测试", "邮件来自Hutool测试", false);
	}
}
