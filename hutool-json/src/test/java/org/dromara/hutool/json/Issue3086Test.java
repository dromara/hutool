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

package org.dromara.hutool.json;

import lombok.Data;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.json.serialize.JSONObjectSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Issue3086Test {

	@Test
	public void serializeTest() {
		JSONUtil.putSerializer(TestBean.class, new TestBean());

		final List<SimpleGrantedAuthority> strings = ListUtil.of(
			new SimpleGrantedAuthority("ROLE_admin"),
			new SimpleGrantedAuthority("ROLE_normal")
		);
		final TestBean testBean = new TestBean();
		testBean.setAuthorities(strings);

		Assertions.assertEquals("{\"authorities\":[\"ROLE_admin\",\"ROLE_normal\"]}",
			JSONUtil.toJsonStr(testBean));
	}

	static class SimpleGrantedAuthority {
		private final String role;

		public SimpleGrantedAuthority(final String role) {
			this.role = role;
		}

		public String getAuthority() {
			return this.role;
		}

		public String toString() {
			return this.role;
		}
	}

	@Data
	static class TestBean implements JSONObjectSerializer<TestBean> {
		private Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		@Override
		public void serialize(final JSONObject json, final TestBean testBean) {
			final List<String> strings = testBean.getAuthorities()
				.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
			json.set("authorities",strings);
		}
	}
}
