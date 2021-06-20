package cn.hutool.extra.mail;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送测试
 * @author looly
 *
 */
public class MailTest {

	@Test
	@Ignore
	public void sendWithFileTest() {
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true, FileUtil.file("d:/测试附件文本.txt"));
	}

	@Test
	@Ignore
	public void sendWithLongNameFileTest() {
		//附件名长度大于60时的测试
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true, FileUtil.file("d:/6-LongLong一阶段平台建设周报2018.3.12-3.16.xlsx"));
	}

	@Test
	@Ignore
	public void sendWithImageTest() {
		Map<String, InputStream> map = new HashMap<>();
		map.put("testImage", FileUtil.getInputStream("f:/test/me.png"));
		MailUtil.sendHtml("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1><img src=\"cid:testImage\" />", map);
	}

	@Test
	@Ignore
	public void sendHtmlTest() {
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true);
	}

	@Test
	@Ignore
	public void sendByAccountTest() {
		MailAccount account = new MailAccount();
		account.setHost("smtp.yeah.net");
		account.setPort(465);
		account.setSslEnable(true);
		account.setFrom("hutool@yeah.net");
		account.setUser("hutool");
		account.setPass("q1w2e3");
		MailUtil.send(account, "hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true);
	}

	@Test
	public void mailAccountTest() {
		MailAccount account = new MailAccount();
		account.setFrom("hutool@yeah.net");
		account.setDebug(true);
		account.defaultIfEmpty();
		Properties props = account.getSmtpProps();
		Assert.assertEquals("true", props.getProperty("mail.debug"));
	}
}
