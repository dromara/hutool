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
