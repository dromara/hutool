package cn.hutool.extra.mail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 邮件工具类，基于javax.mail封装
 *
 * @author looly
 * @since 3.1.2
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
	public static String sendText(String to, String subject, String content, File... files) {
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
	public static String sendHtml(String to, String subject, String content, File... files) {
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
	public static String send(String to, String subject, String content, boolean isHtml, File... files) {
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
	public static String send(String to, String cc, String bcc, String subject, String content, boolean isHtml, File... files) {
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
	public static String sendText(Collection<String> tos, String subject, String content, File... files) {
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
	public static String sendHtml(Collection<String> tos, String subject, String content, File... files) {
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
	public static String send(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
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
	public static String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
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
	public static String send(MailAccount mailAccount, String to, String subject, String content, boolean isHtml, File... files) {
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
	public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
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
	public static String send(MailAccount mailAccount, Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
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
	public static String sendHtml(String to, String subject, String content, Map<String, InputStream> imageMap, File... files) {
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
	public static String send(String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
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
	public static String send(String to, String cc, String bcc, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
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
	public static String sendHtml(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, File... files) {
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
	public static String send(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
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
	public static String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
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
	public static String send(MailAccount mailAccount, String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
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
	public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
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
	public static String send(MailAccount mailAccount, Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap,
	                          boolean isHtml, File... files) {
		return send(mailAccount, false, tos, ccs, bccs, subject, content, imageMap, isHtml, files);
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
	private static String send(MailAccount mailAccount, boolean useGlobalSession, Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content,
	                           Map<String, InputStream> imageMap, boolean isHtml, File... files) {
		final Mail mail = Mail.create(mailAccount).setUseGlobalSession(useGlobalSession);

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
			for (Entry<String, InputStream> entry : imageMap.entrySet()) {
				mail.addImage(entry.getKey(), entry.getValue());
				// 关闭流
				IoUtil.close(entry.getValue());
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
	private static List<String> splitAddress(String addresses) {
		if (StrUtil.isBlank(addresses)) {
			return null;
		}

		List<String> result;
		if (StrUtil.contains(addresses, ',')) {
			result = StrUtil.splitTrim(addresses, ',');
		} else if (StrUtil.contains(addresses, ';')) {
			result = StrUtil.splitTrim(addresses, ';');
		} else {
			result = CollUtil.newArrayList(addresses);
		}
		return result;
	}
	// ------------------------------------------------------------------------------------------------------------------------ Private method end
}
