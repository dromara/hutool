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
