# netty 4.x & netty 3.x

Netty创建全部都是实现自AbstractBootstrap。客户端的是Bootstrap，服务端的则是ServerBootstrap。

EventLoopGroup 是在4.x版本中提出来的一个新概念。用于channel的管理。服务端需要两个。和3.x版本一样，一个是boss线程一个是worker线程。

## HandlerInitializer详解(很重要)

###  Handler在Netty中是一个比较重要的概念。有着相当重要的作用。相比于Netty的底层。我们接触更多的应该是他的Handler。

----------------------------------------------------------

## 在4.x版本中的ChannelHandler

 ChannelHandler接口是Handler里面的最高的接口。

 ChannelInboundHandler接口和ChannelOutboundHandler接口，继承ChannelHandler接口。
 
      ChannelInBoundHandler负责数据进入并在ChannelPipeline中按照从上至下的顺序查找调用相应的InBoundHandler。
      ChannelOutBoundHandler负责数据出去并在ChannelPipeline中按照从下至上的顺序查找调用相应的OutBoundHandler。

## 在5.x版本中的改动

  ChannelInboundHandler和ChannelOutboundHandler接口合并到ChannelHandler里面。
  
  ChannelInboundHandlerAdapter，ChannelOutboundHandlerAdapter以及ChannelDuplexHandlerAdapter被取消，其功能被ChannelHandlerAdapter代替。
  
  由于上述的改动，开发者将无法区分InBoundHandler和OutBoundHandler 所以CombinedChannelDuplexHandler 的功能也被ChannelHandlerAdapter代替。
  
      5.x版本中虽然删除了InBoundHandler和OutBoundHandler，但是在设计思想上InBound和OurBound的概念还是存在的。只不过是作者使用了另外一种方式去实现罢了。
      
      查看过4.x版本代码的朋友可能已经了解知道了。消息在管道中都是以ChannelHandlerContext的形势传递的。而InBound和OutBound主要作用是被当做ChannelPipeline管道中标识。用于Handler中相对应的调用处理，通过两个布尔值变量inBound和outBound来区分是进入还是出去。并以此来区分Handler并调用相应的方法，其实没有什么实际用途。于是作者在5.x版本中对此做出了优化。优化方案笔者感觉very nice。
      
      由于删除了InBoundHandler和OutBoundHandler的接口。作者在DefaultChannelHandlerContext中重写了findContextInBound()和findContextOutBound()方法。并且在方法里引入了参数mask。
      
      在类开始处定义静态终态的变量来标记4.x版本中定义的InBound和OutBound中的方法名(可以变相的认为是枚举)。在源代码中的实现是利用mask来获取对应的flag，最终实现使用mask来区分InBoundHandler亦或是OutBoundHandler。
      
      这样的改动，优点显而易见。简化了层次结构，降低了框架的复杂度。同时功能上却没有什么变化。易于使用了解。
      


