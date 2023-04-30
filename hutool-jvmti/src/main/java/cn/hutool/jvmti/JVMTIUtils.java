package cn.hutool.jvmti;

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;

/**
 * The type JVMTI utils.
 */
public class JVMTIUtils {

    private static String libName;

    static {
		final OsInfo osInfo = SystemUtil.getOsInfo();
		if (osInfo.isMac()) {
            libName = "libJniLibrary.dylib";
        }
        if (osInfo.isLinux()) {
            libName = "libJniLibrary.so";
        }
        if (osInfo.isWindows()) {
            libName = "libJniLibrary.dll";
        }
    }

    /**
     * detect native library name.
     *
     * @return the native library name
     */
    public static String detectLibName() {
        return libName;
    }
}
