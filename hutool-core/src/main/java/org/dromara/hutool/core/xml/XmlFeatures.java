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

package org.dromara.hutool.core.xml;

/**
 * XXE安全相关参数<br>
 * 见：https://blog.spoock.com/2018/10/23/java-xxe/
 *
 * @author looly
 * @since 6.0.0
 */
public class XmlFeatures {

	/**
	 * 禁用xml中的inline DOCTYPE 声明，即禁用DTD<br>
	 * 不允许将外部实体包含在传入的 XML 文档中，从而防止XML实体注入（XML External Entities 攻击，利用能够在处理时动态构建文档的 XML 功能，注入外部实体）
	 */
	public static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
	/**
	 * 忽略外部DTD
	 */
	public static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	/**
	 * 不包括外部一般实体
	 */
	public static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
	/**
	 * 不包含外部参数实体或外部DTD子集。
	 */
	public static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
}
