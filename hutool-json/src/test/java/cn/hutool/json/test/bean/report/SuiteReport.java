package cn.hutool.json.test.bean.report;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试套件报告
 * @author xuwangcheng
 * @version 20181012
 *
 */
public class SuiteReport {
	
	/**
	 * 包含的用例测试报告
	 */
	private List<CaseReport> caseReports = new ArrayList<CaseReport>();
	
	public List<CaseReport> getCaseReports() {
		return caseReports;
	}

	public void setCaseReports(List<CaseReport> caseReports) {
		this.caseReports = caseReports;
	}

	@Override
	public String toString() {
		return "SuiteReport [caseReports=" + caseReports + "]";
	}
}
