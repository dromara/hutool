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
 * 万能类型转换器以及各种类型转换的实现类，其中Convert为转换器入口，提供各种toXXX方法和convert方法
 *
 * <p>
 * 转换器是典型的策略模式应用，通过实现{@link org.dromara.hutool.core.convert.Converter} 接口，
 * 自定义转换策略。Hutool提供了常用类型的转换策略。
 * </p>
 *
 * @author looly
 *
 */
package org.dromara.hutool.core.convert;
