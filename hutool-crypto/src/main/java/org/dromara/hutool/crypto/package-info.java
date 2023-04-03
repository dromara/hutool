/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
