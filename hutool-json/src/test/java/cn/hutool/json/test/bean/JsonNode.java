package cn.hutool.json.test.bean;

import java.io.Serializable;

public class JsonNode implements Serializable {
	private static final long serialVersionUID = -2280206942803550272L;

	private Long id;
	private Integer parentId;
	private String name;

	public JsonNode() {
	}

	public JsonNode(Long id, Integer parentId, String name) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "JsonNode{" + "id=" + id + ", parentId=" + parentId + ", name='" + name + '\'' + '}';
	}
}