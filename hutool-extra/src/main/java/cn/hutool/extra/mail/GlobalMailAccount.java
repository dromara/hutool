package cn.hutool.extra.mail;

import cn.hutool.core.io.IORuntimeException;

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
		for (String mailSettingPath : MailAccount.MAIL_SETTING_PATHS) {
			try {
				return new MailAccount(mailSettingPath);
			} catch (IORuntimeException ignore) {
				//ignore
			}
		}
		return null;
	}
}
