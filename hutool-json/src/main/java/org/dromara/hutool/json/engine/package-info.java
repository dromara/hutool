/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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
 * JSON SPI 实现<br>
 * 通过实现{@link org.dromara.hutool.json.engine.JSONEngine} 以使用不同的JSON库完成：
 * <ul>
 *     <li>JSON序列化，即Java Bean（POJO）对象转为JSON字符串</li>
 *     <li>JSON反序列化，即JSON字符串转为Java Bean（POJO）对象</li>
 * </ul>
 */
package org.dromara.hutool.json.engine;
