package cn.hutool.core.io.file;

/**
 * 文件读写模式，常用于RandomAccessFile
 *
 * @author looly
 * @since 4.5.2
 */
public enum FileMode {
	/** 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。 */
	r,
	/** 打开以便读取和写入。 */
	rw,
	/** 打开以便读取和写入。相对于 "rw"，"rws" 还要求对“文件的内容”或“元数据”的每个更新都同步写入到基础存储设备。 */
	rws,
	/** 打开以便读取和写入，相对于 "rw"，"rwd" 还要求对“文件的内容”的每个更新都同步写入到基础存储设备。 */
	rwd

}
