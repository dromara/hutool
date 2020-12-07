package cn.hutool.extra.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author sidian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnableSpringUtilTest.class)
@EnableSpringUtil
public class EnableSpringUtilTest {

	@Test
	public void test() {
		// 使用@EnableSpringUtil注解后, 能获取上下文
		Assert.assertNotNull(SpringUtil.getApplicationContext());
		// 不使用时, 为null
//        Assert.assertNull(SpringUtil.getApplicationContext());
	}
}
