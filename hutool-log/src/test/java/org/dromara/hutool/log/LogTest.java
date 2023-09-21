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

package org.dromara.hutool.log;

import org.dromara.hutool.log.level.Level;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 日志门面单元测试
 * @author Looly
 *
 */
public class LogTest {

	@Test
	public void logTest(){
		final Log log = Log.get();

		// 自动选择日志实现
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
	}

	/**
	 * 兼容slf4j日志消息格式测试，即第二个参数是异常对象时正常输出异常信息
	 */
	@Test
	@Disabled
	public void logWithExceptionTest() {
		final Log log = Log.get();
		final Exception e = new Exception("test Exception");
		log.error("我是错误消息", e);
	}

	@Test
	public void logNullTest(){
		final Log log = Log.get();
		log.debug(null);
		log.info(null);
		log.warn(null);
	}

	@Test
	void getLogByClassTest() {
		Log.get(LogTest.class);
	}
}
