package org.dromara.hutool.test.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonNode implements Serializable {
	private static final long serialVersionUID = -2280206942803550272L;

	private Long id;
	private Integer parentId;
	private String name;

	public JsonNode() {
	}

	public JsonNode(final Long id, final Integer parentId, final String name) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}
}
