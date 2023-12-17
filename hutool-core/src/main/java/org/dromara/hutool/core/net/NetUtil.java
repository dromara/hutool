/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.net;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.iter.EnumerationIter;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.util.JNDIUtil;
import org.dromara.hutool.core.util.RandomUtil;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.function.Predicate;

/**
 * 网络相关工具
 *
 * @author Looly
 */
public class NetUtil {

	/**
	 * 本地IP
	 */
	public final static String LOCAL_IP = Ipv4Util.LOCAL_IP;

	/**
	 * 默认最小端口，1024
	 */
	public static final int PORT_RANGE_MIN = 1024;
	/**
	 * 默认最大端口，65535
	 */
	public static final int PORT_RANGE_MAX = 0xFFFF;

	/**
	 * 根据long值获取ip v4地址
	 *
	 * @param longIP IP的long表示形式
	 * @return IP V4 地址
	 * @see Ipv4Util#longToIpv4(long)
	 */
	public static String longToIpv4(final long longIP) {
		return Ipv4Util.longToIpv4(longIP);
	}

	/**
	 * 根据ip地址计算出long型的数据
	 *
	 * @param strIP IP V4 地址
	 * @return long值
	 * @see Ipv4Util#ipv4ToLong(String)
	 */
	public static long ipv4ToLong(final String strIP) {
		return Ipv4Util.ipv4ToLong(strIP);
	}

	/**
	 * 检测本地端口可用性<br>
	 * 来自org.springframework.util.SocketUtils
	 *
	 * @param port 被检测的端口
	 * @return 是否可用
	 */
	public static boolean isUsableLocalPort(final int port) {
		if (!isValidPort(port)) {
			// 给定的IP未在指定端口范围中
			return false;
		}

		// issue#765@Github, 某些绑定非127.0.0.1的端口无法被检测到
		try (final ServerSocket ss = new ServerSocket(port)) {
			ss.setReuseAddress(true);
		} catch (final IOException ignored) {
			return false;
		}

		try (final DatagramSocket ds = new DatagramSocket(port)) {
			ds.setReuseAddress(true);
		} catch (final IOException ignored) {
			return false;
		}

		return true;
	}

	/**
	 * 是否为有效的端口<br>
	 * 此方法并不检查端口是否被占用
	 *
	 * @param port 端口号
	 * @return 是否有效
	 */
	public static boolean isValidPort(final int port) {
		// 有效端口是0～65535
		return port >= 0 && port <= PORT_RANGE_MAX;
	}

	/**
	 * 查找1024~65535范围内的可用端口<br>
	 * 此方法只检测给定范围内的随机一个端口，检测65535-1024次<br>
	 * 来自org.springframework.util.SocketUtils
	 *
	 * @return 可用的端口
	 * @since 4.5.4
	 */
	public static int getUsableLocalPort() {
		return getUsableLocalPort(PORT_RANGE_MIN);
	}

	/**
	 * 查找指定范围内的可用端口，最大值为65535<br>
	 * 此方法只检测给定范围内的随机一个端口，检测65535-minPort次<br>
	 * 来自org.springframework.util.SocketUtils
	 *
	 * @param minPort 端口最小值（包含）
	 * @return 可用的端口
	 * @since 4.5.4
	 */
	public static int getUsableLocalPort(final int minPort) {
		return getUsableLocalPort(minPort, PORT_RANGE_MAX);
	}

	/**
	 * 查找指定范围内的可用端口<br>
	 * 此方法只检测给定范围内的随机一个端口，检测maxPort-minPort次<br>
	 * 来自org.springframework.util.SocketUtils
	 *
	 * @param minPort 端口最小值（包含）
	 * @param maxPort 端口最大值（包含）
	 * @return 可用的端口
	 * @since 4.5.4
	 */
	public static int getUsableLocalPort(final int minPort, final int maxPort) {
		final int maxPortExclude = maxPort + 1;
		int randomPort;
		for (int i = minPort; i < maxPortExclude; i++) {
			randomPort = RandomUtil.randomInt(minPort, maxPortExclude);
			if (isUsableLocalPort(randomPort)) {
				return randomPort;
			}
		}

		throw new HutoolException("Could not find an available port in the range [{}, {}] after {} attempts", minPort, maxPort, maxPort - minPort);
	}

	/**
	 * 获取多个本地可用端口<br>
	 * 来自org.springframework.util.SocketUtils
	 *
	 * @param numRequested 尝试次数
	 * @param minPort      端口最小值（包含）
	 * @param maxPort      端口最大值（包含）
	 * @return 可用的端口
	 * @since 4.5.4
	 */
	public static TreeSet<Integer> getUsableLocalPorts(final int numRequested, final int minPort, final int maxPort) {
		final TreeSet<Integer> availablePorts = new TreeSet<>();
		int attemptCount = 0;
		while ((++attemptCount <= numRequested + 100) && availablePorts.size() < numRequested) {
			availablePorts.add(getUsableLocalPort(minPort, maxPort));
		}

		if (availablePorts.size() != numRequested) {
			throw new HutoolException("Could not find {} available  ports in the range [{}, {}]", numRequested, minPort, maxPort);
		}

		return availablePorts;
	}

	/**
	 * 判定是否为内网IPv4<br>
	 * 私有IP：
	 * <pre>
	 * A类 10.0.0.0-10.255.255.255
	 * B类 172.16.0.0-172.31.255.255
	 * C类 192.168.0.0-192.168.255.255
	 * </pre>
	 * 当然，还有127这个网段是环回地址
	 *
	 * @param ipAddress IP地址
	 * @return 是否为内网IP
	 * @see Ipv4Util#isInnerIP(String)
	 */
	public static boolean isInnerIP(final String ipAddress) {
		return Ipv4Util.isInnerIP(ipAddress);
	}

	/**
	 * 相对URL转换为绝对URL
	 *
	 * @param absoluteBasePath 基准路径，绝对
	 * @param relativePath     相对路径
	 * @return 绝对URL
	 */
	public static String toAbsoluteUrl(final String absoluteBasePath, final String relativePath) {
		try {
			final URL absoluteUrl = new URL(absoluteBasePath);
			return new URL(absoluteUrl, relativePath).toString();
		} catch (final Exception e) {
			throw new HutoolException(e, "To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath);
		}
	}

	/**
	 * 隐藏掉IP地址的最后一部分为 * 代替
	 *
	 * @param ip IP地址
	 * @return 隐藏部分后的IP
	 */
	public static String hideIpPart(final String ip) {
		return StrUtil.builder(ip.length()).append(ip, 0, ip.lastIndexOf(".") + 1).append("*").toString();
	}

	/**
	 * 隐藏掉IP地址的最后一部分为 * 代替
	 *
	 * @param ip IP地址
	 * @return 隐藏部分后的IP
	 */
	public static String hideIpPart(final long ip) {
		return hideIpPart(longToIpv4(ip));
	}

	/**
	 * 通过域名得到IP
	 *
	 * @param hostName HOST
	 * @return ip address or hostName if UnknownHostException
	 */
	public static String getIpByHost(final String hostName) {
		try {
			return InetAddress.getByName(hostName).getHostAddress();
		} catch (final UnknownHostException e) {
			return hostName;
		}
	}

	/**
	 * 获取指定名称的网卡信息
	 *
	 * @param name 网络接口名，例如Linux下默认是eth0
	 * @return 网卡，未找到返回{@code null}
	 * @since 5.0.7
	 */
	public static NetworkInterface getNetworkInterface(final String name) {
		final Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (final SocketException e) {
			return null;
		}

		NetworkInterface netInterface;
		while (networkInterfaces.hasMoreElements()) {
			netInterface = networkInterfaces.nextElement();
			if (null != netInterface && name.equals(netInterface.getName())) {
				return netInterface;
			}
		}

		return null;
	}

	/**
	 * 获取本机所有网卡
	 *
	 * @return 所有网卡，异常返回{@code null}
	 * @since 3.0.1
	 */
	public static Collection<NetworkInterface> getNetworkInterfaces() {
		final Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (final SocketException e) {
			return null;
		}

		return CollUtil.addAll(new ArrayList<>(), networkInterfaces);
	}

	/**
	 * 获得本机的IPv4地址列表<br>
	 * 返回的IP列表有序，按照系统设备顺序
	 *
	 * @return IP地址列表 {@link LinkedHashSet}
	 */
	public static LinkedHashSet<String> localIpv4s() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(t -> t instanceof Inet4Address);

		return toIpList(localAddressList);
	}

	/**
	 * 地址列表转换为IP地址列表
	 *
	 * @param addressList 地址{@link Inet4Address} 列表
	 * @return IP地址字符串列表
	 * @since 4.5.17
	 */
	public static LinkedHashSet<String> toIpList(final Set<InetAddress> addressList) {
		final LinkedHashSet<String> ipSet = new LinkedHashSet<>();
		for (final InetAddress address : addressList) {
			ipSet.add(address.getHostAddress());
		}

		return ipSet;
	}

	/**
	 * 获得本机的IP地址列表（包括Ipv4和Ipv6）<br>
	 * 返回的IP列表有序，按照系统设备顺序
	 *
	 * @return IP地址列表 {@link LinkedHashSet}
	 */
	public static LinkedHashSet<String> localIps() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(null);
		return toIpList(localAddressList);
	}

	/**
	 * 获取所有满足过滤条件的本地IP地址对象
	 *
	 * @param addressPredicate 过滤器，{@link Predicate#test(Object)}为{@code true}保留，null表示不过滤，获取所有地址
	 * @return 过滤后的地址对象列表
	 * @since 4.5.17
	 */
	public static LinkedHashSet<InetAddress> localAddressList(final Predicate<InetAddress> addressPredicate) {
		return localAddressList(null, addressPredicate);
	}

	/**
	 * 获取所有满足过滤条件的本地IP地址对象
	 *
	 * @param networkInterfaceFilter 过滤器，null表示不过滤，获取所有网卡
	 * @param addressPredicate       过滤器，{@link Predicate#test(Object)}为{@code true}保留，null表示不过滤，获取所有地址
	 * @return 过滤后的地址对象列表
	 * @since 4.5.17
	 */
	public static LinkedHashSet<InetAddress> localAddressList(final Predicate<NetworkInterface> networkInterfaceFilter, final Predicate<InetAddress> addressPredicate) {
		final Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (final SocketException e) {
			throw new HutoolException(e);
		}

		Assert.notNull(networkInterfaces, ()-> new HutoolException("Get network interface error!"));

		final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			if (networkInterfaceFilter != null && !networkInterfaceFilter.test(networkInterface)) {
				continue;
			}
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if (inetAddress != null && (null == addressPredicate || addressPredicate.test(inetAddress))) {
					ipSet.add(inetAddress);
				}
			}
		}

		return ipSet;
	}

	/**
	 * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
	 * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
	 * 此方法不会抛出异常，获取失败将返回{@code null}<br>
	 * <p>
	 * 参考：<a href="http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java">
	 * http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java</a>
	 *
	 * @return 本机网卡IP地址，获取失败返回{@code null}
	 * @since 3.0.7
	 */
	public static String getLocalhostStrV4() {
		final InetAddress localhost = Ipv4Util.getLocalhost();
		if (null != localhost) {
			return localhost.getHostAddress();
		}
		return null;
	}

	/**
	 * 获取本机网卡IPv4地址，规则如下：
	 *
	 * <ul>
	 *     <li>必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv4地址</li>
	 *     <li>多网卡则返回第一个满足条件的地址</li>
	 *     <li>如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址</li>
	 * </ul>
	 *
	 * <p>
	 * 此方法不会抛出异常，获取失败将返回{@code null}<br>
	 * <p>
	 * 见：https://github.com/dromara/hutool/issues/428
	 *
	 * @return 本机网卡IP地址，获取失败返回{@code null}
	 */
	public static InetAddress getLocalhostV4() {
		return Ipv4Util.getLocalhost();
	}

	/**
	 * 获得本机MAC地址，默认使用获取到的IPv4本地地址对应网卡
	 *
	 * @return 本机MAC地址
	 */
	public static String getLocalMacAddressV4() {
		return Ipv4Util.getLocalMacAddress();
	}

	/**
	 * 创建 {@link InetSocketAddress}
	 *
	 * @param host 域名或IP地址，空表示任意地址
	 * @param port 端口，0表示系统分配临时端口
	 * @return {@link InetSocketAddress}
	 * @since 3.3.0
	 */
	public static InetSocketAddress createAddress(final String host, final int port) {
		if (StrUtil.isBlank(host)) {
			return new InetSocketAddress(port);
		}
		return new InetSocketAddress(host, port);
	}

	/**
	 * 简易的使用Socket发送数据
	 *
	 * @param host    Server主机
	 * @param port    Server端口
	 * @param isBlock 是否阻塞方式
	 * @param data    需要发送的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.3.0
	 */
	public static void netCat(final String host, final int port, final boolean isBlock, final ByteBuffer data) throws IORuntimeException {
		try (final SocketChannel channel = SocketChannel.open(createAddress(host, port))) {
			channel.configureBlocking(isBlock);
			channel.write(data);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 使用普通Socket发送数据
	 *
	 * @param host Server主机
	 * @param port Server端口
	 * @param data 数据
	 * @throws IORuntimeException IO异常
	 * @since 3.3.0
	 */
	public static void netCat(final String host, final int port, final byte[] data) throws IORuntimeException {
		OutputStream out = null;
		try (final Socket socket = new Socket(host, port)) {
			out = socket.getOutputStream();
			out.write(data);
			out.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(out);
		}
	}

	/**
	 * 是否在CIDR规则配置范围内<br>
	 * 方法来自：【成都】小邓
	 *
	 * @param ip   需要验证的IP
	 * @param cidr CIDR规则
	 * @return 是否在范围内
	 * @since 4.0.6
	 */
	public static boolean isInRange(final String ip, final String cidr) {
		final int maskSplitMarkIndex = cidr.lastIndexOf(Ipv4Util.IP_MASK_SPLIT_MARK);
		if (maskSplitMarkIndex < 0) {
			throw new IllegalArgumentException("Invalid cidr: " + cidr);
		}

		final long mask = (-1L << 32 - Integer.parseInt(cidr.substring(maskSplitMarkIndex + 1)));
		final long cidrIpAddr = ipv4ToLong(cidr.substring(0, maskSplitMarkIndex));

		return (ipv4ToLong(ip) & mask) == (cidrIpAddr & mask);
	}

	/**
	 * Unicode域名转puny code
	 *
	 * @param unicode Unicode域名
	 * @return puny code
	 * @since 4.1.22
	 */
	public static String idnToASCII(final String unicode) {
		return IDN.toASCII(unicode);
	}

	/**
	 * 从多级反向代理中获得第一个非unknown IP地址
	 *
	 * @param ip 获得的IP地址
	 * @return 第一个非unknown IP地址
	 * @since 4.4.1
	 */
	public static String getMultistageReverseProxyIp(String ip) {
		// 多级反向代理检测
		if (ip != null && StrUtil.indexOf(ip, CharUtil.COMMA) > 0) {
			final List<String> ips = SplitUtil.splitTrim(ip, StrUtil.COMMA);
			for (final String subIp : ips) {
				if (!isUnknown(subIp)) {
					ip = subIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br>
	 *
	 * @param checkString 被检测的字符串
	 * @return 是否未知
	 * @since 5.2.6
	 */
	public static boolean isUnknown(final String checkString) {
		return StrUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
	}

	/**
	 * 检测IP地址是否能ping通
	 *
	 * @param ip IP地址
	 * @return 返回是否ping通
	 */
	public static boolean ping(final String ip) {
		return ping(ip, 200);
	}

	/**
	 * 检测IP地址是否能ping通
	 *
	 * @param ip      IP地址
	 * @param timeout 检测超时（毫秒）
	 * @return 是否ping通
	 */
	public static boolean ping(final String ip, final int timeout) {
		try {
			return InetAddress.getByName(ip).isReachable(timeout); // 当返回值是true时，说明host是可用的，false则不可。
		} catch (final Exception ex) {
			return false;
		}
	}

	/**
	 * 解析Cookie信息
	 *
	 * @param cookieStr Cookie字符串
	 * @return cookie字符串
	 * @since 5.2.6
	 */
	public static List<HttpCookie> parseCookies(final String cookieStr) {
		if (StrUtil.isBlank(cookieStr)) {
			return Collections.emptyList();
		}
		return HttpCookie.parse(cookieStr);
	}

	/**
	 * 检查远程端口是否开启
	 *
	 * @param address 远程地址
	 * @param timeout 检测超时
	 * @return 远程端口是否开启
	 * @since 5.3.2
	 */
	public static boolean isOpen(final InetSocketAddress address, final int timeout) {
		try (final Socket sc = new Socket()) {
			sc.connect(address, timeout);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * 设置全局验证
	 *
	 * @param user 用户名
	 * @param pass 密码，考虑安全，此处不使用String
	 * @since 5.7.2
	 */
	public static void setGlobalAuthenticator(final String user, final char[] pass) {
		setGlobalAuthenticator(new UserPassAuthenticator(user, pass));
	}

	/**
	 * 设置全局验证
	 *
	 * @param authenticator 验证器
	 * @since 5.7.2
	 */
	public static void setGlobalAuthenticator(final Authenticator authenticator) {
		Authenticator.setDefault(authenticator);
	}

	/**
	 * 获取DNS信息，如TXT信息：<br>
	 * <pre class="code">
	 *     NetUtil.attrNames("hutool.cn", "TXT")
	 * </pre>
	 *
	 * @param hostName  主机域名
	 * @param attrNames 属性
	 * @return DNS信息
	 * @since 5.7.7
	 */
	public static List<String> getDnsInfo(final String hostName, final String... attrNames) {
		final String uri = StrUtil.addPrefixIfNot(hostName, "dns:");
		final Attributes attributes = JNDIUtil.getAttributes(uri, attrNames);

		final List<String> infos = new ArrayList<>();
		for (final Attribute attribute : new EnumerationIter<>(attributes.getAll())) {
			try {
				infos.add((String) attribute.get());
			} catch (final NamingException ignore) {
				//ignore
			}
		}
		return infos;
	}

	/**
	 * 获取地址名称，如果无名称返回地址<br>
	 * 如果提供的地址为{@code null}返回{@code null}
	 *
	 * @param address {@link InetAddress}，提供{@code null}返回{@code null}
	 * @return 地址名称或地址
	 */
	public static String getAddressName(final InetAddress address) {
		if (null == address) {
			return null;
		}
		String name = address.getHostName();
		if (StrUtil.isEmpty(name)) {
			name = address.getHostAddress();
		}
		return name;
	}
}
