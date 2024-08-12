/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.crypto.provider;

import java.security.Provider;

/**
 * Provider对象生产工厂类<br>
 * 通过SPI方式加载可用的{@link ProviderFactory}，并创建对应的{@link Provider}<br>
 * spi定义在：META-INF/services/org.dromara.hutool.crypto.provider.ProviderFactory
 *
 * @author looly
 * @since 6.0.0
 */
public interface ProviderFactory {

	/**
	 * 创建{@link Provider}
	 *
	 * @return {@link Provider}
	 */
	Provider create();
}
