package cn.hutool.json.test2;

import java.util.List;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Test {
	public static void main(String[] args) {
		String readUtf8Str = ResourceUtil.readUtf8Str("json.txt");
		JSONObject json = JSONUtil.parseObj(readUtf8Str);
		SuiteReport bean = json.toBean(SuiteReport.class);
		
		List<CaseReport> caseReports = bean.getCaseReports();
		CaseReport caseReport = caseReports.get(0);
		Console.log("###" + caseReport);
		
		List<StepReport> stepReports = caseReports.get(0).getStepReports();
		
		StepReport stepReport = stepReports.get(0);
		Console.log("###" + stepReport);
	}
}
