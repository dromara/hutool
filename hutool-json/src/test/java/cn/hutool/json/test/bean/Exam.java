package cn.hutool.json.test.bean;

import java.util.Arrays;

public class Exam {
	private String id;
	private String examNumber;
	private String isAnswer;
	private Seq[] answerArray;
	private String isRight;
	private String isSubject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamNumber() {
		return examNumber;
	}

	public void setExamNumber(String examNumber) {
		this.examNumber = examNumber;
	}

	public String getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(String isAnswer) {
		this.isAnswer = isAnswer;
	}

	public Seq[] getAnswerArray() {
		return answerArray;
	}

	public void setAnswerArray(Seq[] answerArray) {
		this.answerArray = answerArray;
	}

	public String getIsRight() {
		return isRight;
	}

	public void setIsRight(String isRight) {
		this.isRight = isRight;
	}

	public String getIsSubject() {
		return isSubject;
	}

	public void setIsSubject(String isSubject) {
		this.isSubject = isSubject;
	}

	@Override
	public String toString() {
		return "Exam [id=" + id + ", examNumber=" + examNumber + ", isAnswer=" + isAnswer + ", answerArray=" + Arrays.toString(answerArray) + ", isRight=" + isRight + ", isSubject=" + isSubject + "]";
	}
	
	
}
