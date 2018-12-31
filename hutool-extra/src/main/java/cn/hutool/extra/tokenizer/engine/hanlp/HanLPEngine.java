package cn.hutool.extra.tokenizer.engine.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Engine;
import cn.hutool.extra.tokenizer.Result;

/**
 * HanLP分词引擎实现<br>
 * 项目地址：https://github.com/hankcs/HanLP
 * 
 * @author looly
 *
 */
public class HanLPEngine implements Engine {

	private Segment seg;
	
	/**
	 * 构造
	 * 
	 */
	public HanLPEngine() {
		this.seg = HanLP.newSegment();
	}

	@Override
	public Result parse(CharSequence text) {
		return new HanLPResult(this.seg.seg(StrUtil.str(text)));
	}

}
