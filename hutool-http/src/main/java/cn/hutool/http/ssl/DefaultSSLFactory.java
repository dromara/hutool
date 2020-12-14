package cn.hutool.http.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * 默认的SSLSocketFactory
 *
 * @author Looly
 * @since 5.1.2
 */
public class DefaultSSLFactory extends CustomProtocolsSSLFactory {

	public DefaultSSLFactory() throws KeyManagementException, NoSuchAlgorithmException {
		super();
	}

}