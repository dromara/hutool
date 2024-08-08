/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.mail;

import org.dromara.hutool.core.io.IORuntimeException;

import java.nio.charset.Charset;

/**
 * 全局邮件帐户，依赖于邮件配置文件{@link MailAccount#MAIL_SETTING_PATHS}
 *
 * @author looly
 */
public enum GlobalMailAccount {
	/**
	 * 单例
	 */
	INSTANCE;

	// mime
	private static final String SPLIT_LONG_PARAMS = "mail.mime.splitlongparameters";
	private static final String CHARSET = "mail.mime.charset";

	static {
		System.setProperty(SPLIT_LONG_PARAMS, "false");
		System.setProperty(CHARSET, INSTANCE.mailAccount.getCharset().name());
	}

	private final MailAccount mailAccount;

	/**
	 * 构造
	 */
	GlobalMailAccount() {
		mailAccount = createDefaultAccount();
	}

	/**
	 * 获得邮件帐户
	 *
	 * @return 邮件帐户
	 */
	public MailAccount getAccount() {
		return this.mailAccount;
	}

	/**
	 * 设置对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）<br>
	 * 注意此项为全局设置，此项会调用
	 * <pre>
	 * System.setProperty("mail.mime.splitlongparameters", true)
	 * </pre>
	 *
	 * @param splitLongParams 对于超长参数是否切分为多份
	 */
	public void setSplitLongParams(final boolean splitLongParams) {
		System.setProperty(SPLIT_LONG_PARAMS, String.valueOf(splitLongParams));
	}

	/**
	 * 设置全局默认编码
	 *
	 * @param charset 编码
	 */
	public void setCharset(final Charset charset) {
		System.setProperty(CHARSET, charset.name());
	}

	/**
	 * 创建默认帐户
	 *
	 * @return MailAccount
	 */
	private MailAccount createDefaultAccount() {
		for (final String mailSettingPath : MailAccount.MAIL_SETTING_PATHS) {
			try {
				return new MailAccount(mailSettingPath);
			} catch (final IORuntimeException ignore) {
				//ignore
			}
		}
		return null;
	}
}
