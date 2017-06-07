package com.xiaoleilu.hutool.io.watch;

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
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * 路径监听器<br>
 * 监听器可监听目录或文件
 * 
 * @author Looly
 *
 */
public class WatchMonitor extends Thread implements Closeable{
//	private static final Log log = LogFactory.get();
	
	/** 事件丢失 */
	public static final WatchEvent.Kind<?> OVERFLOW = StandardWatchEventKinds.OVERFLOW;
	/** 修改事件 */
	public static final WatchEvent.Kind<?> ENTRY_MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
	/** 创建事件 */
	public static final WatchEvent.Kind<?> ENTRY_CREATE = StandardWatchEventKinds.ENTRY_CREATE;
	/** 删除事件 */
	public static final WatchEvent.Kind<?> ENTRY_DELETE = StandardWatchEventKinds.ENTRY_DELETE;
	/** 全部事件 */
	public static final WatchEvent.Kind<?>[] EVENTS_ALL = {//
			StandardWatchEventKinds.OVERFLOW,        //事件丢失
			StandardWatchEventKinds.ENTRY_MODIFY, //修改
			StandardWatchEventKinds.ENTRY_CREATE,  //创建
			StandardWatchEventKinds.ENTRY_DELETE   //删除
	};
	
	/** 监听路径，必须为目录 */
	private Path path;
	/** 监听的文件，对于单文件监听不为空 */
	private Path filePath;
	
	/** 监听服务 */
	private WatchService watchService;
	/** 监听器 */
	private Watcher watcher;
	/** 监听事件列表 */
	private WatchEvent.Kind<?>[] events;
	
	/** 监听是否已经关闭 */
	private boolean isClosed;
	
	//------------------------------------------------------ Static method start
	/**
	 * 创建并初始化监听
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URI uri, WatchEvent.Kind<?>... events){
		return create(Paths.get(uri), events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param url URL
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URL url, WatchEvent.Kind<?>... events){
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
	public static WatchMonitor create(File file, WatchEvent.Kind<?>... events){
		return new WatchMonitor(file, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path, WatchEvent.Kind<?>... events){
		return new WatchMonitor(path, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path, WatchEvent.Kind<?>... events){
		return new WatchMonitor(path, events);
	}
	
	//--------- createAll
	/**
	 * 创建并初始化监听，监听所有事件
	 * @param uri URI
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(URI uri, Watcher watcher){
		return createAll(Paths.get(uri), watcher);
	}
	
	/**
	 * 创建并初始化监听，监听所有事件
	 * @param url URL
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(URL url, Watcher watcher){
		try {
			return createAll(Paths.get(url.toURI()), watcher);
		} catch (URISyntaxException e) {
			throw new WatchException(e);
		}
	}
	
	/**
	 * 创建并初始化监听，监听所有事件
	 * @param file 被监听文件
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(File file, Watcher watcher){
		return createAll(file.toPath(), watcher);
	}
	
	/**
	 * 创建并初始化监听，监听所有事件
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(String path, Watcher watcher){
		return createAll(Paths.get(path), watcher);
	}
	
	/**
	 * 创建并初始化监听，监听所有事件
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(Path path, Watcher watcher){
		final WatchMonitor watchMonitor = create(path, EVENTS_ALL);
		watchMonitor.setWatcher(watcher);
		return watchMonitor;
	}
	//------------------------------------------------------ Static method end
	
	//------------------------------------------------------ Constructor method start
	/**
	 * 构造
	 * @param file 文件
	 * @param events 监听的事件列表
	 */
	public WatchMonitor(File file, WatchEvent.Kind<?>... events) {
		this(file.toPath(), events);
	}
	
	/**
	 * 构造
	 * @param path 字符串路径
	 * @param events 监听的事件列表
	 */
	public WatchMonitor(String path, WatchEvent.Kind<?>... events) {
		this(Paths.get(path), events);
	}
	
	/**
	 * 构造
	 * @param path 字符串路径
	 * @param events 监听事件列表
	 */
	public WatchMonitor(Path path, WatchEvent.Kind<?>... events) {
		this.path = path;
		this.events = events;
		this.init();
	}
	//------------------------------------------------------ Constructor method end
	
	/**
	 * 初始化
	 * @throws WatchException 监听异常，IO异常时抛出此异常
	 */
	public void init() throws WatchException{
		if(path.toFile().isFile()){
			this.filePath = this.path;
			this.path = this.filePath.getParent();
		}
		
		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, ArrayUtil.isEmpty(this.events) ? EVENTS_ALL : this.events);
		} catch (IOException e) {
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
	 */
	public void watch(){
		watch(this.watcher);
	}
	
	/**
	 * 开始监听事件，阻塞当前进程
	 * @param watcher 监听
	 * @throws WatchException 监听异常，如果监听关闭抛出此异常
	 */
	public void watch(Watcher watcher) throws WatchException{
		if(isClosed){
			throw new WatchException("Watch Monitor is closed !");
		}
		
//		log.debug("Start watching path: [{}]", this.path);
		
		while (false == isClosed) {
			WatchKey wk;
			try {
				wk = watchService.take();
			} catch (InterruptedException e) {
//				log.warn(e);
				return;
			}
			
			WatchEvent.Kind<?> kind;
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
