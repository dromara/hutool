package cn.hutool.core.io.unit;


import java.text.DecimalFormat;

/**
 * 文件大小单位转换，方便于各种单位之间的转换
 * @author zrh 455741807@qq.com
 * @date 2022-07-21
 */
public enum FileSizeUnit {
	/**字节*/
	BYTE {
		@Override
		public long toByte(long duration) {
			return duration;
		}

		@Override
		public long toKB(long duration) {
			return duration/(CKB/CBYTE);
		}

		@Override
		public long toMB(long duration) {
			return duration/(CMB/CBYTE);
		}

		@Override
		public long toGB(long duration) {
			return duration/(CGB/CBYTE);
		}

		@Override
		public long toTB(long duration) {
			return duration/(CTB/CBYTE);
		}
	},

	/**KB*/
	KB {
		@Override
		public long toByte(long duration) {
			return x(duration, CKB/CBYTE, MAX/(CKB/CBYTE));
		}

		@Override
		public long toKB(long duration) {
			return duration;
		}

		@Override
		public long toMB(long duration) {
			return duration/(CMB/CKB);
		}

		@Override
		public long toGB(long duration) {
			return duration/(CGB/CKB);
		}

		@Override
		public long toTB(long duration) {
			return duration/(CTB/CKB);
		}
	},

	/**MB*/
	MB {
		@Override
		public long toByte(long duration) {
			return x(duration, CMB/CBYTE, MAX/(CMB/CBYTE));
		}

		@Override
		public long toKB(long duration) {
			return x(duration, CMB/CKB, MAX/(CMB/CKB));
		}

		@Override
		public long toMB(long duration) {
			return duration;
		}

		@Override
		public long toGB(long duration) {
			return duration/(CGB/CMB);
		}

		@Override
		public long toTB(long duration) {
			return duration/(CTB/CMB);
		}
	},

	/**GB*/
	GB {
		@Override
		public long toByte(long duration) {
			return x(duration, CGB/CBYTE, MAX/(CGB/CBYTE));
		}

		@Override
		public long toKB(long duration) {
			return x(duration, CGB/CKB, MAX/(CGB/CKB));
		}

		@Override
		public long toMB(long duration) {
			return x(duration, CGB/CMB, MAX/(CGB/CMB));
		}

		@Override
		public long toGB(long duration) {
			return duration;
		}

		@Override
		public long toTB(long duration) {
			return duration/(CTB/CGB);
		}
	},

	/**TB*/
	TB {
		@Override
		public long toByte(long duration) {
			return x(duration, CTB/CBYTE, MAX/(CTB/CBYTE));
		}

		@Override
		public long toKB(long duration) {
			return x(duration, CTB/CKB, MAX/(CTB/CKB));
		}

		@Override
		public long toMB(long duration) {
			return x(duration, CTB/CMB, MAX/(CTB/CMB));
		}

		@Override
		public long toGB(long duration) {
			return x(duration, CTB/CGB, MAX/(CTB/CTB));
		}

		@Override
		public long toTB(long duration) {
			return duration;
		}
	}
	;

	private static final long CBYTE = 1;
	private static final long CKB = 1024;
	private static final long CMB = (CKB * CKB);
	private static final long CGB = (CKB * CMB);
	private static final long CTB = (CKB * CGB);

	private static final long MAX = Long.MAX_VALUE;

	/**
	 * Scale d by m, checking for overflow.
	 * This has a short name to make above code more readable.
	 */
	private static long x(long d, long m, long over) {
		if (d >  over) { return Long.MAX_VALUE; }
		if (d < -over) { return Long.MIN_VALUE; }
		return d * m;
	}


	public long toByte(long duration) { throw new AbstractMethodError(); }
	public long toKB(long duration) { throw new AbstractMethodError(); }
	public long toMB(long duration) { throw new AbstractMethodError(); }
	public long toGB(long duration) { throw new AbstractMethodError(); }
	public long toTB(long duration) { throw new AbstractMethodError(); }

	/**
	 * 文件大小转换为可读带单位的字符串，如1.2GB、300.0MB等
	 * @param length 文件大小，<code>java.io.File.length()</code>返回值，单位为Bytes
	 * @return
	 */
	public static String readableSizeStr(long length) {
		return readableSizeStr(length, FileSizeUnit.BYTE);
	}

	/**
	 * 文件大小转换为可读带单位的字符串，如1.2GB、300MB等
	 * @param size 文件大小
	 * @param unit 单位
	 * @return
	 */
	public static String readableSizeStr(long size, FileSizeUnit unit) {
		if(size <= 0 || unit == null) {
			return "";
		}
		long byteSize = unit.toByte(size);
		String[] fileSizeArr = getFileSize(byteSize);
		return fileSizeArr[0]+fileSizeArr[1];
	}

	/**
	 * 以合适的单位返回文件大小，返回数组，String[0]为文件大小数字，String[1]为单位
	 * @param length The length, in bytes
	 * @return 返回数组，String[0]为文件大小数字，String[1]为单位
	 */
	private static String[] getFileSize(long length) {
		if(length <= 0) {
			return new String[]{"0","Byte"};
		}

		String[] size = new String[2];
		DecimalFormat df = new DecimalFormat("#.0");
		if (length < CKB) {
			size[0] = df.format(length);
			size[1] = "Byte";
		} else if (length < CMB) {
			size[0] = df.format( length*1.0/ CKB);
			size[1] = "KB";
		} else if (length < CGB) {
			size[0] = df.format(length*1.0/ CMB);
			size[1] = "MB";
		} else if (length < CTB) {
			size[0] = df.format(length*1.0/ CGB);
			size[1] = "GB";
		} else {
			size[0] = df.format(length*1.0/ CTB);
			size[1] = "TB";
		}
		return size;
	}

}
