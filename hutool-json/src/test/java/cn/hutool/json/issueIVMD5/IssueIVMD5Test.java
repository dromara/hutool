package cn.hutool.json.issueIVMD5;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.TypeReference;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;

public class IssueIVMD5Test {
	
	@Test
	public void toBeanTest() {
		String jsonStr = ResourceUtil.readUtf8Str("issueIVMD5.json");
		
//		BaseResult<StudentInfo> bean = JSON.parseObject(jsonStr, new TypeReference<BaseResult<StudentInfo>>() {});
		
		BaseResult<StudentInfo> bean = JSONUtil.toBean(jsonStr, new TypeReference<BaseResult<StudentInfo>>() {}.getType(), false);
		List<StudentInfo> data = bean.getData();
		StudentInfo studentInfo = data.get(0);
		Console.log(studentInfo.getClass());
	}
}
