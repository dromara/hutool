package com.xiaoleilu.hutool.extra.mail;

/**
 * 全局邮件帐户，依赖于邮件配置文件{@link MailAccount#MAIL_SETTING_PATH}
 * @author looly
 *
 */
public enum GlobalMailAccount {
	INSTANCE;
	
	private final MailAccount mailAccount = new MailAccount(MailAccount.MAIL_SETTING_PATH);
	
	/**
	 * 获得邮件帐户
	 * @return 邮件帐户
	 */
	public MailAccount getAccount() {
		return this.mailAccount;
	}
}
