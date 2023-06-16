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
