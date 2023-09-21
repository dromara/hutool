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
