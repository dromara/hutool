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

/**
 * {@link java.security.Provider}相关封装，通过SPI机制，提供灵活的Provider注入。<br>
 * spi定义在：META-INF/services/org.dromara.hutool.crypto.provider.ProviderFactory
 *
 * <pre>
 *                    GlobalProviderFactory（单例持有Provider）
 *                               ^
 *                           （create）
 *                         ProviderFactory
 *                               |
 *                    BouncyCastleProviderFactory
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
package org.dromara.hutool.crypto.provider;
