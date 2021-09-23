package cn.hutool.json.test.bean;

import lombok.Data;

@Data
public class Exam {
	private String id;
	private String examNumber;
	private String isAnswer;
	private Seq[] answerArray;
	private String isRight;
	private String isSubject;
}
