package cn.hutool.bloomfilter.bitMap;

public interface BitMap {

	public final int MACHINE32 = 32;
	public final int MACHINE64 = 64;

	public void add(long i);

	public boolean contains(long i);

	public void remove(long i);
}