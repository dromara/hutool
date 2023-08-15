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

package org.dromara.hutool.core.net;

import org.dromara.hutool.core.text.StrUtil;

/**
 * Ip相关常量
 * <pre>
 * 为了方便处理作出以下特别说明，若后续因此收到影响或有更好处理方式需要及时调整：
 * 1、此处定义的`最小值`、`最大值`并非实际ip可分配的最小值、最大值。而是正常地址格式所能表示的最小值、最大值。
 * 2、比如：1.0.0.0这种保留地址，我们仍然认为属于A类网。
 * 3、类似于127.0.0.1这种本地回环地址，我们仍然认为数据C类网。
 * </pre>
 *
 * @author aoshiguchen
 * @author emptypoint
 * @since 6.0.0
 */
public interface Ipv4Pool {

    /**
     * IP段的分割符
     */
    String IP_SPLIT_MARK = "-";

    /**
     * IP与掩码的分割符
     */
    String IP_MASK_SPLIT_MARK = StrUtil.SLASH;

    /**
     * localhost默认解析的ip地址
     */
    String LOCAL_IP = "127.0.0.1";
    /**
     * localhost默认解析的ip地址的数值形式
     */
    long LOCAL_IP_NUM = Ipv4Util.ipv4ToLong(LOCAL_IP);

    /**
     * Ipv4地址最小值字符串形式
     */
    String IPV4_STR_MIN = "0.0.0.0";
    /**
     * Ipv4最小值数值形式
     */
    long IPV4_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_STR_MIN);

    /**
     * Ipv4地址最大值字符串形式
     */
    String IPV4_STR_MAX = "255.255.255.255";
    /**
     * Ipv4最大值数值形式
     */
    long IPV4_NUM_MAX = 0xffffffffL;

    /**
     * Ipv4未使用地址最小值字符串形式
     */
    String IPV4_UNUSED_STR_MIN = "0.0.0.0";
    /**
     * Ipv4未使用地址最小值数值形式
     */
    long IPV4_UNUSED_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_UNUSED_STR_MIN);

    /**
     * Ipv4未使用地址最大值字符串形式
     */
    String IPV4_UNUSED_STR_MAX = "0.255.255.255";
    /**
     * Ipv4未使用地址最大值数值形式
     */
    long IPV4_UNUSED_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_UNUSED_STR_MAX);

    // region 子网掩码常量
    // ================================================== 子网掩码常量 ==================================================
    /**
     * Ipv4最小掩码位
     */
    int IPV4_MASK_BIT_MIN = 0;

    /**
     * Ipv4有意义的最小掩码位
     */
    int IPV4_MASK_BIT_VALID_MIN = 1;
    /**
     * Ipv4有意义的最小掩码字符串
     */
    String IPV4_MASK_VALID_MIN = MaskBit.get(IPV4_MASK_BIT_VALID_MIN);

    /**
     * Ipv4最大掩码位
     */
    int IPV4_MASK_BIT_MAX = 32;
    /**
     * Ipv4最大掩码字符串
     */
    String IPV4_MASK_MAX = MaskBit.get(IPV4_MASK_BIT_MAX);
    // endregion

    // region 本地回环地址常量
    // ================================================== 本地回环地址常量 ================================================
    /**
     * Ipv4 本地回环地址最小值字符串形式
     */
    String IPV4_LOOPBACK_STR_MIN = "127.0.0.0";
    /**
     * Ipv4 本地回环地址最小值数值形式
     */
    long IPV4_LOOPBACK_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_LOOPBACK_STR_MIN);
    /**
     * Ipv4 本地回环地址最大值字符串形式
     */
    String IPV4_LOOPBACK_STR_MAX = "127.255.255.255";
    /**
     * Ipv4 本地回环地址最大值数值形式
     */
    long IPV4_LOOPBACK_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_LOOPBACK_STR_MAX);
    // endregion

    // region A类地址常量
    // ================================================== A类地址常量 ==================================================
    /**
     * Ipv4 A类地址最小值字符串形式
     */
    String IPV4_A_STR_MIN = "0.0.0.0";
    /**
     * Ipv4 A类地址最小值数值形式
     */
    long IPV4_A_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_A_STR_MIN);

    /**
     * Ipv4 A类地址最大值字符串形式
     */
    String IPV4_A_STR_MAX = "127.255.255.255";
    /**
     * Ipv4 A类地址最大值数值形式
     */
    long IPV4_A_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_A_STR_MAX);

    /**
     * Ipv4 A类地址第一个公网网段最小值字符串形式
     */
    String IPV4_A_PUBLIC_1_STR_MIN = "1.0.0.0";
    /**
     * Ipv4 A类地址第一个公网网段最小值数值形式
     */
    long IPV4_A_PUBLIC_1_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_A_PUBLIC_1_STR_MIN);

    /**
     * Ipv4 A类地址第一个公网网段最大值字符串形式
     */
    String IPV4_A_PUBLIC_1_STR_MAX = "9.255.255.255";
    /**
     * Ipv4 A类地址第一个公网网段最大值数值形式
     */
    long IPV4_A_PUBLIC_1_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_A_PUBLIC_1_STR_MAX);

    /**
     * Ipv4 A类地址私网网段最小值字符串形式
     */
    String IPV4_A_PRIVATE_STR_MIN = "10.0.0.0";
    /**
     * Ipv4 A类地址私网网段最小值数值形式
     */
    long IPV4_A_PRIVATE_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_A_PRIVATE_STR_MIN);

    /**
     * Ipv4 A类地址私网网段最大值字符串形式
     */
    String IPV4_A_PRIVATE_STR_MAX = "10.255.255.255";
    /**
     * Ipv4 A类地址私网网段最大值数值形式
     */
    long IPV4_A_PRIVATE_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_A_PRIVATE_STR_MAX);

    /**
     * Ipv4 A类地址第二个公网网段最小值字符串形式
     */
    String IPV4_A_PUBLIC_2_STR_MIN = "11.0.0.0";
    /**
     * Ipv4 A类地址第二个公网网段最小值数值形式
     */
    long IPV4_A_PUBLIC_2_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_A_PUBLIC_2_STR_MIN);

    /**
     * Ipv4 A类地址第二个公网网段最大值字符串形式
     */
    String IPV4_A_PUBLIC_2_STR_MAX = "126.255.255.255";
    /**
     * Ipv4 A类地址第二个公网网段最大值数值形式
     */
    long IPV4_A_PUBLIC_2_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_A_PUBLIC_2_STR_MAX);
    // endregion

    // region B类地址常量
    // ================================================== B类地址常量 ==================================================
    /**
     * Ipv4 B类地址最小值字符串形式
     */
    String IPV4_B_STR_MIN = "128.0.0.0";
    /**
     * Ipv4 B类地址最小值数值形式
     */
    long IPV4_B_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_B_STR_MIN);

    /**
     * Ipv4 B类地址最大值字符串形式
     */
    String IPV4_B_STR_MAX = "191.255.255.255";
    /**
     * Ipv4 B类地址最大值数值形式
     */
    long IPV4_B_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_B_STR_MAX);

    /**
     * Ipv4 B类地址第一个公网网段最小值字符串形式
     */
    String IPV4_B_PUBLIC_1_STR_MIN = "128.0.0.0";
    /**
     * Ipv4 B类地址第一个公网网段最小值数值形式
     */
    long IPV4_B_PUBLIC_1_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_B_PUBLIC_1_STR_MIN);

    /**
     * Ipv4 B类地址第一个公网网段最大值字符串形式
     */
    String IPV4_B_PUBLIC_1_STR_MAX = "172.15.255.255";
    /**
     * Ipv4 B类地址第一个公网网段最大值数值形式
     */
    long IPV4_B_PUBLIC_1_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_B_PUBLIC_1_STR_MAX);

    /**
     * Ipv4 B类地址私网网段最小值字符串形式
     */
    String IPV4_B_PRIVATE_STR_MIN = "172.16.0.0";
    /**
     * Ipv4 B类地址私网网段最小值数值形式
     */
    long IPV4_B_PRIVATE_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_B_PRIVATE_STR_MIN);

    /**
     * Ipv4 B类地址私网网段最大值字符串形式
     */
    String IPV4_B_PRIVATE_STR_MAX = "172.31.255.255";
    /**
     * Ipv4 B类地址私网网段最大值数值形式
     */
    long IPV4_B_PRIVATE_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_B_PRIVATE_STR_MAX);

    /**
     * Ipv4 B类地址第二个公网网段最小值字符串形式
     */
    String IPV4_B_PUBLIC_2_STR_MIN = "172.32.0.0";
    /**
     * Ipv4 B类地址第二个公网网段最小值数值形式
     */
    long IPV4_B_PUBLIC_2_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_B_PUBLIC_2_STR_MIN);

    /**
     * Ipv4 B类地址第二个公网网段最大值字符串形式
     */
    String IPV4_B_PUBLIC_2_STR_MAX = "191.255.255.255";
    /**
     * Ipv4 B类地址第二个公网网段最大值数值形式
     */
    long IPV4_B_PUBLIC_2_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_B_PUBLIC_2_STR_MAX);
    // endregion

    // region C类地址常量
    // ================================================== C类地址常量 ==================================================
    /**
     * Ipv4 C类地址最小值字符串形式
     */
    String IPV4_C_STR_MIN = "192.0.0.0";
    /**
     * Ipv4 C类地址最小值数值形式
     */
    long IPV4_C_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_C_STR_MIN);

    /**
     * Ipv4 C类地址最大值字符串形式
     */
    String IPV4_C_STR_MAX = "223.255.255.255";
    /**
     * Ipv4 C类地址最大值数值形式
     */
    long IPV4_C_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_C_STR_MAX);

    /**
     * Ipv4 C类地址第一个公网网段最小值字符串形式
     */
    String IPV4_C_PUBLIC_1_STR_MIN = "192.0.0.0";
    /**
     * Ipv4 C类地址第一个公网网段最小值数值形式
     */
    long IPV4_C_PUBLIC_1_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_C_PUBLIC_1_STR_MIN);

    /**
     * Ipv4 C类地址第一个公网网段最大值字符串形式
     */
    String IPV4_C_PUBLIC_1_STR_MAX = "192.167.255.255";
    /**
     * Ipv4 C类地址第一个公网网段最大值数值形式
     */
    long IPV4_C_PUBLIC_1_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_C_PUBLIC_1_STR_MAX);

    /**
     * Ipv4 C类地址私网网段最小值字符串形式
     */
    String IPV4_C_PRIVATE_STR_MIN = "192.168.0.0";
    /**
     * Ipv4 C类地址私网网段最小值数值形式
     */
    long IPV4_C_PRIVATE_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_C_PRIVATE_STR_MIN);

    /**
     * Ipv4 C类地址私网网段最大值字符串形式
     */
    String IPV4_C_PRIVATE_STR_MAX = "192.168.255.255";
    /**
     * Ipv4 C类地址私网网段最大值数值形式
     */
    long IPV4_C_PRIVATE_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_C_PRIVATE_STR_MAX);

    /**
     * Ipv4 C类地址第二个公网网段最小值字符串形式
     */
    String IPV4_C_PUBLIC_2_STR_MIN = "192.169.0.0";
    /**
     * Ipv4 C类地址第二个公网网段最小值数值形式
     */
    long IPV4_C_PUBLIC_2_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_C_PUBLIC_2_STR_MIN);

    /**
     * Ipv4 C类地址第二个公网网段最大值字符串形式
     */
    String IPV4_C_PUBLIC_2_STR_MAX = "223.255.255.255";
    /**
     * Ipv4 C类地址第二个公网网段最大值数值形式
     */
    long IPV4_C_PUBLIC_2_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_C_PUBLIC_2_STR_MAX);
    // endregion

    // region D类地址常量
    // ================================================== D类地址常量 ==================================================
    /**
     * Ipv4 D类地址最小值字符串形式
     */
    String IPV4_D_STR_MIN = "224.0.0.0";
    /**
     * Ipv4 D类地址最小值数值形式
     */
    long IPV4_D_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_D_STR_MIN);

    /**
     * Ipv4 D类地址最大值字符串形式
     */
    String IPV4_D_STR_MAX = "239.255.255.255";
    /**
     * Ipv4 D类地址最大值数值形式
     */
    long IPV4_D_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_D_STR_MAX);

    /**
     * Ipv4 D类地址专用网段(用于广播)最小值字符串形式
     */
    String IPV4_D_DEDICATED_STR_MIN = "224.0.0.0";
    /**
     * Ipv4 D类地址专用网段(用于广播)最小值数值形式
     */
    long IPV4_D_DEDICATED_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_D_DEDICATED_STR_MIN);

    /**
     * Ipv4 D类地址专用网段(用于广播)最大值字符串形式
     */
    String IPV4_D_DEDICATED_STR_MAX = "224.0.0.255";
    /**
     * Ipv4 D类地址专用网段(用于广播)最大值数值形式
     */
    long IPV4_D_DEDICATED_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_D_DEDICATED_STR_MAX);

    /**
     * Ipv4 D类地址公用网段(用于组播)最小值字符串形式
     */
    String IPV4_D_PUBLIC_STR_MIN = "224.0.1.0";
    /**
     * Ipv4 D类地址公用网段(用于组播)最小值数值形式
     */
    long IPV4_D_PUBLIC_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_D_PUBLIC_STR_MIN);

    /**
     * Ipv4 D类地址公用网段(用于组播)最大值字符串形式
     */
    String IPV4_D_PUBLIC_STR_MAX = "238.255.255.255";
    /**
     * Ipv4 D类地址公用网段(用于组播)最大值数值形式
     */
    long IPV4_D_PUBLIC_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_D_PUBLIC_STR_MAX);

    /**
     * Ipv4 D类地址私用网段(用于测试)最小值字符串形式
     */
    String IPV4_D_PRIVATE_STR_MIN = "239.0.0.0";
    /**
     * Ipv4 D类地址私用网段(用于测试)最小值数值形式
     */
    long IPV4_D_PRIVATE_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_D_PRIVATE_STR_MIN);

    /**
     * Ipv4 D类地址私用网段(用于测试)最大值字符串形式
     */
    String IPV4_D_PRIVATE_STR_MAX = "239.255.255.255";
    /**
     * Ipv4 D类地址私用网段(用于测试)最大值数值形式
     */
    long IPV4_D_PRIVATE_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_D_PRIVATE_STR_MAX);
    // endregion

    // region E类地址常量
    // ================================================== E类地址常量 ==================================================
    /**
     * Ipv4 E类地址最小值字符串形式
     */
    String IPV4_E_STR_MIN = "240.0.0.0";
    /**
     * Ipv4 E类地址最小值数值形式
     */
    long IPV4_E_NUM_MIN = Ipv4Util.ipv4ToLong(IPV4_E_STR_MIN);

    /**
     * Ipv4 E类地址最大值字符串形式
     */
    String IPV4_E_STR_MAX = "255.255.255.255";
    /**
     * Ipv4 E类地址最大值数值形式
     */
    long IPV4_E_NUM_MAX = Ipv4Util.ipv4ToLong(IPV4_E_STR_MAX);
    // endregion
}
