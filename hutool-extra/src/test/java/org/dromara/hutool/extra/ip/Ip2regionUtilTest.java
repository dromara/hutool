package org.dromara.hutool.extra.ip;

import net.dreamlu.mica.ip2region.config.Ip2regionProperties;
import net.dreamlu.mica.ip2region.core.IpInfo;
import net.dreamlu.mica.ip2region.impl.Ip2regionSearcherImpl;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.extra.spring.SpringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * IP地址信息工具类单元测试
 * @author LGXTvT
 * @version 1.0
 * @description: TODO
 * @date 2024-08-12 21:23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SpringUtil.class, Ip2regionUtilTest.class})
@TestPropertySource(properties = {"spring.profiles.active=dev"})
public class Ip2regionUtilTest {

	@Bean
	public ResourceLoader resourceLoader() {
		return new DefaultResourceLoader(Ip2regionUtilTest.class.getClassLoader());
	}

	@Bean
	public Ip2regionSearcherImpl ip2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties ip2regionProperties) {
		return new Ip2regionSearcherImpl(resourceLoader, ip2regionProperties);
	}

	@Bean
	public Ip2regionProperties ip2regionProperties() {
		return new Ip2regionProperties();
	}

	@Test
	public void getAddressTest() {
		String address = Ip2regionUtil.getAddress("183.250.92.19");
		Console.log(address); // 中国福建厦门
	}

	@Test
	public void getAddressAndIspTest() {
		String address = Ip2regionUtil.getAddressAndIsp("183.250.92.19");
		Console.log(address); // 中国 福建 厦门 移动
	}

	@Test
	public void getIpInfoTest() {
		IpInfo ipInfo = Ip2regionUtil.getIpInfo("183.250.92.19");
		Console.log(ipInfo); // IpInfo(cityId=null, country=中国, region=null, province=福建, city=厦门, isp=移动, dataPtr=383100)
	}

}
