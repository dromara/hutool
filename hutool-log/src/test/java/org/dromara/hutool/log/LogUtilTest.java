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
