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
