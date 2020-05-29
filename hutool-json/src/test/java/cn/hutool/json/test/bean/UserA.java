package cn.hutool.json.test.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserA {
	private String name;
	private String a;
	private Date date;
	private List<Seq> sqs;
}
