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

import org.dromara.hutool.core.io.file.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
	@Disabled
	public void sendWithFileTest() {
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true, FileUtil.file("d:/测试附件文本.txt"));
	}

	@Test
	@Disabled
	public void sendWithLongNameFileTest() {
		//附件名长度大于60时的测试
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true, FileUtil.file("d:/6-LongLong一阶段平台建设周报2018.3.12-3.16.xlsx"));
	}

	@Test
	@Disabled
	public void sendWithImageTest() {
		final Map<String, InputStream> map = new HashMap<>();
		map.put("testImage", FileUtil.getInputStream("f:/test/me.png"));
		MailUtil.sendHtml("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1><img src=\"cid:testImage\" />", map);
	}

	@Test
	@Disabled
	public void sendHtmlTest() {
		MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true);
	}

	@Test
	@Disabled
	public void sendByAccountTest() {
		final MailAccount account = new MailAccount();
		account.setHost("smtp.yeah.net");
		account.setPort(465);
		account.setSslEnable(true);
		account.setFrom("hutool@yeah.net");
		account.setUser("hutool");
		account.setPass("q1w2e3".toCharArray());
		MailUtil.send(account, "hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true);
	}

	@Test
	public void mailAccountTest() {
		final MailAccount account = new MailAccount();
		account.setFrom("hutool@yeah.net");
		account.setDebug(true);
		account.defaultIfEmpty();
		final Properties props = account.getSmtpProps();
		Assertions.assertEquals("true", props.getProperty("mail.debug"));
	}

	@Test
	@Disabled
	public void sendHtmlWithPicsTest() {
		HashMap<String, InputStream> map = new HashMap<>();
		map.put("abc", FileUtil.getInputStream("D:/test/abc.png"));
		map.put("abcd",FileUtil.getInputStream("D:/test/def.png"));

		MailUtil.sendHtml("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1><img src=\"cid:abc\"/><img src=\"cid:abcd\"/>",
				map);
	}
}
