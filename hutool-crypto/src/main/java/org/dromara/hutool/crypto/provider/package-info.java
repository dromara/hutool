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
