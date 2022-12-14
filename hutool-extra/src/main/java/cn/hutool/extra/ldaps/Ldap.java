package cn.hutool.extra.ldaps;

import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.Properties;

import static cn.hutool.extra.ldaps.LdapConstant.*;

public class Ldap {

	/**
	 * default don't verify host names
	 */
	private static HostnameVerifier hostnameVerifier = (urlHostName, session) -> true;

	private TrustManager[] trustManagers = new TrustManager[]{new DefaultTrustManager()};

	private LdapConfig ldapConfig = new LdapConfig();

	public Ldap(String host) {
		this(host, DEFAULT_LDAP_PORT, null, false);
	}

	public Ldap(String host, int port) {
		this(host, port, null, false);
	}

	public Ldap(String host, boolean isLdaps) {
		this(host, isLdaps ? DEFAULT_LDAPS_PORT : DEFAULT_LDAP_PORT, null, false);
	}

	public Ldap(String host, int port, String domain, boolean isLdaps) {
		String protocol = isLdaps ? LDAPS_PREFIX : LDAP_PREFIX;
		LdapConfig config = new LdapConfig(host, port, protocol, domain, isLdaps);
		this.ldapConfig = config;
	}

	public Ldap(LdapConfig ldapConfig) {
		this.ldapConfig = ldapConfig;
	}

	public void setTrustManagers(TrustManager[] trustManagers) {
		this.trustManagers = trustManagers;
	}

	public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
	}

	public boolean init(String user, String password) {
		this.ldapConfig.setUser(user);
		this.ldapConfig.setPassword(password);
		return init();
	}

	/**
	 * 去连接ldap(s)服务校验账号密码是否正确
	 *
	 * @return true表示成功，false表示失败
	 */
	public boolean init() {

		DirContext ctx = null;

		boolean success = false;
		try {
			ctx = connectToServer();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != ctx) {
				try {
					ctx.close();
				} catch (Exception ex) {
					// ignore
				}
			}
		}
		return success;
	}

	private DirContext connectToServer() throws Exception {
		Properties env = new Properties();
		env.put(JAVA_NAMING_FACTORY_INITIAL, COM_SUN_JNDI_LDAP_LDAPCTXFACTORY);
		env.put(JAVA_NAMING_SECURITY_AUTHENTICATION, "simple");
		env.put(JAVA_NAMING_SECURITY_PRINCIPAL, ldapConfig.getUser());
		env.put(JAVA_NAMING_SECURITY_CREDENTIALS, ldapConfig.getPassword());
		env.put(JAVA_NAMING_PROVIDER_URL, getUrl());
		if (ldapConfig.getLdaps()) {
			System.setProperty(javax_net_ssl_truststore, ldapConfig.getKeystore());
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			trustAllHttpsCertificates();
			env.put(JAVA_NAMING_AUTHORITATIVE, Boolean.FALSE.toString());
			env.put(JAVA_NAMING_SECURITY_PROTOCOL, SSL);
		}
		return new InitialDirContext(env);
	}

	private String getUrl() {
		return ldapConfig.getProtocol() + ldapConfig.getHost() + ":" + ldapConfig.getPort();
	}

	class DefaultTrustManager implements TrustManager, X509TrustManager {
		DefaultTrustManager() {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}
	}

	private void trustAllHttpsCertificates() throws Exception {
		SSLContext sc = SSLContext.getInstance(SSL.toUpperCase());
		sc.init(null, trustManagers, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
}
