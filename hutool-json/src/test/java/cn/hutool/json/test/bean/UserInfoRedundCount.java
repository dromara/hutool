package cn.hutool.json.test.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoRedundCount implements Serializable {
	private static final long serialVersionUID = -8397291070139255181L;

	private String finishedRatio; // 完成率
	private Integer ownershipExamCount; // 自己有多少道题
	private Integer answeredExamCount; // 当前回答了多少道题
}
