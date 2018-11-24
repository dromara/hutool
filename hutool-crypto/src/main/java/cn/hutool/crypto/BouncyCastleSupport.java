package cn.hutool.crypto;

import java.security.Provider;

/**
 * 对于Bouncy Castle做支持<br>
 * 如果用户引入此库，将支持Bouncy Castle中所有算法
 * 
 * @author looly
 * @since 4.2.1
 */
public abstract class BouncyCastleSupport {
	static {
		Provider provider = null;
		try {
			provider = ProviderFactory.createBouncyCastleProvider();
		} catch (NoClassDefFoundError e) {
		}
		if (null != provider) {
			SecureUtil.addProvider(provider);
		}
	}
}
