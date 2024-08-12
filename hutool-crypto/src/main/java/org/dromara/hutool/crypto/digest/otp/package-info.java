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
 * OTP 是 One-Time Password的简写，表示一次性密码。
 * <p>
 * 计算OTP串的公式：
 * <pre>
 * OTP(K,C) = Truncate(HMAC-SHA-1(K,C))
 * K：表示秘钥串
 * C：是一个数字，表示随机数
 * Truncate：是一个函数，就是怎么截取加密后的串，并取加密后串的哪些字段组成一个数字。
 * </pre>
 *
 * 实现包括：
 * <ul>
 *     <li>HMAC-based one-time passwords (HOTP) 基于HMAC算法一次性密码生成器</li>
 *     <li>time-based one-time passwords (TOTP) 基于时间戳算法的一次性密码生成器</li>
 * </ul>
 *
 * @author looly
 */
package org.dromara.hutool.crypto.digest.otp;
