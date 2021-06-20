package cn.hutool.json.test.bean;

public class KeyBean{
	private String akey;
	private String bkey;
	
	public String getAkey() {
		return akey;
	}
	public void setAkey(String akey) {
		this.akey = akey;
	}
	public String getBkey() {
		return bkey;
	}
	public void setBkey(String bkey) {
		this.bkey = bkey;
	}
	
	@Override
	public String toString() {
		return "KeyBean [akey=" + akey + ", bkey=" + bkey + "]";
	}
}
