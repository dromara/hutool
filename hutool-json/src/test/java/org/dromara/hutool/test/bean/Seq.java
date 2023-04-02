package org.dromara.hutool.test.bean;

import lombok.Data;

@Data
public class Seq {
	private String seq;

	public Seq() {
	}

	public Seq(final String seq) {
		this.seq = seq;
	}
}
