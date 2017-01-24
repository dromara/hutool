package com.xiaoleilu.hutool.watch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * 路径监听器<br>
 * 监听器可监听目录或文件
 * 
 * @author Looly
 *
 */
public class WatchMonitor extends Thread implements Closeable{
	private static final Log log = LogFactory.get();
	
	/** 全部事件 */
	public static final Kind<?>[] EVENTS_ALL = {StandardWatchEventKinds.OVERFLOW,StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE};
	
	/** 监听路径，必须为目录 */
	private Path path;
	/** 监听的文件，对于单文件监听不为空 */
	private Path filePath;
	
	/** 监听服务 */
	private WatchService watchService;
	/** 监听器 */
	private Watcher watcher;
	/** 监听事件列表 */
	private Kind<?>[] events;
	
	/** 监听是否已经关闭 */
	private boolean isClosed;
	
	//------------------------------------------------------ Static method start
	/**
	 * 创建并初始化监听
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URI uri, Kind<?>... events){
		return create(Paths.get(uri), events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param url URL
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URL url, Kind<?>... events){
		try {
			return create(Paths.get(url.toURI()), events);
		} catch (URISyntaxException e) {
			throw new WatchException(e);
		}
	}
	
	/**
	 * 创建并初始化监听
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file, Kind<?>... events){
		return new WatchMonitor(file, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path, Kind<?>... events){
		return new WatchMonitor(path, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path, Kind<?>... events){
		return new WatchMonitor(path, events);
	}
	//------------------------------------------------------ Static method end
	
	//------------------------------------------------------ Constructor method start
	/**
	 * 构造
	 * @param file 文件
	 * @param events 监听的事件列表
	 */
	public WatchMonitor(File file, Kind<?>... events) {
		this(file.toPath(), events);
	}
	
	/**
	 * 构造
	 * @param path 字符串路径
	 * @param events 监听的事件列表
	 */
	public WatchMonitor(String path, Kind<?>... events) {
		this(Paths.get(path), events);
	}
	
	/**
	 * 构造
	 * @param path 字符串路径
	 * @param events 监听事件列表
	 */
	public WatchMonitor(Path path, Kind<?>... events) {
		this.path = path;
		this.events = events;
		this.init();
	}
	//------------------------------------------------------ Constructor method end
	
	/**
	 * 初始化
	 * @throws IOException
	 */
	public void init(){
		if(path.toFile().isFile()){
			this.filePath = this.path;
			this.path = this.filePath.getParent();
		}
		
		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, ArrayUtil.isEmpty(this.events) ? EVENTS_ALL : this.events);
		} catch (Exception e) {
			throw new WatchException(e);
		}
		
		isClosed = false;
	}
	
	/**
	 * 设置监听
	 * @param watcher 监听
	 * @return {@link WatchMonitor}
	 */
	public WatchMonitor setWatcher(Watcher watcher){
		this.watcher = watcher;
		return this;
	}
	
	@Override
	public void run() {
		watch();
	}
	
	/**
	 * 开始监听事件，阻塞当前进程
	 * @throws InterruptedException
	 */
	public void watch(){
		watch(this.watcher);
	}
	
	/**
	 * 开始监听事件，阻塞当前进程
	 * @param watcher 监听
	 * @throws InterruptedException
	 */
	public void watch(Watcher watcher){
		if(isClosed){
			throw new WatchException("Watch Monitor is closed !");
		}
		
		log.debug("Start watching path: [{}]", this.path);
		
		while (false == isClosed) {
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
				if(null != filePath && false == this.filePath.endsWith(event.context().toString())){
//					log.debug("[{}] is not fit for [{}], pass it.", event.context(), this.filePath.getFileName());
					continue;
				}
				
				if(kind == StandardWatchEventKinds.ENTRY_CREATE){
					watcher.onCreate(event);
				}else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
					watcher.onModify(event);
				}else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
					watcher.onDelete(event);
				}else if(kind == StandardWatchEventKinds.OVERFLOW){
					watcher.onOverflow(event);
				}
			}
			wk.reset();
		}
	}
	
	/**
	 * 关闭监听
	 */
	@Override
	public void close(){
		isClosed = true;
		IoUtil.close(watchService);
	}
}
