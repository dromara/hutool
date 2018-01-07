package cn.hutool.json.test.bean;

import java.util.Date;
import java.util.List;

public class UserA {
	private String name;
	private String a;
	private Date date;
	private List<Seq> sqs;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Seq> getSqs() {
		return sqs;
	}
	public void setSqs(List<Seq> sqs) {
		this.sqs = sqs;
	}
	@Override
	public String toString() {
		return "UserA [name=" + name + ", a=" + a + ", date=" + date + ", sqs=" + sqs + "]";
	}
}
