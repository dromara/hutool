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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.collection.iter.EnumerationIter;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.JNDIUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class JNDIUtilTest {

	@Test
	@Disabled
	public void getDnsTest() throws NamingException {
		final Attributes attributes = JNDIUtil.getAttributes("dns:paypal.com", "TXT");
		for (final Attribute attribute: new EnumerationIter<>(attributes.getAll())){
			Console.log(attribute.get());
		}
	}
}
