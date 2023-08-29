package cn.hutool.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;


/**
 * 解决issue 3274
 */
public class Issue3274Test {

	@Test
	public void test1(){
		JSONObject entries = new JSONObject("{\n" +
											"    \n" +
											"    \"age\": 36,\n" +
											"    \"gender\": {\n" +
											"        \"display\": [\n" +
											"            {\n" +
											"                \"lang\": \"zh-CN\",\n" +
											"                \"value\": \"男\"\n" +
											"            },\n" +
											"            {\n" +
											"                \"lang\": \"en-US\",\n" +
											"                \"value\": \"Male\"\n" +
											"            }\n" +
											"        ],\n" +
											"        \"enum_name\": \"male\"\n" +
											"    },\n" +
											"    \"id\": \"123123123\"\n" +
											"}");
		LarkCoreHrPersonal larkCoreHrPersonal = JSONUtil.toBean(entries, LarkCoreHrPersonal.class,true);
		Assert.assertNotNull(larkCoreHrPersonal);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class LarkCoreHrPersonal {
		private String id;
		private String age="";
		private Gender gender;
	}

	@Getter
	enum Gender {
		male("male","Male","男"),
		female("female","Female","女"),
		other("other","Other","其他");
		private JSONArray display;
		private  String enum_name;
		private Gender(String enum_name,String en_Us,String zh_CN){
			this.enum_name=enum_name;
			this.display=new JSONArray("[{\"lang\": \"en-US\",\"value\": \""+en_Us+"\"},{\"lang\": \"zh-CN\",\"value\": \""+zh_CN+"\"}]");
		}
	}
}
