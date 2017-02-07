#  Netty

## Netty:Bootstrap的handler和childHandler

不管是服务端还是客户端都进行了handler的设置，通过添加hanlder，我们可以监听Channel的各种动作以及状态的改变，包括连接，绑定，接收消息等。

在基类AbstractBootstrap有handler方法，目的是添加一个handler，监听Bootstrap的动作，客户端的Bootstrap中，继承了这一点。

在服务端的ServerBootstrap中增加了一个方法childHandler，它的目的是添加handler，用来监听已经连接的客户端的Channel的动作和状态。

<p style="color:red;">handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行，这是两者的区别。</p>


>> 在代码中我们看到这样的操作

````

    //添加一个Hanlder用来处理各种Channel状态  
    
    pipeline.addLast("handlerIn", new ClientHandler());  
    
    //添加一个Handler用来接收监听IO操作的  
    
    pipeline.addLast("handlerOut", new OutHandler()); 

````
>> pipeline是伴随Channel的存在而存在的，交互信息通过它进行传递，我们可以addLast（或者addFirst）多个handler，第一个参数是名字，无具体要求，如果填写null，系统会自动命名。


## Netty:ChannelInboundHandlerAdapter和ChannelOutboundHandlerAdapter

ChannelInboundHandlerAdapter，看名字中的 IN，就是进入的意思，一般就是事件（event），比如当有数据到来时，
channel被激活时或者不可用时，下面介绍几个最常用的。

**channelActive**

通道激活时触发，当客户端connect成功后，服务端就会接收到这个事件，从而可以把客户端的Channel记录下来，供后面复用

**channelRead**

这个必须用啊，当收到对方发来的数据后，就会触发，参数msg就是发来的信息，可以是基础类型，也可以是序列化的复杂对象。


**channelReadComplete**

channelRead执行后触发

**exceptionCaught**

出错是会触发，做一些错误处理


ChannelOutboundHandlerAdapter，看到了out，表示出去的动作，监听自己的IO操作，比如connect，bind等，在重写这
个Adapter的方法时，记得执行super.xxxx，否则动作无法执行。

**bind**

服务端执行bind时，会进入到这里，我们可以在bind前及bind后做一些操作

**connect**

客户端执行connect连接服务端时进入

## Netty:EventLoopGroup

Group：群组，Loop：循环，Event：事件，这几个东西联在一起，相比大家也大概明白它的用途了。

Netty内部都是通过线程在处理各种数据，EventLoopGroup就是用来管理调度他们的，注册Channel，管理他们的生命周期，下面就来看看EventLoopGroup是怎样工作的。

> 在Netty框架初探中，当我们启动客户端或者服务端时，都要声明一个Group对象

````
    EventLoopGroup group = new NioEventLoopGroup();  

````

> 这里我们就以NioEventLoopGroup来说明。先看一下它的继承关系

````
    NioEventLoopGroup extends MultithreadEventLoopGroup extends MultithreadEventExecutorGroup  

````
> 看看NioEventLoopGroup的构造函数

````
    public NioEventLoopGroup() {  
        this(0);  
    }  
    //他会连续调用内部的构造函数，直到用下面的构造去执行父类的构造  
    //nThreads此时为0，马上就会提到这个参数的用处  
    public NioEventLoopGroup(  
            int nThreads, Executor executor, final SelectorProvider selectorProvider) {  
        super(nThreads, executor, selectorProvider);  
    }
````

> 基类MultithreadEventLoopGroup的构造

````
    protected MultithreadEventLoopGroup(int nThreads, ThreadFactory threadFactory, Object... args) {  
        super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, threadFactory, args);  
    }  
    //nThreads：内部线程数，如果为0，就取默认值，通常我们会设置为处理器个数*2  
    DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(  
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2)); 
````
> 继续调用再上一级的MultithreadEventExecutorGroup的构造

````
    //这里会根据nThreads创建执行者数组  
    private final EventExecutor[] children;  
      
    protected MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {  
        if (nThreads <= 0) {  
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));  
        }  
      
        if (executor == null) {  
            executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());  
        }  
    //这里创建EventExecutor数组对象  
        children = new EventExecutor[nThreads];  
        if (isPowerOfTwo(children.length)) {  
            chooser = new PowerOfTwoEventExecutorChooser();  
        } else {  
            chooser = new GenericEventExecutorChooser();  
        }  
    //此处循环children数组，来创建内部的NioEventLoop对象  
        for (int i = 0; i < nThreads; i ++) {  
            boolean success = false;  
            try {  
                //newChild是abstract方法，运行期会执行具体的实例对象的重载  
                children[i] = newChild(executor, args);  
                success = true;  
            } catch (Exception e) {  
                // TODO: Think about if this is a good exception type  
                throw new IllegalStateException("failed to create a child event loop", e);  
            } finally {  
                //如果没有成功，做关闭处理  
                if (!success) {  
                    for (int j = 0; j < i; j ++) {  
                        children[j].shutdownGracefully();  
                    }  
      
                    for (int j = 0; j < i; j ++) {  
                        EventExecutor e = children[j];  
                        try {  
                            while (!e.isTerminated()) {  
                                e.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);  
                            }  
                        } catch (InterruptedException interrupted) {  
                            // Let the caller handle the interruption.  
                            Thread.currentThread().interrupt();  
                            break;  
                        }  
                    }  
                }  
            }  
        }  
      
        ......  
}  

````

## Netty:Channel

Channel是客户端和服务端得以传输信息的通道，它维护这套接字或者可进行IO操作的组件。
下面我们以客户端及NioSocketChannel为例，看看他是怎么和socket发生联系的。
当客户端初始化时，首先要进行这样的操作

````
Bootstrap b = new Bootstrap();  
......  
b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);  

````

通过channel方法去创建通道，流程如下：
首先执行AbstractBootstrap中的channel方法