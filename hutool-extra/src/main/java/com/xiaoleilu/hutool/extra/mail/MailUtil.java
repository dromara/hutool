package com.xiaoleilu.hutool.extra.mail;

import java.io.File;
import java.util.Collection;

import com.xiaoleilu.hutool.collection.CollUtil;

/**
 * 邮件工具类，基于javax.mail封装
 * 
 * @author looly
 * @since 3.1.2
 */
public class MailUtil {

	/**
	 * 发送邮件给单收件人
	 * 
	 * @param to 收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml 是否为HTML
	 * @param files 附件列表
	 */
	public static void send(String to, String subject, String content, boolean isHtml, File... files) {
		send(CollUtil.newArrayList(to), subject, content, isHtml, files);
	}

	/**
	 * 使用默认的设置账户发送邮件
	 * 
	 * @param tos 收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml 是否为HTML
	 * @param files 附件列表
	 */
	public static void send(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
		send(GlobalMailAccount.INSTANCE.getAccount(), tos, subject, content, isHtml, files);
	}

	/**
	 * 发送邮件给多人
	 * 
	 * @param mailAccount 邮件认证对象
	 * @param tos 收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml 是否为HTML格式
	 * @param files 附件列表
	 */
	public static void send(MailAccount mailAccount, Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
		Mail.create(mailAccount)//
				.to(tos.toArray(new String[tos.size()]))//
				.setTitle(subject)//
				.setContent(content)//
				.setHtml(isHtml)//
				.setFiles(files)//
				.send();
	}
}
