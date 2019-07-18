package cn.hutool.extra;

import cn.hutool.extra.mail.MailUtil;

public class MailTest {
	public static void main(String[] args) {
		MailUtil.sendText("hutool@foxmail.com", "title test", "mail content test");
	}
}
