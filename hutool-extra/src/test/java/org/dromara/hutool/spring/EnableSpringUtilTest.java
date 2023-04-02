package org.dromara.hutool.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author sidian
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EnableSpringUtilTest.class)
@EnableSpringUtil
public class EnableSpringUtilTest {

	@Test
	public void test() {
		// 使用@EnableSpringUtil注解后, 能获取上下文
		Assertions.assertNotNull(SpringUtil.getApplicationContext());
		// 不使用时, 为null
//        Assertions.assertNull(SpringUtil.getApplicationContext());
	}
}
