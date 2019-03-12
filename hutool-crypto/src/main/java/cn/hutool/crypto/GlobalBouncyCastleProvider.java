package cn.hutool.crypto;

import java.security.Provider;

/**
 * 全局单例的 org.bouncycastle.jce.provider.BouncyCastleProvider 对象
 * @author looly
 *
 */
public enum GlobalBouncyCastleProvider {
	INSTANCE;
	
	private Provider provider;
	
	private GlobalBouncyCastleProvider() {
		try {
			this.provider = ProviderFactory.createBouncyCastleProvider();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
	}
	
	/**
	 * 获取{@link Provider}
	 * @return {@link Provider}
	 */
	public Provider getProvider() {
		return this.provider;
	}
}
