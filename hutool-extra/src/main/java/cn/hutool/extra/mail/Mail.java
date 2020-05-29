package cn.hutool.extra.mail;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.FileTypeMap;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 邮件发送客户端
 *
 * @author looly
 * @since 3.2.0
 */
public class Mail {

	/**
	 * 邮箱帐户信息以及一些客户端配置信息
	 */
	private final MailAccount mailAccount;
	/**
	 * 收件人列表
	 */
	private String[] tos;
	/**
	 * 抄送人列表（carbon copy）
	 */
	private String[] ccs;
	/**
	 * 密送人列表（blind carbon copy）
	 */
	private String[] bccs;
	/**
	 * 回复地址(reply-to)
	 */
	private String[] reply;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 是否为HTML
	 */
	private boolean isHtml;
	/**
	 * 正文、附件和图片的混合部分
	 */
	private final Multipart multipart = new MimeMultipart();
	/**
	 * 是否使用全局会话，默认为false
	 */
	private boolean useGlobalSession = false;

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
	 * @param mailAccount 邮件帐户，如果为null使用默认配置文件的全局邮件配置
	 */
	public Mail(MailAccount mailAccount) {
		mailAccount = (null != mailAccount) ? mailAccount : GlobalMailAccount.INSTANCE.getAccount();
		this.mailAccount = mailAccount.defaultIfEmpty();
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Getters and Setters start

	/**
	 * 设置收件人
	 *
	 * @param tos 收件人列表
	 * @return this
	 * @see #setTos(String...)
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
	 * 设置多个回复地址(reply-to)
	 *
	 * @param reply 回复地址(reply-to)列表
	 * @return this
	 * @since 4.6.0
	 */
	public Mail setReply(String... reply) {
		this.reply = reply;
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
	 * 设置正文<br>
	 * 正文可以是普通文本也可以是HTML（默认普通文本），可以通过调用{@link #setHtml(boolean)} 设置是否为HTML
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
	 * 设置正文
	 *
	 * @param content 正文内容
	 * @param isHtml  是否为HTML
	 * @return this
	 */
	public Mail setContent(String content, boolean isHtml) {
		setContent(content);
		return setHtml(isHtml);
	}

	/**
	 * 设置文件类型附件，文件可以是图片文件，此时自动设置cid（正文中引用图片），默认cid为文件名
	 *
	 * @param files 附件文件列表
	 * @return this
	 */
	public Mail setFiles(File... files) {
		if (ArrayUtil.isEmpty(files)) {
			return this;
		}

		final DataSource[] attachments = new DataSource[files.length];
		for (int i = 0; i < files.length; i++) {
			attachments[i] = new FileDataSource(files[i]);
		}
		return setAttachments(attachments);
	}

	/**
	 * 增加附件或图片，附件使用{@link DataSource} 形式表示，可以使用{@link FileDataSource}包装文件表示文件附件
	 *
	 * @param attachments 附件列表
	 * @return this
	 * @since 4.0.9
	 */
	public Mail setAttachments(DataSource... attachments) {
		if (ArrayUtil.isNotEmpty(attachments)) {
			final Charset charset = this.mailAccount.getCharset();
			MimeBodyPart bodyPart;
			String nameEncoded;
			try {
				for (DataSource attachment : attachments) {
					bodyPart = new MimeBodyPart();
					bodyPart.setDataHandler(new DataHandler(attachment));
					nameEncoded = InternalMailUtil.encodeText(attachment.getName(), charset);
					// 普通附件文件名
					bodyPart.setFileName(nameEncoded);
					if (StrUtil.startWith(attachment.getContentType(), "image/")) {
						// 图片附件，用于正文中引用图片
						bodyPart.setContentID(nameEncoded);
					}
					this.multipart.addBodyPart(bodyPart);
				}
			} catch (MessagingException e) {
				throw new MailException(e);
			}
		}
		return this;
	}

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串，图片类型默认为"image/jpeg"
	 *
	 * @param cid         图片与占位符，占位符格式为cid:${cid}
	 * @param imageStream 图片文件
	 * @return this
	 * @since 4.6.3
	 */
	public Mail addImage(String cid, InputStream imageStream) {
		return addImage(cid, imageStream, null);
	}

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串
	 *
	 * @param cid         图片与占位符，占位符格式为cid:${cid}
	 * @param imageStream 图片流，不关闭
	 * @param contentType 图片类型，null赋值默认的"image/jpeg"
	 * @return this
	 * @since 4.6.3
	 */
	public Mail addImage(String cid, InputStream imageStream, String contentType) {
		ByteArrayDataSource imgSource;
		try {
			imgSource = new ByteArrayDataSource(imageStream, ObjectUtil.defaultIfNull(contentType, "image/jpeg"));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		imgSource.setName(cid);
		return setAttachments(imgSource);
	}

	/**
	 * 增加图片，图片的键对应到邮件模板中的占位字符串
	 *
	 * @param cid       图片与占位符，占位符格式为cid:${cid}
	 * @param imageFile 图片文件
	 * @return this
	 * @since 4.6.3
	 */
	public Mail addImage(String cid, File imageFile) {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(imageFile);
			return addImage(cid, in, FileTypeMap.getDefaultFileTypeMap().getContentType(imageFile));
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 设置字符集编码
	 *
	 * @param charset 字符集编码
	 * @return this
	 * @see MailAccount#setCharset(Charset)
	 */
	public Mail setCharset(Charset charset) {
		this.mailAccount.setCharset(charset);
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
	 * @return message-id
	 * @throws MailException 邮件发送异常
	 */
	public String send() throws MailException {
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
	 * @return message-id
	 * @throws MessagingException 发送异常
	 */
	private String doSend() throws MessagingException {
		final MimeMessage mimeMessage = buildMsg();
		Transport.send(mimeMessage);
		return mimeMessage.getMessageID();
	}

	/**
	 * 构建消息
	 *
	 * @return {@link MimeMessage}消息
	 * @throws MessagingException 消息异常
	 */
	private MimeMessage buildMsg() throws MessagingException {
		final Charset charset = this.mailAccount.getCharset();
		final MimeMessage msg = new MimeMessage(getSession(this.useGlobalSession));
		// 发件人
		final String from = this.mailAccount.getFrom();
		if (StrUtil.isEmpty(from)) {
			// 用户未提供发送方，则从Session中自动获取
			msg.setFrom();
		} else {
			msg.setFrom(InternalMailUtil.parseFirstAddress(from, charset));
		}
		// 标题
		msg.setSubject(this.title, charset.name());
		// 发送时间
		msg.setSentDate(new Date());
		// 内容和附件
		msg.setContent(buildContent(charset));
		// 收件人
		msg.setRecipients(MimeMessage.RecipientType.TO, InternalMailUtil.parseAddressFromStrs(this.tos, charset));
		// 抄送人
		if (ArrayUtil.isNotEmpty(this.ccs)) {
			msg.setRecipients(MimeMessage.RecipientType.CC, InternalMailUtil.parseAddressFromStrs(this.ccs, charset));
		}
		// 密送人
		if (ArrayUtil.isNotEmpty(this.bccs)) {
			msg.setRecipients(MimeMessage.RecipientType.BCC, InternalMailUtil.parseAddressFromStrs(this.bccs, charset));
		}
		// 回复地址(reply-to)
		if (ArrayUtil.isNotEmpty(this.reply)) {
			msg.setReplyTo(InternalMailUtil.parseAddressFromStrs(this.reply, charset));
		}

		return msg;
	}

	/**
	 * 构建邮件信息主体
	 *
	 * @param charset 编码
	 * @return 邮件信息主体
	 * @throws MessagingException 消息异常
	 */
	private Multipart buildContent(Charset charset) throws MessagingException {
		// 正文
		final MimeBodyPart body = new MimeBodyPart();
		body.setContent(content, StrUtil.format("text/{}; charset={}", isHtml ? "html" : "plain", charset));
		this.multipart.addBodyPart(body);

		return this.multipart;
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
		final MailAccount mailAccount = this.mailAccount;
		Authenticator authenticator = null;
		if (mailAccount.isAuth()) {
			authenticator = new UserPassAuthenticator(mailAccount.getUser(), mailAccount.getPass());
		}

		return isSingleton ? Session.getDefaultInstance(mailAccount.getSmtpProps(), authenticator) //
				: Session.getInstance(mailAccount.getSmtpProps(), authenticator);
	}
	// --------------------------------------------------------------- Private method end
}
