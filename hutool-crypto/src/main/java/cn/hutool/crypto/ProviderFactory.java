package cn.hutool.crypto;

import java.security.Provider;
import java.security.Security;

/**
 * Provider对象生产工厂类
 *
 * <pre>
 * 1. 调用{@link #createBouncyCastleProvider()} 用于新建一个org.bouncycastle.jce.provider.BouncyCastleProvider对象
 * </pre>
 *
 * @author looly
 * @since 4.2.1
 */
public class ProviderFactory {

	private ProviderFactory() {
	}

	/**
	 * 创建Bouncy Castle 提供者<br>
	 * 如果用户未引入bouncycastle库，则此方法抛出{@link NoClassDefFoundError} 异常
	 *
	 * @return {@link Provider}
	 */
	public static Provider createBouncyCastleProvider() {
		Provider provider = Security.getProvider(org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME);
		if (provider == null) {
			provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
			// issue#2631@Github
			SecureUtil.addProvider(provider);
		}
		return provider;
	}
}
