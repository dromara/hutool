package cn.hutool.extra.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author sidian
 */
@SpringBootTest(classes = EnableSpringUtilTest.class)
@EnableSpringUtil
@ExtendWith(SpringExtension.class)
public class EnableSpringUtilTest {

	@Test
	public void test() {
		// 使用@EnableSpringUtil注解后, 能获取上下文
		assertNotNull(SpringUtil.getApplicationContext());
		// 不使用时, 为null
//        assertNull(SpringUtil.getApplicationContext());
	}
}
