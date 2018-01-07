package cn.hutool.extra.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import cn.hutool.core.util.ArrayUtil;

/**
 * 邮件内部工具类
 * @author looly
 * @since 3.2.3
 */
public class InternalMailUtil {
	
	/**
	 * 解析第一个地址
	 * 
	 * @param address 地址字符串
	 * @param charset 编码
	 * @return 地址列表
	 */
	public static InternetAddress parseFirstAddress(String address, Charset charset) {
		final InternetAddress[] internetAddresses = parseAddress(address, charset);
		if (ArrayUtil.isEmpty(internetAddresses)) {
			try {
				return new InternetAddress(address);
			} catch (AddressException e) {
				throw new MailException(e);
			}
		}
		return internetAddresses[0];
	}

	/**
	 * 将一个地址字符串解析为多个地址<br>
	 * 地址间使用" "、","、";"分隔
	 * 
	 * @param address 地址字符串
	 * @param charset 编码
	 * @return 地址列表
	 */
	public static InternetAddress[] parseAddress(String address, Charset charset) {
		InternetAddress[] addresses;
		try {
			addresses = InternetAddress.parse(address);
		} catch (AddressException e) {
			throw new MailException(e);
		}
		if (ArrayUtil.isNotEmpty(addresses)) {
			for (InternetAddress internetAddress : addresses) {
				try {
					internetAddress.setPersonal(internetAddress.getPersonal(), charset.name());
				} catch (UnsupportedEncodingException e) {
					throw new MailException(e);
				}
			}
		}

		return addresses;
	}

	/**
	 * 编码中文字符<br>
	 * 编码失败返回原字符串
	 * 
	 * @param text 被编码的文本
	 * @param charset 编码
	 * @return 编码后的结果
	 */
	public static String encodeText(String text, Charset charset) {
		try {
			return MimeUtility.encodeText(text, charset.name(), null);
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return text;
	}
}
