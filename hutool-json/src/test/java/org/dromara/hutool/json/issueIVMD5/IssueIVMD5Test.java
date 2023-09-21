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

package org.dromara.hutool.json.issueIVMD5;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueIVMD5Test {

	/**
	 * 测试泛型对象中有泛型字段的转换成功与否
	 */
	@Test
	public void toBeanTest() {
		final String jsonStr = ResourceUtil.readUtf8Str("issueIVMD5.json");

		final TypeReference<BaseResult<StudentInfo>> typeReference = new TypeReference<BaseResult<StudentInfo>>() {};
		final BaseResult<StudentInfo> bean = JSONUtil.toBean(jsonStr, JSONConfig.of(), typeReference.getType());

		final StudentInfo data2 = bean.getData2();
		Assertions.assertEquals("B4DDF491FDF34074AE7A819E1341CB6C", data2.getAccountId());
	}

	/**
	 * 测试泛型对象中有包含泛型字段的类型的转换成功与否，比如List&lt;T&gt; list
	 */
	@Test
	public void toBeanTest2() {
		final String jsonStr = ResourceUtil.readUtf8Str("issueIVMD5.json");

		final TypeReference<BaseResult<StudentInfo>> typeReference = new TypeReference<BaseResult<StudentInfo>>() {};
		final BaseResult<StudentInfo> bean = JSONUtil.toBean(jsonStr, JSONConfig.of(), typeReference.getType());

		final List<StudentInfo> data = bean.getData();
		final StudentInfo studentInfo = data.get(0);
		Assertions.assertEquals("B4DDF491FDF34074AE7A819E1341CB6C", studentInfo.getAccountId());
	}
}
