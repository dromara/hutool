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
 * 第三方模板引擎封装，提供统一的接口用于适配第三方模板引擎，提供：
 * <ul>
 *     <li>TemplateEngine：模板引擎接口，用于不同引擎的实现。</li>
 *     <li>Template：      模板接口，用于不同引擎模板对象包装。</li>
 *     <li>TemplateConfig：模板配置，用于提供公共配置项。</li>
 * </ul>
 * <pre>
 *                            createEngine                  getTemplate            render
 *     TemplateEngineFactory       =》      TemplateEngine        =》     Template    =》   内容
 * </pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.extra.template;
