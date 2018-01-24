package cn.hutool.extra.mail;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 邮件发送客户端
 * 
 * @author looly
 * @since 3.2.0
 */
public class Mail {

	/** 邮箱帐户信息以及一些客户端配置信息 */
	private MailAccount mailAccount;
	/** 收件人列表 */
	private String[] tos;
	/** 抄送人列表（carbon copy）*/
	private String[] ccs;
	/** 密送人列表（blind carbon copy）*/
	private String[] bccs;
	/** 标题 */
	private String title;
	/** 内容 */
	private String content;
	/** 是否为HTML */
	private boolean isHtml;
	/** 附件列表 */
	private File[] files;
	/** 编码用于编码邮件正文和发送人、收件人等中文 */
	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	/** 是否使用全局会话，默认为true */
	private boolean useGlobalSession = true;

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
	 * 设置多个收件人
	 * 
	 * @param tos 收件人列表
	 * @return this
	 */
	public Mail setTos(String... tos) {
		this.tos = tos;
		return this;
	}
	
	/**
	 * 设置多个抄送人（carbon copy）
	 * 
	 * @param ccs 抄送人列表
	 * @return this
	 * @since 4.0.3
	 */
	public Mail setCcs(String... ccs) {
		this.ccs = ccs;
		return this;
	}
	
	/**
	 * 设置多个密送人（blind carbon copy）
	 * 
	 * @param bccs 密送人列表
	 * @return this
	 * @since 4.0.3
	 */
	public Mail setBccs(String... bccs) {
		this.bccs = bccs;
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
	
	/**
	 * 设置是否使用全局会话，默认为true
	 * 
	 * @param isUseGlobalSession 是否使用全局会话，默认为true
	 * @return this
	 * @since 4.0.2
	 */
	public Mail setUseGlobalSession(boolean isUseGlobalSession) {
		this.useGlobalSession = isUseGlobalSession;
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
		final MimeMessage msg = new MimeMessage(getSession(this.useGlobalSession));
		// 发件人
		final String from = this.mailAccount.getFrom();
		if(StrUtil.isEmpty(from)) {
			//用户未提供发送方，则从Session中自动获取
			msg.setFrom();
		}else {
			msg.setFrom(InternalMailUtil.parseFirstAddress(from, this.charset));
		}
		// 标题
		msg.setSubject(this.title, this.charset.name());
		// 发送时间
		msg.setSentDate(new Date());
		// 内容和附件
		msg.setContent(buildContent());
		// 收件人
		msg.setRecipients(MimeMessage.RecipientType.TO, InternalMailUtil.parseAddressFromStrs(this.tos, this.charset));
		// 抄送人
		if(ArrayUtil.isNotEmpty(this.ccs)) {
			msg.setRecipients(MimeMessage.RecipientType.CC, InternalMailUtil.parseAddressFromStrs(this.ccs, this.charset));
		}
		// 密送人
		if(ArrayUtil.isNotEmpty(this.bccs)) {
			msg.setRecipients(MimeMessage.RecipientType.BCC, InternalMailUtil.parseAddressFromStrs(this.bccs, this.charset));
		}
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
	 * 获取默认邮件会话<br>
	 * 如果为全局单例的会话，则全局只允许一个邮件帐号，否则每次发送邮件会新建一个新的会话
	 * 
	 * @param isSingleton 是否使用单例Session
	 * @return 邮件会话 {@link Session}
	 * @since 4.0.2
	 */
	private Session getSession(boolean isSingleton) {
		Authenticator authenticator = null;
		if (mailAccount.isAuth()) {
			authenticator = new UserPassAuthenticator(mailAccount.getUser(), mailAccount.getPass());
		}

		return isSingleton ? Session.getDefaultInstance(mailAccount.getSmtpProps(), authenticator) //
				: Session.getInstance(mailAccount.getSmtpProps(), authenticator);
	}
	// --------------------------------------------------------------- Private method end
}
