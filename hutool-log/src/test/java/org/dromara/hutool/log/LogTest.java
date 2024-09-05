/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
