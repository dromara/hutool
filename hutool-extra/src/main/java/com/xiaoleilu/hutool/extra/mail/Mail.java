package com.xiaoleilu.hutool.extra.mail;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 邮件发送客户端
 * 
 * @author looly
 * @since 3.2.0
 */
public class Mail {

	//邮件客户端
	private MailAccount mailAccount;
	//收件人列表
	private String[] tos;
	//标题
	private String title;
	//内容
	private String content;
	//是否为HTML
	private boolean isHtml;
	//附件列表
	private File[] files;
	//正文编码
	private Charset charset = CharsetUtil.CHARSET_UTF_8;

	/**
	 * 创建邮件客户端
	 * 
	 * @param mailAccount 邮件帐号
	 * @return {@link Mail}
	 */
	public static Mail create(MailAccount mailAccount) {
		return new Mail(mailAccount);
	}

	/**
	 * 创建邮件客户端，使用全局邮件帐户
	 * 
	 * @return {@link Mail}
	 */
	public static Mail create() {
		return new Mail();
	}

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，使用全局邮件帐户
	 */
	public Mail() {
		this(GlobalMailAccount.INSTANCE.getAccount());
	}

	/**
	 * 构造
	 * 
	 * @param mailAccount 邮件帐户
	 */
	public Mail(MailAccount mailAccount) {
		mailAccount = (null == mailAccount) ? null : GlobalMailAccount.INSTANCE.getAccount();
		this.mailAccount = mailAccount.defaultIfEmpty();
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Getters and Setters start
	/**
	 * 设置收件人
	 * 
	 * @param tos 收件人列表
	 * @return this
	 */
	public Mail to(String... tos) {
		return setTos(tos);
	}
	
	/**
	 * 设置收件人
	 * 
	 * @param tos 收件人列表
	 * @return this
	 */
	public Mail setTos(String... tos) {
		this.tos = tos;
		return this;
	}

	/**
	 * 设置标题
	 * 
	 * @param title 标题
	 * @return this
	 */
	public Mail setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * 设置正文
	 * 
	 * @param content 正文
	 * @return this
	 */
	public Mail setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * 设置是否是HTML
	 * 
	 * @param isHtml 是否为HTML
	 * @return this
	 */
	public Mail setHtml(boolean isHtml) {
		this.isHtml = isHtml;
		return this;
	}

	/**
	 * 设置附件
	 * 
	 * @param files 附件文件列表
	 * @return this
	 */
	public Mail setFiles(File... files) {
		this.files = files;
		return this;
	}

	/**
	 * 设置字符集编码
	 * 
	 * @param charset 字符集编码
	 * @return this
	 */
	public Mail setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}
	// --------------------------------------------------------------- Getters and Setters end

	/**
	 * 发送
	 * 
	 * @return this
	 * @throws MailException 邮件发送异常
	 */
	public Mail send() throws MailException {
		try {
			return doSend();
		} catch (MessagingException e) {
			throw new MailException(e);
		}
	}

	// --------------------------------------------------------------- Private method start
	/**
	 * 执行发送
	 * 
	 * @return this
	 * @throws MessagingException 发送异常
	 */
	private Mail doSend() throws MessagingException {
		Transport.send(buildMsg());
		return this;
	}

	/**
	 * 构建消息
	 * 
	 * @return {@link MimeMessage}消息
	 * @throws MessagingException 消息异常
	 */
	private MimeMessage buildMsg() throws MessagingException {
		final MimeMessage msg = new MimeMessage(createSession());
		// 发件人
		msg.setFrom(InternalMailUtil.parseFirstAddress(this.mailAccount.getFrom(), this.charset));
		// 标题
		msg.setSubject(this.title, this.charset.name());
		// 发送时间
		msg.setSentDate(new Date());
		// 内容和附件
		msg.setContent(buildContent());
		// 收件人
		final InternetAddress[] toAdds = new InternetAddress[tos.length];
		for (int i = 0; i < tos.length; i++) {
			toAdds[i] = new InternetAddress(tos[i]);
		}
		msg.setRecipients(MimeMessage.RecipientType.TO, toAdds);
		return msg;
	}

	/**
	 * 构建邮件信息主体
	 * 
	 * @param content 邮件信息正文
	 * @param isHtml 是否为HTML
	 * @param files 附件列表
	 * @return 邮件信息主体
	 * @throws MessagingException 消息异常
	 */
	private Multipart buildContent() throws MessagingException {
		final Multipart mainPart = new MimeMultipart();

		// 正文
		final BodyPart body = new MimeBodyPart();
		body.setContent(content, StrUtil.format("text/{}; charset={}", isHtml ? "html" : "plain", charset));
		mainPart.addBodyPart(body);

		// 附件
		if (ArrayUtil.isNotEmpty(files)) {
			BodyPart bodyPart;
			for (File file : files) {
				bodyPart = new MimeBodyPart();
				bodyPart.setDataHandler(new DataHandler(new FileDataSource(file)));
				bodyPart.setFileName(InternalMailUtil.encodeText(file.getName(), this.charset));
				mainPart.addBodyPart(bodyPart);
			}
		}

		return mainPart;
	}
	
	/**
	 * 创建邮件会话
	 * 
	 * @param mailAccount 邮件帐号信息
	 * @return 邮件会话 {@link Session}
	 */
	private Session createSession() {
		return Session.getDefaultInstance(mailAccount.getSmtpProps(), //
				mailAccount.isAuth() ? new UserPassAuthenticator(mailAccount.getUser(), mailAccount.getPass()) : null);
	}
	// --------------------------------------------------------------- Private method end
}
