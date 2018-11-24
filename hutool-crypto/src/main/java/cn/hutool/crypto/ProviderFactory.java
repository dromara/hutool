package cn.hutool.crypto;

import java.security.Provider;

/**
 * Provider对象生产法工厂类
 * 
 * @author looly
 * @since 4.2.1
 */
public class ProviderFactory {

	/**
	 * 创建Bouncy Castle 提供者<br>
	 * 如果用户未引入bouncycastle库，则此方法抛出{@link NoClassDefFoundError} 异常
	 * 
	 * @return {@link Provider}
	 */
	public static Provider createBouncyCastleProvider() {
		return new org.bouncycastle.jce.provider.BouncyCastleProvider();
	}
}
