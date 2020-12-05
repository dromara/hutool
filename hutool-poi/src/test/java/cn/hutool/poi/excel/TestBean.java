package cn.hutool.poi.excel.test;

import lombok.Data;

import java.util.Date;

@Data
public class TestBean {
	private String name;
	private int age;
	private double score;
	private boolean isPass;
	private Date examDate;
}
