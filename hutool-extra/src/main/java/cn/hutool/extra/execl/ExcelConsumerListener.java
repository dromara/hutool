package cn.hutool.extra.execl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 消费监听
 * @author simonpdh
 */
public class ExcelConsumerListener<T> extends AnalysisEventListener<T> {
    /**
     * 数据分片大小
     */
    private Integer segmentSize;
    /**
     * 数据列表
     */
    private List<T> list;
    /**
     * 消费型接口
     */
    private Consumer<List<T>> consumer;

    /**
     * 默认数据分片大小
     */
    private static final int DEFAULT_SIZE=10;
    /**
     * 最大的数据分片大小
     */
    private static final int DEFAULT_MAX_SIZE=2000;

    public ExcelConsumerListener(Consumer<List<T>> consumer) {
        this.segmentSize = DEFAULT_SIZE;
        this.consumer = consumer;
        list = new ArrayList<>(segmentSize);
    }

    public ExcelConsumerListener(Integer segmentSize, Consumer<List<T>> consumer) {
        this.segmentSize = checkSize(segmentSize);
        this.consumer = consumer;
        list = new ArrayList<>(segmentSize);
    }

    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        if(Objects.isNull(data)){
            return;
        }
		//所有属性为空是模板错误或者空表格
		if(ExcelUtil.checkAllFieldIsNULL(data)){
			throw new RuntimeException("Excel内容存在错误");
		}
        list.add(data);
        if (list.size() >= segmentSize) {
            consumer.accept(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        consumer.accept(list);
    }

    /**
     * 校验分片大小
     * @param segmentSize
     * @return
     */
    private Integer checkSize(Integer segmentSize){
        if(Objects.isNull(segmentSize)){
            segmentSize=DEFAULT_SIZE;
        }
        if(segmentSize>DEFAULT_MAX_SIZE){
            segmentSize=DEFAULT_MAX_SIZE;
        }
        return segmentSize;
    }
}
