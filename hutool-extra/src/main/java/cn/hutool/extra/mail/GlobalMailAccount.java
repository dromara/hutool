package cn.hutool.extra.mail;

import cn.hutool.core.io.IORuntimeException;

/**
 * 全局邮件帐户，依赖于邮件配置文件{@link MailAccount#MAIL_SETTING_PATH}或{@link MailAccount#MAIL_SETTING_PATH2}
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
	private GlobalMailAccount() {
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
		MailAccount mailAccount;
		try {
			mailAccount = new MailAccount(MailAccount.MAIL_SETTING_PATH);
		} catch (IORuntimeException e) {
			mailAccount = new MailAccount(MailAccount.MAIL_SETTING_PATH2);
		}
		return mailAccount;
	}
}
