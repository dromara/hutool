package cn.hutool.core.net;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * IPV4地址工具类
 *
 * <p>pr自：https://gitee.com/loolly/hutool/pulls/161</p>
 *
 * @author ZhuKun
 * @since 5.4.1
 */
public class Ipv4Util {
	/**
	 * IP段的分割符
	 */
	public static final String IP_SPLIT_MARK = "-";

	/**
	 * IP与掩码的分割符
	 */
	public static final String IP_MASK_SPLIT_MARK = "/";

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
			String[] range = ipRange.split(IP_SPLIT_MARK);
			return list(range[0], range[1]);
		} else if (ipRange.contains(IP_MASK_SPLIT_MARK)) {
			String[] param = ipRange.split(IP_MASK_SPLIT_MARK);
			return list(param[0], Integer.parseInt(param[1]), isAll);
		} else {
			List<String> ips = new ArrayList<>();
			ips.add(ipRange);
			return ips;
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
		List<String> list = new ArrayList<>();
		if (maskBit == IP_MASK_MAX) {
			if (Boolean.TRUE.equals(isAll)) {
				list.add(ip);
			}
		} else {
			String startIp = getBeginIpStr(ip, maskBit);
			String endIp = getEndIpStr(ip, maskBit);
			String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
			String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
			if (Boolean.TRUE.equals(isAll)) {
				startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]));
				endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]));
			} else {
				startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
				endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
			}
			list = list(startIp, endIp);
		}
		return list;
	}

	/**
	 * 得到IP地址区间
	 *
	 * @param ipFrom 开始IP
	 * @param ipTo   结束IP
	 * @return 区间地址
	 */
	public static List<String> list(String ipFrom, String ipTo) {
		final int[] ipf = Convert.convert(int[].class, StrUtil.splitToArray(ipFrom, '.'));
		final int[] ipt = Convert.convert(int[].class, StrUtil.splitToArray(ipTo, '.'));

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
		sb.append((longIP >>> 24));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append((longIP & 0x000000FF));
		return sb.toString();
	}

	/**
	 * 根据ip地址(xxx.xxx.xxx.xxx)计算出long型的数据
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
	 * 根据 ip/掩码位 计算IP段的起始IP（字符串型）
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
	private static Long getBeginIpLong(String ip, int maskBit) {
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
	 * @param mask 掩码，例如xxx.xxx.xxx.xxx
	 * @return 掩码位，例如32
	 */
	public static int getMaskBitByMask(String mask) {
		StringBuffer sbf;
		String str;
		int inetmask = 0;
		int count;
		for (String s : StrUtil.split(mask, ',')) {
			sbf = toBin(Integer.parseInt(s));
			str = sbf.reverse().toString();
			count = 0;
			for (int i = 0; i < str.length(); i++) {
				i = str.indexOf('1', i);
				if (i == -1) {
					break;
				}
				count++;
			}
			inetmask += count;
		}
		return inetmask;
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
		boolean isZero = !isAll && (maskBit <= 0 || maskBit >= 32);
		if (isZero) {
			return 0;
		}
		if (isAll) {
			return (int) Math.pow(2, 32 - maskBit);
		} else {
			return (int) Math.pow(2, 32 - maskBit) - 2;
		}
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
		if (fromIpLong > toIpLong) {
			throw new IllegalArgumentException("to IP must be greater than from IP!");
		}
		String[] fromIpSplit = StrUtil.splitToArray(fromIp, '.');
		String[] toIpSplit = StrUtil.splitToArray(toIp, '.');
		StringBuilder mask = new StringBuilder();
		for (int i = 0; i < toIpSplit.length; i++) {
			mask.append(255 - Integer.parseInt(toIpSplit[i]) + Integer.parseInt(fromIpSplit[i])).append(".");
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
		int[] fromIpSplit = StrUtil.split(fromIp, '.').stream().mapToInt(Integer::parseInt).toArray();
		int[] toIpSplit = StrUtil.split(toIp, '.').stream().mapToInt(Integer::parseInt).toArray();
		for (int i = fromIpSplit.length - 1; i >= 0; i--) {
			count += (toIpSplit[i] - fromIpSplit[i]) * Math.pow(256, fromIpSplit.length - i - 1);
		}
		return count;
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
	private static Long getEndIpLong(String ip, int maskBit) {
		return getBeginIpLong(ip, maskBit)
				+ ~ipv4ToLong(getMaskByMaskBit(maskBit));
	}

	private static StringBuffer toBin(int x) {
		StringBuffer result = new StringBuffer();
		result.append(x % 2);
		x /= 2;
		while (x > 0) {
			result.append(x % 2);
			x /= 2;
		}
		return result;
	}
	//-------------------------------------------------------------------------------- Private method end
}
