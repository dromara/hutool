/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.test.bean.report;

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
	private List<CaseReport> caseReports = new ArrayList<>();

	public List<CaseReport> getCaseReports() {
		return caseReports;
	}

	public void setCaseReports(final List<CaseReport> caseReports) {
		this.caseReports = caseReports;
	}

	@Override
	public String toString() {
		return "SuiteReport [caseReports=" + caseReports + "]";
	}
}
