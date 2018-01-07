package cn.hutool.extra.mail;

import java.io.File;
import java.util.Collection;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;

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
	 * @param to 收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param files 附件列表
	 * @since 3.2.0
	 */
	public static void sendText(String to, String subject, String content, File... files) {
		send(to, subject, content, false, files);
	}
	
	/**
	 * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * 
	 * @param to 收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param files 附件列表
	 * @since 3.2.0
	 */
	public static void sendHtml(String to, String subject, String content, File... files) {
		send(to, subject, content, true, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
	 * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
	 * 
	 * @param to 收件人
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml 是否为HTML
	 * @param files 附件列表
	 */
	public static void send(String to, String subject, String content, boolean isHtml, File... files) {
		send(splitTos(to), subject, content, isHtml, files);
	}
	
	/**
	 * 使用配置文件中设置的账户发送文本邮件，发送给多人
	 * 
	 * @param tos 收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param files 附件列表
	 */
	public static void sendText(Collection<String> tos, String subject, String content, File... files) {
		send(tos, subject, content, false, files);
	}
	
	/**
	 * 使用配置文件中设置的账户发送HTML邮件，发送给多人
	 * 
	 * @param tos 收件人列表
	 * @param subject 标题
	 * @param content 正文
	 * @param files 附件列表
	 * @since 3.2.0
	 */
	public static void sendHtml(Collection<String> tos, String subject, String content, File... files) {
		send(tos, subject, content, true, files);
	}

	/**
	 * 使用配置文件中设置的账户发送邮件，发送给多人
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
	 * @param to 收件人，多个收件人逗号或者分号隔开
	 * @param subject 标题
	 * @param content 正文
	 * @param isHtml 是否为HTML格式
	 * @param files 附件列表
	 * @since 3.2.0
	 */
	public static void send(MailAccount mailAccount, String to, String subject, String content, boolean isHtml, File... files) {
		send(mailAccount, splitTos(to), subject, content, isHtml, files);
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
		Console.log(mailAccount);
		Mail.create(mailAccount)//
				.to(tos.toArray(new String[tos.size()]))//
				.setTitle(subject)//
				.setContent(content)//
				.setHtml(isHtml)//
				.setFiles(files)//
				.send();
	}
	
	//------------------------------------------------------------------------------------------------------------------------ Private method start
	/**
	 * 将多个联系人转为列表，分隔符为逗号或者分号
	 * 
	 * @param to 多个联系人
	 * @return 联系人列表
	 */
	private static List<String> splitTos(String to){
		Assert.notBlank(to);
		
		List<String> tos;
		if(StrUtil.contains(to, ',')) {
			tos = StrUtil.splitTrim(to, ',');
		}else if(StrUtil.contains(to, ';')) {
			tos = StrUtil.splitTrim(to, ';');
		}else {
			tos = CollUtil.newArrayList(to);
		}
		return tos;
	}
	//------------------------------------------------------------------------------------------------------------------------ Private method end
}
