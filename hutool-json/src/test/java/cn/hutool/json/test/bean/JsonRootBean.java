package cn.hutool.json.test.bean;

import java.util.List;

public class JsonRootBean {

	private int statusCode;
	private String message;
	private int skip;
	private int limit;
	private int total;
	private List<Data> data;

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public int getSkip() {
		return skip;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public List<Data> getData() {
		return data;
	}
}
