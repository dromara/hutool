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

import org.dromara.hutool.log.engine.LogEngineFactory;
import org.dromara.hutool.log.engine.console.ConsoleColorLogEngine;
import org.dromara.hutool.log.engine.console.ConsoleLogEngine;
import org.dromara.hutool.log.engine.log4j2.Log4j2LogEngine;
import org.junit.jupiter.api.Test;

public class LogUtilTest {

	@Test
	public void staticLog4j2Test() {
		LogEngineFactory.setDefaultEngine(Log4j2LogEngine.class);
		LogUtil.debug("This is static {} log", "debug");
		LogUtil.info("This is static {} log", "info");
	}

	@Test
	public void test() {
		LogEngineFactory.setDefaultEngine(ConsoleLogEngine.class);
		LogUtil.debug("This is static {} log", "debug");
		LogUtil.info("This is static {} log", "info");
	}

	@Test
	public void colorTest(){
		LogEngineFactory.setDefaultEngine(ConsoleColorLogEngine.class);
		LogUtil.debug("This is static {} log", "debug");
		LogUtil.info("This is static {} log", "info");
		LogUtil.error("This is static {} log", "error");
		LogUtil.warn("This is static {} log", "warn");
		LogUtil.trace("This is static {} log", "trace");
	}
}
