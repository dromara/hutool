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
 * 测试用例报告
 * @author xuwangcheng
 * @version 20181012
 *
 */
public class CaseReport {

	/**
	 * 包含的测试步骤报告
	 */
	private List<StepReport> stepReports = new ArrayList<>();

	public List<StepReport> getStepReports() {
		return stepReports;
	}

	public void setStepReports(final List<StepReport> stepReports) {
		this.stepReports = stepReports;
	}

	@Override
	public String toString() {
		return "CaseReport [stepReports=" + stepReports + "]";
	}
}
