package cn.hutool.core.net;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * IPV4地址工具类
 *
 * <p>pr自：https://gitee.com/loolly/hutool/pulls/161</p>
 *
 * @author ZhuKun
 * @since 5.4.1
 */
public class Ipv4Util {

	public static final String LOCAL_IP = "127.0.0.1";

	/**
	 * IP段的分割符
	 */
	public static final String IP_SPLIT_MARK = "-";

	/**
	 * IP与掩码的分割符
	 */
	public static final String IP_MASK_SPLIT_MARK = StrUtil.SLASH;

	/**
	 * 最大掩码位
	 */
	public static final int IP_MASK_MAX = 32;

	/**
	 * 格式化IP段
	 *
	 * @param ip   IP地址
	 * @param mask 掩码
	 * @return 返回xxx.xxx.xxx.xxx/mask的格式
	 */
	public static String formatIpBlock(String ip, String mask) {
		return ip + IP_MASK_SPLIT_MARK + getMaskBitByMask(mask);
	}

	/**
	 * 智能转换IP地址集合
	 *
	 * @param ipRange IP段，支持X.X.X.X-X.X.X.X或X.X.X.X/X
	 * @param isAll   true:全量地址，false:可用地址；仅在ipRange为X.X.X.X/X时才生效
	 * @return IP集
	 */
	public static List<String> list(String ipRange, boolean isAll) {
		if (ipRange.contains(IP_SPLIT_MARK)) {
			// X.X.X.X-X.X.X.X
			final String[] range = StrUtil.splitToArray(ipRange, IP_SPLIT_MARK);
			return list(range[0], range[1]);
		} else if (ipRange.contains(IP_MASK_SPLIT_MARK)) {
			// X.X.X.X/X
			final String[] param = StrUtil.splitToArray(ipRange, IP_MASK_SPLIT_MARK);
			return list(param[0], Integer.parseInt(param[1]), isAll);
		} else {
			return ListUtil.toList(ipRange);
		}
	}

	/**
	 * 根据IP地址、子网掩码获取IP地址区间
	 *
	 * @param ip      IP地址
	 * @param maskBit 掩码位，例如24、32
	 * @param isAll   true:全量地址，false:可用地址
	 * @return 区间地址
	 */
	public static List<String> list(String ip, int maskBit, boolean isAll) {
		if (maskBit == IP_MASK_MAX) {
			final List<String> list = new ArrayList<>();
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

		int lastDotIndex = startIp.lastIndexOf(CharUtil.DOT) + 1;
		startIp = StrUtil.subPre(startIp, lastDotIndex) +
				(Integer.parseInt(Objects.requireNonNull(StrUtil.subSuf(startIp, lastDotIndex))) + 1);
		lastDotIndex = endIp.lastIndexOf(CharUtil.DOT) + 1;
		endIp = StrUtil.subPre(endIp, lastDotIndex) +
				(Integer.parseInt(Objects.requireNonNull(StrUtil.subSuf(endIp, lastDotIndex))) - 1);
		return list(startIp, endIp);
	}

	/**
	 * 得到IP地址区间
	 *
	 * @param ipFrom 开始IP
	 * @param ipTo   结束IP
	 * @return 区间地址
	 */
	public static List<String> list(String ipFrom, String ipTo) {
		final int[] ipf = Convert.convert(int[].class, StrUtil.splitToArray(ipFrom, CharUtil.DOT));
		final int[] ipt = Convert.convert(int[].class, StrUtil.splitToArray(ipTo, CharUtil.DOT));

		final List<String> ips = new ArrayList<>();
		for (int a = ipf[0]; a <= ipt[0]; a++) {
			for (int b = (a == ipf[0] ? ipf[1] : 0); b <= (a == ipt[0] ? ipt[1]
					: 255); b++) {
				for (int c = (b == ipf[1] ? ipf[2] : 0); c <= (b == ipt[1] ? ipt[2]
						: 255); c++) {
					for (int d = (c == ipf[2] ? ipf[3] : 0); d <= (c == ipt[2] ? ipt[3]
							: 255); d++) {
						ips.add(a + "." + b + "." + c + "." + d);
					}
				}
			}
		}
		return ips;
	}

	/**
	 * 根据long值获取ip v4地址：xx.xx.xx.xx
	 *
	 * @param longIP IP的long表示形式
	 * @return IP V4 地址
	 */
	public static String longToIpv4(long longIP) {
		final StringBuilder sb = StrUtil.builder();
		// 直接右移24位
		sb.append(longIP >> 24 & 0xFF);
		sb.append(CharUtil.DOT);
		// 将高8位置0，然后右移16位
		sb.append(longIP >> 16 & 0xFF);
		sb.append(CharUtil.DOT);
		sb.append(longIP >> 8 & 0xFF);
		sb.append(CharUtil.DOT);
		sb.append(longIP & 0xFF);
		return sb.toString();
	}

	/**
	 * 根据ip地址(xxx.xxx.xxx.xxx)计算出long型的数据
	 * 方法别名：inet_aton
	 *
	 * @param strIP IP V4 地址
	 * @return long值
	 */
	public static long ipv4ToLong(String strIP) {
		final Matcher matcher = PatternPool.IPV4.matcher(strIP);
		if (matcher.matches()) {
			return matchAddress(matcher);
		}
//		Validator.validateIpv4(strIP, "Invalid IPv4 address!");
//		final long[] ip = Convert.convert(long[].class, StrUtil.split(strIP, CharUtil.DOT));
//		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
		throw new IllegalArgumentException("Invalid IPv4 address!");
	}

	/**
	 * 根据ip地址(xxx.xxx.xxx.xxx)计算出long型的数据, 如果格式不正确返回 defaultValue
	 * @param strIP IP V4 地址
	 * @param defaultValue 默认值
	 * @return long值
	 */
	public static long ipv4ToLong(String strIP, long defaultValue) {
		return Validator.isIpv4(strIP) ? ipv4ToLong(strIP) : defaultValue;
	}

	/**
	 * 根据 ip/掩码位 计算IP段的起始IP（字符串型）
	 * 方法别名：inet_ntoa
	 *
	 * @param ip      给定的IP，如218.240.38.69
	 * @param maskBit 给定的掩码位，如30
	 * @return 起始IP的字符串表示
	 */
	public static String getBeginIpStr(String ip, int maskBit) {
		return longToIpv4(getBeginIpLong(ip, maskBit));
	}

	/**
	 * 根据 ip/掩码位 计算IP段的起始IP（Long型）
	 *
	 * @param ip      给定的IP，如218.240.38.69
	 * @param maskBit 给定的掩码位，如30
	 * @return 起始IP的长整型表示
	 */
	public static Long getBeginIpLong(String ip, int maskBit) {
		return ipv4ToLong(ip) & ipv4ToLong(getMaskByMaskBit(maskBit));
	}

	/**
	 * 根据 ip/掩码位 计算IP段的终止IP（字符串型）
	 *
	 * @param ip      给定的IP，如218.240.38.69
	 * @param maskBit 给定的掩码位，如30
	 * @return 终止IP的字符串表示
	 */
	public static String getEndIpStr(String ip, int maskBit) {
		return longToIpv4(getEndIpLong(ip, maskBit));
	}

	/**
	 * 根据子网掩码转换为掩码位
	 *
	 * @param mask 掩码的点分十进制表示，例如 255.255.255.0
	 * @return 掩码位，例如 24
	 * @throws IllegalArgumentException 子网掩码非法
	 */
	public static int getMaskBitByMask(String mask) {
		Integer maskBit = MaskBit.getMaskBit(mask);
		if (maskBit == null) {
			throw new IllegalArgumentException("Invalid netmask " + mask);
		}
		return maskBit;
	}

	/**
	 * 计算子网大小
	 *
	 * @param maskBit 掩码位
	 * @param isAll   true:全量地址，false:可用地址
	 * @return 地址总数
	 */
	public static int countByMaskBit(int maskBit, boolean isAll) {
		//如果是可用地址的情况，掩码位小于等于0或大于等于32，则可用地址为0
		if ((false == isAll) && (maskBit <= 0 || maskBit >= 32)) {
			return 0;
		}

		final int count = (int) Math.pow(2, 32 - maskBit);
		return isAll ? count : count - 2;
	}

	/**
	 * 根据掩码位获取掩码
	 *
	 * @param maskBit 掩码位
	 * @return 掩码
	 */
	public static String getMaskByMaskBit(int maskBit) {
		return MaskBit.get(maskBit);
	}

	/**
	 * 根据开始IP与结束IP计算掩码
	 *
	 * @param fromIp 开始IP
	 * @param toIp   结束IP
	 * @return 掩码x.x.x.x
	 */
	public static String getMaskByIpRange(String fromIp, String toIp) {
		long toIpLong = ipv4ToLong(toIp);
		long fromIpLong = ipv4ToLong(fromIp);
		Assert.isTrue(fromIpLong < toIpLong, "to IP must be greater than from IP!");

		String[] fromIpSplit = StrUtil.splitToArray(fromIp, CharUtil.DOT);
		String[] toIpSplit = StrUtil.splitToArray(toIp, CharUtil.DOT);
		StringBuilder mask = new StringBuilder();
		for (int i = 0; i < toIpSplit.length; i++) {
			mask.append(255 - Integer.parseInt(toIpSplit[i]) + Integer.parseInt(fromIpSplit[i])).append(CharUtil.DOT);
		}
		return mask.substring(0, mask.length() - 1);
	}

	/**
	 * 计算IP区间有多少个IP
	 *
	 * @param fromIp 开始IP
	 * @param toIp   结束IP
	 * @return IP数量
	 */
	public static int countByIpRange(String fromIp, String toIp) {
		long toIpLong = ipv4ToLong(toIp);
		long fromIpLong = ipv4ToLong(fromIp);
		if (fromIpLong > toIpLong) {
			throw new IllegalArgumentException("to IP must be greater than from IP!");
		}
		int count = 1;
		int[] fromIpSplit = StrUtil.split(fromIp, CharUtil.DOT).stream().mapToInt(Integer::parseInt).toArray();
		int[] toIpSplit = StrUtil.split(toIp, CharUtil.DOT).stream().mapToInt(Integer::parseInt).toArray();
		for (int i = fromIpSplit.length - 1; i >= 0; i--) {
			count += (toIpSplit[i] - fromIpSplit[i]) * Math.pow(256, fromIpSplit.length - i - 1);
		}
		return count;
	}

	/**
	 * 判断掩码是否合法
	 *
	 * @param mask 掩码的点分十进制表示，例如 255.255.255.0
	 * @return true：掩码合法；false：掩码不合法
	 */
	public static boolean isMaskValid(String mask) {
		return MaskBit.getMaskBit(mask) != null;
	}

	/**
	 * 判断掩码位是否合法
	 *
	 * @param maskBit 掩码位，例如 24
	 * @return true：掩码位合法；false：掩码位不合法
	 */
	public static boolean isMaskBitValid(int maskBit) {
		return MaskBit.get(maskBit) != null;
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
	 * @since 5.7.18
	 */
	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp;
		long ipNum = ipv4ToLong(ipAddress);

		long aBegin = ipv4ToLong("10.0.0.0");
		long aEnd = ipv4ToLong("10.255.255.255");

		long bBegin = ipv4ToLong("172.16.0.0");
		long bEnd = ipv4ToLong("172.31.255.255");

		long cBegin = ipv4ToLong("192.168.0.0");
		long cEnd = ipv4ToLong("192.168.255.255");

		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || LOCAL_IP.equals(ipAddress);
		return isInnerIp;
	}

	//-------------------------------------------------------------------------------- Private method start

	/**
	 * 根据 ip/掩码位 计算IP段的终止IP（Long型）
	 * 注：此接口返回负数，请使用转成字符串后再转Long型
	 *
	 * @param ip      给定的IP，如218.240.38.69
	 * @param maskBit 给定的掩码位，如30
	 * @return 终止IP的长整型表示
	 */
	public static Long getEndIpLong(String ip, int maskBit) {
		return getBeginIpLong(ip, maskBit)
				+ ~ipv4ToLong(getMaskByMaskBit(maskBit));
	}

	/**
	 * 将匹配到的Ipv4地址的4个分组分别处理
	 *
	 * @param matcher 匹配到的Ipv4正则
	 * @return ipv4对应long
	 */
	private static long matchAddress(Matcher matcher) {
		long addr = 0;
		for (int i = 1; i <= 4; ++i) {
			addr |= Long.parseLong(matcher.group(i)) << 8 * (4 - i);
		}
		return addr;
	}

	/**
	 * 指定IP的long是否在指定范围内
	 *
	 * @param userIp 用户IP
	 * @param begin  开始IP
	 * @param end    结束IP
	 * @return 是否在范围内
	 */
	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}
	//-------------------------------------------------------------------------------- Private method end
}
