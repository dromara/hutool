package cn.hutool.http.server.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;

import java.io.File;
import java.util.List;

/**
 * 默认的处理器，通过解析用户传入的path，找到网页根目录下对应文件后返回
 *
 * @author looly
 * @since 5.2.6
 */
public class RootAction implements Action {

	public static final String DEFAULT_INDEX_FILE_NAME = "index.html";

	private final String rootDir;
	private final List<String> indexFileNames;

	/**
	 * 构造
	 *
	 * @param rootDir 网页根目录
	 */
	public RootAction(String rootDir) {
		this(rootDir, DEFAULT_INDEX_FILE_NAME);
	}

	/**
	 * 构造
	 *
	 * @param rootDir        网页根目录
	 * @param indexFileNames 主页文件名列表
	 */
	public RootAction(String rootDir, String... indexFileNames) {
		this.rootDir = rootDir;
		this.indexFileNames = CollUtil.toList(indexFileNames);
	}

	@Override
	public void doAction(HttpServerRequest request, HttpServerResponse response) {
		final String path = request.getPath();
		File file = FileUtil.file(rootDir, path);
		if (file.exists()) {
			if (file.isDirectory()) {
				for (String indexFileName : indexFileNames) {
					//默认读取主页
					file = FileUtil.file(file, indexFileName);
					if (file.exists() && file.isFile()) {
						response.write(file);
					}
				}
			} else{
				response.write(file);
			}
		}

		response.send404("404 Not Found !");
	}
}
