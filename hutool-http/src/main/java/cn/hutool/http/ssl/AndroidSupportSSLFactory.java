package cn.hutool.http.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;

import static cn.hutool.http.ssl.SSLSocketFactoryBuilder.*;

/**
 * 兼容android低版本SSL连接
 * 咱在测试HttpUrlConnection的时候
 * 发现一部分手机无法连接[GithubPage]
 *
 * 最后发现原来是某些SSL协议没有开启
 * @author MikaGuraNTK
 */
public class AndroidSupportSSLFactory extends SSLSocketFactory {

	private static String[] protocols = {SSLv3, TLSv1, TLSv11, TLSv12};
	// Android低版本不重置的话某些SSL访问就会失败

	private SSLSocketFactory base;

	public AndroidSupportSSLFactory() {

		super();
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new X509TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
			base = context.getSocketFactory();
		} catch (Exception ignored) {
		}
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
	public Socket createSocket(Socket socket, InputStream inputStream, boolean b) throws IOException {
		SSLSocket sslSocket = (SSLSocket) base.createSocket(socket,inputStream,b);
		resetProtocols(sslSocket);
		return sslSocket;
	}

	@Override
	public Socket createSocket() throws IOException {
		SSLSocket sslSocket = (SSLSocket) base.createSocket();
		resetProtocols(sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		SSLSocket socket = (SSLSocket) base.createSocket(s, host, port, autoClose);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		SSLSocket socket = (SSLSocket) base.createSocket(host, port);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {

		SSLSocket socket = (SSLSocket) base.createSocket(host, port, localHost, localPort);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {

		SSLSocket socket = (SSLSocket) base.createSocket(host, port);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {

		SSLSocket socket = (SSLSocket) base.createSocket(address, port, localAddress, localPort);
		resetProtocols(socket);
		return socket;
	}

	/**
	 * 重置可用策略
	 */
	private void resetProtocols(SSLSocket socket) {
		socket.setEnabledProtocols(protocols);
	}

}