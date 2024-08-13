package org.dromara.hutool.extra.ip;

import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.dromara.hutool.extra.spring.SpringUtil;

/**
 * IP地址信息工具类
 * @author LGXTvT
 * @version 1.0
 * @date 2024-08-12 20:43
 */
public class Ip2regionUtil {

	private final static Ip2regionSearcher IP2REGION_SEARCHER = SpringUtil.getBean(Ip2regionSearcher.class);

	/**
	 * 获取地址信息
	 * @param ip ip
	 * @return 地址
	 */
	public static String getAddress(String ip) {
		return IP2REGION_SEARCHER.getAddress(ip);
	}


	/**
	 * 获取地址信息包含 isp
	 * @param ip ip
	 * @return 地址
	 */
	public static String getAddressAndIsp(String ip) {
		return IP2REGION_SEARCHER.getAddressAndIsp(ip);
	}


	/**
	 * ip位置搜索
	 * @param ip ip
	 * @return ip位置信息
	 */
	public static IpInfo getIpInfo(String ip) {
		return IP2REGION_SEARCHER.memorySearch(ip);
	}

}
