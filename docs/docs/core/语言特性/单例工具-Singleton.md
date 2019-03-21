## 为什么会有这个类
平常我们使用单例不外乎两种方式：

1. 在对象里加个静态方法getInstance()来获取。此方式可以参考 [【转】线程安全的单例模式](http://my.oschina.net/looly/blog/152865) 这篇博客，可分为饿汉和饱汉模式。
2. 通过Spring这类容器统一管理对象，用的时候去对象池中拿。Spring也可以通过配置决定懒汉或者饿汉模式

说实话我更倾向于第二种，但是Spring更对的的注入，而不是拿，于是我想做Singleton这个类，维护一个单例的池，用这个单例对象的时候直接来拿就可以，这里我用的懒汉模式。我只是想把单例的管理方式换一种思路，我希望管理单例的是一个容器工具，而不是一个大大的框架，这样能大大减少单例使用的复杂性。

## 使用

```
/**
 * 单例样例
 * @author loolly
 *
 */
public class SingletonDemo {
	
	/**
	 * 动物接口
	 * @author loolly
	 *
	 */
	public static interface Animal{
		public void say();
	}
	
	/**
	 * 狗实现
	 * @author loolly
	 *
	 */
	public static class Dog implements Animal{
		@Override
		public void say() {
			System.out.println("汪汪");
		}
	}
	
	/**
	 * 猫实现
	 * @author loolly
	 *
	 */
	public static class Cat implements Animal{
		@Override
		public void say() {
			System.out.println("喵喵");
		}
	}
	
	public static void main(String[] args) {
		Animal dog = Singleton.get(Dog.class);
		Animal cat = Singleton.get(Cat.class);
		
		//单例对象每次取出为同一个对象，除非调用Singleton.destroy()或者remove方法
		System.out.println(dog == Singleton.get(Dog.class));		//True
		System.out.println(cat == Singleton.get(Cat.class));			//True
		
		dog.say();		//汪汪
		cat.say();		//喵喵
	}
}
```
    
## 总结

大家如果有兴趣可以看下这个类，实现非常简单，一个HashMap用于做为单例对象池，通过newInstance()实例化对象（不支持带参数的构造方法），无论取还是创建对象都是线程安全的（在单例对象数量非常庞大且单例对象实例化非常耗时时可能会出现瓶颈），考虑到在get的时候使双重检查锁，但是并不是线程安全的，故直接加了`synchronized`做为修饰符，欢迎在此给出建议。