package cn.hutool.extra.execl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * 不创建对象消费监听
 * @author simonpdh
 */
public class NoModelExcelConsumerListener<T> extends AnalysisEventListener<Map<Integer,String>> {
    /**
     * 数据分片大小
     */
    private Integer segmentSize;
    /**
     * 数据列表
     */
    private List<Map<String,String>> list;
    /**
     * 消费型接口
     */
    private Consumer<List<Map<String,String>>> consumer;

    /**
     * 动态标题
     */
    private Map<Integer, String> headMap;

    /**
     * 默认数据分片大小
     */
    private static final int DEFAULT_SIZE=10;
    /**
     * 最大的数据分片大小
     */
    private static final int DEFAULT_MAX_SIZE=2000;

    public NoModelExcelConsumerListener(Consumer<List<Map<String,String>>> consumer) {
        this.segmentSize = DEFAULT_SIZE;
        this.consumer = consumer;
        list = new ArrayList<>(segmentSize);
    }

    public NoModelExcelConsumerListener(Integer segmentSize, Consumer<List<Map<String,String>>> consumer) {
        this.segmentSize = checkSize(segmentSize);
        this.consumer = consumer;
        list = new ArrayList<>(segmentSize);
    }

    /**
     * Returns the header as a map.Override the current method to receive header data.
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map headMap, AnalysisContext context) {
        this.headMap=headMap;
    }


    @Override
    public void invoke(Map<Integer,String> data, AnalysisContext analysisContext) {
        if(Objects.isNull(data)||data.isEmpty()){
            return;
        }
        if(Objects.isNull(headMap)||headMap.isEmpty()){
            return;
        }
		//所有属性为空是模板错误或者空表格
		if(ExcelUtil.checkAllFieldIsNULL(data)){
			throw new RuntimeException("Excel内容存在错误");
		}
        //组装数据对象
        Map<String,String> map=new HashMap<>();
        headMap.forEach((k, v) -> {
            map.put(v,data.get(k));
        });
        list.add(map);
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
