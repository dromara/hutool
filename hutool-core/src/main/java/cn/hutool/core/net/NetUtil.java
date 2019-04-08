package cn.hutool.core.net;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import sun.net.util.IPAddressUtil;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * 网络相关工具
 * 
 * @author xiaoleilu
 *
 */
public class NetUtil {

	public final static String LOCAL_IP = "127.0.0.1";

	/** 默认最小端口，1024 */
	public static final int PORT_RANGE_MIN = 1024;
	/** 默认最大端口，65535 */
	public static final int PORT_RANGE_MAX = 0xFFFF;

	/**
	 * 根据long值获取ip v4地址
	 * 
	 * @param longIP IP的long表示形式
	 * @return IP V4 地址
	 */
	public static String longToIpv4(long longIP) {
		final StringBuilder sb = new StringBuilder();
		// 直接右移24位
		sb.append(String.valueOf(longIP >>> 24));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(longIP & 0x000000FF));
		return sb.toString();
	}

	/**
	 * 根据ip地址计算出long型的数据
	 * 
	 * @param strIP IP V4 地址
	 * @return long值
	 */
	public static long ipv4ToLong(String strIP) {
		if (Validator.isIpv4(strIP)) {
			long[] ip = new long[4];
			// 先找到IP地址字符串中.的位置
			int position1 = strIP.indexOf(".");
			int position2 = strIP.indexOf(".", position1 + 1);
			int position3 = strIP.indexOf(".", position2 + 1);
			// 将每个.之间的字符串转换成整型
			ip[0] = Long.parseLong(strIP.substring(0, position1));
			ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
			ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
			ip[3] = Long.parseLong(strIP.substring(position3 + 1));
			return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
		}
		return 0;
	}

	/**
	 * 数字转IP
	 * @param bigInteger
	 * @param isIpV6 是否是IPV6地址
	 * @return
	 */
	public static String bigIntegerToIp(BigInteger bigInteger, boolean isIpV6) {
		return isIpV6 ? bigIntegerToIpv6(bigInteger) : bigIntegerToIpV4(bigInteger);
	}
	/**
	 * 根据BigInteger值获取ip v4地址
	 * @param bigInteger
	 * @return
	 */
	public static String bigIntegerToIpV4(BigInteger bigInteger) {
		long number = bigInteger.longValue();
		return (number >> 24) +
				"." +
				((number & 0x00FFFFFF) >> 16) +
				"." +
				((number & 0x0000FFFF) >> 8) +
				"." +
				(number & 0x000000FF);
	}

	/**
	 * 根据BigInteger值获取ip v6地址
	 * @param number
	 * @return
	 */
	public static String bigIntegerToIpv6(BigInteger number) {
		return bigIntegerToIpv6(number,true);
	}

	/**
	 * 根据BigInteger值获取ip v6地址
	 * @param number
	 * @param isCompress 是否压缩IPv6
	 * @return
	 */
	public static String bigIntegerToIpv6(BigInteger number, boolean isCompress) {
		StringBuilder str = new StringBuilder();
		BigInteger ff = BigInteger.valueOf(0xffff);
		for (int i = 0; i < 8; i++) {
			str.insert(0, number.and(ff).toString(16) + ":");
			number = number.shiftRight(16);
		}
		str = new StringBuilder(str.substring(0, str.length() - 1));
		return isCompress ? PatternPool.IPV6_COMPRESS.matcher(str.toString()).replaceFirst("::") : str.toString();
	}

	/**
	 * 校验ip是否在指定IP范围内
	 *
	 * @param ip  待校验IP
	 * @param beginIp 起始IP
	 * @param endIp 结束IP
	 * @return 是否在起始和结束IP范围内
	 */
	public static boolean isInner(String ip, String beginIp, String endIp) throws UnknownHostException {
		BigInteger ipNum = ipToBigInteger(ip);
		BigInteger beginIpNum = ipToBigInteger(beginIp);
		BigInteger endIpNum = ipToBigInteger(endIp);
		// 如果起始IP小于结束IP
		if (beginIpNum.compareTo(endIpNum) < 0) {
			return ipNum.compareTo(beginIpNum) >= 0 && ipNum.compareTo(endIpNum) <= 0;
		// 如果起始IP大于等于结束IP
		} else {
			return ipNum.compareTo(endIpNum) >= 0 && ipNum.compareTo(beginIpNum) <= 0;
		}

	}

	/**
	 * IP转BigInteger
	 * @param ip IP字符串，支持IPV4和IPv6
	 * @return IP 数值
	 * @throws UnknownHostException
	 */
	public static BigInteger ipToBigInteger(String ip) throws UnknownHostException {
		// 转换为完整形式的ip字符串
		ip = InetAddress.getByName(ip).getHostAddress();
		// ipv4转数字
		if (IPAddressUtil.isIPv4LiteralAddress(ip)) {
			final long[] numbers = StrUtil.splitToLong(ip, ".");
			return BigInteger.valueOf((numbers[0] << 24)
					+ (numbers[1] << 16)
					+ (numbers[2] << 8)
					+ (numbers[3]));
			//ipv6转数字
		} else if (IPAddressUtil.isIPv6LiteralAddress(ip)) {
			final String[] numbers = StrUtil.split(ip, ":");
			BigInteger bigInteger = BigInteger.ZERO;
			for (int i = 0; i < numbers.length; i++) {
				bigInteger = bigInteger.add(
						BigInteger.valueOf(Long.valueOf(numbers[i], 16)).shiftLeft(16 * (numbers.length - 1 - i))
				);
			}
			return bigInteger;
		} else {
			return BigInteger.ZERO;
		}
	}

	/**
	 * 检测本地端口可用性<br>
	 * 来自org.springframework.util.SocketUtils
	 * 
	 * @param port 被检测的端口
	 * @return 是否可用
	 */
	public static boolean isUsableLocalPort(int port) {
		if (false == isValidPort(port)) {
			// 给定的IP未在指定端口范围中
			return false;
		}
		try {
			ServerSocketFactory.getDefault().createServerSocket(port, 1, InetAddress.getByName(LOCAL_IP)).close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否为有效的端口<br>
	 * 此方法并不检查端口是否被占用
	 * 
	 * @param port 端口号
	 * @return 是否有效
	 */
	public static boolean isValidPort(int port) {
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
	public static int getUsableLocalPort(int minPort) {
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
	public static int getUsableLocalPort(int minPort, int maxPort) {
		for (int i = minPort; i <= maxPort; i++) {
			if (isUsableLocalPort(RandomUtil.randomInt(minPort, maxPort + 1))) {
				return i;
			}
		}

		throw new UtilException("Could not find an available port in the range [{}, {}] after {} attempts", minPort, maxPort, maxPort - minPort);
	}

	/**
	 * 获取多个本地可用端口<br>
	 * 来自org.springframework.util.SocketUtils
	 * 
	 * @param minPort 端口最小值（包含）
	 * @param maxPort 端口最大值（包含）
	 * @return 可用的端口
	 * @since 4.5.4
	 */
	public static TreeSet<Integer> getUsableLocalPorts(int numRequested, int minPort, int maxPort) {
		final TreeSet<Integer> availablePorts = new TreeSet<>();
		int attemptCount = 0;
		while ((++attemptCount <= numRequested + 100) && availablePorts.size() < numRequested) {
			availablePorts.add(getUsableLocalPort(minPort, maxPort));
		}

		if (availablePorts.size() != numRequested) {
			throw new UtilException("Could not find {} available  ports in the range [{}, {}]", numRequested, minPort, maxPort);
		}

		return availablePorts;
	}

	/**
	 * 判定是否为内网IP<br>
	 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
	 * 
	 * @param ipAddress IP地址
	 * @return 是否为内网IP
	 */
	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = NetUtil.ipv4ToLong(ipAddress);

		long aBegin = NetUtil.ipv4ToLong("10.0.0.0");
		long aEnd = NetUtil.ipv4ToLong("10.255.255.255");

		long bBegin = NetUtil.ipv4ToLong("172.16.0.0");
		long bEnd = NetUtil.ipv4ToLong("172.31.255.255");

		long cBegin = NetUtil.ipv4ToLong("192.168.0.0");
		long cEnd = NetUtil.ipv4ToLong("192.168.255.255");

		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ipAddress.equals(LOCAL_IP);
		return isInnerIp;
	}

	/**
	 * 相对URL转换为绝对URL
	 * 
	 * @param absoluteBasePath 基准路径，绝对
	 * @param relativePath 相对路径
	 * @return 绝对URL
	 */
	public static String toAbsoluteUrl(String absoluteBasePath, String relativePath) {
		try {
			URL absoluteUrl = new URL(absoluteBasePath);
			return new URL(absoluteUrl, relativePath).toString();
		} catch (Exception e) {
			throw new UtilException(e, "To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath);
		}
	}

	/**
	 * 隐藏掉IP地址的最后一部分为 * 代替
	 * 
	 * @param ip IP地址
	 * @return 隐藏部分后的IP
	 */
	public static String hideIpPart(String ip) {
		return new StringBuffer(ip.length()).append(ip.substring(0, ip.lastIndexOf(".") + 1)).append("*").toString();
	}

	/**
	 * 隐藏掉IP地址的最后一部分为 * 代替
	 * 
	 * @param ip IP地址
	 * @return 隐藏部分后的IP
	 */
	public static String hideIpPart(long ip) {
		return hideIpPart(longToIpv4(ip));
	}

	/**
	 * 构建InetSocketAddress<br>
	 * 当host中包含端口时（用“：”隔开），使用host中的端口，否则使用默认端口<br>
	 * 给定host为空时使用本地host（127.0.0.1）
	 * 
	 * @param host Host
	 * @param defaultPort 默认端口
	 * @return InetSocketAddress
	 */
	public static InetSocketAddress buildInetSocketAddress(String host, int defaultPort) {
		if (StrUtil.isBlank(host)) {
			host = LOCAL_IP;
		}

		String destHost = null;
		int port = 0;
		int index = host.indexOf(":");
		if (index != -1) {
			// host:port形式
			destHost = host.substring(0, index);
			port = Integer.parseInt(host.substring(index + 1));
		} else {
			destHost = host;
			port = defaultPort;
		}

		return new InetSocketAddress(destHost, port);
	}

	/**
	 * 通过域名得到IP
	 * 
	 * @param hostName HOST
	 * @return ip address or hostName if UnknownHostException
	 */
	public static String getIpByHost(String hostName) {
		try {
			return InetAddress.getByName(hostName).getHostAddress();
		} catch (UnknownHostException e) {
			return hostName;
		}
	}

	/**
	 * 获取本机所有网卡
	 * 
	 * @return 所有网卡，异常返回<code>null</code>
	 * @since 3.0.1
	 */
	public static Collection<NetworkInterface> getNetworkInterfaces() {
		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return null;
		}

		return CollectionUtil.addAll(new ArrayList<NetworkInterface>(), networkInterfaces);
	}

	/**
	 * 获得本机的IP地址列表<br>
	 * 返回的IP列表有序，按照系统设备顺序
	 * 
	 * @return IP地址列表 {@link LinkedHashSet}
	 */
	public static LinkedHashSet<String> localIpv4s() {
		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new UtilException(e);
		}

		if (networkInterfaces == null) {
			throw new UtilException("Get network interface error!");
		}

		final LinkedHashSet<String> ipSet = new LinkedHashSet<>();

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if (inetAddress != null && inetAddress instanceof Inet4Address) {
					ipSet.add(inetAddress.getHostAddress());
				}
			}
		}

		return ipSet;
	}

	/**
	 * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
	 * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
	 * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
	 * 
	 * 参考：http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
	 * 
	 * @return 本机网卡IP地址，获取失败返回<code>null</code>
	 * @since 3.0.7
	 */
	public static String getLocalhostStr() {
		InetAddress localhost = getLocalhost();
		if (null != localhost) {
			return localhost.getHostAddress();
		}
		return null;
	}

	/**
	 * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
	 * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
	 * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
	 * 
	 * 参考：http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
	 * 
	 * @return 本机网卡IP地址，获取失败返回<code>null</code>
	 * @since 3.0.1
	 */
	public static InetAddress getLocalhost() {
		InetAddress candidateAddress = null;
		NetworkInterface iface;
		InetAddress inetAddr;
		try {
			for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				iface = ifaces.nextElement();
				for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					inetAddr = inetAddrs.nextElement();
					if (false == inetAddr.isLoopbackAddress()) {
						if (inetAddr.isSiteLocalAddress()) {
							return inetAddr;
						} else if (null == candidateAddress) {
							// 非site-local地址做为候选地址返回
							candidateAddress = inetAddr;
						}
					}
				}
			}
		} catch (SocketException e) {
			// ignore socket exception, and return null;
		}

		if (null == candidateAddress) {
			try {
				candidateAddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// ignore
			}
		}

		return candidateAddress;
	}

	/**
	 * 获得本机MAC地址
	 * 
	 * @return 本机MAC地址
	 */
	public static String getLocalMacAddress() {
		return getMacAddress(getLocalhost());
	}

	/**
	 * 获得指定地址信息中的MAC地址，使用分隔符“-”
	 * 
	 * @param inetAddress {@link InetAddress}
	 * @return MAC地址，用-分隔
	 */
	public static String getMacAddress(InetAddress inetAddress) {
		return getMacAddress(inetAddress, "-");
	}

	/**
	 * 获得指定地址信息中的MAC地址
	 * 
	 * @param inetAddress {@link InetAddress}
	 * @param separator 分隔符，推荐使用“-”或者“:”
	 * @return MAC地址，用-分隔
	 */
	public static String getMacAddress(InetAddress inetAddress, String separator) {
		if (null == inetAddress) {
			return null;
		}

		byte[] mac;
		try {
			mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
		} catch (SocketException e) {
			throw new UtilException(e);
		}
		if (null != mac) {
			final StringBuilder sb = new StringBuilder();
			String s;
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append(separator);
				}
				// 字节转换为整数
				s = Integer.toHexString(mac[i] & 0xFF);
				sb.append(s.length() == 1 ? 0 + s : s);
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * 创建 {@link InetSocketAddress}
	 * 
	 * @param host 域名或IP地址，空表示任意地址
	 * @param port 端口，0表示系统分配临时端口
	 * @return {@link InetSocketAddress}
	 * @since 3.3.0
	 */
	public static InetSocketAddress createAddress(String host, int port) {
		if (StrUtil.isBlank(host)) {
			return new InetSocketAddress(port);
		}
		return new InetSocketAddress(host, port);
	}

	/**
	 * 
	 * 简易的使用Socket发送数据
	 * 
	 * @param host Server主机
	 * @param port Server端口
	 * @param isBlock 是否阻塞方式
	 * @param data 需要发送的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.3.0
	 */
	public static void netCat(String host, int port, boolean isBlock, ByteBuffer data) throws IORuntimeException {
		try (SocketChannel channel = SocketChannel.open(createAddress(host, port))) {
			channel.configureBlocking(isBlock);
			channel.write(data);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 
	 * 使用普通Socket发送数据
	 * 
	 * @param host Server主机
	 * @param port Server端口
	 * @param data 数据
	 * @throws IOException IO异常
	 * @since 3.3.0
	 */
	public static void netCat(String host, int port, byte[] data) throws IORuntimeException {
		OutputStream out = null;
		try (Socket socket = new Socket(host, port)) {
			out = socket.getOutputStream();
			out.write(data);
			out.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(out);
		}
	}

	/**
	 * 是否在CIDR规则配置范围内<br>
	 * 方法来自：【成都】小邓
	 * 
	 * @param ip 需要验证的IP
	 * @param cidr CIDR规则
	 * @return 是否在范围内
	 * @since 4.0.6
	 */
	public static boolean isInRange(String ip, String cidr) {
		String[] ips = StrUtil.splitToArray(ip, '.');
		int ipAddr = (Integer.parseInt(ips[0]) << 24) | (Integer.parseInt(ips[1]) << 16) | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
		int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
		int mask = 0xFFFFFFFF << (32 - type);
		String cidrIp = cidr.replaceAll("/.*", "");
		String[] cidrIps = cidrIp.split("\\.");
		int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24) | (Integer.parseInt(cidrIps[1]) << 16) | (Integer.parseInt(cidrIps[2]) << 8) | Integer.parseInt(cidrIps[3]);
		return (ipAddr & mask) == (cidrIpAddr & mask);
	}

	/**
	 * Unicode域名转puny code
	 * 
	 * @param unicode Unicode域名
	 * @return puny code
	 * @since 4.1.22
	 */
	public static String idnToASCII(String unicode) {
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
		if (ip != null && ip.indexOf(",") > 0) {
			final String[] ips = ip.trim().split(",");
			for (String subIp : ips) {
				if (false == isUnknow(subIp)) {
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
	 * @since 4.4.1
	 */
	public static boolean isUnknow(String checkString) {
		return StrUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
	}

	// ----------------------------------------------------------------------------------------- Private method start
	/**
	 * 指定IP的long是否在指定范围内
	 * 
	 * @param userIp 用户IP
	 * @param begin 开始IP
	 * @param end 结束IP
	 * @return 是否在范围内
	 */
	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}
	// ----------------------------------------------------------------------------------------- Private method end
}
