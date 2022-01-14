package cn.hutool.extra.execl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

/**
 * @author simonpdh
 */
@FunctionalInterface
public interface ExcelWrite {
    /**
     * 写入
     * @param excelWriter
     * @param writeSheet
     */
    void doWrite(ExcelWriter excelWriter, WriteSheet writeSheet);
}
