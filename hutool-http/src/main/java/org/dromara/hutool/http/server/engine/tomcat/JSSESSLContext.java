/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.server.engine.tomcat;

import org.apache.tomcat.util.net.SSLContext;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tomcat SSLContext实现
 *
 * @author looly
 * @since 6.0.0
 */
public class JSSESSLContext implements SSLContext {

	private final javax.net.ssl.SSLContext context;
	private KeyManager[] kms;
	private TrustManager[] tms;

	/**
	 * 构造
	 *
	 * @param context SSLContext
	 */
	public JSSESSLContext(final javax.net.ssl.SSLContext context) {
		this.context = context;
	}

	@Override
	public void init(final KeyManager[] kms, final TrustManager[] tms, final SecureRandom sr)
		throws KeyManagementException {
		this.kms = kms;
		this.tms = tms;
		context.init(kms, tms, sr);
	}

	@Override
	public void destroy() {
	}

	@Override
	public SSLSessionContext getServerSessionContext() {
		return context.getServerSessionContext();
	}

	@Override
	public SSLEngine createSSLEngine() {
		return context.createSSLEngine();
	}

	@Override
	public SSLServerSocketFactory getServerSocketFactory() {
		return context.getServerSocketFactory();
	}

	@Override
	public SSLParameters getSupportedSSLParameters() {
		return context.getSupportedSSLParameters();
	}

	@Override
	public X509Certificate[] getCertificateChain(final String alias) {
		X509Certificate[] result = null;
		if (kms != null) {
			for (int i = 0; i < kms.length && result == null; i++) {
				if (kms[i] instanceof X509KeyManager) {
					result = ((X509KeyManager) kms[i]).getCertificateChain(alias);
				}
			}
		}
		return result;
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		final Set<X509Certificate> certs = new HashSet<>();
		if (tms != null) {
			for (final TrustManager tm : tms) {
				if (tm instanceof X509TrustManager) {
					final X509Certificate[] accepted = ((X509TrustManager) tm).getAcceptedIssuers();
					certs.addAll(Arrays.asList(accepted));
				}
			}
		}
		return certs.toArray(new X509Certificate[0]);
	}
}
