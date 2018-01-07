package cn.hutool.core.io.watch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.watch.watchers.WatcherChain;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

/**
 * 路径监听器<br>
 * 监听器可监听目录或文件<br>
 * 如果监听的Path不存在，则递归创建空目录然后监听此空目录<br>
 * 递归监听目录时，并不会监听新创建的目录
 * 
 * @author Looly
 *
 */
public class WatchMonitor extends Thread implements Closeable{
	
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
	/** 递归目录的最大深度，当小于1时不递归下层目录 */
	private int maxDepth;
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
	
	/** WatchKey 和 Path的对应表 */
	private Map<WatchKey, Path> watchKeyPathMap = new HashMap<>();
	
	//------------------------------------------------------ Static method start
	/**
	 * 创建并初始化监听
	 * @param url URL
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URL url, WatchEvent.Kind<?>... events){
		return create(url, 0, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param url URL
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(URL url, int maxDepth, WatchEvent.Kind<?>... events){
		return create(URLUtil.toURI(url), maxDepth, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URI uri, WatchEvent.Kind<?>... events){
		return create(uri, 0, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(URI uri, int maxDepth, WatchEvent.Kind<?>... events){
		return create(Paths.get(uri), maxDepth, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file, WatchEvent.Kind<?>... events){
		return create(file, 0, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file, int maxDepth, WatchEvent.Kind<?>... events){
		return create(file.toPath(), maxDepth, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path, WatchEvent.Kind<?>... events){
		return create(path, 0, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path, int maxDepth, WatchEvent.Kind<?>... events){
		return create(Paths.get(path), maxDepth, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path, WatchEvent.Kind<?>... events){
		return create(path, 0, events);
	}
	
	/**
	 * 创建并初始化监听
	 * @param path 路径
	 * @param events 监听事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path, int maxDepth, WatchEvent.Kind<?>... events){
		return new WatchMonitor(path, maxDepth, events);
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
		this(path, 0, events);
	}
	
	/**
	 * 构造<br>
	 * 例如设置：
	 * <pre>
	 * maxDepth &lt;= 1 表示只监听当前目录
	 * maxDepth = 2 表示监听当前目录以及下层目录
	 * maxDepth = 3 表示监听当前目录以及下层
	 * </pre>
	 * 
	 * @param path 字符串路径
	 * @param maxDepth 递归目录的最大深度，当小于2时不递归下层目录
	 * @param events 监听事件列表
	 */
	public WatchMonitor(Path path, int maxDepth, WatchEvent.Kind<?>... events) {
		this.path = path;
		this.maxDepth = maxDepth;
		this.events = events;
		this.init();
	}
	//------------------------------------------------------ Constructor method end
	
	/**
	 * 初始化<br>
	 * 初始化包括：
	 * <pre>
	 * 1、解析传入的路径，判断其为目录还是文件
	 * 2、创建{@link WatchService} 对象
	 * </pre>
	 * 
	 * @throws WatchException 监听异常，IO异常时抛出此异常
	 */
	public void init() throws WatchException{
		//获取目录或文件路径
		if(false ==Files.exists(this.path, LinkOption.NOFOLLOW_LINKS)) {
			final Path lastPathEle = FileUtil.getLastPathEle(this.path);
			if(null != lastPathEle) {
				final String lastPathEleStr = lastPathEle.toString();
				//带有点表示有扩展名，按照未创建的文件对待。Linux下.d的为目录，排除之
				if(StrUtil.contains(lastPathEleStr, StrUtil.C_DOT) && false ==StrUtil.endWithIgnoreCase(lastPathEleStr, ".d")) {
					this.filePath = this.path;
					this.path = this.filePath.getParent();
				}
			}
			
			//创建不存在的目录或父目录
			try {
				Files.createDirectories(this.path);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}else if(Files.isRegularFile(this.path, LinkOption.NOFOLLOW_LINKS)){
			this.filePath = this.path;
			this.path = this.filePath.getParent();
		}
		
		//初始化监听
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			throw new WatchException(e);
		}
		
		isClosed = false;
	}
	
	/**
	 * 设置监听<br>
	 * 多个监听请使用{@link WatcherChain}
	 * 
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
		registerPath();
//		log.debug("Start watching path: [{}]", this.path);
		
		while (false == isClosed) {
			WatchKey wk;
			try {
				wk = watchService.take();
			} catch (InterruptedException e) {
//				log.warn(e);
				return;
			}
			
			final Path currentPath = watchKeyPathMap.get(wk);
			WatchEvent.Kind<?> kind;
			for (WatchEvent<?> event : wk.pollEvents()) {
				kind = event.kind();
				if(null != this.filePath && false == this.filePath.endsWith(event.context().toString())){
//					log.debug("[{}] is not fit for [{}], pass it.", event.context(), this.filePath.getFileName());
					continue;
				}
				
				if(kind == StandardWatchEventKinds.ENTRY_CREATE){
					watcher.onCreate(event, currentPath);
				}else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
					watcher.onModify(event, currentPath);
				}else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
					watcher.onDelete(event, currentPath);
				}else if(kind == StandardWatchEventKinds.OVERFLOW){
					watcher.onOverflow(event, currentPath);
				}
			}
			wk.reset();
		}
	}
	
	/**
	 * 当监听目录时，监听目录的最大深度<br>
	 * 当设置值为1（或小于1）时，表示不递归监听子目录<br>
	 * 例如设置：
	 * <pre>
	 * maxDepth &lt;= 1 表示只监听当前目录
	 * maxDepth = 2 表示监听当前目录以及下层目录
	 * maxDepth = 3 表示监听当前目录以及下层
	 * </pre>
	 * 
	 * @param maxDepth 最大深度，当设置值为1（或小于1）时，表示不递归监听子目录，监听所有子目录请传{@link Integer#MAX_VALUE}
	 * @return this
	 */
	public WatchMonitor setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
		return this;
	}
	
	/**
	 * 关闭监听
	 */
	@Override
	public void close(){
		isClosed = true;
		IoUtil.close(watchService);
	}
	
	//------------------------------------------------------ private method start
	/**
	 * 注册监听路径
	 */
	private void registerPath() {
		registerPath(this.path, (null != this.filePath) ? 0 : this.maxDepth);
	}
	
	/**
	 * 将指定路径加入到监听中
	 * @param path 路径
	 * @param maxDepth 递归下层目录的最大深度
	 * @return {@link WatchKey}
	 */
	private void registerPath(Path path, int maxDepth) {
		try {
			final WatchKey key = path.register(watchService, ArrayUtil.isEmpty(this.events) ? EVENTS_ALL : this.events);
			watchKeyPathMap.put(key, path);
			if(maxDepth > 1) {
				//遍历所有子目录并加入监听
				Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>(){
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						registerPath(dir, 0);//继续添加目录
						return super.postVisitDirectory(dir, exc);
					}
				});
			}
		} catch (IOException e) {
			if(e instanceof AccessDeniedException) {
				//对于禁止访问的目录，跳过监听
				return;
			}
			throw new WatchException(e);
		}
	}
	//------------------------------------------------------ private method end
}
