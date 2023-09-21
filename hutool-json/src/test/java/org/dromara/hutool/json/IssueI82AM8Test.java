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
