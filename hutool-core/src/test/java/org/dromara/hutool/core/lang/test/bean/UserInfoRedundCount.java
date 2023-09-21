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

package org.dromara.hutool.core.lang.test.bean;

import java.io.Serializable;

public class UserInfoRedundCount implements Serializable {

	private static final long serialVersionUID = -8397291070139255181L;
	private String finishedRatio; // 完成率

	private Integer ownershipExamCount; // 自己有多少道题

	private Integer answeredExamCount; // 当前回答了多少道题

	public Integer getOwnershipExamCount() {
		return ownershipExamCount;
	}

	public void setOwnershipExamCount(final Integer ownershipExamCount) {
		this.ownershipExamCount = ownershipExamCount;
	}

	public Integer getAnsweredExamCount() {
		return answeredExamCount;
	}

	public void setAnsweredExamCount(final Integer answeredExamCount) {
		this.answeredExamCount = answeredExamCount;
	}

	public String getFinishedRatio() {
		return finishedRatio;
	}

	public void setFinishedRatio(final String finishedRatio) {
		this.finishedRatio = finishedRatio;
	}

}
