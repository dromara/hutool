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
 * @author looly
 */
package cn.hutool.crypto.digest.opt;