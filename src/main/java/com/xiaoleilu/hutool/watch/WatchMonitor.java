package com.xiaoleilu.hutool.watch;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.xiaoleilu.hutool.exceptions.WatchException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 路径监听器
 * @author Looly
 *
 */
public class WatchMonitor {
	private static final Log log = LogFactory.get();
	
	/** 监听路径 */
	private Path path;
	/** 监听服务 */
	private WatchService watchService;
	
	//------------------------------------------------------ Static method start
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path){
		return new WatchMonitor(path);
	}
	
	/**
	 * 创建并初始化监听
	 * @param file 文件
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file){
		return new WatchMonitor(file);
	}
	//------------------------------------------------------ Static method end
	
	//------------------------------------------------------ Constructor method start
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path){
		return new WatchMonitor(path);
	}
	
	/**
	 * 构造
	 * @param path Path
	 */
	public WatchMonitor(Path path) {
		this.path = path;
		this.init();
	}
	/**
	 * 构造
	 * @param file 文件
	 */
	public WatchMonitor(File file) {
		this(file.toPath());
	}
	/**
	 * 构造
	 * @param path 字符串路径
	 */
	public WatchMonitor(String path) {
		this(Paths.get(path));
	}
	//------------------------------------------------------ Constructor method end
	
	/**
	 * 初始化
	 * @throws IOException
	 */
	public void init(){
		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, 
					StandardWatchEventKinds.OVERFLOW,
					StandardWatchEventKinds.ENTRY_MODIFY, 
					StandardWatchEventKinds.ENTRY_CREATE, 
					StandardWatchEventKinds.ENTRY_DELETE
					);
		} catch (Exception e) {
			throw new WatchException(e);
		}
	}
	
	/**
	 * 开始监听事件
	 * @param listener 监听
	 * @throws InterruptedException
	 */
	public void start(WatchListener listener){
		log.debug("Start watching path: [{}]", this.path);
		while (true) {
			WatchKey wk;
			try {
				wk = watchService.take();
			} catch (InterruptedException e) {
				log.warn(e);
				return;
			}
			
			Kind<?> kind;
			for (WatchEvent<?> event : wk.pollEvents()) {
				kind = event.kind();
				if(kind == StandardWatchEventKinds.ENTRY_CREATE){
					listener.onCreate(event);
				}else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
					listener.onModify(event);
				}else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
					listener.onDelete(event);
				}else if(kind == StandardWatchEventKinds.OVERFLOW){
					
				}
			}
			wk.reset();
		}
	}
	
	/**
	 * 关闭监听
	 */
	public void close(){
		IoUtil.close(watchService);
	}
}
