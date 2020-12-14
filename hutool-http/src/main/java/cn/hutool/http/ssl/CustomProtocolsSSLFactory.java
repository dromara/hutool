package cn.hutool.http.ssl;

import cn.hutool.core.util.ArrayUtil;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * 自定义支持协议类型的SSLSocketFactory
 * 
 * @author looly
 */
public class CustomProtocolsSSLFactory extends SSLSocketFactory {

	private final String[] protocols;
	private final SSLSocketFactory base;

	/**
	 * 构造
	 * 
	 * @param protocols 支持协议列表
	 * @throws KeyManagementException KeyManagementException
	 * @throws NoSuchAlgorithmException NoSuchAlgorithmException
	 */
	public CustomProtocolsSSLFactory(String... protocols) throws KeyManagementException, NoSuchAlgorithmException {
		super();
		this.protocols = protocols;
		this.base = SSLSocketFactoryBuilder.create().build();
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return base.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return base.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket() throws IOException {
		final SSLSocket sslSocket = (SSLSocket) base.createSocket();
		resetProtocols(sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		final SSLSocket socket = (SSLSocket) base.createSocket(s, host, port, autoClose);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		final SSLSocket socket = (SSLSocket) base.createSocket(host, port);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
		final SSLSocket socket = (SSLSocket) base.createSocket(host, port, localHost, localPort);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		final SSLSocket socket = (SSLSocket) base.createSocket(host, port);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
		final SSLSocket socket = (SSLSocket) base.createSocket(address, port, localAddress, localPort);
		resetProtocols(socket);
		return socket;
	}

	/**
	 * 重置可用策略
	 * 
	 * @param socket SSLSocket
	 */
	private void resetProtocols(SSLSocket socket) {
		if(ArrayUtil.isNotEmpty(this.protocols)){
			socket.setEnabledProtocols(this.protocols);
		}
	}

}