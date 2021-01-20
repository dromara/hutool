package cn.hutool.json.test.bean;

public class Seq {
	private String seq;
	
	public Seq() {
	}
	
	public Seq(String seq) {
		this.seq = seq;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "Seq [seq=" + seq + "]";
	}
}
