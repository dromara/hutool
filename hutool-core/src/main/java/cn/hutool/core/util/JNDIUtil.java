package cn.hutool.core.util;

import javax.naming.directory.InitialDirContext;
import java.util.Map;

public class JNDIUtil {

	public static InitialDirContext createInitialDirContext(Map<String, String> environment){
//		return new InitialDirContext(Convert.convert(Hashtable.class, environment));
		return null;
	}
}
