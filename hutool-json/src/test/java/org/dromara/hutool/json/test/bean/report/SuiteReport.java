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
