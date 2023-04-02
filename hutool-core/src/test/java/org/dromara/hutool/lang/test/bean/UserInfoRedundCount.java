package org.dromara.hutool.lang.test.bean;

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
