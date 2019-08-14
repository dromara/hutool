package cn.hutool.http.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * SSLSocketFactory构建器
 * @author Looly
 *
 */
public class SSLSocketFactoryBuilder{
	
	/** Supports some version of SSL; may support other versions */
	public static final String SSL = "SSL";
	/** Supports SSL version 2 or later; may support other versions */
	public static final String SSLv2 = "SSLv2";
	/** Supports SSL version 3; may support other versions */
	public static final String SSLv3 = "SSLv3";
	
	/** Supports some version of TLS; may support other versions */
	public static final String TLS = "TLS";
	/** Supports RFC 2246: TLS version 1.0 ; may support other versions */
	public static final String TLSv1 = "TLSv1";
	/** Supports RFC 4346: TLS version 1.1 ; may support other versions */
	public static final String TLSv11 = "TLSv1.1";
	/** Supports RFC 5246: TLS version 1.2 ; may support other versions */
	public static final String TLSv12 = "TLSv1.2";

	private String protocol = TLS;
	private KeyManager[] keyManagers;
	private TrustManager[] trustManagers = {new DefaultTrustManager()};
	private SecureRandom secureRandom  = new SecureRandom();
	
	
	/**
	 * 创建 SSLSocketFactoryBuilder
	 * @return SSLSocketFactoryBuilder
	 */
	public static SSLSocketFactoryBuilder create(){
		return new SSLSocketFactoryBuilder();
	}
	
	/**
	 * 设置协议
	 * @param protocol 协议
	 * @return 自身
	 */
	public SSLSocketFactoryBuilder setProtocol(String protocol){
		if(StrUtil.isNotBlank(protocol)){
			this.protocol = protocol;
		}
		return this;
	}
	
	/**
	 * 设置信任信息
	 * 
	 * @param trustManagers TrustManager列表
	 * @return 自身
	 */
	public SSLSocketFactoryBuilder setTrustManagers(TrustManager... trustManagers) {
		if (ArrayUtil.isNotEmpty(trustManagers)) {
			this.trustManagers = trustManagers;
		}
		return this;
	}
	
	/**
	 * 设置 JSSE key managers
	 * 
	 * @param keyManagers JSSE key managers
	 * @return 自身
	 */
	public SSLSocketFactoryBuilder setKeyManagers(KeyManager... keyManagers) {
		if (ArrayUtil.isNotEmpty(keyManagers)) {
			this.keyManagers = keyManagers;
		}
		return this;
	}
	
	/**
	 * 设置 SecureRandom
	 * @param secureRandom SecureRandom
	 * @return 自己
	 */
	public SSLSocketFactoryBuilder setSecureRandom(SecureRandom secureRandom){
		if(null != secureRandom){
			this.secureRandom = secureRandom;
		}
		return this;
	}
	
	/**
	 * 构建SSLSocketFactory
	 * @return SSLSocketFactory
	 * @throws NoSuchAlgorithmException 无此算法
	 * @throws KeyManagementException Key管理异常
	 */
	public SSLSocketFactory build() throws NoSuchAlgorithmException, KeyManagementException{
		SSLContext sslContext = SSLContext.getInstance(protocol);
		sslContext.init(this.keyManagers, this.trustManagers, this.secureRandom);
		return sslContext.getSocketFactory();
	}
}
