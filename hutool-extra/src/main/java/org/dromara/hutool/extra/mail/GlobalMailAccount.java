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

import org.dromara.hutool.core.io.IORuntimeException;

/**
 * 全局邮件帐户，依赖于邮件配置文件{@link MailAccount#MAIL_SETTING_PATHS}
 *
 * @author looly
 *
 */
public enum GlobalMailAccount {
	INSTANCE;

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
