package cn.hutool.crypto.digest.mac;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.digests.SM3Digest;

import cn.hutool.crypto.digest.HmacAlgorithm;

/**
 * {@link MacEngine} 实现工厂类
 * 
 * @author Looly
 *@since 4.5.13
 */
public class MacEngineFactory {
	
	/**
	 * 根据给定算法和密钥生成对应的{@link MacEngine}
	 * @param algorithm 算法，见{@link HmacAlgorithm}
	 * @param key 密钥
	 * @return {@link MacEngine}
	 */
	public static MacEngine createEngine(String algorithm, SecretKey key) {
		if(algorithm.equalsIgnoreCase(HmacAlgorithm.HmacSM3.getValue())) {
			// HmacSM3算法是BC库实现的
			return new BCHMacEngine(new SM3Digest(), key.getEncoded());
		}
		return new DefaultHMacEngine(algorithm, key);
	}
}
