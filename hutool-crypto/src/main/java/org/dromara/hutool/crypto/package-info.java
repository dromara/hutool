/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
 * 加密解密模块，实现了对JDK中加密解密算法的封装，入口为SecureUtil，实现了：
 *
 * <pre>
 * 1. 对称加密（symmetric），例如：AES、DES等
 * 2. 非对称加密（asymmetric），例如：RSA、DSA等
 * 3. 摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等
 * </pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.crypto;
