/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 邮件工具类，基于jakarta.mail封装
 *
 * @author looly
 * @since 6.0.0
 */
public class MailUtil {
	/**
	 * 使用配置文件中设置的账户发送文本邮件，发送给单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to      收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param files   附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String sendText(final String to, final String subject, final String content, final File... files) {
		return send(to, subject, content, false, files);
	}

	/**
	 * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to      收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param files   附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String sendHtml(final String to, final String subject, final String content, final File... files) {
		return send(to, subject, content, true, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to      收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml  是否为HTML
	 * @param files   附件列表
	 * @return message-id
	 */
	public static String send(final String to, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(splitAddress(to), subject, content, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
	 * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to      收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * @param cc      抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * @param bcc     密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml  是否为HTML
	 * @param files   附件列表
	 * @return message-id
	 * @since 4.0.3
	 */
	public static String send(final String to, final String cc, final String bcc, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送文本邮件，发送给多人
	 *
	 * @param tos     收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param files   附件列表
	 * @return message-id
	 */
	public static String sendText(final Collection<String> tos, final String subject, final String content, final File... files) {
		return send(tos, subject, content, false, files);
	}

	/**
	 * 使用配置文件中设置的账户发送HTML邮件，发送给多人
	 *
	 * @param tos     收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param files   附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String sendHtml(final Collection<String> tos, final String subject, final String content, final File... files) {
		return send(tos, subject, content, true, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送给多人
	 *
	 * @param tos     收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml  是否为HTML
	 * @param files   附件列表
	 * @return message-id
	 */
	public static String send(final Collection<String> tos, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(tos, null, null, subject, content, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送给多人
	 *
	 * @param tos     收件人列表
	 * @param ccs     抄送人列表，可以为null或空
	 * @param bccs    密送人列表，可以为null或空
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml  是否为HTML
	 * @param files   附件列表
	 * @return message-id
	 * @since 4.0.3
	 */
	public static String send(final Collection<String> tos, final Collection<String> ccs, final Collection<String> bccs, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(GlobalMailAccount.INSTANCE.getAccount(), true, tos, ccs, bccs, subject, content, null, isHtml, files);
	}

	// ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount 邮件认证对象
	 * @param to          收件人，多个收件人逗号或者分号隔开
	 * @param subject     标题
	 * @param content     正文
	 * @param isHtml      是否为HTML格式
	 * @param files       附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String send(final MailAccount mailAccount, final String to, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(mailAccount, splitAddress(to), subject, content, isHtml, files);
	}

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount 邮件帐户信息
	 * @param tos         收件人列表
	 * @param subject     标题
	 * @param content     正文
	 * @param isHtml      是否为HTML格式
	 * @param files       附件列表
	 * @return message-id
	 */
	public static String send(final MailAccount mailAccount, final Collection<String> tos, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(mailAccount, tos, null, null, subject, content, isHtml, files);
	}

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount 邮件帐户信息
	 * @param tos         收件人列表
	 * @param ccs         抄送人列表，可以为null或空
	 * @param bccs        密送人列表，可以为null或空
	 * @param subject     标题
	 * @param content     正文
	 * @param isHtml      是否为HTML格式
	 * @param files       附件列表
	 * @return message-id
	 * @since 4.0.3
	 */
	public static String send(final MailAccount mailAccount, final Collection<String> tos, final Collection<String> ccs, final Collection<String> bccs, final String subject, final String content, final boolean isHtml, final File... files) {
		return send(mailAccount, false, tos, ccs, bccs, subject, content, null, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to       收件人
	 * @param subject  标题
	 * @param content  正文
	 * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param files    附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String sendHtml(final String to, final String subject, final String content, final Map<String, InputStream> imageMap, final File... files) {
		return send(to, subject, content, imageMap, true, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to       收件人
	 * @param subject  标题
	 * @param content  正文
	 * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml   是否为HTML
	 * @param files    附件列表
	 * @return message-id
	 */
	public static String send(final String to, final String subject, final String content, final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		return send(splitAddress(to), subject, content, imageMap, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
	 * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 *
	 * @param to       收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * @param cc       抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * @param bcc      密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * @param subject  标题
	 * @param content  正文
	 * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml   是否为HTML
	 * @param files    附件列表
	 * @return message-id
	 * @since 4.0.3
	 */
	public static String send(final String to, final String cc, final String bcc, final String subject, final String content, final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		return send(splitAddress(to), splitAddress(cc), splitAddress(bcc), subject, content, imageMap, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送HTML邮件，发送给多人
	 *
	 * @param tos      收件人列表
	 * @param subject  标题
	 * @param content  正文
	 * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param files    附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String sendHtml(final Collection<String> tos, final String subject, final String content, final Map<String, InputStream> imageMap, final File... files) {
		return send(tos, subject, content, imageMap, true, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送给多人
	 *
	 * @param tos      收件人列表
	 * @param subject  标题
	 * @param content  正文
	 * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml   是否为HTML
	 * @param files    附件列表
	 * @return message-id
	 */
	public static String send(final Collection<String> tos, final String subject, final String content, final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		return send(tos, null, null, subject, content, imageMap, isHtml, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送给多人
	 *
	 * @param tos      收件人列表
	 * @param ccs      抄送人列表，可以为null或空
	 * @param bccs     密送人列表，可以为null或空
	 * @param subject  标题
	 * @param content  正文
	 * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml   是否为HTML
	 * @param files    附件列表
	 * @return message-id
	 * @since 4.0.3
	 */
	public static String send(final Collection<String> tos, final Collection<String> ccs, final Collection<String> bccs, final String subject, final String content, final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		return send(GlobalMailAccount.INSTANCE.getAccount(), true, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
	}

	// ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount 邮件认证对象
	 * @param to          收件人，多个收件人逗号或者分号隔开
	 * @param subject     标题
	 * @param content     正文
	 * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml      是否为HTML格式
	 * @param files       附件列表
	 * @return message-id
	 * @since 3.2.0
	 */
	public static String send(final MailAccount mailAccount, final String to, final String subject, final String content, final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		return send(mailAccount, splitAddress(to), subject, content, imageMap, isHtml, files);
	}

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount 邮件帐户信息
	 * @param tos         收件人列表
	 * @param subject     标题
	 * @param content     正文
	 * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml      是否为HTML格式
	 * @param files       附件列表
	 * @return message-id
	 * @since 4.6.3
	 */
	public static String send(final MailAccount mailAccount, final Collection<String> tos, final String subject, final String content, final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		return send(mailAccount, tos, null, null, subject, content, imageMap, isHtml, files);
	}

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount 邮件帐户信息
	 * @param tos         收件人列表
	 * @param ccs         抄送人列表，可以为null或空
	 * @param bccs        密送人列表，可以为null或空
	 * @param subject     标题
	 * @param content     正文
	 * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
	 * @param isHtml      是否为HTML格式
	 * @param files       附件列表
	 * @return message-id
	 * @since 4.6.3
	 */
	public static String send(final MailAccount mailAccount, final Collection<String> tos, final Collection<String> ccs, final Collection<String> bccs, final String subject, final String content, final Map<String, InputStream> imageMap,
							  final boolean isHtml, final File... files) {
		return send(mailAccount, false, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
	}

	/**
	 * 根据配置文件，获取邮件客户端会话
	 *
	 * @param mailAccount 邮件账户配置
	 * @param isSingleton 是否单例（全局共享会话）
	 * @return {@link Session}
	 * @since 5.5.7
	 */
	public static Session getSession(final MailAccount mailAccount, final boolean isSingleton) {
		Authenticator authenticator = null;
		if (mailAccount.isAuth()) {
			authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
						mailAccount.getUser(),
						String.valueOf(mailAccount.getPass())
					);
				}
			};
		}

		return isSingleton ? Session.getDefaultInstance(mailAccount.getSmtpProps(), authenticator) //
			: Session.getInstance(mailAccount.getSmtpProps(), authenticator);
	}

	// ------------------------------------------------------------------------------------------------------------------------ Private method start

	/**
	 * 发送邮件给多人
	 *
	 * @param mailAccount      邮件帐户信息
	 * @param useGlobalSession 是否全局共享Session
	 * @param tos              收件人列表
	 * @param ccs              抄送人列表，可以为null或空
	 * @param bccs             密送人列表，可以为null或空
	 * @param subject          标题
	 * @param content          正文
	 * @param imageMap         图片与占位符，占位符格式为cid:${cid}
	 * @param isHtml           是否为HTML格式
	 * @param files            附件列表
	 * @return message-id
	 * @since 4.6.3
	 */
	private static String send(final MailAccount mailAccount, final boolean useGlobalSession, final Collection<String> tos, final Collection<String> ccs, final Collection<String> bccs, final String subject, final String content,
							   final Map<String, InputStream> imageMap, final boolean isHtml, final File... files) {
		final Mail mail = Mail.of(mailAccount).setUseGlobalSession(useGlobalSession);

		// 可选抄送人
		if (CollUtil.isNotEmpty(ccs)) {
			mail.setCcs(ccs.toArray(new String[0]));
		}
		// 可选密送人
		if (CollUtil.isNotEmpty(bccs)) {
			mail.setBccs(bccs.toArray(new String[0]));
		}

		mail.setTos(tos.toArray(new String[0]));
		mail.setTitle(subject);
		mail.setContent(content);
		mail.setHtml(isHtml);
		mail.setFiles(files);

		// 图片
		if (MapUtil.isNotEmpty(imageMap)) {
			for (final Entry<String, InputStream> entry : imageMap.entrySet()) {
				mail.addImage(entry.getKey(), entry.getValue());
				// 关闭流
				IoUtil.closeQuietly(entry.getValue());
			}
		}

		return mail.send();
	}

	/**
	 * 将多个联系人转为列表，分隔符为逗号或者分号
	 *
	 * @param addresses 多个联系人，如果为空返回null
	 * @return 联系人列表
	 */
	private static List<String> splitAddress(final String addresses) {
		if (StrUtil.isBlank(addresses)) {
			return null;
		}

		final List<String> result;
		if (StrUtil.contains(addresses, CharUtil.COMMA)) {
			result = SplitUtil.splitTrim(addresses, StrUtil.COMMA);
		} else if (StrUtil.contains(addresses, ';')) {
			result = SplitUtil.splitTrim(addresses, ";");
		} else {
			result = ListUtil.of(addresses);
		}
		return result;
	}
	// ------------------------------------------------------------------------------------------------------------------------ Private method end
}
