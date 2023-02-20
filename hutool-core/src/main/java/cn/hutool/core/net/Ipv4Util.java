package cn.hutool.core.net;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.regex.PatternPool;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * IPV4地址工具类
 *
 * <p>pr自：https://gitee.com/loolly/hutool/pulls/161</p>
 *
 * <p>名词解释：
 * <ul>
 *     <li>ip字符串：点分十进制，形如：xxx.xxx.xxx.xxx</li>
 *     <li>ip的Long类型：有效位32位，每8位可以转为一个十进制数，例如：0xC0A802FA， 转为点分十进制是：192.168.2.250</li>
 *     <li>掩码地址：点分十进制，例如：255.255.255.0</li>
 *     <li>掩码位：int类型，例如 24, 它代表的掩码地址为：255.255.255.0；掩码位和掩码地址的相互转换，请使用 {@link MaskBit}</li>
 *     <li>CIDR：无类域间路由，形如：xxx.xxx.xxx.xxx/掩码位</li>
 *     <li>全量地址：区间内所有ip地址，包含区间两端</li>
 *     <li>可用地址：区间内所有ip地址，但是不包含区间两端</li>
 * </ul>
 *
 * @author ZhuKun
 * @author emptypoint
 * @author aoshiguchen
 * @since 5.4.1
 */
public class Ipv4Util implements Ipv4Pool {

	/**
	 * 根据 ip地址 和 掩码地址 获得 CIDR格式字符串
	 *
	 * @param ip   IP地址，点分十进制，如：xxx.xxx.xxx.xxx
	 * @param mask 掩码地址，点分十进制，如：255.255.255.0
	 * @return 返回 {@literal xxx.xxx.xxx.xxx/掩码位} 的格式
	 */
	public static String formatIpBlock(final String ip, final String mask) {
		return ip + IP_MASK_SPLIT_MARK + getMaskBitByMask(mask);
	}

	/**
	 * 智能获取指定区间内的所有IP地址
	 *
	 * @param ipRange IP区间，支持 {@literal X.X.X.X-X.X.X.X} 或 {@literal X.X.X.X/X}
	 * @param isAll   true:全量地址，false:可用地址；该参数仅在ipRange为X.X.X.X/X时才生效
	 * @return 区间内的所有IP地址，点分十进制格式
	 */
	public static List<String> list(final String ipRange, final boolean isAll) {
		if (ipRange.contains(IP_SPLIT_MARK)) {
			// X.X.X.X-X.X.X.X
			final String[] range = StrUtil.splitToArray(ipRange, IP_SPLIT_MARK);
			return list(range[0], range[1]);
		} else if (ipRange.contains(IP_MASK_SPLIT_MARK)) {
			// X.X.X.X/X
			final String[] param = StrUtil.splitToArray(ipRange, IP_MASK_SPLIT_MARK);
			return list(param[0], Integer.parseInt(param[1]), isAll);
		} else {
			return ListUtil.of(ipRange);
		}
	}

	/**
	 * 根据 IP地址 和 掩码位数 获取 子网所有ip地址
	 *
	 * @param ip      IP地址，点分十进制
	 * @param maskBit 掩码位，例如24、32
	 * @param isAll   true:全量地址，false:可用地址
	 * @return 子网所有ip地址
	 */
	public static List<String> list(final String ip, final int maskBit, final boolean isAll) {
		assertMaskBitValid(maskBit);
		// 避免后续的计算异常
		if (countByMaskBit(maskBit, isAll) == 0) {
			return new ArrayList<>(0);
		}

		if (maskBit == IPV4_MASK_BIT_MAX) {
			final List<String> list = new ArrayList<>(isAll ? 1 : 0);
			if (isAll) {
				list.add(ip);
			}
			return list;
		}

		String startIp = getBeginIpStr(ip, maskBit);
		String endIp = getEndIpStr(ip, maskBit);
		if (isAll) {
			return list(startIp, endIp);
		}

		// 可用地址，排除开始和结束的地址
		int lastDotIndex = startIp.lastIndexOf(CharUtil.DOT) + 1;
		startIp = StrUtil.subPre(startIp, lastDotIndex) +
				(Integer.parseInt(Objects.requireNonNull(StrUtil.subSuf(startIp, lastDotIndex))) + 1);
		lastDotIndex = endIp.lastIndexOf(CharUtil.DOT) + 1;
		endIp = StrUtil.subPre(endIp, lastDotIndex) +
				(Integer.parseInt(Objects.requireNonNull(StrUtil.subSuf(endIp, lastDotIndex))) - 1);
		return list(startIp, endIp);
	}

	/**
	 * 	获得 指定区间内 所有ip地址
	 *
	 * @param ipFrom 开始IP，包含，点分十进制
	 * @param ipTo   结束IP，包含，点分十进制
	 * @return 区间内所有ip地址
	 */
	public static List<String> list(final String ipFrom, final String ipTo) {
		return list(ipv4ToLong(ipFrom), ipv4ToLong(ipTo));
	}

	/**
	 * 得到指定区间内的所有IP地址
	 *
	 * @param ipFrom 开始IP, 包含
	 * @param ipTo   结束IP, 包含
	 * @return 区间内所有ip地址，点分十进制表示
	 */
	public static List<String> list(final long ipFrom, final long ipTo) {
		// 确定ip数量
		final int count = countByIpRange(ipFrom, ipTo);

		final List<String> ips = new ArrayList<>(count);
		// 是否是循环的第一个值
		boolean aIsStart = true, bIsStart = true, cIsStart = true;
		// 是否是循环的最后一个值
		boolean aIsEnd, bIsEnd, cIsEnd;
		// 循环的结束值
		final int aEnd = getPartOfIpLong(ipTo, 1);
		int bEnd;
		int cEnd;
		int dEnd;
		final StringBuilder sb = StrUtil.builder(15);
		for (int a = getPartOfIpLong(ipFrom, 1); a <= aEnd; a++) {
			aIsEnd = (a == aEnd);
			// 本次循环的结束结束值
			bEnd = aIsEnd ? getPartOfIpLong(ipTo, 2) : 255;
			for (int b = (aIsStart ? getPartOfIpLong(ipFrom, 2) : 0); b <= bEnd; b++) {
				// 在上一个循环是最后值的基础上进行判断
				bIsEnd = aIsEnd && (b == bEnd);
				cEnd = bIsEnd ? getPartOfIpLong(ipTo, 3) : 255;
				for (int c = (bIsStart ? getPartOfIpLong(ipFrom, 3) : 0); c <= cEnd; c++) {
					// 在之前循环是最后值的基础上进行判断
					cIsEnd = bIsEnd && (c == cEnd);
					dEnd = cIsEnd ? getPartOfIpLong(ipTo, 4) : 255;
					for (int d = (cIsStart ? getPartOfIpLong(ipFrom, 4) : 0); d <= dEnd; d++) {
						sb.setLength(0);
						ips.add(sb.append(a).append(CharUtil.DOT)
								.append(b).append(CharUtil.DOT)
								.append(c).append(CharUtil.DOT)
								.append(d)
								.toString());
					}
					cIsStart = false;
				}
				bIsStart = false;
			}
			aIsStart = false;
		}
		return ips;
	}

	/**
	 * 根据 ip的long值 获取 ip字符串，即：xxx.xxx.xxx.xxx
	 *
	 * @param longIp IP的long表示形式
	 * @return 点分十进制ip地址
	 */
	public static String longToIpv4(final long longIp) {
		return StrUtil.builder(15)
				.append(getPartOfIpLong(longIp, 1)).append(CharUtil.DOT)
				.append(getPartOfIpLong(longIp, 2)).append(CharUtil.DOT)
				.append(getPartOfIpLong(longIp, 3)).append(CharUtil.DOT)
				.append(getPartOfIpLong(longIp, 4))
				.toString();
	}

	/**
	 * 将 ip字符串 转换为 long值
	 * <p>方法别名：inet_aton</p>
	 *
	 * @param strIp ip地址，点分十进制，xxx.xxx.xxx.xxx
	 * @return ip的long值
	 */
	public static long ipv4ToLong(final String strIp) {
		final Matcher matcher = PatternPool.IPV4.matcher(strIp);
		Assert.isTrue(matcher.matches(), "Invalid IPv4 address: {}", strIp);
		return matchAddress(matcher);
	}

	/**
	 * 根据 ip 和 掩码位 获取 子网的起始IP（字符串型）
	 * <p>方法别名：inet_ntoa</p>
	 *
	 * @param ip      给定的IP，点分十进制，如：xxx.xxx.xxx.xxx
	 * @param maskBit 给定的掩码位，如：30
	 * @return 起始IP的字符串表示
	 */
	public static String getBeginIpStr(final String ip, final int maskBit) {
		return longToIpv4(getBeginIpLong(ip, maskBit));
	}

	/**
	 * 根据 ip 和 掩码位 获取 子网的起始IP（Long型）
	 *
	 * @param ip      给定的IP，点分十进制，如：xxx.xxx.xxx.xxx
	 * @param maskBit 给定的掩码位，如：30
	 * @return 起始IP的长整型表示
	 */
	public static Long getBeginIpLong(final String ip, final int maskBit) {
		assertMaskBitValid(maskBit);
		return ipv4ToLong(ip) & MaskBit.getMaskIpLong(maskBit);
	}

	/**
	 * 根据 ip 和 掩码位 获取 子网的终止IP（字符串型）
	 *
	 * @param ip      给定的IP，点分十进制，如：xxx.xxx.xxx.xxx
	 * @param maskBit 给定的掩码位，如：30
	 * @return 终止IP的字符串表示
	 */
	public static String getEndIpStr(final String ip, final int maskBit) {
		return longToIpv4(getEndIpLong(ip, maskBit));
	}

	/**
	 * 根据 ip 和 掩码位 获取 子网的终止IP（Long型）
	 *
	 * @param ip      给定的IP，点分十进制，如：xxx.xxx.xxx.xxx
	 * @param maskBit 给定的掩码位，如：30
	 * @return 终止IP的长整型表示
	 */
	public static Long getEndIpLong(final String ip, final int maskBit) {
		return getBeginIpLong(ip, maskBit) + ~MaskBit.getMaskIpLong(maskBit);
	}

	/**
	 * 将 子网掩码 转换为 掩码位
	 *
	 * @param mask 掩码的点分十进制表示，例如 255.255.255.0
	 * @return 掩码位，例如 24
	 * @throws IllegalArgumentException 子网掩码非法
	 */
	public static int getMaskBitByMask(final String mask) {
		final Integer maskBit = MaskBit.getMaskBit(mask);
		Assert.notNull(maskBit, "Invalid netmask：{}", mask);
		return maskBit;
	}

	/**
	 * 获取 子网内的 地址总数
	 *
	 * @param maskBit 掩码位，取值范围：({@link Ipv4Pool#IPV4_MASK_BIT_VALID_MIN}, {@link Ipv4Pool#IPV4_MASK_BIT_MAX}]
	 * @param isAll   true:全量地址，false:可用地址
	 * @return 子网内地址总数
	 */
	public static int countByMaskBit(final int maskBit, final boolean isAll) {
		Assert.isTrue(maskBit > IPV4_MASK_BIT_VALID_MIN && maskBit <= IPV4_MASK_BIT_MAX,
				"Not support mask bit: {}", maskBit);
		//如果掩码位等于32，则可用地址为0
		if (maskBit == IPV4_MASK_BIT_MAX && false == isAll) {
			return 0;
		}

		final int count = 1 << (IPV4_MASK_BIT_MAX - maskBit);
		return isAll ? count : count - 2;
	}

	/**
	 * 根据 掩码位 获取 掩码地址
	 *
	 * @param maskBit 掩码位，如：24，取值范围：[{@link Ipv4Pool#IPV4_MASK_BIT_VALID_MIN}, {@link Ipv4Pool#IPV4_MASK_BIT_MAX}]
	 * @return 掩码地址，点分十进制，如:255.255.255.0
	 */
	public static String getMaskByMaskBit(final int maskBit) {
		return MaskBit.get(maskBit);
	}

	/**
	 * 根据 开始IP 与 结束IP 获取 掩码地址
	 *
	 * @param fromIp 开始IP，包含，点分十进制
	 * @param toIp   结束IP，包含，点分十进制
	 * @return 掩码地址，点分十进制
	 */
	public static String getMaskByIpRange(final String fromIp, final String toIp) {
		final long toIpLong = ipv4ToLong(toIp);
		final long fromIpLong = ipv4ToLong(fromIp);
		Assert.isTrue(fromIpLong <= toIpLong, "Start IP must be less than or equal to end IP!");

		return StrUtil.builder(15)
				.append(255 - getPartOfIpLong(toIpLong, 1) + getPartOfIpLong(fromIpLong, 1)).append(CharUtil.DOT)
				.append(255 - getPartOfIpLong(toIpLong, 2) + getPartOfIpLong(fromIpLong, 2)).append(CharUtil.DOT)
				.append(255 - getPartOfIpLong(toIpLong, 3) + getPartOfIpLong(fromIpLong, 3)).append(CharUtil.DOT)
				.append(255 - getPartOfIpLong(toIpLong, 4) + getPartOfIpLong(fromIpLong, 4))
				.toString();
	}

	/**
	 * 获得 指定区间内的 ip数量
	 *
	 * @param fromIp 开始IP，包含，点分十进制
	 * @param toIp   结束IP，包含，点分十进制
	 * @return IP数量
	 */
	public static int countByIpRange(final String fromIp, final String toIp) {
		final long toIpLong = ipv4ToLong(toIp);
		final long fromIpLong = ipv4ToLong(fromIp);
		return countByIpRange(fromIpLong, toIpLong);
	}

	/**
	 * 获得 指定区间内的 ip数量
	 *
	 * @param fromIp 开始IP，包含
	 * @param toIp   结束IP，包含
	 * @return IP数量
	 */
	public static int countByIpRange(final long fromIp, final long toIp) {
		Assert.isTrue(fromIp <= toIp, "Start IP must be less than or equal to end IP!");

		int count = 1;
		count += (getPartOfIpLong(toIp, 4) - getPartOfIpLong(fromIp, 4));
		count += (getPartOfIpLong(toIp, 3) - getPartOfIpLong(fromIp, 3)) << 8;
		count += (getPartOfIpLong(toIp, 2) - getPartOfIpLong(fromIp, 2)) << 16;
		count += (getPartOfIpLong(toIp, 1) - getPartOfIpLong(fromIp, 1)) << 24;
		return count;
	}

	/**
	 * 判断掩码是否合法
	 *
	 * @param mask 掩码的点分十进制表示，例如 255.255.255.0
	 * @return true：掩码合法；false：掩码不合法
	 */
	public static boolean isMaskValid(final String mask) {
		return MaskBit.getMaskBit(mask) != null;
	}

	/**
	 * 判断掩码位是否合法
	 *
	 * @param maskBit 掩码位，有效范围：[{@link Ipv4Pool#IPV4_MASK_BIT_VALID_MIN}, {@link Ipv4Pool#IPV4_MASK_BIT_MAX}]
	 * @return true：掩码位合法；false：掩码位不合法
	 */
	public static boolean isMaskBitValid(final int maskBit) {
		return maskBit >= IPV4_MASK_BIT_VALID_MIN && maskBit <= IPV4_MASK_BIT_MAX;
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
	 * @param ipAddress IP地址，点分十进制
	 * @return 是否为内网IP
	 * @since 5.7.18
	 */
	public static boolean isInnerIP(final String ipAddress) {
		return isInnerIP(ipv4ToLong(ipAddress));
	}

	/**
	 * 是否为内网地址
	 *
	 * @param ipNum IP地址数值形式
	 * @return 是否为内网IP
	 * @see #isInnerIP(String)
	 * @since 6.0.0
	 */
	public static boolean isInnerIP(final long ipNum) {
		return isBetween(ipNum, IPV4_A_PRIVATE_NUM_MIN, IPV4_A_PRIVATE_NUM_MAX)
				|| isBetween(ipNum, IPV4_B_PRIVATE_NUM_MIN, IPV4_B_PRIVATE_NUM_MAX)
				|| isBetween(ipNum, IPV4_C_PRIVATE_NUM_MIN, IPV4_C_PRIVATE_NUM_MAX)
				|| LOCAL_IP_NUM == ipNum;
	}

	/**
	 * 是否为公网地址
	 * <p>
	 * 公网IP：
	 * <pre>
	 * A类 1.0.0.0-9.255.255.255，11.0.0.0-126.255.255.255
	 * B类 128.0.0.0-172.15.255.255，172.32.0.0-191.255.255.255
	 * C类 192.0.0.0-192.167.255.255，192.169.0.0-223.255.255.255
	 * </pre>
	 *
	 * @param ipAddress IP地址，点分十进制
	 * @return 是否为公网IP
	 * @since 6.0.0
	 */
	public static boolean isPublicIP(final String ipAddress) {
		return isPublicIP(ipv4ToLong(ipAddress));
	}

	/**
	 * 是否为公网地址
	 *
	 * @param ipNum IP地址数值形式
	 * @return 是否为公网IP
	 * @see #isPublicIP(String)
	 * @since 6.0.0
	 */
	public static boolean isPublicIP(final long ipNum) {
		return isBetween(ipNum, IPV4_A_PUBLIC_1_NUM_MIN, IPV4_A_PUBLIC_1_NUM_MAX)
				|| isBetween(ipNum, IPV4_A_PUBLIC_2_NUM_MIN, IPV4_A_PUBLIC_2_NUM_MAX)
				|| isBetween(ipNum, IPV4_B_PUBLIC_1_NUM_MIN, IPV4_B_PUBLIC_1_NUM_MAX)
				|| isBetween(ipNum, IPV4_B_PUBLIC_2_NUM_MIN, IPV4_B_PUBLIC_2_NUM_MAX)
				|| isBetween(ipNum, IPV4_C_PUBLIC_1_NUM_MIN, IPV4_C_PUBLIC_1_NUM_MAX)
				|| isBetween(ipNum, IPV4_C_PUBLIC_2_NUM_MIN, IPV4_C_PUBLIC_2_NUM_MAX);
	}

	/**
	 * 获取ip(Long类型)指定部分的十进制值，即，{@literal X.X.X.X }形式中每个部分的值
	 * <p>例如，ip为{@literal 0xC0A802FA}，第1部分的值为：
	 * <ul>
	 * <li>第1部分的值为：{@literal 0xC0}，十进制值为：192</li>
	 * <li>第2部分的值为：{@literal 0xA8}，十进制值为：168</li>
	 * <li>第3部分的值为：{@literal 0x02}，十进制值为：2</li>
	 * <li>第4部分的值为：{@literal 0xFA}，十进制值为：250</li>
	 * </ul>
	 *
	 * @param ip       ip地址，Long类型
	 * @param position 指定位置，取值范围：[1,4]
	 * @return ip地址指定部分的十进制值
	 * @since 6.0.0
	 */
	public static int getPartOfIpLong(final long ip, final int position) {
		switch (position) {
			case 1:
				return ((int) ip >> 24) & 0xFF;
			case 2:
				return ((int) ip >> 16) & 0xFF;
			case 3:
				return ((int) ip >> 8) & 0xFF;
			case 4:
				return ((int) ip) & 0xFF;
			default:
				throw new IllegalArgumentException("Illegal position of ip Long: " + position);
		}
	}
	//-------------------------------------------------------------------------------- Private method start

	/**
	 * 将匹配到的Ipv4地址转为Long类型
	 *
	 * @param matcher 匹配到的Ipv4正则
	 * @return ip的long值
	 */
	private static long matchAddress(final Matcher matcher) {
		int addr = 0;
		// 每个点分十进制数字 转为 8位二进制
		for (int i = 1; i <= 4; ++i) {
			addr <<= 8;
			addr |= Integer.parseInt(matcher.group(i));
		}
		// int的最高位无法直接使用，转为Long
		if (addr < 0) {
			return IPV4_NUM_MAX & addr;
		}
		return addr;
	}

	/**
	 * 指定IP是否在指定范围内
	 *
	 * @param userIp 用户IP
	 * @param begin  开始IP，包含
	 * @param end    结束IP，包含
	 * @return 是否在范围内
	 */
	private static boolean isBetween(final long userIp, final long begin, final long end) {
		return (userIp >= begin) && (userIp <= end);
	}

	/**
	 * 校验 掩码位数，合法范围为：[{@link Ipv4Pool#IPV4_MASK_BIT_VALID_MIN}, {@link Ipv4Pool#IPV4_MASK_BIT_MAX}]，不合法则抛出异常
	 *
	 * @param maskBit 掩码位数
	 */
	private static void assertMaskBitValid(final int maskBit) {
		Assert.isTrue(isMaskBitValid(maskBit), "Invalid maskBit：{}", maskBit);
	}
	//-------------------------------------------------------------------------------- Private method end
}
