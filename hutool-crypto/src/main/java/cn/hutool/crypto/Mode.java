package cn.hutool.crypto;

/**
 * 模式
 * @author Looly
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher"> Cipher章节</a>
 * @since 3.0.8
 */
public enum Mode{
	/** 无模式 */
	NONE, 
	/** Cipher Block Chaining */
	CBC, 
	/** Cipher Feedback */
	CFB, 
	/** A simplification of OFB */
	CTR,
	/** Cipher Text Stealing */
	CTS,
	/** Electronic Codebook */
	ECB, 
	/** Output Feedback */
	OFB, 
	/** Propagating Cipher Block */
	PCBC;
}
