package cn.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class IssueI82AM8Test {

	@Test
	public void toBeanTest() {
		final String json = ResourceUtil.readUtf8Str("issueI82AM8.json");

		Map<String, MedicalCenter.MedicalCenterLocalized> bean1 =
			JSONUtil.toBean(json, new TypeReference<Map<String, MedicalCenter.MedicalCenterLocalized>>() {
			}, false);

		bean1.forEach((k, v) -> Assert.assertNotNull(v.getTestimonials()));
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
