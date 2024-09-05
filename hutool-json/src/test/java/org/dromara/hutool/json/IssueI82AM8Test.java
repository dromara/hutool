/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json;

import lombok.Data;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class IssueI82AM8Test {

	@Test
	void toBeanTest() {
		final String json = ResourceUtil.readUtf8Str("issueI82AM8.json");

		final Map<String, MedicalCenter.MedicalCenterLocalized> bean1 =
			JSONUtil.toBean(json, new TypeReference<Map<String, MedicalCenter.MedicalCenterLocalized>>() {});

		//Console.log(bean1);
		bean1.forEach((k, v) -> Assertions.assertNotNull(v.getTestimonials()));
	}

	// 对象
	@Data
	public static class MedicalCenter {

		private Map<String, MedicalCenterLocalized> medicalCenterLocalized;

		@Data
		public static class MedicalCenterLocalized {

			private List<Testimonial> testimonials;

			@Data
			public static class Testimonial {
				private LocalDateTime createTime;
			}
		}
	}
}
