/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
