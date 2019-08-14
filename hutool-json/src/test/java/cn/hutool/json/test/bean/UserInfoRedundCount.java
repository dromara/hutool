package cn.hutool.json.test.bean;

import java.io.Serializable;

public class UserInfoRedundCount implements Serializable {

	private static final long serialVersionUID = -8397291070139255181L;
	private String finishedRatio; // 完成率

	private Integer ownershipExamCount; // 自己有多少道题

	private Integer answeredExamCount; // 当前回答了多少道题

	public Integer getOwnershipExamCount() {
		return ownershipExamCount;
	}

	public void setOwnershipExamCount(Integer ownershipExamCount) {
		this.ownershipExamCount = ownershipExamCount;
	}

	public Integer getAnsweredExamCount() {
		return answeredExamCount;
	}

	public void setAnsweredExamCount(Integer answeredExamCount) {
		this.answeredExamCount = answeredExamCount;
	}

	public String getFinishedRatio() {
		return finishedRatio;
	}

	public void setFinishedRatio(String finishedRatio) {
		this.finishedRatio = finishedRatio;
	}

}
