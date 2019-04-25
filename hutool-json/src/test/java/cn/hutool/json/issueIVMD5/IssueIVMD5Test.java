package cn.hutool.json.issueIVMD5;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

public class IssueIVMD5Test {
	
	@Test
	public void toBeanTest() {
		String jsonStr = ResourceUtil.readUtf8Str("issueIVMD5.json");
		
//		BaseResult<StudentInfo> bean = JSON.parseObject(jsonStr, new TypeReference<BaseResult<StudentInfo>>() {});
		
		TypeReference<BaseResult<StudentInfo>> typeReference = new TypeReference<BaseResult<StudentInfo>>() {};
		BaseResult<StudentInfo> bean = JSONUtil.toBean(jsonStr, typeReference.getType(), false);
		
		StudentInfo data2 = bean.getData2();
		Assert.assertEquals("B4DDF491FDF34074AE7A819E1341CB6C", data2.getAccountId());
		
		List<StudentInfo> data = bean.getData();
		StudentInfo studentInfo = data.get(0);
		Console.log(studentInfo.getClass());
	}
}
