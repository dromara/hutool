package org.dromara.hutool.jmh.json;

import lombok.Data;

import java.util.Date;
import java.util.List;

public class JSONJmhData {
	public static String jsonStr = "{\n" +
		"  \"name\": \"张三\",\n" +
		"  \"age\": 18,\n" +
		"  \"birthday\": \"2020-01-01\",\n" +
		"  \"booleanValue\": true,\n" +
		"  \"jsonObjectSub\": {\n" +
		"    \"subStr\": \"abc\",\n" +
		"    \"subNumber\": 150343445454,\n" +
		"    \"subBoolean\": true\n" +
		"  },\n" +
		"  \"jsonArraySub\": [\n" +
		"    \"abc\",\n" +
		"    123,\n" +
		"    false\n" +
		"  ]\n" +
		"}";

	@Data
	public static class TestBean{
		private String name;
		private int age;
		private boolean gender;
		private Date createDate;
		private Object nullObj;
		private SubBean jsonObjectSub;
		private List<Object> jsonArraySub;
	}

	@Data
	public static class SubBean{
		private String subStr;
		private Long subNumber;
		private boolean subBoolean;
	}
}
