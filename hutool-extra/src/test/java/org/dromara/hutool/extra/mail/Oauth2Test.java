package org.dromara.hutool.extra.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class Oauth2Test {
	@Test
	@Disabled
	void sendTest() {
		final MailAccount mailAccount = new MailAccount();
		mailAccount.setHost("smtp.office365.com");
		mailAccount.setPort(587);
		mailAccount.setFrom("xxxx@outlook.com");
		mailAccount.setUser("xxxx");
		// 这里使用生成的token
		mailAccount.setPass("token".toCharArray());
		mailAccount.setAuth(true);
		mailAccount.setStarttlsEnable(true);
		// 这里关掉SSL
		mailAccount.setSslEnable(false);
		// 使用XOAUTH2
		mailAccount.setCustomProperty("mail.smtp.auth.mechanisms", "XOAUTH2");
		mailAccount.setCustomProperty("mail.smtp.auth.login.disable", "true");
		mailAccount.setCustomProperty("mail.smtp.auth.plain.disable", "true");

		final String id = Mail.of(mailAccount)
			.setTos("xxx@qq.com")
			.setContent("Mail test from Outlook!")
			.setTitle("测试Outlook邮件")
			.send();

		Console.log(id);
	}

	/**
	 * https://medium.com/@tempmailwithpassword/sending-emails-with-java-using-oauth2-and-office-365-b164d54f68fc
	 *
	 * @throws MessagingException 异常
	 */
	@Test
	@Disabled
	void sendTest2() throws MessagingException {
		final Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
		props.put("mail.smtp.auth.login.disable", "true");
		props.put("mail.smtp.auth.plain.disable", "true");
		final Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("xx", "123");
			}
		});
		final MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("xxx@outlook.com"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));
		message.setSubject("Your Subject Here");
		message.setText("Email body content here.");
		Transport.send(message);
	}
}
