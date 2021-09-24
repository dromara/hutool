package cn.hutool.json.test.bean;

import lombok.Data;

@Data
public class Seq {
	private String seq;

	public Seq() {
	}

	public Seq(String seq) {
		this.seq = seq;
	}
}
